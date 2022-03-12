package com.hundsun.zjfae.activity.accountcenter;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.presenter.SecurityIssuesDisplayPresenter;
import com.hundsun.zjfae.activity.accountcenter.view.SecurityIssuesDisplayView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

//安保问题展示
public class SecurityIssuesDisplayActivity extends CommActivity implements View.OnClickListener, SecurityIssuesDisplayView {

    private TextView mProblemOneTv;
    private TextView mOneAnswer;
    private TextView mProblemTwoTv;
    private TextView mTwoAnswer;
    private TextView mProblemThreeTv;
    private TextView mThreeAnswer;
    private SecurityIssuesDisplayPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_security_issues_display;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_security_issues_display);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new SecurityIssuesDisplayPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("设置安保问题");
        mProblemOneTv = (TextView) findViewById(R.id.tv_problem_one);
        mOneAnswer = (TextView) findViewById(R.id.answer_one);
        mProblemTwoTv = (TextView) findViewById(R.id.tv_problem_two);
        mTwoAnswer = (TextView) findViewById(R.id.answer_two);
        mProblemThreeTv = (TextView) findViewById(R.id.tv_problem_three);
        mThreeAnswer = (TextView) findViewById(R.id.answer_three);
        mProblemOneTv.setText(getIntent().getStringExtra("questionA"));
        mOneAnswer.setText(getIntent().getStringExtra("answerA"));
        mProblemTwoTv.setText(getIntent().getStringExtra("questionB"));
        mTwoAnswer.setText(getIntent().getStringExtra("answerB"));
        mProblemThreeTv.setText(getIntent().getStringExtra("questionC"));
        mThreeAnswer.setText(getIntent().getStringExtra("answerC"));
        findViewById(R.id.bt_next).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                mPresenter.setProblem();
                break;
            default:
                break;
        }
    }

    @Override
    public void setProblem(String code, String msg) {
        if ("0000".equals(code)) {
            Intent intent = new Intent(this, ModifySuccessActivity.class);
            intent.putExtra("type", "setProblem");
            baseStartActivity(intent);
            finish();
        } else {
            showDialog(msg);
        }
    }
}
