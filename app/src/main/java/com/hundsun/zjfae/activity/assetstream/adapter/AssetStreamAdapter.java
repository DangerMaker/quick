package com.hundsun.zjfae.activity.assetstream.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gensazj.FundAccountLogPB;

/**
 * @Description:资金流水 Recyclerview列表适配器
 * @Author: zhoujianyu
 * @Time: 2018/9/12 10:02
 */
public class AssetStreamAdapter extends RecyclerView.Adapter<AssetStreamAdapter.ViewHolder> {
    private List<FundAccountLogPB.PBIFE_fund_queryFundAccountLog.FundAccountLogList> mList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public AssetStreamAdapter(Context context, List<FundAccountLogPB.PBIFE_fund_queryFundAccountLog.FundAccountLogList> list) {
        this.mList = list;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_asset_stream, viewGroup, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder mViewHolder, final int position) {
        if (mList.get(position).getTransType() != null) {
            if (mList.get(position).getTransType().equals("01")) {
                mViewHolder.tv_transAmount.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            } else if (mList.get(position).getTransType().equals("00")) {
                mViewHolder.tv_transAmount.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            } else {
                mViewHolder.tv_transAmount.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }
        }
        mViewHolder.tv_bizNo.setText(mList.get(position).getBizNo());
        mViewHolder.tv_gmtCreate.setText(mList.get(position).getGmtCreate());
        mViewHolder.tv_transAmount.setText(mList.get(position).getTransAmount() + "元");
        mViewHolder.tv_subTransCodeName.setText(mList.get(position).getSubTransCodeName());
        mViewHolder.tv_postAmount.setText(mList.get(position).getPostAmount() + "元");
        mViewHolder.tv_memo.setText(mList.get(position).getMemo());

        mViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_layout;
        TextView tv_bizNo, tv_gmtCreate, tv_transAmount, tv_postAmount, tv_memo, tv_subTransCodeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_bizNo = itemView.findViewById(R.id.tv_bizNo);
            tv_gmtCreate = itemView.findViewById(R.id.tv_gmtCreate);
            tv_transAmount = itemView.findViewById(R.id.tv_transAmount);
            tv_postAmount = itemView.findViewById(R.id.tv_postAmount);
            tv_memo = itemView.findViewById(R.id.tv_memo);
            tv_subTransCodeName = itemView.findViewById(R.id.tv_subTransCodeName);
            SupportDisplay.resetAllChildViewParam(item_layout);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

    }
}
