package com.hundsun.zjfae.activity.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.CompanyOfflineRechargeActivity;
import com.hundsun.zjfae.activity.mine.OfflineRechargeActivity;
import com.hundsun.zjfae.activity.mine.RechargeActivity;
import com.hundsun.zjfae.activity.product.bean.CardVoucherBean;
import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean;
import com.hundsun.zjfae.activity.product.bean.RadEnvelopeBean;
import com.hundsun.zjfae.activity.product.presenter.ProductPlayPresenter;
import com.hundsun.zjfae.activity.product.view.ProductPlayView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.MoneyUtil;
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
import onight.zjfae.afront.gensazj.Dictionary;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.product
 * @ClassName: ProductPlayActivity
 * @Description: 产品购买界面
 * @Author: moran
 * @CreateDate: 2019/6/13 15:31
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/13 15:31
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class ProductPlayActivity extends CommActivity<ProductPlayPresenter> implements View.OnClickListener, ProductPlayView {


    private TextView tv_productName, tv_expectedMaxAnnualRate, tv_play_type,tv_isTransfer,tv_deadline, quan_number, bao_number, payAmount, total_payAmount, balanceY;


    private TextView quan_tv_info, quan_value, bao_value, bao_tv_info;
    public static final int QUAN_LIST_REQUEST_CODE = 0x7071;
    private static final int BAO_LIST_REQUEST_CODE = 0x7072;
    private static final int QUAN_LIST_RESULT_CODE = 0x758;
    private static final int BAO_LIST_RESULT_CODE = 0x759;

    private static final int PLAY_CODE = 0x789;


    private static final int RECHARGE_CODE = 0x790;


    //支付信息类
    private ProductPlayBean playInfo;

    private LinearLayout choose_quan_layout, choose_bao;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_code_play;
    }

    @Override
    protected ProductPlayPresenter createPresenter() {
        return new ProductPlayPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("交易支付");
        tv_productName = findViewById(R.id.productName);
        tv_expectedMaxAnnualRate = findViewById(R.id.expectedMaxAnnualRate);
        tv_play_type = findViewById(R.id.tv_play_type);
        tv_isTransfer = findViewById(R.id.tv_isTransfer);
        tv_deadline = findViewById(R.id.deadline);
        quan_number = findViewById(R.id.quan_number);
        bao_number = findViewById(R.id.bao_number);
        payAmount = findViewById(R.id.payAmount);
        total_payAmount = findViewById(R.id.total_payAmount);
        balanceY = findViewById(R.id.balanceY);
        quan_tv_info = findViewById(R.id.quan_tv_info);
        quan_value = findViewById(R.id.quan_value);
        bao_value = findViewById(R.id.bao_value);
        bao_tv_info = findViewById(R.id.bao_tv_info);
        findViewById(R.id.product_play).setOnClickListener(this);
        choose_bao = findViewById(R.id.choose_bao);
        choose_quan_layout = findViewById(R.id.choose_quan_layout);

    }


    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("product_bundle");
        playInfo = bundle.getParcelable("product_info");
        tv_expectedMaxAnnualRate.setText(playInfo.getExpectedMaxAnnualRate() + "%");
        tv_deadline.setText(playInfo.getDeadline() + "天");
        tv_productName.setText(playInfo.getProductName() + "");
        tv_play_type.setText(playInfo.getPayStyle());
        tv_isTransfer.setText(playInfo.getIsTransfer());
        presenter.init(playInfo.getProductCode(), playInfo.getPlayAmount(), playInfo.getSerialNoStr());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == QUAN_LIST_REQUEST_CODE || requestCode == BAO_LIST_REQUEST_CODE) {
            //卡券选择
            if (resultCode == QUAN_LIST_RESULT_CODE) {
                if (data != null) {
                    Bundle bundle = data.getBundleExtra("quanBundle");
                    if (bundle != null) {
                        playInfo = bundle.getParcelable("playQuan");
                        calculatePlayMoney();
                    }
                }
            } else if (resultCode == BAO_LIST_RESULT_CODE) {
                if (data != null) {
                    Bundle bundle = data.getBundleExtra("baoBundle");
                    if (bundle != null) {
                        playInfo = bundle.getParcelable("playBao");
                        calculatePlayMoney();

                    }
                }
            }


        } else if (requestCode == RECHARGE_CODE && resultCode == RESULT_OK) {
            presenter.rechargePlayAmount();

        }


    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.play_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.choose_quan_layout:
                Intent intent = new Intent(ProductPlayActivity.this, ChooseQuanActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("playQuan", playInfo);
                intent.putExtra("quanBundle", bundle);
                startActivityForResult(intent, QUAN_LIST_REQUEST_CODE);
                break;

            case R.id.choose_bao:
                Intent baoIntent = new Intent(ProductPlayActivity.this, ChooseBaoActivity.class);
                Bundle baoBundle = new Bundle();
                baoBundle.putParcelable("playBao", playInfo);
                baoIntent.putExtra("baoBundle", baoBundle);
                startActivityForResult(baoIntent, BAO_LIST_REQUEST_CODE);
                break;

            //购买
            case R.id.product_play:
                //余额
                String balanceAmount = playInfo.getBalanceY();
                //支付总额
                String totalPayAmount = playInfo.getTotalPayAmount();

                boolean isComp = PlayBaoInfo.moneyComp(totalPayAmount, balanceAmount);

                CCLog.e("isComp", isComp);

                if (PlayBaoInfo.moneyComp(totalPayAmount, balanceAmount)) {

                    String userType = UserInfoSharePre.getUserType();

                    if (userType.equals("personal")) {

                        if (presenter.getIsBondedCard().equals("true")) {

                            showDialog("您的账户余额不足，请先对账户进行充值！", "去充值", "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    presenter.queryBankInfo();
                                }
                            });

                        } else {
                            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(ProductPlayActivity.this, AddBankActivity.class);
                                    intent.putExtra("isPlay", true);

                                    baseStartActivity(intent);
                                }
                            });
                        }
                    }
                    //机构用户提示
                    else {

                        baseStartActivity(this, CompanyOfflineRechargeActivity.class);

                    }


                } else {


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
                                    isPlay();
                                }
                            });
                            builder.create().show();
                        } else {
                            isPlay();
                        }

                    } else {
                        isPlay();
                    }

                }

                break;
            default:
                break;
        }
    }


    private void isPlay() {
        final PlayWindow play = new PlayWindow(ProductPlayActivity.this);
        play.showAtLocation(findViewById(R.id.play_layout));
        play.setPayListener(new PlayWindow.OnPayListener() {
            @Override
            public void onSurePay(String password) {
                //支付
                playInfo.setPlayPassWord(password);
                presenter.playProduct(playInfo);
            }
        });
    }


    @Override
    public void onPlayInit(QueryPayInit.Ret_PBIFE_trade_queryPayInit payInit) {
        payAmount.setText(payInit.getData().getPayInitWrap().getPayAmount() + "元");
        balanceY.setText(payInit.getData().getPayInitWrap().getBalanceY());
        total_payAmount.setText(MoneyUtil.fmtMicrometer(payInit.getData().getPayInitWrap().getPayAmount()));
        playInfo.setBalanceY(payInit.getData().getPayInitWrap().getBalanceY());
        //playInfo.setPlayAmount(payInitBean.getData().getPayInitWrap().getPayAmount());
    }

    @Override
    public void onUserKquanInfo(QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan myKqQuan) {
        initCoupon(myKqQuan);
    }

    @Override
    public void playProduct(String returnCode, String returnMsg) {
        if (returnCode.equals("0000")) {
            Intent intent = new Intent(ProductPlayActivity.this, ProductPlayStateActivity.class);
            intent.putExtra("playState", "交易成功");
            startActivityForResult(intent, PLAY_CODE);
        }

        //交易密码错误
        else if (returnCode.equals("0012")) {

            showDialog(returnMsg);
        }
        //其他错误
        else {
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

    //购买余额不足
    @Override
    public void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
        if (fundBankInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            String totalPayAmount = total_payAmount.getText().toString().replace(",", "");
            String balance = balanceY.getText().toString();
            String payBanlance = MoneyUtil.moneySub(totalPayAmount, balance);
            intent.putExtra("payAmount", payBanlance);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", bankCard);
            intent.putExtra("type", "302");
            intent.putExtra("tag", "isProduct");
            startActivityForResult(intent, RECHARGE_CODE);
        } else {
            String totalPayAmount = total_payAmount.getText().toString().replace(",", "");
            String balance = balanceY.getText().toString();
            String payBanlance = MoneyUtil.moneySub(totalPayAmount, balance);
            Intent intent = new Intent(this, RechargeActivity.class);
            intent.putExtra("payAmount", payBanlance);
            intent.putExtra("tag", "isProduct");
            startActivityForResult(intent, RECHARGE_CODE);
        }
    }

    @Override
    public void onKQDescription(final Dictionary.Ret_PBAPP_dictionary dictionary) {

        findViewById(R.id.kq_description_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showDialog(dictionary.getData().getParms(0).getKeyCode());

                CardDictionaryDialog dialog = new CardDictionaryDialog(ProductPlayActivity.this);

                dialog.setContextStr(dictionary.getData().getParms(0).getKeyCode());

                dialog.createDialog().show();



            }
        });
    }

    @Override
    public void rechargePlayAmount(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit) {
        balanceY.setText(queryPayInit.getData().getPayInitWrap().getBalanceY());
        playInfo.setBalanceY(queryPayInit.getData().getPayInitWrap().getBalanceY());
    }


    //初始化卡券信息
    private void initCoupon(final QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan queryQuanInfo) {

        quan_tv_info.setText("");
        quan_value.setText("0.00元");

        bao_tv_info.setText("");
        bao_value.setText("0.00元");

        String voucherSize = queryQuanInfo.getData().getQuanSize();
        String cardSize = queryQuanInfo.getData().getBaoSize();
        quan_number.setText("(" + voucherSize + ")");
        bao_number.setText("(" + cardSize + ")");
        List<CardVoucherBean> cardVoucherList = new ArrayList<>();

        //卡券遍历
        if (!queryQuanInfo.getData().getQuanListList().isEmpty()) {
            for (QueryMyKqQuan.PBIFE_trade_queryMyKqQuan.QuanList quanList : queryQuanInfo.getData().getQuanListList()) {
                CardVoucherBean cardVoucherBean = new CardVoucherBean();
                cardVoucherBean.setQuanDetailsId(quanList.getQuanDetailsId());
                cardVoucherBean.setQuanType(quanList.getQuanType());
                cardVoucherBean.setQuanCatalogRemark(quanList.getQuanCatalogRemark());
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

        }
        playInfo.setVoucherSize(voucherSize);
        playInfo.setCardVoucherList(cardVoucherList);
        playInfo.setCardSize(cardSize);
        playInfo.setRadEnvelopeList(radEnvelopeList);

        //卡券
        if (!cardVoucherList.isEmpty()) {

            choose_quan_layout.setOnClickListener(this);

            for (CardVoucherBean cardVoucherBean : cardVoucherList) {

                if (cardVoucherBean.getMostFineFlag().equals("1")) {

                    defaultQuan(cardVoucherBean);

                    break;
                }

            }

        }
        //红包
        if (!radEnvelopeList.isEmpty()) {

            choose_bao.setOnClickListener(this);

            for (RadEnvelopeBean radEnvelopeBean : radEnvelopeList) {

                if (radEnvelopeBean.getMostFineFlag().equals("1")) {
                    defaultBao(radEnvelopeBean);
                }
            }
        }
    }

    final PlayBaoInfo playBaoInfo = new PlayBaoInfo();

    private void defaultQuan(CardVoucherBean cardVoucherBean) {

        HashMap<String, Object> playMap = new HashMap();


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
        playMap.put("kqAddRatebj", kqAddRatebj);

        playBaoInfo.setPlayMap(playMap);
        playInfo.setPlayBaoInfo(playBaoInfo);
        calculatePlayMoney();
    }

    private List<HashMap> playList = new ArrayList<>();

    private void defaultBao(RadEnvelopeBean radEnvelopeBean) {

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
        playMap.put("quanCanStack", quanCanStack);

        playList.add(playMap);
        playBaoInfo.setPlayList(playList);
        playInfo.setPlayBaoInfo(playBaoInfo);

        calculatePlayMoney();

    }


    //计算金额
    private void calculatePlayMoney() {

        //红包信息
        List<HashMap> playList = new ArrayList<>();
        //卡券
        HashMap<String, Object> playMap = new HashMap<>();

        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();


        if (playBaoInfo != null) {
            playList = playBaoInfo.getPlayList();
            playMap = playBaoInfo.getPlayMap();
        }


        if (playList == null) {

            if (!playInfo.getRadEnvelopeList().isEmpty()) {
                bao_tv_info.setText("未选择");
                bao_value.setText("0.00元");
            } else {
                bao_tv_info.setText("");
                bao_value.setText("0.00元");
            }

        } else {
            //未选择红包
            if (playList.isEmpty() && !playInfo.getRadEnvelopeList().isEmpty()) {
                bao_tv_info.setText("未选择");
                bao_value.setText("0.00元");
            } else if (!playList.isEmpty() && !playInfo.getRadEnvelopeList().isEmpty()) {
                bao_tv_info.setText(playList != null && !playList.isEmpty() ? "已选" + playList.size() + "个红包" : "未选择");
                String baoAmount = PlayBaoInfo.allBaoAmount(playBaoInfo.getPlayList());
                bao_value.setText("-" + baoAmount + "元");
            }
            //没有红包
            else if (playList.isEmpty() && playInfo.getRadEnvelopeList().isEmpty()) {
                bao_tv_info.setText("");
                bao_value.setText("0.00元");
            }

        }


        //卡券
        if (playMap == null) {

            if (!playInfo.getCardVoucherList().isEmpty()) {
                quan_tv_info.setText("未选择");
                quan_value.setText("0.00元");
            } else {
                quan_tv_info.setText("");
                quan_value.setText("0.00元");
            }

        } else {

            if (playMap.isEmpty() ) {
                quan_tv_info.setText("未选择");
                quan_value.setText("0.00元");
            } else {
                //金额
                String value = (String) playMap.get("value");
                //卡券类型
                String type = (String) playMap.get("type");

                String kqAddRatebj = (String) playMap.get("kqAddRatebj");

                //"A":加息券,"L":抵用券,"F":满减券
                switch (type) {
                    case "A":
                        double percentage = Double.parseDouble(value);
                        DecimalFormat df = new DecimalFormat("0.00");
                        String percent = df.format((percentage * 100));
                        playInfo.getPlayBaoInfo().setKqAddRatebj(kqAddRatebj);
                        quan_tv_info.setText("已选 加息券" + percent + "%");
                        quan_value.setText("0.00元");
                        break;
                    case "L":
                        quan_tv_info.setText("已选 抵价券" + value);
                        quan_value.setText("-" + value + "元");
                        break;
                    case "F":
                        quan_tv_info.setText("已选 满减券" + value + "元");
                        quan_value.setText("-" + value + "元");
                        break;
                    default:
                        break;
                }
            }
        }

        String totalPayAmount = playInfo.getPlayAmount();

        totalPayAmount = PlayBaoInfo.allAmount(playBaoInfo.getPlayMap(), playBaoInfo.getPlayList(), totalPayAmount);
        playInfo.setTotalPayAmount(totalPayAmount);
        total_payAmount.setText(MoneyUtil.fmtMicrometer(totalPayAmount));
    }

}
