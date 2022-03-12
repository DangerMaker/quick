package com.hundsun.zjfae.common.http.observer;

import com.google.gson.Gson;
import com.hundsun.zjfae.activity.mine.view.AddBankView;
import com.hundsun.zjfae.activity.mine.view.BankCardView;
import com.hundsun.zjfae.activity.mine.view.FaceDeleteBankView;
import com.hundsun.zjfae.activity.mine.view.RechargeView;
import com.hundsun.zjfae.activity.mine.view.WithdrawalView;
import com.hundsun.zjfae.activity.product.view.ProductPlayView;
import com.hundsun.zjfae.activity.product.view.SpvProductPlayView;
import com.hundsun.zjfae.activity.product.view.TransferDetailPlayView;
import com.hundsun.zjfae.activity.productreserve.view.ReservePayView;
import com.hundsun.zjfae.activity.productreserve.view.ReserveProductPlayView;
import com.hundsun.zjfae.common.base.BaseView;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.fragment.account.AccountView;
import com.hundsun.zjfae.fragment.home.HomeView;

import java.lang.reflect.InvocationTargetException;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.PBIFEBankcardmanageChangeBankCardPre;
import onight.zjfae.afront.gens.PBIFEBankcardmanageDeleteBankCardPrere;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.v2.PBIFEFundRecharge;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.common.http.observer
 * @ClassName:      BaseBankProtoBufObserver
 * @Description:     绑卡/解绑卡/充值/提现Observer
 * @Author:         moran
 * @CreateDate:     2019/6/12 19:40
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/12 19:40
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public abstract class BaseBankProtoBufObserver <T> extends ProtoBufObserver<T> {
    private static  final String TAG = BaseBankProtoBufObserver.class.getSimpleName();


    public BaseBankProtoBufObserver(){


    }

    public BaseBankProtoBufObserver(BaseView baseView){
        super(baseView);
    }
    public BaseBankProtoBufObserver(BaseView view,String dialogMessage) {
        super(view,dialogMessage);
    }
    @Override
    public void onNext(T t) {
        try {

            CCLog.i(TAG,"----------数据返回解析 Start----------------");
            Class<?> mClass = t.getClass();
            String returnCode = mClass.getDeclaredMethod("getReturnCode").invoke(t).toString();
            String returnMsg = mClass.getDeclaredMethod("getReturnMsg").invoke(t).toString();
            Gson gson = new Gson();
            String json = gson.toJson(t);
            CCLog.i(TAG+"returnCode",returnCode);
            CCLog.i(TAG,"\n");
            CCLog.i(TAG+"returnMsg",returnMsg);
            CCLog.i(TAG,"\n");
            CCLog.i(TAG+"Data",json);
            CCLog.i(TAG,"\n");
            CCLog.i(TAG,"\n");
            CCLog.i(TAG,"----------数据返回解析 End----------------");
            onCoreCodeError(t,returnCode,returnMsg);
            if (view != null) {
                view.hideLoading();
                if (ConstantCode.RETURN_CODE.equals(returnCode)){
                    onSuccess(t);
                }
                else {
                    onReturnCode(t,returnCode,returnMsg);
                }
            }
            else {
                onSuccess(t);
            }



        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

    }
    /**
     * 业务code码处理
     * @param t 返回请求对象
     * @param returnCode 返回code码
     * @param returnMsg 返回信息
     * */
    protected void onReturnCode(T t, String returnCode, String returnMsg) {
        if (t instanceof RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo){
            RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo = (RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo) t;
            String payChannelNo = bankCardInfo.getData().getPayChannelNo();
            String bankName =  bankCardInfo.getData().getBankName();
            String bankCard = bankCardInfo.getData().getBankCardNo();
            String bankNo = bankCardInfo.getData().getBankNo();
            String showTips = bankCardInfo.getData().getShowTips();
            //账户中心
            if (view instanceof AccountView){
                ((AccountView)view).queryRechargeBankInfo(bankName,bankCard,bankNo,showTips,payChannelNo);
            }
            //购买
            else if (view instanceof ProductPlayView){
                ((ProductPlayView)view).queryRechargeBankInfo(bankName,bankCard,bankNo,showTips,payChannelNo);
            }
            //spv购买
            else if (view instanceof SpvProductPlayView){

                ((SpvProductPlayView)view).queryRechargeBankInfo(bankName,bankCard,bankNo,showTips,payChannelNo);
            }
            //转让
            else if (view instanceof TransferDetailPlayView){
                ((TransferDetailPlayView)view).queryRechargeBankInfo(bankName,bankCard,bankNo,showTips,payChannelNo);
            }
            //充值
            else if (view instanceof RechargeView){

                if (returnCode.equals(ConstantCode.NO_PLAY_PASSWORD)){
                    ((RechargeView)view).onSettingUserPlayPassWord(returnMsg);
                }
                else {
                    view.isFinishActivity(returnMsg);
                }

            }

            else {
                onException(returnMsg);
            }
        }
        else if (returnCode.equals(ConstantCode.BANK_CHANNEL_CLOSE)){
            if (view instanceof BankCardView){
                ((BankCardView)view).onBankChannelClose();
            }
        }
        //添加银行卡超时/充值超时
        else if (ConstantCode.ADD_BANK_TIME_OUT.equals(returnCode)){
            if (view != null){
                if (view instanceof AddBankView){
                    ((AddBankView)view) .onAddBankError(returnCode,returnMsg);
                }
                else if (view instanceof BankCardView){
                    ((BankCardView) view).onDeleteBankError(returnCode,returnMsg);
                }
                else if (view instanceof FaceDeleteBankView){
                    ((FaceDeleteBankView) view).onDeleteBankError(returnCode,returnMsg);
                }
                else if (view instanceof RechargeView){
                    ((RechargeView) view).onFundRechargeError(returnCode,returnMsg);
                } else {
                    onException(returnMsg);
                }

            }

        }
        else if (ConstantCode.LOGIN_TIME_OUT.equals(returnCode)){
            if (view != null){
                view.loginTimeOut(returnMsg);
            }
        }
        //充值失败
        else if (t instanceof PBIFEFundRecharge.Ret_PBIFE_fund_recharge){
            if (view instanceof RechargeView){
                ((RechargeView)view).onFundRechargeError(returnCode,returnMsg);
            }
            else {
                onException(returnMsg);
            }
        }
        //解绑卡
        else if (t instanceof PBIFEBankcardmanageDeleteBankCardPrere.Ret_PBIFE_bankcardmanage_deleteBankCardPrere){
            if (view != null ){
                if (ConstantCode.CAPITAL_FLOW.equals(returnCode)){
                    view.showError(returnMsg.replaceAll("<br/>","\n"));
                }
                else {
                    if (view instanceof BankCardView){
                        ((BankCardView)view).onFaceDeleteBank(returnCode,returnMsg);
                    }
                    else {
                        onException(returnMsg);
                    }
                }
            }
        }
        //换卡
        else if (t instanceof PBIFEBankcardmanageChangeBankCardPre.Ret_PBIFE_bankcardmanage_changeBankCardPre){
            if (view != null ) {
                if (ConstantCode.CAPITAL_FLOW.equals(returnCode)) {
                    view.showError(returnMsg.replaceAll("<br/>", "\n"));
                }
                else {

                    if (view instanceof BankCardView){
                        ((BankCardView)view).onFaceChangeBank();
                    }
                    else {
                        onException(returnMsg);
                    }
                }

            }
        }
        else if (t instanceof FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo){
            FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo = (FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo) t;

            if (view instanceof AccountView){
                ((AccountView)view).bankCardManage(fundBankInfo);
            }
            else if (view instanceof ProductPlayView){
                ((ProductPlayView)view).bankCardManage(fundBankInfo);
            }
            else if (view instanceof SpvProductPlayView){

                ((SpvProductPlayView)view).bankCardManage(fundBankInfo);

            }
            else if (view instanceof ReservePayView){
                ((ReservePayView)view).bankCardManage(fundBankInfo);
            }
            else if (view instanceof ReserveProductPlayView){
                ((ReserveProductPlayView)view).bankCardManage(fundBankInfo);
            }
            else if (view instanceof TransferDetailPlayView){
                ((TransferDetailPlayView)view).bankCardManage(fundBankInfo);
            }
            else if (view instanceof BankCardView){

            }
            else {
                onException(returnMsg);
            }
        }
        //特殊处理--提现
        else if (view instanceof WithdrawalView){

            if (returnCode.equals(ConstantCode.NO_TRANSACTION_TIME) || returnCode.equals(ConstantCode.WITH_DRAWAL_CODE) || returnCode.equals(ConstantCode.WITHDRAWAL_ERROR_CODE)){
                view.isFinishActivity(returnMsg);
            }
            else {
                view.showError(returnMsg);
            }

        }
        else {
            if (view != null){
                if (view instanceof HomeView){
                }
                else {
                    view.showError(returnMsg);
                }

            }
        }

    }
}
