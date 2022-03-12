package com.hundsun.zjfae.activity.accountcenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import onight.zjfae.afront.gens.QueryRiskAssessmentQuestionPB;

/**
 * 选择责任人列表适配器
 */
public class RiskAssessmentProblemAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<QueryRiskAssessmentQuestionPB.PBIFE_riskassessment_queryRiskAssessmentQuestion.TcAssessmentT.TcAssessmentQList> mList;
    private Map<String, String> mAnswer = new HashMap<>();//选择的答案

    public RiskAssessmentProblemAdapter(Context mContext, List<QueryRiskAssessmentQuestionPB.PBIFE_riskassessment_queryRiskAssessmentQuestion.TcAssessmentT.TcAssessmentQList> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    private CommitClick mCommitClick;

    public interface CommitClick {
        void commit();
    }

    public void setCommitClick(CommitClick commitClick) {
        this.mCommitClick = commitClick;
    }

    @Override
    public int getGroupCount() {
        return mList.size() + 2;
    }

    public void update(Map<String, String> answer) {
        mAnswer.clear();
        mAnswer.putAll(answer);
        notifyDataSetChanged();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0 || groupPosition == mList.size() + 1) {
            return 0;
        }
        return mList.get(groupPosition - 1).getTcAssessmentAListList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getTcAssessmentAListList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder viewHolderparent;
        if (convertView == null) {
            viewHolderparent = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_risk_problem_group, null);
            viewHolderparent.tvTitle = (TextView) convertView.findViewById(R.id.tv_problem);
            viewHolderparent.tvT = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolderparent.btComplete = convertView.findViewById(R.id.bt_complete);
            convertView.setTag(viewHolderparent);
        } else {
            viewHolderparent = (ViewHolder) convertView.getTag();
        }
        if (groupPosition == 0) {
            viewHolderparent.tvTitle.setVisibility(View.GONE);
            viewHolderparent.tvT.setVisibility(View.VISIBLE);
            viewHolderparent.btComplete.setVisibility(View.GONE);
        } else if (groupPosition >= mList.size() + 1) {
            viewHolderparent.tvTitle.setVisibility(View.GONE);
            viewHolderparent.tvT.setVisibility(View.GONE);
            viewHolderparent.btComplete.setVisibility(View.VISIBLE);
        } else {
            viewHolderparent.tvTitle.setVisibility(View.VISIBLE);
            viewHolderparent.tvT.setVisibility(View.GONE);
            viewHolderparent.btComplete.setVisibility(View.GONE);
            viewHolderparent.tvTitle.setText((groupPosition) + "." + mList.get(groupPosition - 1).getQuestion());
        }
        viewHolderparent.btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommitClick.commit();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolderchild;
        if (convertView == null) {
            viewHolderchild = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_risk_problem_child, null);
            viewHolderchild.tvTitle = (TextView) convertView.findViewById(R.id.tv_answer);
            viewHolderchild.llAnswer = convertView.findViewById(R.id.ll_answer);
            viewHolderchild.ivAnswer = convertView.findViewById(R.id.iv_answer);
            convertView.setTag(viewHolderchild);
        } else {
            viewHolderchild = (ViewHolder) convertView.getTag();
        }
        if (groupPosition > 0 && groupPosition < mList.size() + 1) {
            if (mList.get(groupPosition - 1).getTcAssessmentAListList().get(childPosition).getAnswer().equals(mAnswer.get(mList.get(groupPosition - 1).getQuestion()))) {
                viewHolderchild.llAnswer.setBackgroundColor(mContext.getResources().getColor(R.color.list_divider_color));
                viewHolderchild.ivAnswer.setVisibility(View.VISIBLE);
            } else {
                viewHolderchild.llAnswer.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                viewHolderchild.ivAnswer.setVisibility(View.GONE);
            }
            viewHolderchild.tvTitle.setText(mList.get(groupPosition - 1).getTcAssessmentAListList().get(childPosition).getAnswer());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvT;
        Button btComplete;
        LinearLayout llAnswer;
        ImageView ivAnswer;
    }
}
