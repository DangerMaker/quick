package com.hundsun.zjfae.fragment.finance;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v5.ProductList;


/**
 * @author moran
 * 产品列表View
 *
 * */
public interface ProductView extends BaseView {



    /**
     * 获取产品列表及产品列表属性
     * @param productListNew 产品列表
     * */
    void onProductList(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew productListNew);


    /**
     * 购买列表筛选条件
     * @param productControl 购买列表筛选条件
     * */
    void onControl(AllAzjProto.PEARetControl productControl);

    /**
     *  用户详细信息
     * @param userDetailInfo 用户详细信息
     * */
    void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo,boolean isAuthentication);

    /**
     * 失败原因
     * @param body 用户详细信息
     * @param isRealInvestor  是否真正合格投资者
     * */
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, String isRealInvestor);




    /**
     * 列表为空时展示的图片
     *
     * @param isjump = 是否跳转
     * @param jumpurl  跳转形式，url，合格投资者，身份认证
     * @param returnMsg 图片链接
     * @param isShare 是否分享
     *
     * */
    void onInvestmentState(String isjump,String jumpurl,String returnMsg,String isShare);
}
