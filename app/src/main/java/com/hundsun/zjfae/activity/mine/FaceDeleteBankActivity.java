package com.hundsun.zjfae.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.UnbindReasonAdapter;
import com.hundsun.zjfae.activity.mine.bean.ConfigurationUtils;
import com.hundsun.zjfae.activity.mine.presenter.FaceDeleteBankPresenter;
import com.hundsun.zjfae.activity.mine.view.FaceDeleteBankView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.view.dialog.SendSmsCodeDialog;
import com.hundsun.zjfae.common.view.popwindow.MaterialsUploadedPopupWindow;

import onight.zjfae.afront.gens.BankCardManageUnBindBankCard;

public class FaceDeleteBankActivity extends CommActivity<FaceDeleteBankPresenter> implements View.OnClickListener, FaceDeleteBankView {



    //是否需要短信验证码
    private boolean isNeedSms = false;

    private EditText other_ed;


    private UnbindReasonAdapter mUnbindReasonAdapter;
    private MaterialsUploadedPopupWindow popupWindow;

    private LinearLayout unbind_card_layout,change_layout;

    private Button affirm_bt;


    private TextView unbind_shade,delete_bank_remake;

    private String reasonDetails = "", cardReason ="cancel";


    private  SendSmsCodeDialog.Builder bankSmsDialog  ;

    private ConfigurationUtils faceConfiguration;

    /**
     * 短信验证码
     * */
    private String smsCode = "";

    @Override
    public void initData() {
        Bundle bundle = getIntent().getBundleExtra("faceBundle");
        CCLog.e("bundle",bundle.toString());
        if (bundle != null){
            faceConfiguration = bundle.getParcelable("face");
            if (faceConfiguration!= null){
                isNeedSms = faceConfiguration.isNeedSms();
                presenter.setPayChannelNo(faceConfiguration.getPayChannelNo());

                if (faceConfiguration.getVerifyscene().equals("unbindCard")){
                    affirm_bt.setVisibility(View.VISIBLE);
                    unbind_card_layout.setVisibility(View.VISIBLE);
                    delete_bank_remake.setVisibility(View.VISIBLE);
                }
                else {
                    change_layout.setVisibility(View.VISIBLE);
                }
            }

        }

    }

    @Override
    public void initView() {
        setTitle("成功");
        other_ed = findViewById(R.id.other_ed);
        affirm_bt = findViewById(R.id.affirm_bt);
        affirm_bt.setOnClickListener(this);
        unbind_shade = findViewById(R.id.unbind_shade);
        unbind_shade.setOnClickListener(this);
        change_layout = findViewById(R.id.change_layout);
        unbind_card_layout = findViewById(R.id.unbind_card_layout);
        findViewById(R.id.change_button).setOnClickListener(this);
        findViewById(R.id.clean_button).setOnClickListener(this);
        initDeleteBankInfo();
        bankSmsDialog  = new SendSmsCodeDialog.Builder(this);
        delete_bank_remake = findViewById(R.id.delete_bank_remake);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.face_delete_bank_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_delete_bank;
    }

    @Override
    protected FaceDeleteBankPresenter createPresenter() {

        return new FaceDeleteBankPresenter(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            //解绑原因
            case R.id.unbind_shade:
                popupWindow.showAsDropDown(unbind_shade);
                break;

            //确认解绑
            case R.id.affirm_bt:
                //需要短信验证码
                reasonDetails = other_ed.getText().toString();


                if (cardReason.equals("other")){

                    if (Utils.isViewEmpty(other_ed)){

                        showDialog("请输入您的解绑原因");
                        return;
                    }
                }

                if (isNeedSms){

                    bankSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    bankSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                        @Override
                        public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                            if (Utils.isStringEmpty(smsCode)){
                                showDialog("请输入短信验证码");
                            }
                            else {
                                dialog.dismiss();
                                FaceDeleteBankActivity.this.smsCode = smsCode;
                                presenter.deleteBankCard(smsCode,reasonDetails,cardReason,faceConfiguration.getTencentfaceOrder());

                            }

                        }

                        @Override
                        public void onSmsClick() {

                            presenter.unBindBankCardSMS();

                        }
                    });

                    bankSmsDialog.show();
                }
                else {
                    FaceDeleteBankActivity.this.smsCode = smsCode;
                    presenter.deleteBankCard("",reasonDetails,cardReason,faceConfiguration.getTencentfaceOrder());

                }
                break;

            //换卡操作
            case R.id.change_button:
                Intent intent = new Intent(this,ChangeCardActivity.class);
                Bundle bundle = new Bundle();
                //人脸识别成功，换卡businessType = "true";
                faceConfiguration.setBusinessType("true");
                bundle.putParcelable("face",faceConfiguration);
                intent.putExtra("faceBundle",bundle);
                baseStartActivity(intent);
                finish();

                break;

            case R.id.clean_button:
                HomeActivity.show(this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                break;

                default:
                    break;

        }

    }



    private void initDeleteBankInfo(){
        mUnbindReasonAdapter = new UnbindReasonAdapter(this);
        popupWindow = new MaterialsUploadedPopupWindow(this);
        final RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUnbindReasonAdapter.setItemOnClickListener(new UnbindReasonAdapter.ItemOnClickListener() {
            @Override
            public void onItemClick(String name, String shade) {
                cardReason = name;


                unbind_shade.setText(shade);

                if (shade.equals("其他") && name .equals("other")){

                    other_ed.setVisibility(View.VISIBLE);
                }
                else {
                    other_ed.setVisibility(View.GONE);
                }

                popupWindow.dismiss();

            }
        });

        recyclerView.setAdapter(mUnbindReasonAdapter);
        popupWindow.setCreateView(new MaterialsUploadedPopupWindow.CreateView() {
            @Override
            public View contentView() {
                return recyclerView;
            }
        });
    }

    @Override
    public void onDeleteBankCard(BankCardManageUnBindBankCard.Ret_PBIFE_bankcardmanage_unbindBankCard deleteBankCard) {


        String returnCode = deleteBankCard.getReturnCode();
        String returnMsg = deleteBankCard.getReturnMsg();

        if (returnCode.equals("0000")){
            bankSmsDialog.setSmsButtonState(false);
            baseStartActivity(FaceDeleteBankActivity.this,DeleteBankStataActivity.class);
            finish();

        }
    }

    @Override
    public void onDeleteBankError(String returnCode, String returnMsg) {
        if (returnCode.equals(ConstantCode.ADD_BANK_TIME_OUT)){
            showDialog(returnMsg, "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    HomeActivity.show(FaceDeleteBankActivity.this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                    finish();
                }
            });
        }

    }

    @Override
    public void onDeleteBankSms() {
        bankSmsDialog.setSmsButtonState(true);
    }

    @Override
    public void showErrorDialog(String msg) {
        if (!smsCode.equals("")){
            bankSmsDialog.show();
        }
        super.showErrorDialog(msg);
    }
}
