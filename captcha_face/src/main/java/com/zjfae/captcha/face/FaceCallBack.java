package com.zjfae.captcha.face;

import com.webank.facelight.ui.FaceVerifyStatus;

public interface FaceCallBack {

    void openCloudFaceService(final FaceVerifyStatus.Mode mode);

    void getFaceId(String sign);

    void onError(int code,String msg);
}
