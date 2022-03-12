package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.activity.home.bean.ImageUploadBean;
import com.hundsun.zjfae.activity.home.bean.ImageUploadStateBean;
import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

public interface UnbindBankCardView extends BaseView {

    void requestImageUpload(ImageUploadBean uploadBean, int index);

    void requestUnbindBankCard(String returnCode,String returnMsg);

    void requestChangeBankCard(String returnCode,String returnMsg);
}
