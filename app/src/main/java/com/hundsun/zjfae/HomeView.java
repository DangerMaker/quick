package com.hundsun.zjfae;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.PrivateNotice;


/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae
 * @ClassName:      HomeView
 * @Description:     首页回调View
 * @Author:         moran
 * @CreateDate:     2019/8/2 16:12
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/8/2 16:12
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface HomeView extends BaseView {


    /**
     * 获取头像接口
     * @param pic 拼接信息
     * @param fix 拼接信息
     * */
    void getPicDictionary(String pic, String fix);

    /**
     * 获取协议版本号
     * @param version 拼接信息
     * */
    void getAgreementVersion(String version);

    /**
     * 合格投资者审核不通过
     * @param highNetWorthInfo 审核不通过
     * @param isRealInvestor 是否是合格投资者
     * */
    void getUserHighNetWorthInfo(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo highNetWorthInfo, String isRealInvestor);

    /**
     * 用户详细信息
     * @param userDetailInfo 用户详细信息
     * */
    void getUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

    /**
     * App请求更新
     * @param appUpDate app更新信息
     *
     * */
    void onAppUpdate(AppUpDate appUpDate);

    /***
     * 更新接口异常
     * */
    void onAppUpdateError();

    /**
     * 退出登录
     * */
    void outLogin();


    /**
     * 登录成功
     * @param returnMsg  返回信息
     * @param loginMethod 登录模式
     * @param mobile 手机号
     * @param fundAccount 资金账号
     * @param userType 用户类型 personal(个人)，company(机构)
     * @param brokerNo 用户渠道编号
     *
     * */
    void onLoginSuccess(String returnMsg,String mobile,String loginMethod,String fundAccount,String userType, String brokerNo);


    /**
     * 是否需要校验验证码
     * @param needValidateAuthCode 是否需要校验验证码 0 不校检，1校检
     * */
    void setNeedValidateAuthCode(String needValidateAuthCode);

    /**
     * 图形验证码
     * @param bytes 图形验证码流
     * */
    void onImageCode(byte [] bytes);

    /**
     * 用户隐私
     * @param privacyNotice 接口回调数据
     * @param force 是否强制提醒 true-强制，false-非强制，""无效
     *
     * */
    void onUserPrivateNotice(PrivateNotice.PBAPP_privacyNotice.PrivacyNotice privacyNotice, String force);


    /**
     * 刷新图形验证码
     */
    void refreshImageAuthCode();
}
