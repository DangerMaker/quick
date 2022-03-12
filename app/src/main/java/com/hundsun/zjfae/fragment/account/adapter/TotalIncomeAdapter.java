package com.hundsun.zjfae.fragment.account.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gens.QueryFundEarningsLog;

/**
 * @Description:总收益 Recyclerview列表适配器
 * @Author: zhoujianyu
 * @Time: 2018/9/12 10:02
 */
public class TotalIncomeAdapter extends RecyclerView.Adapter<TotalIncomeAdapter.ViewHolder> {
    private List<QueryFundEarningsLog.PBIFE_statistic_queryFundEarningsLog.FundEarningsLogList> mList;
    private Context mContext;

    public TotalIncomeAdapter(Context context, List<QueryFundEarningsLog.PBIFE_statistic_queryFundEarningsLog.FundEarningsLogList> list) {
        this.mList = list;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_total_income, viewGroup, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder mViewHolder, final int position) {
        mViewHolder.tv_title.setText(mList.get(position).getEarningsName());
        mViewHolder.tv_date.setText(mList.get(position).getTransDate());
        mViewHolder.tv_money.setText(mList.get(position).getAmount());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_layout;
        TextView tv_title, tv_date, tv_money;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_money = itemView.findViewById(R.id.tv_money);
            SupportDisplay.resetAllChildViewParam(item_layout);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

    }
}
