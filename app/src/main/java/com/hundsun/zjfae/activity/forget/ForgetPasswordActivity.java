package com.hundsun.zjfae.activity.forget;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import okhttp3.ResponseBody;

//忘记密码
public class ForgetPasswordActivity extends CommActivity<ForgetPasswordPresenter> implements View.OnClickListener, ForgetPasswordView {

    private EditText mPhoneForget;
    private EditText mId;
    private EditText mNumberImage;
    private ImageView mCodeForget;
    private Button mNextBt;

    @Override
    protected ForgetPasswordPresenter createPresenter() {
        return new ForgetPasswordPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.forget_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("找回登录密码");
        mPhoneForget = (EditText) findViewById(R.id.forget_phone);
        mId = (EditText) findViewById(R.id.id);
        mNumberImage = (EditText) findViewById(R.id.image_number);
        mCodeForget = (ImageView) findViewById(R.id.forget_code);
        mNextBt = (Button) findViewById(R.id.bt_next);
        mCodeForget.setOnClickListener(this);
        mNextBt.setOnClickListener(this);
        initRegisterImageView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_code:
                initRegisterImageView();
                break;
            case R.id.bt_next:
                if (Utils.isViewEmpty(mPhoneForget)) {
                    showDialog("请输入手机号");
                    return;
                }
                if (!Utils.isPhone(mPhoneForget.getText().toString())) {
                    showDialog("请输入正确的手机号");
                    return;
                }
                if (Utils.isViewEmpty(mNumberImage)) {
                    showDialog("请输入图形验证码");
                    return;
                }
                presenter.checkUserName(mPhoneForget.getText().toString(), mId.getText().toString(), mNumberImage.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void imageCode(ResponseBody body) {
        try {
            ImageLoad.getImageLoad().LoadImage(ForgetPasswordActivity.this, body.bytes(), mCodeForget);
        } catch (Exception e) {
            CCLog.e("Io异常", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void refreshImageAuthCode(String msg) {
        showDialog(msg, "知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.imageAuthCode();
            }
        });
    }


    @Override
    public void checkUserName(String code, String msg) {
        if ("0000".equals(code)) {
            baseStartActivity(this, ForgetPasswordValidateCode.class);
            finish();
        } else {
            showDialog(msg);
        }
    }

    @Override
    public void sendCode(String code, String msg) {
    }

    @Override
    public void submitCode(String code, String msg) {

    }

    @Override
    public void setPassword(String code, String msg) {

    }

    public void initRegisterImageView() {
        presenter.imageAuthCode();
    }
}
