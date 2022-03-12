package com.hundsun.zjfae.common.base;


import com.hundsun.zjfae.BuildConfig;
import com.hundsun.zjfae.common.http.api.ApiRetrofit;
import com.hundsun.zjfae.common.http.api.ApiServer;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.utils.CCLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
/**
 * 作者： ch
 * 时间： 2016/12/27.13:56
 * 描述：
 * 来源：
 */
public class BasePresenter<V extends BaseView> {

    private CompositeDisposable compositeDisposable;

    public V baseView;

    public ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    public BasePresenter(V baseView) {

        this.baseView = baseView;
    }


    /**
     * 解除绑定
     */
    public void detachView() {
        removeDisposable();
    }

    public void onDestroy() {
        removeDisposable();
        baseView = null;
    }

    /**
     * 返回 view
     *
     * @return
     */
    public V getBaseView() {
        return baseView;
    }

    public static final String APPVERSION = BuildConfig.VERSION_NAME;

    //142开发环境 ---> https://medev-141.zjfae.com

    //104测试环境 ---> https://testapp.zjfae.com

    //108环境    ---> http://test2.app.zjfae.com

    //并行环境   ---> http://uat.app.zjfae.com:29999

    //8443环境  ---> https://me8443.zjfae.com

    //灾备环境  ---> https://me.zjfae.cn

    //生产环境  ---> https://me.zjfae.com

    public static final String BASE_HOSTS = BuildConfig.hosts;

    public static final String BASE_URL = BASE_HOSTS + "/ife/";
    /**
     * DOMAIN
     */
    public static final String DOMAIN = "zjfae.com";

    //区块链链接
    public static final String BASE_CHAIN_URL = BASE_HOSTS + "/appblock/unsigned.html";

    public static final String BASE_CHAIN_CACHE_URL = BASE_HOSTS + "/appblock/cache.html";

    public static final String APP_BLOCK_URL = BASE_HOSTS + "/appblock/deblocking.html";


    /**
     * app接口版本控制
     **/
    public static final String version = "v3";
    public static final String twoVersion = "v2";
    public static final String FOUR_VERSION = "v4";
    public static final String FIVE_VERSION = "v5";
    public static final String DEFAULT_KEY = "GR";

    public static final String VICOAZJ = "VICOAZJ000000P00";
    public static final String VUPGAZJ = "VUPGAZJ000000J00";
    public static final String VREGMZJ = "VREGMZJ000000P00";

    public static final String VIMGMZJ = "VIMGMZJ000000P00";

    public static final String VAFTAZJ = "VAFTAZJ000000P00";

    public static final String VADSAZJ = "VADSAZJ000000P00";

    public static final String VCTLAZJ = "VCTLAZJ000000P00";

    public static final String VIFEMZJ = "VIFEMZJ000000P00";

    public static final String VMCOAZJ = "VMCOAZJ000000P00";

    public static final String VMOOMZJ = "VMOOMZJ000000P00";

    public static final String VGBPAZJ = "VGBPAZJ000000P00";

    public static final String VMERAZJ = "VMERAZJ000000P00";

    
    public static final String VNOTAZJ = "VNOTAZJ000000P00";

    public static final String VURLAZJ = "VURLAZJ000000P00";

    public static final String VMOIMZJ = "VMOIMZJ000000P00";
    public static final String VBLGAZJ = "VBLGAZJ000000P00";

    public static final String AZJ = "azj"; // 前置
    public static final String MZJ = "mzj"; // 核心业务
    public static final String PBAFT = "pbaft.do";  // 前置
    public static final String PBIFE = "pbife.do";  // 核心

    public static final String PBICO = "pbico.do";
    public static final String PBUPG = "pbupg.do";

    public static final String PBADS = "pbads.do";
    public static final String PBCTL = "pbctl.do";
    public static final String PBCTV = "pbctv.do";

    public static final String PBIMG = "pbimg.do";
    public static final String PBMCO = "pbmco.do";
    public static final String PBMOO = "pbmoo.do";
    public static final String PBGBP = "pbgbp.do";
    public static final String PBMER = "pbmer.do";
    public static final String PBNOT = "pbnot.do";
    public static final String PBURL = "pburl.do";

    public static final String PBMOI = "pbmoi.do";
    public static final String PBBLG = "pbblg.do";


    //登录图形验证码
    public static final String AccountImageCode = BASE_URL + "mzj/pbimg.do?fh=VIMGMZJ000000J00&type=4&p=and";


    //资产证明图片上传
    public static String UpLoadPicImage = BASE_URL + "mzj/pbimg.do?fh=VIMGMZJ000000J00&scene=uploadAttachment&p=and";
    //身份证识别
    public static String ID_CARD_IMAGE_URL = BASE_URL + "mzj/pbimg.do?fh=VIMGMZJ000000J00&scene=certificate&p=and";
    //用户头像文件上传
    public static String UpLoadPicImage_UserInfo = BASE_URL + "mzj/pbimg.do?fh=VIMGMZJ000000J00&scene=uploadExec";
    //face++上送
    public static String FACE_URL = BASE_URL + "mzj/pbimg.do?fh=VIMGMZJ000000J00&scene=verify&p=and";
    /**
     * 账户等级
     */
    public static String accountLevelUrl = "https://www.zjfae.com/api/club.php";


    public void addDisposable(Observable<?> observable, DisposableObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }

    public void addDisposable(String url, DisposableObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(apiServer.request(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }


    public void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    private static final Map<String, String> requestMap = new HashMap<>(8);

    public static Map<String, String> getRequestMap() {
        if (!requestMap.isEmpty()) {
            requestMap.clear();
        }
        return requestMap;
    }


    public static RequestBody getBody(byte[] bt) {
        return RequestBody.create(MediaType.parse("application/octet-stream"), bt);
    }


    public Observable getUserInfo() {
        Map map = getRequestMap();
        map.put("version", version);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.USERBASEINFO, map);
        UserDetailInfo.REQ_PBIFE_userbaseinfo_getUserDetailInfo.Builder user = UserDetailInfo.REQ_PBIFE_userbaseinfo_getUserDetailInfo.newBuilder();
        user.setVersion("1.0.1");
        return apiServer.getUserInfo(url, getBody(user.build().toByteArray()));
    }

    public static String parseUrl(String zj, String pb, Map<String, String> map) {
        if (zj == null || zj.equals("")) {
            return "zj不能为空";
        }
        if (pb == null || pb.equals("")) {
            return "pb不能为空";
        }

        StringBuffer buffer = new StringBuffer(BASE_URL);
        buffer.append(zj).append("/").append(pb).append("?");
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            buffer.append(entry.getKey()).append("=");
            buffer.append(entry.getValue()).append("&");
        }
        String url = buffer.deleteCharAt(buffer.length() - 1).toString();
        CCLog.e("url", url);
        return url;
    }


    public static String parseUrl(String zj, String pb) {
        if (zj == null || zj.equals("")) {
            return "zj不能为空";
        }
        if (pb == null || pb.equals("")) {
            return "pb不能为空";
        }

        StringBuffer buffer = new StringBuffer(BASE_URL);
        buffer.append(zj).append("/").append(pb).append("?");
        String url = buffer.deleteCharAt(buffer.length() - 1).toString();
        CCLog.e("url", url);
        return url;
    }


    public static String parseUrl(String zj, String pb, String fh, Map<String, String> map) {
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


    public static String parseUrl(String zj, String pb, String fh) {
        if (zj == null || zj.equals("")) {
            return "zj不能为空";
        }
        if (pb == null || pb.equals("")) {
            return "pb不能为空";
        }
        StringBuffer buffer = new StringBuffer(BASE_URL);
        buffer.append(zj).append("/").append(pb).append("?");
        buffer.append("fh").append("=").append(fh).append("&");
        String url = buffer.deleteCharAt(buffer.length() - 1).toString();
        return url;
    }


    public String parseUrl(String zj, String pb, String fh, String pbname) {
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


    public static String loginStatusParseUrl(String zj, String pb, String fh, String pbname, Map<String, String> map) {
        if (zj == null || zj.equals("")) {
            return "zj不能为空";
        }
        if (pb == null || pb.equals("")) {
            return "pb不能为空";
        }
        StringBuffer buffer = new StringBuffer(BASE_URL);
        buffer.append(zj).append("/").append(pb).append("?");
        buffer.append("fh").append("=").append(fh).append("&");
        buffer.append(pbname).append("&");
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            buffer.append(entry.getKey()).append("=");
            buffer.append(entry.getValue()).append("&");
        }
        String url = buffer.deleteCharAt(buffer.length() - 1).toString();
        return url;
    }


    public String parseBody(Map body) {
        JSONObject object = new JSONObject(body);
        return object.toString();
    }
}
