package com.hundsun.zjfae.activity.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import onight.zjfae.afront.gens.MyDiscount;

public class OtherCardVoucherAdapter extends RecyclerView.Adapter<OtherCardVoucherAdapter.OtherCardViewHolder> {


    private List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> listBeans;
    private Context context;
    private LayoutInflater inflater;
    private String type;
    private OnItemClickCopy itemClickCopy;

    private Map<Integer, Boolean> booleanMap;
    private Map<Integer ,Integer> oldHeightMap;

    public OtherCardVoucherAdapter(Context context, List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> listBeans, String type) {
        this.context = context;
        this.listBeans = listBeans;
        this.type = type;
        inflater = LayoutInflater.from(context);
        oldHeightMap = new HashMap<>();
        booleanMap = new HashMap<>();

    }

    public void setItemClickCopy(OnItemClickCopy itemClickCopy) {
        this.itemClickCopy = itemClickCopy;
    }

    public void rest(List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> listBeans) {
        this.listBeans = listBeans;

        notifyDataSetChanged();
    }


    public void cleanData(){
        if (listBeans != null && !listBeans.isEmpty()){
            listBeans.clear();
        }
    }

    @NonNull
    @Override
    public OtherCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        int viewType = getItemViewType(i);

        if (viewType == 20) {
            //已使用
            View efficacy = inflater.inflate(R.layout.other_card_voucher_usage_layout, viewGroup, false);
            OtherCardViewHolder viewHolder = new OtherCardViewHolder(efficacy);
            return viewHolder;
        } else if (viewType == 30) {
            //失效
            View efficacy = inflater.inflate(R.layout.other_card_voucher_efficacy_layout, viewGroup, false);
            OtherCardViewHolder viewHolder = new OtherCardViewHolder(efficacy);
            return viewHolder;
        } else {
            //可用
            View efficacy = inflater.inflate(R.layout.other_card_voucher_default_layout, viewGroup, false);
            OtherCardViewHolder viewHolder = new OtherCardViewHolder(efficacy);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final OtherCardViewHolder otherCardViewHolder, final int position) {

        final int itemType = otherCardViewHolder.getItemViewType();
        String splitRemark = listBeans.get(position).getQuanCatalogRemark();
        StringBuffer buffer = new StringBuffer();
        if (!listBeans.get(position).getQuanPwdName().equals("")) {
            buffer.append("◇").append(listBeans.get(position).getQuanPwdName()).append(":").append(listBeans.get(position).getQuanDetailsPassword()).append("\n");
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

        buffer.append("◇有效期:").append(listBeans.get(position).getQuanValidityStart()).append("至");
        buffer.append(listBeans.get(position).getQuanValidityEnd());


        otherCardViewHolder.quanCodeName.setText(listBeans.get(position).getQuanCodeName());
        otherCardViewHolder.quanDetailsCode.setText(String.format(context.getResources().getString(R.string.quanDetailsCode), listBeans.get(position).getQuanDetailsCode()));
        otherCardViewHolder.quanName.setText(listBeans.get(position).getQuanName());
        otherCardViewHolder.quanCatalogRemark.setText(buffer.toString());
        //kaquan_isexpired
        if (listBeans.get(position).getIsExpired() != null && listBeans.get(position).getIsExpired().equals("1")) {
            if (otherCardViewHolder.isExpired_image != null) {
                otherCardViewHolder.isExpired_image.setVisibility(View.VISIBLE);
            }
        } else {
            if (otherCardViewHolder.isExpired_image != null) {
                otherCardViewHolder.isExpired_image.setVisibility(View.GONE);
            }
        }

        if (itemType == 0) {
            //可用
            if (listBeans.get(position).getQuanOtherQuanType().equals("discount")) {
                //其他折扣券
                BigDecimal bigDecimal = new BigDecimal(listBeans.get(position).getQuanValue());
                BigDecimal bignum2 = new BigDecimal("10");
                otherCardViewHolder.quanValue.setText(bigDecimal.multiply(bignum2).stripTrailingZeros() + "折");
                otherCardViewHolder.moneytype.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(context, R.drawable.other_rates, otherCardViewHolder.image_type);
            } else if (listBeans.get(position).getQuanOtherQuanType().equals("value")) {
                //其他面值权
                //红图
                otherCardViewHolder.quanValue.setText(listBeans.get(position).getQuanValue());
                otherCardViewHolder.moneytype.setVisibility(View.VISIBLE);
                ImageLoad.getImageLoad().LoadImage(context, R.drawable.other_usable, otherCardViewHolder.image_type);
            }
            otherCardViewHolder.copy_quanCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickCopy != null) {
                        itemClickCopy.onCopyValue(listBeans.get(position).getQuanDetailsCode());
                    }
                }
            });
            if (!listBeans.get(position).getInterfaceCode().equals("")) {
                if (!listBeans.get(position).getLinkAddress().equals("")) {
                    otherCardViewHolder.use_button.setVisibility(View.VISIBLE);
                    otherCardViewHolder.use_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemClickCopy != null) {
                                itemClickCopy.onUse(listBeans.get(position).getLinkAddress());
                            }
                        }
                    });
                }
            } else {
                otherCardViewHolder.use_button.setVisibility(View.GONE);
            }
            if (!listBeans.get(position).getQuanPwdName().equals("")) {
                otherCardViewHolder.copy_passWord.setVisibility(View.VISIBLE);
                otherCardViewHolder.copy_passWord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickCopy != null) {
                            itemClickCopy.onCopyPassWord(listBeans.get(position).getQuanDetailsPassword());
                        }
                    }
                });
            } else {
                otherCardViewHolder.copy_passWord.setVisibility(View.GONE);
            }
        } else {
            //券失效
            if (listBeans.get(position).getQuanOtherQuanType().equals("discount")) {
                //其他折扣券
                BigDecimal bigDecimal = new BigDecimal(listBeans.get(position).getQuanValue());
                BigDecimal bignum2 = new BigDecimal("10");
                otherCardViewHolder.quanValue.setText(bigDecimal.multiply(bignum2).stripTrailingZeros() + "折");
                otherCardViewHolder.moneytype.setVisibility(View.GONE);
            } else if (listBeans.get(position).getQuanOtherQuanType().equals("value")) {
                //其他面值权
                otherCardViewHolder.quanValue.setText(listBeans.get(position).getQuanValue());
                otherCardViewHolder.moneytype.setVisibility(View.VISIBLE);
            }
        }




        otherCardViewHolder.more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!oldHeightMap.containsKey(position)){
                    oldHeightMap.put(position,otherCardViewHolder.quanCatalogRemark.getHeight());
                }

                if (!booleanMap.containsKey(position)){
                    booleanMap.put(position,false);
                }

                if (!booleanMap.get(position)) {

                    booleanMap.put(position, true);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    otherCardViewHolder.quanCatalogRemark.setLayoutParams(params);
                    otherCardViewHolder.more_tv.setText("收起");

                    if (itemType == 30) {
                        ImageLoad.getImageLoad().LoadImage(context, R.drawable.gray_refresh, otherCardViewHolder.more_iv);
                    } else {
                        ImageLoad.getImageLoad().LoadImage(context, R.drawable.red_refresh, otherCardViewHolder.more_iv);
                    }


                } else {
                    booleanMap.put(position, false);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, oldHeightMap.get(position));
                    otherCardViewHolder.quanCatalogRemark.setLayoutParams(params);
                    otherCardViewHolder.more_tv.setText("查看全部");

                    if (itemType == 30) {
                        ImageLoad.getImageLoad().LoadImage(context, R.drawable.gray_more, otherCardViewHolder.more_iv);
                    } else {
                        ImageLoad.getImageLoad().LoadImage(context, R.drawable.red_more, otherCardViewHolder.more_iv);
                    }


                }

            }
        });


    }



    @Override
    public int getItemCount() {
        return listBeans.size();
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

    public class OtherCardViewHolder extends RecyclerView.ViewHolder {

        TextView quanDetailsCode, moneytype, quanValue, quanName, quanCatalogRemark, copy_passWord, copy_quanCode, quanCodeName, more_tv;
        Button use_button;
        ImageView image_type, more_iv, isExpired_image;
        LinearLayout more_layout, content_layout;

        public OtherCardViewHolder(@NonNull View itemView) {
            super(itemView);
            quanDetailsCode = itemView.findViewById(R.id.quanDetailsCode);
            moneytype = itemView.findViewById(R.id.money_type);
            quanValue = itemView.findViewById(R.id.quanValue);
            quanCatalogRemark = itemView.findViewById(R.id.quanCatalogRemark);
            quanName = itemView.findViewById(R.id.quanName);
            image_type = itemView.findViewById(R.id.image_type);
            copy_passWord = itemView.findViewById(R.id.copy_passWord);
            copy_quanCode = itemView.findViewById(R.id.copy_quanCode);
            quanCodeName = itemView.findViewById(R.id.quanCodeName);
            use_button = itemView.findViewById(R.id.use_button);
            isExpired_image = itemView.findViewById(R.id.isExpired_image);
            content_layout = itemView.findViewById(R.id.content_layout);
            more_layout = itemView.findViewById(R.id.more_layout);
            more_tv = itemView.findViewById(R.id.more_tv);
            more_iv = itemView.findViewById(R.id.more_iv);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }

    public interface OnItemClickCopy {
        void onCopyValue(String copyCode);

        void onCopyPassWord(String passWord);

        void onUse(String linkAddress);
    }
}
