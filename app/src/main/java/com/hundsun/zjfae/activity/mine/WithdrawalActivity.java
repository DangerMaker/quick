package com.hundsun.zjfae.activity.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banner.Banner;
import com.android.banner.BannerConfig;
import com.android.banner.listener.OnBannerListener;
import com.android.banner.loader.ImageLoader;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.WithdrawalBankAdapter;
import com.hundsun.zjfae.activity.mine.presenter.WithdrawalPresenter;
import com.hundsun.zjfae.activity.mine.view.WithdrawalView;
import com.hundsun.zjfae.common.adapter.OnItemClickListener;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.popwindow.PlayWindow;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.util.List;

import onight.zjfae.afront.gens.LoadWithDrawBankInfo;
import onight.zjfae.afront.gens.WithDraw;
import onight.zjfae.afront.gensazj.WithdrawalsCoupon;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.mine
 * @ClassName: WithdrawalActivity
 * @Description: 提现Activity
 * @Author: moran
 * @CreateDate: 2019/6/10 13:56
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/10 13:56
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class WithdrawalActivity extends CommActivity<WithdrawalPresenter> implements WithdrawalView, View.OnClickListener {

    private TextView with_drawer_money, money_number, bank_name, bank_id, remark, branchName, modification;
    private ImageView bank_icon;
    private EditText width_money;
    private PlayWindow play;
    private LinearLayout bank_state_layout;


    private static final int REQUESTCODE = 0x1520;

    private boolean isNeedSubbranch = false;

    private Banner banner;

    private WebView content_webView;

    private TextView kb_tv;

    private boolean isExistCoupon = false, isBao = false;

    private int beforeDot = 10;
    private int afterDot = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    public void initView() {
        setTitle("提现");
        with_drawer_money = findViewById(R.id.with_drawer_money);
        money_number = findViewById(R.id.money_number);
        bank_name = findViewById(R.id.bank_name);
        bank_id = findViewById(R.id.bank_id);
        bank_icon = findViewById(R.id.bank_icon);
        remark = findViewById(R.id.remark);
        width_money = findViewById(R.id.width_money);
        branchName = findViewById(R.id.branchName);
        bank_state_layout = findViewById(R.id.bank_state_layout);
        modification = findViewById(R.id.modification);
        modification.setOnClickListener(this);
        width_money.addTextChangedListener(new MoneyTextWatcher());
        findViewById(R.id.withdrawal_all_money).setOnClickListener(this);
        findViewById(R.id.withdrawal_button).setOnClickListener(this);
        banner = findViewById(R.id.banner);
        content_webView = findViewById(R.id.content_webView);
        kb_tv = findViewById(R.id.kb_tv);
    }

    @Override
    public void initData() {
        isExistCoupon = getIntent().getBooleanExtra("isExistCoupon", false);
        isBao = getIntent().getBooleanExtra("isBao", false);
        presenter.initData(UserInfoSharePre.getUserType());
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.withdrawal_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected WithdrawalPresenter createPresenter() {
        return new WithdrawalPresenter(this);
    }


    @Override
    public void onWithdrawalsCoupon(WithdrawalsCoupon.Ret_PBAPP_kqWithdrawals kqWithdrawals) {
        List<WithdrawalsCoupon.PBAPP_kqWithdrawals.RemindS> remindSList = kqWithdrawals.getData().getRemindSListList();


        if (!remindSList.isEmpty()) {
            String content = "";
            for (WithdrawalsCoupon.PBAPP_kqWithdrawals.RemindS remindS : remindSList) {
                final List<WithdrawalsCoupon.PBAPP_kqWithdrawals.Remind> list = remindS.getRemindListList();
                if (remindS.getType().equals("text")) {
                    //有可用红包
                    content_webView.setVisibility(View.VISIBLE);
                    kb_tv.setVisibility(View.VISIBLE);
                    for (WithdrawalsCoupon.PBAPP_kqWithdrawals.Remind remind : list) {
                        content = remind.getContent();
                    }

                } else if (remindS.getType().equals("icons")) {
                    if (!list.isEmpty()) {
                        banner.setImages(list);
                        banner.setImageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(Context context, Object mClass, ImageView imageView) {

                                if (mClass instanceof WithdrawalsCoupon.PBAPP_kqWithdrawals.Remind) {
                                    ImageLoad.getImageLoad().LoadImage(context, ((WithdrawalsCoupon.PBAPP_kqWithdrawals.Remind) mClass).getIconUrl(), imageView);
                                }

                            }
                        }).setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                ShareBean shareBean = new ShareBean();
                                shareBean.setFuncUrl(list.get(position).getChainUrl());
                                shareBean.setIsShare(list.get(position).getIsShare());
                                startWebActivity(shareBean);


                            }
                        }).setIndicatorGravity(BannerConfig.RIGHT).start();
                    } else {
                        banner.setVisibility(View.GONE);
                    }

                } else {
                    banner.setVisibility(View.GONE);
                }

            }
            if (isExistCoupon || isBao) {
                onRemindKB(content);
            } else {
                content_webView.setVisibility(View.GONE);
                kb_tv.setVisibility(View.GONE);
            }
        } else {
            banner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUserBankInfo(String bankName) {
        bank_name.setText(bankName);
    }

    @Override
    public void onCompanyUserBankInfo(String bankName) {
        branchName.setText(bankName);

    }

    @Override
    public void onWithDrawBankInfo(LoadWithDrawBankInfo.Ret_PBIFE_fund_loadWithDrawBankInfo loadWithDrawBankInfo) {
        with_drawer_money.setText(loadWithDrawBankInfo.getData().getBalanceQ());
        StringBuffer buffer = new StringBuffer("特别注意：");
        buffer.append(loadWithDrawBankInfo.getData().getRemark());
        remark.setText(buffer.toString());
        String userType = UserInfoSharePre.getUserType();

        if (userType.equals("personal")) {

            bank_id.setText(loadWithDrawBankInfo.getData().getBankCardNo());
            String bankIcon = presenter.downloadImage();
            ImageLoad.getImageLoad().LoadImage(this, bankIcon, bank_icon);
        } else {
            final List<LoadWithDrawBankInfo.PBIFE_fund_loadWithDrawBankInfo.FundCorrelateRecordList> fundCorrelateRecordLists = loadWithDrawBankInfo.getData().getFundCorrelateRecordListList();

            if (fundCorrelateRecordLists != null && !fundCorrelateRecordLists.isEmpty()) {

                bank_id.setText(fundCorrelateRecordLists.get(0).getBankAccount());
                bank_name.setText(fundCorrelateRecordLists.get(0).getBankAccount());

                presenter.onUserBankInfo(fundCorrelateRecordLists.get(0).getId());

                bank_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        View rootView = LayoutInflater.from(WithdrawalActivity.this).inflate(R.layout.dialog_withdrawal_layout, null);

                        final BottomSheetDialog bankDialog = new BottomSheetDialog(WithdrawalActivity.this);

                        RecyclerView recyclerView = rootView.findViewById(R.id.ry_withdrawal);

                        View iv_close = rootView.findViewById(R.id.iv_close);

                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                bankDialog.dismiss();
                            }
                        });

                        recyclerView.setLayoutManager(new LinearLayoutManager(WithdrawalActivity.this));


                        WithdrawalBankAdapter adapter = new WithdrawalBankAdapter(WithdrawalActivity.this, fundCorrelateRecordLists);

                        adapter.setOnItemClickListener(new OnItemClickListener<LoadWithDrawBankInfo.PBIFE_fund_loadWithDrawBankInfo.FundCorrelateRecordList>() {

                            @Override
                            public void onItemClickListener(View v, LoadWithDrawBankInfo.PBIFE_fund_loadWithDrawBankInfo.FundCorrelateRecordList fundCorrelateRecordList, int position) {
                                bankDialog.dismiss();
                                bank_id.setText(fundCorrelateRecordList.getBankAccount());
                                bank_name.setText(fundCorrelateRecordList.getBankAccount());
                                branchName.setText(fundCorrelateRecordList.getBankName());
                                presenter.onUserBankInfo(fundCorrelateRecordList.getId());
                            }
                        });

                        recyclerView.setAdapter(adapter);

                        bankDialog.setContentView(rootView);

                        bankDialog.show();

                    }
                });
            }
        }

        if (loadWithDrawBankInfo.getData().getIsNeedSubbranch().equals("1") && loadWithDrawBankInfo.getData().getBranchName().equals("") && userType.equals("personal")) {

            isNeedSubbranch = true;
            bank_state_layout.setVisibility(View.VISIBLE);
            showDialog("您所绑定的银行卡提现需选择支行信息", "确定", "放弃提现", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(WithdrawalActivity.this, LoadProvinceActivity.class);
                    intent.putExtra("bankCode", presenter.getBankCode());
                    intent.putExtra("branchNames", presenter.getBranchName());
                    intent.putExtra("branchNo", presenter.getBranchNo());
                    startActivityForResult(intent, REQUESTCODE);

                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        } else if (loadWithDrawBankInfo.getData().getIsNeedSubbranch().equals("1")) {
            isNeedSubbranch = false;
            bank_state_layout.setVisibility(View.VISIBLE);
            branchName.setText(loadWithDrawBankInfo.getData().getBranchName());
        }
    }


    @Override
    public void onWithDrawBean(WithDraw.Ret_PBIFE_fund_withDraw fundWithDraw) {
        String returnCode = fundWithDraw.getReturnCode();
        String returnMsg = fundWithDraw.getReturnMsg();
        if (returnCode.equals("0000")) {
            returnMsg = "提现申请成功，已提交银行处理";
            showDialog(returnMsg, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();

                }
            });
        } else {
            showDialog(returnMsg);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdrawal_all_money:
                String allMoney = with_drawer_money.getText().toString().replaceAll(",", "");
                int posDot = allMoney.indexOf(".");
                if ((posDot == -1 && allMoney.length() > beforeDot) || posDot > beforeDot) {
                    showDialog("金额超过单笔限额");
                    return;
                }
                width_money.setText(with_drawer_money.getText().toString());
                break;
            case R.id.withdrawal_button:
                if (Utils.isViewEmpty(width_money)) {
                    showDialog("提现金额不能为空");
                } else {
                    play = new PlayWindow(WithdrawalActivity.this);
                    play.showAtLocation(findViewById(R.id.withdrawal_layout));
                    play.setPayListener(new PlayWindow.OnPayListener() {
                        @Override
                        public void onSurePay(String password) {
                            presenter.withDrawal(width_money.getText().toString(), EncDecUtil.AESEncrypt(password));
                        }
                    });

                }
                break;
            //轻触修改
            case R.id.modification:
                Intent intent = new Intent(this, LoadProvinceActivity.class);
                intent.putExtra("bankCode", presenter.getBankCode());
                intent.putExtra("branchNames", presenter.getBranchName());
                intent.putExtra("branchNo", presenter.getBranchNo());
                startActivityForResult(intent, REQUESTCODE);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userType = UserInfoSharePre.getUserType();
        if (REQUESTCODE == requestCode && resultCode == RESULT_OK) {
            String bankNameData = data.getStringExtra("branchName");
            branchName.setText(bankNameData);
            presenter.setBranchName(bankNameData);
            String branchNo = data.getStringExtra("branchNo");
            presenter.setBranchNo(branchNo);
        } else {
            if (isNeedSubbranch) {
                showDialog("您所绑定的银行卡提现需选择支行信息", "确定", "放弃提现", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(WithdrawalActivity.this, LoadProvinceActivity.class);
                        intent.putExtra("bankCode", presenter.getBankCode());
                        intent.putExtra("branchNames", presenter.getBranchName());
                        intent.putExtra("branchNo", presenter.getBranchNo());
                        startActivityForResult(intent, REQUESTCODE);

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                });
            }


        }
    }

    private class MoneyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (s.length() !=0 ){
//                String money =  Utils.number2CNMontrayUnit(s.toString());
//                money_number.setText(money);
//            }
//            else {
//                money_number.setText("零元整");
//            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            judge(s);
            if (s.length() != 0) {
                String money = Utils.number2CNMontrayUnit(s.toString());
                money_number.setText(money);
            } else {
                money_number.setText("零元整");
            }

        }

        private void judge(Editable s) {
            String temp = s.toString().replaceAll(",", "");
            int posDot = temp.indexOf(".");
            //直接输入小数点的情况
            if (posDot == 0) {
                s.insert(0, ".");
                return;
            }
            //连续输入0
            if (temp.equals("00")) {
                s.delete(1, 2);
                return;
            }
            //输入08等类似情况
            if (temp.startsWith("0") && temp.length() > 1 && (posDot == -1 || posDot > 1)) {
                s.delete(0, 1);
                return;
            }
            //不包含小数点
            if (posDot < 0) {
                if (temp.length() > beforeDot) {
                    s.delete(beforeDot, beforeDot + 1);
                }
            } else {
                //包含小数点
                if (temp.length() - posDot - 1 > afterDot) {
                    s.delete(posDot + afterDot + 1, posDot + afterDot + 2);
                }
                //整数位
                if (posDot > beforeDot) {
                    s.delete(beforeDot, posDot);
                }
            }

//            StringBuilder sb = new StringBuilder(temp);
//            //千分位
//            if (posDot == -1 && temp.length() > 3) {
//                int length = temp.length();
//                for (int i = 0; i < length / 3; i++) {
//                    sb.insert(length - 3 * (i + 1), ",");
//                }
//            } else if (posDot > 3) {
//                for (int i = 0; i < posDot / 3; i++) {
//                    sb.insert(posDot - 3 * (i + 1), ",");
//                }
//            }
//            .setText(sb.toString());

        }
    }

    private void onRemindKB(String bodyHTML) {
        content_webView.setVisibility(View.VISIBLE);
        kb_tv.setVisibility(View.VISIBLE);
        CCLog.e("bodyHTML", bodyHTML);
        //该语句在设置后必加，不然没有任何效果
        content_webView.loadDataWithBaseURL(null, getHtmlData(bodyHTML), "text/html", "UTF-8", null);
        kb_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isExistCoupon) {
                    baseStartActivity(WithdrawalActivity.this, DiscountCouponActivity.class);
                } else if (isBao) {
                    baseStartActivity(WithdrawalActivity.this, EnvelopeActivity.class);
                }


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        content_webView.destroy();
    }
}
