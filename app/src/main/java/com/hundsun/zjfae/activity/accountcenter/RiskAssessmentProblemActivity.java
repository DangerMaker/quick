package com.hundsun.zjfae.activity.accountcenter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.adapter.RiskAssessmentProblemAdapter;
import com.hundsun.zjfae.activity.accountcenter.presenter.RiskAssessmentProblemPresenter;
import com.hundsun.zjfae.activity.accountcenter.view.RiskAssessmentProblemView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import onight.zjfae.afront.gens.QueryRiskAssessmentQuestionPB;

//风险评测答卷
public class RiskAssessmentProblemActivity extends CommActivity<RiskAssessmentProblemPresenter> implements RiskAssessmentProblemView {

    private ExpandableListView mElvProblem;//列表
    private RiskAssessmentProblemAdapter mRiskAssessmentProblemAdapter;
    // ExpandListView 列表状态 1展开 0关闭 该案例中设置为三级
    private int[] isExpand = new int[]{0, 0, 0};
    private Map<String, String> answer = new HashMap<>();//选择的答案  问题和答案
    private Map<String, String> mPoint = new HashMap<>();//选择的答案  问题和分数

    private static final int Risk_RESULT_CODE = 0x1888;

    private static final int Risk_requestCode = 0x778;
    @Override
    protected RiskAssessmentProblemPresenter createPresenter() {
        return new RiskAssessmentProblemPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_risk_assessment_problem);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override

    protected int getLayoutId() {
        return R.layout.activity_risk_assessment_problem;
    }

    @Override
    public void initView() {
        setTitle("风险评测");
        mTopDefineCancel = true;
        mElvProblem = findViewById(R.id.elv_problem);
        presenter.getProblemList();
    }

    @Override
    protected void topDefineCancel() {
        Intent intent = new Intent();
        intent.putExtra("addBankState", true);
        setResult(Risk_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void loadList(final List<QueryRiskAssessmentQuestionPB.PBIFE_riskassessment_queryRiskAssessmentQuestion.TcAssessmentT.TcAssessmentQList> list) {
        mRiskAssessmentProblemAdapter = new RiskAssessmentProblemAdapter(this, list);
        mRiskAssessmentProblemAdapter.setCommitClick(new RiskAssessmentProblemAdapter.CommitClick() {
            @Override
            public void commit() {
                if (answer.size() != list.size()) {
                    showDialog("请回答完所有问题，再提交");
                } else {
                    int point = 0;
                    for (String i : mPoint.values()) {
                        point = point + Integer.parseInt(i);
                    }
                    presenter.commit(point + "");
                }
            }
        });
        mElvProblem.setAdapter(mRiskAssessmentProblemAdapter);
        //默认展开
        int groupCount = mElvProblem.getCount();
        for (int i = 0; i < groupCount; i++) {
            mElvProblem.expandGroup(i);
        }
        setListViewHeightBasedOnChildren(mElvProblem);
        mElvProblem.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        mElvProblem.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                answer.put(list.get(i - 1).getQuestion(), list.get(i - 1).getTcAssessmentAListList().get(i1).getAnswer());
                mPoint.put(list.get(i - 1).getQuestion(), list.get(i - 1).getTcAssessmentAListList().get(i1).getPoint());
                mRiskAssessmentProblemAdapter.update(answer);
                mElvProblem.setSelectedGroup(i + 1);
                return true;
            }
        });
    }

    @Override
    public void commit(String code, String msg, String riskExpiredDate, String riskLevel) {
        if ("0000".equals(code)) {
            Intent intent = new Intent();
            intent.putExtra("riskLevel", riskLevel);
            intent.putExtra("riskExpiredDate", riskExpiredDate);
            intent.putExtra("isRiskTest", "true");
//            intent.setClass(this,RiskAssessmentStateActivity.class);
//            startActivityForResult(intent,Risk_requestCode);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            showToast(msg);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        setResult(RESULT_OK, data);
//        finish();
//    }

    //设置list高度
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
