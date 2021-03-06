package com.hundsun.zjfae.activity.productreserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.RiskAssessmentActivity;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.FirstPlayPassWordActivity;
import com.hundsun.zjfae.activity.product.AgeFragmentDialog;
import com.hundsun.zjfae.activity.product.DisclaimerActivity;
import com.hundsun.zjfae.activity.product.HighActivity;
import com.hundsun.zjfae.activity.product.adapter.AttachmentAdapter;
import com.hundsun.zjfae.activity.productreserve.presenter.ReserveListDetailPresenter;
import com.hundsun.zjfae.activity.productreserve.view.ReserveListDetailView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.view.PurchaseDialogReserveDetail;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;

import java.util.List;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v3.ProductOrderInfoDetailPB;

/**
 * @Description:??????????????????????????????
 * @Author: zhoujianyu
 * @Time: 2018/9/18 17:05
 */
public class LongorShortReserveDetailActivity extends CommActivity<ReserveListDetailPresenter> implements ReserveListDetailView, View.OnClickListener {
    private String id;//??????id
    private RecyclerView attachment;
    private LinearLayout lin_pdf, attach_layout;
    private CheckBox product_check;
    private Button btn;
    private PurchaseDialogReserveDetail.Builder builder;
    private String orderProductCode = "", productTitle = "";
    public static int PAY_SUCCESS = 10;
    private String pay_money = "";
    private String smallestAmount = "";//????????????
    private String mostAmount = "";//???????????????
    private String depositRate = "";//???????????????
    private String type = "";//?????????????????????????????????????????????
    private LinearLayout lin_risk_level;
    private TextView tv_risk_level_name, tv_risk_level;
    private View lin_risk_level_view;
    //??????????????????????????????
    private boolean age = true;
    //??????????????????text
    private TextView tv_agreement;


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_longorshort_reserve_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_longor_short_reserve_detail;
    }

    @Override
    protected ReserveListDetailPresenter createPresenter() {
        return new ReserveListDetailPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("????????????");
        builder = new PurchaseDialogReserveDetail.Builder(this);
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        attachment = findViewById(R.id.attachment);
        lin_pdf = findViewById(R.id.lin_pdf);
        attach_layout = findViewById(R.id.attach_layout);
        tv_agreement = findViewById(R.id.tv_agreement);
        product_check = findViewById(R.id.product_check);
        btn = findViewById(R.id.btn);
        lin_risk_level = findViewById(R.id.lin_risk_level);
        tv_risk_level_name = findViewById(R.id.tv_risk_level_name);
        tv_risk_level = findViewById(R.id.tv_risk_level);
        lin_risk_level_view = findViewById(R.id.lin_risk_level_view);
        if (!NetworkCheck.isNetworkAvailable(this)) {
            setNoNetViewVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshData();
                }
            });
            return;
        }
        setDisclaimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (age) {
            refreshData();
        }
    }

    private void refreshData() {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("????????????");
            return;
        } else {
            setNoNetViewGone();
            presenter.allProductRequest(id);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utils.isFastDoubleClick()) {
                        return;
                    }
                    if (attach_layout.getVisibility() != View.GONE) {
                        if (!product_check.isChecked()) {

                            showDialog("??????????????????????????????????????????");
                            return;
                        } else {
                            builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPurchaseMoney(new PurchaseDialogReserveDetail.PurchaseMoney() {
                                @Override
                                public void purchaseMoney(boolean commit, DialogInterface dialog, String money) {
                                    if (orderProductCode == null || orderProductCode.equals("")) {
                                        showToast("??????id??????");
                                        return;
                                    }
                                    pay_money = money;
                                    presenter.reserveProductPre(commit, orderProductCode, money);

                                }
                            });
                            presenter.getUserDate();
                            builder.setSmallAndMostMoney(smallestAmount, mostAmount, depositRate);
                        }
                    } else {
                        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPurchaseMoney(new PurchaseDialogReserveDetail.PurchaseMoney() {
                            @Override
                            public void purchaseMoney(boolean commit, DialogInterface dialog, String money) {
                                if (orderProductCode == null || orderProductCode.equals("")) {
                                    showToast("??????id??????");
                                    return;
                                }
                                pay_money = money;
                                presenter.reserveProductPre(commit, orderProductCode, money);

                            }
                        });
                        presenter.getUserDate();
                        builder.setSmallAndMostMoney(smallestAmount, mostAmount, depositRate);
                    }
                }
            });
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param commit           ??????????????????
     * @param orderProductCode ???????????? code
     */
    @Override
    public void reserveProductPre(boolean commit, final String orderProductCode) {
        if (commit) {
            presenter.reserveProduct(orderProductCode);
        }
        //

    }

    /**
     * ????????????
     */
    @Override
    public void reserveProduct(String returnCode, String returnMsg, String msg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("????????????");
        builder.setMessage(msg);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //??????????????????
                Intent intent = new Intent(LongorShortReserveDetailActivity.this, ReservePayActivity.class);
                intent.putExtra("id", orderProductCode);
                intent.putExtra("money", pay_money);
                intent.putExtra("title", productTitle);
                intent.putExtra("type", type);
                startActivityForResult(intent, PAY_SUCCESS);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    public void onProductOrderInfo(final ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail productOrderInfoDetail) {
        final ProductOrderInfoDetailPB.PBIFE_trade_queryProductOrderInfoDetail.TaProductOrderInfo bean = productOrderInfoDetail.getData().getTaProductOrderInfo();
        orderProductCode = bean.getProductCode();
        productTitle = bean.getProductName();
        //????????????
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(bean.getProductName());
        Utils.setTextViewGravity(tv_title);
        ((TextView) findViewById(R.id.tv_title)).setText(bean.getProductName());
        //????????????
        ((TextView) findViewById(R.id.tv_money_buy)).setText(bean.getBuyerSmallestAmount() + "???");
        builder.setMessage(bean.getBuyerSmallestAmount());
        smallestAmount = bean.getBuyerSmallestAmount();
        mostAmount = bean.getBuyRemainAmount();
        depositRate = bean.getDepositRate();
        //????????????
        ((TextView) findViewById(R.id.tv_money_add)).setText(bean.getUnActualPriceIncreases() + "???");
        //?????????????????????
        ((TextView) findViewById(R.id.tv_percent)).setText(bean.getExpectedMaxAnnualRate() + "%");
        //??????
        ((TextView) findViewById(R.id.tv_date)).setText(bean.getDeadline() + "???");
        //??????????????????
        ((TextView) findViewById(R.id.tv_reserve_time_start)).setText(bean.getOrderStartDate());
        //??????????????????
        ((TextView) findViewById(R.id.tv_reserve_time_end)).setText(bean.getOrderEndDate());
        //??????????????????
        ((TextView) findViewById(R.id.tv_raise_time_start)).setText(bean.getBuyStartDate());
        //??????????????????
        ((TextView) findViewById(R.id.tv_raise_time_end)).setText(bean.getBuyEndDate());
        //??????????????????
        ((TextView) findViewById(R.id.tv_transfer)).setText(bean.getIsTransferStr());
        //????????????
        ((TextView) findViewById(R.id.tv_payment_way)).setText(bean.getPayStyle());
        //????????????
        if (bean.getPayFrequency() == null || bean.getPayFrequency().equals("")) {
            ((TextView) findViewById(R.id.tv_payment_frequency)).setText("--");
        } else {
            ((TextView) findViewById(R.id.tv_payment_frequency)).setText(bean.getPayFrequency());
        }
        //????????????
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelValue())) {
            tv_risk_level.setText(bean.getRiskLevelLabelValue());
            lin_risk_level.setVisibility(View.VISIBLE);
            lin_risk_level_view.setVisibility(View.VISIBLE);
        } else {
            lin_risk_level_view.setVisibility(View.GONE);
            lin_risk_level.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelName())) {
            tv_risk_level_name.setText(bean.getRiskLevelLabelName());
        }
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelUrl())) {
            tv_risk_level.setTextColor(getResources().getColor(R.color.blue));
            tv_risk_level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String accreditedBuyIs = bean.getAccreditedBuyIs();
                    presenter.attachmentUserInfo(accreditedBuyIs, null, productOrderInfoDetail.getData().getRatingTitle(), bean.getRiskLevelLabelUrl(), false);
                }
            });
        }
        //????????????
        ((TextView) findViewById(R.id.tv_interest_way)).setText(bean.getYearDay());
        //???????????????
        ((TextView) findViewById(R.id.tv_buyRemainAmount)).setText(bean.getBuyRemainAmount() + "???");
        //??????????????????
        if (bean.getMostHoldAmount().equals("?????????")) {
            ((TextView) findViewById(R.id.tv_mostHoldAmount)).setText(bean.getMostHoldAmount());
        } else {
            ((TextView) findViewById(R.id.tv_mostHoldAmount)).setText(MoneyUtil.fmtMicrometer(bean.getMostHoldAmount()) + "???");
        }
    }


    @Override
    public void onUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userInfo, String accreditedBuyIs) {

        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userInfo.getData();
        String isRealInvestor = userDetailInfo.getIsRealInvestor();
        String userType = userDetailInfo.getUserType();
        if (userDetailInfo.getIsFundPasswordSet().equals("false")) {
            showDialog("?????????????????????????????????", "??????????????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????????????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getVerifyName().equals("1") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                }
            });
        }
        //???????????????
        else if (accreditedBuyIs.equals("1")
                && !userDetailInfo.getIsAccreditedInvestor().equals("1")) {

            if (userType.equals("personal")) {

                if (userDetailInfo.getIsBondedCard().equals("false")) {
                    showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //?????????
                            dialog.dismiss();
                            baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                        }
                    });
                }


            }
            // 0???????????????
            if (userDetailInfo.getHighNetWorthStatus().equals("0")) {
                presenter.requestInvestorStatus(isRealInvestor);
            } else if (!userDetailInfo.getHighNetWorthStatus().equals("-1")) {

                if (userDetailInfo.getUserType().equals("personal")) {
                    showUserLevelDialog("000", isRealInvestor);
                } else {
                    showUserLevelDialog("020", isRealInvestor);
                }


                //showUserLevelDialog("000", userDetailInfo.getIsRealInvestor());

            }

        } else if (userDetailInfo.getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        }

    }


    @Override
    public void onAttachmentInfo(Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentList, final String accreditedBuyIs) {
        final List<Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList>
                list = attachmentList.getData().getTaProductAttachmentListList();
        if (list == null || list.size() <= 0) {
            attach_layout.setVisibility(View.GONE);
            lin_pdf.setVisibility(View.GONE);
        } else {
            AttachmentAdapter adapter = new AttachmentAdapter(this, list);
            adapter.setOnItemClickListener(new AttachmentAdapter.OnItemListener() {
                @Override
                public void onItemClick(int position) {
                    presenter.attachmentUserInfo(accreditedBuyIs, list.get(position), "", "", true);
                }
            });
            attachment.setLayoutManager(new LinearLayoutManager(this));
            attachment.setAdapter(adapter);
            attachment.addItemDecoration(new DividerItemDecorations());

            attach_layout.setVisibility(View.VISIBLE);
            lin_pdf.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.NestedScrollView).setVisibility(View.VISIBLE);
        btn.setEnabled(true);
    }

    @Override
    public void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, final String isRealInvestor) {
        StringBuffer buffer = new StringBuffer();
        for (UserHighNetWorthInfo.DictDynamic dynamic : body.getData().getDictDynamicListList()) {
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
                intent.setClass(LongorShortReserveDetailActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
            }
        });
    }

    //??????????????????????????????,???????????????????????????
    private UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo;

    @Override
    public void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        this.userDetailInfo = userDetailInfo;
        String userType = userDetailInfo.getData().getUserType();
        if (userDetailInfo.getData().getIsFundPasswordSet().equals("false")) {
            showDialog("?????????????????????????????????", "??????????????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????????????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {

            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else {
            builder.create().show();
        }
    }


    @Override
    public void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, boolean isAttachment) {
        final String isAccreditedInvestor = userDetailInfo.getData().getIsAccreditedInvestor();
        final String isRealInvestor = userDetailInfo.getData().getIsRealInvestor();
        final String highNetWorthStatus = userDetailInfo.getData().getHighNetWorthStatus();
        String userType = userDetailInfo.getData().getUserType();
        if (accreditedBuyIs.equals("1") && !isAccreditedInvestor.equals("1")) {

            if (userDetailInfo.getData().getIsBondedCard().equals("false") && userType.equals("personal")) {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????????
                        dialog.dismiss();
                        baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else if (!isAccreditedInvestor.equals("1")) {
                // ????????????????????????
                if (highNetWorthStatus.equals("-1")) {
                    String pmtInfo = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                    if (userDetailInfo.getData().getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "????????????????????????????????????????????????";
                    }
                    showDialog(pmtInfo);
                }
                // ??????????????????????????????
                else if (highNetWorthStatus.equals("0")) {
                    //????????????
                    presenter.requestInvestorStatus(isRealInvestor);
                } else {


                    //showUserLevelDialog("000", isRealInvestor);
                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }
                }
            } else {
                if (isAttachment) {
                    //????????????
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "12");
                } else {
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }

        } else {
            if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????????
                        dialog.dismiss();
                        baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                if (isAttachment) {
                    //????????????
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "12");
                } else {
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }
        }
    }


    @Override
    public void onHighAge() {
        age = false;
        final AgeFragmentDialog ageFragmentDialog = new AgeFragmentDialog();

        ageFragmentDialog.setOnClickListener(new AgeFragmentDialog.OnClickListener() {
            @Override
            public void isReadProtocol(boolean isRead) {
                ageFragmentDialog.dismissDialog();
                if (isRead) {
                    presenter.ageRequest();
                }
            }

            @Override
            public void cancel() {
                ageFragmentDialog.dismissDialog();
                finish();
                //finishActivity();
            }
        });
        ageFragmentDialog.show(getSupportFragmentManager());
    }

    @Override
    public void onQualifiedMember(String returnMsg) {
        final String userType = UserInfoSharePre.getUserType();
        if (userDetailInfo.getData().getIsBondedCard().equals("false") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                }
            });
        } else {
            showDialog(returnMsg, "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //????????????????????????
                    dialog.dismiss();
                    if (userType.equals("personal")) {

                        showUserLevelDialog("000", userDetailInfo.getData().getIsRealInvestor());
                    } else {
                        showUserLevelDialog("020", userDetailInfo.getData().getIsRealInvestor());
                    }

                }
            });
        }
    }

    @Override
    public void onNovicePrompt(String returnMsg) {
        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.reserveProduct(orderProductCode);
            }
        });
    }

    @Override
    public void onRiskAssessment(String returnMsg) {
        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //?????????
                dialog.dismiss();
                baseStartActivity(LongorShortReserveDetailActivity.this, RiskAssessmentActivity.class);
            }
        });
    }

    @Override
    public void onPlayNoTransfer(String returnMsg) {

        showDialog(returnMsg, "????????????", "????????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.reserveProduct(orderProductCode);
            }
        });
    }

    @Override
    public void onSeniorMember(String returnMsg) {
        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                baseStartActivity(LongorShortReserveDetailActivity.this, HighActivity.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAY_SUCCESS) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.attach_layout:
                if (product_check.isChecked()) {
                    product_check.setChecked(false);
                } else {
                    product_check.setChecked(true);
                }
                break;

            default:
                break;
        }
    }

    //?????????????????????????????????????????????????????????????????????????????????
    public void setDisclaimer() {
        if (ADSharePre.getConfiguration(ADSharePre.disclaimer, BaseCacheBean.class) == null) {
            String clicktext = "?????????????????????????????????";
            tv_agreement.setText(clicktext);
            attach_layout.setOnClickListener(this);
            product_check.setClickable(false);
            return;
        }
        BaseCacheBean baseCacheBean = ADSharePre.getConfiguration(ADSharePre.disclaimer, BaseCacheBean.class);
        if (StringUtils.isNotBlank(baseCacheBean.getButton_title())) {
            CCLog.e("??????????????????" + baseCacheBean.getButton_title());
            final SpannableStringBuilder full = new SpannableStringBuilder();
            String clickText1 = "????????????????????????????????????";
            String clickText2 = baseCacheBean.getButton_title();
            final SpannableStringBuilder before = new SpannableStringBuilder();
            final SpannableStringBuilder after = new SpannableStringBuilder();
            before.append(clickText1);
            before.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    if (product_check.isChecked()) {
                        product_check.setChecked(false);
                    } else {
                        product_check.setChecked(true);
                    }
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    //super.updateDrawState(ds); --> ?????????????????????????????????
                }
            }, 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            before.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, before.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            after.append(clickText2);
            after.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    baseStartActivity(LongorShortReserveDetailActivity.this, DisclaimerActivity.class);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                }
            }, 0, after.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            after.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, after.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            full.append(before).append(after).append("\u200b");
            tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
            tv_agreement.setText(full);
            product_check.setClickable(true);
        } else {
            String clicktext = "?????????????????????????????????";
            tv_agreement.setText(clicktext);
            attach_layout.setOnClickListener(this);
            product_check.setClickable(false);
        }


    }

}
