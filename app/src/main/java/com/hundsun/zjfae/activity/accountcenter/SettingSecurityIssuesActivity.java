package com.hundsun.zjfae.activity.accountcenter;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.presenter.SettingSecurityIssuesPresenter;
import com.hundsun.zjfae.activity.accountcenter.view.SettingSecurityIssuesView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.SettingSecurityIssuesDialog;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.LoadSecurityQuestionPB;

public class SettingSecurityIssuesActivity extends CommActivity implements View.OnClickListener, SettingSecurityIssuesView {
    private TextView mTvProblemOne;
    private TextView mTvProblemTwo;
    private TextView mTvProblemThree;
    private SettingSecurityIssuesDialog.Builder builder;
    private SettingSecurityIssuesPresenter mPresenter;
    private EditText mOneAnswer;
    private EditText mTwoAnswer;
    private EditText mThreeAnswer;
    List<LoadSecurityQuestionPB.PBIFE_securityquestionmanage_loadSecurityQuestion.TcSecurityQuestionTemplateList> mList;
    private String id1 = "";
    private String id2 = "";
    private String id3 = "";
    private int position = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_security_issues;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_setting_security_issues);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new SettingSecurityIssuesPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("设置安保问题");
        mTvProblemOne = findViewById(R.id.tv_problem_one);
        mTvProblemTwo = findViewById(R.id.tv_problem_two);
        mTvProblemThree = findViewById(R.id.tv_problem_three);
        findViewById(R.id.ll_problem_one).setOnClickListener(this);
        findViewById(R.id.ll_problem_two).setOnClickListener(this);
        findViewById(R.id.ll_problem_three).setOnClickListener(this);
        findViewById(R.id.bt_next).setOnClickListener(this);
        mPresenter.loadQuestion();
        mOneAnswer = findViewById(R.id.answer_one);
        mTwoAnswer = findViewById(R.id.answer_two);
        mThreeAnswer = findViewById(R.id.answer_three);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_problem_one:
                position = 1;
                builder.create(mTvProblemOne, 1).show();
                break;
            case R.id.ll_problem_two:
                position = 2;
                builder.create(mTvProblemTwo, 2).show();
                break;
            case R.id.ll_problem_three:
                position = 3;
                builder.create(mTvProblemThree, 3).show();
                break;
            case R.id.bt_next:// TODO 18/10/25
                if (isProblemExist(false)) {
                    showDialog("不能选择重复问题");
                    return;
                }
                if (mTvProblemOne.getText().toString().isEmpty() || mTvProblemTwo.getText().toString().isEmpty() || mTvProblemThree.getText().toString().isEmpty()) {
                    showDialog("有问题暂未设置");
                    return;
                }
                if (mOneAnswer.getText().toString().isEmpty() || mTwoAnswer.getText().toString().isEmpty() || mThreeAnswer.getText().toString().isEmpty()) {
                    showDialog("有问题未填写完整");
                    return;
                }
                mPresenter.submitQuestion(getQuestionId(mTvProblemOne.getText().toString()), mOneAnswer.getText().toString(), getQuestionId(mTvProblemTwo.getText().toString()), mTwoAnswer.getText().toString(), getQuestionId(mTvProblemThree.getText().toString()), mThreeAnswer.getText().toString());
                break;
        }
    }

    private String getQuestionId(String question) {
        for (int i = 0; i < mList.size(); i++) {
            if (question.equals(mList.get(i).getQuestion())) {
                return mList.get(i).getId();
            }
        }
        return "";
    }

    @Override
    public void loadQuestion(List<LoadSecurityQuestionPB.PBIFE_securityquestionmanage_loadSecurityQuestion.TcSecurityQuestionTemplateList> list) {
        mList = list;
        List<String> question = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            question.add(list.get(i).getQuestion());
        }
        builder = new SettingSecurityIssuesDialog.Builder(this, question);
        builder.setOnItemClick(new SettingSecurityIssuesDialog.onItemClick() {
            @Override
            public void onItemClick(int position, String problem) {
                switch (position) {
                    case 1:
                        id1 = problem;
                        break;
                    case 2:
                        id2 = problem;
                        break;
                    case 3:
                        id3 = problem;
                        break;
                    default:
                        break;

                }
                isProblemExist(true);
            }
        });
    }

    @Override
    public void submitQuestion(String code, String msg) {
        if ("0000".equals(code)) {
            Intent intent = new Intent(this, SecurityIssuesDisplayActivity.class);
            intent.putExtra("questionA", mTvProblemOne.getText().toString());
            intent.putExtra("answerA", mOneAnswer.getText().toString());
            intent.putExtra("questionB", mTvProblemTwo.getText().toString());
            intent.putExtra("answerB", mTwoAnswer.getText().toString());
            intent.putExtra("questionC", mTvProblemThree.getText().toString());
            intent.putExtra("answerC", mThreeAnswer.getText().toString());
            baseStartActivity(intent);
        } else {
            showDialog(msg);
        }
    }

    //判断当前是否有问题重复
    public Boolean isProblemExist(Boolean isShow) {
        if (!id1.equals("") && !id2.equals("") && !id3.equals("") && id1.equals(id2) && id2.equals(id3)) {
            if (isShow) {
                showDialog("1、2、3题重复");
            }
            return true;
        }
        if (!id1.equals("")) {
            if (!id2.equals("") && id1.equals(id2)) {
                if (isShow) {
                    showDialog("1、2题重复");
                }
                return true;
            }
            if (!id3.equals("") && id1.equals(id3)) {
                if (isShow) {
                    showDialog("1、3题重复");
                }
                return true;
            }
        }
        if (!id2.equals("")) {
            if (!id1.equals("") && id1.equals(id2)) {
                if (isShow) {
                    showDialog("1、2题重复");
                }
                return true;
            }
            if (!id3.equals("") && id2.equals(id3)) {
                if (isShow) {
                    showDialog("2、3题重复");
                }
                return true;
            }
        }
        if (!id3.equals("")) {
            if (!id1.equals("") && id3.equals(id1)) {
                if (isShow) {
                    showDialog("1、3题重复");
                }
                return true;
            }
            if (!id2.equals("") && id3.equals(id2)) {
                if (isShow) {
                    showDialog("2、3题重复");
                }
                return true;
            }
        }
        return false;
    }

}
