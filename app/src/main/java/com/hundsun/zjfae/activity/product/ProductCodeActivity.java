package com.hundsun.zjfae.activity.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.KeyEvent;
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
import com.hundsun.zjfae.activity.product.adapter.AttachmentAdapter;
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean;
import com.hundsun.zjfae.activity.product.listener.LockScreenBroadcastReceiver;
import com.hundsun.zjfae.activity.product.listener.LockScreenListener;
import com.hundsun.zjfae.activity.product.presenter.ProductCodePresenter;
import com.hundsun.zjfae.activity.product.util.ScheduledExecutorUtils;
import com.hundsun.zjfae.activity.product.view.ProductCodeView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.BaseSharedPreferences;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.view.dialog.EarningsDialog;
import com.hundsun.zjfae.common.view.dialog.PurchaseDialog;

import java.util.List;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @author moran
 * ??????????????????
 */
public class ProductCodeActivity extends CommActivity<ProductCodePresenter> implements View.OnClickListener, ProductCodeView {

    private String productCode, sellingStatus;
    private TextView productName, expectedMaxAnnualRate, deadline, buyerSmallestAmount, riskLevel, buyTotalAmount, buyRemainAmount, buyStartDate, buyEndDate, manageStartDate, manageEndDate, leastHoldAmount, tradeLeastHoldDay, payStyle, canBuyNum, isTransferStr, remark, tradeStartDate_start_time, tradeStartDate_end_time;
    private View riskLevel_view;
    private PurchaseDialog.Builder productAmountDialog;

    private ProductPlayBean playInfo;

    private RecyclerView attachment;


    private LinearLayout remark_layout, attach_layout, ll_isWholeTransfer;
    private CheckBox product_check;
    private NestedScrollView product_scroll;
    private Button product_buy;


    private EarningsDialog.Builder earBuilder;

    private String rate = "0", amount = "0", timeLimit = "0";

    private static int requestCodes = 0x701;
    /**
     * ??????????????????text
     **/
    private TextView tv_agreement;
    /**
     * ???????????????????????????????????????
     **/
    private LinearLayout lin_transfer_start, lin_transfer_end;
    private String type = "";

    /**
     * ?????????????????????????????????
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
                intent.setClass(ProductCodeActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
            }
        });
    }

    /**
     * ??????????????????????????????,???????????????????????????
     **/
    private UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo;


    /**
     * ??????????????????????????????
     **/
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
                    baseStartActivity(ProductCodeActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {

            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ProductCodeActivity.this, AddBankActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ProductCodeActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getData().getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ProductCodeActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else {
            productAmountDialog.create().show();
        }
    }

    /**
     * ????????????????????????????????????
     */
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
                        baseStartActivity(ProductCodeActivity.this, AddBankActivity.class);
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

                    //showUserLevelDialog("000",isRealInvestor);

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
                        baseStartActivity(ProductCodeActivity.this, AddBankActivity.class);
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
     * ????????????
     **/
    @Override
    public void onProductData(PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails productDetails, long requestTime) {
        PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails.TaProductFinanceDetail taProductFinanceDetail =
                productDetails.getData().getTaProductFinanceDetail();
        setInfo(productDetails.getData());
        productStatus(sellingStatus, taProductFinanceDetail, requestTime);

    }

    /**
     * ????????????????????????
     **/
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
                    baseStartActivity(ProductCodeActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getVerifyName().equals("1") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ProductCodeActivity.this, AddBankActivity.class);
                }
            });
        }
        //???????????????
        else if (accreditedBuyIs.equals("1")
                && !userDetailInfo.getIsAccreditedInvestor().equals("1")) {
            if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {
                showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????????
                        dialog.dismiss();
                        baseStartActivity(ProductCodeActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                // 0???????????????
                if (userDetailInfo.getHighNetWorthStatus().equals("0")) {
                    presenter.requestInvestorStatus(isRealInvestor);
                } else if (!userDetailInfo.getHighNetWorthStatus().equals("-1")) {

                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }
                    //showUserLevelDialog("000",isRealInvestor);


                }
            }

        } else if (userDetailInfo.getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ProductCodeActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ProductCodeActivity.this, RiskAssessmentActivity.class);
                }
            });
        }

    }

    /**
     * ????????????
     **/
    @Override
    public void onAttachmentInfo(Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentList, final String accreditedBuyIs) {

        final List<Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList>
                list = attachmentList.getData().getTaProductAttachmentListList();

        if (list == null || list.isEmpty()) {
            attach_layout.setVisibility(View.GONE);
            return;
        }
        AttachmentAdapter adapter = new AttachmentAdapter(ProductCodeActivity.this, list);
        adapter.setOnItemClickListener(new AttachmentAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position) {

                presenter.attachmentUserInfo(accreditedBuyIs, list.get(position), "", "", true);

            }
        });
        attachment.setLayoutManager(new LinearLayoutManager(ProductCodeActivity.this));
        attachment.setAdapter(adapter);
    }

    /**
     * ????????????????????????
     **/
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
                intent.putExtra("type", "");
                intent.setClass(ProductCodeActivity.this, ProductPlayActivity.class);
                startActivityForResult(intent, requestCodes);
            }
        });
    }


    /**
     * ?????????????????????
     **/
    @Override
    public void onQualifiedMember(String returnMsg) {

        final String userType = UserInfoSharePre.getUserType();
        if (userDetailInfo.getData().getIsBondedCard().equals("false") && userType.equals("personal")) {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(ProductCodeActivity.this, AddBankActivity.class);
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

                    //showUserLevelDialog("000","");
                }
            });

        }
    }


    /**
     * ????????????????????????
     **/
    @Override
    public void onUpDataUserPurchaseAmount(ProductSec.Ret_PBIFE_trade_subscribeProductSec subscribeProductSec) {
        buyRemainAmount.setText(subscribeProductSec.getData().getRemainBuyAmount());
        showDialog(subscribeProductSec.getReturnMsg());
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
                baseStartActivity(ProductCodeActivity.this, HighActivity.class);
            }
        });
    }

    /**
     * ????????????
     **/
    @Override
    public void onRiskAssessment(String returnMsg) {

        showDialog(returnMsg, "??????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //?????????
                dialog.dismiss();
                baseStartActivity(ProductCodeActivity.this, RiskAssessmentActivity.class);
            }
        });

    }

    /**
     * ??????????????????
     */
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
                intent.putExtra("type", "");
                intent.setClass(ProductCodeActivity.this, ProductPlayActivity.class);
                startActivityForResult(intent, requestCodes);
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

    //????????????
    @Override
    public void productCheckState(ProductSec.Ret_PBIFE_trade_subscribeProductSec subscribeProductSec) {
        String returnCode = subscribeProductSec.getReturnCode();
        if (returnCode.equals("0000")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("product_info", playInfo);
            intent.putExtra("product_bundle", bundle);
            intent.putExtra("type", "");
            intent.setClass(ProductCodeActivity.this, ProductPlayActivity.class);
            startActivityForResult(intent, requestCodes);
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
                //????????????
                lp.width = (int) (display.getWidth() * 0.75);
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

    /**
     * ????????????
     **/
    private void setInfo(final PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails productDetails) {
        final PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails.TaProductFinanceDetail data = productDetails.getTaProductFinanceDetail();
        //??????ID
        String quanDetailsId = getIntent().getStringExtra("quanDetailsId");
        playInfo.setQuanDetailsId(quanDetailsId);
        playInfo.setSerialNoStr(data.getSerialNoStr());
        playInfo.setProductCode(data.getProductCode());
        productName.setText(data.getProductName());
        playInfo.setProductName(data.getProductName());
        playInfo.setExpectedMaxAnnualRate(data.getExpectedMaxAnnualRate());
        playInfo.setDeadline(data.getDeadline());
        playInfo.setPayStyle(data.getPayStyle());
        playInfo.setIsTransfer(data.getIsTransferStr());
        playInfo.setBuyerSmallestAmount(data.getBuyerSmallestAmount());
        playInfo.setBuyRemainAmount(data.getBuyRemainAmount());
        expectedMaxAnnualRate.setText(data.getExpectedMaxAnnualRate() + "%");
        rate = data.getExpectedMaxAnnualRate();
        deadline.setText(data.getDeadline());
        timeLimit = data.getDeadline();
        buyerSmallestAmount.setText(data.getBuyerSmallestAmount());
        amount = data.getBuyerSmallestAmount();
        //????????????????????????
        if (StringUtils.isNotBlank(data.getRiskLevelLabelValue())) {
            riskLevel.setText(data.getRiskLevelLabelValue());
            riskLevel.setVisibility(View.VISIBLE);
            riskLevel_view.setVisibility(View.VISIBLE);
        } else {
            riskLevel.setVisibility(View.GONE);
            riskLevel_view.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(data.getRiskLevelLabelUrl())) {
            riskLevel.setTextColor(getResources().getColor(R.color.blue));
            riskLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String accreditedBuyIs = data.getAccreditedBuyIs();
                    presenter.attachmentUserInfo(accreditedBuyIs, null, productDetails.getRatingTitle(), data.getRiskLevelLabelUrl(), false);
                }
            });
        }
        buyTotalAmount.setText(data.getBuyTotalAmount() + "???");
        buyRemainAmount.setText(data.getBuyRemainAmount() + "???");
        isTransferStr.setText(data.getIsTransferStr());
        buyStartDate.setText(data.getBuyStartDate());
        buyEndDate.setText(data.getBuyEndDate());
        manageStartDate.setText(data.getManageStartDate());
        manageEndDate.setText(data.getManageEndDate());
        leastHoldAmount.setText(data.getUnActualPriceIncreases() + "???");
        if (data.getIsTransferStr().equals("?????????")) {
            tradeLeastHoldDay.setText(data.getTradeLeastHoldDay() + "????????????");
        } else {
            tradeLeastHoldDay.setText("--");
        }
        payStyle.setText(data.getPayStyle());
        canBuyNum.setText(data.getCanBuyNum());
        tradeStartDate_start_time.setText(data.getTradeStartDate());
        tradeStartDate_end_time.setText(data.getTradeEndDate());
        if (data.getIsTransferStr().equals("?????????")) {
            lin_transfer_start.setVisibility(View.VISIBLE);
            lin_transfer_end.setVisibility(View.VISIBLE);
        } else {
            lin_transfer_start.setVisibility(View.GONE);
            lin_transfer_end.setVisibility(View.GONE);
        }
        String isWholeTransfer = data.getIsWholeTransfer();
        //???????????????????????????
        if ("1".equals(isWholeTransfer)) {
            ll_isWholeTransfer.setVisibility(View.VISIBLE);
        } else {
            ll_isWholeTransfer.setVisibility(View.GONE);
        }


        if (data.getRemark() != null && !data.getRemark().equals("")) {
            remark_layout.setVisibility(View.VISIBLE);
            remark.setText(data.getRemark());
        }
        productAmountDialog.setMessage(data.getBuyerSmallestAmount());
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
    protected ProductCodePresenter createPresenter() {

        return new ProductCodePresenter(this);
    }

    @Override
    protected void topDefineCancel() {
        ScheduledExecutorUtils.cancel();
        finish();
    }

    @Override
    public void initView() {
        setTitle("????????????");
        mTopDefineCancel = true;
        productCode = getIntent().getStringExtra("productCode");
        sellingStatus = getIntent().getStringExtra("sellingStatus");
        if (StringUtils.isNotBlank(getIntent().getStringExtra("type"))) {
            type = getIntent().getStringExtra("type");
        }
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
        lin_transfer_start = findViewById(R.id.lin_transfer_start);
        lin_transfer_end = findViewById(R.id.lin_transfer_end);
        findViewById(R.id.calculator).setOnClickListener(this);
        findViewById(R.id.doubt).setOnClickListener(this);
        ll_isWholeTransfer = findViewById(R.id.ll_isWholeTransfer);
        setDisclaimer();
    }


    @Override
    public void initData() {
        playInfo = new ProductPlayBean();
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
                if (Utils.isStringEmpty(money)) {

                    showDialog("????????????????????????");
                } else {
                    playInfo.setPlayAmount(money);
                    presenter.productBuyCheck(productCode, money);
                }

            }
        });

        presenter.initProductData(productCode, type);

    }

    //??????????????????
    private void productStatus(String sellingStatus, PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails.TaProductFinanceDetail taProductFinanceDetail, long requestTime) {
        //0-???????????????1-????????????2-????????????3-?????????
        //countDownSec?????????
        String countDownSec = taProductFinanceDetail.getCountDownSec();
        String remain = taProductFinanceDetail.getBuyRemainAmount();
        //???????????????????????????
        if (taProductFinanceDetail.getComparison().equals("2")) {
            findViewById(R.id.doubt).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.doubt).setVisibility(View.GONE);
        }
        if (remain.equals("0") && !sellingStatus.equals("4")) {
            sellingStatus = "2";
        }
        switch (sellingStatus) {
            case "0":
                product_buy.setEnabled(false);
                product_buy.setClickable(false);
                //product_buy.setText("????????????");
                long countTime = Long.parseLong(countDownSec.trim());

                CCLog.e("countTime", countTime);

                CCLog.e("requestTime", requestTime);

                final long baseCountTime = countTime - requestTime;

                ScheduledExecutorUtils.setCountTime(baseCountTime);
                ScheduledExecutorUtils.setExecutorListener(new ScheduledExecutorUtils.ScheduledExecutorListener() {
                    @Override
                    public void onSuccess() {
                        product_buy.setClickable(true);
                        product_buy.setEnabled(true);
                        product_buy.setText("????????????");
                        product_buy.setBackgroundResource(R.drawable.product_buy_clickable);
                    }

                    @Override
                    public void onNoClick(String tv) {
                        product_buy.setClickable(false);
                        product_buy.setEnabled(false);
                        product_buy.setText(tv);
                        product_buy.setBackgroundResource(R.drawable.product_buy);
                    }
                });
                ScheduledExecutorUtils.startTime();
                registerReceiver();

                break;
            case "2":
                product_buy.setEnabled(false);
                product_buy.setClickable(false);
                product_buy.setText("?????????");
                product_buy.setBackgroundResource(R.drawable.product_buy);
                break;
            case "3":
                product_buy.setEnabled(false);
                product_buy.setClickable(false);
                product_buy.setText("?????????");
                product_buy.setBackgroundResource(R.drawable.product_buy);
                break;
            case "4":
                product_buy.setEnabled(false);
                product_buy.setClickable(false);
                product_buy.setText("?????????");
                product_buy.setBackgroundResource(R.drawable.product_buy);
                break;
            default:
                product_buy.setEnabled(true);
                product_buy.setClickable(true);
                product_buy.setText("????????????");
                product_buy.setBackgroundResource(R.drawable.product_buy_clickable);
                break;


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ScheduledExecutorUtils.cancel();
        finish();
        return true;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.product_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_code;
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
                    baseStartActivity(ProductCodeActivity.this, DisclaimerActivity.class);
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

    private LockScreenBroadcastReceiver lockScreenBroadcastReceiver;

    private void registerReceiver() {

        if (lockScreenBroadcastReceiver == null) {
            lockScreenBroadcastReceiver = new LockScreenBroadcastReceiver(new LockScreenListener() {
                @Override
                public void onScreenOn() {
                    // ??????
                    CCLog.e("TAG", "??????");

                }

                @Override
                public void onScreenOff() {
                    // ??????
                    CCLog.e("TAG", "??????");
                }

                @Override
                public void onUserPresent() {
                    // ??????
                    CCLog.e("TAG", "??????");
                    presenter.initProductData(productCode, type);
                }
            });

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_USER_PRESENT);
            registerReceiver(lockScreenBroadcastReceiver, intentFilter);
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver();
        super.onDestroy();
    }

    private void unregisterReceiver() {
        if (lockScreenBroadcastReceiver != null) {
            unregisterReceiver(lockScreenBroadcastReceiver);
        }
    }

    @Override
    public boolean isShowLoad() {
        return false;
    }
}
