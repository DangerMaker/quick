package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.ModifyPasswordView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.AlterLoginPasswordPB;
import onight.zjfae.afront.gens.AlterTradePasswordPB;
import onight.zjfae.afront.gens.ResetTradePasswordPB;

/**
 * @Description:修改密码（presenter）
 * @Author: yangtianren
 */
public class ModifyPasswordPresenter extends BasePresenter<ModifyPasswordView> {
    public ModifyPasswordPresenter(ModifyPasswordView baseView) {
        super(baseView);
    }

    //修改密码
    public void modifyPassword(String type, String password, String passwordNew, String passwordSure) {
        if ("login".equals(type)) {
            AlterLoginPasswordPB.REQ_PBIFE_passwordmanage_alterLoginPassword.Builder builder = AlterLoginPasswordPB.REQ_PBIFE_passwordmanage_alterLoginPassword.newBuilder();
            builder.setPassword(password);
            builder.setPasswordNew(passwordNew);
            builder.setPasswordSure(passwordSure);
            String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ModifyLoginPassword, getRequestMap());
            addDisposable(apiServer.modifyPassword(url,getBody(builder.build().toByteArray())), new ProtoBufObserver< AlterLoginPasswordPB.Ret_PBIFE_passwordmanage_alterLoginPassword>(baseView) {
                @Override
                public void onSuccess(AlterLoginPasswordPB.Ret_PBIFE_passwordmanage_alterLoginPassword ret_pbife_passwordmanage_alterLoginPassword) {
                    baseView.modify(ret_pbife_passwordmanage_alterLoginPassword.getReturnCode(), ret_pbife_passwordmanage_alterLoginPassword.getReturnMsg());
                }
            });
        } else if ("transaction".equals(type)) {
            AlterTradePasswordPB.REQ_PBIFE_passwordmanage_alterTradePassword.Builder builder = AlterTradePasswordPB.REQ_PBIFE_passwordmanage_alterTradePassword.newBuilder();
            builder.setPassword(password);
            builder.setPasswordNew(passwordNew);
            builder.setPasswordSure(passwordSure);
            String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ModifyTradePassword, getRequestMap());
            addDisposable(apiServer.transactionModifyPassword(url, getBody(builder.build().toByteArray())), new ProtoBufObserver< AlterTradePasswordPB.Ret_PBIFE_passwordmanage_alterTradePassword>(baseView) {
                @Override
                public void onSuccess(AlterTradePasswordPB.Ret_PBIFE_passwordmanage_alterTradePassword ret_pbife_passwordmanage_alterTradePassword) {
                    baseView.modify(ret_pbife_passwordmanage_alterTradePassword.getReturnCode(), ret_pbife_passwordmanage_alterTradePassword.getReturnMsg());
                }
            });
        } else if ("reset_transaction".equals(type)) {
            ResetTradePasswordPB.REQ_PBIFE_passwordmanage_resetTradePassword.Builder builder = ResetTradePasswordPB.REQ_PBIFE_passwordmanage_resetTradePassword.newBuilder();
            builder.setPassword(passwordNew);
            builder.setPasswordSure(passwordSure);
            String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ResetTradePassword, getRequestMap());
            addDisposable(apiServer.ResetTransactionModifyPassword(url,getBody( builder.build().toByteArray())), new ProtoBufObserver< ResetTradePasswordPB.Ret_PBIFE_passwordmanage_resetTradePassword>(baseView) {
                @Override
                public void onSuccess(ResetTradePasswordPB.Ret_PBIFE_passwordmanage_resetTradePassword ret_pbife_passwordmanage_resetTradePassword) {
                    baseView.modify(ret_pbife_passwordmanage_resetTradePassword.getReturnCode(), ret_pbife_passwordmanage_resetTradePassword.getReturnMsg());
                }
            });
        }
    }
}
