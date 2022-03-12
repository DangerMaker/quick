package com.hundsun.zjfae.activity.accountcenter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.presenter.BindNewPhonePresenter;
import com.hundsun.zjfae.activity.accountcenter.view.BindNewPhoneView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;

import java.io.File;

import okhttp3.ResponseBody;

//绑定新手机
public class BindNewPhoneActivity extends CommActivity implements View.OnClickListener, BindNewPhoneView {
    private CustomCountDownTimer countDownTimer;
    private TextView mTvVerificationCode;//验证码按钮
    private BindNewPhonePresenter mPresenter;
    private EditText mIdEt;
    private EditText mLoginEt;
    private ImageView mCodeRegister;
    private EditText mVerificationCodeEt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_new_phone;
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new BindNewPhonePresenter(this);
    }

    @Override
    public void initView() {
        setTitle("绑定新手机");
        mTvVerificationCode = findViewById(R.id.tv_verification_code);
        mTvVerificationCode.setOnClickListener(this);
        countDownTimer = new CustomCountDownTimer(60000, 1000, mTvVerificationCode);
        findViewById(R.id.bt_next).setOnClickListener(this);
        mIdEt = (EditText) findViewById(R.id.et_id);
        mLoginEt = (EditText) findViewById(R.id.et_login);
        mCodeRegister = (ImageView) findViewById(R.id.register_code);
        mCodeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initRegisterImageView();
            }
        });
        mVerificationCodeEt = (EditText) findViewById(R.id.et_verification_code);
        initRegisterImageView();
    }


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_bind_new_phone);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_verification_code:
                if (mIdEt.getText().toString().equals("")) {
                    showDialog("请输入新手机号");
                    return;
                }
                if (mLoginEt.getText().toString().equals("")) {
                    showDialog("请输入图形验证码");
                    return;
                }
                mPresenter.getVerificationCode(mIdEt.getText().toString(), mLoginEt.getText().toString());
                break;
            case R.id.bt_next:
                if (mIdEt.getText().toString().equals("")) {
                    showDialog("请输入新手机号");
                    return;
                }
                if (mLoginEt.getText().toString().equals("")) {
                    showDialog("请输入图形验证码");
                    return;
                }
                if (mVerificationCodeEt.getText().toString().equals("")) {
                    showDialog("请输入验证码");
                    return;
                }
                mPresenter.modifyUserMobile(mIdEt.getText().toString(), mVerificationCodeEt.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void imageCode(ResponseBody body) {
        try {
            // Glide.with(RegisterActivity.this).load("https://testapp.zjfae.com/ife/mzj/pbimg.do?platform=android&ReqTime=1535958788859&appVersion=1.6.49&fh=VIMGMZJ000000J00&p=and&tdsourcetag=s_pctim_aiomsg&type=4&userid=13400000001").into(register_code);
            ImageLoad.getImageLoad().LoadImage(BindNewPhoneActivity.this, body.bytes(), mCodeRegister);
        } catch (Exception e) {
            CCLog.e("Io异常", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void getVerificationCode(String code, String msg) {
        if ("0000".equals(code)) {
            showToast(msg);
            countDownTimer.start();
        }else{
            showDialog(msg);
        }
    }

    @Override
    public void modifyUserMobile(String code, String msg) {
        showToast(msg);
        if ("0000".equals(code)) {
            //修改本地存储的手机号码
            UserInfoSharePre.setUserName(mIdEt.getText().toString());
            UserInfoSharePre.setMobile(mIdEt.getText().toString());
            setResult(RESULT_OK);
            finish();
        } else if ("2101".equals(code)) {
            initRegisterImageView();
        }
    }

    @Override
    public void refreshImageAuthCode(String msg) {
        showDialog(msg);
        initRegisterImageView();

    }

    public void initRegisterImageView() {
        File file = getExternalCacheDir();
        File imageFile = new File(file, System.currentTimeMillis() + ".jpg");
        mPresenter.imageAuthCode(imageFile);
    }
}
