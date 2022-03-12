package com.hundsun.zjfae.activity.forget;

import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.ImageObserver;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;

import java.util.Map;

import okhttp3.ResponseBody;
import onight.zjfae.afront.gens.ForgetCheckMobileCodePb;
import onight.zjfae.afront.gens.ForgetFindWaysInfoPb;
import onight.zjfae.afront.gens.ForgetSendMobileValidateCodePb;
import onight.zjfae.afront.gens.ForgetSetPasswordPb;
import onight.zjfae.afront.gens.v2.ForgetCheckUserNamePb;

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordView> {
    public ForgetPasswordPresenter(ForgetPasswordView baseView) {
        super(baseView);
    }

    /**
     * 请求图形验证码
     */

    public void imageAuthCode() {
        Map map = getRequestMap();
        map.put("fh", VIMGMZJ);
        map.put("type", "4");
        map.put("tdsourcetag", "s_pctim_aiomsg");
        String url = parseUrl(MZJ, PBIMG, map);

        addDisposable(apiServer.imageCode(url), new ImageObserver(baseView) {
            @Override
            public void onSuccess(ResponseBody body) {
                baseView.imageCode(body);
            }
        });
    }

    //验证身份信息
    public void checkUserName(String phone, String id, String imageCode) {
        ForgetCheckUserNamePb.REQ_PBIFE_forgetpasswordmanage_checkUsername.Builder builder = ForgetCheckUserNamePb.REQ_PBIFE_forgetpasswordmanage_checkUsername.newBuilder();
        builder.setUsername(phone);
        builder.setCertificateCode(id);
        builder.setImageCode(imageCode);
        builder.setVersion("1.0.1");
        Map<String,String> map = getRequestMap();
        map.put("version","v2");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ForgetCheckUserName, map);
        addDisposable(apiServer.checkUserName(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ForgetCheckUserNamePb.Ret_PBIFE_forgetpasswordmanage_checkUsername>(baseView) {
            @Override
            public void onSuccess(ForgetCheckUserNamePb.Ret_PBIFE_forgetpasswordmanage_checkUsername ret_pbife_forgetpasswordmanage_checkUsername) {
                checkPhone();
            }
        });
    }

    //验证身份信息
    public void checkPhone() {
        ForgetFindWaysInfoPb.REQ_PBIFE_forgetpasswordmanage_findWaysInfo.Builder builder = ForgetFindWaysInfoPb.REQ_PBIFE_forgetpasswordmanage_findWaysInfo.newBuilder();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ForgetFindWaysInfo, getRequestMap());
        addDisposable(apiServer.checkPhone(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ForgetFindWaysInfoPb.Ret_PBIFE_forgetpasswordmanage_findWaysInfo>(baseView) {
            @Override
            public void onSuccess(ForgetFindWaysInfoPb.Ret_PBIFE_forgetpasswordmanage_findWaysInfo ret_pbife_forgetpasswordmanage_findWaysInfo) {
                baseView.checkUserName(ret_pbife_forgetpasswordmanage_findWaysInfo.getReturnCode(), ret_pbife_forgetpasswordmanage_findWaysInfo.getReturnMsg());
            }
        });
    }

    //发送短信验证码
    public void sendCode() {
        ForgetSendMobileValidateCodePb.REQ_PBIFE_forgetpasswordmanage_sendMobileValidateCode.Builder builder = ForgetSendMobileValidateCodePb.REQ_PBIFE_forgetpasswordmanage_sendMobileValidateCode.newBuilder();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ForgetSendCode, getRequestMap());
        addDisposable(apiServer.sendCode(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ForgetSendMobileValidateCodePb.Ret_PBIFE_forgetpasswordmanage_sendMobileValidateCode>(baseView) {
            @Override
            public void onSuccess(ForgetSendMobileValidateCodePb.Ret_PBIFE_forgetpasswordmanage_sendMobileValidateCode ret_pbife_forgetpasswordmanage_sendMobileValidateCode) {
                baseView.sendCode(ret_pbife_forgetpasswordmanage_sendMobileValidateCode.getReturnCode(), ret_pbife_forgetpasswordmanage_sendMobileValidateCode.getReturnMsg());
            }
        });
    }

    //验证短信验证码
    public void checkCode(String code) {
        ForgetCheckMobileCodePb.REQ_PBIFE_forgetpasswordmanage_checkMobileCode.Builder builder = ForgetCheckMobileCodePb.REQ_PBIFE_forgetpasswordmanage_checkMobileCode.newBuilder();
        builder.setMobileValidateCode(code);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ForgetCheckCode, getRequestMap());
        addDisposable(apiServer.checkCode(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ForgetCheckMobileCodePb.Ret_PBIFE_forgetpasswordmanage_checkMobileCode>(baseView) {
            @Override
            public void onSuccess(ForgetCheckMobileCodePb.Ret_PBIFE_forgetpasswordmanage_checkMobileCode ret_pbife_forgetpasswordmanage_checkMobileCode) {
                baseView.submitCode(ret_pbife_forgetpasswordmanage_checkMobileCode.getReturnCode(), ret_pbife_forgetpasswordmanage_checkMobileCode.getReturnMsg());
            }
        });
    }

    //提交新密码
    public void setPassword(String password, String passwordAgain) {
        ForgetSetPasswordPb.REQ_PBIFE_forgetpasswordmanage_setPassword.Builder builder = ForgetSetPasswordPb.REQ_PBIFE_forgetpasswordmanage_setPassword.newBuilder();
        builder.setPassword(EncDecUtil.AESEncrypt(password));
        builder.setPasswordSure(EncDecUtil.AESEncrypt(passwordAgain));
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ForgetSetPassword, getRequestMap());
        addDisposable(apiServer.setPassword(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ForgetSetPasswordPb.Ret_PBIFE_forgetpasswordmanage_setPassword>(baseView) {
            @Override
            public void onSuccess(ForgetSetPasswordPb.Ret_PBIFE_forgetpasswordmanage_setPassword ret_pbife_forgetpasswordmanage_setPassword) {
                baseView.setPassword(ret_pbife_forgetpasswordmanage_setPassword.getReturnCode(), ret_pbife_forgetpasswordmanage_setPassword.getReturnMsg());
            }
        });
    }
}
