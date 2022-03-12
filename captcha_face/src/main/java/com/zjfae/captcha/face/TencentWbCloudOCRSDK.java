package com.zjfae.captcha.face;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.webank.mbank.ocr.WbCloudOcrSDK;
import com.webank.mbank.ocr.net.EXIDCardResult;

/**
 * @ProjectName:
 * @Package: com.zjfae.captcha.face
 * @ClassName: TencentWbCloudOCRSDK
 * @Description: 腾讯身份证OCR识别
 * @Author: moran
 * @CreateDate: 2019/7/18 18:53
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/7/18 18:53
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class TencentWbCloudOCRSDK {

    private static TencentWbCloudOCRSDK tencentWbCloudOcrSDK = null;

    private Context mContext;


    public static TencentWbCloudOCRSDK getInstance() {
        if (tencentWbCloudOcrSDK == null) {
            tencentWbCloudOcrSDK = new TencentWbCloudOCRSDK();
        }
        return tencentWbCloudOcrSDK;
    }

    private TencentWbCloudOCRSDK() {

    }

    public TencentWbCloudOCRSDK init(Context context) {

        this.mContext = context;
        return this;
    }


    public void execute(final String orderNo, String appId, String nonce, String userId, String sign, final TencentOCRCallBack ocrCallBack, final boolean ocrTypeMode) {
        Bundle data = new Bundle();
        WbCloudOcrSDK.InputData inputData = new WbCloudOcrSDK.InputData(
                orderNo,
                appId,
                "1.0.0",
                nonce,
                userId,
                sign,
                "ip=xxx.xxx.xxx.xxx",
                "lgt=xxx,xxx;lat=xxx.xxx");
        data.putSerializable(WbCloudOcrSDK.INPUT_DATA, inputData);
        //个性化参数设置,可以不设置，不设置则为默认选项。
        //此处均设置为和默认设置不同
        data.putString(WbCloudOcrSDK.TITLE_BAR_COLOR, "#409eff");
        //设置 SDK 标题栏文字内容，默认展示身份证识别,此处设置为居民身份证识别data.putString(WbCloudOcrSDK.TITLE_BAR_CONTENT, "居民身份证识别");
        //设置 SDK 水印文字内容，默认仅供内部业务使用，此处设置为仅供本次业务使用
        data.putString(WbCloudOcrSDK.WATER_MASK_TEXT, "仅供本次业务使用");
        //设置扫描识别的时间上限,默认 20 秒，建议默认
        data.putLong(WbCloudOcrSDK.SCAN_TIME, 20000);
        WbCloudOcrSDK.getInstance().init(mContext, data, new WbCloudOcrSDK.OcrLoginListener() {
            @Override
            public void onLoginSuccess() {
                WbCloudOcrSDK.getInstance().startActivityForOcr(mContext, new WbCloudOcrSDK.IDCardScanResultListener() {
                    @Override
                    public void onFinish(String resultCode, String resultMsg) {
                        if ("0".equals(resultCode)) {
                            EXIDCardResult cardResult = WbCloudOcrSDK.getInstance().getResultReturn();
                            if (cardResult != null) {
//                                String warning = "";
//                                if (ocrTypeMode && cardResult.frontMultiWarning != null && cardResult.frontMultiWarning.length() > 7) {
//                                    warning = cardResult.frontMultiWarning;
//                                } else if (!ocrTypeMode && cardResult.backMultiWarning != null && cardResult.backMultiWarning.length() > 7) {
//                                    warning = cardResult.backMultiWarning;
//                                }
//                                if (warning.length() > 0) {
//                                    String msg = "";
//                                    if ((warning.charAt(0) + "").equals("1")) {
//                                        msg = "身份证边框不完整, 请重新上传";
//                                    } else if ((warning.charAt(1) + "").equals("1")) {
//                                        msg = "您使用的是身份证复印件, 请使用身份证原件上传。";
//                                    } else if ((warning.charAt(2) + "").equals("1")) {
//                                        msg = "身份证日期不合法, 请检查后重试。";
//                                    } else if ((warning.charAt(3) + "").equals("1")) {
//                                        msg = "您使用的是身份证翻拍件, 请使用身份证原件上传。";
//                                    } else if ((warning.charAt(4) + "").equals("1")) {
//                                        msg = "您使用的是临时身份证, 请使用正式身份证上传。";
//                                    } else if ((warning.charAt(5) + "").equals("1")) {
//                                        msg = "身份证框内被遮挡, 请重新上传。";
//                                    }
//                                    if (!TextUtils.isEmpty(msg)) {
//                                        ocrCallBack.onError(msg);
//                                        return;
//                                    }
//                                }
                                ocrCallBack.onTencentCallBack(orderNo);
                                IDCardResult idCardResult = IDCardResult.getInstance();
                                idCardResult.setType(cardResult.type);
                                idCardResult.setCardNum(cardResult.cardNum);
                                idCardResult.setName(cardResult.name);
                                idCardResult.setSex(cardResult.sex);
                                idCardResult.setAddress(cardResult.address);
                                idCardResult.setNation(cardResult.nation);
                                idCardResult.setBirth(cardResult.birth);
                                idCardResult.setFrontFullImageSrc(cardResult.frontFullImageSrc);
                                idCardResult.setFrontWarning(cardResult.frontWarning);
                                idCardResult.setOffice(cardResult.office);
                                idCardResult.setValidDate(cardResult.validDate);
                                idCardResult.setBackFullImageSrc(cardResult.backFullImageSrc);
                                idCardResult.setBackWarning(cardResult.backWarning);
                                idCardResult.setSign(cardResult.sign);
                                idCardResult.setOrderNo(cardResult.orderNo);
                                idCardResult.setOcrId(cardResult.ocrId);
                                ocrCallBack.onSuccess(idCardResult);
                            }
                        } else {

                            if (!resultCode.equals("200101") && !resultCode.equals("200100")) {
                                ocrCallBack.onError(resultMsg);
                                ocrCallBack.onTencentCallBack(orderNo);


                            }

                        }


                    }
                }, ocrTypeMode ? WbCloudOcrSDK.WBOCRTYPEMODE.WBOCRSDKTypeFrontSide : WbCloudOcrSDK.WBOCRTYPEMODE.WBOCRSDKTypeBackSide);
            }

            @Override
            public void onLoginFailed(String s, String s1) {
                ocrCallBack.onLoginTimeOut("身份证扫描失败，请重试");
                ocrCallBack.onTencentCallBack(orderNo);
            }
        });
    }
}
