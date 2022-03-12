package com.hundsun.zjfae.common.http.api;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.common.http.api
 * @ClassName:      ConstantCode
 * @Description:     业务Code码
 * @Author:         moran
 * @CreateDate:     2019/6/11 14:29
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/11 14:29
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class ConstantCode {

    //接口正常返回
    public static final String RETURN_CODE = "0000";
    //登录超时
    public static final String LOGIN_TIME_OUT = "000001";
    //1
    public static final String SERVER_EXCEPTION = "9999";


    //转让列表限流
    public static final int REFRESH_CODE = 503;

    //65周岁公告
    public static final String HIGH_AGE_CODE = "3326";

    //系统升级中
    public static final String SYSTEM_UPDATING = "I699999";

    //特殊名单：0225
    public static final String BLACK_LIST_CODE = "0225";

    //新客专享购买提示 -----3643
    public static final String NOVICE_CODE = "3643";
    //unknown
    public static final String UNKNOWN_CODE = "1999";


    //合格投资者申请 -----3629 ---Qualified member
    public static final String QUALIFIED_MEMBER_CODE = "3629";

    //暂不支持非合格投资者入金 -----3679 ---UnQualified member
    public static final String UNQUALIFIED_MEMBER_CODE = "3679";

    // 更新个人可购金额--- 3303 Purchase amount
    public static final String PURCHASE_AMOUNT_CODE = "3303";

    //风险等级不符 --3318，3319，3320

    public static final String RISK_ASSESSMENT_CODE = "3318";

    public static final String RISK_ASSESSMENT_CODE_1 = "3319";

    public static final String RISK_ASSESSMENT_CODE_2 = "3320";

    //高净值会员
    public static final String SENIOR_MEMBER_CODE = "3620";

    //购买产品不可转让
    public static final String NO_TRANSFER_CODE = "I799999";

    //购买金额错误----9998
    public static final String PAYMENT_AMOUNT_CODE = "9998";
    //未知code -----3317
    // 高净值 ----3620
    // 未知code -- 3629
    // 未知code --- 3303；
    // 未知code --- I799999；
    //接口请求超时 ---2888
    //网络异常 ---I29999
    //未知异常 ---1999
    /**
     * 用户信息不存在
     * */
    public static final String USER_INFO_NO_EXIST = "0009";
    /**
     *证件信息不一致
     * */
    public static final String USER_ID_CARD_ERROR = "2112";
    /**
     * 验证码不存在已过期
     * */
    public static final String IMAGE_CODE_ERROR = "i50002";
    /**
     * 验证码错误
     * */
    public static final String IMAGE_CODE_ERROR2 = "i50003";

    /**
     * 已经预约-- 3410 ---reservation
     * */
    public static final String RESEVATION_CODE = "3410";

    /**
     * 当日有资金流水 --- 2115
     * */
    public static final String CAPITAL_FLOW = "2115";

    /**
     * SMID不存在--- 1005
     * */
    public static final String SMID_NO_PRESENCE = "1005";


    //我要卖计算收益----1209
    public static final String INCOME = "1209";

    /**
     * 用户签署人脸协议
     * */
    public static final String FACE_AGREEMENT = "1208";

    /**
     * 系统维护
     * */
    public static final String NO_TRANSACTION_TIME = "6005";

    //1063 --- Withdrawal
    public static final String WITHDRAWAL_ERROR_CODE = "1063";
    /**
     * 交易密码错误
     * */
    public static final String PASSWORD_ERROR_CODE = "0012";

    /**
     * 受让支付引导用户去绑卡
     * */
    public static final String GUIDE_ADD_BANK_CODE = "0028";

    /**
     * 线下充值 2003
     * */
    public static final String RECHARGE_CODE = "2003";

    /**
     * 上海银行超时
     * */
    public static final String ADD_BANK_TIME_OUT = "2114";
    /**
     * 银行渠道暂停
     * */
    public static final String BANK_CHANNEL_PAUSE = "1066";
    /**
     * 银行渠道关闭
     * */
    public static final String BANK_CHANNEL_CLOSE = "1067";

    /**
     *
     * 机构用户未绑卡
     * **/
    public static final String WITH_DRAWAL_CODE = "2010";

    /**
     * 未设置交易密码
     * */
    public static final String NO_PLAY_PASSWORD = "1042";

    public static final String NO_PRODUCT_CODE_1 = "666666";



}
