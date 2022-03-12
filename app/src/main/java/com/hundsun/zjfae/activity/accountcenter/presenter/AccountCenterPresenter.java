package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.AccountCenterView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.List;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;

/**
 * @Description:账户中心（presenter）
 * @Author: yangtianren
 */
public class AccountCenterPresenter extends BasePresenter<AccountCenterView> {



    public AccountCenterPresenter(AccountCenterView baseView) {
        super(baseView);
    }


    public void onUserData(){

        Observable observable = Observable.mergeDelayError(getUserInfo(),getDictionary());

        addDisposable(observable, new ProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo){

                    baseView.onUserData((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) o);

                }
                else if (o instanceof Dictionary.Ret_PBAPP_dictionary){
                    Dictionary.Ret_PBAPP_dictionary dictionary = (Dictionary.Ret_PBAPP_dictionary) o;
                    List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();
                    baseView.onUserPortrait(parms.get(0).getKeyCode(), parms.get(1).getKeyCode());
                }
            }
        });
    }




    /**
     * 获取头像接口
     */
    private Observable getDictionary() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());
        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("userHeader.urlPrefix");
        diction.addKeyNo("userHeader.urlSuffix");
        return apiServer.getDictionary(url,getBody(diction.build().toByteArray()));
    }



    /**
     * 申请合格投资者失败原因
     * */
    public void getUserHighNetWorthInfo(final String isRealInvestor){
        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.UserHighNetWorthInfo,getRequestMap());
        UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.Builder builder =
                UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.newBuilder();
        builder.setDynamicType1("highNetWorthUpload");

        addDisposable(apiServer.getUserHighNetWorthInfo(url, getBody(builder.build().toByteArray())), new BaseObserver<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo>(baseView) {

            @Override
            public void onSuccess(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo mClass) {
                baseView.getUserHighNetWorthInfo(mClass,isRealInvestor);

            }
        });

    }

}
