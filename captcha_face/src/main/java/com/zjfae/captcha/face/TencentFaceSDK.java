package com.zjfae.captcha.face;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.webank.facelight.contants.WbCloudFaceContant;
import com.webank.facelight.contants.WbFaceError;
import com.webank.facelight.contants.WbFaceVerifyResult;
import com.webank.facelight.listerners.WbCloudFaceVeirfyLoginListner;
import com.webank.facelight.listerners.WbCloudFaceVeirfyResultListener;
import com.webank.facelight.tools.WbCloudFaceVerifySdk;
import com.webank.facelight.ui.FaceVerifyStatus;

/**
 * @ProjectName:
 * @Package:        com.zjfae.captcha
 * @ClassName:      TencentFaceSDK
 * @Description:     腾讯人脸识别
 * @Author:         moran
 * @CreateDate:     2019/7/15 16:00
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/15 16:00
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class TencentFaceSDK {
    private static final String TAG = TencentFaceSDK.class.getSimpleName();
    private static TencentFaceSDK tencentFace = null;
    private TencentFaceCallBack tencentFaceCallBack = null;
    private Context mContext;


    public static TencentFaceSDK getInstance() {
        if (tencentFace == null){
            tencentFace = new TencentFaceSDK();
        }
        return tencentFace;
    }


    public TencentFaceSDK init(Context context){

        this.mContext = context;

        return this;

    }


    private TencentFaceSDK(){

    }


    /**
     * @param faceId 人脸识别ID
     * @param orderNo 其他备注
     * @param appId appID
     * @param nonce 32位随机字符串
     * @param userId 用户id
     * @param sign 合作方后台服务器通过 ticket 计算出来的签名信息
     * @param keyLicence 腾讯云线下对接分配的 Licence
     * */

    public void execute(String faceId, final String orderNo, String appId, String nonce, String userId, String sign, String keyLicence, TencentFaceCallBack callBack){

        Log.i("faceId",faceId);
        Log.i("order",orderNo);
        Log.i("appId",appId);
        Log.i("nonce",nonce);
        Log.i("userId",userId);
        Log.i("sign",sign);
        Log.i("keyLicence",keyLicence);
        this.tencentFaceCallBack = callBack;
        Bundle data = new Bundle();
        WbCloudFaceVerifySdk.InputData inputData = new WbCloudFaceVerifySdk.InputData(
                faceId,
                orderNo,
                appId,
                "1.0.0",
                nonce,
                userId,
                sign,
                FaceVerifyStatus.Mode.REFLECTION,
                keyLicence);

        data.putSerializable(WbCloudFaceContant.INPUT_DATA, inputData);
        //以下设置如果选择默认项，则无须设置传输
        //是否展示刷脸成功页面，默认展示
        data.putBoolean(WbCloudFaceContant.SHOW_SUCCESS_PAGE, false);
        //是否展示刷脸失败页面，默认展示
        data.putBoolean(WbCloudFaceContant.SHOW_FAIL_PAGE, false);
        //颜色设置
        data.putString(WbCloudFaceContant.COLOR_MODE, WbCloudFaceContant.BLACK);
        //是否录制视频存证
        //默认录制
        data.putBoolean(WbCloudFaceContant.VIDEO_UPLOAD, true);
        //是否对录制视频进行检查,默认不检查
        data.putBoolean(WbCloudFaceContant.VIDEO_CHECK, false);
        //设置选择的比对类型  默认为公安网纹图片对比
        //公安网纹图片比对 WbCloudFaceVerifySdk.ID_CRAD
        //自带比对源比对  WbCloudFaceVerifySdk.SRC_IMG
        //仅活体检测  WbCloudFaceVerifySdk.NONE
        //默认公安网纹图片比对
        data.putString(WbCloudFaceContant.COMPARE_TYPE, WbCloudFaceContant.ID_CARD);
        WbCloudFaceVerifySdk.getInstance().initSdk(mContext, data, new WbCloudFaceVeirfyLoginListner() {
            @Override
            public void onLoginSuccess() {

                WbCloudFaceVerifySdk.getInstance().startWbFaceVeirifySdk(mContext, new WbCloudFaceVeirfyResultListener() {
                    @Override
                    public void onFinish(WbFaceVerifyResult result) {
                        tencentFaceCallBack.onComplete();
                        if (result != null) {
                            if (result.isSuccess()) {
                                TencentFaceStatus status = new TencentFaceStatus();
                                status.setSuccess(result.isSuccess());
                                status.setSign(result.getSign());
                                status.setLiveRate(result.getLiveRate());
                                status.setSimilarity(result.getSimilarity());
                                status.setUserImageString(result.getUserImageString());
                                tencentFaceCallBack.onSuccess(status);
                            }
                            else {
                                WbFaceError error = result.getError();
                                if (error != null) {
                                    TencentFaceError faceError = new TencentFaceError();
                                    faceError.setDomain(error.getDomain());
                                    faceError.setCode(error.getCode());
                                    faceError.setDesc("人脸识别失败，请核实本人账号信息~");
                                    faceError.setReason(error.getReason());
                                    if (!error.getCode().equals("41000")){
                                        tencentFaceCallBack.onError(faceError);
                                    }

                                }
                                else {
                                    tencentFaceCallBack.onError();
                                }
                            }

                        }
                        else {
                            tencentFaceCallBack.onError();
                        }

                    }
                });
            }

            @Override
            public void onLoginFailed(WbFaceError error) {
                Log.i(TAG, "onLoginFailed!");
                tencentFaceCallBack.onComplete();
                if (error != null){
                    TencentFaceError faceError = new TencentFaceError();
                    faceError.setDomain(error.getDomain());
                    faceError.setCode(error.getCode());
                    faceError.setDesc("人脸识别初始化失败，请重试");
                    faceError.setReason(error.getReason());
                    tencentFaceCallBack.onError(faceError);
                }
                else {
                    tencentFaceCallBack.onError();
                }
            }
        });

    }
}
