package com.zjfae.captcha;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.netease.nis.captcha.Captcha;
import com.netease.nis.captcha.CaptchaConfiguration;
import com.netease.nis.captcha.CaptchaListener;


/**
  * @ProjectName:
  * @Package:        com.zjfae.captcha
  * @ClassName:      CustomCaptchaUtils
  * @Description:     java类作用描述
  * @Author:         moran
  * @CreateDate:     2019/7/18 18:49
  * @UpdateUser:     更新者：
  * @UpdateDate:     2019/7/18 18:49
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class CustomCaptchaUtils {


    //9205af2b07c143a7b07767409685daa9点选
    //87e30702b0774b72a675440230930ec1滑动
    //默认滑动key
    private  String captchaId = "87e30702b0774b72a675440230930ec1";

    private String  senseFlag = "false";

    private CaptListener captListener;

    private Captcha mCaptcha = null;
    private Context context;

    private  CaptchaConfiguration.Builder builder;

    public CustomCaptchaUtils(Context context){

        this.context = context;

        builder = new CaptchaConfiguration.Builder().listener(captchaListener);
        builder.touchOutsideDisappear(false);

        init();
    }


    private void init(){

        builder.captchaId(captchaId);
//        final CaptchaConfiguration configuration = new CaptchaConfiguration.Builder()
//                .captchaId(captchaId)
//                //.mode(CaptchaConfiguration.ModeType.MODE_CAPTCHA)
//                .listener(captchaListener).build(context);
//
//
//        mCaptcha = Captcha.getInstance().init(configuration);
    }



    //设置点选模式的key
    public void setCaptchaId(String captchaId) {
        if (captchaId == null || captchaId.equals("")){
            captchaId = "87e30702b0774b72a675440230930ec1";
        }

        this.captchaId = captchaId;
        builder.captchaId(captchaId);
    }

    public void setSenseFlag(String senseFlag) {
        this.senseFlag = senseFlag;
        if (senseFlag.equals("true")){
            builder.mode(CaptchaConfiguration.ModeType.MODE_INTELLIGENT_NO_SENSE);
        }
        else {
            builder.mode(CaptchaConfiguration.ModeType.MODE_CAPTCHA);
        }


    }

    public void start(){
        mCaptcha = Captcha.getInstance().init(builder.build(context));
        mCaptcha.validate();
    }




    public void setCaptchaListener(CaptListener captchaListener) {
        this.captListener = captchaListener;
    }


    private CaptchaListener captchaListener = new CaptchaListener() {
        @Override
        public void onReady() {

        }

        @Override
        public void onValidate(String result,final String token, String message) {

            if (result.equals("true")){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = SUCCESS_CODE;
                        Bundle bundle = new Bundle();
                        bundle.putString("token",token);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                }).start();


            }
        }

        @Override
        public void onError(final String error) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.what = ERROR_CODE;
                    Bundle bundle = new Bundle();
                    bundle.putString("error",error);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }).start();
        }

        @Override
        public void onCancel() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.what = CANCEL_CODE;
                    handler.sendMessage(message);
                }
            }).start();
        }

        @Override
        public void onClose() {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.what = CLOSE_CODE;
                    handler.sendMessage(message);
                }
            }).start();

        }
    };



    public interface CaptListener{
         /**
          * 描述一下方法的作用
          * @date: 2020/8/27 11:04
          * @author: moran
          * @param token 网易云盾返回的token
          * @return
          */
        void onSuccess(String token);

         /**
          * 描述一下方法的作用
          * @date: 2020/8/27 11:04
          * @author: moran
          * @param error 网易云盾错误信息
          * @return
          */
        void onError(String error);

         /**
          * 取消网易云盾检测
          * @date: 2020/8/27 11:04
          * @author: moran
          * @return
          */
        void onCancel();

         /**
          * 网易云盾关闭
          * @date: 2020/8/27 11:05
          * @author: moran
          * @return
          */
        void onClose();


    }

    private static final int SUCCESS_CODE = 0x754;

    private static final int ERROR_CODE = 0x854;

    private static final int CANCEL_CODE = 0x954;

    private static final int CLOSE_CODE = 0x654;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

            switch (msg.what){
                case SUCCESS_CODE:
                    if (captListener != null){
                        captListener.onSuccess(bundle.getString("token"));
                    }
                    break;
                case ERROR_CODE:
                    if (captListener != null){
                        captListener.onError(bundle.getString("error"));
                    }
                    break;

                case CANCEL_CODE:
                    if (captListener != null){
                        captListener.onCancel();
                    }
                    break;

                case CLOSE_CODE:
                    if (captchaListener != null){

                        captListener.onClose();
                    }
                    break;
                    default:
                        break;
            }
        }
    };

}
