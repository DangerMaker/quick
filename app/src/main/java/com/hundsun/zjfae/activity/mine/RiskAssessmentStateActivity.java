package com.hundsun.zjfae.activity.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class RiskAssessmentStateActivity extends BasicsActivity implements View.OnClickListener {

    private ImageView mRiskIv;
    private TextView mRiskDateTv;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_risk_assessment_state;
    }

    @Override
    public void initData() {
        mRiskDateTv.setText("有效截止时间：" + getIntent().getStringExtra("riskExpiredDate"));
        String riskLevel = getIntent().getStringExtra("riskLevel");
        if (riskLevel == null) {
            riskLevel = "0";
        }
        switch (riskLevel) {
            case "1":
                mRiskIv.setImageResource(R.drawable.riskone);
                break;
            case "2":
                mRiskIv.setImageResource(R.drawable.risktwo);
                break;
            case "3":
                mRiskIv.setImageResource(R.drawable.riskthree);
                break;
            case "4":
                mRiskIv.setImageResource(R.drawable.riskfour);
                break;
            case "5":
                mRiskIv.setImageResource(R.drawable.riskfive);
                break;
        }

    }

    @Override
    public void initView() {
        setNoBack();
        setTitle("风险承受能力评估结果");
        mRiskIv = (ImageView) findViewById(R.id.iv_risk);
        mRiskDateTv = (TextView) findViewById(R.id.tv_risk_date);
        findViewById(R.id.finish).setOnClickListener(this);

    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_risk_assessment_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finish:
                setResult(RESULT_OK, getIntent());
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
