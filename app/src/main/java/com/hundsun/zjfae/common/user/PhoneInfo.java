package com.hundsun.zjfae.common.user;

import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class PhoneInfo {


    @Id(assignable = true)
    long uid = 1;
    //手机号
    public String phoneNumber = "";

    public String  phoneNumberStatus = "3";

    //Wifi地址ip

    public String routingAddress = "";

    //App版本号
    public String clientOsver = "";

    //手机的型号 设备名称
    public String deviceModal = "";

    //DEVICE_ID
    public String deviceId = "";

    //用户id
    public String user_id = "";

    //平台
    public String p = "and";

    //平台
    public String platform = "android";


    //app版本号
    public String appVersion = BasePresenter.APPVERSION;

    //渠道信息
    public String channel = "";


    //增加修改数据
    public static void putData (PhoneInfo phoneInfo){

        Box<PhoneInfo> phoneInfoBox = ObjectBox.getBoxStore().boxFor(PhoneInfo.class);
        phoneInfoBox.put(phoneInfo);
    }


    public static PhoneInfo getPhoneInfo(){

        Box<PhoneInfo> phoneInfoBox = ObjectBox.getBoxStore().boxFor(PhoneInfo.class);

        PhoneInfo phoneInfo = phoneInfoBox.query().build().findFirst();

        if (phoneInfo == null){

            return new PhoneInfo();
        }

        return phoneInfo;
    }

}
