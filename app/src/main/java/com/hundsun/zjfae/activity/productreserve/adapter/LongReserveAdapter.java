package com.hundsun.zjfae.activity.productreserve.adapter;

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
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.view.RoundProgressBar;

import java.util.List;

import onight.zjfae.afront.gensazj.ProductOrderInfoPB;

/**
 * @Description:长期预约 Recyclerview列表适配器
 * @Author: zhoujianyu
 * @Time: 2018/9/12 10:02
 */
public class LongReserveAdapter extends RecyclerView.Adapter<LongReserveAdapter.ViewHolder> {
    private List<ProductOrderInfoPB.PBIFE_trade_queryProductOrderInfo.TaProductOrderInfoWrapList> mList;
    private Context mContext;
    private String type = "";
    private onItemClicker onItemClicker;

    public LongReserveAdapter(Context context, List<ProductOrderInfoPB.PBIFE_trade_queryProductOrderInfo.TaProductOrderInfoWrapList> list, String type, onItemClicker onItemClicker) {
        this.mList = list;
        this.mContext = context;
        this.type = type;
        this.onItemClicker = onItemClicker;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_long_reserve, viewGroup, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder mViewHolder, final int position) {
        mViewHolder.productName.setText(mList.get(position).getProductName());
        mViewHolder.expectedMaxAnnualRate.setText(mList.get(position).getExpectedMaxAnnualRate() + "%");
        mViewHolder.tv_1.setText(mList.get(position).getOrderEndDate() + "停止预约");
        mViewHolder.tv_2.setText(mList.get(position).getBuyerSmallestAmount() + "元起");
        mViewHolder.tv_3.setText(MoneyUtil.moneyMul(mList.get(position).getDepositRate(),"100.00") + "%保证金比例");
        float progress = Float.valueOf(mList.get(position).getProcess());
        mViewHolder.roundProgressBar.setProgress((int) progress);

        mViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                onItemClicker.OnItemClick(mList.get(position).getProductCode());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_layout;
        TextView productName, expectedMaxAnnualRate, tv_1, tv_2, tv_3;
        RoundProgressBar roundProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            productName = itemView.findViewById(R.id.productName);
            expectedMaxAnnualRate = itemView.findViewById(R.id.expectedMaxAnnualRate);
            roundProgressBar = itemView.findViewById(R.id.roundProgressBar);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            tv_3 = itemView.findViewById(R.id.tv_3);
            SupportDisplay.resetAllChildViewParam(item_layout);
        }
    }

    public interface onItemClicker {
        void OnItemClick(String orderProductCode);
    }

}
