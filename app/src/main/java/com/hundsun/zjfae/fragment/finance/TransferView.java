package com.hundsun.zjfae.fragment.finance;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v4.TransferList;


/**
 * @author moran 转让列表View
 */
public interface TransferView extends BaseView {


    /**
     * 转让列表
     *
     * @param listNew 转让列表
     */
    void initTransfer(TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew listNew);


    /**
     * 转让列表集合数量
     * @param body 转让列表信息
     */
    void onQueryTransferList(TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew body);

    /**
     * 转让列表筛选条件
     *
     * @param productControl 购买列表筛选条件
     */
    void onControl(AllAzjProto.PEARetControl productControl, String controlType);

    /**
     * 用户详细信息
     *
     * @param userDetailInfo 用户详细信息
     */
    void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, boolean isAuthentication);

    /**
     * 失败原因
     *
     * @param body           用户详细信息
     * @param isRealInvestor 是否真正合格投资者
     */
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, String isRealInvestor);


    /**
     * 列表为空时展示的图片
     *
     * @param isjump    = 是否跳转
     * @param jumpurl   跳转形式，url，合格投资者，身份认证
     * @param returnMsg 图片链接
     * @param isShare   是否分享
     */
    void onInvestmentState(String isjump, String jumpurl, String returnMsg, String isShare);
}
