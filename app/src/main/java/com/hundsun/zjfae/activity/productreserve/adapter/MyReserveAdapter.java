package com.hundsun.zjfae.activity.productreserve.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.SpvProductDetailActivity;
import com.hundsun.zjfae.activity.productreserve.ReserveProductDetailActivity;
import com.hundsun.zjfae.activity.productreserve.SpvReserveProductDetailActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.Utils;

import java.util.List;

import onight.zjfae.afront.gens.PBIFETradeQueryMyOrderList;

/**
 * @Description:我的预约 Recyclerview列表适配器
 * @Author: zhoujianyu
 * @Time: 2018/9/12 10:02
 */
public class MyReserveAdapter extends RecyclerView.Adapter<MyReserveAdapter.ViewHolder> {
    private List<PBIFETradeQueryMyOrderList.PBIFE_trade_queryMyOrderList.TaProductOrderDetailWrapList> mList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private OnCancleReserveListener onCancleReserveListener;

    public MyReserveAdapter(Context context, List<PBIFETradeQueryMyOrderList.PBIFE_trade_queryMyOrderList.TaProductOrderDetailWrapList> list) {
        this.mList = list;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCancleReserveListener(OnCancleReserveListener onCancleReserveListener) {
        this.onCancleReserveListener = onCancleReserveListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_my_reserve, viewGroup, false);

        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder mViewHolder, final int position) {
        mViewHolder.tv_content.setText(mList.get(position).getProductName());
        mViewHolder.tv_money.setText(mList.get(position).getOrderBuyAmount() + "元");
        if (!mList.get(position).getRemainTimeOne().equals("0")) {
            mViewHolder.tv_startorend_time.setText("挂牌开始时间");
            mViewHolder.tv_date.setText(mList.get(position).getBuyStartDate());
        } else if (!mList.get(position).getRemainTimeTwo().equals("0")) {
            mViewHolder.tv_date.setText(mList.get(position).getBuyEndDate());
            mViewHolder.tv_startorend_time.setText("挂牌结束时间");
        } else {
            mViewHolder.tv_date.setText("已结束");
            mViewHolder.tv_startorend_time.setText("挂牌结束时间");
        }
        mViewHolder.tv_percent.setText(mList.get(position).getProductRate() + "%");

        if (mList.get(position).getIsDisplayCancel().equals("0")) {
            //取消按钮不显示
            mViewHolder.btn_1.setVisibility(View.GONE);
        } else if (mList.get(position).getIsDisplayCancel().equals("1")) {
            //显示取消预约
            mViewHolder.btn_1.setVisibility(View.VISIBLE);
        }

        if (mList.get(position).getIsDisplayBuy().equals("1")) {

            mViewHolder.btn_2.setText("立即交易");
            mViewHolder.btn_2.setTextColor(mContext.getResources().getColor(R.color.white));
            mViewHolder.btn_2.setBackground(mContext.getResources().getDrawable(R.drawable.shap_background_red_nopadding));
            mViewHolder.btn_2.setVisibility(View.VISIBLE);

        } else if (mList.get(position).getStatus().equals("取消预约")) {
            mViewHolder.btn_2.setText("已取消");
            mViewHolder.btn_2.setTextColor(mContext.getResources().getColor(R.color.black));
            mViewHolder.btn_2.setBackground(mContext.getResources().getDrawable(R.drawable.shap_background_grey_nopadding));
        } else if (mList.get(position).getStatus().equals("")) {
            mViewHolder.btn_2.setVisibility(View.GONE);
        } else {
            mViewHolder.btn_2.setText(mList.get(position).getStatus());
            mViewHolder.btn_2.setTextColor(mContext.getResources().getColor(R.color.black));
            mViewHolder.btn_2.setBackground(mContext.getResources().getDrawable(R.drawable.shap_background_grey_nopadding));
        }


//        reserveState(position, mViewHolder);
        mViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    if (Utils.isFastDoubleClick()) {
                        return;
                    }
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        mViewHolder.btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消预约弹框
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                onCancleReserveListener.cancleReserve(mList.get(position).getOrderNum());
            }
        });
        mViewHolder.btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                if (mViewHolder.btn_2.getText().equals("立即交易")) {
                    if (mList.get(position).getOrderType().equals("4")) {
                        Intent intent = new Intent(mContext, SpvReserveProductDetailActivity.class);
                        intent.putExtra("productCode", mList.get(position).getOrderProductCode());
                        intent.putExtra("ifAllBuy", mList.get(position).getIfAllBuy());
                        intent.putExtra("delegationCode",mList.get(position).getTeaoId());
                        intent.putExtra("delegationId",mList.get(position).getTeaoId());
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, ReserveProductDetailActivity.class);
                        intent.putExtra("productCode", mList.get(position).getOrderProductCode());
                        intent.putExtra("sellingStatus", "1");
                        intent.putExtra("orderType", mList.get(position).getOrderType());
                        intent.putExtra("orderNum", mList.get(position).getOrderNum());
                        mContext.startActivity(intent);
                    }

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


//    //设置预约的状态
//    private void reserveState(int position, ViewHolder mViewHolder) {
//        String state = mList.get(position).getOrderStatus();
//        if (state == null) {
//            return;
//        }
//        //
//        switch (state) {
//            case "0":
//                break;
//            case "1":
//                break;
//            case "2":
//                break;
//            case "3":
//                break;
//            default:
//                break;
//        }
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_layout;
        TextView tv_content, tv_money, tv_date, tv_percent, tv_startorend_time;
        TextView btn_1, btn_2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_percent = itemView.findViewById(R.id.tv_percent);
            tv_startorend_time = itemView.findViewById(R.id.tv_startorend_time);
            btn_1 = itemView.findViewById(R.id.btn_1);
            btn_2 = itemView.findViewById(R.id.btn_2);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    //取消预约接口 回到Activity 弹框 请求接口
    public interface OnCancleReserveListener {
        void cancleReserve(String orderNum);
    }
}
