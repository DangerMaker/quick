package com.hundsun.zjfae.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.home.WebActivity;
import com.hundsun.zjfae.activity.home.X5WebActivity;
import com.hundsun.zjfae.activity.home.dialog.UserLevelDialog;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.CareerSelsectActivity;
import com.hundsun.zjfae.activity.mine.CertificationActivity;
import com.hundsun.zjfae.activity.mine.CertificationStatus;
import com.hundsun.zjfae.activity.mine.OfflineRechargeActivity;
import com.hundsun.zjfae.activity.mine.RechargeActivity;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.BaseView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.UserInfo;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.fragment.finance.bean.ProductDate;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;

import static com.hundsun.zjfae.common.base.BasePresenter.MZJ;
import static com.hundsun.zjfae.common.base.BasePresenter.PBIFE;
import static com.hundsun.zjfae.common.base.BasePresenter.VREGMZJ;

public abstract class  BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {
    protected static Context mContext;
    protected CommActivity mActivity;
    protected View mRootView;
    protected P presenter;
    protected static Bundle mBundle;
    private static final int NO_ID = -1;


    private OnFragmentListener fragmentListener;


    private static final int LOGIN_TIMEOUT = 101419;

    //Recharge
    private static final int RECHARGE_CODE = 0x1204;


    protected static final int RESULT_OK = -1;

    protected static final int RESULT_CANCELED = 0;

    public void setFragmentListener(OnFragmentListener fragmentListener) {

        this.fragmentListener = fragmentListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }


        } else {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            onBindViewBefore(mRootView);
            if (savedInstanceState != null) {
                onRestartInstance(savedInstanceState);
            }

            // Init
            presenter = createPresenter();
            initWidget(mRootView);
            initWidget();
            initData();
            resetLayout();
        }
        return mRootView;
    }

    public void initData() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = (CommActivity) getActivity();

    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    protected void onBindViewBefore(View root) {
        // ...
    }

    protected void onRestartInstance(Bundle bundle) {

    }

    protected void initBundle(Bundle bundle) {

    }

    protected void initWidget(View root) {

    }

    protected void initWidget() {

    }


    /**
     * 控件寻找id
     */
    public final <T extends View> T findViewById(int id) {
        return (T) mRootView.findViewById(id);

    }


    /**
     * 创建Presenter
     *
     * @return P
     */
    protected abstract P createPresenter();

    /**
     * 屏幕适配方法
     */
    protected  void resetLayout(){

    };

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int getLayoutId();


    public void baseStartActivity(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        baseStartActivity(intent);
    }

    public void baseStartActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    /**
     * 启动WebActivity
     **/
    public void startWebActivity(String funcUrl) {
        ShareBean shareBean = new ShareBean();
        shareBean.setFuncUrl(funcUrl);
        startWebActivity(shareBean);
    }

    /**
     * 启动WebActivity
     **/
    public void startWebActivity(ShareBean shareBean) {
        Intent intent = new Intent();

        CCLog.e("TAGTAG", "SKIP=" + UserInfoSharePre.getSkip());
        if (StringUtils.isNotBlank(UserInfoSharePre.getSkip()) && UserInfoSharePre.getSkip().equals(UserInfo.SKIPTENCENT)) {
            CCLog.e("信息","X5");
            intent.setClass(mActivity, X5WebActivity.class);
        } else {
            CCLog.e("信息","原生");
            intent.setClass(mActivity, WebActivity.class);
        }
        intent.putExtra("shareBean", shareBean);

        mActivity.startWebActivity(intent);

    }


    @Override
    public void showLoading() {


        if (mActivity == null) {
            mActivity = (HomeActivity) getActivity();
        }
        mActivity.showLoading();

    }

    @Override
    public void showLoading(String content) {

        if (mActivity == null) {
            mActivity = (HomeActivity) getActivity();
        }
        mActivity.showLoading(content);
    }


    @Override
    public void hideLoading() {
        if (mActivity == null) {
            mActivity = (HomeActivity) getActivity();
        }
        mActivity.hideLoading();

    }

    @Override
    public void showError(String msg) {

        mActivity.showErrorDialog(msg);
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

    @Override
    public void loginTimeOut(String returnMsg) {

        if (mActivity == null) {
            mActivity = (HomeActivity) getActivity();
        }
        mActivity.loginTimeOut(returnMsg);

    }

    public void showDialog(String msg) {
        if (mActivity == null) {
            mActivity = (HomeActivity) getActivity();
        }

        if (msg == null) {
            return;

        }
        mActivity.showDialog(msg);

    }


    public void showDialog(String msg, String confirm_btnText, DialogInterface.OnClickListener listener) {
        if (mActivity == null) {
            mActivity = (HomeActivity) getActivity();
        }
        if (msg == null) {
            return;
        }
        mActivity.showDialog(msg, confirm_btnText, listener);
    }

    public void showDialog(String msg, String cancel_btnText) {
        if (mActivity == null) {
            mActivity = (HomeActivity) getActivity();
        }
        if (msg == null) {
            return;

        }
        mActivity.showDialog(msg, cancel_btnText);
    }


    public void showDialog(String msg, String confirm_btnText, String cancel_btnText, DialogInterface.OnClickListener postDialogInterface) {
        if (mActivity == null) {
            mActivity = (HomeActivity) getActivity();
        }
        mActivity.showDialog(msg, confirm_btnText, cancel_btnText, postDialogInterface);
    }


    /**
     * 以下代码为列表数据为空时 或者没有网络时 调用显示无数据的布局
     */
    protected LinearLayout lin_no_net_data;
    protected LinearLayout lin_no_net;
    protected LinearLayout lin_no_data;
    protected LinearLayout lin_reload;


    //没有数据
    protected void setNoDataViewVisiable() {
        lin_no_net_data = (LinearLayout) findViewById(R.id.layout_no_net_data);
        lin_no_net_data.setVisibility(View.VISIBLE);
        lin_no_data = (LinearLayout) findViewById(R.id.lin_no_data);
        lin_no_data.setVisibility(View.VISIBLE);
    }

    //隐藏没有数据的视图
    protected void setNoDataViewGone() {
        lin_no_net_data = (LinearLayout) findViewById(R.id.layout_no_net_data);
        lin_no_net_data.setVisibility(View.GONE);
        lin_no_data = (LinearLayout) findViewById(R.id.lin_no_data);
        lin_no_data.setVisibility(View.GONE);
    }

    //网络断开
    protected void setNoNetViewVisiable(View.OnClickListener reload) {
        lin_no_net_data = (LinearLayout) findViewById(R.id.layout_no_net_data);
        lin_no_net_data.setVisibility(View.VISIBLE);
        lin_no_net = (LinearLayout) findViewById(R.id.lin_no_net);
        lin_no_net.setVisibility(View.VISIBLE);
        lin_reload = (LinearLayout) findViewById(R.id.lin_reload);
        lin_reload.setOnClickListener(reload);
    }

    //隐藏网络断开视图
    protected void setNoNetViewGone() {
        lin_no_net_data = (LinearLayout) findViewById(R.id.layout_no_net_data);
        lin_no_net_data.setVisibility(View.GONE);
        lin_no_net = (LinearLayout) findViewById(R.id.lin_no_net);
        lin_no_net.setVisibility(View.GONE);
    }

    /**
     * @param s
     */
    public void showToast(String s) {
        Toast.makeText(mActivity, s, Toast.LENGTH_LONG).show();

    }

    @Override
    public Context onAttach() {

        return mActivity;
    }

    @Override
    public boolean isShowLoad() {
        return true;
    }

    @Override
    public void isFinishActivity(String returnMsg) {
        mActivity.isFinishActivity(returnMsg);
    }

    /**
     * 删除List中重复元素，并保持顺序
     *
     * @param list 待去重的list
     * @return 去重后的list
     */
    protected static <T> List<T> removeDuplicateKeepOrder(List<T> list) {
        Set set = new HashSet();

        List<T> newList = new ArrayList<>();
        for (T element : list) {
            //set能添加进去就代表不是重复的元素
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
        return list;
    }


    /*************************************** 合格投资者弹框-start*******************************************************/
    UserLevelDialog userLevelDialog = null;

    public void showUserLevelDialog(String type, String isRealInvestor) {
        userLevelDialog = new UserLevelDialog(getActivity());

        userLevelDialog.setIsRealInvestor(isRealInvestor);
        userLevelDialog.setType(type);
        userLevelDialog.setOnClickListener(onClickListener);
        notice(type);
    }

    /**
     * dialog事件单击
     */
    protected UserLevelDialog.OnClickListener onClickListener = new UserLevelDialog.OnClickListener() {

        @Override
        public void onRecharge() {
            queryBankInfo();
        }

        @Override
        public void onMaterial(String isRealInvestor) {
            userLevelDialog.onDmiss();
            Intent intent = new Intent(mActivity, HighNetWorthMaterialsUploadedActivity.class);
            intent.putExtra("isRealInvestor", isRealInvestor);
            baseStartActivity(intent);
        }


    };

    public void onUserLevelNotice(Notices.Ret_PBAPP_notice retNotice) {
        userLevelDialog.setDataUpContent(retNotice.getData().getNotice().getButtonTitle());
        userLevelDialog.setNotice_title(retNotice.getData().getNotice().getNoticeTitle());
        userLevelDialog.setIsShow(retNotice.getData().getNotice().getIsShow());
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
        //充值渠道关闭
        if (bankCardInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(mActivity, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "301");
            baseStartActivity(intent);
        } else {
            //查询银行卡限额
            queryFundBankInfo(bankCardInfo.getData().getBankNo());
        }
    }

    public void onUserLevelBankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
        userLevelDialog.onDmiss();
        //限额
        if (fundBankInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(mActivity, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "302");
            baseStartActivity(intent);

        } else {
            //baseStartActivity(mActivity, RechargeActivity.class);
            Intent intent = new Intent(mActivity, RechargeActivity.class);
            startActivityForResult(intent, RECHARGE_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECHARGE_CODE && resultCode == RESULT_OK) {
            onRefresh();
        }
    }

    protected void onRefresh() {

    }

    private String payChannelNo = "";//支付渠道


    /*
     * 000非合格投资者弹框
     * 004恭喜弹框
     * 010伪合格投资者
     * */
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
                    showDialog(notice.getReturnMsg());
                }
            }
        });
    }


    //查询渠道关闭或查询银行卡信息
    public void queryBankInfo() {
        String url = presenter.parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.LoadRechargeBankCardInfo, BasePresenter.getRequestMap());
        presenter.addDisposable(presenter.apiServer.queryBankInfo(url), new BaseObserver<RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo>(this) {
            @Override
            public void onSuccess(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo) {
                if (bankCardInfo.getReturnCode().equals(ConstantCode.RETURN_CODE)) {
                    payChannelNo = bankCardInfo.getData().getPayChannelNo();
                    onUserLevelQueryRechargeBankInfo(bankCardInfo);
                } else {
                    showDialog(bankCardInfo.getReturnMsg());
                }

            }
        });

    }

    /**
     * 查询银行限额信息
     */
    public void queryFundBankInfo(final String bankNo) {
        FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.Builder fundBank = FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.newBuilder();
        fundBank.setBankCode(bankNo);
        fundBank.setPayChannel(payChannelNo);
        fundBank.setTransType("1");
        String url = presenter.parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryFundBankInfo);
        presenter.addDisposable(presenter.apiServer.queryFundBankInfo(url, BasePresenter.getBody(fundBank.build().toByteArray())), new ProtoBufObserver<FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo>(this) {
            @Override
            public void onSuccess(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
                if (fundBankInfo.getReturnCode().equals(ConstantCode.RETURN_CODE)) {
                    onUserLevelBankCardManage(fundBankInfo);
                } else {
                    showDialog(fundBankInfo.getReturnMsg());
                }

            }
        });
    }


    /**
     * 加载html标签
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


    /*************************************** 合格投资者弹框-end*******************************************************/


    /**
     * 身份认证
     *
     * @param verifyName            是否实名
     * @param certificateStatusType 实名认证状态
     **/
    protected void certificateStatusType(String verifyName, String certificateStatusType) {

        if (!verifyName.equals("1")) {
            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(mActivity, AddBankActivity.class);
                }
            });
        } else {
            switch (certificateStatusType) {
                case "1":
                case "2":
                    Intent intent = new Intent(getContext(), CertificationActivity.class);
                    intent.putExtra("isProfession", true);
                    baseStartActivity(intent);
                    break;
                case "3":
                    baseStartActivity(getContext(), CareerSelsectActivity.class);
                    break;
                case "4":
                    baseStartActivity(getContext(), CertificationActivity.class);
                    break;
                case "5":
                    baseStartActivity(getContext(), CertificationStatus.class);
                    break;
                case "6":
                    baseStartActivity(getContext(), CertificationActivity.class);
                    break;
                default:
                    break;
            }

        }

    }

    //跳转
    public void onJumpAction( @NonNull BaseCacheBean baseCacheBean) {


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

        if (shareBean != null){

            String jumpAction = shareBean.getJumpRule();

            //身份认证
            if (jumpAction.equals("authentication")) {

                UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());
                onAuthentication();

            }
            //合格投资者
            else if (jumpAction.equals("callPhoneQualifiedInvestor")) {
                UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());
                onQualifiedInvestor();

            }
            //产品列表
            else if (jumpAction.equals("subscribeList")) {
                UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());
                String keyword_name = shareBean.getLinkKeywordName();
                ProductDate.rest();
                ProductDate.keywordName = keyword_name;
                String uuids = shareBean.getKeyword();
                ProductDate.uuids = uuids;
                ((HomeActivity) mActivity).setProductDate();

            }
            //外链
            else if (jumpAction.equals("outerlink")) {

                    Intent intent = new Intent();

                    String funcUrl = shareBean.getFuncUrl();

                    //链接非空判断
                    if (StringUtils.isNotBlank(funcUrl)) {
                        UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());
                        //外链分享
                        if (funcUrl.indexOf("http://") >= 0 || funcUrl.indexOf("https://") >= 0) {

                            if (!baseCacheBean.getShareIsSure().equals("1")){
                                ShareBean bean = new ShareBean();
                                bean.setFuncUrl(shareBean.getFuncUrl());
                                bean.setIsShare(shareBean.getIsShare());
                                startWebActivity(bean);
                            }
                            else {
                                startWebActivity(shareBean);
                            }


                        }
                        // 站内产品详情跳转
                        else if (funcUrl.indexOf("product://") >= 0) {

                            funcUrl = funcUrl.replace("product://", "");
                            intent.setClass(getContext(), ProductCodeActivity.class);
                            intent.putExtra("productCode", funcUrl);
                            intent.putExtra("sellingStatus", "1");
                            baseStartActivity(intent);
                        }

                        // 站内产品列表跳转
                        else {
                            funcUrl = funcUrl.replaceAll("func://productlist://", "");
                            ProductDate.rest();
                            ProductDate.productName = funcUrl;
                            ((HomeActivity) mActivity).setProductDate();
                        }
                    }



            }
        }



    }


    //合格投资者申请
    private void onQualifiedInvestor() {

        presenter.addDisposable(presenter.getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>() {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userbaseinfoGetUserDetailInfo) {


                UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userbaseinfoGetUserDetailInfo.getData();

                String userType = userDetailInfo.getUserType();


                if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {
                    showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //去绑卡
                            dialog.dismiss();
                            baseStartActivity(mActivity, AddBankActivity.class);
                        }
                    });
                }
                //合格投资者审核中
                else if (userDetailInfo.getHighNetWorthStatus().equals("-1")) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。";
                    if (userDetailInfo.getIsAccreditedInvestor().equals("") || userDetailInfo.getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "您的合格投资者认定材料正在审核中";
                    }

                    showDialog(pmtInfo);
                } else {
                    // 合格投资者审核不通过
                    if (userDetailInfo.getHighNetWorthStatus().equals("0")) {

                        getUserHighNetWorthInfo(userDetailInfo.getIsRealInvestor());
                    }
                    //非合格投资者，合格投资者，高净值统一弹框
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

    //身份认证
    private void onAuthentication() {

        presenter.addDisposable(presenter.getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>() {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                certificateStatusType(userDetailInfo.getData().getVerifyName(), userDetailInfo.getData().getCertificateStatusType());
            }
        });

    }

    /**
     * 申请合格投资者失败原因
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
                showDialog(buffer.toString() + "".trim(), "重新上传", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent();
                        intent.putExtra("isRealInvestor", isRealInvestor);
                        intent.setClass(getActivity(), HighNetWorthMaterialsUploadedActivity.class);
                        baseStartActivity(intent);

                    }
                });

            }
        });

    }


}
