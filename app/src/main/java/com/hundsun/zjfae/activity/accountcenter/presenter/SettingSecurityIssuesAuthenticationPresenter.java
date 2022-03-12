package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.SettingSecurityIssuesAuthenticationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.SecurityCheckUserInfoPB;
import onight.zjfae.afront.gens.SendSmsValidateCodeWithInnerMobilePB;
import onight.zjfae.afront.gens.VerifySecurityInfoPB;

/**
 * @Description:设置安保问题（presenter）
 * @Author: yangtianren
 */
public class SettingSecurityIssuesAuthenticationPresenter extends BasePresenter<SettingSecurityIssuesAuthenticationView> {
    public SettingSecurityIssuesAuthenticationPresenter(SettingSecurityIssuesAuthenticationView baseView) {
        super(baseView);
    }

    //发送短信验证码之前的校验接口
    public void verifySecurityInfo(String certificateType, String idCardNo, String password) {
        VerifySecurityInfoPB.REQ_PBIFE_userbaseinfo_verifySecurityInfo.Builder builder = VerifySecurityInfoPB.REQ_PBIFE_userbaseinfo_verifySecurityInfo.newBuilder();
        builder.setCertificateType(certificateType);
        builder.setIdCardNo(idCardNo);
        builder.setPassword(password);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.VerifySecurityInfo, getRequestMap());
        addDisposable(apiServer.checkSmsVerifySecurityInfo(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<VerifySecurityInfoPB.Ret_PBIFE_userbaseinfo_verifySecurityInfo>(baseView) {
            @Override
            public void onSuccess(VerifySecurityInfoPB.Ret_PBIFE_userbaseinfo_verifySecurityInfo ret_pbife_userbaseinfo_verifySecurityInfo) {
                baseView.verifySecurityInfo(ret_pbife_userbaseinfo_verifySecurityInfo.getData());
            }
        });
    }

    //发送验证码
    public void getVerificationCode() {
        SendSmsValidateCodeWithInnerMobilePB.REQ_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile.Builder builder = SendSmsValidateCodeWithInnerMobilePB.REQ_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile.newBuilder();
        builder.setSmsValidateCodeType("M1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SendSmsValidateCodeWithInnerMobile, getRequestMap());
        addDisposable(apiServer.getVerificationCode(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<SendSmsValidateCodeWithInnerMobilePB.Ret_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile>(baseView) {
            @Override
            public void onSuccess(SendSmsValidateCodeWithInnerMobilePB.Ret_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile ret_pbife_smsvalidatecode_sendSmsValidateCodeWithInnerMobile) {
                baseView.getVerificationCode(ret_pbife_smsvalidatecode_sendSmsValidateCodeWithInnerMobile.getReturnCode(), ret_pbife_smsvalidatecode_sendSmsValidateCodeWithInnerMobile.getReturnMsg());
            }
        });
    }

    //检查
    public void check(String idCardNo, String password, String smsValidateCode) {
        SecurityCheckUserInfoPB.REQ_PBIFE_securityquestionmanage_checkUserInfo.Builder builder=SecurityCheckUserInfoPB.REQ_PBIFE_securityquestionmanage_checkUserInfo.newBuilder();
        builder.setCertificateCode(idCardNo);
        builder.setPassword(password);
        builder.setSmsValidateCode(smsValidateCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SecurityCheckUserInfo, getRequestMap());
        addDisposable(apiServer.checkProblem(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<SecurityCheckUserInfoPB.Ret_PBIFE_securityquestionmanage_checkUserInfo>(baseView) {
            @Override
            public void onSuccess(SecurityCheckUserInfoPB.Ret_PBIFE_securityquestionmanage_checkUserInfo ret_pbife_securityquestionmanage_checkUserInfo) {
                baseView.check(ret_pbife_securityquestionmanage_checkUserInfo.getReturnCode(), ret_pbife_securityquestionmanage_checkUserInfo.getReturnMsg());
            }
        });
    }
}
