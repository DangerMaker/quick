package com.hundsun.zjfae.common.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hundsun.zjfae.UpLoadCrashBean;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ApiRetrofit;
import com.hundsun.zjfae.common.http.api.ApiServer;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.CrashUtils;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.useroperation.UserOperation;

import java.util.Iterator;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import onight.zjfae.afront.AllAzjProto;

import static com.hundsun.zjfae.common.base.BasePresenter.AZJ;
import static com.hundsun.zjfae.common.base.BasePresenter.BASE_URL;
import static com.hundsun.zjfae.common.base.BasePresenter.PBBLG;
import static com.hundsun.zjfae.common.base.BasePresenter.VBLGAZJ;
import static com.hundsun.zjfae.common.base.BasePresenter.getBody;
import static com.hundsun.zjfae.common.base.BasePresenter.getRequestMap;

/**
 * @ProjectName:
 * @Package:        com.hundsun.zjfae.common.service
 * @ClassName:      UpLoadFileJobIntentService
 * @Description:     上传本地日志服务
 * @Author:         moran
 * @CreateDate:     2019/7/12 16:03
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/12 16:03
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class UpLoadFileJobIntentService extends JobIntentService {

    private static final int JOB_ID = 1;
    public ApiServer apiServer;

    public static void enqueueWork(Context context){

        enqueueWork(context,UpLoadFileJobIntentService.class,JOB_ID,new Intent());
    }

    public static void enqueueWork(Context context,Intent intent){
        enqueueWork(context,UpLoadFileJobIntentService.class,JOB_ID,intent);
    }



    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        apiServer = ApiRetrofit.getInstance().getApiService();

        //上送操作日志
        upLoadErorLog();
        //上送崩溃日志
        upLoadCrashFile();
    }




    /**
     * 上传奔溃日志文件
     */
    public void upLoadCrashFile() {
        //构建要上传的奔溃信息
        String crashContent = CrashUtils.getInstance().readTxtFile();
        Log.e("奔溃日志",crashContent+"--->1111");
        if (StringUtils.isNotBlank(crashContent)) {
            Log.e("TAG","奔溃日志不为空");
            crashContent = "AndroidNativeCrash;" + crashContent;
            UpLoadCrashBean UpLoadCrashBean = new UpLoadCrashBean();
            UpLoadCrashBean.setContents(crashContent);
            UpLoadCrashBean.setUser_id(UserInfoSharePre.getAccount());
            UpLoadCrashBean.setClient_os(android.os.Build.VERSION.RELEASE);
            UpLoadCrashBean.setApp_vers(BasePresenter.APPVERSION);
            Gson gson = new Gson();
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(UpLoadCrashBean));
            Log.e("TAG","奔溃日志上送信息----" + gson.toJson(UpLoadCrashBean));
            ApiServer apiServer = ApiRetrofit.getInstance().getApiService();
            String url = BASE_URL + "azj/pblog.do?fh=VREGMZJ000000J00&p=android&startUpLog=true";
            apiServer.upLoadFile(url, body).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribeWith(new BaseObserver<JsonObject>() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                   // UserSetting.setCrachUpLoadTime();
                    CrashUtils.getInstance().deleteCrashFile();
                }
            });

        }

    }

    //日志批量上传
    public void upLoadErorLog() {
        String userOperationContent = UserOperation.getInstance().readTxtFile();
        Log.e("批量上送日志",userOperationContent+"-->1111232");
        if (userOperationContent != null && !userOperationContent.equals("")){
            AllAzjProto.PEABatchLogs.Builder builder = AllAzjProto.PEABatchLogs.newBuilder();
            builder.setUserId(UserInfoSharePre.getAccount());
            builder.setClientOs(android.os.Build.VERSION.RELEASE);
            builder.setAppVers(BasePresenter.APPVERSION);

            AllAzjProto.PEABatchLogsSub.Builder logsSub = AllAzjProto.PEABatchLogsSub.newBuilder();
            logsSub.setPbname("UserOperation");
            logsSub.setContents(userOperationContent);
            CCLog.e("用户登录日志" + UserOperation.getInstance().readTxtFile());
            builder.addBatchLogs(logsSub);
            String url = parseUrl(AZJ, PBBLG, VBLGAZJ, getRequestMap());
            apiServer.upLoadUserOperation(url,getBody(builder.build().toByteArray()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribeWith(new ProtoBufObserver<AllAzjProto.PEARetBatchAppLogs>() {
                        @Override
                        public void onSuccess(AllAzjProto.PEARetBatchAppLogs peaRetBatchAppLogs) {
                            if (peaRetBatchAppLogs.getReturnCode().equals("0000")) {
                                //UserSetting.setCrachUpLoadTime();
                                UserOperation.getInstance().deleteCreateFile();
                            }
                        }
                    });
        }


    }




    public  String parseUrl(String zj, String pb, String fh, Map<String, String> map) {
        if (zj == null || zj.equals("")) {
            return "zj不能为空";
        }
        if (pb == null || pb.equals("")) {
            return "pb不能为空";
        }
        StringBuffer buffer = new StringBuffer(BASE_URL);
        buffer.append(zj).append("/").append(pb).append("?");
        buffer.append("fh").append("=").append(fh).append("&");
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            buffer.append(entry.getKey()).append("=");
            buffer.append(entry.getValue()).append("&");
        }
        String url = buffer.deleteCharAt(buffer.length() - 1).toString();
        return url;
    }

    public String parseUrl(String zj, String pb, String fh, String pbname, Map<String, String> map) {
        if (zj == null || zj.equals("")) {
            return "zj不能为空";
        }
        if (pb == null || pb.equals("")) {
            return "pb不能为空";
        }
        StringBuffer buffer = new StringBuffer(BASE_URL);
        buffer.append(zj).append("/").append(pb).append("?");
        buffer.append("fh").append("=").append(fh).append("&");
        buffer.append("pbname").append("=").append(pbname).append("&");
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            buffer.append(entry.getKey()).append("=");
            buffer.append(entry.getValue()).append("&");
        }
        String url = buffer.deleteCharAt(buffer.length() - 1).toString();
        return url;
    }
}
