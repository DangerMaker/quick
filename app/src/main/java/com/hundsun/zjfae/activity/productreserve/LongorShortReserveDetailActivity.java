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
 * @Description:长期或者短期预约详情
 * @Author: zhoujianyu
 * @Time: 2018/9/18 17:05
 */
public class LongorShortReserveDetailActivity extends CommActivity<ReserveListDetailPresenter> implements ReserveListDetailView, View.OnClickListener {
    private String id;//产品id
    private RecyclerView attachment;
    private LinearLayout lin_pdf, attach_layout;
    private CheckBox product_check;
    private Button btn;
    private PurchaseDialogReserveDetail.Builder builder;
    private String orderProductCode = "", productTitle = "";
    public static int PAY_SUCCESS = 10;
    private String pay_money = "";
    private String smallestAmount = "";//起购金额
    private String mostAmount = "";//可预约金额
    private String depositRate = "";//保证金比例
    private String type = "";//用于区别是长期预约还是短期预约
    private LinearLayout lin_risk_level;
    private TextView tv_risk_level_name, tv_risk_level;
    private View lin_risk_level_view;
    //判断用户是否满足年龄
    private boolean age = true;
    //免责声明显示text
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
        setTitle("预约详情");
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
            showToast("网络异常");
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

                            showDialog("请先阅读并同意本产品相关协议");
                            return;
                        } else {
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPurchaseMoney(new PurchaseDialogReserveDetail.PurchaseMoney() {
                                @Override
                                public void purchaseMoney(boolean commit, DialogInterface dialog, String money) {
                                    if (orderProductCode == null || orderProductCode.equals("")) {
                                        showToast("产品id为空");
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
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPurchaseMoney(new PurchaseDialogReserveDetail.PurchaseMoney() {
                            @Override
                            public void purchaseMoney(boolean commit, DialogInterface dialog, String money) {
                                if (orderProductCode == null || orderProductCode.equals("")) {
                                    showToast("产品id为空");
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
     * 预约第一步检查预约金额结果
     *
     * @param commit           是否提交预约
     * @param orderProductCode 预约产品 code
     */
    @Override
    public void reserveProductPre(boolean commit, final String orderProductCode) {
        if (commit) {
            presenter.reserveProduct(orderProductCode);
        }
        //

    }

    /**
     * 确认预约
     */
    @Override
    public void reserveProduct(String returnCode, String returnMsg, String msg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //跳转支付界面
                Intent intent = new Intent(LongorShortReserveDetailActivity.this, ReservePayActivity.class);
                intent.putExtra("id", orderProductCode);
                intent.putExtra("money", pay_money);
                intent.putExtra("title", productTitle);
                intent.putExtra("type", type);
                startActivityForResult(intent, PAY_SUCCESS);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
        //产品标题
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(bean.getProductName());
        Utils.setTextViewGravity(tv_title);
        ((TextView) findViewById(R.id.tv_title)).setText(bean.getProductName());
        //起购金额
        ((TextView) findViewById(R.id.tv_money_buy)).setText(bean.getBuyerSmallestAmount() + "元");
        builder.setMessage(bean.getBuyerSmallestAmount());
        smallestAmount = bean.getBuyerSmallestAmount();
        mostAmount = bean.getBuyRemainAmount();
        depositRate = bean.getDepositRate();
        //递增金额
        ((TextView) findViewById(R.id.tv_money_add)).setText(bean.getUnActualPriceIncreases() + "元");
        //预期年化收益率
        ((TextView) findViewById(R.id.tv_percent)).setText(bean.getExpectedMaxAnnualRate() + "%");
        //期限
        ((TextView) findViewById(R.id.tv_date)).setText(bean.getDeadline() + "天");
        //预约开始时间
        ((TextView) findViewById(R.id.tv_reserve_time_start)).setText(bean.getOrderStartDate());
        //预约结束时间
        ((TextView) findViewById(R.id.tv_reserve_time_end)).setText(bean.getOrderEndDate());
        //募集开始时间
        ((TextView) findViewById(R.id.tv_raise_time_start)).setText(bean.getBuyStartDate());
        //募集结束时间
        ((TextView) findViewById(R.id.tv_raise_time_end)).setText(bean.getBuyEndDate());
        //是否允许转让
        ((TextView) findViewById(R.id.tv_transfer)).setText(bean.getIsTransferStr());
        //付息方式
        ((TextView) findViewById(R.id.tv_payment_way)).setText(bean.getPayStyle());
        //付息频率
        if (bean.getPayFrequency() == null || bean.getPayFrequency().equals("")) {
            ((TextView) findViewById(R.id.tv_payment_frequency)).setText("--");
        } else {
            ((TextView) findViewById(R.id.tv_payment_frequency)).setText(bean.getPayFrequency());
        }
        //风险级别
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
        //计息方式
        ((TextView) findViewById(R.id.tv_interest_way)).setText(bean.getYearDay());
        //可预约金额
        ((TextView) findViewById(R.id.tv_buyRemainAmount)).setText(bean.getBuyRemainAmount() + "元");
        //最高预约金额
        if (bean.getMostHoldAmount().equals("不限制")) {
            ((TextView) findViewById(R.id.tv_mostHoldAmount)).setText(bean.getMostHoldAmount());
        } else {
            ((TextView) findViewById(R.id.tv_mostHoldAmount)).setText(MoneyUtil.fmtMicrometer(bean.getMostHoldAmount()) + "元");
        }
    }


    @Override
    public void onUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userInfo, String accreditedBuyIs) {

        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userInfo.getData();
        String isRealInvestor = userDetailInfo.getIsRealInvestor();
        String userType = userDetailInfo.getUserType();
        if (userDetailInfo.getIsFundPasswordSet().equals("false")) {
            showDialog("您还尚未设置交易密码哦", "设置交易密码", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去设置交易密码
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getVerifyName().equals("1") && userType.equals("personal")) {
            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                }
            });
        }
        //合格投资者
        else if (accreditedBuyIs.equals("1")
                && !userDetailInfo.getIsAccreditedInvestor().equals("1")) {

            if (userType.equals("personal")) {

                if (userDetailInfo.getIsBondedCard().equals("false")) {
                    showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //去绑卡
                            dialog.dismiss();
                            baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                        }
                    });
                }


            }
            // 0审核不通过
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
            showDialog("还未进行风险评测，先去评测?", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("您的风险评测已经过期，请重新进行评测~", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
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
        showDialog(buffer.toString() + "".trim(), "重新上传", "取消", new DialogInterface.OnClickListener() {
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

    //购买预检查调用第二步,获取用户信息，判断
    private UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo;

    @Override
    public void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        this.userDetailInfo = userDetailInfo;
        String userType = userDetailInfo.getData().getUserType();
        if (userDetailInfo.getData().getIsFundPasswordSet().equals("false")) {
            showDialog("您还尚未设置交易密码哦", "设置交易密码", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去设置交易密码
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {

            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("还未进行风险评测，先去评测?", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("您的风险评测已经过期，请重新进行评测~", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
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
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else if (!isAccreditedInvestor.equals("1")) {
                // 合格投资者审核中
                if (highNetWorthStatus.equals("-1")) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。";
                    if (userDetailInfo.getData().getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "您的合格投资者认定材料正在审核中";
                    }
                    showDialog(pmtInfo);
                }
                // 合格投资者审核不通过
                else if (highNetWorthStatus.equals("0")) {
                    //查询原因
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
                    //附件单击
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "12");
                } else {
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }

        } else {
            if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                if (isAttachment) {
                    //附件单击
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
            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(LongorShortReserveDetailActivity.this, AddBankActivity.class);
                }
            });
        } else {
            showDialog(returnMsg, "去申请", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去申请合格投资者
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
        showDialog(returnMsg, "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.reserveProduct(orderProductCode);
            }
        });
    }

    @Override
    public void onRiskAssessment(String returnMsg) {
        showDialog(returnMsg, "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //去风评
                dialog.dismiss();
                baseStartActivity(LongorShortReserveDetailActivity.this, RiskAssessmentActivity.class);
            }
        });
    }

    @Override
    public void onPlayNoTransfer(String returnMsg) {

        showDialog(returnMsg, "我不买了", "继续交易", new DialogInterface.OnClickListener() {
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
        showDialog(returnMsg, "查看", "取消", new DialogInterface.OnClickListener() {
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

    //设置免责声明或者没有免责声明显示我已阅读并接受相关协议
    public void setDisclaimer() {
        if (ADSharePre.getConfiguration(ADSharePre.disclaimer, BaseCacheBean.class) == null) {
            String clicktext = "我已阅读并接受相关协议";
            tv_agreement.setText(clicktext);
            attach_layout.setOnClickListener(this);
            product_check.setClickable(false);
            return;
        }
        BaseCacheBean baseCacheBean = ADSharePre.getConfiguration(ADSharePre.disclaimer, BaseCacheBean.class);
        if (StringUtils.isNotBlank(baseCacheBean.getButton_title())) {
            CCLog.e("免责声明文字" + baseCacheBean.getButton_title());
            final SpannableStringBuilder full = new SpannableStringBuilder();
            String clickText1 = "我已阅读并接受相关协议及";
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
                    //super.updateDrawState(ds); --> 注释掉，就没有下划线了
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
            String clicktext = "我已阅读并接受相关协议";
            tv_agreement.setText(clicktext);
            attach_layout.setOnClickListener(this);
            product_check.setClickable(false);
        }


    }

}
