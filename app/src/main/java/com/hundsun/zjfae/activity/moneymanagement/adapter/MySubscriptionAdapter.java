package com.hundsun.zjfae.activity.moneymanagement.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.MoneyUtil;

import java.util.List;

import onight.zjfae.afront.gens.PrdQuerySubscribeListPb;


/**
 * 我的购买适配器
 */
public class MySubscriptionAdapter extends RecyclerView.Adapter<MySubscriptionAdapter.ViewHolder> {
    private List<PrdQuerySubscribeListPb.PBIFE_prdquery_prdQuerySubscribeList.SubscribeList> mList;

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public void setCancel(Cancel cancel) {
        cancelClick = cancel;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);
    }

    public interface Cancel {
        void onCancel(int pos);
    }

    public interface OnKqClick {
        void onKqClcik(String msg);
    }

    public MySubscriptionAdapter(List<PrdQuerySubscribeListPb.PBIFE_prdquery_prdQuerySubscribeList.SubscribeList> list) {
        mList = list;
    }

    public void setOnKqClick(OnKqClick onKqClick) {
        this.onKqClick = onKqClick;
    }

    private Cancel cancelClick;
    private ItemClickCallBack clickCallBack;
    private OnKqClick onKqClick;


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_subscription, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.mTitle.setText(mList.get(position).getProductName());
        viewHolder.mAmount.setText(mList.get(position).getDelegateAmount());
        viewHolder.mState.setText(mList.get(position).getDelegateStatusName());
        viewHolder.mRate.setText(MoneyUtil.moneyMul(mList.get(position).getExpectedMaxAnnualRate(), "100.00") + "%");
        if ("1".equals(mList.get(position).getCancelFlag())) {
            viewHolder.mCancel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mCancel.setVisibility(View.INVISIBLE);
        }
        if (mList.get(position).getKqType().equals("A")) {
            viewHolder.tv_kqvalue.setText("(+" + mList.get(position).getKqValue() + "%)");
            viewHolder.lin_kq.setVisibility(View.VISIBLE);
            viewHolder.lin_kq1.setVisibility(View.INVISIBLE);
            viewHolder.lin_kq2.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.lin_kq.setVisibility(View.GONE);
            viewHolder.lin_kq1.setVisibility(View.GONE);
            viewHolder.lin_kq2.setVisibility(View.GONE);
        }
        viewHolder.lin_kq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onKqClick.onKqClcik("加息" + mList.get(position).getKqValue() + "%针对于" + mList.get(position).getKqAddRatebj() + "元本金");
            }
        });
        viewHolder.mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelClick.onCancel(position);
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.onItemClick(position);
                }

            }
        });
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
        TextView mTitle, mAmount, mState, mRate, tv_kqvalue;
        Button mCancel;
        LinearLayout lin_kq, lin_kq1, lin_kq2;

        public ViewHolder(View view) {
            super(view);
            SupportDisplay.resetAllChildViewParam((LinearLayout) view.findViewById(R.id.ll_item_my_subscription));
            mTitle = view.findViewById(R.id.tv_title);
            mAmount = view.findViewById(R.id.tv_subscribed_amount);
            mState = view.findViewById(R.id.tv_subscription_state);
            mRate = view.findViewById(R.id.rate_of_return);
            mCancel = view.findViewById(R.id.bt_cancel);

            lin_kq = view.findViewById(R.id.lin_kq);
            lin_kq1 = view.findViewById(R.id.lin_kq1);
            lin_kq2 = view.findViewById(R.id.lin_kq2);
            tv_kqvalue = view.findViewById(R.id.tv_kqvalue);
        }
    }
}





















