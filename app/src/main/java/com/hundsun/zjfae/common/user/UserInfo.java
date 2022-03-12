package com.hundsun.zjfae.common.user;

import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

//用户详细信息
@Entity
public class UserInfo {
    public static final String SKIPANDNATIVE = "andNative";  //标记用户使用的WebView内核：原生浏览器
    public static final String SKIPTENCENT = "tencentSkip"; //标记哟用户使用的WebView内核：腾讯浏览器

    @Id(assignable = true)
    long id = 1;

    public String loginName;

    public String passWord;

    //用户手机号
    public String mobile;

    //用户资金账号
    public String funtAccount;

    //用户类型
    public String iconsUserType;

    //交易账号
    public String tradeAccount;

    //账户
    public String account;


    public String resTime;

    //标记用户使用的WebView内核：1、andNative(原生WebView，默认) 2、tencentSkip（腾讯X5内核）
    public String skipType;

    //是否开启区块链
    public String isOpenChain;

    //用户注册渠道
    public String brokerNo;

    /**
     * 用户类型 personal(个人)，company(机构)
     * */
    public String userType;


    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", mobile='" + mobile + '\'' +
                ", funtAccount='" + funtAccount + '\'' +
                ", iconsUserType='" + iconsUserType + '\'' +
                ", tradeAccount='" + tradeAccount + '\'' +
                ", account='" + account + '\'' +
                ", resTime='" + resTime + '\'' +
                ", skipType='" + skipType + '\'' +
                '}';
    }

    public static void putData(UserInfo userInfo) {

        String iconsUserType = userInfo.iconsUserType;
        String resTime = userInfo.resTime;
        String userType = userInfo.userType;
        UserInfo us = getUserData();
        if (us.loginName != null && !us.loginName.equals(userInfo.loginName)) {
            //用户不一致，删除所有表数据
            removeData();
            UserSetting.removeData();
            ChangeCard.removeAll();
            UnBindCard.removeAll();
            AssetProof.removeAll();
        }
        userInfo.iconsUserType = iconsUserType;
        userInfo.resTime = resTime;
        //userInfo.userType = userType;
        Box<UserInfo> userInfoBox = ObjectBox.getBoxStore().boxFor(UserInfo.class);
        userInfoBox.put(userInfo);
    }


    public static UserInfo getUserData() {
        Box<UserInfo> userInfoBox = ObjectBox.getBoxStore().boxFor(UserInfo.class);
        UserInfo userInfo = userInfoBox.query().build().findFirst();
        if (userInfo == null) {
            return new UserInfo();
        }
        return userInfo;
    }


    public static void removeData() {
        UserInfo userInfo = getUserData();
        Box<UserInfo> userInfoBox = ObjectBox.getBoxStore().boxFor(UserInfo.class);
        userInfoBox.remove(userInfo);
    }
}
