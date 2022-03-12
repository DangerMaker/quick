package com.hundsun.zjfae.activity.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.RiskAssessmentActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import java.util.Map;

import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;

public class RegisterStateActivity extends CommActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 0x1412;

    private static final int CASULA_CODE = 0x759;

    private static final int ADD_BANK_RESULT_CODE = 0x1889;
    private TextView state;

    private Button buttonStation;

    private ImageView progress_img;

    private String isRealInvestor;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }
    @Override
    public void initView() {
        setNoBack();
        setTitle("注册");
        state = findViewById(R.id.state);
        progress_img = findViewById(R.id.progress_img);
        buttonStation =  findViewById(R.id.add_bank_button);
        buttonStation.setOnClickListener(this);
        findViewById(R.id.casual_button).setOnClickListener(this);

        getUserInfo();
    }

    /**
     * 查询用户状态
     */
    private void getUserInfo() {
        presenter.addDisposable(presenter.getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>() {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userbaseinfoGetUserDetailInfo) {

                UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userbaseinfoGetUserDetailInfo.getData();
                String userType = userDetailInfo.getUserType();

                if (userType.equals("company")) {
                    notice("020");
                } else {
                    notice("000");
                }
            }
        });
    }

    /**
     * 000非合格投资者弹框
     * 004恭喜弹框
     * 010伪合格投资者
     */
    public void notice(String type) {
        Notices.REQ_PBAPP_notice.Builder notice = Notices.REQ_PBAPP_notice.newBuilder();
        notice.setType(type);

        Map<String, String> map = BasePresenter.getRequestMap();
        map.put("version", BasePresenter.twoVersion);
        String url = presenter.parseUrl(BasePresenter.AZJ, BasePresenter.PBAFT, BasePresenter.VAFTAZJ, ConstantName.Notice, map);

        presenter.addDisposable(presenter.apiServer.notice(url, BasePresenter.getBody(notice.build().toByteArray())), new BaseObserver<Notices.Ret_PBAPP_notice>(this) {
            @Override
            public void onSuccess(Notices.Ret_PBAPP_notice notice) {
                if (notice.getReturnCode().equals(ConstantCode.RETURN_CODE)) {
                    dealNotice(notice);
                } else {
                    showError(notice.getReturnMsg());
                }
            }
        });
    }

    public void dealNotice(Notices.Ret_PBAPP_notice retNotice) {
        userStatus = retNotice.getData().getNotice().getIsShow();

        if (userStatus.equals("2")) {
            progress_img.setImageResource(R.drawable.schedule3);
        } else {
            progress_img.setImageResource(R.drawable.schedule);
        }
    }

    @Override
    public void initData() {
        isRealInvestor = getIntent().getStringExtra("isRealInvestor");
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.guide_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_bank_button:
                switch (buttonStation.getText().toString()){
                    case "去绑定银行卡":
                        Intent intent = new Intent(this,AddBankActivity.class);
                        intent.putExtra("register",true);
                        startActivityForResult(intent,ADD_BANK_RESULT_CODE);
                        break;

                    //风评
                    case "认定合格投资者":
                        showUserLevelDialog("000",isRealInvestor,true);
//                        Intent riskIntent = new Intent(this, RiskAssessmentActivity.class);
//                        riskIntent.putExtra("guide",true);
//                        startActivityForResult(riskIntent,REQUEST_CODE);
                        break;
                    default:
                        break;
                }
                break;
            //随便逛逛
            case R.id.casual_button:
                HomeActivity.show(RegisterStateActivity.this, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT);
//                setResult(CASULA_CODE);
//                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return new BasePresenter(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        HomeActivity.show(RegisterStateActivity.this, HomeActivity.HomeFragmentType.HOME_FRAGMENT);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //绑卡成功回来
        if (userStatus.equals("2")) {
            if (requestCode == ADD_BANK_RESULT_CODE && resultCode == RESULT_OK ){
                state.setText("恭喜！银行卡绑定成功");
                buttonStation.setVisibility(View.INVISIBLE);
                //setTitle("绑卡成功");
                progress_img.setImageResource(R.drawable.bundsuccess3);
            }
        } else {
            if (requestCode == ADD_BANK_RESULT_CODE && resultCode == RESULT_OK ){
                state.setText("恭喜！银行卡绑定成功");
                buttonStation.setText("认定合格投资者");
                //setTitle("绑卡成功");
                progress_img.setImageResource(R.drawable.bundsuccess);
            } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
                //setTitle("合格投资者认证完成");
                state.setText("恭喜！合格投资者认证成功");
                buttonStation.setVisibility(View.INVISIBLE);
                progress_img.setImageResource(R.drawable.risksuccess);
            }
        }
    }
}
