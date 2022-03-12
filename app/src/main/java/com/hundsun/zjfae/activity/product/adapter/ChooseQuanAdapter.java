package com.hundsun.zjfae.activity.product.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.bean.CardVoucherBean;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseQuanAdapter extends RecyclerView.Adapter<ChooseQuanAdapter.ChooseQuanViewHolder> {


    private List<CardVoucherBean> quanList;

    private Context context;
    private LayoutInflater inflater;


    private ItemOnClick itemOnClick;
    private boolean isCheck = false;



    private String quanDetailsId = "";

    private Map<Integer, Boolean> booleanMap;

    private Map<Integer ,Integer> oldHeightMap;

    public ChooseQuanAdapter(Context context, List<CardVoucherBean> quanList) {
        this.context = context;
        this.quanList = quanList;
        inflater = LayoutInflater.from(context);

        booleanMap = new HashMap<>();
        oldHeightMap = new HashMap<>();
    }


    public void setQuanDetailsId(String quanDetailsId) {
        this.quanDetailsId = quanDetailsId;
        notifyDataSetChanged();
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void setItemOnClick(ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
    }

    @NonNull
    @Override
    public ChooseQuanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View rootView = inflater.inflate(R.layout.choose_quan_item_layout, viewGroup, false);
        ChooseQuanViewHolder viewHolder = new ChooseQuanViewHolder(rootView);
        return viewHolder;
    }


    public void onBindViewHolder(@NonNull final ChooseQuanViewHolder chooseQuanViewHolder, final int position) {

        if (quanDetailsId.equals(quanList.get(position).getQuanDetailsId())) {
            if (chooseQuanViewHolder.image_check.getVisibility() == View.VISIBLE) {
            } else {
                chooseQuanViewHolder.image_check.setVisibility(View.VISIBLE);
                if (quanList.get(position).getQuanType().equals("A")) {
                    chooseQuanViewHolder.tv_benjin.setText("可加息本金\n¥" + quanList.get(position).getEnableIncreaseInterestAmount());
                    chooseQuanViewHolder.tv_benjin.setVisibility(View.VISIBLE);
                } else {
                    chooseQuanViewHolder.tv_benjin.setVisibility(View.INVISIBLE);
                }
            }

        } else {
            chooseQuanViewHolder.tv_benjin.setVisibility(View.INVISIBLE);
            chooseQuanViewHolder.image_check.setVisibility(View.INVISIBLE);
        }

        //"A":加息券,"L":抵用券,"F":满减券
        if (quanList.get(position).getQuanType().equals("A")) {
            chooseQuanViewHolder.money_type.setVisibility(View.INVISIBLE);
            StringBuffer buffer = new StringBuffer();
            ImageLoad.getImageLoad().LoadImage(context, R.drawable.rates, chooseQuanViewHolder.image_type);
            if (quanList.get(position).getQuanIncreaseInterestAmount().equals("-1")) {
                buffer.append("◇本券加息本金:不限").append("\n");

            } else {
                buffer.append("◇本券加息本金:").append(quanList.get(position).getQuanIncreaseInterestAmount()).append("元\n");

            }

            String splitRemark = quanList.get(position).getCatalogRemark();


            if (!splitRemark.equals("")) {
                String[] remark = splitRemark.split("\\n");
                if (remark.length > 1) {
                    for (int i = 0; i < remark.length; i++) {
                        buffer.append("◇").append(remark[i]).append("\n");
                    }
                }
                else {
                    buffer.append("◇").append(splitRemark.replaceAll("\\n","").trim()).append("\n");
                }
            }



            buffer.append("◇有效期至:").append(quanList.get(position).getQuanValidityEnd());

            chooseQuanViewHolder.quanCatalogRemark.setText(buffer.toString().trim());

            BigDecimal bigDecimal = new BigDecimal(MoneyUtil.moneyMul(quanList.get(position).getQuanValue().trim(), "100"));
            chooseQuanViewHolder.quanValue.setText(bigDecimal.stripTrailingZeros().toPlainString() + "%");

        }
        //抵用券
        else if (quanList.get(position).getQuanType().equals("L")) {


            String splitRemark = quanList.get(position).getCatalogRemark();
            StringBuffer buffer = new StringBuffer();


            chooseQuanViewHolder.money_type.setVisibility(View.VISIBLE);

            ImageLoad.getImageLoad().LoadImage(context, R.drawable.diyong_use, chooseQuanViewHolder.image_type);

            if (!quanList.get(position).getQuanArrivalPriceLadder().equals("")) {

                buffer.append("◇").append(quanList.get(position).getQuanArrivalPriceLadder()).append("\n");
            }


            if (!splitRemark.equals("")) {
                String[] remark = splitRemark.split("\\n");
                if (remark.length > 1) {

                    for (int i = 0; i < remark.length; i++) {
                        CCLog.e("index" + i, remark[i]);
                        buffer.append("◇").append(remark[i]).append("\n");
                    }

                } else {
                    buffer.append("◇").append(splitRemark).append("\n");
                }
            }

            buffer.append("◇有效期至:").append(quanList.get(position).getQuanValidityEnd());
            chooseQuanViewHolder.quanCatalogRemark.setText(buffer.toString().trim());
            chooseQuanViewHolder.quanValue.setText(quanList.get(position).getQuanValue());



        }
        //满减券
        else if (quanList.get(position).getQuanType().equals("F")) {
            chooseQuanViewHolder.money_type.setVisibility(View.VISIBLE);
            ImageLoad.getImageLoad().LoadImage(context, R.drawable.usable, chooseQuanViewHolder.image_type);
            StringBuffer buffer = new StringBuffer();
            buffer.append("◇满").append(quanList.get(position).getQuanFullReducedAmount()).append("可用").append("\n");

            String splitRemark = quanList.get(position).getCatalogRemark();


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



            buffer.append("◇有效期至:").append(quanList.get(position).getQuanValidityEnd());
            chooseQuanViewHolder.quanCatalogRemark.setText(buffer.toString().trim());

            chooseQuanViewHolder.quanValue.setText(quanList.get(position).getQuanValue());

        }


        chooseQuanViewHolder.quanDetailsCode.setText(String.format(context.getResources().getString(R.string.quanDetailsCode), quanList.get(position).getQuanDetailsId()));
        chooseQuanViewHolder.quanName.setText(quanList.get(position).getQuanName());
        chooseQuanViewHolder.quan_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClick != null) {


                    if (!quanList.get(position).getQuanDetailsId().equals(quanDetailsId)) {
                        isCheck = true;
                    } else {
                        if (isCheck) {
                            isCheck = false;
                        } else {
                            isCheck = true;
                        }

                    }
                    itemOnClick.onItemClick(position, isCheck);
                }

            }
        });




        chooseQuanViewHolder.more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!oldHeightMap.containsKey(position)){
                    oldHeightMap.put(position,chooseQuanViewHolder.quanCatalogRemark.getHeight());
                }

                if (!booleanMap.containsKey(position)){
                    booleanMap.put(position,false);
                }


                if (!booleanMap.get(position)) {

                    booleanMap.put(position, true);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    chooseQuanViewHolder.quanCatalogRemark.setLayoutParams(params);
                    chooseQuanViewHolder.more_tv.setText("收起");
                    ImageLoad.getImageLoad().LoadImage(context, R.drawable.red_refresh, chooseQuanViewHolder.more_iv);


                } else {
                    booleanMap.put(position, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, oldHeightMap.get(position));
                    chooseQuanViewHolder.quanCatalogRemark.setLayoutParams(params);
                    chooseQuanViewHolder.more_tv.setText("查看全部");
                    ImageLoad.getImageLoad().LoadImage(context, R.drawable.red_more, chooseQuanViewHolder.more_iv);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return quanList.size();
    }

    public class ChooseQuanViewHolder extends RecyclerView.ViewHolder {
        LinearLayout quan_item_layout, content_layout, more_layout;
        TextView quanValue, quanName, quanDetailsCode, quanCatalogRemark, more_tv, money_type, tv_benjin;
        ImageView image_check, image_type, more_iv;

        public ChooseQuanViewHolder(@NonNull View itemView) {
            super(itemView);
            quan_item_layout = itemView.findViewById(R.id.quan_item_layout);
            quanValue = itemView.findViewById(R.id.quanValue);
            quanName = itemView.findViewById(R.id.quanName);
            money_type = itemView.findViewById(R.id.money_type);
            quanDetailsCode = itemView.findViewById(R.id.quanDetailsCode);
            quanCatalogRemark = itemView.findViewById(R.id.quanCatalogRemark);
            image_check = itemView.findViewById(R.id.image_check);
            image_type = itemView.findViewById(R.id.image_type);

            content_layout = itemView.findViewById(R.id.content_layout);
            more_layout = itemView.findViewById(R.id.more_layout);
            more_tv = itemView.findViewById(R.id.more_tv);
            more_iv = itemView.findViewById(R.id.more_iv);
            tv_benjin = itemView.findViewById(R.id.tv_benjin);//加息本金
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }

    public interface ItemOnClick {
        void onItemClick(int position, boolean isCheck);
    }


}
