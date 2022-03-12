package com.zjfae.captcha.face;

/**
 * @ProjectName:
 * @Package:        com.zjfae.captcha.face
 * @ClassName:      TencentOCRCallBack
 * @Description:     腾讯OCR识别结果回调
 * @Author:         moran
 * @CreateDate:     2019/7/19 14:19
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/19 14:19
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface TencentOCRCallBack {

    void onSuccess(IDCardResult idCardResult);

    void onError(String errorMsg);

    void onLoginTimeOut(String errorMsg);

    void onTencentCallBack(String orderNo);
}
