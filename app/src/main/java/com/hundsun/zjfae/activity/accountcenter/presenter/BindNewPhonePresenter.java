package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.BindNewPhoneView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.ImageObserver;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.FileUtil;

import java.io.File;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import onight.zjfae.afront.gens.ModifyUserMobilePB;
import onight.zjfae.afront.gens.SendSmsValidateCodeWithImageCodePB;

public class BindNewPhonePresenter extends BasePresenter<BindNewPhoneView> {
    public BindNewPhonePresenter(BindNewPhoneView baseView) {
        super(baseView);
    }

    /**
     * 请求图形验证码
     */

    public void imageAuthCode(final File file) {
        Map map = getRequestMap();
        map.put("fh", VIMGMZJ);
        map.put("type", "4");
        map.put("tdsourcetag", "s_pctim_aiomsg");
        String url = parseUrl(MZJ, PBIMG, map);

        apiServer.imageCode(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ImageObserver(baseView) {
                    @Override
                    public void onSuccess(ResponseBody body) {
                        baseView.imageCode(body);
                        CCLog.e("长度", body.contentLength());
                        File files = FileUtil.saveFile(file.getPath(), body);
                    }

                });
    }

    //发送验证码
    public void getVerificationCode(String phone, String imgCode) {
        SendSmsValidateCodeWithImageCodePB.REQ_PBIFE_smsvalidatecode_sendSmsValidateCodeWithImageCode.Builder builder=SendSmsValidateCodeWithImageCodePB.REQ_PBIFE_smsvalidatecode_sendSmsValidateCodeWithImageCode.newBuilder();
        builder.setMobile(phone);
        builder.setImageCode(imgCode);
        builder.setSmsValidateCodeType("D");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SendSmsValidateCodeWithImageCode, getRequestMap());
        addDisposable(apiServer.getSmsVerificationCode(url,getBody(builder.build().toByteArray())), new ProtoBufObserver< SendSmsValidateCodeWithImageCodePB.Ret_PBIFE_smsvalidatecode_sendSmsValidateCodeWithImageCode>(baseView) {
            @Override
            public void onSuccess(SendSmsValidateCodeWithImageCodePB.Ret_PBIFE_smsvalidatecode_sendSmsValidateCodeWithImageCode ret_pbife_smsvalidatecode_sendSmsValidateCodeWithImageCode) {
                baseView.getVerificationCode(ret_pbife_smsvalidatecode_sendSmsValidateCodeWithImageCode.getReturnCode(), ret_pbife_smsvalidatecode_sendSmsValidateCodeWithImageCode.getReturnMsg());
            }
        });
    }

    //发送验证码
    public void modifyUserMobile(String phone, String Code) {
        ModifyUserMobilePB.REQ_PBIFE_mobilemanage_modifyUserMobile.Builder builder=ModifyUserMobilePB.REQ_PBIFE_mobilemanage_modifyUserMobile.newBuilder();
        builder.setMobile(phone);
        builder.setSmsValidateCode(Code);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ModifyUserMobile, getRequestMap());
        addDisposable(apiServer.modifyUserMobile(url,getBody(builder.build().toByteArray())), new ProtoBufObserver< ModifyUserMobilePB.Ret_PBIFE_mobilemanage_modifyUserMobile>(baseView) {
            @Override
            public void onSuccess(ModifyUserMobilePB.Ret_PBIFE_mobilemanage_modifyUserMobile ret_pbife_mobilemanage_modifyUserMobile) {
                baseView.modifyUserMobile(ret_pbife_mobilemanage_modifyUserMobile.getReturnCode(), ret_pbife_mobilemanage_modifyUserMobile.getReturnMsg());
            }
        });
    }

}
