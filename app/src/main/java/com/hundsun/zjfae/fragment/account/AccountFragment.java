package com.hundsun.zjfae.fragment.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.AccountCenterActivity;
import com.hundsun.zjfae.activity.assetstream.AssetStreamActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.BankCardActivity;
import com.hundsun.zjfae.activity.mine.CompanyOfflineRechargeActivity;
import com.hundsun.zjfae.activity.mine.DiscountCouponActivity;
import com.hundsun.zjfae.activity.mine.EnvelopeActivity;
import com.hundsun.zjfae.activity.mine.FirstPlayPassWordActivity;
import com.hundsun.zjfae.activity.mine.OfflineRechargeActivity;
import com.hundsun.zjfae.activity.mine.OtherVouchersActivity;
import com.hundsun.zjfae.activity.mine.RechargeActivity;
import com.hundsun.zjfae.activity.mine.WithdrawalActivity;
import com.hundsun.zjfae.activity.moneymanagement.MyHoldingActivity;
import com.hundsun.zjfae.activity.moneymanagement.MyMoneyManagementActivity;
import com.hundsun.zjfae.activity.myinvitation.MyInvitationActivity;
import com.hundsun.zjfae.activity.mymessage.MyMessageActivity;
import com.hundsun.zjfae.activity.productreserve.ProductReserveActivity;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.cookies.PersistentCookieStore;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserSetting;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.fragment.BaseFragment;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.UserAssetsInfo;
import onight.zjfae.afront.gens.v2.CountMessage;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.MyDiscountCount;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.fragment.account
 * @ClassName: AccountFragment
 * @Description: ????????????Fragment
 * @Author: moran
 * @CreateDate: 2019/6/10 13:53
 * @UpdateUser: ????????????
 * @UpdateDate: 2019/6/10 13:53
 * @UpdateRemark: ???????????????
 * @Version: 1.0
 */
public class AccountFragment extends BaseFragment implements View.OnClickListener, AccountView {
    private AccountPresenter presenter;
    private TextView mobile, yesterdayProfit, allUnit, amount, tv_messagecount, certificateStatusType;
    private TextView yhq, availabler, availablet, real_investor;
    private CheckBox account_check;
    private ImageView certificateStatusTypeImg;
    private NestedScrollView scrollview;

    private LinearLayout certification_layout, blockchain_layout, invitation, bank_manage_layout;

    private UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = null;

    private boolean isExistCoupon = false, isBao = false;

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new AccountPresenter(this);
    }

    @Override
    protected void resetLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.account_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mActivity.isLogin) {
            presenter.queryAccount();
        }
//        presenter.queryFaceStatus();
    }

    @Override
    public void initWidget() {
        scrollview = (NestedScrollView) findViewById(R.id.scrollview);
        bank_manage_layout = (LinearLayout) findViewById(R.id.bank_manage_layout);
        bank_manage_layout.setOnClickListener(this);
        findViewById(R.id.login_out).setOnClickListener(this);
        findViewById(R.id.recharge_layout).setOnClickListener(this);
        findViewById(R.id.withdrawal_layout).setOnClickListener(this);
        findViewById(R.id.discount_coupon_layout).setOnClickListener(this);
        findViewById(R.id.envelope_layout).setOnClickListener(this);
        findViewById(R.id.other_vouchers_layout).setOnClickListener(this);
        findViewById(R.id.my_money_management).setOnClickListener(this);
        findViewById(R.id.account_center).setOnClickListener(this);
        findViewById(R.id.my_holding).setOnClickListener(this);
        blockchain_layout = (LinearLayout) findViewById(R.id.blockchain_layout);
        blockchain_layout.setOnClickListener(this);

        certification_layout = (LinearLayout) findViewById(R.id.certification_layout);
        certification_layout.setOnClickListener(this);
        certificateStatusType = (TextView) findViewById(R.id.certificateStatusType);
        certificateStatusTypeImg = (ImageView) findViewById(R.id.certificateStatusTypeImg);
        account_check = (CheckBox) findViewById(R.id.account_check);
        yesterdayProfit = (TextView) findViewById(R.id.yesterdayProfit);
        allUnit = (TextView) findViewById(R.id.allUnit);
        mobile = (TextView) findViewById(R.id.mobile);
        amount = (TextView) findViewById(R.id.amount);
        yhq = (TextView) findViewById(R.id.yhq);
        availabler = (TextView) findViewById(R.id.availabler);
        availablet = (TextView) findViewById(R.id.availablet);
        real_investor = (TextView) findViewById(R.id.real_investor);
        tv_messagecount = (TextView) findViewById(R.id.tv_messagecount);
        //????????????
        findViewById(R.id.product_reserve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(getContext(), ProductReserveActivity.class);
            }
        });
        //?????????
        findViewById(R.id.message_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(getContext(), TotalIncomeActivity.class);
            }
        });
        //????????????
        findViewById(R.id.account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(getContext(), AssetDetailActivity.class);
            }
        });
        //????????????
        findViewById(R.id.tv_asset_stream).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(getContext(), AssetStreamActivity.class);
            }
        });
        //????????????
        invitation = (LinearLayout) findViewById(R.id.invitation);
        invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.myInvitationClick();
            }
        });
        //????????????
        findViewById(R.id.my_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(getContext(), MyMessageActivity.class);
            }
        });
        //???????????????
        findViewById(R.id.img_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((HomeActivity) mActivity).showMenu();
            }
        });
    }


    @Override
    public void initData() {
        if (!BaseActivity.isLogin) {
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

            if (UserInfoSharePre.isOpenChain()) {

                if (blockchain_layout != null) {
                    blockchain_layout.setVisibility(View.VISIBLE);
                }
            } else {

                if (blockchain_layout != null) {
                    blockchain_layout.setVisibility(View.GONE);
                }
            }
        } else {
            if (blockchain_layout != null) {

                blockchain_layout.setVisibility(View.GONE);
            }

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.account_fragment_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //???????????????
            case R.id.bank_manage_layout:
                baseStartActivity(getContext(), BankCardActivity.class);
                break;
            //????????????
            case R.id.login_out:
                presenter.outLogin();
                break;
            //??????
            case R.id.recharge_layout:
                if (userDetailInfo != null) {
                    //?????????????????????
                    if (userDetailInfo.getIsFundPasswordSet().equals("false")) {
                        showDialog("?????????????????????????????????", "??????????????????", "??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //?????????????????????
                                dialog.dismiss();
                                baseStartActivity(mActivity, FirstPlayPassWordActivity.class);
                            }
                        });
                    } else if (userDetailInfo.getUserType().equals("personal")) {

                        if (userDetailInfo.getIsBondedCard().equals("false")) {

                            showDialog("??????????????????????????????????????????????????????", "?????????", "????????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //?????????
                                    dialog.dismiss();
                                    baseStartActivity(mActivity, AddBankActivity.class);
                                }
                            });
                        } else {
                            presenter.queryBankInfo();
                        }
                    }
                    //????????????--????????????
                    else if (userDetailInfo.getUserType().equals("company")) {

                        baseStartActivity(mActivity, CompanyOfflineRechargeActivity.class);

                    }

                }
                break;
            //??????
            case R.id.withdrawal_layout:

                if (userDetailInfo != null) {

                    //?????????????????????
                    if (userDetailInfo.getIsFundPasswordSet().equals("false")) {
                        showDialog("?????????????????????????????????", "??????????????????", "??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //?????????????????????
                                dialog.dismiss();
                                baseStartActivity(mActivity, FirstPlayPassWordActivity.class);
                            }
                        });
                    }
                    //????????????
                    else if (userDetailInfo.getUserType().equals("personal")) {

                        //?????????
                        if (userDetailInfo.getIsBondedCard().equals("false")) {

                            showDialog("??????????????????????????????????????????????????????", "?????????", "????????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //?????????
                                    dialog.dismiss();
                                    baseStartActivity(mActivity, AddBankActivity.class);
                                }
                            });

                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("isExistCoupon", isExistCoupon);
                            intent.putExtra("isBao", isBao);
                            intent.setClass(mActivity, WithdrawalActivity.class);
                            baseStartActivity(intent);
                        }

                    }
                    //????????????
                    else {

                        Intent intent = new Intent();
                        intent.putExtra("isExistCoupon", isExistCoupon);
                        intent.putExtra("isBao", isBao);
                        intent.setClass(mActivity, WithdrawalActivity.class);
                        baseStartActivity(intent);

                    }

                }
                break;
            //?????????
            case R.id.discount_coupon_layout:
                startActivity(new Intent(mActivity, DiscountCouponActivity.class));
                break;
            //??????
            case R.id.envelope_layout:
                startActivity(new Intent(mActivity, EnvelopeActivity.class));
                break;
            //????????????
            case R.id.other_vouchers_layout:
                baseStartActivity(getContext(), OtherVouchersActivity.class);
                break;
            //????????????
            case R.id.account_center:
                baseStartActivity(getContext(), AccountCenterActivity.class);
                break;
            //????????????
            case R.id.my_money_management:
                baseStartActivity(getContext(), MyMoneyManagementActivity.class);
                break;
            //????????????
            case R.id.my_holding:
                baseStartActivity(getContext(), MyHoldingActivity.class);
                break;
            //????????????
            case R.id.certification_layout:
                certificateStatusType(presenter.getVerifyName(), presenter.getCertificateStatusType());
                break;

            case R.id.blockchain_layout:

                startWebActivity(BasePresenter.BASE_CHAIN_URL);


                break;

            default:
                break;
        }
    }


    /**
     * ????????????????????????????????????
     *
     * @param pwRetMerges ?????????????????????????????????????????????????????????
     */
    @Override
    public void pWRetMerges(AllAzjProto.PWRetMerges pwRetMerges) {


        if (!pwRetMerges.getRetpackList().isEmpty()) {
            try {
                //??????????????????
                final UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo userAssetsInfo = UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo.parseFrom(pwRetMerges.getRetpack(0).getPbbody());
                //??????????????????
                UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo detailInfo = UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo.parseFrom(pwRetMerges.getRetpack(2).getPbbody());

                userDetailInfo = detailInfo.getData();

                String returnCode = userAssetsInfo.getReturnCode();
                String returnMsg = userAssetsInfo.getReturnMsg();

                if (returnCode.equals(ConstantCode.LOGIN_TIME_OUT)) {
                    showDialog(returnMsg, "?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            BaseActivity.isLogin = false;
                            PersistentCookieStore.getCookieStore().cleanCookie();
                            HomeActivity.show(mActivity, HomeActivity.HomeFragmentType.HOME_FRAGMENT);
                        }
                    });
                } else if (returnCode.equals(ConstantCode.SERVER_EXCEPTION)) {
                    showDialog(returnMsg);
                } else if (returnCode.equals("I29999")) {
                    showDialog(returnMsg);
                } else {

                    account_check.setChecked(UserSetting.isUserAmountVisible());
                    if (UserSetting.isUserAmountVisible()) {
                        yesterdayProfit.setText("****???");
                        allUnit.setText("****???>>");
                        amount.setText("****???>>");
                        checkUserInvestor(false);
                    } else {
                        yesterdayProfit.setText(userAssetsInfo.getData().getYesterdayProfit() + "???");
                        allUnit.setText(userAssetsInfo.getData().getAllUnit() + "???>>");
                        amount.setText(userAssetsInfo.getData().getAmount() + "???>>");

                        checkUserInvestor(true);
                    }
                    account_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                //???????????????,??????****
                                yesterdayProfit.setText("****???");
                                allUnit.setText("****???>>");
                                amount.setText("****???>>");
                                checkUserInvestor(false);
                                UserSetting.setUserAmountVisible(true);
                            } else {
                                //????????????,????????????
                                yesterdayProfit.setText(userAssetsInfo.getData().getYesterdayProfit() + "???");
                                allUnit.setText(userAssetsInfo.getData().getAllUnit() + "???>>");
                                amount.setText(userAssetsInfo.getData().getAmount() + "???>>");
                                checkUserInvestor(true);
                                UserSetting.setUserAmountVisible(false);
                            }
                        }
                    });
                    //??????????????????
                    CountMessage.Ret_PBIFE_messagemanage_countMessage countMessage = CountMessage.Ret_PBIFE_messagemanage_countMessage.parseFrom(pwRetMerges.getRetpack(1).getPbbody());
                    if (StringUtils.isNotBlank(countMessage.getData().getUnReadMessageCount())) {
                        if (countMessage.getData().getUnReadMessageCount().equals("0")) {
                            tv_messagecount.setVisibility(View.GONE);
                        } else {
                            tv_messagecount.setText(countMessage.getData().getUnReadMessageCount());
                            tv_messagecount.setVisibility(View.VISIBLE);
                        }
                    }


                    presenter.setVerifyName(userDetailInfo.getVerifyName());
                    presenter.setCertificateStatusType(userDetailInfo.getCertificateStatusType());

                    switch (presenter.getCertificateStatusType()) {
                        case "1":
                            certificateStatusType.setText("??????????????????");
                            break;
                        case "2":
                            certificateStatusType.setText("????????????????????????????????????");
                            break;
                        case "3":
                            certificateStatusType.setText("?????????????????????");
                            break;
                        case "4":
                            certificateStatusType.setText("??????????????????");
                            break;
                        case "5":
                            certificateStatusType.setText("?????????");
                            certificateStatusTypeImg.setVisibility(View.GONE);
                            break;
                        case "6":
                            certificateStatusType.setText("????????????????????????????????????");
                            break;
                        default:
                            break;
                    }

                    UserInfoSharePre.setAccount(userDetailInfo.getAccount());
                    UserInfoSharePre.setFundAccount(userDetailInfo.getFundAccount());
                    if (userDetailInfo.getUserType().equals("personal")) {

                        mobile.setText(userDetailInfo.getMobile());
                    } else {
                        mobile.setText(userDetailInfo.getFundAccount());
                    }

//                    if (!userDetailInfo.getUserGroup().equals("0")) {
//                        real_investor.setText("?????????" + userDetailInfo.getUserGroup());
//                    }

                    //????????????
                    MyDiscountCount.Ret_PBIFE_kq_getMyDiscountCount discountCount = MyDiscountCount.Ret_PBIFE_kq_getMyDiscountCount.parseFrom(pwRetMerges.getRetpack(3).getPbbody());
                    yhq.setText("?????????(" + discountCount.getData().getYhqCount() + ")");
                    availabler.setText("??????(" + discountCount.getData().getAvailableRCount() + ")");
                    availablet.setText("????????????(" + discountCount.getData().getAvailableTCount() + ")");

                    if (!discountCount.getData().getYhqCount().equals("0")) {
                        isExistCoupon = true;
                    } else {
                        isExistCoupon = false;
                    }

                    if (!discountCount.getData().getAvailableRCount().equals("0")) {
                        isBao = true;
                    } else {
                        isBao = false;
                    }


                    String userType = userDetailInfo.getUserType();

                    if (userType.equals("personal")) {
                        invitation.setVisibility(View.VISIBLE);
                        bank_manage_layout.setVisibility(View.VISIBLE);
                    } else {
                        invitation.setVisibility(View.GONE);
                        bank_manage_layout.setVisibility(View.GONE);
                        certification_layout.setVisibility(View.GONE);
                    }

                }


            } catch (
                    InvalidProtocolBufferException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * ???????????????????????????
     * isUserAmountVisible ????????????
     */
    private void checkUserInvestor(boolean isUserAmountVisible) {
        if (userDetailInfo.getIsAccreditedInvestor().equals("1")) {
            real_investor.setVisibility(View.VISIBLE);
            if (userDetailInfo.getUserGroup().equals("0")) {
                real_investor.setText("???????????????");
            } else {
                if (isUserAmountVisible) {
                    real_investor.setText("?????????" + userDetailInfo.getUserGroup());
                } else {
                    real_investor.setText("???????????????");
                }
            }
        } else {
            if (!userDetailInfo.getIsShow().equals("2")) {
                real_investor.setText("??????????????????");
                real_investor.setVisibility(View.VISIBLE);
            } else {
                real_investor.setVisibility(View.GONE);
            }
        }
    }


    private String bankName, bankCard;

    @Override
    public void queryRechargeBankInfo(String bankName, String bankCard, String bankNo, String showTips) {
        this.bankName = bankName;
        this.bankCard = bankCard;
        //??????????????????
        if (showTips.equals("1")) {
            Intent intent = new Intent(mActivity, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "301");
            baseStartActivity(intent);
        } else {
            //?????????????????????
            presenter.queryFundBankInfo(bankNo);
        }
    }

    @Override
    public void queryRechargeBankInfo(String bankName, String bankCard, String bankNo, String showTips, String payChannelNo) {
        presenter.setPayChannelNo(payChannelNo);
        queryRechargeBankInfo(bankName, bankCard, bankNo, showTips);
    }

    /**
     * ???????????????
     */
    @Override
    public void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
        if (fundBankInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(mActivity, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "302");
            baseStartActivity(intent);
        } else {
            baseStartActivity(mActivity, RechargeActivity.class);
        }
    }

    //??????????????????????????????
    @Override
    public void myInvitation(String url, String isShare) {
        if (StringUtils.isNotBlank(url)) {
            ShareBean shareBean = new ShareBean();
            shareBean.setFuncUrl(url);
            shareBean.setShareItem("0");
            shareBean.setIsShare(isShare);
            mActivity.startWebActivity(shareBean);
        } else {
            baseStartActivity(getContext(), MyInvitationActivity.class);
        }
    }

    @Override
    public void outLogin() {
        scrollview.scrollTo(0, 0);
        mActivity.outLogin(mActivity);
    }

    @Override
    public void onFaceStatus(boolean isTencentFace) {
        if (isTencentFace && UserInfoSharePre.getUserType().equals("personal")) {
            certification_layout.setVisibility(View.VISIBLE);
        } else {
            certification_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBankChannelClose(String bankName, String bankCard) {
        Intent intent = new Intent(mActivity, OfflineRechargeActivity.class);
        intent.putExtra("bankName", bankName);
        intent.putExtra("bankCard", bankCard);
        intent.putExtra("type", "301");
        baseStartActivity(intent);
    }

    @Override
    public void showLoading() {
        if (userDetailInfo == null) {
            super.showLoading();
        }

    }


}
