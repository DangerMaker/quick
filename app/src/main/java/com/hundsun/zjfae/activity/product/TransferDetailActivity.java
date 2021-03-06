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
 * @Description ??????????????????
 * @Author moran
 * @CreateDate 2019/6/10 12:21
 * @UpdateUser ???????????? 
 * @UpdateDate 2019/6/10 12:21
 * @UpdateRemark ???????????????
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
     * ??????????????????text
     */
    private TextView tv_agreement;
    private TextView tv_tips;

    private String yearDayValue = "365";

    /**
     * ????????????
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
        //??????????????????
        if (StringUtils.isNotBlank(detailBean.getRiskLevelLabelValue())) {
            riskLevel.setText(detailBean.getRiskLevelLabelValue());
            riskLevel.setVisibility(View.VISIBLE);
            riskLevel_name.setVisibility(View.VISIBLE);
        } else {
            riskLevel.setVisibility(View.GONE);
            riskLevel_name.setVisibility(View.GONE);
        }

        String isWholeTransfer = detailBean.getIsWholeTransfer();
        //???????????????????????????
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

        //???????????????????????????
        String ifAllBuy = playInfo.getIfAllBuy();
        if (ifAllBuy.equals("1")) {
            isBuy = true;
            earnings.setText(playInfo.getDelegateNum() + "???(????????????)");
            //??????????????????
            playInfo.setPlayAmount(playInfo.getDelegateNum());
            presenter.initTransferData(playInfo.getProductCode(), playInfo.getDelegationCode(), playInfo.getDelegateNum(), playInfo.getActualRate());
        } else {
            presenter.initTransferData(playInfo.getProductCode(), playInfo.getDelegationCode());
            earnings.setOnClickListener(this);
        }
        //????????????
        transferFloatEnd.setText(playInfo.getActualRate() + "%");
        productStatusStr.setText(detailBean.getProductStatusStr());
        subjectTypeName.setText(detailBean.getSubjectTypeName());
        payStyle.setText(detailBean.getPayStyle());
        manageEndDate.setText(detailBean.getManageEndDate());
        buySmallestAmount.setText(playInfo.getDelegateNum() + "???");

        yearDay.setText(detailBean.getYearDay());
        isTransferStr.setText(detailBean.getIsTransferStr());
        tradeLeastHoldDay.setText(detailBean.getTradeLeastHoldDay() + "????????????");
        buyerSmallestAmount.setText(detailBean.getLeastHoldAmount() + "???");
        tradeIncrease.setText(detailBean.getTradeIncrease() + "???");

        nextPayDate.setText(playInfo.getNextPayDate());
        holdDayNum.setText(productDetails.getData().getDiffDays() + "???");
        this.canBuyNum.setText(playInfo.getCanBuyNum());
        if (playInfo.getCanBuyNum().equals("?????????")) {
            this.canBuyNum.setText("?????????");
        } else if (playInfo.getCanBuyNum().equals("0")) {
            doubt.setVisibility(View.VISIBLE);
            doubt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????", "??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });
                }
            });



        }

        //????????????????????????????????????
        if (productDetails.getData().getIsShowHonourAdvance().equals("1")) {
            tv_tips.setVisibility(View.VISIBLE);
            tv_tips.setText(productDetails.getData().getHonourAdvanceTips());
        } else {
            tv_tips.setVisibility(View.GONE);
        }

    }

    /**
     * ??????????????????
     */
    @Override
    public void onUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userInfo, String accreditedBuyIs) {

        //????????????????????????????????????????????????
        playInfo.setMobile(userInfo.getData().getMobile());

        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userInfo.getData();
        //??????????????????
        String riskLevel = userDetailInfo.getRiskLevel();
        //?????????????????????
        String riskExpiredDate = userDetailInfo.getRiskExpiredDate();
        //???????????????
        String isRiskTest = userDetailInfo.getIsRiskTest();

        String userType = userInfo.getData().getUserType();
        //???????????????intent
        final Intent intentRisk = new Intent(TransferDetailActivity.this, RiskAssessmentActivity.class);
        intentRisk.putExtra("riskLevel", riskLevel);
        intentRisk.putExtra("riskExpiredDate", riskExpiredDate);
        intentRisk.putExtra("isRiskTest", isRiskTest);
        if (userDetailInfo.getIsFundPasswordSet().equals("false")) {
            showDialog("?????????????????????????????????", "??????????????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????????????????
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getVerifyName().equals("1") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                }
            });
        }
        //???????????????
        else if (accreditedBuyIs.equals("1") && !userDetailInfo.getIsAccreditedInvestor().equals("1")) {
            if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????????
                        dialog.dismiss();
                        baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                // 0???????????????
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
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(intentRisk);
                }
            });
        } else if (userDetailInfo.getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(intentRisk);
                }
            });
        }

    }

    /**
     * ??????????????????
     **/
    @Override
    public void onEarningsInfo(TransferBuyProfits.Ret_PBIFE_trade_queryTransferBuyProfits transferBuyProfits) {
        isBuy = true;
        TransferBuyProfits.PBIFE_trade_queryTransferBuyProfits dataBean = transferBuyProfits.getData();
        amount.setText(dataBean.getAmount() + "???");
        floatingProfit.setText(dataBean.getFloatingProfit() + "???");
        transferIncome.setText(dataBean.getTransferIncome() + "???");
        extraCost.setText(dataBean.getExtraCost() + "???");
    }

    /**
     * ????????????
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
     * ????????????
     */
    @Override
    public void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, Boolean isAttachment) {
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
                        baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
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

                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }

                    //showUserLevelDialog("000", isRealInvestor);

                }
            } else {
                if (isAttachment) {
                    //????????????
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "9");
                } else {
                    //????????????
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
                        baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                if (isAttachment) {
                    //????????????
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "9");
                } else {
                    //????????????
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }
        }
    }


    /**
     * ????????????????????????
     */
    @Override
    public void getTransferOrderSecBean(TransferOrderSec.Ret_PBIFE_trade_transferOrderSec orderSecBean) {

        if (playInfo.getIsWholeTransfer().equals("1")) {

            showDialog("???????????????????????????/??????????????????????????????", "??????", "??????", new DialogInterface.OnClickListener() {
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
     * ???????????????????????????
     */
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
                intent.setClass(TransferDetailActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
                finish();
            }
        });
    }

    /**
     * ??????????????????
     */
    @Override
    public void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        String userType = userDetailInfo.getData().getUserType();
        if (userDetailInfo.getData().getIsFundPasswordSet().equals("false")) {
            showDialog("?????????????????????????????????", "??????????????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????????????????
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {

            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else {
            presenter.getTransferOrderSec(playInfo.getPlayAmount(), playInfo.getDelegationCode());
        }

    }

    /**
     * ?????????????????????
     */
    @Override
    public void onQualifiedMember(String returnMsg) {

        String userType = UserInfoSharePre.getUserType();
        if (presenter.getIsBondedCard().equals("false") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(TransferDetailActivity.this, AddBankActivity.class);
                }
            });
        } else {
            showDialog(returnMsg, "????????????", "??????", new DialogInterface.OnClickListener() {
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
     * ???????????????
     */
    @Override
    public void onRiskAssessment(String returnMsg) {
        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //?????????
                dialog.dismiss();
                baseStartActivity(TransferDetailActivity.this, RiskAssessmentActivity.class);
            }
        });
    }

    /**
     * ?????????
     */
    @Override
    public void onSeniorMember(String returnMsg) {
        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                baseStartActivity(TransferDetailActivity.this, HighActivity.class);
            }
        });
    }


    /**
     * 65??????
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
                        showDialog("??????????????????????????????????????????");
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
                showDialog("????????????????????????????????????????????????????????????????????????????????????");
                break;
            case R.id.amount_img:
                showDialog("????????????=????????????+????????????" + "\n" + "?????????????????????", "??????");
                break;
            case R.id.floatingProfit_img:

                showDialog("??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ?? (?????????????????????-????????????) ?? ?????????????????? ?? "+yearDayValue, "??????");

                break ;
            case R.id.transferIncome_img:
                showDialog("?????????????????????????????????????????????????????????????????????????????????????? ?? ?????????????????? ?? "+yearDayValue, "??????");
                break;
            case R.id.extraCost_img:
                showDialog("??????????????????5??????????????????????????????????????????????????????2?????????????????????????????????5??????(???)??????????????????????????????????????????????????????5??????????????????", "??????");
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
                //????????????
                lp.width = (int) (display.getWidth() * 0.75);
                earningsDialog.getWindow().setAttributes(lp);
                break;
            default:
                break;
        }
    }

    private void buy() {
        AssigneePaymentDialog.Builder builder = new AssigneePaymentDialog.Builder(this);
        builder.setMessage("????????????" + RMBUtils.formatWanNum(playInfo.getLeastTranAmount()).replace("???", ""));
        builder.setTitle("?????????????????????");
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //????????????
        builder.setEarningsCallback("??????", new AssigneePaymentDialog.EarningsCallback() {
            @Override
            public void earnings(DialogInterface dialog, String money) {
                dialog.dismiss();

                if (StringUtils.isEmpty(money)) {

                    showDialog("????????????????????????");

                } else {
                    earnings.setText(money + "???(????????????)");
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
            bt_purchase.setText("????????????");
            bt_purchase.setBackgroundResource(R.drawable.product_buy);
        } else {
            bt_purchase.setClickable(true);
        }


    }


    @Override
    public void initView() {
        setTitle("????????????");
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
     * ?????????????????????????????????????????????????????????????????????????????????
     */
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
            String clicktext = "?????????????????????????????????";
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
