package com.hundsun.zjfae.activity.moneymanagement.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gens.PrdQueryTaUnitFlowPb;


/**
 * 持仓流水适配器
 */
public class HoldingWaterAdapter extends RecyclerView.Adapter<HoldingWaterAdapter.ViewHolder> {
    private List<PrdQueryTaUnitFlowPb.PBIFE_prdquery_prdQueryTaUnitFlow.TaUnitFlow> mList;

    public HoldingWaterAdapter(List<PrdQueryTaUnitFlowPb.PBIFE_prdquery_prdQueryTaUnitFlow.TaUnitFlow> list) {
        mList = list;
    }

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);
    }


    private ItemClickCallBack clickCallBack;


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_holding_water, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.mTitle.setText(mList.get(position).getProductName());
        viewHolder.mTime.setText(mList.get(position).getGmtCreate());
        viewHolder.mNumber.setText(mList.get(position).getTransferProductCode());
        viewHolder.mType.setText(mList.get(position).getTransferTypeName());
        viewHolder.mHold.setText(mList.get(position).getTransferUnit());
        viewHolder.mVolumeTransactions.setText(mList.get(position).getTransferPostUnit());
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
        TextView mTitle, mTime, mNumber, mType, mHold, mVolumeTransactions;

        public ViewHolder(View view) {
            super(view);
            SupportDisplay.resetAllChildViewParam((LinearLayout) view.findViewById(R.id.ll_item_holding_water));
            mTitle = view.findViewById(R.id.title);
            mTime = view.findViewById(R.id.time);
            mNumber = view.findViewById(R.id.number);
            mType = view.findViewById(R.id.type);
            mHold = view.findViewById(R.id.volume_transactions);
            mVolumeTransactions = view.findViewById(R.id.hold);
        }
    }
}





















