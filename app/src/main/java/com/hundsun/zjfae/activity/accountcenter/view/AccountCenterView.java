package com.hundsun.zjfae.activity.accountcenter.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;

/**
 * @Description:账户中心（View）
 * @Author: yangtianren
 */
public interface AccountCenterView extends BaseView {



    /**
     * 申请合格投资者失败原因
     * @param highNetWorthInfo 合格投资失败原因
     * @param isRealInvestor 是否真正合格投资者
     * */
    void getUserHighNetWorthInfo(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo highNetWorthInfo,String isRealInvestor);


    /**
     * 用户图片地址
     * @param pic 拼接地址1
     * @param fix 拼接地址2
     * */
    void onUserPortrait(String pic,String fix);


    /**
     * 获取用户详细信息
     * @param userDetailInfo 用户详细信息
     * */
    void onUserData(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

}
