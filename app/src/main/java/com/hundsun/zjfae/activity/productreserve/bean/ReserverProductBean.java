package com.hundsun.zjfae.activity.productreserve.bean;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductPre;
import onight.zjfae.afront.gens.v2.ReserveProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

public class ReserverProductBean {


    private ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails productDetails;//产品详情

    private Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachment;//附件列表

    private UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo;//用户详细信息

    private ProductPre.Ret_PBIFE_trade_subscribeProductPre productPre;//预检查第一步

    public ProductPre.Ret_PBIFE_trade_subscribeProductPre getProductPre() {
        return productPre;
    }

    public void setProductPre(ProductPre.Ret_PBIFE_trade_subscribeProductPre productPre) {
        this.productPre = productPre;
    }



    public ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    public Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachment) {
        this.attachment = attachment;
    }

    public UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo getUserDetailInfo() {
        return userDetailInfo;
    }

    public void setUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        this.userDetailInfo = userDetailInfo;
    }




}
