package com.hundsun.zjfae.activity.home.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.UserBaseInfoPB;

/**
 * @Description:个人基本信息界面（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface UserInfoView extends BaseView {
    void getUserInfoData(UserBaseInfoPB.PBIFE_userinfomanage_userBaseInfo data);

    void modifyUserInfoData(String code, String message, int type, String data);

    void getPicDictionary(String pic,String fix);

    void upLoadPicImage(String code,String msg);

    void setUserHeader(String code,String msg);

}
