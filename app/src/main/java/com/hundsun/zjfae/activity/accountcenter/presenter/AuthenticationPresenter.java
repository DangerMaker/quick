package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.AuthenticationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.CheckUserInfoPB;
import onight.zjfae.afront.gens.LoadMySecurityQuestionPB;
import onight.zjfae.afront.gens.PasswordmanageCheckUserInfoPB;
import onight.zjfae.afront.gens.SendSmsValidateCodeWithInnerMobilePB;
import onight.zjfae.afront.gens.VerifySecurityInfoPB;

/**
 * @Description:身份认证（presenter）
 * @Author: yangtianren
 */
public class AuthenticationPresenter extends BasePresenter<AuthenticationView> {
    public AuthenticationPresenter(AuthenticationView baseView) {
        super(baseView);
    }

    public void getProblem() {
        LoadMySecurityQuestionPB.REQ_PBIFE_securityquestionmanage_loadMySecurityQuestion.Builder builder = LoadMySecurityQuestionPB.REQ_PBIFE_securityquestionmanage_loadMySecurityQuestion.newBuilder();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.LoadMySecurityQuestion, getRequestMap());
        addDisposable(apiServer.getProblem(url,getBody(builder.build().toByteArray())), new ProtoBufObserver< LoadMySecurityQuestionPB.Ret_PBIFE_securityquestionmanage_loadMySecurityQuestion>(baseView) {
            @Override
            public void onSuccess(LoadMySecurityQuestionPB.Ret_PBIFE_securityquestionmanage_loadMySecurityQuestion ret_pbife_securityquestionmanage_loadMySecurityQuestion) {
                baseView.getProblem(ret_pbife_securityquestionmanage_loadMySecurityQuestion.getData().getTcSecurityQuestionAnswerListList());
            }
        });
    }

    //发送短信验证码之前的校验接口
    public void verifySecurityInfo(String securityQuestion, String securityQuestionAnswer, String certificateType, String idCardNo, String password) {
        VerifySecurityInfoPB.REQ_PBIFE_userbaseinfo_verifySecurityInfo.Builder builder = VerifySecurityInfoPB.REQ_PBIFE_userbaseinfo_verifySecurityInfo.newBuilder();
        builder.setSecurityQuestion(securityQuestion);
        builder.setSecurityQuestionAnswer(securityQuestionAnswer);
        builder.setCertificateType(certificateType);
        builder.setIdCardNo(idCardNo);
        builder.setPassword(password);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.VerifySecurityInfo, getRequestMap());
        addDisposable(apiServer.verifySecurityInfo(url,getBody( builder.build().toByteArray())), new ProtoBufObserver< VerifySecurityInfoPB.Ret_PBIFE_userbaseinfo_verifySecurityInfo>(baseView) {
            @Override
            public void onSuccess(VerifySecurityInfoPB.Ret_PBIFE_userbaseinfo_verifySecurityInfo ret_pbife_userbaseinfo_verifySecurityInfo) {
                baseView.verifySecurityInfo(ret_pbife_userbaseinfo_verifySecurityInfo);
            }
        });
    }

    //发送验证码
    public void getVerificationCode(String code) {
        SendSmsValidateCodeWithInnerMobilePB.REQ_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile.Builder builder = SendSmsValidateCodeWithInnerMobilePB.REQ_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile.newBuilder();
        builder.setSmsValidateCodeType(code);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SendSmsValidateCodeWithInnerMobile, getRequestMap());
        addDisposable(apiServer.getVerificationCode(url,getBody(builder.build().toByteArray())), new ProtoBufObserver<  SendSmsValidateCodeWithInnerMobilePB.Ret_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile>(baseView) {
            @Override
            public void onSuccess(SendSmsValidateCodeWithInnerMobilePB.Ret_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile ret_pbife_smsvalidatecode_sendSmsValidateCodeWithInnerMobile) {
                baseView.getVerificationCode(ret_pbife_smsvalidatecode_sendSmsValidateCodeWithInnerMobile.getReturnCode(), ret_pbife_smsvalidatecode_sendSmsValidateCodeWithInnerMobile.getReturnMsg());
            }
        });
    }

    //检查
    public void check(String securityQuestion, String securityQuestionAnswer, String idCardNo, String password, String smsValidateCode) {
        CheckUserInfoPB.REQ_PBIFE_mobilemanage_checkUserInfo.Builder builder = CheckUserInfoPB.REQ_PBIFE_mobilemanage_checkUserInfo.newBuilder();
        builder.setSecurityQuestion(securityQuestion);
        builder.setSecurityQuestionAnswer(securityQuestionAnswer);
        builder.setCertificateCode(idCardNo);
        builder.setPassword(password);
        builder.setSmsValidateCode(smsValidateCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.CheckUserInfo, getRequestMap());
        addDisposable(apiServer.check(url,getBody(builder.build().toByteArray())), new ProtoBufObserver< CheckUserInfoPB.Ret_PBIFE_mobilemanage_checkUserInfo>(baseView) {
            @Override
            public void onSuccess(CheckUserInfoPB.Ret_PBIFE_mobilemanage_checkUserInfo ret_pbife_mobilemanage_checkUserInfo) {
                baseView.check(ret_pbife_mobilemanage_checkUserInfo.getReturnCode(), ret_pbife_mobilemanage_checkUserInfo.getReturnMsg());
            }
        });
    }

    //检查
    public void passwordCheck(String securityQuestion, String securityQuestionAnswer, String idCardNo, String smsValidateCode) {
        PasswordmanageCheckUserInfoPB.REQ_PBIFE_passwordmanage_checkUserInfo.Builder builder=PasswordmanageCheckUserInfoPB.REQ_PBIFE_passwordmanage_checkUserInfo.newBuilder();
        builder.setSecurityQuestion(securityQuestion);
        builder.setSecurityQuestionAnswer(securityQuestionAnswer);
        builder.setCertificateCode(idCardNo);
        builder.setSmsValidateCode(smsValidateCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PasswordCheckUserInfo, getRequestMap());
        addDisposable(apiServer.passwordCheck(url,getBody(builder.build().toByteArray())), new ProtoBufObserver< PasswordmanageCheckUserInfoPB.Ret_PBIFE_passwordmanage_checkUserInfo>(baseView) {
            @Override
            public void onSuccess(PasswordmanageCheckUserInfoPB.Ret_PBIFE_passwordmanage_checkUserInfo ret_pbife_passwordmanage_checkUserInfo) {
                baseView.check(ret_pbife_passwordmanage_checkUserInfo.getReturnCode(), ret_pbife_passwordmanage_checkUserInfo.getReturnMsg());
            }
        });
    }
}
