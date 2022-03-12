package com.hundsun.zjfae.activity.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.assetstream.AssetStreamActivity;
import com.hundsun.zjfae.common.adapter.BaseAdapter;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import onight.zjfae.afront.gens.MyDiscount;

public class EnvelopeAdapter extends BaseAdapter<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> {

    private Context context;
    private String type;
    private WithdrawalsClickListener withdrawalsClickListener;

    private Map<Integer, Boolean> booleanMap;
    private Map<Integer, Integer> oldHeightMap;

    public EnvelopeAdapter(Context context, List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> kaQuanLists, String type) {
        super(context, kaQuanLists);
        this.context = context;
        this.type = type;
        oldHeightMap = new HashMap<>();
        booleanMap = new HashMap<>();
    }


    public void setWithdrawalsClickListener(WithdrawalsClickListener withdrawalsClickListener) {
        this.withdrawalsClickListener = withdrawalsClickListener;
    }



    public void cleanData(){
        if (mData != null && !mData.isEmpty()){
            mData.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId(int position) {
        int types = getItemViewType(position);
        if (types == 30) {
            return R.layout.envelope_item_layout_efficacy;
        } else if (types == 20) {

            return R.layout.envelope_item_layout_use;
        } else {

            return R.layout.envelope_item_layout;
        }
    }

    @Override
    protected void bindData(final MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList kaQuanList, final int position) {
        TextView quanDetailsCode = holder.getView(R.id.quanDetailsCode);
        TextView quanValue = holder.getView(R.id.quanValue);
        final TextView quanCatalogRemark = holder.getView(R.id.quanCatalogRemark);
        TextView quanName = holder.getView(R.id.quanName);
        TextView use_button = holder.getView(R.id.use_button);
        TextView recharge_button = holder.getView(R.id.recharge_button);

        ImageView isExpired_image = holder.getView(R.id.isExpired_image);
        LinearLayout more_layout = holder.getView(R.id.more_layout);
        final TextView more_tv = holder.getView(R.id.more_tv);
        final ImageView more_iv = holder.getView(R.id.more_iv);


        String splitRemark = kaQuanList.getQuanCatalogRemark();
        StringBuffer buffer = new StringBuffer();

        if (!splitRemark.equals("")) {
            String[] remark = splitRemark.split("\\n");
            if (remark.length > 1) {
                for (int i = 0; i < remark.length; i++) {
                    buffer.append("◇").append(remark[i]).append("\n");
                }

            } else {
                buffer.append("◇").append(splitRemark.replaceAll("\\n","").trim()).append("\n");
            }
        }

        buffer.append("◇有效期:").append(kaQuanList.getQuanValidityStart()).append("至");
        buffer.append(kaQuanList.getQuanValidityEnd());


        quanValue.setText(kaQuanList.getQuanValue());
        quanDetailsCode.setText(String.format(context.getResources().getString(R.string.quanDetailsCode),kaQuanList.getQuanDetailsCode()));
        quanName.setText(kaQuanList.getQuanName());
        quanCatalogRemark.setText(buffer.toString());
        final int itemType = getItemViewType(position);
        if (itemType == 0) {
            String quanCanCash = kaQuanList.getQuanCanCash();

            String isExpired = kaQuanList.getIsExpired();
            if (quanCanCash.equals("1")) {
                //可提现
                recharge_button.setText("可提现>");
                recharge_button.setClickable(true);
                recharge_button.setEnabled(true);
                recharge_button.setTextColor(context.getResources().getColor(android.R.color.black));
                recharge_button.setBackground(context.getResources().getDrawable(R.drawable.envelope_button_deposite_shape));
            } else {
                //不可提现
                recharge_button.setText("不可提现");
                recharge_button.setClickable(false);
                recharge_button.setEnabled(false);
                recharge_button.setTextColor(context.getResources().getColor(android.R.color.black));
                recharge_button.setBackground(context.getResources().getDrawable(R.drawable.envelope_gray_shape));
            }


            if (isExpired.equals("1")) {
                isExpired_image.setVisibility(View.VISIBLE);
            } else {
                isExpired_image.setVisibility(View.GONE);
            }


            use_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListner != null) {
                        onItemClickListner.onItemClickListener(v, kaQuanList, position);
                    }
                }
            });

            recharge_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (withdrawalsClickListener != null) {
                        withdrawalsClickListener.onItemWithdrawalsClick(position);
                    }
                }
            });
        }


        more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!oldHeightMap.containsKey(position)) {
                    oldHeightMap.put(position, quanCatalogRemark.getHeight());
                }
                if (!booleanMap.containsKey(position)) {
                    booleanMap.put(position, false);
                }

                if (!booleanMap.get(position)) {

                    booleanMap.put(position, true);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    quanCatalogRemark.setLayoutParams(params);
                    more_tv.setText("收起");

                    if (itemType == 30) {
                        ImageLoad.getImageLoad().LoadImage(context, R.drawable.gray_refresh, more_iv);
                    } else {
                        ImageLoad.getImageLoad().LoadImage(context, R.drawable.red_refresh, more_iv);
                    }


                } else {

                    booleanMap.put(position, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, oldHeightMap.get(position));
                    quanCatalogRemark.setLayoutParams(params);
                    more_tv.setText("查看全部");

                    if (itemType == 30) {
                        ImageLoad.getImageLoad().LoadImage(context, R.drawable.gray_more, more_iv);
                    } else {
                        ImageLoad.getImageLoad().LoadImage(context, R.drawable.red_more, more_iv);
                    }


                }

            }
        });

        if (type.equals("20")) {
            holder.getView(R.id.discount_coupon_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, AssetStreamActivity.class);
                    intent.putExtra("id", kaQuanList.getQuanDetailsId());
                    mContext.startActivity(intent);
                }
            });
        }


    }




    @Override
    public int getItemViewType(int position) {
        if (type.equals("10")) {//可用
            return super.getItemViewType(position);

        } else if (type.equals("30")) {//失效
            return 30;
        } else if (type.equals("20")) {//已使用
            return 20;
        }
        return super.getItemViewType(position);

    }


    public interface WithdrawalsClickListener {

        void onItemWithdrawalsClick(int position);
    }
}
