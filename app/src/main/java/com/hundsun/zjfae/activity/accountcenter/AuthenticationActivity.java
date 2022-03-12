package com.hundsun.zjfae.activity.accountcenter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.presenter.AuthenticationPresenter;
import com.hundsun.zjfae.activity.accountcenter.view.AuthenticationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;
import com.hundsun.zjfae.common.view.dialog.AuthenticationDialog;

import java.util.List;

import onight.zjfae.afront.gens.LoadMySecurityQuestionPB;
import onight.zjfae.afront.gens.VerifySecurityInfoPB;

//身份验证或者重置登录密码验证界面
public class AuthenticationActivity extends CommActivity implements View.OnClickListener, AuthenticationView {
    private CustomCountDownTimer countDownTimer;
    private TextView mTvVerificationCode;//验证码按钮
    private TextView mProblem;
    private EditText mAnswer;
    private LinearLayout mProblemLl, ll_problem_one;
    private EditText mIdEt;
    private EditText mLoginEt;
    private AuthenticationPresenter mPresenter;
    private EditText mVerificationCodeEt;
    private AuthenticationDialog.Builder builder;
    private String mProblemId = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new AuthenticationPresenter(this);
    }

    @Override
    public void initView() {
        if ("bind_phone".equals(getIntent().getStringExtra("type"))) {
            setTitle("身份验证");
        } else {
            setTitle("重置交易密码");
        }
        mTvVerificationCode = findViewById(R.id.tv_verification_code);
        mTvVerificationCode.setOnClickListener(this);
        countDownTimer = new CustomCountDownTimer(60000, 1000, mTvVerificationCode);
        findViewById(R.id.bt_next).setOnClickListener(this);
        mProblem = (TextView) findViewById(R.id.problem);
        ll_problem_one = findViewById(R.id.ll_problem_one);
        ll_problem_one.setOnClickListener(this);
        mAnswer = (EditText) findViewById(R.id.answer);
        mProblemLl = (LinearLayout) findViewById(R.id.ll_problem);
        mIdEt = (EditText) findViewById(R.id.et_id);
        mLoginEt = (EditText) findViewById(R.id.et_login);
        mVerificationCodeEt = (EditText) findViewById(R.id.et_verification_code);
        if ("99".equals(getIntent().getStringExtra("certificateType"))) {
            //99的话说明没有绑定身份证 所以身份证布局隐藏
            mIdEt.setVisibility(View.GONE);
        } else {
            mIdEt.setVisibility(View.VISIBLE);
        }
        if ("true".equals(getIntent().getStringExtra("isSecuritySet"))) {
            mProblemLl.setVisibility(View.VISIBLE);
            mPresenter.getProblem();
        }
        if ("reset_transaction".equals(getIntent().getStringExtra("type"))) {
            mLoginEt.setVisibility(View.GONE);
        }
    }


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_authentication);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_problem_one:
                builder.create(mProblem).show();
                break;
            case R.id.tv_verification_code:
                if (mProblemLl.getVisibility() == View.VISIBLE && Utils.isViewEmpty(mAnswer)) {
                    showDialog("请输入答案");
                    return;
                }
                if (mIdEt.getVisibility() == View.VISIBLE && Utils.isViewEmpty(mIdEt)) {
                    showDialog("请输入身份证号");
                    return;
                }
                if (mLoginEt.getVisibility() == View.VISIBLE && Utils.isViewEmpty(mLoginEt)) {
                    showDialog("请输入登录密码");
                    return;
                }
                mPresenter.verifySecurityInfo(mProblemId, mAnswer.getText().toString(), getIntent().getStringExtra("certificateType"), mIdEt.getText().toString(), EncDecUtil.AESEncrypt(mLoginEt.getText().toString()));
                break;
            case R.id.bt_next:
                if (mProblemLl.getVisibility() == View.VISIBLE && Utils.isViewEmpty(mAnswer)) {
                    showDialog("请输入答案");
                    return;
                }
                if (mIdEt.getVisibility() == View.VISIBLE && Utils.isViewEmpty(mIdEt)) {
                    showDialog("请输入身份证号");
                    return;
                }
                if (mLoginEt.getVisibility() == View.VISIBLE && Utils.isViewEmpty(mLoginEt)) {
                    showDialog("请输入登录密码");
                    return;
                }
                if (Utils.isViewEmpty(mVerificationCodeEt)) {
                    showDialog("请输入验证码");
                    return;
                }
                if ("reset_transaction".equals(getIntent().getStringExtra("type"))) {
                    mPresenter.passwordCheck(mProblemId, mAnswer.getText().toString(), mIdEt.getText().toString(), mVerificationCodeEt.getText().toString());
                } else {
                    mPresenter.check(mProblemId, mAnswer.getText().toString(), mIdEt.getText().toString(), EncDecUtil.AESEncrypt(mLoginEt.getText().toString()), mVerificationCodeEt.getText().toString());
                }
                break;
        }
    }

    @Override
    public void getProblem(List<LoadMySecurityQuestionPB.PBIFE_securityquestionmanage_loadMySecurityQuestion.TcSecurityQuestionAnswerList> list) {

        builder = new AuthenticationDialog.Builder(this, list);
        mProblemId = list.get(0).getId();
        mProblem.setText(list.get(0).getQuestion());
        builder.setItemClick(new AuthenticationDialog.Builder.ItemClick() {
            @Override
            public void onItemClick(String id) {
                mProblemId = id;
            }
        });
    }

    @Override
    public void verifySecurityInfo(VerifySecurityInfoPB.Ret_PBIFE_userbaseinfo_verifySecurityInfo data) {

        if (data.getReturnCode().equals("0000")) {
            VerifySecurityInfoPB.PBIFE_userbaseinfo_verifySecurityInfo securityInfo = data.getData();

            if (mProblemLl.getVisibility() == View.VISIBLE && securityInfo.getAnswerValid().equals("false")) {
                //输入的安保问题错误
                showDialog("输入的答案有误");
            } else if (mIdEt.getVisibility() == View.VISIBLE && securityInfo.getCardValid().equals("false")) {
                showDialog("输入的身份证号有误");
            } else if (mLoginEt.getVisibility() == View.VISIBLE && securityInfo.getPasswordValid().equals("false")) {
                showDialog("输入的登录密码错误");
            } else {
                if ("bind_phone".equals(getIntent().getStringExtra("type"))) {
                    mPresenter.getVerificationCode("B4");
                } else if ("reset_transaction".equals(getIntent().getStringExtra("type"))) {
                    mPresenter.getVerificationCode("E");
                }
            }
        } else {
            showDialog(data.getReturnMsg());
        }


    }

    @Override
    public void getVerificationCode(String code, String msg) {
        if ("0000".equals(code)) {
            countDownTimer.start();
        }
        showDialog(msg);
    }

    @Override
    public void check(String code, String msg) {
        if ("0000".equals(code)) {
            if (getIntent() != null) {
                if ("bind_phone".equals(getIntent().getStringExtra("type"))) {
                    Intent intent = new Intent(this, BindNewPhoneActivity.class);
                    startActivityForResult(intent, 1);
                } else if ("reset_transaction".equals(getIntent().getStringExtra("type"))) {
                    Intent intent = new Intent(this, ModifyPasswordActivity.class);
                    intent.putExtra("type", "reset_transaction");
                    startActivityForResult(intent, 2);
                }
            }
        } else {
            showDialog(msg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            finish();
        }
    }
}
