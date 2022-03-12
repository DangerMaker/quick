package com.hundsun.zjfae.activity.moneymanagement.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gens.PrdQueryInterestPayDetailsPb;


/**
 * 我的购买适配器
 */
public class PayInterestAdapter extends RecyclerView.Adapter<PayInterestAdapter.ViewHolder> {
    private List<PrdQueryInterestPayDetailsPb.PBIFE_prdquery_prdQueryInterestPayDetails.ProductCashAddInterestPay> mList;

    public PayInterestAdapter(List<PrdQueryInterestPayDetailsPb.PBIFE_prdquery_prdQueryInterestPayDetails.ProductCashAddInterestPay> list) {
        mList = list;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pay_interest, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.seqNo.setText(mList.get(position).getSeqNo());
        viewHolder.startDate.setText(mList.get(position).getStartDate());
        viewHolder.endDate.setText(mList.get(position).getEndDate());
        viewHolder.deadline.setText(mList.get(position).getDeadline());
        viewHolder.payDate.setText(mList.get(position).getPayDate());
        viewHolder.pripal.setText(mList.get(position).getPripal());
        if (!mList.get(position).getIncome().isEmpty()) {
            String content = "<font color=\"#333333\">" + mList.get(position).getIncome() + "</font>";
            if(!mList.get(position).getAddIncome().equals("0.00")){
                content = content+"<font color=\"#00A000\">" +"(+" +mList.get(position).getAddIncome()+")" + "</font>";
            }
            viewHolder.income.setText(Html.fromHtml(content));
        }
        if (!mList.get(position).getTotalAmount().isEmpty()) {
            String content = "<font color=\"#333333\">" + mList.get(position).getTotalAmount() + "</font>";
            if(!mList.get(position).getDelayIncome().equals("0.00")){
                content = content+"<font color=\"#00A000\">" +"(" +mList.get(position).getDelayIncome()+")" + "</font>";
            }
            viewHolder.totalAmount.setText(Html.fromHtml(content));
        }
        viewHolder.statusName.setText(mList.get(position).getStatusName());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView seqNo, startDate, endDate, deadline, payDate, pripal, income, totalAmount, statusName;

        public ViewHolder(View view) {
            super(view);
            SupportDisplay.resetAllChildViewParam((LinearLayout) view.findViewById(R.id.ll_item_my_subscription));
            seqNo = view.findViewById(R.id.seqNo);
            startDate = view.findViewById(R.id.startDate);
            endDate = view.findViewById(R.id.endDate);
            deadline = view.findViewById(R.id.deadline);
            payDate = view.findViewById(R.id.payDate);
            pripal = view.findViewById(R.id.pripal);
            income = view.findViewById(R.id.income);
            totalAmount = view.findViewById(R.id.totalAmount);
            statusName = view.findViewById(R.id.statusName);
        }
    }
}





















