package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.home.bean.ImageUploadBean;
import com.hundsun.zjfae.activity.home.bean.ImageUploadStateBean;
import com.hundsun.zjfae.activity.mine.view.UnbindBankCardView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import onight.zjfae.afront.gens.ChangeBankCardUpload;
import onight.zjfae.afront.gens.UnbindBankCard;

public class UnbindBankCardPresenter extends BasePresenter<UnbindBankCardView> {


    public UnbindBankCardPresenter(UnbindBankCardView baseView) {
        super(baseView);
    }




    /**
     * highNetWorth = 合格投资者材料上传
     * unbindCard = 解绑
     * changeCard = 换卡
     * account = 上传头像
     * */
    public void unbindBankCard(File file, final int index,String partValue){

        if (file == null &&!file.exists()){
            baseView.showError("网络异常,请联系管理员");
        }
        else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("businessType", partValue);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("file","Image"+partValue+index+".png",requestFile);

            addDisposable(apiServer.upLoadPicImage(UpLoadPicImage, builder.build()), new BaseObserver<ImageUploadBean>() {
                @Override
                public void onSuccess(ImageUploadBean uploadBean) {
                    baseView.requestImageUpload(uploadBean,index);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    baseView.showError("网络不太顺畅哦~");
                }
            });
        }




    }

    //解绑
    //cardReason = cancel
    //reasonDetails = 银行卡已注销
    public void unbindBankCardUpload(List<ImageUploadStateBean> stateBeanList,String cardReason,String reasonDetails ,String partValue){

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.UnbindBankCard);
        UnbindBankCard.REQ_PBIFE_bankcardmanage_unbindBankCardUpload.Builder builder
                =UnbindBankCard.REQ_PBIFE_bankcardmanage_unbindBankCardUpload.newBuilder();
        builder.setApplyChannel("12");
        builder.setBusinessType(partValue);
        builder.setUnbindCardReason(cardReason);
        builder.setReasonDetails(reasonDetails);

        for (int i = 0; i < stateBeanList.size(); i++) {
            UnbindBankCard.REQ_PBIFE_bankcardmanage_unbindBankCardUpload.NameAndPath.Builder namePath
                    =  UnbindBankCard.REQ_PBIFE_bankcardmanage_unbindBankCardUpload.NameAndPath.newBuilder();
            namePath.setFileNamesp(stateBeanList.get(i).getFileName());
            namePath.setFilePath(stateBeanList.get(i).getFilePath());
            namePath.setModel(stateBeanList.get(i).getModel());

            builder.addNameAndPathList(namePath.build());
        }
        addDisposable(apiServer.unBindHighNetWorthUpload(url, getBody(builder.build().toByteArray())), new ProtoBufObserver< UnbindBankCard.Ret_PBIFE_bankcardmanage_unbindBankCardUpload>(baseView) {
            @Override
            public void onSuccess(UnbindBankCard.Ret_PBIFE_bankcardmanage_unbindBankCardUpload ret_pbife_bankcardmanage_unbindBankCardUpload) {
                baseView.requestUnbindBankCard(ret_pbife_bankcardmanage_unbindBankCardUpload.getReturnCode(),ret_pbife_bankcardmanage_unbindBankCardUpload.getReturnMsg());
            }

        });

    }


    //材料换卡申请
    public void changeBankCardUpload(List<ImageUploadStateBean> stateBeanList ,String partValue){



        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.ChangeBankCard);

        ChangeBankCardUpload.REQ_PBIFE_bankcardmanage_changeBankCardUpload.Builder builder= ChangeBankCardUpload.REQ_PBIFE_bankcardmanage_changeBankCardUpload.newBuilder();

        builder.setApplyChannel("12");
        builder.setBusinessType(partValue);

        for (int i = 0; i < stateBeanList.size(); i++) {

            ChangeBankCardUpload.REQ_PBIFE_bankcardmanage_changeBankCardUpload.NameAndPath.Builder namePath
                    =  ChangeBankCardUpload.REQ_PBIFE_bankcardmanage_changeBankCardUpload.NameAndPath.newBuilder();
            namePath.setFileNamesp(stateBeanList.get(i).getFileName());
            namePath.setFilePath(stateBeanList.get(i).getFilePath());
            namePath.setModel(stateBeanList.get(i).getModel());
            builder.addNameAndPathList(namePath.build());
        }

        addDisposable(apiServer.changeBankCardUpload(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ChangeBankCardUpload.Ret_PBIFE_bankcardmanage_changeBankCardUpload>(baseView) {
            @Override
            public void onSuccess(ChangeBankCardUpload.Ret_PBIFE_bankcardmanage_changeBankCardUpload ret_pbife_bankcardmanage_changeBankCardUpload) {

                baseView.requestChangeBankCard(ret_pbife_bankcardmanage_changeBankCardUpload.getReturnCode(),ret_pbife_bankcardmanage_changeBankCardUpload.getReturnMsg());

            }
        });
    }




}
