package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.bean.FaceIdCardBean;
import com.hundsun.zjfae.activity.mine.view.AddBankView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import onight.zjfae.afront.gens.AcquireBankSmsCheckCode;
import onight.zjfae.afront.gens.AddBankCard;
import onight.zjfae.afront.gens.BasicInfo;
import onight.zjfae.afront.gens.CheckIsDayCut;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.QueryBankInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.TencentFaceCallback;
import onight.zjfae.afront.gensazj.TencentOcrId;

public class AddBankPresenter extends BasePresenter<AddBankView> {

    private String bankcardpic,postfix;

    private String certificateType = "";


    private String orderNo = "";


    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public AddBankPresenter(AddBankView baseView) {
        super(baseView);
    }



    public void initUserCardData(){

        Observable observable = Observable.mergeDelayError(getUserInfo(),getDictionary(),getBasicInfo());


        addDisposable(observable, new BaseBankProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo){

                    baseView.onUserDetailInfo((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) o);
                }
                else if (o instanceof Dictionary.Ret_PBAPP_dictionary){
                    Dictionary.Ret_PBAPP_dictionary dictionary = (Dictionary.Ret_PBAPP_dictionary) o;
                    List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();
                    bankcardpic = parms.get(0).getKeyCode();
                    postfix = parms.get(1).getKeyCode();
                }

                else if (o instanceof BasicInfo.Ret_PBIFE_userbaseinfo_getBasicInfo){
                    BasicInfo.Ret_PBIFE_userbaseinfo_getBasicInfo    basicInfo = (BasicInfo.Ret_PBIFE_userbaseinfo_getBasicInfo) o;
                    certificateType = basicInfo.getData().getCertificateType();
                    if (certificateType.equals("99")){
                        certificateType = "1";
                    }
                    baseView.onUserBasicInfo(basicInfo);

                }

            }
        });

    }


    /**
     * 日切时间----外面拦截，可以不用
     * */
    private Observable<CheckIsDayCut.Ret_PBIFE_systemcheck_checkIsDayCut> getCheckIsDayCut(){

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.CheckIsDayCut,getRequestMap());

        return apiServer.getCheckIsDayCut(url);
    }

    /**
     * 获取银行卡图片下载地址
     ***/
    private Observable<Dictionary.Ret_PBAPP_dictionary> getDictionary(){
        String [] keyNo = {"bankcardpic.prefix","bankcardpic.suffix"};
        Map body = new HashMap();
        body.put("keyNo",keyNo);
        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Dictionary,getRequestMap());
        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("bankcardpic.prefix");
        diction.addKeyNo("bankcardpic.suffix");

        return apiServer.getDictionary(url, getBody(diction.build().toByteArray()));
    }


    //获取用户基本信息
    private Observable<BasicInfo.Ret_PBIFE_userbaseinfo_getBasicInfo> getBasicInfo(){
        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.BasicInfo,getRequestMap());
        return apiServer.getBasicInfo(url);
    }


    /**
     * 下载银行卡icon
     * **/
    public String downloadImage(String bankCode){
        String url = bankcardpic+bankCode+postfix;
        return url;
    }




    //卡宾
    public void getCheckBankType(String bankCardNo){
        String url = parseUrl(MZJ,PBIFE,VIFEMZJ, ConstantName.QueryBankInfo,getRequestMap());
        QueryBankInfo.REQ_PBIFE_bankcardmanage_queryBankInfo.Builder query =   QueryBankInfo.REQ_PBIFE_bankcardmanage_queryBankInfo.newBuilder();
        query.setBankCardNo(bankCardNo);
        addDisposable(apiServer.getCheckBankType(url, getBody(query.build().toByteArray())), new ProtoBufObserver<QueryBankInfo.Ret_PBIFE_bankcardmanage_queryBankInfo>() {
            @Override
            public void onSuccess(QueryBankInfo.Ret_PBIFE_bankcardmanage_queryBankInfo ret_pbife_bankcardmanage_queryBankInfo) {
                baseView.onBindingBankBean(ret_pbife_bankcardmanage_queryBankInfo);
            }

        });

    }

    //渠道号
    private String payChannelNo = "";
    private String bankCode = "";

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
        payChannelNo = "";
    }

    /**
     * 查询银行渠道
     * 通过payChannelNo渠道号去查询银行渠道号
     * */
    public void queryFundBankInfo(final String bankCodeNo){
        FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.Builder fundBank = FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.newBuilder();
        fundBank.setBankCode(bankCodeNo);
        fundBank.setTransType("0");

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.QueryFundBankInfo,getRequestMap());

        addDisposable(apiServer.queryFundBankInfo(url, getBody(fundBank.build().toByteArray())), new BaseBankProtoBufObserver< FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo>() {
            @Override
            public void onSuccess(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {

                if (fundBankInfo.getReturnCode().equals(ConstantCode.RETURN_CODE)){
                    payChannelNo =  fundBankInfo.getData().getFundBankDict().getBankNo();
                    bankCode = fundBankInfo.getData().getFundBankDict().getBankId();
                    baseView.onFundBankInfoBean(fundBankInfo);
                }

            }
        });

    }


    /***
     * @param bankCardNo 银行卡号
     * @param bankCode 银行渠道
     * @param bankName 银行名字
     * @param idCard 身份证号
     * @param idName 身份证名字
     * */
    //发送短信验证码
    public void requestAddBankNeedSms(String bankCardNo,String bankCode,String bankName,String idCard,String idName){
        String url =  parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.AcquireBankSmsCheckCode,getRequestMap());
        AcquireBankSmsCheckCode.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode.Builder acquire
                = AcquireBankSmsCheckCode.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode.newBuilder();
        acquire.setBankCardNo(bankCardNo);
        acquire.setBankCode(bankCode);
        acquire.setBankName(bankName);
        acquire.setCertificateCode(idCard);
        acquire.setPayChannelNo(payChannelNo);
        acquire.setRealName(idName);
        acquire.setValidateType("1");

        addDisposable(apiServer.requestAddBankNeedSms(url, getBody(acquire.build().toByteArray())), new BaseBankProtoBufObserver<AcquireBankSmsCheckCode.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode>(baseView) {
            @Override
            public void onSuccess(AcquireBankSmsCheckCode.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode acquireBankSmsCheckCode) {
                checkCodeUniqueNo = acquireBankSmsCheckCode.getData().getCheckCodeUniqueNo();
                serialNo = acquireBankSmsCheckCode.getData().getSerialNo();
                baseView.onBankSmsCheckCodeBean(acquireBankSmsCheckCode);
            }
        });

    }

    private String checkCodeUniqueNo = "";
    private String serialNo = "";
    /**
     *
     *
     * **/
    //确认绑卡

    public void addBankCard( String bankCardNo,String bankName,String checkCode,String id_name,String certificateCode){
        AddBankCard.REQ_PBIFE_bankcardmanage_addBankCard.Builder addBank
                = AddBankCard.REQ_PBIFE_bankcardmanage_addBankCard.newBuilder();

        CCLog.e("银行卡号<<<",bankCardNo + ">>>银行卡号");
        CCLog.e("银行渠道号<<<",bankCode +">>>渠道号" );
        CCLog.e("银行名字<<<",bankName+">>>银行名字");
        CCLog.e("短信验证码<<<",checkCode+">>>短信验证码");
        CCLog.e("短信流水序列号<<<",serialNo+">>>短信流水序列号");
        CCLog.e("浦发短信验证码唯一编号<<<",checkCodeUniqueNo+">>>浦发短信验证码唯一编号");
        CCLog.e("身份证号<<<",certificateCode+">>>身份证号");
        CCLog.e("渠道号<<<",payChannelNo+">>>渠道号");
        CCLog.e("姓名<<<",id_name+">>>姓名");
        CCLog.e("证件类型<<<",certificateType+">>>证件类型");

        CCLog.e(bankCardNo+":"+bankCode+":"+bankName+":"+checkCode+":"+serialNo+":"+checkCodeUniqueNo+":"+certificateCode+":"+payChannelNo+":"+id_name+":"+certificateType);
        addBank.setBankCardNo(bankCardNo);
        addBank.setBankCode(bankCode);
        addBank.setBankName(bankName);
        addBank.setCheckCode(checkCode);
        addBank.setCheckCodeSerialNo(serialNo);
        addBank.setCheckCodeUniqueNo(checkCodeUniqueNo);
        addBank.setCertificateCode(certificateCode);
        addBank.setPayChannelNo(payChannelNo);
        addBank.setRealName(id_name);
        addBank.setCertificateType(certificateType);
        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.AddBankCard);
        addDisposable(apiServer.AddBankCard(url, getBody(addBank.build().toByteArray())), new BaseBankProtoBufObserver< AddBankCard.Ret_PBIFE_bankcardmanage_addBankCard>(baseView,"银行处理中...") {
            @Override
            public void onSuccess(AddBankCard.Ret_PBIFE_bankcardmanage_addBankCard addBankCard) {
                onTencentFaceCallback(orderNo,"IdCardProtol");
                baseView.onAddBank(addBankCard);


            }

        });

    }




    public void requestIdCardImage(String filepath){

        final File filePic = new File(filepath);
        if (!filePic.exists()){
            try {
                throw new FileNotFoundException("File no exists");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), filePic);
        builder.addFormDataPart("image",filePic.getName(),requestFile);

        addDisposable(apiServer.idCardImage(ID_CARD_IMAGE_URL, builder.build()), new BaseObserver(baseView) {
            @Override
            public void onSuccess(Object body) {
                filePic.delete();
                if (body instanceof FaceIdCardBean){
                    FaceIdCardBean cardBean = (FaceIdCardBean) body;
                    baseView.onIdCardImage(cardBean);
                }
            }

        });
    }

    public void initTencentOcrId(){
        String url = parseUrl(AZJ,PBAFT,VAFTAZJ,ConstantName.TENCENT_OCR_ID);
        addDisposable(apiServer.onTencentOcrId(url), new ProtoBufObserver<TencentOcrId.Ret_PBAPP_tencentID>(baseView,"身份证扫描初始化") {
            @Override
            public void onSuccess(TencentOcrId.Ret_PBAPP_tencentID ret_pbapp_tencentID) {
                baseView.onTencentOcrId(ret_pbapp_tencentID);
            }
        });

    }


    /**
     * 查询人脸识别状态
     ***/
    public void queryFaceStatus(){
        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Dictionary);
        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("face.type");

        addDisposable(apiServer.getDictionary(url, getBody(diction.build().toByteArray())), new ProtoBufObserver<Dictionary.Ret_PBAPP_dictionary>(baseView) {
            @Override
            public void onSuccess(Dictionary.Ret_PBAPP_dictionary ret_pbapp_dictionary) {
                String keyCode = "";
                List<Dictionary.PBAPP_dictionary.Parms> parmsList = ret_pbapp_dictionary.getData().getParmsList();

                for (Dictionary.PBAPP_dictionary.Parms parms :parmsList){
                    keyCode = parms.getKeyCode();
                }

                if (!keyCode.equals("") && keyCode.equals("tencentface")){
                    baseView.onFaceStatus(true);
                }
                else {
                    baseView.onFaceStatus(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                baseView.onFaceStatus(true);
            }
        });
    }


    /**
     * @param orderNo 订单号
     * @param sessionFlag 业务场景，IdCard/IdCardProtol
     * **/
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
