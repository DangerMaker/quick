package com.hundsun.zjfae.activity.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.RiskAssessmentActivity;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.FirstPlayPassWordActivity;
import com.hundsun.zjfae.activity.product.adapter.AttachmentAdapter;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.activity.product.presenter.TransferDetailPresenter;
import com.hundsun.zjfae.activity.product.view.TransferDetailView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.utils.RMBUtils;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.view.dialog.AssigneePaymentDialog;
import com.hundsun.zjfae.common.view.dialog.EarningsDialog;

import java.util.List;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.TransferBuyProfits;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v2.PBIFEPrdtransferqueryPrdDeliveryInfoDetail;
import onight.zjfae.afront.gens.v2.TransferOrderSec;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.product
 * @ClassName: TransferDetailActivity
 * @Description 转让详情界面
 * @Author moran
 * @CreateDate 2019/6/10 12:21
 * @UpdateUser 更新者： 
 * @UpdateDate 2019/6/10 12:21
 * @UpdateRemark 更新说明：
 * @Version: 1.0
 */
public class TransferDetailActivity extends CommActivity<TransferDetailPresenter> implements View.OnClickListener, TransferDetailView {
    private TextView product_name, expectedMaxAnnualRate, riskLevel, riskLevel_name, transferFloatEnd;
    private TextView productStatusStr, subjectTypeName, payStyle;
    private TextView manageEndDate, nextPayDate, holdDayNum;
    private TextView buySmallestAmount, yearDay, isTransferStr;
    private TextView tradeLeastHoldDay, buyerSmallestAmount, canBuyNum;
    private TextView tradeIncrease;

    private RecyclerView attachment;
    private ScrollView product_scroll;

    private TextView earnings, amount, floatingProfit, transferIncome, extraCost;

    private CheckBox product_check;

    private boolean isBuy = false;

    private TransferDetailPlay playInfo;

    private LinearLayout attach_layout, ll_isWholeTransfer;

    private Button bt_purchase;


    private static int requestCodes = 0x701;


    private ImageView doubt;
    /**
     * 免责声明显示text
     */
    private TextView tv_agreement;
    private TextView tv_tips;

    private String yearDayValue = "365";

    /**
     * 转让详情
     */
    @Override
    public void onTransferData(final PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail productDetails) {
        final PBIFEPrdtransferqueryPrdDeliveryInfoDetail.PBIFE_prdtransferquery_prdDeliveryInfoDetail.TaProductFinanceDetail detailBean = productDetails.getData().getTaProductFinanceDetail();

        PBIFEPrdtransferqueryPrdDeliveryInfoDetail.PBIFE_prdtransferquery_prdDeliveryInfoDetail.TcDelegation delegation = productDetails.getData().getTcDelegation();

        PBIFEPrdtransferqueryPrdDeliveryInfoDetail.PBIFE_prdtransferquery_prdDeliveryInfoDetail deliveryInfoDetail = productDetails.getData().getDefaultInstanceForType();


        playInfo.setProductCode(detailBean.getProductCode());
        playInfo.setNextPayDate(productDetails.getData().getNextPayDate());
        playInfo.setCanBuyNum(productDetails.getData().getCanBuyNum());
        playInfo.setDelegateNum(delegation.getDelegateNum());
        playInfo.setTargetRate(delegation.getTargetRate());
        playInfo.setLeftDays(productDetails.getData().getLeftDays());
        playInfo.setDelegationCode(delegation.getDelegationCode());
        playInfo.setLeastTranAmount(detailBean.getLeastTranAmount());
        playInfo.setActualRate(delegation.getActualRate());
        playInfo.setIsWholeTransfer(detailBean.getIsWholeTransfer());


        if (StringUtils.isNotBlank(detailBean.getYearDay())) {
            yearDayValue = detailBean.getYearDay().substring(4);
        }
        product_name.setText(detailBean.getProductName());
        playInfo.setSerialNoStr(detailBean.getSerialNo());
        playInfo.setProductCode(detailBean.getProductCode());
        expectedMaxAnnualRate.setText(detailBean.getExpectedMaxAnnualRate() + "%");
        playInfo.setProductName(detailBean.getProductName());
        playInfo.setExpectedMaxAnnualRate(detailBean.getExpectedMaxAnnualRate());
        //风险等级设置
        if (StringUtils.isNotBlank(detailBean.getRiskLevelLabelValue())) {
            riskLevel.setText(detailBean.getRiskLevelLabelValue());
            riskLevel.setVisibility(View.VISIBLE);
            riskLevel_name.setVisibility(View.VISIBLE);
        } else {
            riskLevel.setVisibility(View.GONE);
            riskLevel_name.setVisibility(View.GONE);
        }

        String isWholeTransfer = detailBean.getIsWholeTransfer();
        //是否是整体转让产品
        if ("1".equals(isWholeTransfer)) {
            ll_isWholeTransfer.setVisibility(View.VISIBLE);
        } else {
            ll_isWholeTransfer.setVisibility(View.GONE);
        }

        if (StringUtils.isNotBlank(detailBean.getRiskLevelLabelName())) {
            riskLevel_name.setText(detailBean.getRiskLevelLabelName());
        }
        if (StringUtils.isNotBlank(detailBean.getRiskLevelLabelURL())) {
            riskLevel.setTextColor(getResources().getColor(R.color.blue));
            riskLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String accreditedBuyIs = detailBean.getAccreditedBuyIs();
                    presenter.attachmentUserInfo(accreditedBuyIs, null, productDetails.getData().getRatingTitle(), detailBean.getRiskLevelLabelURL(), false);
                }
            });
        }

        //是否须一次全额购买
        String ifAllBuy = playInfo.getIfAllBuy();
        if (ifAllBuy.equals("1")) {
            isBuy = true;
            earnings.setText(playInfo.getDelegateNum() + "元(不可修改)");
            //设置支付金额
            playInfo.setPlayAmount(playInfo.getDelegateNum());
            presenter.initTransferData(playInfo.getProductCode(), playInfo.getDelegationCode(), playInfo.getDelegateNum(), playInfo.getActualRate());
        } else {
            presenter.initTransferData(playInfo.getProductCode(), playInfo.getDelegationCode());
            earnings.setOnClickListener(this);
        }
        //金额转换
        transferFloatEnd.setText(playInfo.getActualRate() + "%");
        productStatusStr.setText(detailBean.getProductStatusStr());
        subjectTypeName.setText(detailBean.getSubjectTypeName());
        payStyle.setText(detailBean.getPayStyle());
        manageEndDate.setText(detailBean.getManageEndDate());
        buySmallestAmount.setText(playInfo.getDelegateNum() + "元");

        yearDay.setText(detailBean.getYearDay());
        isTransferStr.setText(detailBean.getIsTransferStr());
        tradeLeastHoldDay.setText(detailBean.getTradeLeastHoldDay() + "个交易日");
        buyerSmallestAmount.setText(detailBean.getLeastHoldAmount() + "元");
        tradeIncrease.setText(detailBean.getTradeIncrease() + "元");

        nextPayDate.setText(playInfo.getNextPayDate());
        holdDayNum.setText(productDetails.getData().getDiffDays() + "天");
        this.canBuyNum.setText(playInfo.getCanBuyNum());
        if (playInfo.getCanBuyNum().equals("无限制")) {
            this.canBuyNum.setText("无限制");
        } else if (playInfo.getCanBuyNum().equals("0")) {
            doubt.setVisibility(View.VISIBLE);
            doubt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog("当前产品持有人数已达上限，如果您没有持有该产品，可能会交易失败。（请先尝试交易，再充值）", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                }
            });



        }

        //是否展示提前兑付相关信息
        if (productDetails.getData().getIsShowHonourAdvance().equals("1")) {
            tv_tips.setVisibility(View.VISIBLE);
            tv_tips.setText(productDetails.getData().getHonourAdvanceTips());
        } else {
            tv_tips.setVisibility(View.GONE);
        }

    }

    /**
     * 用户详细信息
     */
    @Override
    public void onUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userInfo, String accreditedBuyIs) {

        //添加用户手机号，转让购买字段上送
        playInfo.setMobile(userInfo.getData().getMobile());

        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userInfo.getData();
        //风险测评等级
        String riskLevel = userDetailInfo.getRiskLevel();
        //风险测评有效期
        String riskExpiredDate = userDetailInfo.getRiskExpiredDate();
        //是否风评过
        String isRiskTest = userDetailInfo.getIsRiskTest();

        String userType = userInfo.getData().getUserType();
        //需要评测的intent
        final Intent intentRisk = new Intent(TransferDetailActivity.this, RiskAssessmentActivity.class);
        intentRisk.putExtra("riskLevel", riskLevel);
        intentRisk.putExtra("riskExpiredDate", riskExpiredDate);
        intentRisk.putExtra("isRiskTest", isRiskTest);
        if (userDetailInfo.getIsFundPasswordSet().equals("false")) {
            showDialog("您还尚未设置交易密码哦", "设置交易密码", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去设置交易密码
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getVerifyName().equals("1") && userType.equals("personal")) {
            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                }
            });
        }
        //合格投资者
        else if (accreditedBuyIs.equals("1") && !userDetailInfo.getIsAccreditedInvestor().equals("1")) {
            if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                // 0审核不通过
                if (userDetailInfo.getHighNetWorthStatus().equals("0")) {
                    presenter.requestInvestorStatus(userDetailInfo.getIsRealInvestor());

                } else if (!userDetailInfo.getHighNetWorthStatus().equals("-1")) {

                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", "");
                    } else {
                        showUserLevelDialog("020", "");
                    }


                }


            }

        } else if (userDetailInfo.getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("还未进行风险评测，先去评测?", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
                    dialog.dismiss();
                    baseStartActivity(intentRisk);
                }
            });
        } else if (userDetailInfo.getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("您的风险评测已经过期，请重新进行评测~", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
                    dialog.dismiss();
                    baseStartActivity(intentRisk);
                }
            });
        }

    }

    /**
     * 请求本金信息
     **/
    @Override
    public void onEarningsInfo(TransferBuyProfits.Ret_PBIFE_trade_queryTransferBuyProfits transferBuyProfits) {
        isBuy = true;
        TransferBuyProfits.PBIFE_trade_queryTransferBuyProfits dataBean = transferBuyProfits.getData();
        amount.setText(dataBean.getAmount() + "元");
        floatingProfit.setText(dataBean.getFloatingProfit() + "元");
        transferIncome.setText(dataBean.getTransferIncome() + "元");
        extraCost.setText(dataBean.getExtraCost() + "元");
    }

    /**
     * 附件列表
     */
    @Override
    public void onAttachmentInfo(Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentList, final String accreditedBuyIs) {

        final List<Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList>
                list = attachmentList.getData().getTaProductAttachmentListList();
        if (list == null || list.isEmpty()) {
            attach_layout.setVisibility(View.GONE);
            return;
        }
        AttachmentAdapter adapter = new AttachmentAdapter(TransferDetailActivity.this, list);
        adapter.setOnItemClickListener(new AttachmentAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position) {

                presenter.attachmentUserInfo(accreditedBuyIs, list.get(position), "", "", true);


            }
        });
        attachment.setLayoutManager(new LinearLayoutManager(TransferDetailActivity.this));
        attachment.setAdapter(adapter);
        attachment.addItemDecoration(new DividerItemDecorations());

    }


    /**
     * 附件单击
     */
    @Override
    public void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, Boolean isAttachment) {
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
                        baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
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

                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }

                    //showUserLevelDialog("000", isRealInvestor);

                }
            } else {
                if (isAttachment) {
                    //附件单击
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "9");
                } else {
                    //信用评级
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
                        baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                if (isAttachment) {
                    //附件单击
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "9");
                } else {
                    //信用评级
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }
        }
    }


    /**
     * 购买预检查第二步
     */
    @Override
    public void getTransferOrderSecBean(TransferOrderSec.Ret_PBIFE_trade_transferOrderSec orderSecBean) {

        if (playInfo.getIsWholeTransfer().equals("1")) {

            showDialog("该产品不可拆分转让/受让，请确认是否购买", "确认", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    Intent intent = new Intent(TransferDetailActivity.this, TransferDetailPlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("playBean", playInfo);
                    intent.putExtra("playBundle", bundle);
                    startActivityForResult(intent, requestCodes);
                }
            });
        } else {
            Intent intent = new Intent(this, TransferDetailPlayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("playBean", playInfo);
            intent.putExtra("playBundle", bundle);
            startActivityForResult(intent, requestCodes);
        }


    }

    /**
     * 合格投资者失败原因
     */
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
                intent.setClass(TransferDetailActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
                finish();
            }
        });
    }

    /**
     * 预检查第二步
     */
    @Override
    public void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        String userType = userDetailInfo.getData().getUserType();
        if (userDetailInfo.getData().getIsFundPasswordSet().equals("false")) {
            showDialog("您还尚未设置交易密码哦", "设置交易密码", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去设置交易密码
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {

            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("还未进行风险评测，先去评测?", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("您的风险评测已经过期，请重新进行评测~", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else {
            presenter.getTransferOrderSec(playInfo.getPlayAmount(), playInfo.getDelegationCode());
        }

    }

    /**
     * 申请合格投资者
     */
    @Override
    public void onQualifiedMember(String returnMsg) {

        String userType = UserInfoSharePre.getUserType();
        if (presenter.getIsBondedCard().equals("false") && userType.equals("personal")) {
            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                }
            });
        } else {
            showDialog(returnMsg, "立即申请", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String isRealInvestor = presenter.getIsRealInvestor();

                    String userType = UserInfoSharePre.getUserType();

                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }

                    //showUserLevelDialog("000", isRealInvestor);
                }
            });

        }
    }


    /**
     * 去风险测评
     */
    @Override
    public void onRiskAssessment(String returnMsg) {
        showDialog(returnMsg, "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //去风评
                dialog.dismiss();
                baseStartActivity(TransferDetailActivity.this, RiskAssessmentActivity.class);
            }
        });
    }

    /**
     * 高净值
     */
    @Override
    public void onSeniorMember(String returnMsg) {
        showDialog(returnMsg, "查看", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                baseStartActivity(TransferDetailActivity.this, HighActivity.class);
            }
        });
    }


    /**
     * 65周岁
     */
    @Override
    public void onHighAge() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodes && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bt_purchase:
                if (attach_layout.getVisibility() != View.GONE) {
                    if (!product_check.isChecked()) {
                        showDialog("请先阅读并同意本产品相关协议");
                        product_scroll.fullScroll(ScrollView.FOCUS_DOWN);
                    } else {
                        if (isBuy) {
                            presenter.getUserDate();
                        } else {
                            buy();
                        }
                    }
                } else {
                    if (isBuy) {
                        presenter.getUserDate();
                    } else {
                        buy();
                    }
                }

                break;
            case R.id.earnings:
                buy();
                break;


            case R.id.lin_holdDayNum:
                showDialog("指本次付息周期计息起始日至交易当天前一自然日的实际天数。");
                break;
            case R.id.amount_img:
                showDialog("购买总价=购买本金+应付利息" + "\n" + "（不含手续费）", "确定");
                break;
            case R.id.floatingProfit_img:

                showDialog("本次交易浮动盈亏：本次付息周期内，以当前转让利率所获收益相比较产品预期年化收益率所获收益的差额。计算公式：转让本金 × (预期年化收益率-转让利率) × 本期存续天数 ÷ "+yearDayValue, "确定");

                break ;
            case R.id.transferIncome_img:
                showDialog("应付利息：应付给转让方的利息。计算公式：购买本金×转让利率 × 本期存续天数 ÷ "+yearDayValue, "确定");
                break;
            case R.id.extraCost_img:
                showDialog("成交金额不足5万元的，买卖双方各向浙金中心支付每笔2元交易手续费；成交金额5万元(含)以上的，买卖双方各向浙金中心支付每笔5元交易手续费", "确定");
                break;

            case R.id.attach_layout:
                if (product_check.isChecked()) {
                    product_check.setChecked(false);
                } else {
                    product_check.setChecked(true);
                }

                break;
            case R.id.calculator:
                EarningsDialog.Builder builder = new EarningsDialog.Builder(this);
                builder.setTimeLimit(playInfo.getLeftDays());
                builder.setAmount(playInfo.getDelegateNum());
                builder.setRate(playInfo.getTargetRate());
                EarningsDialog earningsDialog = builder.create();
                earningsDialog.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = earningsDialog.getWindow().getAttributes();
                //设置宽度
                lp.width = (int) (display.getWidth() * 0.75);
                earningsDialog.getWindow().setAttributes(lp);
                break;
            default:
                break;
        }
    }

    private void buy() {
        AssigneePaymentDialog.Builder builder = new AssigneePaymentDialog.Builder(this);
        builder.setMessage("最低受让" + RMBUtils.formatWanNum(playInfo.getLeastTranAmount()).replace("元", ""));
        builder.setTitle("请输入交易本金");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //获取信息
        builder.setEarningsCallback("确认", new AssigneePaymentDialog.EarningsCallback() {
            @Override
            public void earnings(DialogInterface dialog, String money) {
                dialog.dismiss();

                if (StringUtils.isEmpty(money)) {

                    showDialog("输入金额不能为空");

                } else {
                    earnings.setText(money + "元(点击修改)");
                    playInfo.setPlayAmount(money);
                    presenter.getEarnings(playInfo.getProductCode(), money, playInfo.getActualRate(), playInfo.getDelegationCode());
                }
            }
        });
        builder.create().show();


    }


    @Override
    public void initData() {
        Bundle bundle = getIntent().getBundleExtra("playBundle");
        playInfo = bundle.getParcelable("playInfo");

        presenter.getTransferDetailInfo(playInfo.getDelegationId());

        if (playInfo.isMyEntry()) {
            bt_purchase.setClickable(false);
            bt_purchase.setText("我的挂单");
            bt_purchase.setBackgroundResource(R.drawable.product_buy);
        } else {
            bt_purchase.setClickable(true);
        }


    }


    @Override
    public void initView() {
        setTitle("转让详情");
        product_scroll = findViewById(R.id.product_scroll);
        bt_purchase = findViewById(R.id.bt_purchase);
        bt_purchase.setOnClickListener(this);
        product_name = findViewById(R.id.product_name);
        expectedMaxAnnualRate = findViewById(R.id.expectedMaxAnnualRate);
        riskLevel = findViewById(R.id.riskLevel);
        riskLevel_name = findViewById(R.id.riskLevel_name);
        transferFloatEnd = findViewById(R.id.transferFloatEnd);
        productStatusStr = findViewById(R.id.productStatusStr);
        subjectTypeName = findViewById(R.id.subjectTypeName);
        payStyle = findViewById(R.id.payStyle);
        manageEndDate = findViewById(R.id.manageEndDate);
        nextPayDate = findViewById(R.id.nextPayDate);
        holdDayNum = findViewById(R.id.holdDayNum);


        buySmallestAmount = findViewById(R.id.buySmallestAmount);
        yearDay = findViewById(R.id.yearDay);
        isTransferStr = findViewById(R.id.isTransferStr);
        tradeLeastHoldDay = findViewById(R.id.tradeLeastHoldDay);
        canBuyNum = findViewById(R.id.canBuyNum);

        buyerSmallestAmount = findViewById(R.id.buyerSmallestAmount);


        tradeIncrease = findViewById(R.id.tradeIncrease);

        earnings = findViewById(R.id.earnings);

        attachment = findViewById(R.id.attachment);

        amount = findViewById(R.id.amount);
        floatingProfit = findViewById(R.id.floatingProfit);
        transferIncome = findViewById(R.id.transferIncome);
        extraCost = findViewById(R.id.extraCost);
        product_check = findViewById(R.id.product_check);
        attach_layout = findViewById(R.id.attach_layout);
        tv_agreement = findViewById(R.id.tv_agreement);
        tv_tips = findViewById(R.id.tv_tips);
        doubt = findViewById(R.id.doubt);

        findViewById(R.id.lin_holdDayNum).setOnClickListener(this);
        findViewById(R.id.amount_img).setOnClickListener(this);
        findViewById(R.id.floatingProfit_img).setOnClickListener(this);
        findViewById(R.id.transferIncome_img).setOnClickListener(this);
        findViewById(R.id.extraCost_img).setOnClickListener(this);
        findViewById(R.id.calculator).setOnClickListener(this);
        ll_isWholeTransfer = findViewById(R.id.ll_isWholeTransfer);
        setDisclaimer();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_detail;
    }


    @Override
    protected TransferDetailPresenter createPresenter() {
        return new TransferDetailPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_transfer_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    /**
     * 设置免责声明或者没有免责声明显示我已阅读并接受相关协议
     */
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
            }, 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 1. click
            before.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, before.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            after.append(clickText2);
            after.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    baseStartActivity(TransferDetailActivity.this, DisclaimerActivity.class);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                }
            }, 0, after.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 1. click
            after.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, after.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2. color

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

    @Override
    public boolean isShowLoad() {
        return false;
    }
}
