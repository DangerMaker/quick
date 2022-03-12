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
import com.hundsun.zjfae.activity.product.TransferDetailPlayActivity;
import com.hundsun.zjfae.activity.product.bean.CardVoucherBean;
import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
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
 * @Description:预约购买支付界面
 * @Author: zhoujianyu
 * @Time: 2019/3/4 17:08
 */
public class ReserveProductPlayActivity extends CommActivity<ReserveProductPlayPresenter> implements View.OnClickListener, ReserveProductPlayView {

    public static final int QUAN_LIST_REQUEST_CODE = 0x7071;
    private static final int BAO_LIST_REQUEST_CODE = 0x7072;
    private static final int QUAN_LIST_RESULT_CODE = 0x758;
    private static final int BAO_LIST_RESULT_CODE = 0x759;

    private static final int PLAY_CODE = 0x789;


    private static final int RECHARGE_CODE = 0x790;


    //优惠券数量
    private TextView quan_number;
    //优惠券名字
    private TextView quan_name;
    //优惠券点击布局
    private LinearLayout choose_quan_layout;
    //红包数量
    private TextView bao_number;
    //红包大小
    private TextView bao_size;
    //红包点击布局
    private LinearLayout choose_bao_layout;

    //购买总价
    private TextView payAmount;
    //优惠券抵用
    private TextView quan_value;
    //红包抵用
    private TextView bao_value;
    //总计支付
    private TextView total_payAmount;

    //余额支付金额
    private TextView balance;
    //余额不足是否勾选余额支付+银行卡支付
    private CheckBox play_type;
    //银行卡支付布局
    private LinearLayout bank_play_layout;
    //银行卡支付金额
    private TextView bank_play_tv;
    //银行卡名字
    private TextView bank_name;
    //保证金比例布局
    private LinearLayout lin_freezeDeposit;
    private TextView tv_freezeDeposit;
    private String freezeDeposit = "0.00";

    //短信验证码布局
    private LinearLayout sms_layout;

    //发送短信验证码按钮
    private Button sms_button;

    //输入短信验证码
    private EditText sms_ed;

    //短信倒计时
    private CustomCountDownTimer countDownTimer;

    private ReserveProductPlayPresenter presenter;
    //支付信息类
    private ReserveProductPlay playInfo;

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
        setTitle("预约支付");
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
    }


    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("product_bundle");
        playInfo = bundle.getParcelable("product_info");
        orderType = intent.getStringExtra("orderType");
        presenter.init(playInfo.getProductCode(), playInfo.getPlayAmount(), playInfo.getSerialNoStr());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CCLog.e("支付页requestCode", requestCode);
        CCLog.e("支付页resultCode", resultCode);
        if (requestCode == PLAY_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        //卡券选择
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
            //红包
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
        //如果键盘显示 隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(sms_ed.getWindowToken(), 0); //强制隐藏键盘
        final PlayWindow play = new PlayWindow(ReserveProductPlayActivity.this);
        play.showAtLocation(findViewById(R.id.play_layout));
        play.setPayListener(new PlayWindow.OnPayListener() {
            @Override
            public void onSurePay(String password) {
                //支付
                playInfo.setPlayPassWord(EncDecUtil.AESEncrypt(password));
                presenter.playProduct(playInfo);
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

    //初始化项目信息
    private void productInit(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit) {

        final QueryPayInit.PBIFE_trade_queryPayInit.PayInitWrap wrapBean = queryPayInit.getData().getPayInitWrap();
        if (orderType.equals("1")) {
            //如果保证金不为空 显示保证金比例布局
            tv_freezeDeposit.setText(queryPayInit.getData().getPayInitWrap().getFreezeDeposit() + "元");
            lin_freezeDeposit.setVisibility(View.VISIBLE);
        } else {
            lin_freezeDeposit.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(queryPayInit.getData().getPayInitWrap().getFreezeDeposit())) {
            //如果保证金不为空 则赋值
            freezeDeposit = queryPayInit.getData().getPayInitWrap().getFreezeDeposit();
        }
        //设置支付总金额
        playInfo.setTotalAmount(wrapBean.getPayAmount());
        //设置可用余额
        playInfo.setBalanceY(wrapBean.getBalanceY());
        //设置当前支付充值金额
        playInfo.setInAmountWithBalance(wrapBean.getInAmountWithBalance());
        //总计支付默认支付总金额
        total_payAmount.setText(playInfo.getTotalAmount());
        //购买总价默认支付总金额
        payAmount.setText(wrapBean.getPayAmount() + "元");
        //余额支付默认支付总金额
        balance.setText(MoneyUtil.fmtMicrometer(MoneyUtil.moneySub(playInfo.getTotalAmount(), freezeDeposit)) + "元");


        if (!wrapBean.getInAmountWithBalance().equals("0.00")) {
            String userType = UserInfoSharePre.getUserType();
            if (userType.equals("company")) {
                showDialog("您的账户余额不足，请通过线下充值的方式进行充值！", "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                return;
            }


        }

        //提示去充值
        if (wrapBean.getIsJumpToInFund().equals("1")) {

            String userType = UserInfoSharePre.getUserType();

            if (userType.equals("personal")) {

                showDialog("您的账户余额不足，请先对账户进行充值！", "去充值", "取消", new DialogInterface.OnClickListener() {
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
                showDialog("您的账户余额不足，请通过线下充值的方式进行充值！", "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });

            }


        }

        //当余额支付充值金额不为0.00，说明要充值方式支付
        if (MoneyUtil.moneyComp(playInfo.getInAmountWithBalance(), "0.00")) {
            //请求银行卡信息
            presenter.queryUserBankCard();
            //设置支付类型
            playInfo.setPlayType("1");
            //显示银行卡信息
            bank_play_layout.setVisibility(View.VISIBLE);
            //设置可用余额
            balance.setText(MoneyUtil.fmtMicrometer(wrapBean.getBalanceY()) + "元");
            //银行卡支付金额
            bank_play_tv.setText(MoneyUtil.fmtMicrometer(wrapBean.getInAmountWithBalance()) + "元");

            //当可用余额为0.00时，不显示play_type
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


                        CCLog.e("状态", play_type.isChecked());

                        if (play_type.isChecked()) {
                            //选中，余额支付
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
            balance.setText(MoneyUtil.fmtMicrometer(MoneyUtil.moneySub(playInfo.getTotalAmount(), freezeDeposit)) + "元");
        }

    }


    final  PlayBaoInfo playBaoInfo = new PlayBaoInfo();
    //初始化卡券信息
    private void cardInit(QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan queryQuanInfo) {

        quan_name.setText("");

        bao_size.setText("");

        String quanSize = queryQuanInfo.getData().getQuanSize();
        String baoSize = queryQuanInfo.getData().getBaoSize();
        quan_number.setText("(" + quanSize + ")");
        bao_number.setText("(" + baoSize + ")");

        List<CardVoucherBean> cardVoucherList = new ArrayList<>();
        //卡券遍历
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
        //红包遍历
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

                    //金额
                    String value = cardVoucherBean.getQuanValue();
                    //卡券类型
                    String type = cardVoucherBean.getQuanType();
                    //卡券id
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
                    //金额
                    String value = radEnvelopeBean.getQuanValue();
                    //卡券类型
                    String type = radEnvelopeBean.getQuanType();
                    //卡券id
                    String id = radEnvelopeBean.getQuanDetailsId();
                    //是否可叠加
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


//            //只有一张卡券，没有红包
//            if (quanSize.equals("1") && baoSize.equals("0")) {
//                bao_size.setText("");//红包那一栏清空未选择
//                choose_quan_layout.setOnClickListener(this);
//                HashMap<String, Object> playMap = new HashMap();
//                PlayBaoInfo playBaoInfo = new PlayBaoInfo();
//                List<HashMap> playList = new ArrayList<>();
//                for (CardVoucherBean cardVoucherBean : playInfo.getCardVoucherList()) {
//                    //金额
//                    String value = cardVoucherBean.getQuanValue();
//                    //卡券类型
//                    String type = cardVoucherBean.getQuanType();
//                    //卡券id
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
//            //只有一张红包，没有卡券
//            else if (quanSize.equals("0") && baoSize.equals("1")) {
//                quan_name.setText("");//卡券那一栏清空未选择
//                choose_bao_layout.setOnClickListener(this);
//                List<HashMap> playList = new ArrayList<>();
//                HashMap<Object, Object> playMap = new HashMap();
//                PlayBaoInfo playBaoInfo = new PlayBaoInfo();
//                for (RadEnvelopeBean radEnvelopeBean : playInfo.getRadEnvelopeList()) {
//                    //金额
//                    String value = radEnvelopeBean.getQuanValue();
//                    //卡券类型
//                    String type = radEnvelopeBean.getQuanType();
//                    //卡券id
//                    String id = radEnvelopeBean.getQuanDetailsId();
//                    //是否可叠加
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
//                //卡券有
//                if (!quanSize.equals("0")) {
//                    choose_quan_layout.setOnClickListener(this);
//                } else {
//                    quan_name.setText("");
//
//                }
//                //红包有
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
            Intent intent = new Intent(ReserveProductPlayActivity.this, ProductPlayStateActivity.class);
            intent.putExtra("playState", "交易成功");
            startActivityForResult(intent, PLAY_CODE);
        }

    }

    /**
     * 转让购买
     */
    @Override
    public void playTransferDetail(String returnCode, String returnMsg) {
        Intent intent = new Intent();
        intent.putExtra("playState", "交易成功");
        intent.setClass(ReserveProductPlayActivity.this, ProductPlayStateActivity.class);
        startActivityForResult(intent, PLAY_CODE);
    }


    @Override
    public void onFundRechargeError(String returnMsg) {
        showDialog(getHtml(returnMsg), "是", "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //线下充值
                dialog.dismiss();
                baseStartActivity(ReserveProductPlayActivity.this, RechargeGuideActivity.class);
            }
        });
    }

    @Override
    public void onGuideAddBank(String returnMsg) {
        showDialog(returnMsg, "去绑卡", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //去绑卡
                dialog.dismiss();
                baseStartActivity(ReserveProductPlayActivity.this, AddBankActivity.class);
            }
        });
    }

    @Override
    public void onKQDescription(final Dictionary.Ret_PBAPP_dictionary dictionary) {

        findViewById(R.id.kq_description_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(dictionary.getData().getParms(0).getKeyCode());

                CardDictionaryDialog dialog = new CardDictionaryDialog(ReserveProductPlayActivity.this);

                dialog.setContextStr(dictionary.getData().getParms(0).getKeyCode());

                dialog.createDialog().show();

            }
        });
    }


    @Override
    public void sendCode(String returnCode, String returnMsg, String serialNo) {
        if (returnCode.equals("0000")) {
            showDialog("短信验证码获取成功");
            sendmessage = true;
            playInfo.setCheckCodeSerialNo(serialNo);
            //发送成功
            countDownTimer.start();
        } else {
            showDialog(returnMsg);
        }
    }

    @Override
    public void onBankInfo(String bankName) {
        bank_name.setText(bankName);
    }


    //计算总金额-红包金额
    private void calculatePlayMoney() {
        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();

        String basePlay = playInfo.getTotalAmount();

        //计算总支付金额-红包-卡券
        String remainPlayMoney = PlayBaoInfo.calculate(playBaoInfo, basePlay);

        total_payAmount.setText(MoneyUtil.fmtMicrometer(remainPlayMoney));


        //需要充值
        if (!playInfo.getInAmountWithBalance().equals("0.00")) {
            //减去红包，卡券，保证金之后余额是否可支付
            String money = PlayBaoInfo.moneySub(remainPlayMoney, freezeDeposit);
            boolean isBankPlay = PlayBaoInfo.moneyCompare(playInfo.getBalanceY(), money);
            if (isBankPlay) {
                play_type.setVisibility(View.GONE);
                bank_play_layout.setVisibility(View.GONE);
                //余额支付
                balance.setText(MoneyUtil.fmtMicrometer(PlayBaoInfo.moneySub(remainPlayMoney, freezeDeposit)) + "元");
            } else {
                boolean isBalance = PlayBaoInfo.moneyComp(playInfo.getBalanceY(), "0.00");
                //是否有可用余额
                if (isBalance) {
                    play_type.setVisibility(View.VISIBLE);
                } else {
                    play_type.setVisibility(View.GONE);
                }
                bank_play_layout.setVisibility(View.VISIBLE);
                //要充值金额
                String bankPlayMoney = PlayBaoInfo.moneySub(remainPlayMoney, freezeDeposit);
                //当选中余额支付
                if (play_type.isChecked()) {
                    //总支付金额-红包-卡券-保证金-余额 = 充值金额
                    //要充值金额
                    bankPlayMoney = PlayBaoInfo.moneySub(bankPlayMoney, playInfo.getBalanceY());
                    bank_play_tv.setText(MoneyUtil.fmtMicrometer(bankPlayMoney) + "元");
                    //当前余额支付金额
                    balance.setText(MoneyUtil.fmtMicrometer(playInfo.getBalanceY()) + "元");
                } else {
                    bank_play_tv.setText(MoneyUtil.fmtMicrometer(bankPlayMoney) + "元");
                    balance.setText("0.00" + "元");
                }
            }
        } else {
            play_type.setVisibility(View.GONE);
            balance.setText(MoneyUtil.fmtMicrometer(PlayBaoInfo.moneySub(remainPlayMoney, freezeDeposit)) + "元");
        }

    }

    //默认选中卡券
    private void defaultCheckQuan() {
        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();
        HashMap<String, Object> playMap = playBaoInfo.getPlayMap();

        if (playMap != null && !playMap.isEmpty()) {
            //外部设置值
            String type = (String) playMap.get("type");
            String value = (String) playMap.get("value");
            //"A":加息券,"L":抵用券,"F":满减券
            switch (type) {
                case "A":
                    double percentage = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("0.00");
                    String percent = df.format((percentage * 100));
                    String kqAddRatebj = (String) playMap.get("kqAddRatebj");
                    playInfo.getPlayBaoInfo().setKqAddRatebj(kqAddRatebj);
//                    BigDecimal bigDecimal = new BigDecimal(MoneyUtil.moneyMul(value, "100"));
                    quan_name.setText("已选 加息券" + percent + "%");
                    quan_value.setText("0.00");
                    break;
                case "L":
                    quan_name.setText("已选 抵价券" + value);
                    quan_value.setText("-" + value);
                    break;
                case "F":
                    quan_name.setText("已选 满减券" + value + "元");
                    quan_value.setText("-" + value);
                    break;
                default:
                    break;
            }
        } else {
            quan_name.setText("未选择");
            quan_value.setText("0.00");
        }


        calculatePlayMoney();
    }

    //默认选中红包
    private void defaultCheckBao() {
        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();
        List<HashMap> playList = playBaoInfo.getPlayList();

        if (playList != null && !playList.isEmpty()) {
            bao_size.setText("已选" + playList.size() + "个红包");
            String baoAmount = playBaoInfo.addBaoListMoney(playBaoInfo.getPlayList());
            bao_value.setText("-" + baoAmount);
        } else {
            bao_value.setText("0.00");
            bao_size.setText("未选择");
        }
        calculatePlayMoney();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.choose_quan_layout:
                Intent intent = new Intent(ReserveProductPlayActivity.this, ChooseQuanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("playQuan", playInfo);
                intent.putExtra("reserveProductQuanBundle", bundle);
                startActivityForResult(intent, QUAN_LIST_REQUEST_CODE);
                break;

            case R.id.choose_bao_layout:
                Intent baoIntent = new Intent(ReserveProductPlayActivity.this, ChooseBaoActivity.class);
                Bundle baoBundle = new Bundle();
                baoBundle.putParcelable("playBao", playInfo);
                baoIntent.putExtra("reserveProductBaoBundle", baoBundle);
                startActivityForResult(baoIntent, BAO_LIST_REQUEST_CODE);
                break;


            case R.id.play:


                List<CardVoucherBean> cardVoucherList = playInfo.getCardVoucherList();
                //红包集合
                List<RadEnvelopeBean> radEnvelopeList = playInfo.getRadEnvelopeList();

                PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();


                //卡券或者红包不为空
                if ((cardVoucherList != null && !cardVoucherList.isEmpty()) || (radEnvelopeList != null && !radEnvelopeList.isEmpty())) {


                    HashMap<String, Object> playMap = null;
                    List<HashMap> playList = null;

                    if (playBaoInfo != null) {

                        playMap = playBaoInfo.getPlayMap();

                        playList = playBaoInfo.getPlayList();

                    }

                    if ((playMap == null || playMap.isEmpty()) && (playList == null || playList.isEmpty())) {
                        CustomDialog.Builder builder = new CustomDialog.Builder(this);
                        builder.setTitle("温馨提示");
                        builder.setMessage("您确定不使用优惠券/红包吗？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (sms_layout.getVisibility() == View.VISIBLE && !sendmessage) {
                                    showDialog("请先获取短信验证码");
                                    return;
                                }
                                if (Utils.isViewEmpty(sms_ed) && sms_layout.getVisibility() == View.VISIBLE) {
                                    showDialog("请输入短信验证码");
                                } else {
                                    playInfo.setCheckCode(sms_ed.getText().toString());
                                    isPlay();
                                }
                            }
                        });
                        builder.create().show();
                    } else {
                        if (sms_layout.getVisibility() == View.VISIBLE && !sendmessage) {
                            showDialog("请先获取短信验证码");
                            return;
                        }
                        if (Utils.isViewEmpty(sms_ed) && sms_layout.getVisibility() == View.VISIBLE) {
                            showDialog("请输入短信验证码");
                        } else {
                            playInfo.setCheckCode(sms_ed.getText().toString());
                            isPlay();
                        }
                    }

                } else {
                    if (sms_layout.getVisibility() == View.VISIBLE && !sendmessage) {
                        showDialog("请先获取短信验证码");
                        return;
                    }
                    if (Utils.isViewEmpty(sms_ed) && sms_layout.getVisibility() == View.VISIBLE) {
                        showDialog("请输入短信验证码");
                    } else {
                        playInfo.setCheckCode(sms_ed.getText().toString());
                        isPlay();
                    }
                }

                break;
            case R.id.sms_button://短信验证码
                presenter.sendSms(playInfo.getInAmountWithBalance());
                break;
            default:
                break;

        }
    }

    //检验是否需要短信验证码
    @Override
    public void querySms(String returnCode, String returnMsg, String needSms) {
        if (returnCode.equals("0000")) {
            //需要发送短信验证码
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
        //充值渠道关闭
        if (bankCardInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "301");
            baseStartActivity(intent);
        } else {
            //查询银行卡限额
            presenter.queryFundBankInfo(bankCardInfo.getData().getBankNo());
        }
    }

    @Override
    public void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {

        //限额
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

        String str = "您也可通过“银行卡转账”方式充值，是否前往？";

        StringBuffer buffer = new StringBuffer(content);

        buffer.append("\n").append("您也可通过“银行卡转账”方式充值，是否前往？");

        SpannableString spannableString = new SpannableString(buffer.toString());

        StyleSpan span = new StyleSpan(Typeface.BOLD);

        spannableString.setSpan(span, buffer.length() - str.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return spannableString;
    }
}
