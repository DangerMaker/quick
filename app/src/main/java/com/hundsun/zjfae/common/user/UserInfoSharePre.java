package com.hundsun.zjfae.common.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hundsun.zjfae.activity.splash.StatisticsDataBean;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.SharedPreferenceAccesser;
import com.hundsun.zjfae.common.utils.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author moran
 */
public class UserInfoSharePre extends SharedPreferenceAccesser {




    public static void saveUserInfo(String userName, String passWord) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.loginName = userName;
        userInfo.passWord = passWord;
        UserInfo.putData(userInfo);
    }


    //用户交易账号
    public static void setTradeAccount(String tradeAccount) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.tradeAccount = tradeAccount;
        UserInfo.putData(userInfo);
    }

    public static String getTradeAccount() {
        UserInfo userInfo = UserInfo.getUserData();
        String tradeAccount = userInfo.tradeAccount;
        if (tradeAccount == null) {
            return "";
        }
        return tradeAccount;
    }


    public static void setUserName(String userName) {
        if (!getUserName().equals("")) {
            if (!userName.equals(getUserName())) {
                deleteData();
            }
        }


        UserInfo userInfo = UserInfo.getUserData();
        userInfo.loginName = userName;
        UserInfo.putData(userInfo);
    }


    public static String getUserName() {
        UserInfo userInfo = UserInfo.getUserData();
        String userName = userInfo.loginName;
        if (null == userName) {
            return "";
        }
        return userName;

    }

    //保存用户渠道编号
    public static void setBrokerNo(String brokerNo) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.brokerNo = brokerNo;
        UserInfo.putData(userInfo);
    }

    public static String getBrokerNo() {
        UserInfo userInfo = UserInfo.getUserData();
        String brokerNo = userInfo.brokerNo;
        if (null == brokerNo) {
            return "";
        }
        return brokerNo;
    }

    /**
     * 设置用户使用浏览器内核开关
     *
     * @param skip
     */
    public static void setSkip(String skip) {
        CCLog.e("TAGTAG", skip);
        if (!StringUtils.isNotBlank(skip)) {
            return;
        }
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.skipType = skip;
        UserInfo.putData(userInfo);
        CCLog.e("TAGTAG", getData().toString());
        CCLog.e("TAGTAG", getSkip());
    }

    /**
     * 获取用户使用浏览器内核开关
     *
     * @return
     */
    public static String getSkip() {
        UserInfo userInfo = UserInfo.getUserData();
        if (userInfo == null) {
            return null;
        }

        return userInfo.skipType;
    }

    //存储区块链开启状态
    public static void  setIsOpenChain(String isOpenChain){

        UserInfo userInfo = UserInfo.getUserData();

        userInfo.isOpenChain = isOpenChain;

        UserInfo.putData(userInfo);
    }

    public static boolean isOpenChain(){

        UserInfo userInfo = UserInfo.getUserData();

        String isOpenChain = userInfo.isOpenChain;

        if (isOpenChain != null && isOpenChain.equals("1")){

            return true;
        }

        return false;
    }



    public static String getPassWord() {
        UserInfo userInfo = UserInfo.getUserData();
        String passWord = userInfo.passWord;
        if (null == passWord) {
            return "";
        }

        return passWord;
    }

    public static void setPassWord(String passWord) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.passWord = passWord;
        UserInfo.putData(userInfo);
    }

    //获取加密的手势密码--校验作用
    public static String getGessturePwd() {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        String fingerprintPassWord = userSetting.fingerprint_password;
        if (null == fingerprintPassWord) {
            return "";
        }

        return fingerprintPassWord;
    }

    //加密的手势密码--校验作用
    public static void setGessturePwd(String fingerprintPassWord) {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        userSetting.fingerprint_password = fingerprintPassWord;
        UserSetting.putData(userSetting);
    }

    //明文的手势密码---登录上送
    public static String getFingerprintPassWordBefore() {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        String fingerprintPassWord = userSetting.fingerprint_password_before;
        if (null == fingerprintPassWord) {
            return "";
        }

        return fingerprintPassWord;
    }

    //获取明文的手势密码--登录上送
    public static void setFingerprintPassWordBefore(String beforeFingerprintPassWord) {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        userSetting.fingerprint_password_before = beforeFingerprintPassWord;
        UserSetting.putData(userSetting);
    }


    //用于校验区块链手势密码
    public static String getBlockchainGessturePwd() {
        UserSetting userSetting = UserSetting.getUserSettingInfo();

        String blockchainPassWord = userSetting.blockchainPassWord;

        if (null == blockchainPassWord) {
            return "";
        }

        return blockchainPassWord;
    }


    //存储本地校验区块链密码
    public static void setBlockchainGessturePwd(String blockchainPassWord) {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        userSetting.blockchainPassWord = blockchainPassWord;
        UserSetting.putData(userSetting);

    }


    //获取区块链密码--上送JS交互
    public static String getBlockchainGessturePwdBefore() {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        String blockchainPassWord_before = userSetting.blockchainPassWord_before;

        if (null == blockchainPassWord_before) {
            return "";
        }

        return blockchainPassWord_before;
    }


    //存储明文区块链密码--上送JS交互
    public static void setBlockchainGessturePwdBefore(String beforeBlockchainGessturePassWord) {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        userSetting.blockchainPassWord_before = beforeBlockchainGessturePassWord;
        UserSetting.putData(userSetting);
    }






    //是否切换账号
    public static boolean isReplaceUser() {

        UserSetting userSetting = UserSetting.getUserSettingInfo();

        return userSetting.isReplaceUser;
    }

    public static void setReplaceUser(boolean isReplaceUser) {

        UserSetting userSetting = UserSetting.getUserSettingInfo();

        userSetting.isReplaceUser = isReplaceUser;

        UserSetting.putData(userSetting);
    }


    public static void setAccount(String account) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.account = account;
        UserInfo.putData(userInfo);
    }

    public static String getAccount() {
        UserInfo userInfo = UserInfo.getUserData();
        String account = userInfo.account;
        if (null == account) {
            return "";
        }

        return account;
    }

    public static void setMobile(String mobile) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.mobile = mobile;
        UserInfo.putData(userInfo);
    }

    public static String getMobile() {

        UserInfo userInfo = UserInfo.getUserData();
        String mobile = userInfo.mobile;
        if (null == mobile) {
            return "";
        }

        return mobile;
    }

    public static void setFundAccount(String fundAccount) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.funtAccount = fundAccount;
        UserInfo.putData(userInfo);
    }

    public static String getFundAccount() {
        UserInfo userInfo = UserInfo.getUserData();
        String fundAccount = userInfo.funtAccount;
        if (null == fundAccount) {
            return "";
        }

        return fundAccount;
    }

    public static void saveUserType(String userType){

        UserInfo userInfo = UserInfo.getUserData();

        userInfo.userType = userType;

        UserInfo.putData(userInfo);
    }


    /**
     * 用户类型 personal(个人)，company(机构)
     * */
    public static String getUserType(){
        UserInfo userInfo = UserInfo.getUserData();
        String userType = userInfo.userType;

        if (userType == null){
            return "";
        }

        return userType;

    }


    public static void saveUserNameType(boolean isMemorization) {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        userSetting.isCheckUserName = isMemorization;
        UserSetting.putData(userSetting);
    }


    public static boolean getUserNameType() {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        return userSetting.isCheckUserName;
    }

    //    判断是否开启指纹登录
    public static void saveFingerprintLoginType(boolean fingerprintLogin) {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        userSetting.fingerprint_state = fingerprintLogin;
        UserSetting.putData(userSetting);

    }

    public static boolean getFingerprintLogin() {

        UserSetting userSetting = UserSetting.getUserSettingInfo();

        return userSetting.fingerprint_state;
    }

    //    判断是否开启手势登录
    public static void saveGestureLoginType(boolean gesture_state) {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        userSetting.gesture_state = gesture_state;
        UserSetting.putData(userSetting);

    }

    public static boolean getGestureLoginType() {
        UserSetting userSetting = UserSetting.getUserSettingInfo();

        return userSetting.gesture_state;
    }

    //    判断是否修改头像
    public static void saveHeadPic(boolean isHeadPic) {
        UserSetting userSetting = UserSetting.getUserSettingInfo();
        userSetting.isHeadPic = isHeadPic;
        UserSetting.putData(userSetting);

    }

    public static boolean getHeadPic() {

        UserSetting userSetting = UserSetting.getUserSettingInfo();

        return userSetting.isHeadPic;
    }


    //保存区块链开启状态
    public static void saveBlockchainState(boolean isState) {

        UserSetting userSetting = UserSetting.getUserSettingInfo();

        userSetting.blockchainState = isState;

        UserSetting.putData(userSetting);
    }

    //获取区块链开启状态

    public static boolean getBlockchainState() {

        UserSetting userSetting = UserSetting.getUserSettingInfo();


        return userSetting.blockchainState;
    }

    //    判断切换用户重新登录的时候是否需要
    public static void saveIconsUserType(String IconsUserType) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.iconsUserType = IconsUserType;
        UserInfo.putData(userInfo);

    }

    public static String getIconsUserType() {
        UserInfo userInfo = UserInfo.getUserData();

        String IconsUserType = userInfo.iconsUserType;
        if (null == IconsUserType) {
            return "";
        }

        return IconsUserType;
    }

    // 判断app一天中是否已经弹过广告
    public static void saveResTime(String resTime) {
        UserInfo userInfo = UserInfo.getUserData();
        userInfo.resTime = resTime;
        UserInfo.putData(userInfo);

    }

    public static String getResTime() {
        UserInfo userInfo = UserInfo.getUserData();
        String resTime = userInfo.resTime;
        if (null == resTime) {
            return "";
        }

        return resTime;
    }


    //    保存本地存储的点击事件类型
    public static void saveStatistticsData(String adsUuid) {
        String statistticsData = "";
        List<StatisticsDataBean> list = new ArrayList<>();
        Gson gson = new Gson();
        if (StringUtils.isNotBlank(getStringData("StatistticsData"))) {//说明之前保存过点击数量
            Type listType = new TypeToken<List<StatisticsDataBean>>() {
            }.getType();
            list = gson.fromJson(getStringData("StatistticsData"), listType);
            int size = 0;

            for (StatisticsDataBean dataBean : list) {
                if ( dataBean.getAdsUuid().equals(adsUuid) ) {
                    dataBean.setCount(Integer.parseInt(dataBean.getCount()) + 1 + "");
                    break;
                }
                size++;
            }

            if (size == list.size()) {
                StatisticsDataBean bean = new StatisticsDataBean();
                bean.setCount("1");
                bean.setAdsUuid(adsUuid);
                list.add(bean);
            }
        } else {
            StatisticsDataBean bean = new StatisticsDataBean();
            bean.setCount("1");
            bean.setAdsUuid(adsUuid);
            list.add(bean);
        }
        statistticsData = gson.toJson(list);
        saveStringData("StatistticsData", statistticsData);
    }

    public static String getStatistticsData() {

        return getStringData("StatistticsData");
    }

    public static void clearStatistticsData() {
        saveStringData("StatistticsData", "");
    }





    public static void deleteData() {
        clearAllData();
        //ADSQLiteHelperUtils.deleteData();
        UserInfo.removeData();
        UserSetting.removeData();
        ChangeCard.removeAll();
        UnBindCard.removeAll();
        AssetProof.removeAll();
        //CacheConfiguration.deleteData();
    }

    public static UserInfo getData() {
        return UserInfo.getUserData();
    }
}
