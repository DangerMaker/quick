package com.hundsun.zjfae.common.http.api;

import com.google.gson.JsonObject;
import com.hundsun.zjfae.AppUpDate;
import com.hundsun.zjfae.activity.home.bean.ImageUploadBean;
import com.hundsun.zjfae.activity.mine.bean.FaceIdCardBean;
import com.hundsun.zjfae.activity.mine.bean.FaceResultBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.*;
import onight.zjfae.afront.gens.v2.ForgetCheckUserNamePb;
import onight.zjfae.afront.gens.v2.PBIFEFundRecharge;
import onight.zjfae.afront.gens.v2.PBIFEPrdtransferqueryPrdDeliveryInfoDetail;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v2.QueryTransferSellProfitsPB;
import onight.zjfae.afront.gens.v2.ReserveProductDetails;
import onight.zjfae.afront.gens.v2.SetSecurityQuestionPrePB;
import onight.zjfae.afront.gens.v2.TransferOrderSec;
import onight.zjfae.afront.gens.v3.OrderNotSubscribedProduct;
import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew;
import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;
import onight.zjfae.afront.gens.v3.PrdQueryTaUnitFinanceNewPb;
import onight.zjfae.afront.gens.v3.ProductOrderInfoDetailPB;
import onight.zjfae.afront.gens.v3.QuerySystemDict;
import onight.zjfae.afront.gens.v4.TransferList;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v5.PBIFETradeTransferOrder;
import onight.zjfae.afront.gensazj.ADPictureProtoBuf;
import onight.zjfae.afront.gensazj.Advertise;
import onight.zjfae.afront.gensazj.DictDynamics;
import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.FaceProtol;
import onight.zjfae.afront.gensazj.FinanceSection;
import onight.zjfae.afront.gensazj.FundAccountLogPB;
import onight.zjfae.afront.gensazj.ListMessagePB;
import onight.zjfae.afront.gensazj.v2.Notices;
import onight.zjfae.afront.gensazj.PBAPPAndNativeWhiteScreen;
import onight.zjfae.afront.gensazj.PrivateNotice;
import onight.zjfae.afront.gensazj.ProductOrderInfoPB;
import onight.zjfae.afront.gensazj.ReadMessagePB;
import onight.zjfae.afront.gensazj.RecommendDetailsInfoPB;
import onight.zjfae.afront.gensazj.StatisticsStepsPB;
import onight.zjfae.afront.gensazj.TencentFace;
import onight.zjfae.afront.gensazj.TencentFaceCallback;
import onight.zjfae.afront.gensazj.TencentOcrId;
import onight.zjfae.afront.gensazj.UnReadMes;
import onight.zjfae.afront.gensazj.UrlParams;
import onight.zjfae.afront.gensazj.UserAppList;
import onight.zjfae.afront.gensazj.UserRecommendInfoPB;
import onight.zjfae.afront.gensazj.WithdrawalsCoupon;
import onight.zjfae.afront.gensazj.v2.BannerProto;
import onight.zjfae.afront.gensazj.v2.Login;
import onight.zjfae.afront.gensazj.v2.LoginIcons;
import onight.zjfae.afront.gensazj.v2.UpdInterface;
import onight.zjfae.afront.gensazj.v3.IconsLogin;
import onight.zjfae.afront.gensazj.v3.StatisticsDataProtoBuf;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 作者： ch
 * 时间： 2016/12/27.13:56
 * 描述：
 * 来源：
 */

public interface ApiServer {
    //图片更新
    @POST
    Observable<UpdInterface.Ret_PBAPP_updInterface> imageIconUpdate(@Url String url, @Body RequestBody body);

    //获取所有pbnot所有icon
    @POST
    Observable<AllAzjProto.PEARetNotice> noticeIcon(@Url String url, @Body RequestBody body);

    //获取所有pbads的icon,启动广告,首页弹框
    @POST
    Observable<AllAzjProto.PEARetAds> getAllPbadsIcon(@Url String url, @Body RequestBody body);

    //获取所有pbaft的icon,产品列表
    @POST
    Observable<Notices.Ret_PBAPP_notice> getAllPbaftString(@Url String url, @Body RequestBody body);

    //获取图形验证码
    @GET
    Observable<ResponseBody> imageCode(@Url String url);


    //APP更新
    @POST
    Observable<AppUpDate> appUpdate(@Url String url, @Body String body);

    //获取用户隐私权限
    @POST
    Observable<PrivateNotice.Ret_PBAPP_privacyNotice> getUserPrivate(@Url String url);

    //上送用户确认隐私协议的状态及时间
    @POST
    Observable<PBIFERegAddUsersRegisterPrivacyAgree.Ret_PBIFE_reg_addUsersRegisterPrivacyAgree> onUpUserNoticeStatus(@Url String url,@Body RequestBody body);


    //首页banner
    @POST
    Observable<BannerProto.Ret_PBAPP_ads> homeBanner(@Url String url, @Body RequestBody body);


    //首页banner
    @POST
    Observable<Advertise.Ret_PBAPP_advertise> loginBanner(@Url String url, @Body RequestBody body);

    //未登录的Icon图
    @POST
    Observable<AllAzjProto.PEARetIcons> outLoginIcon(@Url String url, @Body RequestBody body);

    //登录后的icon图
    @POST
    Observable<IconsLogin.Ret_PBAPP_icons_login> loginIcon(@Url String url);


    @POST
    Observable<ADPictureProtoBuf.Ret_PBAPP_ads_picture> onAdPrice(@Url String url, @Body RequestBody body);

    /**
     * 首页轮播信息
     *
     * @param url  请求地址
     * @param body 请求信息
     */
    @POST
    Observable<UnReadMes.Ret_PBAPP_unreadMes> getUnReadMsg(@Url String url, @Body RequestBody body);

    //高净值专区
    @POST
    Observable<FinanceSection.Ret_PBAPP_FinanceSection_login> highProductIcon(@Url String url);

    //高净值专区
    @POST
    Observable<AllAzjProto.PEARetIcons> highProductStartIcon(@Url String url, @Body RequestBody body);

//
//    //新手专享
//    @POST
//    Observable<AllAzjProto.PEARetIcons> homeSubscrilbe(@Url String url, @Body RequestBody body);

    //首页底部产品列表
    @POST
    Observable<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome> homeBottomProduct(@Url String url, @Body RequestBody body);

    @POST
    Observable<AllAzjProto.PEARetMessageCount> userMassage(@Url String url, @Body RequestBody body);

    //用户信息
    @POST
    Observable<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo> getUserInfo(@Url String url, @Body RequestBody body);

    //用户信息
    @POST
    Observable<UserAppList.Ret_PBAPP_user_appList> requestUserAppList(@Url String url, @Body RequestBody body);

    //查询是否登录超时

    //登录后的icon图
    @POST
    Observable<LoginStatus.Ret_PBIFE_login_status> loginStatus(@Url String url);


    //登录
    @POST
    Observable<Login.Ret_PBAPP_login> login(@Url String url, @Body RequestBody body);

    //所有Icon图
    @POST
    Observable<AllAzjProto.PEARetIcons> allIconImage(@Url String url, @Body RequestBody body);


    //登录图
    @POST
    Observable<LoginIcons.Ret_PBAPP_icons> onLoginIcons(@Url String url, @Body RequestBody body);


     /**
      * 产品列表title请求
      * @method
      * @date: 2020/10/26 18:38
      * @author: moran
      * @param url 请求url
      * @return
      */
    @POST
    Observable<PBIFEUserbaseinfoGetFunctionMenus.Ret_PBIFE_userbaseinfo_getFunctionMenus>getProductListTitle(@Url String url);

    //产品条件
    @POST
    Observable<AllAzjProto.PEARetControl> subscribeProduct(@Url String url, @Body RequestBody body);

    //产品列表
    @POST
    Observable<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew> productList(@Url String url, @Body RequestBody body);



    //高净值列表
    @POST
    Observable<ProductHighTransferOrderList.Ret_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList> productHighList(@Url String url, @Body RequestBody body);


    //首页获取购买数目的接口
    @POST
    Observable<OrderNotSubscribedProduct.Ret_PBIFE_trade_queryOrderNotSubscribedProduct> goProduct(@Url String url,@Body RequestBody body);

    //转让条件
    @POST
    Observable<AllAzjProto.PEARetControl> subscribeTransfer(@Url String url, @Body RequestBody body);

    //转让列表
    @POST
    Observable<TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew> transferList(@Url String url, @Body RequestBody body);

    //产品详情
    @POST
    Observable<PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails> productCodeInfo(@Url String url, @Body RequestBody body);

    //附件列表
    @POST
    Observable<Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment> attachmentList(@Url String url, @Body RequestBody body);

    //受托管理报告
    @POST
    Observable<EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails> entrustedDetailsList(@Url String url, @Body RequestBody body);

    //查询银行卡信息
    @POST
    Observable<RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo> queryBankInfo(@Url String url);

    //合格投资者失败原因
    @POST
    Observable<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo> investorStatus(@Url String url, @Body RequestBody body);


    //申请合格投资者失败原因
    @POST
    Observable<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo> getUserHighNetWorthInfo(@Url String url, @Body RequestBody body);


    @POST
    Observable<LoadMySecurityQuestionPB.Ret_PBIFE_securityquestionmanage_loadMySecurityQuestion> getProblem(@Url String url, @Body RequestBody body);

    //发送短信验证码之前的校验接口
    @POST
    Observable<VerifySecurityInfoPB.Ret_PBIFE_userbaseinfo_verifySecurityInfo> verifySecurityInfo(@Url String url, @Body RequestBody body);

    //发送验证码
    @POST
    Observable<SendSmsValidateCodeWithInnerMobilePB.Ret_PBIFE_smsvalidatecode_sendSmsValidateCodeWithInnerMobile> getVerificationCode(@Url String url, @Body RequestBody body);


    //检查
    @POST
    Observable<CheckUserInfoPB.Ret_PBIFE_mobilemanage_checkUserInfo> check(@Url String url, @Body RequestBody body);

    //检查
    @POST
    Observable<PasswordmanageCheckUserInfoPB.Ret_PBIFE_passwordmanage_checkUserInfo> passwordCheck(@Url String url, @Body RequestBody body);


    //发送验证码
    @POST
    Observable<SendSmsValidateCodeWithImageCodePB.Ret_PBIFE_smsvalidatecode_sendSmsValidateCodeWithImageCode> getSmsVerificationCode(@Url String url, @Body RequestBody body);


    //发送验证码
    @POST
    Observable<ModifyUserMobilePB.Ret_PBIFE_mobilemanage_modifyUserMobile> modifyUserMobile(@Url String url, @Body RequestBody body);


    //修改密码
    @POST
    Observable<AlterLoginPasswordPB.Ret_PBIFE_passwordmanage_alterLoginPassword> modifyPassword(@Url String url, @Body RequestBody body);


    //修改密码
    @POST
    Observable<AlterTradePasswordPB.Ret_PBIFE_passwordmanage_alterTradePassword> transactionModifyPassword(@Url String url, @Body RequestBody body);


    //重置密码
    @POST
    Observable<ResetTradePasswordPB.Ret_PBIFE_passwordmanage_resetTradePassword> ResetTransactionModifyPassword(@Url String url, @Body RequestBody body);

    //获取风险测评问题答案
    @POST
    Observable<QueryRiskAssessmentQuestionPB.Ret_PBIFE_riskassessment_queryRiskAssessmentQuestion> getProblemList(@Url String url, @Body RequestBody body);


    //提交答案
    @POST
    Observable<RiskAssessmentCommitPB.Ret_PBIFE_riskassessment_riskAssessmentCommit> commit(@Url String url, @Body RequestBody body);


    //设置安保问题
    @POST
    Observable<SetSecurityQuestionPB.Ret_PBIFE_securityquestionmanage_setSecurityQuestion> setProblem(@Url String url, @Body RequestBody body);


    //设置安保问题
    @POST
    Observable<VerifySecurityInfoPB.Ret_PBIFE_userbaseinfo_verifySecurityInfo> checkSmsVerifySecurityInfo(@Url String url, @Body RequestBody body);


    //检查
    @POST
    Observable<SecurityCheckUserInfoPB.Ret_PBIFE_securityquestionmanage_checkUserInfo> checkProblem(@Url String url, @Body RequestBody body);


    //下载问题
    @POST
    Observable<LoadSecurityQuestionPB.Ret_PBIFE_securityquestionmanage_loadSecurityQuestion> loadQuestion(@Url String url, @Body RequestBody body);


    //下载问题
    @POST
    Observable<SetSecurityQuestionPrePB.Ret_PBIFE_securityquestionmanage_setSecurityQuestionPre> submitQuestion(@Url String url, @Body RequestBody body);


    //获取资金流水列表数据
    @POST
    Observable<FundAccountLogPB.Ret_PBIFE_fund_queryFundAccountLog> getReserveListData(@Url String url, @Body RequestBody body);


    //验证身份信息
    @POST
    Observable<ForgetCheckUserNamePb.Ret_PBIFE_forgetpasswordmanage_checkUsername> checkUserName(@Url String url, @Body RequestBody body);


    //验证身份信息
    @POST
    Observable<ForgetFindWaysInfoPb.Ret_PBIFE_forgetpasswordmanage_findWaysInfo> checkPhone(@Url String url, @Body RequestBody body);


    //发送短信验证码
    @POST
    Observable<ForgetSendMobileValidateCodePb.Ret_PBIFE_forgetpasswordmanage_sendMobileValidateCode> sendCode(@Url String url, @Body RequestBody body);

    //验证短信验证码
    @POST
    Observable<ForgetCheckMobileCodePb.Ret_PBIFE_forgetpasswordmanage_checkMobileCode> checkCode(@Url String url, @Body RequestBody body);


    //提交新密码
    @POST
    Observable<ForgetSetPasswordPb.Ret_PBIFE_forgetpasswordmanage_setPassword> setPassword(@Url String url, @Body RequestBody body);

    //获取用户基本数据接口
    @POST
    Observable<UserBaseInfoPB.Ret_PBIFE_userinfomanage_userBaseInfo> getUserInfoData(@Url String url);

    //修改用户基本数据接口
    @POST
    Observable<ModifyUserBaseInfoPB.Ret_PBIFE_userinfomanage_modifyUserBaseInfo> modifyUserInfoData(@Url String url, @Body RequestBody body);


    //修改用户基本数据接口
    @POST
    Observable<Dictionary.Ret_PBAPP_dictionary> getDictionary(@Url String url, @Body RequestBody body);

    //图片上传成功以后还需通知后台截屏
    @POST
    Observable<SetUserHeaderPB.Ret_PBIFE_userheader_setUserHeader> setUserHeader(@Url String url, @Body RequestBody body);

    /**
     * 010假合格投资者
     * 004高净值承诺函
     * 000合格投资者申请
     * 003查询是否签署人脸协议
     */
    @POST
    Observable<Notices.Ret_PBAPP_notice> notice(@Url String url, @Body RequestBody body);

    //认证成为合格投资者
    @POST
    Observable<BecomeInvestor.Ret_PBIFE_userinfomanage_becomeInvestor> initBecomeInvestor(@Url String url, @Body RequestBody body);

    @POST
    Observable<UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo> getData(@Url String url);

    @POST
    Observable<DictDynamics.Ret_PBAPP_dictDynamic> dictDynamic(@Url String url, @Body RequestBody body);


    @POST
    Observable<HighNetWorthUpload.Ret_PBIFE_bankcardmanage_highNetWorthUpload> highNetWorthUpload(@Url String url, @Body RequestBody body);


    @POST
    Observable<AllAzjProto.PEARetIcons> requestMessageIcon(@Url String url, @Body RequestBody body);


    @POST
    Observable<DelegateCancelPb.Ret_PBIFE_trade_delegateCancel> cancel(@Url String url, @Body RequestBody body);


    //获取数据
    @POST
    Observable<CreateTransferOrderPB.Ret_PBIFE_trade_createTransferOrder> getDate(@Url String url, @Body RequestBody body);


    @POST
    Observable<Notices.Ret_PBAPP_notice> offline(@Url String url, @Body RequestBody body);

    //材料解绑
    @POST
    Observable<UnbindBankCard.Ret_PBIFE_bankcardmanage_unbindBankCardUpload> unBindHighNetWorthUpload(@Url String url, @Body RequestBody body);

    //材料换卡申请
    @POST
    Observable<ChangeBankCardUpload.Ret_PBIFE_bankcardmanage_changeBankCardUpload> changeBankCardUpload(@Url String url, @Body RequestBody body);


    //换卡获取短信验证码
    @POST
    Observable<BankSmsCheckCod.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4changeBind> changeCodeSmsCode(@Url String url, @Body RequestBody body);


    //换卡获取短信验证码
    @POST
    Observable<BankCardChangeManage.Ret_PBIFE_bankcardmanage_changeBankCard> onChangeBank(@Url String url, @Body RequestBody body);

//    //获取信息
//    @POST
//    Observable<Dictionary.Ret_PBAPP_dictionary>offlineRechargeDictionary(@Url String url, @Body RequestBody body);


    @POST
    Observable<FaceProtol.Ret_PBAPP_faceprotol> getFaceProtol(@Url String url, @Body RequestBody body);

    @POST
    Observable<LoadTcBindingBankInfo.Ret_PBIFE_bankcardmanage_loadTcBindingBankInfo> loadTcBindingBank(@Url String url, @Body RequestBody body);

    @POST
    Observable<PrdQuerySubscribeListPb.Ret_PBIFE_prdquery_prdQuerySubscribeList> loadData(@Url String url, @Body RequestBody body);

    //撤单预检测
    @POST
    Observable<SubscribeCancelPrePb.Ret_PBIFE_trade_subscribeCancelPre> cancelPre(@Url String url, @Body RequestBody body);


    //撤单
    @POST
    Observable<SubscribeCancelPb.Ret_PBIFE_trade_subscribeCancel> cancelPlay(@Url String url, @Body RequestBody body);


    //红包查询
    @POST
    Observable<MyDiscount.Ret_PBIFE_kq_getMyDiscountPage> queryDiscount(@Url String url, @Body RequestBody body);


    //红包查询
    @POST
    Observable<Withdrawals.Ret_PBIFE_kq_kqWithdrawals> withdraw(@Url String url, @Body RequestBody body);


    ////获取用户基本信息
    @POST
    Observable<BasicInfo.Ret_PBIFE_userbaseinfo_getBasicInfo> getBasicInfo(@Url String url);


    @POST
    Observable<PBIFEPrdqueryPrdQueryTaUnitFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceById> sellGetDate(@Url String url, @Body RequestBody body);


    //挂卖金额测算
    @POST
    Observable<QueryTransferSellProfitsPB.Ret_PBIFE_trade_queryTransferSellProfits> sellProfits(@Url String url, @Body RequestBody body);

    //挂卖发送短信验证码
    @POST
    Observable<SMSTransferRateLowSendValidateCode.Ret_PBIFE_smsValidateCode_transferRateLowSendValidateCode> onSellSendSms(@Url String url, @Body RequestBody body);

    //挂卖校验短信验证码
    @POST
    Observable<CheckSmsValidateCodeWithInnerMobile.Ret_PBIFE_smsValidateCode_checkSmsValidateCodeWithInnerMobile> onVerifySmsCode(@Url String url, @Body RequestBody body);


    @POST
    Observable<CheckIsDayCut.Ret_PBIFE_systemcheck_checkIsDayCut> getCheckIsDayCut(@Url String url);


    //查询银行卡渠道
    @POST
    Observable<QueryBankInfo.Ret_PBIFE_bankcardmanage_queryBankInfo> getCheckBankType(@Url String url, @Body RequestBody body);


    /**
     * 查询银行渠道
     * 通过payChannelNo渠道号去查询银行渠道号
     */
    @POST
    Observable<FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo> queryFundBankInfo(@Url String url, @Body RequestBody body);

    //发送短信验证码
    @POST
    Observable<AcquireBankSmsCheckCode.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode> requestAddBankNeedSms(@Url String url, @Body RequestBody body);

    //确认绑卡
    @POST
    Observable<AddBankCard.Ret_PBIFE_bankcardmanage_addBankCard> AddBankCard(@Url String url, @Body RequestBody body);


    //获取银行卡信息
    @POST
    Observable<PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard> getBankInfo(@Url String url);

    //解绑银行卡方式
    @POST
    Observable<PBIFEBankcardmanageDeleteBankCardPrere.Ret_PBIFE_bankcardmanage_deleteBankCardPrere> deleteBankCardPrere(@Url String url, @Body RequestBody body);


    //换卡方式
    @POST
    Observable<PBIFEBankcardmanageChangeBankCardPre.Ret_PBIFE_bankcardmanage_changeBankCardPre> changeBankCardPrere(@Url String url, @Body RequestBody body);


    //解绑银行卡
    @POST
    Observable<DeleteBankCard.Ret_PBIFE_bankcardmanage_deleteBankCard> deleteBankCard(@Url String url, @Body RequestBody body);


    //审核通过解绑银行卡
    @POST
    Observable<PBIFEBankcardmanageUnbindBankCardForUserOper.Ret_PBIFE_bankcardmanage_unbindBankCardForUserOper> deleteBankCardForUserOper(@Url String url, @Body RequestBody body);

    //强制解绑银行卡
    @POST
    Observable<BankCardManageUnBindBankCard.Ret_PBIFE_bankcardmanage_unbindBankCard> faceDeleteBank(@Url String url, @Body RequestBody body);


    @POST
    Observable<PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind> unBindBankCardSMS(@Url String url, @Body RequestBody bod);

    //查询解绑卡失败原因
    @POST
    Observable<UserUnbindCardInfo.Ret_PBIFE_bankcardmanage_getUserUnbindCardInfo> getUserUnbindCardInfo(@Url String url, @Body RequestBody body);

    //查询换卡失败原因
    @POST
    Observable<UserChangeCardInfo.Ret_PBIFE_bankcardmanage_getUserChangeCardInfo> getUserChangeCardInfo(@Url String url, @Body RequestBody body);

    //查询换卡失败原因
    @POST
    Observable<Object> test(@Url String url, @Body RequestBody body);


    //取消解绑申请
    @POST
    Observable<CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication> cleanUnbindBankCard(@Url String url, @Body RequestBody body);

    //校检支付密码
    @POST
    Observable<PBIFEUserinfomanageCheckTradePassword.Ret_PBIFE_userinfomanage_checkTradePassword> checkPlayPassWord(@Url String url, @Body RequestBody body);


    @POST
    Observable<TradePassword.Ret_PBIFE_passwordmanage_setTradePassword> setTradePassword(@Url String url, @Body RequestBody body);


    //获取省份信息
    @POST
    Observable<LoadProvince.Ret_PBIFE_chinacity_loadProvince> province(@Url String url);


    //根据省份pno获取城市
    @POST
    Observable<LoadCity.Ret_PBIFE_chinacity_loadCity> city(@Url String url, @Body RequestBody body);


    //根据省份，城市查询支行信息
    @POST
    Observable<LoadTmbBankInfo.Ret_PBIFE_bankcardmanage_loadTmbBankInfo> queryBranchName(@Url String url, @Body RequestBody body);

    //发送短信验证码
    @POST
    Observable<AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge> sendRechargeSms(@Url String url, @Body RequestBody body);


    //充值
    @POST
    Observable<PBIFEFundRecharge.Ret_PBIFE_fund_recharge> fundRecharge(@Url String url, @Body RequestBody body);

    @POST
    Observable<AllAzjProto.PEARetBankPic> getSupportBankIcon(@Url String url, @Body RequestBody body);


    @POST
    Observable<LoadWithDrawBankInfo.Ret_PBIFE_fund_loadWithDrawBankInfo> queryWithDrawBankInfo(@Url String url);


    //提现
    @POST
    Observable<WithDraw.Ret_PBIFE_fund_withDraw> withDrawal(@Url String url, @Body RequestBody body);

    @POST
    Observable<UserBankInfo.Ret_PBIFE_bankcardmanage_queryUserBankInfo> onUserBankInfo(@Url String url, @Body RequestBody body);

    //卡券是否可用
    @POST
    Observable<WithdrawalsCoupon.Ret_PBAPP_kqWithdrawals> getWithdrawalsCoupon(@Url String url, @Body RequestBody body);


    //获取数据
    @POST
    Observable<PrdQueryTaUnitFlowPb.Ret_PBIFE_prdquery_prdQueryTaUnitFlow> holdLoadData(@Url String url, @Body RequestBody body);


    //我的委托
    @POST
    Observable<PrdQueryTcDelegationFinanceListPb.Ret_PBIFE_prdquery_prdQueryTcDelegationFinanceList> myEntrustLoadData(@Url String url, @Body RequestBody body);


    //我的持仓
    @POST
    Observable<PrdQueryTaUnitFinanceNewPb.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceNew> myHoldLoadData(@Url String url, @Body RequestBody body);

    //付息明细
    @POST
    Observable<PrdQueryInterestPayDetailsPb.Ret_PBIFE_prdquery_prdQueryInterestPayDetails> payInterestLoadData(@Url String url, @Body RequestBody body);


    //产品购买预检查
    @POST
    Observable<ProductSec.Ret_PBIFE_trade_subscribeProductSec> productBuyCheck(@Url String url, @Body RequestBody body);


    //购买金额初始化及查询卡券
    @POST
    Observable<QueryPayInit.Ret_PBIFE_trade_queryPayInit> queryPayInit(@Url String url, @Body RequestBody body);


    //查询卡券
    @POST
    Observable<QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan> queryMyKqQuan(@Url String url, @Body RequestBody body);

    //转让购买
    @POST
    Observable<PBIFETradeTransferOrder.Ret_PBIFE_trade_transferOrder> playTransferDetail(@Url String url, @Body RequestBody body);


    //获取转让产品具体信息
    @POST
    Observable<PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail> getTransferDetailInfo(@Url String url, @Body RequestBody body);


    //获取spv产品详情
    @POST
    Observable<PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail> getSvpProductDetail(@Url String url, @Body RequestBody body);




    //请求本金信息
    @POST
    Observable<TransferBuyProfits.Ret_PBIFE_trade_queryTransferBuyProfits> getEarnings(@Url String url, @Body RequestBody body);


    //65周岁检查
    @POST
    Observable<TransferOrderPre.Ret_PBIFE_trade_transferOrderPre> getTransferOrderPre(@Url String url, @Body RequestBody body);


    //确购买买预检查
    @POST
    Observable<TransferOrderSec.Ret_PBIFE_trade_transferOrderSec> getTransferOrderSec(@Url String url, @Body RequestBody body);


    //询是否需要短信验证码接口  只有银行卡参与支付的过程才需要调用
    @POST
    Observable<QueryUserAccountDetail.Ret_PBIFE_fund_queryUserAccountDetail> querySms(@Url String url);


    //预约支付购买
    @POST
    Observable<ProductOrderPay.Ret_PBIFE_trade_productOrderPay> playOrder(@Url String url, @Body RequestBody body);

    //成交记录
    @POST
    Observable<PrdQueryTcDeliveryListPb.Ret_PBIFE_prdquery_prdQueryTcDeliveryList> recordTransactionLoadData(@Url String url, @Body RequestBody body);


    //交易记录
    @POST
    Observable<PBIFEPrdqueryQueryUnifyPurchaseTradeList.Ret_PBIFE_prdquery_queryUnifyPurchaseTradeList> getTransactionRecord(@Url String url, @Body RequestBody body);


    //推荐明细
    @POST
    Observable<RecommendDetailsInfoPB.Ret_PBIFE_friendsrecommend_recommendDetailsInfo> getMyInvitationListData(@Url String url, @Body RequestBody body);


    //我的邀请
    @POST
    Observable<UserRecommendInfoPB.Ret_PBIFE_friendsrecommend_userRecommendInfo> getMyInvitationData(@Url String url);

    //我的消息
    @POST
    Observable<ListMessagePB.Ret_PBIFE_messagemanage_listMessage> getMyMessageListData(@Url String url, @Body RequestBody body);

    //修改未读消息个数方法
    @POST
    Observable<ReadMessagePB.Ret_PBIFE_messagemanage_readMessageBatch> setReadMessage(@Url String url, @Body RequestBody body);

    //购买购买
    @POST
    Observable<ProductPlay.Ret_PBIFE_trade_rengou> playProduct(@Url String url, @Body RequestBody body);

    //产品预约模块 获取长期或者短期列表数据
    @POST
    Observable<ProductOrderInfoPB.Ret_PBIFE_trade_queryProductOrderInfo> reserveListGetReserveListData(@Url String url, @Body RequestBody body);

    //长期或者短期列表数据点击请求
    @POST
    Observable<ProductOrderValidate.Ret_PBIFE_trade_productOrderValidate> orderValidate(@Url String url, @Body RequestBody body);


    //获取长期或者短期详情
    @POST
    Observable<ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail> getReserveListDetailData(@Url String url, @Body RequestBody body);

    //预约金额检查接口
    @POST
    Observable<ProductReservePrePB.Ret_PBIFE_trade_queryProductOrderDeposit> reserveProductPre(@Url String url, @Body RequestBody body);

    //预约接口
    @POST
    Observable<ProductReservePB.Ret_PBIFE_trade_queryProductOrderTips> reserveProduct(@Url String url, @Body RequestBody body);


    //获取我的预约列表数据
    @POST
    Observable<PBIFETradeQueryMyOrderList.Ret_PBIFE_trade_queryMyOrderList> myReserveGetReserveListData(@Url String url, @Body RequestBody body);

    //取消预约预检查接口
    @POST
    Observable<CancelOrderPrePB.Ret_PBIFE_trade_cancelOrderPre> cancelOrderPre(@Url String url, @Body RequestBody body);

    //取消预约接口
    @POST
    Observable<CancelOrderPB.Ret_PBIFE_trade_cancelOrder> cancelOrder(@Url String url, @Body RequestBody body);

    //我的预约列表立即购买产品详情
    @POST
    Observable<ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails> requestPrdQueryProductDetails(@Url String url, @Body RequestBody body);

    //查询产品状态信息--是否可购买
    @POST
    Observable<ProductPre.Ret_PBIFE_trade_subscribeProductPre> checkProductCState(@Url String url, @Body RequestBody body);


    //查询产品状态信息--是否可购买
    @POST
    Observable<ProductOrderValidate.Ret_PBIFE_trade_productOrderValidate> productOrderValidateState(@Url String url, @Body RequestBody body);


    //检测手机号是否被注册
    @POST
    Observable<CheckIfMobileUseful.Ret_PBIFE_reg_checkIfMobileUseful> checkMobile(@Url String url, @Body RequestBody body);

    //获取短信验证码
    @POST
    Observable<RegistrationSMS.Ret_PBIFE_smsvalidatecode_activityRegistration> mobileNumberCode(@Url String url, @Body RequestBody body);

    //注册
    @POST
    Observable<Login.Ret_PBAPP_login> register(@Url String url, @Body RequestBody body);

    //查询网易盾
    @POST
    Observable<QuerySystemDict.Ret_PBIFE_query_querySystemDict> initCaptcha(@Url String url, @Body RequestBody body);

    //退出登录
    @POST
    Observable<LoginOut.Ret_PBIFE_logout> outLogin(@Url String url, @Body RequestBody body);


    //我的账户聚合接口
    @POST
    Observable<AllAzjProto.PWRetMerges> queryAccount(@Url String url, @Body RequestBody body);

    //获取总收益列表数据
    @POST
    Observable<QueryFundEarningsLog.Ret_PBIFE_statistic_queryFundEarningsLog> getListData(@Url String url, @Body RequestBody body);

    //获取更多列表的数据
    @POST
    Observable<UrlParams.Ret_PBAPP_urlparams> getMoreList(@Url String url);

    @POST
        //意见反馈点击请求获取url 或者我的邀请点击
    Observable<AllAzjProto.PEARetUrl> getFeedbackUrl(@Url String url, @Body RequestBody body);

    @POST
        //意见反馈点击请求获取brokerNo
    Observable<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo> getBrokerNo(@Url String url, @Body RequestBody body);

    //附件下载
    @GET
    Observable<ResponseBody> attachment(@Url String fileUrl);

    @Streaming
    @GET
    /**
     * 大文件官方建议用 @Streaming 来进行注解，不然会出现IO异常，小文件可以忽略不注入
     */
    Observable<ResponseBody> downloadFile(@Url String url);


    @Streaming
    @GET
    Observable<ResponseBody> downloadImage(@Url String fileUrl);


    @POST
    Observable<ImageUploadBean> upLoadPicImage(@Url String url, @Body MultipartBody image);

    @POST
    Observable<FaceIdCardBean> idCardImage(@Url String url, @Body MultipartBody image);

    @POST
    Observable<ResponseBody> request(@Url String url, @Body MultipartBody image);

    @Multipart
    @POST
    Observable<FaceResultBean> faceLiveness(@Url String url, @Part List<MultipartBody.Part> partList);

    /**
     * @param url 地址
     *            腾讯人脸识别
     */
    @POST
    Observable<TencentFace.Ret_PBAPP_tencentface> onTencentFace(@Url String url, @Body RequestBody body);

    /**
     * @param url 地址
     *            腾讯Ocr识别
     */
    @POST
    Observable<TencentOcrId.Ret_PBAPP_tencentID> onTencentOcrId(@Url String url);


    /**
     * @param url 地址
     *            腾讯Ocr识别
     */
    @POST
    Observable<TencentFaceCallback.Ret_PBAPP_tencentface_callback> onTencentFaceCallback(@Url String url, @Body RequestBody body);

    /**
     * 身份验证
     *
     * @param url  地址
     * @param body 请求参数
     */
    @POST
    Observable<PBIFEUserinfomanageUpdateCertificateDetail.Ret_PBIFE_userinfomanage_updateCertificateDetail> onUserIdCardUpload(@Url String url, @Body RequestBody body);

    /**
     * 职业查询
     *
     * @param url  地址
     * @param body 请求参数
     */
    @POST
    Observable<CareerEnumTypeCom.Ret_PBIFE_userbaseinfo_getEnumTypeCom> onCareerEnumTypeCom(@Url String url, @Body RequestBody body);


    /**
     * 职业上送
     *
     * @param url  地址
     * @param body 请求参数
     */
    @POST
    Observable<UpdateProfession.Ret_PBIFE_userinfomanage_updateProfession> onCommitUpdateProfession(@Url String url, @Body RequestBody body);

    //上送文件
    @Multipart
    @POST
    Observable<ImageUploadBean> updateUserPhoto(@Url String url, @Part List<MultipartBody.Part> partList);


    @POST
    Observable<ResponseBody> request(@Url String url);

    //统计点击上传
    @POST
    Observable<StatisticsDataProtoBuf.Ret_PBAPP_statisticsDataNew> statisticsData(@Url String url, @Body RequestBody body);


    @GET
    Observable<ResponseBody> getHtmlContent(@Url String url);

    //图片下载
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);

    //启动app上传奔溃日志
    @POST
    Observable<JsonObject> upLoadFile(@Url String url, @Body RequestBody requestBodyMap);

    //基金持仓详情
    @POST
    Observable<PrdQueryTaUnitFundFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFundFinanceById> holdingFundDetailData(@Url String url, @Body RequestBody body);

    //上传日志
    @POST
    Observable<AllAzjProto.PEARetBatchAppLogs> upLoadUserOperation(@Url String url, @Body RequestBody body);

    //上送步数
    @POST
    Observable<StatisticsStepsPB.Ret_PBAPP_StatisticsSteps> upLoadSteps(@Url String url, @Body RequestBody body);

    //获取WebView类型
    @POST
    Observable<PBAPPAndNativeWhiteScreen.Ret_PBAPP_andNative_whiteScreen> getWebViewType(@Url String url, @Body RequestBody body);
}
