package com.hundsun.zjfae.common.user;

import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;

import java.util.Calendar;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;


/**
 * 用户设置信息
 **/
@Entity
public class UserSetting {

    @Id(assignable = true)
    long id = 1;

    //资金是否可见
    public boolean isUserAmountType = false;

    //上送app时间

    public String upLoadTime;

    //首页小图标是否更新
    public boolean ishomeIconsOutLogin;

    //理财专区是否需要更新
    public boolean ishomeSectionOutLogin;

    //是否记住账号
    public boolean isCheckUserName = true;

    //是否开启指纹
    public boolean fingerprint_state;

    //是否开启手势锁
    public boolean gesture_state;


    //是否更新头像
    public boolean isHeadPic;

    //手势密码
    public String fingerprint_password;

    //原始手势密码 没有加密之前
    public String fingerprint_password_before;

    //区块链密码
    public String blockchainPassWord;

    //原始区块链密码，未加密之前
    public String blockchainPassWord_before;

    //crash上送时间，一天一次
    public String crachUpLoadTime;

    //区块链设置
    public boolean blockchainState;






    public boolean isReplaceUser = true;

    //是否首次弹出隐私协议
    public boolean isAgreementShow = false;





    public static boolean isAgreementHasShow() {
        UserSetting setting = getUserSettingInfo();

        if (setting == null) {
            return false;
        }
        return setting.isAgreementShow;
    }

    public static void setAgreementShow(boolean show) {

        UserSetting setting = getUserSettingInfo();

        setting.isAgreementShow = show;

        putData(setting);
    }
    public static boolean isUserAmountVisible() {
        UserSetting setting = getUserSettingInfo();

        if (setting == null) {
            return false;
        }
        return setting.isUserAmountType;
    }

    public static void setUserAmountVisible(boolean amountVisible) {

        UserSetting setting = getUserSettingInfo();

        setting.isUserAmountType = amountVisible;

        putData(setting);
    }


    public static void putData(UserSetting userSetting) {
        Box<UserSetting> userInfoBox = ObjectBox.getBoxStore().boxFor(UserSetting.class);
        userInfoBox.put(userSetting);
    }

    public static UserSetting getUserSettingInfo() {

        Box<UserSetting> userInfoBox = ObjectBox.getBoxStore().boxFor(UserSetting.class);
        UserSetting userSetting = userInfoBox.query().build().findFirst();
        if (userSetting == null) {

            return new UserSetting();
        }
        return userSetting;
    }


    public static void removeData() {
        Box<UserSetting> userInfoBox = ObjectBox.getBoxStore().boxFor(UserSetting.class);
        UserSetting userSetting = getUserSettingInfo();

        boolean isCheckUserName = userSetting.isCheckUserName;
        boolean isAgreementShow = userSetting.isAgreementShow;


        userInfoBox.remove(userSetting);

        userSetting = new UserSetting();
        userSetting.isCheckUserName = isCheckUserName;
        userSetting.isAgreementShow = isAgreementShow;
        UserSetting.putData(userSetting);
    }

    //删除全部数据
    public static void removeAll(){
        Box<UserSetting> box = ObjectBox.getBoxStore().boxFor(UserSetting.class);
        box.removeAll();
    }


    public static boolean getCrachUpLoadTime() {

        UserSetting setting = getUserSettingInfo();

        String time = setting.crachUpLoadTime;

        final Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        buffer.append(year);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        buffer.append(month);
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        buffer.append(day);
        if (time == null) {

            return false;
        } else if (time.equals(buffer.toString())) {

            return true;
        }

        return false;
    }

    public static void setCrachUpLoadTime() {
        final Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        buffer.append(year);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        buffer.append(month);
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        buffer.append(day);
        UserSetting setting = getUserSettingInfo();
        setting.crachUpLoadTime = buffer.toString();
        putData(setting);
    }



    //首页四个圆圈缓存数据登录以后是否需要更新

    public static boolean ishomeIconsOutLogin() {
        UserSetting userSetting = getUserSettingInfo();
        return userSetting.ishomeIconsOutLogin;
    }


    public static void setishomeIconsOutLogin(boolean ishomeIconsOutLogin) {
        UserSetting userSetting = getUserSettingInfo();
        userSetting.ishomeIconsOutLogin = ishomeIconsOutLogin;
        putData(userSetting);
    }

    //首页理财中心缓存数据 登录以后是否需要更新
    public static boolean ishomeSectionOutLogin() {
        UserSetting userSetting = getUserSettingInfo();
        return userSetting.ishomeSectionOutLogin;
        //return getBooleanData("ishomeSectionOutLogin", false);
    }


    public static void setishomeSectionOutLogin(boolean ishomeSectionOutLogin) {
        UserSetting userSetting = getUserSettingInfo();
        userSetting.ishomeSectionOutLogin = ishomeSectionOutLogin;
        putData(userSetting);
    }


}
