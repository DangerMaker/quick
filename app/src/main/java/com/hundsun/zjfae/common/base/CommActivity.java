package com.hundsun.zjfae.common.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.home.dialog.UserLevelDialog;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.CareerSelsectActivity;
import com.hundsun.zjfae.activity.mine.CertificationActivity;
import com.hundsun.zjfae.activity.mine.CertificationStatus;
import com.hundsun.zjfae.activity.mine.OfflineRechargeActivity;
import com.hundsun.zjfae.activity.mine.RechargeActivity;
import com.hundsun.zjfae.activity.product.OpenAttachmentActivity;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
import com.hundsun.zjfae.activity.product.bean.AttachmentEntity;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.api.CustomProgressFragmentDialog;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.PhoneInfo;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.PhoneInfoUtils;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.view.dialog.CustomProgressDialog;
import com.hundsun.zjfae.fragment.finance.bean.ProductDate;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.meituan.android.walle.WalleChannelReader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.LoginStatus;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;

import static com.hundsun.zjfae.common.base.BasePresenter.MZJ;
import static com.hundsun.zjfae.common.base.BasePresenter.PBIFE;
import static com.hundsun.zjfae.common.base.BasePresenter.VREGMZJ;

public abstract class CommActivity<P extends BasePresenter> extends BaseActivity implements BaseView {

    protected P presenter;

    protected CustomProgressDialog mCustomProgressDialog = null;

    protected String strMessage = "????????????";

    protected String userStatus = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        mCustomProgressDialog = getCustomProgressDialog(this);
        initView();
        initData();
    }


    protected Dialog initDialog() {

        mCustomProgressDialog = getCustomProgressDialog(this);

        return mCustomProgressDialog;
    }


    protected CustomProgressDialog getCustomProgressDialog(Context context) {
        mCustomProgressDialog = new CustomProgressDialog(context, strMessage);
        mCustomProgressDialog.setCanceledOnTouchOutside(false);
        return mCustomProgressDialog;
    }


    /**
     * strMessage ????????????
     */
    protected CustomProgressDialog getCustomProgressDialog(Context context, String strMessage) {
        mCustomProgressDialog = null;
        mCustomProgressDialog = new CustomProgressDialog(context, strMessage);
        mCustomProgressDialog.setCanceledOnTouchOutside(false);
        return mCustomProgressDialog;
    }


    protected abstract P createPresenter();


    /**
     * ???????????????????????????????????? ????????????????????? ??????????????????????????????
     */
    protected LinearLayout lin_no_net_data;
    protected LinearLayout lin_no_net;
    protected LinearLayout lin_no_data;
    protected LinearLayout lin_reload;


    //????????????
    protected void setNoDataViewVisiable() {
        lin_no_net_data = (LinearLayout) findViewById(R.id.layout_no_net_data);
        lin_no_net_data.setVisibility(View.VISIBLE);
        lin_no_data = (LinearLayout) findViewById(R.id.lin_no_data);
        lin_no_data.setVisibility(View.VISIBLE);
    }

    //???????????????????????????
    protected void setNoDataViewGone() {
        lin_no_net_data = (LinearLayout) findViewById(R.id.layout_no_net_data);
        lin_no_net_data.setVisibility(View.GONE);
        lin_no_data = (LinearLayout) findViewById(R.id.lin_no_data);
        lin_no_data.setVisibility(View.GONE);
    }

    //????????????
    protected void setNoNetViewVisiable(View.OnClickListener reload) {
        lin_no_net_data = (LinearLayout) findViewById(R.id.layout_no_net_data);
        lin_no_net_data.setVisibility(View.VISIBLE);
        lin_no_net = (LinearLayout) findViewById(R.id.lin_no_net);
        lin_no_net.setVisibility(View.VISIBLE);
        lin_reload = (LinearLayout) findViewById(R.id.lin_reload);
        lin_reload.setOnClickListener(reload);
    }

    //????????????????????????
    protected void setNoNetViewGone() {
        lin_no_net_data = (LinearLayout) findViewById(R.id.layout_no_net_data);
        lin_no_net_data.setVisibility(View.GONE);
        lin_no_net = (LinearLayout) findViewById(R.id.lin_no_net);
        lin_no_net.setVisibility(View.GONE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {  //???????????????
            if (!mTopDefineCancel) {
                CommActivity.this.finish();
            } else {
                topDefineCancel();
            }

        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEB_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED) {

            String url = BasePresenter.loginStatusParseUrl(BasePresenter.MZJ, BasePresenter.PBMOI, BasePresenter.VMOIMZJ, ConstantName.LOGIN_STATUS, BasePresenter.getRequestMap());

            Observable observable = Observable.concat(presenter.apiServer.loginStatus(url), presenter.getUserInfo());
            CCLog.e("url", url);

            //??????????????????
            presenter.addDisposable(observable, new ProtoBufObserver(this) {
                @Override
                public void onSuccess(Object mClass) {

                    if (mClass instanceof LoginStatus.Ret_PBIFE_login_status) {
                        LoginStatus.Ret_PBIFE_login_status ret_pbife_login_status = (LoginStatus.Ret_PBIFE_login_status) mClass;
                        CCLog.e("????????????", ret_pbife_login_status.getReturnMsg());
                        CCLog.e("??????code", ret_pbife_login_status.getReturnCode());
                    } else if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                        UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = (UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass;
                        UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfoData = userDetailInfo.getData();
                        UserInfoSharePre.setTradeAccount(userDetailInfoData.getTradeAccount());
                        UserInfoSharePre.setAccount(userDetailInfoData.getAccount());
                        UserInfoSharePre.setFundAccount(userDetailInfoData.getFundAccount());
                    }
                }
            });


        }
    }


    public JSONObject os = new JSONObject();

    Pattern p = Pattern.compile("^((\\+86)|(86))?[1][3456789][0-9]{9}$");

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    protected void readPhoneState() {

        PhoneInfo phoneInfo = PhoneInfo.getPhoneInfo();

//        PhoneInfoUtils phoneInfoUtils = new PhoneInfoUtils(mBaseApplication);
//        String phoneNumber = phoneInfoUtils.getNativePhoneNumber();
//
//        if (!TextUtils.isEmpty(phoneNumber)) {
//
//            Matcher m = p.matcher(phoneNumber);
//            if (m.matches()) {
//                phoneInfo.phoneNumber = phoneNumber + "";
//                phoneInfo.phoneNumberStatus = "1";
//            } else {
//                phoneInfo.phoneNumber = phoneNumber + "";
//                phoneInfo.phoneNumberStatus = "3";
//            }
//
//        } else {
        phoneInfo.phoneNumber = "";
        phoneInfo.phoneNumberStatus = "3";
//        }

//        phoneInfo.routingAddress = phoneInfoUtils.getLocalMacAddressFromWifiInfo();


        phoneInfo.clientOsver = Build.VERSION.RELEASE;

        phoneInfo.deviceModal = Build.MANUFACTURER + Build.MODEL;


//        StringBuffer buffer = new StringBuffer();
//        buffer.append(getIMEI(this, 0)).append(",");
//        buffer.append(getIMEI(this, 1)).append(",");
//        phoneInfo.deviceId = buffer.deleteCharAt(buffer.length() - 1).toString();
        phoneInfo.deviceId = new PhoneInfoUtils(this).getUniquePsuedoID();
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

//        CCLog.e("deviceId----" + tm.getDeviceId());
        phoneInfo.user_id = UserInfoSharePre.getUserName();

        phoneInfo.appVersion = BasePresenter.APPVERSION;


        String channel = WalleChannelReader.getChannel(this);

        phoneInfo.channel = channel + "";

        PhoneInfo.putData(phoneInfo);
    }

    protected void openAttachment(String id, String fileName, String type) {
        Map map = BasePresenter.getRequestMap();
        map.put("type", type);
        String serverUrl = "";
        if (type.equals("8")) {

            map.put("different", "03");
            map.put("productCode", id);
            map.put("version", "1.0.1");
            map.put("entrustedPath", Utils.getEncodeURIComponent(id));
            map.put("entrustedReport", Utils.getEncodeURIComponent(fileName));

            serverUrl = BasePresenter.BASE_URL + "mzj/" + "pbimg.do?" + "type=" + type + "&entrustedPath=" + Utils.getEncodeURIComponent(id) + "&entrustedReport=" + Utils.getEncodeURIComponent(fileName) + "&p=and" + "&version=" + BasePresenter.APPVERSION + "&different=03&productCode=" + id + "&version=1.0.1";

        } else {
            serverUrl = BasePresenter.BASE_URL + "mzj/" + "pbimg.do?" + "type=" + type + "&filePath=" + Utils.getEncodeURIComponent(id) + "&fileName=" + Utils.getEncodeURIComponent(fileName) + "&p=and" + "&version=" + BasePresenter.APPVERSION;
        }
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setTitle(fileName);
        attachmentEntity.setOpenUrl(serverUrl);
        Intent intent = new Intent(this, OpenAttachmentActivity.class);
        intent.putExtra("attachmentEntity", attachmentEntity);
        baseStartActivity(intent);
    }


    protected void openAttachment(String id, String fileName, String type, String productCode) {

        Map map = BasePresenter.getRequestMap();
        map.put("type", type);
        String serverUrl = "";
        if (type.equals("8")) {

            map.put("different", "03");
            map.put("productCode", productCode);
            map.put("version", "1.0.1");
            map.put("entrustedPath", Utils.getEncodeURIComponent(id));
            map.put("entrustedReport", Utils.getEncodeURIComponent(fileName));

            serverUrl = BasePresenter.BASE_URL + "mzj/" + "pbimg.do?" + "type=" + type + "&entrustedPath=" + Utils.getEncodeURIComponent(id) + "&entrustedReport=" + Utils.getEncodeURIComponent(fileName) + "&p=and" + "&version=" + BasePresenter.APPVERSION + "&different=03&productCode=" + productCode + "&version=1.0.1";

        } else {

            serverUrl = BasePresenter.BASE_URL + "mzj/" + "pbimg.do?" + "type=" + type + "&filePath=" + Utils.getEncodeURIComponent(id) + "&fileName=" + Utils.getEncodeURIComponent(fileName) + "&p=and" + "&version=" + BasePresenter.APPVERSION;

        }
        AttachmentEntity attachmentEntity = new AttachmentEntity();

        attachmentEntity.setTitle(fileName);
        attachmentEntity.setOpenUrl(serverUrl);
        Intent intent = new Intent(this, OpenAttachmentActivity.class);
        intent.putExtra("attachmentEntity", attachmentEntity);
        baseStartActivity(intent);

    }


    /**
     * @param slotId slotId?????????Id??????????????? 0???1???
     * @return
     */
    private String getIMEI(Context context, int slotId) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = manager.getClass().getMethod("getImei", int.class);
            String imei = (String) method.invoke(manager, slotId);
            return imei;
        } catch (Exception e) {
            return "";
        }
    }

    protected void openAttachment(String riskLevelLabelName, String riskLevelLabelUrl) {
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setTitle(riskLevelLabelName);
        attachmentEntity.setOpenUrl(riskLevelLabelUrl);
        Intent intent = new Intent(this, OpenAttachmentActivity.class);
        intent.putExtra("attachmentEntity", attachmentEntity);
        baseStartActivity(intent);


    }


    @Override
    protected void onDestroy() {

        if (presenter != null) {
            presenter.onDestroy();
        }
        CCLog.e("onDestroy");
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        if (mCustomProgressDialog != null) {
            mCustomProgressDialog.show(strMessage);
        }
    }

    @Override
    public void showLoading(String content) {
        if (mCustomProgressDialog != null) {
            mCustomProgressDialog.show(content);
        }
    }

    private void closeLoadingDialog() {
        if (mCustomProgressDialog != null && mCustomProgressDialog.isShowing()) {
            mCustomProgressDialog.dismiss();
        }
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        closeLoadingDialog();
        showErrorDialog(msg);
        SmartRefreshLayout smartLayout1 = findViewById(R.id.smartLayout);
        if (smartLayout1 != null) {
            smartLayout1.finishRefresh();
            smartLayout1.finishLoadMore();
        }
        SmartRefreshLayout smartLayout2 = findViewById(R.id.refreshLayout);
        if (smartLayout2 != null) {
            smartLayout2.finishRefresh();
            smartLayout2.finishLoadMore();
        }

    }


    private CustomProgressFragmentDialog.Builder builder = null;

    @Override
    public void loginTimeOut(String returnMsg) {
        closeLoadingDialog();

        if (builder == null) {
            builder = new CustomProgressFragmentDialog.Builder(this);
            builder.setTitle("????????????");
            builder.setMessage(returnMsg);
            builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    outLogin(CommActivity.this);
                }
            });
        } else {
            builder.setMessage(returnMsg);
        }

        builder.create().show();


    }

    @Override
    public Context onAttach() {
        return this;
    }

    @Override
    public boolean isShowLoad() {
        return true;
    }


    @Override
    public void isFinishActivity(String returnMsg) {
        showDialog(returnMsg, "?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }

    /**
     * ??????html??????
     *
     * @param bodyHTML
     * @return
     */
    protected String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    /*************************************** ?????????????????????-start*******************************************************/
    UserLevelDialog userLevelDialog = null;
    private boolean isRegister = false;

    public void showUserLevelDialog(String type, String isRealInvestor) {

        userLevelDialog = new UserLevelDialog(this);

        userLevelDialog.setIsRealInvestor(isRealInvestor);
        userLevelDialog.setType(type);
        userLevelDialog.setOnClickListener(onClickListener);
        notice(type);
    }


    public void showUserLevelDialog(String type, String isRealInvestor, boolean isRegister) {
        this.isRegister = isRegister;

        userLevelDialog = new UserLevelDialog(this);
        userLevelDialog.setIsRealInvestor(isRealInvestor);
        userLevelDialog.setType(type);
        userLevelDialog.setOnClickListener(onClickListener);
        notice(type);
    }

    /**
     * dialog????????????
     */
    protected UserLevelDialog.OnClickListener onClickListener = new UserLevelDialog.OnClickListener() {

        @Override
        public void onMaterial(String isRealInvestor) {
            userLevelDialog.onDmiss();
            Intent intent = new Intent(CommActivity.this, HighNetWorthMaterialsUploadedActivity.class);
            intent.putExtra("isRealInvestor", isRealInvestor);
            intent.putExtra("isRegister", isRegister);
            baseStartActivity(intent);
        }

        @Override
        public void onRecharge() {
            queryBankInfo();
        }
    };


    public void onUserLevelNotice(Notices.Ret_PBAPP_notice retNotice) {

        userStatus = retNotice.getData().getNotice().getIsShow();

        userLevelDialog.setIsShow(retNotice.getData().getNotice().getIsShow());
        userLevelDialog.setDataUpContent(retNotice.getData().getNotice().getButtonTitle());
        userLevelDialog.setNotice_title(retNotice.getData().getNotice().getNoticeTitle());
        userLevelDialog.loadDataWithBaseURL(getHtmlData(retNotice.getData().getNotice().getNoticeContent()));
        if (!retNotice.getData().getNotice().getIsShow().equals("2")) {
            userLevelDialog.createDialog().show();
        }
    }

    private String bankName, bankCard;

    public void onUserLevelQueryRechargeBankInfo(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo) {
        bankName = bankCardInfo.getData().getBankName();
        bankCard = bankCardInfo.getData().getBankCardNo();
        userLevelDialog.onDmiss();
        //??????????????????
        if (bankCardInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "301");
            baseStartActivity(intent);
        } else {
            //?????????????????????
            queryFundBankInfo(bankCardInfo.getData().getBankNo());
        }
    }

    public void onUserLevelBankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {

        userLevelDialog.onDmiss();
        //??????
        if (fundBankInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "302");
            intent.putExtra("isRegister", isRegister);
            baseStartActivity(intent);

        } else {
            Intent intent = new Intent(this, RechargeActivity.class);
            intent.putExtra("isRegister", isRegister);
            baseStartActivity(intent);
        }
    }


    //????????????
    private String payChannelNo = "";


    /**
     * 000????????????????????????
     * 004????????????
     * 010??????????????????
     */
    public void notice(String type) {
        Notices.REQ_PBAPP_notice.Builder notice = Notices.REQ_PBAPP_notice.newBuilder();
        notice.setType(type);

        Map<String, String> map = BasePresenter.getRequestMap();
        map.put("version", BasePresenter.twoVersion);
        String url = presenter.parseUrl(BasePresenter.AZJ, BasePresenter.PBAFT, BasePresenter.VAFTAZJ, ConstantName.Notice, map);

        presenter.addDisposable(presenter.apiServer.notice(url, BasePresenter.getBody(notice.build().toByteArray())), new BaseObserver<Notices.Ret_PBAPP_notice>(this) {
            @Override
            public void onSuccess(Notices.Ret_PBAPP_notice notice) {
                if (notice.getReturnCode().equals(ConstantCode.RETURN_CODE)) {
                    onUserLevelNotice(notice);
                } else {
                    showError(notice.getReturnMsg());
                }
            }
        });
    }


    /**
     * ??????????????????????????????????????????
     */
    public void queryBankInfo() {
        String url = presenter.parseUrl(BasePresenter.MZJ, BasePresenter.PBIFE, BasePresenter.VREGMZJ, ConstantName.LoadRechargeBankCardInfo, BasePresenter.getRequestMap());
        presenter.addDisposable(presenter.apiServer.queryBankInfo(url), new BaseObserver<RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo>(this) {
            @Override
            public void onSuccess(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo) {
                //1042????????????????????????
//                if (bankCardInfo.getReturnCode().equals(ConstantCode.RETURN_CODE) || bankCardInfo.getReturnCode().equals("1042")) {
//
//                }
//                else {
//                    showDialog(bankCardInfo.getReturnMsg());
//                }

                payChannelNo = bankCardInfo.getData().getPayChannelNo();
                onUserLevelQueryRechargeBankInfo(bankCardInfo);
            }
        });

    }

    /**
     * ????????????????????????
     */
    public void queryFundBankInfo(final String bankNo) {
        FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.Builder fundBank = FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.newBuilder();
        fundBank.setBankCode(bankNo);
        fundBank.setPayChannel(payChannelNo);
        fundBank.setTransType("1");
        String url = presenter.parseUrl(BasePresenter.MZJ, BasePresenter.PBIFE, BasePresenter.VREGMZJ, ConstantName.QueryFundBankInfo, BasePresenter.getRequestMap());
        presenter.addDisposable(presenter.apiServer.queryFundBankInfo(url, BasePresenter.getBody(fundBank.build().toByteArray())), new BaseObserver<FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo>(this) {
            @Override
            public void onSuccess(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
                onUserLevelBankCardManage(fundBankInfo);
            }
        });
    }

    /*************************************** ?????????????????????-end*******************************************************/


    /**
     * ????????????
     *
     * @param verifyName            ????????????
     * @param certificateStatusType ??????????????????
     **/
    protected void certificateStatusType(String verifyName, String certificateStatusType) {


        String userType = UserInfoSharePre.getUserType();

        if (userType.equals("personal")) {

            if (!verifyName.equals("1")) {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????????
                        dialog.dismiss();
                        baseStartActivity(CommActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                switch (certificateStatusType) {
                    case "1":
                    case "2":
                        Intent intent = new Intent(CommActivity.this, CertificationActivity.class);
                        intent.putExtra("isProfession", true);
                        baseStartActivity(intent);
                        break;
                    case "3":
                        baseStartActivity(CommActivity.this, CareerSelsectActivity.class);
                        break;
                    case "4":
                        baseStartActivity(CommActivity.this, CertificationActivity.class);
                        break;
                    case "5":
                        baseStartActivity(this, CertificationStatus.class);
                        break;
                    case "6":
                        baseStartActivity(CommActivity.this, CertificationActivity.class);
                        break;
                    default:
                        break;
                }

            }
        }


    }


    //??????
    public void onJumpAction(@NonNull BaseCacheBean baseCacheBean) {


        ShareBean shareBean = new ShareBean();
        shareBean.setJumpRule(baseCacheBean.getJumpRule());
        shareBean.setUuid(baseCacheBean.getUuid());
        shareBean.setShareUrl(baseCacheBean.getShareUrl());
        shareBean.setIsShare(baseCacheBean.getIsShare());
        shareBean.setFuncUrl(baseCacheBean.getFuncUrl());
        shareBean.setSharePicUrl(baseCacheBean.getSharePicUrl());
        shareBean.setFuncIcons(baseCacheBean.getFuncIcons());
        shareBean.setShareDesc(baseCacheBean.getShareDesc());
        shareBean.setShareItem(baseCacheBean.getShareItem());
        shareBean.setShareIsSure(baseCacheBean.getShareIsSure());
        shareBean.setShareTitle(baseCacheBean.getShareTitle());
        shareBean.setLinkKeywordName(baseCacheBean.getLink_keyword_name());
        shareBean.setKeyword(baseCacheBean.getKeyword());

        if (shareBean != null) {

            String jumpAction = shareBean.getJumpRule();

            //????????????
            if (jumpAction.equals("authentication")) {
                UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());

                onAuthentication();

            }
            //???????????????
            else if (jumpAction.equals("callPhoneQualifiedInvestor")) {
                UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());

                onQualifiedInvestor();

            }
            //????????????
            else if (jumpAction.equals("subscribeList")) {

                UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());
                String keyword_name = shareBean.getLinkKeywordName();
                ProductDate.rest();
                ProductDate.keywordName = keyword_name;
                String uuids = shareBean.getKeyword();
                ProductDate.uuids = uuids;
                ((HomeActivity) this).setProductDate();

            }
            //??????
            else if (jumpAction.equals("outerlink")) {

                Intent intent = new Intent();

                String funcUrl = shareBean.getFuncUrl();

                //??????????????????
                if (StringUtils.isNotBlank(funcUrl)) {

                    UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());
                    //????????????
                    if (funcUrl.indexOf("http://") >= 0 || funcUrl.indexOf("https://") >= 0) {

                        startWebActivity(shareBean);
                    }
                    // ????????????????????????
                    else if (funcUrl.indexOf("product://") >= 0) {

                        funcUrl = funcUrl.replace("product://", "");
                        intent.setClass(CommActivity.this, ProductCodeActivity.class);
                        intent.putExtra("productCode", funcUrl);
                        intent.putExtra("sellingStatus", "1");
                        baseStartActivity(intent);
                    }

                    // ????????????????????????
                    else {
                        funcUrl = funcUrl.replaceAll("func://productlist://", "");
                        ProductDate.rest();
                        ProductDate.productName = funcUrl;
                        ((HomeActivity) this).setProductDate();
                    }
                }

            }
        }


    }


    //?????????????????????
    private void onQualifiedInvestor() {

        presenter.addDisposable(presenter.getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>() {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userbaseinfoGetUserDetailInfo) {


                UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userbaseinfoGetUserDetailInfo.getData();

                String userType = userDetailInfo.getUserType();


                if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {
                    showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //?????????
                            dialog.dismiss();
                            baseStartActivity(CommActivity.this, AddBankActivity.class);
                        }
                    });
                }
                //????????????????????????
                else if (userDetailInfo.getHighNetWorthStatus().equals("-1")) {
                    String pmtInfo = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                    if (userDetailInfo.getIsAccreditedInvestor().equals("") || userDetailInfo.getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "????????????????????????????????????????????????";
                    }

                    showDialog(pmtInfo);
                } else {
                    // ??????????????????????????????
                    if (userDetailInfo.getHighNetWorthStatus().equals("0")) {

                        getUserHighNetWorthInfo(userDetailInfo.getIsRealInvestor());
                    }
                    //????????????????????????????????????????????????????????????
                    else {

                        if (userDetailInfo.getUserType().equals("personal")) {
                            showUserLevelDialog("000", userDetailInfo.getIsRealInvestor());
                        } else if (userDetailInfo.getUserType().equals("company")) {
                            showUserLevelDialog("020", userDetailInfo.getIsRealInvestor());
                        }
                    }
                }


            }
        });


    }

    //????????????
    private void onAuthentication() {

        presenter.addDisposable(presenter.getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>() {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                certificateStatusType(userDetailInfo.getData().getVerifyName(), userDetailInfo.getData().getCertificateStatusType());
            }
        });

    }

    /**
     * ?????????????????????????????????
     */
    private void getUserHighNetWorthInfo(final String isRealInvestor) {
        String url = presenter.parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserHighNetWorthInfo);
        UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.Builder builder =
                UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.newBuilder();
        builder.setDynamicType1("highNetWorthUpload");

        presenter.addDisposable(presenter.apiServer.getUserHighNetWorthInfo(url, BasePresenter.getBody(builder.build().toByteArray())), new BaseObserver<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo>() {

            @Override
            public void onSuccess(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo highNetWorthInfo) {

                StringBuffer buffer = new StringBuffer();
                for (UserHighNetWorthInfo.DictDynamic dynamic : highNetWorthInfo.getData().getDictDynamicListList()) {
                    if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {
                        buffer.append(dynamic.getAuditComment()).append("\n");
                    }
                }
                showDialog(buffer.toString() + "".trim(), "????????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent();
                        intent.putExtra("isRealInvestor", isRealInvestor);
                        intent.setClass(CommActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                        baseStartActivity(intent);

                    }
                });

            }
        });

    }

    @Override
    protected void onStop() {

        if (presenter != null) {
            presenter.detachView();
        }
        super.onStop();

        CCLog.e("?????????");
    }
}
