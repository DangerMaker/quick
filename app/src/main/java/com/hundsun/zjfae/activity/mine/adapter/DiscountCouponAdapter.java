package com.hundsun.zjfae.activity.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.assetstream.AssetStreamActivity;
import com.hundsun.zjfae.common.adapter.BaseAdapter;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import onight.zjfae.afront.gens.MyDiscount;

public class DiscountCouponAdapter extends BaseAdapter<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> {


    private String type;
    private Map<Integer, Boolean> booleanMap;

    private Map<Integer, Integer> oldHeightMap;

    public DiscountCouponAdapter(Context mContext, List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> list, String type) {
        super(mContext, list);
        this.type = type;
        oldHeightMap = new HashMap<>();
        booleanMap = new HashMap<>();

    }


    public void cleanData(){
        if (mData != null && !mData.isEmpty()){
            mData.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (type.equals("10")) {//可用
            return super.getItemViewType(position);
        } else if (type.equals("30")) {//失效
            return 30;
        } else if (type.equals("20")) {//已使用
            return 20;
        } else {
            return super.getItemViewType(position);
        }

    }


    @Override
    protected int getLayoutId(int position) {
        int types = getItemViewType(position);
        if (types == 30) {
            return R.layout.discount_coupon_item_layout_efficacy;
        } else if (types == 20) {
            return R.layout.discount_coupon_usage_item_layout;
        } else {
            return R.layout.discount_coupon_item_layout;
        }

    }


    @Override
    protected void bindData(final MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList kaQuanList, final int position) {
        TextView quanDetailsCode = holder.getView(R.id.quanDetailsCode);
        TextView quanValue = holder.getView(R.id.quanValue);
        final TextView quanCatalogRemark = holder.getView(R.id.quanCatalogRemark);
        TextView quanName = holder.getView(R.id.quanName);
        ImageView image_type = holder.getView(R.id.image_type);
        Button use_button = holder.getView(R.id.use_button);
        TextView money_type = holder.getView(R.id.money_type);
        final LinearLayout content_layout = holder.getView(R.id.content_layout);
        LinearLayout more_layout = holder.getView(R.id.more_layout);
        final TextView more_tv = holder.getView(R.id.more_tv);
        final ImageView more_iv = holder.getView(R.id.more_iv);

        final int types = getItemViewType(position);


        if (types == 30) {//失效
            if (kaQuanList.getQuanType().equals("F")) {
                //满减券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.use_image, image_type);
            } else if (kaQuanList.getQuanType().equals("A")) {
                //加息券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.rates_hui, image_type);
            } else if (kaQuanList.getQuanType().equals("L")) {
                //抵用券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.diyong_use_hui, image_type);
            }
        }
        //已使用
        else if (types == 20) {

            if (kaQuanList.getQuanType().equals("F")) {
                //满减券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.usable, image_type);
            } else if (kaQuanList.getQuanType().equals("A")) {
                //加息券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.rates, image_type);
            } else if (kaQuanList.getQuanType().equals("L")) {
                //抵用券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.diyong_use, image_type);
            }
        }
        //可用
        else {
            if (kaQuanList.getQuanType().equals("F")) {
                //满减券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.usable, image_type);
            } else if (kaQuanList.getQuanType().equals("A")) {
                //加息券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.rates, image_type);
            } else if (kaQuanList.getQuanType().equals("L")) {
                //抵用券
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.diyong_use, image_type);
            }
            //kaquan_isexpired
            String isExpired = kaQuanList.getIsExpired();
            ImageView isExpired_image = getView(R.id.isExpired_image);
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
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    quanCatalogRemark.setLayoutParams(params);
                    more_tv.setText("收起");
                    if (types == 30) {
                        ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.gray_refresh, more_iv);
                    } else {
                        ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.red_refresh, more_iv);
                    }

                } else {
                    booleanMap.put(position, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, oldHeightMap.get(position));
                    quanCatalogRemark.setLayoutParams(params);
                    more_tv.setText("查看全部");

                    if (types == 30) {
                        ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.gray_more, more_iv);
                    } else {
                        ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.red_more, more_iv);
                    }

                }

            }
        });


        String splitRemark = kaQuanList.getQuanCatalogRemark();
        StringBuffer buffer = new StringBuffer();
        String quanValues = kaQuanList.getQuanValue();
        //加息券
        if (kaQuanList.getQuanType().equals("A")) {

            money_type.setVisibility(View.INVISIBLE);

            if (kaQuanList.getQuanIncreaseInterestAmount().equals("-1")) {
                //不限制
                buffer.append("◇本券加息本金:不限").append("\n");
            } else {
                buffer.append("◇本券加息本金:").append(kaQuanList.getQuanIncreaseInterestAmount() + "元").append("\n");
            }


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


            BigDecimal bigDecimal = new BigDecimal(MoneyUtil.moneyMul(kaQuanList.getQuanValue(), "100"));
            quanValues = bigDecimal.stripTrailingZeros().toPlainString() + "%";


        }

        //满减券
        else if (kaQuanList.getQuanType().equals("F")) {
            money_type.setVisibility(View.VISIBLE);
            buffer.append("◇满").append(kaQuanList.getQuanFullReducedAmount() + "可用").append("\n");
            if (!splitRemark.equals("")) {
                String[] remark = splitRemark.split("\\n");
                if (remark.length > 1) {
                    for (int i = 0; i < remark.length; i++) {
                        buffer.append("◇").append(remark[i]).append("\n");
                    }

                } else {
                    buffer.append("◇").append(splitRemark).append("\n");
                }
            }

            buffer.append("◇有效期:").append(kaQuanList.getQuanValidityStart()).append("至");
            buffer.append(kaQuanList.getQuanValidityEnd());
        }

        //抵用券
        else if (kaQuanList.getQuanType().equals("L")) {
            money_type.setVisibility(View.VISIBLE);


            if (!kaQuanList.getQuanArrivalPriceLadder().equals("")) {

                buffer.append("◇").append(kaQuanList.getQuanArrivalPriceLadder()).append("\n");
            }


            if (!splitRemark.equals("")) {
                String[] remark = splitRemark.split("\\n");
                if (remark.length > 1) {


                    for (int i = 0; i < remark.length; i++) {
                        buffer.append("◇").append(remark[i]).append("\n");
                    }

                } else {
                    buffer.append("◇").append(splitRemark).append("\n");
                }
            }

            buffer.append("◇有效期:").append(kaQuanList.getQuanValidityStart()).append("至");
            buffer.append(kaQuanList.getQuanValidityEnd());
        }


        quanValue.setText(quanValues);
        quanDetailsCode.setText(String.format(mContext.getResources().getString(R.string.quanDetailsCode), kaQuanList.getQuanDetailsCode()));
        quanName.setText(kaQuanList.getQuanName());
        quanCatalogRemark.setText(buffer.toString());

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
}
