package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.activity.mine.bean.FaceIdCardBean;
import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.AcquireBankSmsCheckCode;
import onight.zjfae.afront.gens.AddBankCard;
import onight.zjfae.afront.gens.BasicInfo;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.QueryBankInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.TencentOcrId;

/**
  *  @ProjectName:
  * @Package:        com.hundsun.zjfae.activity.mine.view
  * @ClassName:      AddBankView
  * @Description:    添加银行卡
  * @Author:         moran
  * @CreateDate:     2019/6/14 16:52
  * @UpdateUser:     更新者：
  * @UpdateDate:     2019/6/14 16:52
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public interface AddBankView extends BaseView {



    /**
     * 用户详细信息接口
     * @param userDetailInfo 用户详细信息
     * */
    void onUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

    /**
     * 用户基本信息
     * @param basicInfo 用户基本信息
     * */
    void onUserBasicInfo(BasicInfo.Ret_PBIFE_userbaseinfo_getBasicInfo basicInfo);

    void onBindingBankBean( QueryBankInfo.Ret_PBIFE_bankcardmanage_queryBankInfo queryBankInfo);//识别银行卡信息

    void onFundBankInfoBean(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo);//获取银行渠道号

    void onBankSmsCheckCodeBean(AcquireBankSmsCheckCode.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode acquireBankSmsCheckCode);//获取银行短信验证码

    void onAddBank(AddBankCard.Ret_PBIFE_bankcardmanage_addBankCard addBankCard);//绑卡


    void onIdCardImage(FaceIdCardBean cardBean);


    /**
     * 充值失败
     * @param returnCode 返回code码
     *  @param returnMsg 充值失败信息
     * */
    void onAddBankError(String returnCode,String returnMsg);

    /**
     * 腾讯ocr识别
     * @param ret_pbapp_tencentID orc需要的参数
     * */
    void onTencentOcrId(TencentOcrId.Ret_PBAPP_tencentID ret_pbapp_tencentID);
    /**
     * 人脸识别状态
     * @param isTencentFace 是否开启人脸识别
     * */
    void onFaceStatus(boolean isTencentFace);
}
