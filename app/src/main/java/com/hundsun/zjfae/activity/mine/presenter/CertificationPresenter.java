package com.hundsun.zjfae.activity.mine.presenter;

import android.widget.ImageView;

import com.hundsun.zjfae.activity.mine.view.CertificationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.zjfae.captcha.face.IDCardResult;

import onight.zjfae.afront.gens.PBIFEUserinfomanageUpdateCertificateDetail;
import onight.zjfae.afront.gensazj.TencentFaceCallback;
import onight.zjfae.afront.gensazj.TencentOcrId;

public class CertificationPresenter extends BasePresenter<CertificationView> {
    public CertificationPresenter(CertificationView baseView) {
        super(baseView);
    }

    private String orderNO = "",appid = "",nonce = "",userID = "",sign = "";

    public void getTencentOcrInfo( final ImageView imageView,final boolean ocrTypeMode){

        if (orderNO.equals("") || appid.equals("") || nonce.equals("") || userID.equals("") || sign.equals("")){
            String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.TENCENT_OCR_ID);
            addDisposable(apiServer.onTencentOcrId(url), new ProtoBufObserver<TencentOcrId.Ret_PBAPP_tencentID>(baseView,"身份证扫描初始化") {
                @Override
                public void onSuccess(TencentOcrId.Ret_PBAPP_tencentID ret_pbapp_tencentID) {
                    final TencentOcrId.PBAPP_tencentID idData =  ret_pbapp_tencentID.getData();
                    orderNO = idData.getOrderNO();
                    appid = idData.getAppid();
                    nonce = idData.getNonce();
                    userID = idData.getUserID();
                    sign = idData.getSign();
                    baseView.onTencentOcrId(orderNO,appid,nonce,userID,sign,imageView,ocrTypeMode);
                }
            });
        }
        else {
            baseView.onTencentOcrId(orderNO,appid,nonce,userID,sign,imageView,ocrTypeMode);
        }

    }



    public void onUserIdCardUpload(IDCardResult cardResult){

        if (IDCardResult.getInstance().getName().equals("") ||IDCardResult.getInstance().getCardNum().equals("") || IDCardResult.getInstance().getAddress().equals("") || IDCardResult.getInstance().getValidDate().equals("")){

            baseView.showError("请确认信息完整且正确");
        }
        else {
            PBIFEUserinfomanageUpdateCertificateDetail.REQ_PBIFE_userinfomanage_updateCertificateDetail.Builder userUpload = PBIFEUserinfomanageUpdateCertificateDetail.REQ_PBIFE_userinfomanage_updateCertificateDetail.newBuilder();
            userUpload.setName(cardResult.getName());
            userUpload.setSex(cardResult.getSex().contains("男")? "0":"1");
            userUpload.setNation(cardResult.getNation());
            userUpload.setCertificateCode(cardResult.getCardNum());
            userUpload.setCertificateAddressApp(cardResult.getAddress());
            userUpload.setIssuingAuthority(cardResult.getOffice());
            userUpload.setOcrOrderNo(cardResult.getOrderNo());
            userUpload.setCardVaildityDate(cardResult.getValidDate());
            userUpload.setUnbindChangeCard("");
            String url = parseUrl(MZJ,PBIFE,VIFEMZJ,ConstantName.USER_IDCARD_UPLOAD);

            addDisposable(apiServer.onUserIdCardUpload(url, getBody(userUpload.build().toByteArray())), new ProtoBufObserver<PBIFEUserinfomanageUpdateCertificateDetail.Ret_PBIFE_userinfomanage_updateCertificateDetail>(baseView,"资料上传中") {
                @Override
                public void onSuccess(PBIFEUserinfomanageUpdateCertificateDetail.Ret_PBIFE_userinfomanage_updateCertificateDetail ret_pbife_useridcard_upload) {
                    onTencentFaceCallback(orderNO,"cardUploadProtol");
                    baseView.onUserIdCardUpload(ret_pbife_useridcard_upload.getReturnMsg());
                }
            });
        }

    }

    public void clean(){
        orderNO = "";appid = "";nonce = "";userID = "";sign = "";
    }


    /**
     *@param sessionFlag   cardUpload/cardUploadProtol
     * */
    public void onTencentFaceCallback(String orderNo,String sessionFlag){
        TencentFaceCallback.REQ_PBAPP_tencentface_callback.Builder builder = TencentFaceCallback.REQ_PBAPP_tencentface_callback.newBuilder();
        builder.setOrderNo(orderNo);
        builder.setSessionFlag(sessionFlag);
        String url = parseUrl(AZJ,PBAFT,VAFTAZJ,ConstantName.TENCENT_CALLBACK);
        addDisposable(apiServer.onTencentFaceCallback(url,getBody(builder.build().toByteArray())), new ProtoBufObserver<TencentFaceCallback.Ret_PBAPP_tencentface_callback>() {
            @Override
            public void onSuccess(TencentFaceCallback.Ret_PBAPP_tencentface_callback ret_pbapp_tencentface_callback) {

            }
        });
    }

}
