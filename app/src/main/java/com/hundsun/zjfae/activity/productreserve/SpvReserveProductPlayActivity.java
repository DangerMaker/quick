package com.hundsun.zjfae.activity.productreserve;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.OfflineRechargeActivity;
import com.hundsun.zjfae.activity.mine.RechargeActivity;
import com.hundsun.zjfae.activity.mine.RechargeGuideActivity;
import com.hundsun.zjfae.activity.product.ChooseBaoActivity;
import com.hundsun.zjfae.activity.product.ChooseQuanActivity;
import com.hundsun.zjfae.activity.product.ProductPlayStateActivity;
import com.hundsun.zjfae.activity.product.bean.CardVoucherBean;
import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean;
import com.hundsun.zjfae.activity.product.bean.RadEnvelopeBean;
import com.hundsun.zjfae.activity.productreserve.bean.ReserveProductPlay;
import com.hundsun.zjfae.activity.productreserve.presenter.ReserveProductPlayPresenter;
import com.hundsun.zjfae.activity.productreserve.view.ReserveProductPlayView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;
import com.hundsun.zjfae.common.view.dialog.CardDictionaryDialog;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;
import com.hundsun.zjfae.common.view.popwindow.PlayWindow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.QueryMyKqQuan;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;

/**
 * @Description:????????????????????????
 * @Author: zhoujianyu
 * @Time: 2019/3/4 17:08
 */
public class SpvReserveProductPlayActivity extends CommActivity<ReserveProductPlayPresenter> implements View.OnClickListener, ReserveProductPlayView {

    public static final int QUAN_LIST_REQUEST_CODE = 0x7071;
    private static final int BAO_LIST_REQUEST_CODE = 0x7072;
    private static final int QUAN_LIST_RESULT_CODE = 0x758;
    private static final int BAO_LIST_RESULT_CODE = 0x759;

    private static final int PLAY_CODE = 0x789;


    private static final int RECHARGE_CODE = 0x790;


    //???????????????
    private TextView quan_number;
    //???????????????
    private TextView quan_name;
    //?????????????????????
    private LinearLayout choose_quan_layout;
    //????????????
    private TextView bao_number;
    //????????????
    private TextView bao_size;
    //??????????????????
    private LinearLayout choose_bao_layout;

    //????????????
    private TextView payAmount;
    //???????????????
    private TextView quan_value;
    //????????????
    private TextView bao_value;
    //????????????
    private TextView total_payAmount;

    //??????????????????
    private TextView balance;
    //????????????????????????????????????+???????????????
    private CheckBox play_type;
    //?????????????????????
    private LinearLayout bank_play_layout;
    //?????????????????????
    private TextView bank_play_tv;
    //???????????????
    private TextView bank_name;
    //?????????????????????
    private LinearLayout lin_freezeDeposit;
    private TextView tv_freezeDeposit;
    private String freezeDeposit = "0.00";

    //?????????????????????
    private LinearLayout sms_layout;

    //???????????????????????????
    private Button sms_button;

    //?????????????????????
    private EditText sms_ed;

    //???????????????
    private CustomCountDownTimer countDownTimer;

    private ReserveProductPlayPresenter presenter;
    //???????????????
    private ProductPlayBean playInfo;

    private String orderType = "";

    private Boolean sendmessage = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reserve_product_code_play;
    }

    @Override
    protected ReserveProductPlayPresenter createPresenter() {
        return presenter = new ReserveProductPlayPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("????????????");
        payAmount = findViewById(R.id.payAmount);
        total_payAmount = findViewById(R.id.total_payAmount);
        balance = findViewById(R.id.balance);
        quan_name = findViewById(R.id.quan_name);
        quan_number = findViewById(R.id.quan_number);
        bao_number = findViewById(R.id.bao_number);
        quan_value = findViewById(R.id.quan_value);
        bao_value = findViewById(R.id.bao_value);
        bao_size = findViewById(R.id.bao_size);
        bank_play_layout = findViewById(R.id.bank_play_layout);
        bank_play_tv = findViewById(R.id.bank_play_tv);
        play_type = findViewById(R.id.play_type);
        findViewById(R.id.play).setOnClickListener(this);
        bank_name = findViewById(R.id.bank_name);
        choose_bao_layout = findViewById(R.id.choose_bao_layout);
        choose_quan_layout = findViewById(R.id.choose_quan_layout);
        sms_layout = findViewById(R.id.sms_layout);
        sms_button = findViewById(R.id.sms_button);
        sms_button.setOnClickListener(this);
        sms_ed = findViewById(R.id.sms_ed);
        countDownTimer = new CustomCountDownTimer(60000, 1000, sms_button);
        lin_freezeDeposit = findViewById(R.id.lin_freezeDeposit);
        tv_freezeDeposit = findViewById(R.id.tv_freezeDeposit);
        orderType = getIntent().getStringExtra("orderType");
    }


    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("product_bundle");
        playInfo = bundle.getParcelable("product_info");
        presenter.init(playInfo.getProductCode(), playInfo.getPlayAmount(), playInfo.getSerialNoStr());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CCLog.e("?????????requestCode", requestCode);
        CCLog.e("?????????resultCode", resultCode);
        if (requestCode == PLAY_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        //????????????
        if (requestCode == QUAN_LIST_REQUEST_CODE || requestCode == BAO_LIST_REQUEST_CODE) {
            if (resultCode == QUAN_LIST_RESULT_CODE) {
                if (data != null) {
                    Bundle bundle = data.getBundleExtra("quanBundle");
                    if (bundle != null) {
                        this.playInfo = bundle.getParcelable("playQuan");
                        defaultCheckQuan();
                    }
                }

            }
            //??????
            else if (resultCode == BAO_LIST_RESULT_CODE) {
                if (data != null) {
                    Bundle bundle = data.getBundleExtra("baoBundle");
                    if (bundle != null) {
                        this.playInfo = bundle.getParcelable("playBao");
                        defaultCheckBao();
                    }


                }
            }
        } else if (requestCode == RECHARGE_CODE && resultCode == RESULT_OK) {
            initData();

        }

    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.play_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    private void isPlay() {
        //?????????????????? ????????????
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(sms_ed.getWindowToken(), 0); //??????????????????
        final PlayWindow play = new PlayWindow(SpvReserveProductPlayActivity.this);
        play.showAtLocation(findViewById(R.id.play_layout));
        play.setPayListener(new PlayWindow.OnPayListener() {
            @Override
            public void onSurePay(String password) {
                //??????
                playInfo.setPlayPassWord(password);
                presenter.playTransferDetail(playInfo);
            }
        });
    }


    @Override
    public void init(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit, UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan queryQuanInfo) {
        if (queryPayInit != null) {
            productInit(queryPayInit);
        }
        if (queryQuanInfo != null) {
            cardInit(queryQuanInfo);
        }
    }

    //?????????????????????
    private void productInit(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit) {

        final QueryPayInit.PBIFE_trade_queryPayInit.PayInitWrap wrapBean = queryPayInit.getData().getPayInitWrap();
        if (orderType.equals("1")) {
            //???????????????????????? ???????????????????????????
            tv_freezeDeposit.setText(queryPayInit.getData().getPayInitWrap().getFreezeDeposit() + "???");
            lin_freezeDeposit.setVisibility(View.VISIBLE);
        } else {
            lin_freezeDeposit.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(queryPayInit.getData().getPayInitWrap().getFreezeDeposit())) {
            //???????????????????????? ?????????
            freezeDeposit = queryPayInit.getData().getPayInitWrap().getFreezeDeposit();
        }
        //?????????????????????
        playInfo.setTotalAmount(wrapBean.getPayAmount());
        //??????????????????
        playInfo.setBalanceY(wrapBean.getBalanceY());
        //??????????????????????????????
        playInfo.setInAmountWithBalance(wrapBean.getInAmountWithBalance());
        //?????????????????????????????????
        total_payAmount.setText(playInfo.getTotalAmount());
        //?????????????????????????????????
        payAmount.setText(wrapBean.getPayAmount() + "???");
        //?????????????????????????????????
        balance.setText(MoneyUtil.fmtMicrometer(MoneyUtil.moneySub(playInfo.getTotalAmount(), freezeDeposit)) + "???");


        if (!wrapBean.getInAmountWithBalance().equals("0.00")) {
            String userType = UserInfoSharePre.getUserType();
            if (userType.equals("company")) {
                showDialog("????????????????????????????????????????????????????????????????????????", "?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                return;
            }


        }

        //???????????????
        if (wrapBean.getIsJumpToInFund().equals("1")) {

            String userType = UserInfoSharePre.getUserType();

            if (userType.equals("personal")) {

                showDialog("?????????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.queryBankInfo();

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

            } else {
                showDialog("????????????????????????????????????????????????????????????????????????", "?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });

            }


        }

        //?????????????????????????????????0.00??????????????????????????????
        if (MoneyUtil.moneyComp(playInfo.getInAmountWithBalance(), "0.00")) {
            //?????????????????????
            presenter.queryUserBankCard();
            //??????????????????
            playInfo.setPlayType("1");
            //?????????????????????
            bank_play_layout.setVisibility(View.VISIBLE);
            //??????????????????
            balance.setText(MoneyUtil.fmtMicrometer(wrapBean.getBalanceY()) + "???");
            //?????????????????????
            bank_play_tv.setText(MoneyUtil.fmtMicrometer(wrapBean.getInAmountWithBalance()) + "???");

            //??????????????????0.00???????????????play_type
            if (playInfo.getBalanceY().equals("0.00")) {
                playInfo.setPlayType("0");
                play_type.setVisibility(View.GONE);
                play_type.setChecked(false);
            } else {
                play_type.setVisibility(View.VISIBLE);
                play_type.setEnabled(true);
                play_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        calculatePlayMoney();


                        CCLog.e("??????", play_type.isChecked());

                        if (play_type.isChecked()) {
                            //?????????????????????
                            playInfo.setPlayType("1");
                        } else {
                            playInfo.setPlayType("0");
                        }

                    }
                });
            }
        } else {
            playInfo.setPlayType("1");
            play_type.setVisibility(View.GONE);
            bank_play_layout.setVisibility(View.GONE);
            balance.setText(MoneyUtil.fmtMicrometer(MoneyUtil.moneySub(playInfo.getTotalAmount(), freezeDeposit)) + "???");
        }

    }


    final  PlayBaoInfo playBaoInfo = new PlayBaoInfo();
    //?????????????????????
    private void cardInit(QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan queryQuanInfo) {

        quan_name.setText("");

        bao_size.setText("");

        String quanSize = queryQuanInfo.getData().getQuanSize();
        String baoSize = queryQuanInfo.getData().getBaoSize();
        quan_number.setText("(" + quanSize + ")");
        bao_number.setText("(" + baoSize + ")");

        List<CardVoucherBean> cardVoucherList = new ArrayList<>();
        //????????????
        if (!queryQuanInfo.getData().getQuanListList().isEmpty()) {

            for (QueryMyKqQuan.PBIFE_trade_queryMyKqQuan.QuanList quanList : queryQuanInfo.getData().getQuanListList()) {
                CardVoucherBean cardVoucherBean = new CardVoucherBean();
                cardVoucherBean.setQuanDetailsId(quanList.getQuanDetailsId());
                cardVoucherBean.setQuanType(quanList.getQuanType());
                cardVoucherBean.setQuanTypeName(quanList.getQuanTypeName());
                cardVoucherBean.setQuanValue(quanList.getQuanValue());
                cardVoucherBean.setQuanCanStack(quanList.getQuanCanStack());
                cardVoucherBean.setQuanValidityEnd(quanList.getQuanValidityEnd());
                cardVoucherBean.setQuanIncreaseInterestAmount(quanList.getQuanIncreaseInterestAmount());
                cardVoucherBean.setEnableIncreaseInterestAmount(quanList.getEnableIncreaseInterestAmount());
                cardVoucherBean.setPercentValue(quanList.getPercentValue());
                cardVoucherBean.setQuanName(quanList.getQuanName());
                cardVoucherBean.setQuanFullReducedAmount(quanList.getQuanFullReducedAmount());
                cardVoucherBean.setCatalogRemark(quanList.getQuanCatalogRemark());
                cardVoucherBean.setQuanArrivalPriceLadder(quanList.getQuanArrivalPriceLadder());
                cardVoucherBean.setMostFineFlag(quanList.getMostFineFlag());
                cardVoucherList.add(cardVoucherBean);
            }
            playInfo.setCardVoucherList(cardVoucherList);
        }


        List<RadEnvelopeBean> radEnvelopeList = new ArrayList<>();
        //????????????
        if (!queryQuanInfo.getData().getBaoListList().isEmpty()) {

            for (QueryMyKqQuan.PBIFE_trade_queryMyKqQuan.BaoList baoList : queryQuanInfo.getData().getBaoListList()) {
                RadEnvelopeBean radEnvelopeBean = new RadEnvelopeBean();
                radEnvelopeBean.setQuanCanStack(baoList.getQuanCanStack());
                radEnvelopeBean.setQuanDetailsId(baoList.getQuanDetailsId());
                radEnvelopeBean.setQuanName(baoList.getQuanName());
                radEnvelopeBean.setQuanType(baoList.getQuanType());
                radEnvelopeBean.setQuanTypeName(baoList.getQuanTypeName());
                radEnvelopeBean.setQuanValue(baoList.getQuanValue());
                radEnvelopeBean.setQuanValidityEnd(baoList.getQuanValidityEnd());
                radEnvelopeBean.setQuanCatalogRemark(baoList.getQuanCatalogRemark());
                radEnvelopeBean.setQuanFullReducedAmount(baoList.getQuanFullReducedAmount());
                radEnvelopeBean.setMostFineFlag(baoList.getMostFineFlag());
                radEnvelopeList.add(radEnvelopeBean);
            }
            playInfo.setRadEnvelopeList(radEnvelopeList);
        }


        if (!cardVoucherList.isEmpty()) {

            choose_quan_layout.setOnClickListener(this);

            final HashMap<String, Object> playMap = new HashMap();

            final List<HashMap> playList = new ArrayList<>();

            for (CardVoucherBean cardVoucherBean : cardVoucherList) {

                if (cardVoucherBean.getMostFineFlag().equals("1")) {

                    //??????
                    String value = cardVoucherBean.getQuanValue();
                    //????????????
                    String type = cardVoucherBean.getQuanType();
                    //??????id
                    String id = cardVoucherBean.getQuanDetailsId();

                    String kqAddRatebj = cardVoucherBean.getEnableIncreaseInterestAmount();
                    playMap.put("value", value);
                    playMap.put("type", type);
                    playMap.put("id", id);
                    playMap.put("position", 0);
                    playMap.put("kqAddRatebj", kqAddRatebj);

                    break;
                }
            }
            playList.add(playMap);
            playBaoInfo.setPlayMap(playMap);
            playInfo.setPlayBaoInfo(playBaoInfo);

            defaultCheckQuan();
        }

        if (!radEnvelopeList.isEmpty()) {

            choose_bao_layout.setOnClickListener(this);


            List<HashMap> playList = new ArrayList<>();

            for (RadEnvelopeBean radEnvelopeBean : radEnvelopeList) {

                if (radEnvelopeBean.getMostFineFlag().equals("1")) {
                    HashMap<String, Object> playMap = new HashMap();
                    //??????
                    String value = radEnvelopeBean.getQuanValue();
                    //????????????
                    String type = radEnvelopeBean.getQuanType();
                    //??????id
                    String id = radEnvelopeBean.getQuanDetailsId();
                    //???????????????
                    String quanCanStack = radEnvelopeBean.getQuanCanStack();

                    playMap.put("value", value);
                    playMap.put("type", type);
                    playMap.put("id", id);
                    playMap.put("position", 0);
                    playMap.put("quanCanStack", quanCanStack);
                    playList.add(playMap);

                }

            }
            playBaoInfo.setPlayList(playList);
            playInfo.setPlayBaoInfo(playBaoInfo);
            defaultCheckBao();
        }


//            //?????????????????????????????????
//            if (quanSize.equals("1") && baoSize.equals("0")) {
//                bao_size.setText("");//??????????????????????????????
//                choose_quan_layout.setOnClickListener(this);
//                HashMap<String, Object> playMap = new HashMap();
//                PlayBaoInfo playBaoInfo = new PlayBaoInfo();
//                List<HashMap> playList = new ArrayList<>();
//                for (CardVoucherBean cardVoucherBean : playInfo.getCardVoucherList()) {
//                    //??????
//                    String value = cardVoucherBean.getQuanValue();
//                    //????????????
//                    String type = cardVoucherBean.getQuanType();
//                    //??????id
//                    String id = cardVoucherBean.getQuanDetailsId();
//
//                    String kqAddRatebj = cardVoucherBean.getEnableIncreaseInterestAmount();
//                    playMap.put("value", value);
//                    playMap.put("type", type);
//                    playMap.put("id", id);
//                    playMap.put("position", 0);
//                    playMap.put("kqAddRatebj", kqAddRatebj);
//
//                }
//
//                playList.add(playMap);
//                playBaoInfo.setPlayMap(playMap);
//                playInfo.setPlayBaoInfo(playBaoInfo);
//                defaultCheckQuan();
//
//            }
//            //?????????????????????????????????
//            else if (quanSize.equals("0") && baoSize.equals("1")) {
//                quan_name.setText("");//??????????????????????????????
//                choose_bao_layout.setOnClickListener(this);
//                List<HashMap> playList = new ArrayList<>();
//                HashMap<Object, Object> playMap = new HashMap();
//                PlayBaoInfo playBaoInfo = new PlayBaoInfo();
//                for (RadEnvelopeBean radEnvelopeBean : playInfo.getRadEnvelopeList()) {
//                    //??????
//                    String value = radEnvelopeBean.getQuanValue();
//                    //????????????
//                    String type = radEnvelopeBean.getQuanType();
//                    //??????id
//                    String id = radEnvelopeBean.getQuanDetailsId();
//                    //???????????????
//                    String quanCanStack = radEnvelopeBean.getQuanCanStack();
//
//                    playMap.put("value", value);
//                    playMap.put("type", type);
//                    playMap.put("id", id);
//                    playMap.put("position", 0);
//                    playMap.put("quanCanStack", quanCanStack);
//                }
//                playList.add(playMap);
//                playBaoInfo.setPlayList(playList);
//                playInfo.setPlayBaoInfo(playBaoInfo);
//                defaultCheckBao();
//            } else {
//                //?????????
//                if (!quanSize.equals("0")) {
//                    choose_quan_layout.setOnClickListener(this);
//                } else {
//                    quan_name.setText("");
//
//                }
//                //?????????
//                if (!baoSize.equals("0")) {
//                    choose_bao_layout.setOnClickListener(this);
//                } else {
//                    bao_size.setText("");
//                }
//            }

    }

    @Override
    public void playProduct(String returnCode, String returnMsg) {
        if (returnCode.equals("0000")) {
            Intent intent = new Intent(SpvReserveProductPlayActivity.this, ProductPlayStateActivity.class);
            intent.putExtra("playState", "????????????");
            startActivityForResult(intent, PLAY_CODE);
        }

    }

    /**
     * ????????????
     */
    @Override
    public void playTransferDetail(String returnCode, String returnMsg) {
        Intent intent = new Intent();
        intent.putExtra("playState", "????????????");
        intent.setClass(SpvReserveProductPlayActivity.this, ProductPlayStateActivity.class);
        startActivityForResult(intent, PLAY_CODE);
    }


    @Override
    public void onFundRechargeError(String returnMsg) {
        showDialog(getHtml(returnMsg), "???", "???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //????????????
                dialog.dismiss();
                baseStartActivity(SpvReserveProductPlayActivity.this, RechargeGuideActivity.class);
            }
        });
    }

    @Override
    public void onGuideAddBank(String returnMsg) {
        showDialog(returnMsg, "?????????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //?????????
                dialog.dismiss();
                baseStartActivity(SpvReserveProductPlayActivity.this, AddBankActivity.class);
            }
        });
    }

    @Override
    public void onKQDescription(final Dictionary.Ret_PBAPP_dictionary dictionary) {

        findViewById(R.id.kq_description_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(dictionary.getData().getParms(0).getKeyCode());

                CardDictionaryDialog dialog = new CardDictionaryDialog(SpvReserveProductPlayActivity.this);

                dialog.setContextStr(dictionary.getData().getParms(0).getKeyCode());

                dialog.createDialog().show();

            }
        });
    }


    @Override
    public void sendCode(String returnCode, String returnMsg, String serialNo) {
        if (returnCode.equals("0000")) {
            showDialog("???????????????????????????");
            sendmessage = true;
            playInfo.setCheckCodeSerialNo(serialNo);
            //????????????
            countDownTimer.start();
        } else {
            showDialog(returnMsg);
        }
    }

    @Override
    public void onBankInfo(String bankName) {
        bank_name.setText(bankName);
    }


    //???????????????-????????????
    private void calculatePlayMoney() {
        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();

        String basePlay = playInfo.getTotalAmount();

        //?????????????????????-??????-??????
        String remainPlayMoney = PlayBaoInfo.calculate(playBaoInfo, basePlay);

        total_payAmount.setText(MoneyUtil.fmtMicrometer(remainPlayMoney));


        //????????????
        if (!playInfo.getInAmountWithBalance().equals("0.00")) {
            //????????????????????????????????????????????????????????????
            String money = PlayBaoInfo.moneySub(remainPlayMoney, freezeDeposit);
            boolean isBankPlay = PlayBaoInfo.moneyCompare(playInfo.getBalanceY(), money);
            if (isBankPlay) {
                play_type.setVisibility(View.GONE);
                bank_play_layout.setVisibility(View.GONE);
                //????????????
                balance.setText(MoneyUtil.fmtMicrometer(PlayBaoInfo.moneySub(remainPlayMoney, freezeDeposit)) + "???");
            } else {
                boolean isBalance = PlayBaoInfo.moneyComp(playInfo.getBalanceY(), "0.00");
                //?????????????????????
                if (isBalance) {
                    play_type.setVisibility(View.VISIBLE);
                } else {
                    play_type.setVisibility(View.GONE);
                }
                bank_play_layout.setVisibility(View.VISIBLE);
                //???????????????
                String bankPlayMoney = PlayBaoInfo.moneySub(remainPlayMoney, freezeDeposit);
                //?????????????????????
                if (play_type.isChecked()) {
                    //???????????????-??????-??????-?????????-?????? = ????????????
                    //???????????????
                    bankPlayMoney = PlayBaoInfo.moneySub(bankPlayMoney, playInfo.getBalanceY());
                    bank_play_tv.setText(MoneyUtil.fmtMicrometer(bankPlayMoney) + "???");
                    //????????????????????????
                    balance.setText(MoneyUtil.fmtMicrometer(playInfo.getBalanceY()) + "???");
                } else {
                    bank_play_tv.setText(MoneyUtil.fmtMicrometer(bankPlayMoney) + "???");
                    balance.setText("0.00" + "???");
                }
            }
        } else {
            play_type.setVisibility(View.GONE);
            balance.setText(MoneyUtil.fmtMicrometer(PlayBaoInfo.moneySub(remainPlayMoney, freezeDeposit)) + "???");
        }

    }

    //??????????????????
    private void defaultCheckQuan() {
        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();
        HashMap<String, Object> playMap = playBaoInfo.getPlayMap();

        if (playMap != null && !playMap.isEmpty()) {
            //???????????????
            String type = (String) playMap.get("type");
            String value = (String) playMap.get("value");
            //"A":?????????,"L":?????????,"F":?????????
            switch (type) {
                case "A":
                    double percentage = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("0.00");
                    String percent = df.format((percentage * 100));
                    String kqAddRatebj = (String) playMap.get("kqAddRatebj");
                    playInfo.getPlayBaoInfo().setKqAddRatebj(kqAddRatebj);
//                    BigDecimal bigDecimal = new BigDecimal(MoneyUtil.moneyMul(value, "100"));
                    quan_name.setText("?????? ?????????" + percent + "%");
                    quan_value.setText("0.00");
                    break;
                case "L":
                    quan_name.setText("?????? ?????????" + value);
                    quan_value.setText("-" + value);
                    break;
                case "F":
                    quan_name.setText("?????? ?????????" + value + "???");
                    quan_value.setText("-" + value);
                    break;
                default:
                    break;
            }
        } else {
            quan_name.setText("?????????");
            quan_value.setText("0.00");
        }


        calculatePlayMoney();
    }

    //??????????????????
    private void defaultCheckBao() {
        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();
        List<HashMap> playList = playBaoInfo.getPlayList();

        if (playList != null && !playList.isEmpty()) {
            bao_size.setText("??????" + playList.size() + "?????????");
            String baoAmount = playBaoInfo.addBaoListMoney(playBaoInfo.getPlayList());
            bao_value.setText("-" + baoAmount);
        } else {
            bao_value.setText("0.00");
            bao_size.setText("?????????");
        }
        calculatePlayMoney();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.choose_quan_layout:
                Intent intent = new Intent(SpvReserveProductPlayActivity.this, ChooseQuanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("playQuan", playInfo);
                intent.putExtra("quanBundle", bundle);
                startActivityForResult(intent, QUAN_LIST_REQUEST_CODE);
                break;

            case R.id.choose_bao_layout:
                Intent baoIntent = new Intent(SpvReserveProductPlayActivity.this, ChooseBaoActivity.class);
                Bundle baoBundle = new Bundle();
                baoBundle.putParcelable("playBao", playInfo);
                baoIntent.putExtra("baoBundle", baoBundle);
                startActivityForResult(baoIntent, BAO_LIST_REQUEST_CODE);
                break;


            case R.id.play:


                List<CardVoucherBean> cardVoucherList = playInfo.getCardVoucherList();
                //????????????
                List<RadEnvelopeBean> radEnvelopeList = playInfo.getRadEnvelopeList();

                PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();


                //???????????????????????????
                if ((cardVoucherList != null && !cardVoucherList.isEmpty()) || (radEnvelopeList != null && !radEnvelopeList.isEmpty())) {


                    HashMap<String, Object> playMap = null;
                    List<HashMap> playList = null;

                    if (playBaoInfo != null) {

                        playMap = playBaoInfo.getPlayMap();

                        playList = playBaoInfo.getPlayList();

                    }

                    if ((playMap == null || playMap.isEmpty()) && (playList == null || playList.isEmpty())) {
                        CustomDialog.Builder builder = new CustomDialog.Builder(this);
                        builder.setTitle("????????????");
                        builder.setMessage("???????????????????????????/????????????");
                        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (sms_layout.getVisibility() == View.VISIBLE && !sendmessage) {
                                    showDialog("???????????????????????????");
                                    return;
                                }
                                if (Utils.isViewEmpty(sms_ed) && sms_layout.getVisibility() == View.VISIBLE) {
                                    showDialog("????????????????????????");
                                } else {
                                    playInfo.setCheckCode(sms_ed.getText().toString());
                                    isPlay();
                                }
                            }
                        });
                        builder.create().show();
                    } else {
                        if (sms_layout.getVisibility() == View.VISIBLE && !sendmessage) {
                            showDialog("???????????????????????????");
                            return;
                        }
                        if (Utils.isViewEmpty(sms_ed) && sms_layout.getVisibility() == View.VISIBLE) {
                            showDialog("????????????????????????");
                        } else {
                            playInfo.setCheckCode(sms_ed.getText().toString());
                            isPlay();
                        }
                    }

                } else {
                    if (sms_layout.getVisibility() == View.VISIBLE && !sendmessage) {
                        showDialog("???????????????????????????");
                        return;
                    }
                    if (Utils.isViewEmpty(sms_ed) && sms_layout.getVisibility() == View.VISIBLE) {
                        showDialog("????????????????????????");
                    } else {
                        playInfo.setCheckCode(sms_ed.getText().toString());
                        isPlay();
                    }
                }

                break;
            case R.id.sms_button://???????????????
                presenter.sendSms(playInfo.getInAmountWithBalance());
                break;
            default:
                break;

        }
    }

    //?????????????????????????????????
    @Override
    public void querySms(String returnCode, String returnMsg, String needSms) {
        if (returnCode.equals("0000")) {
            //???????????????????????????
            if (needSms.equals("false")) {

                sms_layout.setVisibility(View.GONE);
            } else {
                sms_layout.setVisibility(View.VISIBLE);
            }
        } else {
            showDialog(returnMsg);
        }

    }


    private String bankName, bankCard;

    @Override
    public void queryRechargeBankInfo(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo) {
        bankName = bankCardInfo.getData().getBankName();
        bankCard = bankCardInfo.getData().getBankCardNo();
        //??????????????????
        if (bankCardInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "301");
            baseStartActivity(intent);
        } else {
            //?????????????????????
            presenter.queryFundBankInfo(bankCardInfo.getData().getBankNo());
        }
    }

    @Override
    public void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {

        //??????
        if (fundBankInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            String totalPayAmount = total_payAmount.getText().toString();
            String balance = playInfo.getBalanceY();
            String payBanlance = MoneyUtil.moneySub(totalPayAmount, balance);
            intent.putExtra("payAmount", MoneyUtil.moneySub(payBanlance, freezeDeposit));
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "302");
            intent.putExtra("tag", "isReserveSubscribe");
            startActivityForResult(intent, RECHARGE_CODE);
        } else {
            String totalPayAmount = total_payAmount.getText().toString();
            String balance = playInfo.getBalanceY();
            String payBanlance = MoneyUtil.moneySub(totalPayAmount, balance);
            Intent intent = new Intent(this, RechargeActivity.class);
            intent.putExtra("payAmount", MoneyUtil.moneySub(payBanlance, freezeDeposit));
            intent.putExtra("tag", "isReserveSubscribe");
            startActivityForResult(intent, RECHARGE_CODE);
        }
    }


    private SpannableString getHtml(String content) {

        String str = "??????????????????????????????????????????????????????????????????";

        StringBuffer buffer = new StringBuffer(content);

        buffer.append("\n").append("??????????????????????????????????????????????????????????????????");

        SpannableString spannableString = new SpannableString(buffer.toString());

        StyleSpan span = new StyleSpan(Typeface.BOLD);

        spannableString.setSpan(span, buffer.length() - str.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return spannableString;
    }
}
