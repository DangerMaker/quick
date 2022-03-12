package com.hundsun.zjfae.activity.home.view;

import com.hundsun.zjfae.activity.home.bean.ImageUploadBean;
import com.hundsun.zjfae.activity.home.bean.ImageUploadStateBean;
import com.hundsun.zjfae.common.base.BaseView;

public interface ImageUploadView extends BaseView {


    void requestImageUpload(ImageUploadBean uploadBean,int index);

    void requestNetWorthUpload(String returnCode,String returnMsg);

}
