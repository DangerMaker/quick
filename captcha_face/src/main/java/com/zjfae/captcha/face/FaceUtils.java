package com.zjfae.captcha.face;


import android.text.TextUtils;

import com.webank.mbank.wehttp.WeLog;
import com.webank.mbank.wehttp.WeOkHttp;
import com.webank.mbank.wehttp.WeReq;

import java.io.IOException;
import java.io.Serializable;

/**
 * @ProjectName:
 * @Package:        com.zjfae.captcha.face
 * @ClassName:      FaceUtils
 * @Description:     java类作用描述
 * @Author:         moran
 * @CreateDate:     2019/7/15 16:26
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/15 16:26
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class FaceUtils {

    private AppHandler handler;

    private WeOkHttp myOkHttp = new WeOkHttp();
    public FaceUtils(FaceCallBack faceCallBack){
        initHttp();
        handler = new AppHandler(faceCallBack);
    }



    private void initHttp(){
        myOkHttp.config()
                //配置超时,单位:s
                .timeout(20, 20, 20)
                //添加PIN
                .log(WeLog.Level.BODY);
    }


    public void execute(final String mode, final String appId, String userId, String nonce) {
        String url = getUrl(appId,userId,nonce);
        requestExec(url, new WeReq.WeCallback<SignResponse>() {
            @Override
            public void onStart(WeReq weReq) {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailed(WeReq weReq, int i, int i1, String s, IOException e) {
                // requestError(i1, s);

                handler.sendSignError(i1, s);
            }

            @Override
            public void onSuccess(WeReq weReq, SignResponse signResponse) {
                if (signResponse != null) {
                    String sign = signResponse.sign;
                    processBody(mode, sign);
                } else {
                    handler.sendSignError(AppHandler.ERROR_DATA, "get response is null");
                }
            }
        });
    }


    private void requestExec(String url, WeReq.WeCallback<SignResponse> callback){
        myOkHttp.<SignResponse>get(url).execute(SignResponse.class, callback);
    }


    private void processBody(String mode, String sign){
        if (TextUtils.isEmpty(sign) || "null".equals(sign)) {
            handler.sendSignError(AppHandler.ERROR_DATA, "sign is null.");
        } else {

        }

    }




    public static class SignResponse implements Serializable {
        public String sign;     //签名
    }


    public  String getUrl(String appId, String userId, String nonce) {
        final String s = "https://ida.webank.com/" + "/ems-partner/cert/signature?appid=" + appId + "&nonce=" + nonce + "&userid=" + userId;
        return s;
    }






}
