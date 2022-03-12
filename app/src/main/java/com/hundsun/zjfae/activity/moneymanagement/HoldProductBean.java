package com.hundsun.zjfae.activity.moneymanagement;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.EntrustedDetails;
import onight.zjfae.afront.gens.ProductPre;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;

public class HoldProductBean {


    private PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails productDetails;//产品详情

    private Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachment;//附件列表

    private EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails entrustedDetails;//受托管理报告

    private UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo;//用户详细信息

    private ProductPre.Ret_PBIFE_trade_subscribeProductPre productPre;//预检查第一步

    private Notices.Ret_PBAPP_notice notice;//免责声明

    public ProductPre.Ret_PBIFE_trade_subscribeProductPre getProductPre() {
        return productPre;
    }

    public void setProductPre(ProductPre.Ret_PBIFE_trade_subscribeProductPre productPre) {
        this.productPre = productPre;
    }



    public PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails productDetails) {
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

    public EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails getEntrustedDetails() {
        return entrustedDetails;
    }

    public void setEntrustedDetails(EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails entrustedDetails) {
        this.entrustedDetails = entrustedDetails;
    }
    public Notices.Ret_PBAPP_notice getNotice() {
        return notice;
    }

    public void setNotice(Notices.Ret_PBAPP_notice notice) {
        this.notice = notice;
    }

}
