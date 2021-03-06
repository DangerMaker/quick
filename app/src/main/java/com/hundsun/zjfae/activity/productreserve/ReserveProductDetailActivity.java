package com.hundsun.zjfae.activity.productreserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
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
import com.hundsun.zjfae.activity.productreserve.bean.ReserveProductPlay;
import com.hundsun.zjfae.activity.productreserve.bean.ReserverProductBean;
import com.hundsun.zjfae.activity.productreserve.presenter.ReserverProductDetailPresenter;
import com.hundsun.zjfae.activity.productreserve.view.ReserveProductDetailView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.BaseSharedPreferences;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.view.PurchaseDialogReserveProductDetail;
import com.hundsun.zjfae.common.view.dialog.EarningsDialog;
import com.hundsun.zjfae.common.view.dialog.PurchaseDialog;

import java.util.List;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v2.ReserveProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @Description:?????????????????????????????????
 * @Author: zhoujianyu
 * @Time: 2018/10/23 13:49
 */
public class ReserveProductDetailActivity extends CommActivity<ReserverProductDetailPresenter> implements View.OnClickListener, ReserveProductDetailView {

    private String orderProductCode, sellingStatus, orderType, orderNum, productCode;
    private TextView productName, expectedMaxAnnualRate, deadline, buyerSmallestAmount, riskLevel, buyTotalAmount, buyRemainAmount, buyStartDate, buyEndDate, manageStartDate, manageEndDate, leastHoldAmount, tradeLeastHoldDay, payStyle, canBuyNum, productStatusStr, subjectTypeName, isTransferStr, remark, tradeStartDate_start_time, tradeStartDate_end_time, tv_fuxi;
    private View riskLevel_view;
    private PurchaseDialog.Builder productAmountDialog;

    private PurchaseDialogReserveProductDetail.Builder productAmountFixedDialog;

    private ReserveProductPlay playInfo;

    private RecyclerView attachment;


    private LinearLayout remark_layout, attach_layout;
    private CheckBox product_check;
    private NestedScrollView product_scroll;
    private Button product_buy;

    private EarningsDialog.Builder earBuilder;

    private String rate = "0", amount = "0", timeLimit = "0";

    private static int requestCodes = 0x701;

    private String amountFixed = "";//??????????????????????????? ??????????????????
    private TextView tv_agreement;//??????????????????text
    private LinearLayout lin_transfer_start, lin_transfer_end;//???????????????????????????????????????


    @Override
    public void getProductBean(ReserverProductBean productBean) {

        UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userInfo = productBean.getUserDetailInfo();
        if (userInfo != null && productBean.getProductDetails() != null) {
            getUserDate(userInfo, productBean.getProductDetails());
        }

        if (productBean.getProductDetails() != null && productBean.getAttachment() != null) {
            //????????????
            productAttachment(productBean);
        }

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
                intent.setClass(ReserveProductDetailActivity.this, HighNetWorthMaterialsUploadedActivity.class);
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
                    baseStartActivity(ReserveProductDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {

            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ReserveProductDetailActivity.this, AddBankActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ReserveProductDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ReserveProductDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else {
            if (orderType.equals("0")) {
                productAmountDialog.create().show();
            } else {
                productAmountFixedDialog.create().show();
            }
        }
    }

    @Override
    public void getProductDetail(final ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails productDetails) {
        if (productDetails.getReturnCode().equals("0000")) {
            ReserveProductDetails.PBIFE_prdquery_prdQueryOrderProductDetails.TaProductFinanceDetail taProductFinanceDetail =
                    productDetails.getData().getTaProductFinanceDetail();
            productCode = taProductFinanceDetail.getProductCode();
            setInfo(productDetails.getData());
            productStatus(sellingStatus, taProductFinanceDetail);
            presenter.allProductRequest(productCode);
        } else if (productDetails.getReturnCode().equals("0225")) {
            //#21918????????????????????????????????????????????????????????? ????????????????????????
            showDialog(productDetails.getReturnMsg(), "?????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        } else {
            showDialog(productDetails.getReturnMsg(), "?????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (productDetails.getReturnCode().equals("0045") || productDetails.getReturnCode().equals("3435")) {
                        //????????????????????? ??????????????????
                        finish();
                    }
                }
            });
        }
    }

    //????????????
    @Override
    public void productCheckState(ProductSec.Ret_PBIFE_trade_subscribeProductSec subscribeProductSec) {

        String returnCode = subscribeProductSec.getReturnCode();
        String returnMsg = subscribeProductSec.getReturnMsg();
        if (returnCode.equals("0000")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("product_info", playInfo);
            intent.putExtra("product_bundle", bundle);
            intent.putExtra("orderType", orderType);
            intent.setClass(ReserveProductDetailActivity.this, ReserveProductPlayActivity.class);
            startActivityForResult(intent, requestCodes);
        }
    }



    @Override
    public void onHighAge() {
        final AgeFragmentDialog ageFragmentDialog = new AgeFragmentDialog();

        ageFragmentDialog.setOnClickListener(new AgeFragmentDialog.OnClickListener() {
            @Override
            public void isReadProtocol(boolean isRead) {
                ageFragmentDialog.dismissDialog();
                if (isRead){
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
    public void onSeniorMember(String returnMsg) {
        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                baseStartActivity(ReserveProductDetailActivity.this, HighActivity.class);
            }
        });
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
                    baseStartActivity(ReserveProductDetailActivity.this, AddBankActivity.class);
                }
            });
        } else {
            showDialog(returnMsg, "????????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", "");
                    } else {
                        showUserLevelDialog("020", "");
                    }

                    //showUserLevelDialog("000", "");
                }
            });

        }
    }

    @Override
    public void onUpDataUserPurchaseAmount(ProductSec.Ret_PBIFE_trade_subscribeProductSec subscribeProductSec) {
        buyRemainAmount.setText(subscribeProductSec.getData().getRemainBuyAmount());
        showDialog(subscribeProductSec.getReturnMsg());
    }

    @Override
    public void onRiskAssessment(String returnMsg) {
        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //?????????
                dialog.dismiss();
                baseStartActivity(ReserveProductDetailActivity.this, RiskAssessmentActivity.class);
            }
        });
    }

    @Override
    public void onNovicePrompt(String returnMsg) {
        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("product_info", playInfo);
                intent.putExtra("product_bundle", bundle);
                intent.putExtra("orderType", orderType);
                intent.setClass(ReserveProductDetailActivity.this, ReserveProductPlayActivity.class);
                startActivityForResult(intent, requestCodes);
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
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("product_info", playInfo);
                intent.putExtra("product_bundle", bundle);
                intent.putExtra("orderType", orderType);
                intent.setClass(ReserveProductDetailActivity.this, ReserveProductPlayActivity.class);
                startActivityForResult(intent, requestCodes);
            }
        });
    }

    //??????????????????
    public void getUserDate(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userInfo, final ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails productData) {

        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userInfo.getData();

        String userType = userDetailInfo.getUserType();
        if (userDetailInfo.getIsFundPasswordSet().equals("false")) {
            showDialog("?????????????????????????????????", "??????????????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????????????????
                    dialog.dismiss();
                    baseStartActivity(ReserveProductDetailActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getVerifyName().equals("1") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ReserveProductDetailActivity.this, AddBankActivity.class);
                }
            });
        }
        //???????????????
        else if (productData.getData().getTaProductFinanceDetail().getAccreditedBuyIs().equals("1")
                && !userDetailInfo.getIsAccreditedInvestor().equals("1")) {
            if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????????
                        dialog.dismiss();
                        baseStartActivity(ReserveProductDetailActivity.this, AddBankActivity.class);
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

                    //showUserLevelDialog("000", "");

                }
            }

        } else if (userDetailInfo.getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ReserveProductDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ReserveProductDetailActivity.this, RiskAssessmentActivity.class);
                }
            });
        }


    }


    //????????????
    public void productAttachment(ReserverProductBean productBean) {

        //????????????
        final ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails productInfo =
                productBean.getProductDetails();
        final String accreditedBuyIs = productInfo.getData().getTaProductFinanceDetail().getAccreditedBuyIs();

        //????????????
        final Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentInfo = productBean.getAttachment();


        final List<Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList>
                list = attachmentInfo.getData().getTaProductAttachmentListList();

        if (list == null || list.isEmpty()) {
            attach_layout.setVisibility(View.GONE);
            return;
        }
        AttachmentAdapter adapter = new AttachmentAdapter(ReserveProductDetailActivity.this, list);
        adapter.setOnItemClickListener(new AttachmentAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position) {
                presenter.attachmentUserInfo(accreditedBuyIs, list.get(position), "", "", true);
            }
        });
        attachment.setLayoutManager(new LinearLayoutManager(ReserveProductDetailActivity.this));
        attachment.setAdapter(adapter);
        attachment.addItemDecoration(new DividerItemDecorations());
    }

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
                        baseStartActivity(ReserveProductDetailActivity.this, AddBankActivity.class);
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
                        showUserLevelDialog(" 020", isRealInvestor);
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
                        baseStartActivity(ReserveProductDetailActivity.this, AddBankActivity.class);
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



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doubt:
                showDialog("??????????????????????????????????????????????????????");
                break;

            case R.id.attach_layout:
                if (product_check.isChecked()) {
                    product_check.setChecked(false);
                } else {
                    product_check.setChecked(true);
                }
                break;

            case R.id.calculator:
                earBuilder = new EarningsDialog.Builder(this);
                earBuilder.setAmount(amount);
                earBuilder.setRate(rate);
                earBuilder.setTimeLimit(timeLimit);
                EarningsDialog earningsDialog = earBuilder.create();
                earningsDialog.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = earningsDialog.getWindow().getAttributes();
                lp.width = (int) (display.getWidth() * 0.75); //????????????
                earningsDialog.getWindow().setAttributes(lp);
                break;
            case R.id.product_buy:
                if (attach_layout.getVisibility() != View.GONE) {
                    if (!product_check.isChecked()) {
                        product_scroll.fullScroll(NestedScrollView.FOCUS_DOWN);
                        showDialog("??????????????????????????????????????????");
                        return;
                    }
                }
                presenter.getUserDate();
                break;
                default:
                    break;
        }
    }

    //????????????????????????
    private void setInfo(final ReserveProductDetails.PBIFE_prdquery_prdQueryOrderProductDetails data) {
        final ReserveProductDetails.PBIFE_prdquery_prdQueryOrderProductDetails.TaProductFinanceDetail bean = data.getTaProductFinanceDetail();
        playInfo = new ReserveProductPlay();
        if (bean.getPayFrequency() == null || bean.getPayFrequency().equals("")) {
            tv_fuxi.setText("--");
        } else {
            tv_fuxi.setText(bean.getPayFrequency());
        }
        playInfo.setSerialNoStr(bean.getSerialNoStr());
        playInfo.setProductCode(bean.getProductCode());
        productName.setText(bean.getProductName());
        playInfo.setProductName(bean.getProductName());
        playInfo.setExpectedMaxAnnualRate(bean.getExpectedMaxAnnualRate());
        playInfo.setDeadline(bean.getDeadline());
        playInfo.setBuyerSmallestAmount(bean.getBuyerSmallestAmount());
        playInfo.setBuyRemainAmount(bean.getBuyRemainAmount());
        expectedMaxAnnualRate.setText(bean.getExpectedMaxAnnualRate() + "%");
        rate = bean.getExpectedMaxAnnualRate();
        deadline.setText(bean.getDeadline());
        timeLimit = bean.getDeadline();
        if (orderType.equals("0")) {
            buyerSmallestAmount.setText(bean.getSpecialBuyAmount());
            amount = bean.getSpecialBuyAmount();
            productAmountDialog.setMessage(bean.getSpecialBuyAmount());
        } else {
            buyerSmallestAmount.setText(bean.getBuyerSmallestAmount());
            amount = bean.getBuyerSmallestAmount();
        }
        //????????????????????????
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelValue())) {
            riskLevel.setText(bean.getRiskLevelLabelValue());
            riskLevel.setVisibility(View.VISIBLE);
            riskLevel_view.setVisibility(View.VISIBLE);
        } else {
            riskLevel.setVisibility(View.GONE);
            riskLevel_view.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelUrl())) {
            riskLevel.setTextColor(getResources().getColor(R.color.blue));
            riskLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String accreditedBuyIs = bean.getAccreditedBuyIs();
                    presenter.attachmentUserInfo(accreditedBuyIs, null, data.getRatingTitle(), bean.getRiskLevelLabelUrl(), false);
                }
            });
        }

        buyTotalAmount.setText(bean.getBuyTotalAmount() + "???");
        if (orderType.equals("0")) {
            buyRemainAmount.setText(bean.getSpecialUnit() + "???");
        } else {
            buyRemainAmount.setText(bean.getOrderBuyAmount() + "???");
            productAmountFixedDialog.setMessage(bean.getOrderBuyAmount());
            amountFixed = bean.getOrderBuyAmount();
        }
        productStatusStr.setText(bean.getProductStatusStr());
        subjectTypeName.setText(bean.getSubjectTypeName());
        isTransferStr.setText(bean.getIsTransferStr());
        buyStartDate.setText(bean.getBuyStartDate());
        buyEndDate.setText(bean.getBuyEndDate());
        manageStartDate.setText(bean.getManageStartDate());
        manageEndDate.setText(bean.getManageEndDate());
        if (orderType.equals("0")) {
            leastHoldAmount.setText(bean.getSpecialIncreaseAmount() + "???");
        } else {
            leastHoldAmount.setText(bean.getUnActualPriceIncreases() + "???");
        }
        if (bean.getIsTransferStr().equals("?????????")) {
            tradeLeastHoldDay.setText(bean.getTradeLeastHoldDay() + "????????????");
        } else {
            tradeLeastHoldDay.setText("--");
        }
        payStyle.setText(bean.getPayStyle());
        canBuyNum.setText(bean.getCanBuyNum());
        tradeStartDate_start_time.setText(bean.getTradeStartDate());
        tradeStartDate_end_time.setText(bean.getTradeEndDate());
        if (bean.getIsTransferStr().equals("?????????")) {
            lin_transfer_start.setVisibility(View.VISIBLE);
            lin_transfer_end.setVisibility(View.VISIBLE);

        } else {
            lin_transfer_start.setVisibility(View.GONE);
            lin_transfer_end.setVisibility(View.GONE);
        }
        if (bean.getRemark() != null && !bean.getRemark().equals("")) {
            remark_layout.setVisibility(View.VISIBLE);
            remark.setText(bean.getRemark());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CCLog.e("requestCodes", requestCodes);
        CCLog.e("resultCode", resultCode);
        if (requestCode == requestCodes && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }


    @Override
    protected ReserverProductDetailPresenter createPresenter() {

        return  new ReserverProductDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reserve_product_detail;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.product_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("????????????");
        orderProductCode = getIntent().getStringExtra("productCode");
        sellingStatus = getIntent().getStringExtra("sellingStatus");
        orderType = getIntent().getStringExtra("orderType");
        orderNum = getIntent().getStringExtra("orderNum");
        productName = findViewById(R.id.productName);
        expectedMaxAnnualRate = findViewById(R.id.expectedMaxAnnualRate);
        deadline = findViewById(R.id.deadline);
        buyerSmallestAmount = findViewById(R.id.buyerSmallestAmount);
        riskLevel = findViewById(R.id.riskLevel);
        riskLevel_view = findViewById(R.id.riskLevel_view);
        buyTotalAmount = findViewById(R.id.buyTotalAmount);
        buyRemainAmount = findViewById(R.id.buyRemainAmount);
        buyStartDate = findViewById(R.id.buyStartDate);
        buyEndDate = findViewById(R.id.buyEndDate);
        manageStartDate = findViewById(R.id.manageStartDate);
        manageEndDate = findViewById(R.id.manageEndDate);
        leastHoldAmount = findViewById(R.id.leastHoldAmount);
        tradeLeastHoldDay = findViewById(R.id.tradeLeastHoldDay);
        payStyle = findViewById(R.id.payStyle);
        canBuyNum = findViewById(R.id.canBuyNum);
        productStatusStr = findViewById(R.id.productStatusStr);
        subjectTypeName = findViewById(R.id.subjectTypeName);
        isTransferStr = findViewById(R.id.isTransferStr);
        product_buy = findViewById(R.id.product_buy);
        product_buy.setOnClickListener(this);
        attachment = findViewById(R.id.attachment);
        remark = findViewById(R.id.remark);
        remark_layout = findViewById(R.id.remark_layout);
        tradeStartDate_start_time = findViewById(R.id.tradeStartDate_start_time);
        tradeStartDate_end_time = findViewById(R.id.tradeStartDate_end_time);
        product_check = findViewById(R.id.product_check);
        attach_layout = findViewById(R.id.attach_layout);
        tv_agreement = findViewById(R.id.tv_agreement);
        product_scroll = findViewById(R.id.product_scroll);
        tv_fuxi = findViewById(R.id.tv_fuxi);
        lin_transfer_start = findViewById(R.id.lin_transfer_start);
        lin_transfer_end = findViewById(R.id.lin_transfer_end);
        findViewById(R.id.calculator).setOnClickListener(this);
        findViewById(R.id.doubt).setOnClickListener(this);
        setDisclaimer();
    }

    @Override
    public void initData() {
        productAmountDialog = new PurchaseDialog.Builder(this);
        productAmountDialog.setTitle("?????????????????????");
        productAmountDialog.setMessage("0");
        productAmountDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        productAmountDialog.setPurchaseMoney(new PurchaseDialog.PurchaseMoney() {
            @Override
            public void purchaseMoney(DialogInterface dialog, String money) {
                dialog.dismiss();
                playInfo.setPlayAmount(money);

                presenter.productBuyCheck(productCode, money);


            }
        });

        productAmountFixedDialog = new PurchaseDialogReserveProductDetail.Builder(this);
        productAmountFixedDialog.setTitle("????????????");
        productAmountFixedDialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        productAmountFixedDialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                playInfo.setPlayAmount(amountFixed.replace(",", ""));

                presenter.productBuyCheck(productCode, amountFixed.replace(",", ""));

            }
        });

        presenter.requestPrdQueryProductDetails(orderProductCode, orderType, orderNum);

    }

    //??????????????????
    private void productStatus(String sellingStatus, ReserveProductDetails.PBIFE_prdquery_prdQueryOrderProductDetails.TaProductFinanceDetail taProductFinanceDetail) {
        //0-???????????????1-????????????2-????????????3-?????????
        //countDownSec?????????
//        String countDownSec = taProductFinanceDetail.getCountDownSec();
        //???????????????????????????
        if (taProductFinanceDetail.getComparison().equals("2")) {
            findViewById(R.id.doubt).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.doubt).setVisibility(View.GONE);
        }
        String remain = taProductFinanceDetail.getBuyRemainAmount();
        if (remain.equals("0")) {
            sellingStatus = "2";
        }
//        switch (sellingStatus) {
////            case "0":
////                product_buy.setEnabled(false);
////                product_buy.setClickable(false);
////                //product_buy.setText("????????????");
////                long countTime = Long.parseLong(countDownSec.trim());
////                Utils.transitionTime(countTime,product_buy);
////                product_buy.setBackgroundResource(R.drawable.product_buy);
////                break;
//            case "1":
//                product_buy.setEnabled(true);
//                product_buy.setClickable(true);
//                product_buy.setBackgroundResource(R.drawable.product_buy_clickable);
//                break;
//            case "2":
//                product_buy.setEnabled(false);
//                product_buy.setClickable(false);
//                product_buy.setText("?????????");
//                product_buy.setBackgroundResource(R.drawable.product_buy);
//                break;
//            case "3":
//                product_buy.setEnabled(false);
//                product_buy.setClickable(false);
//                product_buy.setText("?????????");
//                product_buy.setBackgroundResource(R.drawable.product_buy);
//                break;
//
//
//        }
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
            }, 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 1. click
            before.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, before.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            after.append(clickText2);
            after.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    baseStartActivity(ReserveProductDetailActivity.this, DisclaimerActivity.class);
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
}
