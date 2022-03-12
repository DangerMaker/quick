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
public class UserAgreementSetting {

    @Id
    long id = 0;

    //用户账号
    public String account;

    //隐私协议版本号
    public String agreementVersion;


    public static String getAgreementVersion(String account) {
        Box<UserAgreementSetting> userBox = ObjectBox.getBoxStore().boxFor(UserAgreementSetting.class);
        UserAgreementSetting setting = userBox.query().equal(UserAgreementSetting_.account, account).build().findFirst();
        if (setting == null) {
            return null;
        }
        return setting.agreementVersion;
    }

    public static void setAgreementVersion(String account, String version) {
        Box<UserAgreementSetting> userBox = ObjectBox.getBoxStore().boxFor(UserAgreementSetting.class);
        UserAgreementSetting setting = userBox.query().equal(UserAgreementSetting_.account, account).build().findFirst();
        if (setting == null) {
            setting = new UserAgreementSetting();
            setting.account = account;
        }
        setting.agreementVersion = version;
        userBox.put(setting);
    }


    public static void putData(UserAgreementSetting userSetting) {
        Box<UserAgreementSetting> userInfoBox = ObjectBox.getBoxStore().boxFor(UserAgreementSetting.class);
        userInfoBox.put(userSetting);
    }

    public static UserAgreementSetting getUserSettingInfo() {

        Box<UserAgreementSetting> userInfoBox = ObjectBox.getBoxStore().boxFor(UserAgreementSetting.class);
        UserAgreementSetting userSetting = userInfoBox.query().build().findFirst();
        if (userSetting == null) {

            return new UserAgreementSetting();
        }
        return userSetting;
    }


}
