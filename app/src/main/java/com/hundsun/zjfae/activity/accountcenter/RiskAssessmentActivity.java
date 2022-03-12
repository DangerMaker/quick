package com.hundsun.zjfae.activity.accountcenter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.presenter.RiskAssessmentPresenter;
import com.hundsun.zjfae.activity.accountcenter.view.RiskAssessmentView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import onight.zjfae.afront.gens.v3.UserDetailInfo;

//风险评测
public class RiskAssessmentActivity extends CommActivity<RiskAssessmentPresenter> implements View.OnClickListener, RiskAssessmentView {

    private ImageView mRiskIv;
    private TextView mRiskDateTv;
    private Button mCommit;
    private TextView mTitle;

    //默认未风险测评
    private boolean riskAssessment = false;

    private boolean isGuide = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_risk_assessment;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_risk_assessment);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected RiskAssessmentPresenter createPresenter() {
        return  new RiskAssessmentPresenter(this);
    }

    @Override
    protected void topDefineCancel() {

        if (isGuide && riskAssessment){
            HomeActivity.show(this,HomeActivity.HomeFragmentType.HOME_FRAGMENT);
        }
        else  if (riskAssessment){
            setResult(RESULT_OK);
        }

        finish();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isGuide && riskAssessment){
            HomeActivity.show(this,HomeActivity.HomeFragmentType.HOME_FRAGMENT);
        }
        else  if (riskAssessment){
            setResult(RESULT_OK);
        }

        finish();

        return true;
    }

    @Override
    public void initView() {
        setTitle("风险评测");
        mCommit = findViewById(R.id.bt_test);
        mTitle = findViewById(R.id.tv_title);
        mCommit.setOnClickListener(this);
        findViewById(R.id.bt_return).setOnClickListener(this);
        mRiskIv = (ImageView) findViewById(R.id.iv_risk);
        mRiskDateTv = (TextView) findViewById(R.id.tv_risk_date);
    }

    @Override
    public void initData() {
        isGuide = getIntent().getBooleanExtra("guide",false);
        presenter.getUserDate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                riskAssessment = true;
                mCommit.setText("我要重测");
                mTitle.setVisibility(View.VISIBLE);
                mRiskDateTv.setVisibility(View.VISIBLE);
                mRiskDateTv.setText("有效截止时间：" + data.getStringExtra("riskExpiredDate"));
                String riskLevel = data.getStringExtra("riskLevel");
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
                    default:
                        break;
                }
            }
        }
        else if (requestCode == WEB_ACTIVITY_REQUEST_CODE){
            presenter.getUserDate();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_test:
                presenter.myInvitationClick();
                break;
            case R.id.bt_return:
                if (isGuide && riskAssessment){
                    HomeActivity.show(this,HomeActivity.HomeFragmentType.HOME_FRAGMENT);
                }
                else  if (riskAssessment){
                    setResult(RESULT_OK);
                }

                finish();

                break;
            default:
                break;
        }
    }


    @Override
    public void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        String riskLevel = userDetailInfo.getData().getRiskLevel();//风险测评等级
        String riskExpiredDate = userDetailInfo.getData().getRiskExpiredDate();//风险测评有效期
        String isRiskTest = userDetailInfo.getData().getIsRiskTest();//是否风评过
        if ("true".equals(isRiskTest)) {
            riskAssessment = true;
            mCommit.setText("我要重测");
            mTitle.setVisibility(View.VISIBLE);
            mRiskDateTv.setVisibility(View.VISIBLE);
            mRiskDateTv.setText("有效截止时间：" + riskExpiredDate);
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
                default:
                    break;
            }
        }
    }

    @Override
    public void myInvitation(String url, String isShare) {
        boolean isUrl = !TextUtils.isEmpty(url)&&(url.contains("https") || url.contains("http"));
        if (isUrl){
            ShareBean shareBean = new ShareBean();
            shareBean.setFuncUrl(url);
            shareBean.setIsShare(isShare);
            startWebActivity(shareBean);
        }
        else {
            Intent intent = new Intent(this, RiskAssessmentProblemActivity.class);
            startActivityForResult(intent, 100);
        }
    }

}
