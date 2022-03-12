package com.hundsun.zjfae.activity.mine.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.adapter.BaseAdapter;
import com.hundsun.zjfae.common.user.UserInfoSharePre;

import java.util.List;

import onight.zjfae.afront.gens.ProductHighTransferOrderList;

public class TransferOrderHighAdapter extends BaseAdapter<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList> {


    public TransferOrderHighAdapter(Context context, List<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList> list) {
        super(context, list);
    }



    @Override
    protected int getLayoutId(int position) {
        return R.layout.item_transfer_product;
    }

    @Override
    protected void bindData(final ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList productTradeInfoList, final int position) {

        TextView productName = holder.getView(R.id.productName);
        TextView targetRate = holder.getView(R.id.targetRate);
        TextView leastTranAmount = holder.getView(R.id.leastTranAmount);
        TextView leftDays = holder.getView(R.id.leftDays);
        TextView delegateAmount = holder.getView(R.id.delegateAmount);
        ImageView my_entry = holder.getView(R.id.my_entry);


        productName.setText(productTradeInfoList.getProductName());
        delegateAmount.setText(productTradeInfoList.getDelegateAmount());
        targetRate.setText(productTradeInfoList.getTargetRate()+"%");
        leastTranAmount.setText(productTradeInfoList.getLeastTranAmount());
        leftDays.setText(productTradeInfoList.getLeftDays());

        if (UserInfoSharePre.getTradeAccount().equals( productTradeInfoList.getTradeAccount())){
            my_entry.setVisibility(View.VISIBLE);
        }
        else {
            my_entry.setVisibility(View.GONE);
        }

        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListner != null){
                    onItemClickListner.onItemClickListener(holder.getRootView(),productTradeInfoList,position);
                }
            }
        });
    }
}
