package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.bean.FaceResultBean;
import com.hundsun.zjfae.activity.mine.view.FaceLivenessView;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.utils.CCLog;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FaceLivenessPresenter extends BasePresenter<FaceLivenessView> {

    public FaceLivenessPresenter(FaceLivenessView baseView) {
        super(baseView);
    }

    public void faceType(String delta,byte [] imageBaseData,String idcard_name,String idcard_number,String verifyscene){
        CCLog.e("idcard_name",idcard_name+"=======");
        CCLog.e("idcard_number",idcard_number+"=======");
        CCLog.e("verifyscene",verifyscene+"=======");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("idcard_name", idcard_name)
                .addFormDataPart("idcard_number", idcard_number)
                .addFormDataPart("verifyscene", verifyscene)
                .addFormDataPart("delta", delta)
                .addFormDataPart("image_best","image_best" + ".png",RequestBody.create(MediaType.parse("image/*"),imageBaseData));

        addDisposable(apiServer.faceLiveness(FACE_URL, builder.build().parts()), new BaseObserver<FaceResultBean>(baseView) {
            @Override
            public void onSuccess( FaceResultBean face) {
                baseView.faceResultCallBack(face);

            }
        });

    }
}
