package com.hundsun.zjfae.activity.moneymanagement.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gens.PrdQueryTcDelegationFinanceListPb;


/**
 * 我的委托适配器
 */
public class MyEntrustAdapter extends RecyclerView.Adapter<MyEntrustAdapter.ViewHolder> {

    private List<PrdQueryTcDelegationFinanceListPb.PBIFE_prdquery_prdQueryTcDelegationFinanceList.TcDelegationFinaceList> mList;

    public MyEntrustAdapter(List<PrdQueryTcDelegationFinanceListPb.PBIFE_prdquery_prdQueryTcDelegationFinanceList.TcDelegationFinaceList> list) {
        mList = list;
    }

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);

        void onCancleClick(String delegationCode);
    }


    private ItemClickCallBack clickCallBack;


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_entrust, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.mTitle.setText(mList.get(position).getProductName());
        viewHolder.mType.setText(mList.get(position).getDelegateTypeName());
        viewHolder.mState.setText(mList.get(position).getDelegateStatusName());
        viewHolder.mEntrustAmount.setText(mList.get(position).getDelegateNum()+"元");
        viewHolder.mDealAmount.setText(mList.get(position).getTransactionAmount()+"元");
        if(mList.get(position).getDelegateStatus().equals("DELEGATEING")){
            viewHolder.bt_sell.setVisibility(View.VISIBLE);
        }else{
            viewHolder.bt_sell.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.onItemClick(position);
                }

            }
        });

        //撤单按钮点击
        viewHolder.bt_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCallBack.onCancleClick(mList.get(position).getDelegationCode());
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
        TextView mTitle, mType, mState, mEntrustAmount, mDealAmount;
        Button bt_sell;

        public ViewHolder(View view) {
            super(view);
            SupportDisplay.resetAllChildViewParam((LinearLayout) view.findViewById(R.id.ll_item_my_entrust));
            mTitle = view.findViewById(R.id.tv_title);
            mType = view.findViewById(R.id.type);
            mState = view.findViewById(R.id.state);
            mEntrustAmount = view.findViewById(R.id.entrust_amount);
            mDealAmount = view.findViewById(R.id.deal_amount);
            bt_sell = view.findViewById(R.id.bt_sell);
        }
    }
}





















