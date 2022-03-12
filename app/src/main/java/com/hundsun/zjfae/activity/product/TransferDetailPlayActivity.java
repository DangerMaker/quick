package com.hundsun.zjfae.activity.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
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
import com.hundsun.zjfae.activity.product.bean.CardVoucherBean;
import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.RadEnvelopeBean;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.activity.product.presenter.TransferDetailPlayPresenter;
import com.hundsun.zjfae.activity.product.view.TransferDetailPlayView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;
import com.hundsun.zjfae.common.view.dialog.CardDictionaryDialog;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;
import com.hundsun.zjfae.common.view.dialog.ImageCodeDialog;
import com.hundsun.zjfae.common.view.popwindow.PlayWindow;
import com.zjfae.captcha.CustomCaptchaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.QueryMyKqQuan;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gensazj.Dictionary;


/**
 * @author moran
 * 受让支付
 */
public class TransferDetailPlayActivity extends CommActivity<TransferDetailPlayPresenter> implements View.OnClickListener, TransferDetailPlayView {


    //产品名称
    private TextView productName;
    //发行利率
    private TextView expectedMaxAnnualRate;
    //参考收益率
    private TextView targetRate;
    //可购本金
    private TextView delegateNum;
    //理财期限
    private TextView leftDays;

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
    /**
     * 银行卡名字
     */
    private TextView bank_name;


    /**
     * 短信验证码布局
     */
    private LinearLayout sms_layout;

    /**
     * 发送短信验证码按钮
     */
    private Button sms_button;

    /**
     * 输入短信验证码
     */
    private EditText sms_ed;

    /**
     * 短信倒计时
     */
    private CustomCountDownTimer countDownTimer;


    public static final int QUAN_LIST_REQUEST_CODE = 0x7071;
    private static final int BAO_LIST_REQUEST_CODE = 0x7072;
    private static final int QUAN_LIST_RESULT_CODE = 0x758;
    private static final int BAO_LIST_RESULT_CODE = 0x759;
    private static final int RECHARGE_CODE = 0x790;
    private static final int PLAY_CODE = 0x789;

    /**
     * 支付信息类
     */
    private TransferDetailPlay transferDetailPlay;


    private Button play;

    /**
     * 记录网易盾请求失败原因
     */
    private int count = 0;
    /**
     * 判断是网易盾还是图形验证码
     */
    private String paraValue = "";

    /**
     * 是否智能无感知
     */
    private String senseFlag = "false";

    /**
     * 转让购买
     */
    @Override
    public void playTransferDetail(String returnCode, String returnMsg) {
        Intent intent = new Intent();
        intent.putExtra("playState", "受让成功");
        intent.setClass(TransferDetailPlayActivity.this, ProductPlayStateActivity.class);
        startActivityForResult(intent, PLAY_CODE);
    }

    @Override
    public void onFundRechargeError(String returnMsg) {
        showDialog(getHtml(returnMsg), "是", "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //线下充值
                dialog.dismiss();
                baseStartActivity(TransferDetailPlayActivity.this, RechargeGuideActivity.class);
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

                baseStartActivity(TransferDetailPlayActivity.this, AddBankActivity.class);
            }
        });
    }

    @Override
    public void onKQDescription(final Dictionary.Ret_PBAPP_dictionary dictionary) {

        findViewById(R.id.kq_description_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(dictionary.getData().getParms(0).getKeyCode());
                CardDictionaryDialog dialog = new CardDictionaryDialog(TransferDetailPlayActivity.this);

                dialog.setContextStr(dictionary.getData().getParms(0).getKeyCode());

                dialog.createDialog().show();

            }
        });

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


    @Override
    public void onTransferPlayInit(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit) {


        final QueryPayInit.PBIFE_trade_queryPayInit.PayInitWrap wrapBean = queryPayInit.getData().getPayInitWrap();

        //设置支付总金额
        transferDetailPlay.setTotalAmount(wrapBean.getPayAmount());
        //设置可用余额
        transferDetailPlay.setBalanceY(wrapBean.getBalanceY());
        //设置当前支付充值金额
        transferDetailPlay.setInAmountWithBalance(wrapBean.getInAmountWithBalance());
        //总计支付默认支付总金额
        total_payAmount.setText(transferDetailPlay.getTotalAmount());
        //购买总价默认支付总金额
        payAmount.setText(transferDetailPlay.getTotalAmount() + "元");
        //余额支付默认支付总金额
        balance.setText(transferDetailPlay.getTotalAmount() + "元");


        String userType = UserInfoSharePre.getUserType();

        if (!queryPayInit.getData().getPayInitWrap().getInAmountWithBalance().equals("0.00")) {
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

            //个人
            if (userType.equals("personal")) {

                showDialog("您的账户余额不足，请先对账户进行充值！", "去充值", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.queryBankInfo();

                    }
                });
            }

            //机构用户提示
            else {
                showDialog("您的账户余额不足，请通过线下充值的方式进行充值！", "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });

            }


        } else {

            //当余额支付充值金额不为0.00，说明要充值方式支付
            if (PlayBaoInfo.moneyComp(transferDetailPlay.getInAmountWithBalance(), "0.00")) {
                //请求银行卡信息onBankInfo
                presenter.queryUserBankCard();
                //设置支付类型
                transferDetailPlay.setPlayType("1");
                //显示银行卡信息
                bank_play_layout.setVisibility(View.VISIBLE);
                //设置可用余额
                balance.setText(wrapBean.getBalanceY() + "元");
                //银行卡支付金额
                bank_play_tv.setText(MoneyUtil.fmtMicrometer(wrapBean.getInAmountWithBalance()));

                //当可用余额为0.00时，不显示play_type
                if (transferDetailPlay.getBalanceY().equals("0.00")) {
                    transferDetailPlay.setPlayType("0");
                    play_type.setVisibility(View.GONE);
                    play_type.setChecked(false);
                } else {
                    play_type.setVisibility(View.VISIBLE);
                    play_type.setChecked(true);
                    play_type.setEnabled(true);
                    play_type.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            calculatePlayMoney();

                            if (play_type.isChecked()) {
                                //选中，余额支付
                                transferDetailPlay.setPlayType("1");
                            } else {
                                transferDetailPlay.setPlayType("0");
                            }

                        }
                    });
                }
            }

        }

    }

    private final PlayBaoInfo playBaoInfo = new PlayBaoInfo();
    @Override
    public void onQuanInfo(QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan queryQuanInfo) {

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
            transferDetailPlay.setCardVoucherList(cardVoucherList);
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
            transferDetailPlay.setRadEnvelopeList(radEnvelopeList);
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
            transferDetailPlay.setPlayBaoInfo(playBaoInfo);

            defaultCheckQuan();
        }

        if (!radEnvelopeList.isEmpty()) {

            choose_bao_layout.setOnClickListener(this);

             final List<HashMap> playList = new ArrayList<>();

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
            transferDetailPlay.setPlayBaoInfo(playBaoInfo);
            defaultCheckBao();
        }


    }

    /**
     * 银行卡信息
     **/
    @Override
    public void onBankInfo(String bankName) {

        bank_name.setText(bankName);
    }

    /**
     * 检验是否需要短信验证码
     **/
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
    public void queryRechargeBankInfo(String bankName, String bankCard, String bankNo, String showTips) {
        this.bankName = bankName;
        this.bankCard = bankCard;
        //充值渠道关闭
        if (showTips.equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "301");
            baseStartActivity(intent);
        } else {
            //查询银行卡限额
            presenter.queryFundBankInfo(bankNo);
        }
    }

    @Override
    public void queryRechargeBankInfo(String bankName, String bankCard, String bankNo, String showTips, String payChannelNo) {
        presenter.setPayChannelNo(payChannelNo);
        queryRechargeBankInfo(bankName, bankCard, bankNo, showTips);
    }

    @Override
    public void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {

        //限额
        if (fundBankInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            String totalPayAmount = total_payAmount.getText().toString();
            String balance = transferDetailPlay.getBalanceY();
            String payBanlance = MoneyUtil.moneySub(totalPayAmount, balance);
            intent.putExtra("payAmount", payBanlance);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("tag", "isTransfer");
            intent.putExtra("type", "302");
            startActivityForResult(intent, RECHARGE_CODE);
        } else {
            String totalPayAmount = total_payAmount.getText().toString();
            String balance = transferDetailPlay.getBalanceY();
            String payBanlance = MoneyUtil.moneySub(totalPayAmount, balance);
            Intent intent = new Intent(this, RechargeActivity.class);
            intent.putExtra("payAmount", payBanlance);
            intent.putExtra("tag", "isTransfer");
            startActivityForResult(intent, RECHARGE_CODE);
        }
    }


    @Override
    public void sendCode(String returnCode, String returnMsg) {
        if (returnCode.equals("0000")) {
            showDialog("短信验证码获取成功");
            //发送成功
            countDownTimer.start();
        } else {
            showDialog(returnMsg);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        //卡券选择
        else if (requestCode == QUAN_LIST_REQUEST_CODE || requestCode == BAO_LIST_REQUEST_CODE) {
            if (resultCode == QUAN_LIST_RESULT_CODE) {
                if (data != null) {
                    Bundle bundle = data.getBundleExtra("quanBundle");
                    if (bundle != null) {
                        this.transferDetailPlay = bundle.getParcelable("playQuan");
                        defaultCheckQuan();
                    }
                }

            }
            //红包
            else if (resultCode == BAO_LIST_RESULT_CODE) {
                if (data != null) {
                    Bundle bundle = data.getBundleExtra("baoBundle");
                    if (bundle != null) {
                        this.transferDetailPlay = bundle.getParcelable("playBao");
                        defaultCheckBao();
                    }


                }
            }
        } else if (requestCode == RECHARGE_CODE && resultCode != RESULT_OK) {
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
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.choose_quan_layout:
                Intent intent = new Intent(TransferDetailPlayActivity.this, ChooseQuanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("playQuan", transferDetailPlay);
                intent.putExtra("transferQuanBundle", bundle);
                startActivityForResult(intent, QUAN_LIST_REQUEST_CODE);
                break;

            case R.id.choose_bao_layout:
                Intent baoIntent = new Intent(TransferDetailPlayActivity.this, ChooseBaoActivity.class);
                Bundle baoBundle = new Bundle();
                baoBundle.putParcelable("playBao", transferDetailPlay);
                baoIntent.putExtra("transferBaoBundle", baoBundle);
                startActivityForResult(baoIntent, BAO_LIST_REQUEST_CODE);
                break;


            case R.id.play:
                    List<CardVoucherBean> cardVoucherList = transferDetailPlay.getCardVoucherList();
                    //红包集合
                    List<RadEnvelopeBean> radEnvelopeList = transferDetailPlay.getRadEnvelopeList();

                    PlayBaoInfo playBaoInfo = transferDetailPlay.getPlayBaoInfo();

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
                                    //有红包卡券，但不使用
                                    onPlay();
                                }
                            });
                            builder.create().show();
                        } else {
                            //已经选择红包卡券
                            onPlay();
                        }

                    } else {
                        //没有红包卡券
                        onPlay();
                    }





                break;
            //短信验证码
            case R.id.sms_button:
                presenter.sendSms(transferDetailPlay.getInAmountWithBalance());
                break;

            default:
                break;

        }
    }


    //转让支付
    private void onPlay(){
        play.setEnabled(false);
        if (Utils.isViewEmpty(sms_ed) && sms_layout.getVisibility() == View.VISIBLE) {
            showDialog("请输入短信验证码");
        }
        else {
            transferDetailPlay.setCheckCode(sms_ed.getText().toString());
            final PlayWindow play = new PlayWindow(TransferDetailPlayActivity.this);
            play.showAtLocation(findViewById(R.id.play));
            play.setPayListener(new PlayWindow.OnPayListener() {
                @Override
                public void onSurePay(String password) {
                    transferDetailPlay.setPlayPassWord(EncDecUtil.AESEncrypt(password));
                    if (!paraValue.equals("00")) {
                        initCaptcha();
                    } else {
                        showImageCode();
                    }
                }
            });
        }






    }


    @Override
    public void initCaptcha(String paraValue, String senseFlag) {
        //paraValue 01   LvmTGN2CDYZQI32ye6235w==
        this.paraValue = paraValue;
        this.senseFlag = senseFlag;
    }


    //网易云盾
    public void initCaptcha() {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showImageCode();
            return;
        }
        CustomCaptchaUtils captchaUtils = new CustomCaptchaUtils(this);
        captchaUtils.setCaptchaId(paraValue);
        captchaUtils.setSenseFlag(senseFlag);
        captchaUtils.setCaptchaListener(new CustomCaptchaUtils.CaptListener() {
            @Override
            public void onSuccess(String token) {
                CCLog.e("网易盾验证成功---->");
                showLoading();
                transferDetailPlay.setToken(token);
                presenter.playTransferDetail(transferDetailPlay);
            }

            @Override
            public void onError(String error) {
                play.setEnabled(true);
                CCLog.e("网易盾加载出错---->" + error);
//                count++;
//                if (count < 3) {
//                    initCaptcha();
//                } else {
//                    showImageCode();
//                }
            }

            @Override
            public void onCancel() {
                CCLog.e("网易盾取消---->");
                play.setEnabled(true);
            }

            @Override
            public void onClose() {
                CCLog.e("网易盾关闭---->");
                play.setEnabled(true);
            }
        });
        captchaUtils.start();
    }

    /**
     * 计算总金额-红包金额
     */
    private void calculatePlayMoney() {
        PlayBaoInfo playBaoInfo = transferDetailPlay.getPlayBaoInfo();

        //总支付金额
        String basePlay = transferDetailPlay.getTotalAmount();
        //计算总支付金额-红包-卡券 = 剩余支付金额
        String remainPlayMoney = PlayBaoInfo.calculate(playBaoInfo, basePlay);
        //剩下支付金额
        total_payAmount.setText(remainPlayMoney);


        //需要充值
        if (!transferDetailPlay.getInAmountWithBalance().equals("0.00")) {
            //剩余支付金额与余额比较
            boolean isBankPlay = PlayBaoInfo.moneyCompare(transferDetailPlay.getBalanceY(), remainPlayMoney);
            //不需要银行卡支付
            if (isBankPlay) {
                play_type.setVisibility(View.GONE);
                bank_play_layout.setVisibility(View.GONE);
                //余额支付
                balance.setText(remainPlayMoney + "元");
            }
            //需要银行卡支付
            else {
                boolean isBalance = PlayBaoInfo.moneyComp(transferDetailPlay.getBalanceY(), "0.00");
                //是否有可用余额
                if (isBalance) {
                    play_type.setVisibility(View.VISIBLE);
                } else {
                    play_type.setVisibility(View.GONE);
                }

                bank_play_layout.setVisibility(View.VISIBLE);
                //要充值金额
                String bankPlayMoney = remainPlayMoney;

                //当选中余额支付
                if (play_type.isChecked()) {
                    //要充值金额
                    bankPlayMoney = PlayBaoInfo.moneySub(remainPlayMoney, transferDetailPlay.getBalanceY());
                    bank_play_tv.setText(MoneyUtil.fmtMicrometer(bankPlayMoney));
                    //当前余额支付金额
                    balance.setText(transferDetailPlay.getBalanceY() + "元");
                }
                //未选中余额支付
                else {
                    bank_play_tv.setText(MoneyUtil.fmtMicrometer(bankPlayMoney));
                    balance.setText("0.00" + "元");
                }

            }

        } else {
            play_type.setVisibility(View.GONE);
            balance.setText(remainPlayMoney + "元");
        }

    }


    //默认选中卡券
    private void defaultCheckQuan() {
        PlayBaoInfo playBaoInfo = transferDetailPlay.getPlayBaoInfo();
        HashMap<String, Object> playMap = playBaoInfo.getPlayMap();

        if (playMap != null && !playMap.isEmpty()) {
            //外部设置值
            String type = (String) playMap.get("type");
            String value = (String) playMap.get("value");
            //"A":加息券,"L":抵用券,"F":满减券
            switch (type) {
                case "A":
                    String percent = MoneyUtil.formatMoney(value);
                    String kqAddRatebj = (String) playMap.get("kqAddRatebj");
                    transferDetailPlay.getPlayBaoInfo().setKqAddRatebj(kqAddRatebj);
                    quan_name.setText("已选 加息券" + MoneyUtil.moneyMul(percent, "100") + "%");
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
        PlayBaoInfo playBaoInfo = transferDetailPlay.getPlayBaoInfo();
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


    //验证码弹出框
    private void showImageCode() {
        ImageCodeDialog.Builder builder = new ImageCodeDialog.Builder();
        builder.setContext(TransferDetailPlayActivity.this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                play.setEnabled(true);
            }
        });

        builder.setCodeCallback("确认", new ImageCodeDialog.ImageCodeCallback() {
            @Override
            public void imageCodeDate(DialogInterface dialog, String imageCode) {
                dialog.dismiss();
                if (Utils.isStringEmpty(imageCode)) {
                    showDialog("图形验证码不能为空");
                } else {
                    showLoading();
                    transferDetailPlay.setAuthCode(imageCode);
                    presenter.playTransferDetail(transferDetailPlay);
                }


            }
        });
        builder.create().show();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("playBundle");
        transferDetailPlay = bundle.getParcelable("playBean");
        productName.setText(transferDetailPlay.getProductName());
        targetRate.setText(transferDetailPlay.getTargetRate() + "%");
        expectedMaxAnnualRate.setText(transferDetailPlay.getExpectedMaxAnnualRate() + "%");
        delegateNum.setText(transferDetailPlay.getDelegateNum() + "元");
        leftDays.setText(transferDetailPlay.getLeftDays() + "天");
        presenter.init(transferDetailPlay.getProductCode(), transferDetailPlay.getPlayAmount(), transferDetailPlay.getSerialNoStr(), transferDetailPlay.getDelegationCode());
    }

    @Override
    public void initView() {
        setTitle("受让支付");
        productName = findViewById(R.id.productName);
        targetRate = findViewById(R.id.targetRate);
        expectedMaxAnnualRate = findViewById(R.id.expectedMaxAnnualRate);
        delegateNum = findViewById(R.id.delegateNum);
        leftDays = findViewById(R.id.leftDays);
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
        play = findViewById(R.id.play);
        play.setOnClickListener(this);
        bank_name = findViewById(R.id.bank_name);
        choose_bao_layout = findViewById(R.id.choose_bao_layout);
        choose_quan_layout = findViewById(R.id.choose_quan_layout);
        sms_layout = findViewById(R.id.sms_layout);
        sms_button = findViewById(R.id.sms_button);
        sms_button.setOnClickListener(this);
        sms_ed = findViewById(R.id.sms_ed);
        countDownTimer = new CustomCountDownTimer(60000, 1000, sms_button);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_detail_play;
    }

    @Override
    protected TransferDetailPlayPresenter createPresenter() {
        return new TransferDetailPlayPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.play_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        //此处是防止用户手速过快，导致多次点击按钮弹起支付键盘框，一个支付流程只能弹出一次交易密码框
        play.setEnabled(true);
    }
}
