package com.hundsun.zjfae.activity.home.presenter;

import com.hundsun.zjfae.activity.home.bean.ImageUploadBean;
import com.hundsun.zjfae.activity.home.bean.ImageUploadStateBean;
import com.hundsun.zjfae.activity.home.view.ImageUploadView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import onight.zjfae.afront.gens.HighNetWorthUpload;

public class ImageUploadPresenter extends BasePresenter<ImageUploadView> {



    public ImageUploadPresenter(ImageUploadView baseView) {
        super(baseView);
    }



    /**
     * highNetWorth = 合格投资者材料上传
     * unbindCard = 解绑
     * changeCard = 换卡
     * account = 上传头像
     * */
    public void highNetWorth(File file, final int index){

        if (file == null && !file.exists()){
            baseView.showError("上传文件不存在，请联系管理员");
        }
        else {
            CCLog.e(file);
            CCLog.e(index);
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("businessType", "highNetWorth");
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("file","Image"+index+".png",requestFile);

            addDisposable(apiServer.upLoadPicImage(UpLoadPicImage, builder.build()), new BaseObserver<ImageUploadBean>() {
                @Override
                public void onSuccess(ImageUploadBean uploadBean) {

                    if (ConstantCode.RETURN_CODE.equals(uploadBean.getReturnCode())) {
                        baseView.requestImageUpload(uploadBean,index);
                    }
                    else {
                        baseView.showError(uploadBean.getReturnMsg());
                    }

                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    baseView.showError("网络不太顺畅哦~");
                }
            });
        }


    }




    public void highNetWorthUpload(List<ImageUploadStateBean> stateBeanList){
        CCLog.e("入库",stateBeanList);

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.HighNetWorthUpload,getRequestMap());
        HighNetWorthUpload.REQ_PBIFE_bankcardmanage_highNetWorthUpload.Builder builder
                =HighNetWorthUpload.REQ_PBIFE_bankcardmanage_highNetWorthUpload.newBuilder();
        builder.setApplyChannel("12");
        builder.setBusinessType("highNetWorth");

        for (int i = 0; i < stateBeanList.size(); i++) {
            HighNetWorthUpload.REQ_PBIFE_bankcardmanage_highNetWorthUpload.NameAndPath.Builder namePath = HighNetWorthUpload.REQ_PBIFE_bankcardmanage_highNetWorthUpload.NameAndPath.newBuilder();
            namePath.setFileNamesp(stateBeanList.get(i).getFileName());
            namePath.setFilePath(stateBeanList.get(i).getFilePath());
            namePath.setModel(stateBeanList.get(i).getModel());
            builder.addNameAndPathList(namePath.build());
        }
        addDisposable(apiServer.highNetWorthUpload(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<HighNetWorthUpload.Ret_PBIFE_bankcardmanage_highNetWorthUpload>(baseView) {
            @Override
            public void onSuccess(HighNetWorthUpload.Ret_PBIFE_bankcardmanage_highNetWorthUpload ret_pbife_bankcardmanage_highNetWorthUpload) {

                baseView.requestNetWorthUpload(ret_pbife_bankcardmanage_highNetWorthUpload.getReturnCode(),ret_pbife_bankcardmanage_highNetWorthUpload.getReturnMsg());
            }

        });

    }


}
