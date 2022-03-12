package com.hundsun.zjfae.fragment.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
import com.hundsun.zjfae.activity.product.SpvProductDetailActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;


public class RecyclerViewLimitScrollAdapter extends RecyclerView.Adapter<RecyclerViewLimitScrollAdapter.MyViewHolder> {


    private List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> data;

    private Context mContext;

    private LayoutInflater inflater;
    private int count = 0;

    public RecyclerViewLimitScrollAdapter(Context context, List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> data, int count) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.count = count;
    }


    private void initData(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject deta, int index, MyViewHolder viewHolder) {

        List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject.IconsList> iconsList
                = deta.getIconsListList();
        setImage(iconsList, viewHolder);

        viewHolder.productName.setText(deta.getProductName());
        viewHolder.expectedMaxAnnualRate.setText(deta.getExpectedMaxAnnualRate());
        viewHolder.deadline.setText(deta.getLeftDays());
        viewHolder.buyerSmallestAmount.setText(MoneyUtil.formatMoney2(deta.getBuyerSmallestAmount()) + "元起购");

        productState(deta, viewHolder);
    }


    //设置产品状态
    private void productState(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject deta, MyViewHolder viewHolder) {

        String state = deta.getSellingStatus();

        //0-敬请期待，1-售卖中，2-已售罄，3-已结束
        switch (state) {
            case "0":
                viewHolder.selling_image.setVisibility(View.VISIBLE);
                viewHolder.roundProgressBar.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.stay_tuned, viewHolder.selling_image);
                break;
            case "2":
                viewHolder.selling_image.setVisibility(View.VISIBLE);
                viewHolder.roundProgressBar.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.sold_out, viewHolder.selling_image);
                //Glide.with(mContext).load(R.drawable.sold_out).into(viewHolder.selling_image);
                break;
            case "3":
                viewHolder.selling_image.setVisibility(View.VISIBLE);
                viewHolder.roundProgressBar.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.ended, viewHolder.selling_image);
                // Glide.with(mContext).load(R.drawable.ended).into(viewHolder.selling_image);
                break;
            case "4":
                viewHolder.selling_image.setVisibility(View.VISIBLE);
                viewHolder.roundProgressBar.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.confirming, viewHolder.selling_image);
                break;
            default:
                viewHolder.selling_image.setVisibility(View.GONE);
                viewHolder.roundProgressBar.setVisibility(View.VISIBLE);
                float progress = Float.valueOf(deta.getProcess());
                viewHolder.roundProgressBar.setProgress((int) progress);
                break;
        }


    }


    private void setImage(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject.IconsList> iconsList, MyViewHolder viewHolder) {

        if (iconsList.size() == 0 || iconsList.isEmpty()) {
            return;
        }

        List<String> leftList = new ArrayList<>();
        List<String> middleList = new ArrayList<>();
        List<String> rightList = new ArrayList<>();

        for (int i = 0; i < iconsList.size(); i++) {
            String type = iconsList.get(i).getIconsPosition();
            if (type.equals("left")) {
                leftList.add(iconsList.get(i).getUuid());
            } else if (type.equals("l_middle")) {
                middleList.add(iconsList.get(i).getUuid());
            } else if (type.equals("right_lower")) {
                rightList.add(iconsList.get(i).getUuid());
            }
        }
        setLeftImage(leftList, viewHolder);
        setMiddleImage(middleList, viewHolder);
        setRightImage(rightList, viewHolder);
    }

    public void setImage(ImageView imageView, String fileName) {
        imageView.setVisibility(View.VISIBLE);
        if (ADSharePre.getListConfiguration(ADSharePre.homeProductIcon, BaseCacheBean.class) == null) {
            imageView.setVisibility(View.GONE);
            return;
        }
        List<BaseCacheBean> imageList = ADSharePre.getListConfiguration(ADSharePre.homeProductIcon, BaseCacheBean.class);
        if (imageList.isEmpty()) {
            imageView.setVisibility(View.GONE);
            return;
        } else {

            for (BaseCacheBean imageBean : imageList) {
                if (fileName.equals(imageBean.getUuid())) {


                    ImageLoad.getImageLoad().LoadImage(mContext, imageBean.getIconsAddress(), imageView);

                }

            }

        }


    }

    public void setImage(ImageView imageView) {
        imageView.setVisibility(View.INVISIBLE);
    }


    //设置左边的角标
    private void setLeftImage(List<String> leftList, MyViewHolder viewHolder) {

        if (leftList.size() == 1) {
            setImage(viewHolder.product_left_top_icon, leftList.get(0));
            setImage(viewHolder.product_left_bottom_icon);
        } else if (leftList.size() == 2) {
            setImage(viewHolder.product_left_top_icon, leftList.get(0));

            setImage(viewHolder.product_left_bottom_icon, leftList.get(1));
        } else {
            setImage(viewHolder.product_left_top_icon);
            setImage(viewHolder.product_left_bottom_icon);
        }

    }


    //设置中间的角标
    private void setMiddleImage(List<String> middleList, MyViewHolder viewHolder) {
        //只有一张图
        if (middleList.size() == 1) {
            String leftImage_path = middleList.get(0);
            setImage(viewHolder.left_middle, leftImage_path);
            setImage(viewHolder.center_middle);
            setImage(viewHolder.right_middle);
        }
        //只有两张图
        else if (middleList.size() == 2) {
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            setImage(viewHolder.left_middle, leftImage_path);
            setImage(viewHolder.center_middle, centerImage_path);
            setImage(viewHolder.right_middle);
        }
        //三张图
        else if (middleList.size() == 3) {
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            String rightImage_path = middleList.get(2);
            setImage(viewHolder.left_middle, leftImage_path);
            setImage(viewHolder.center_middle, centerImage_path);
            setImage(viewHolder.right_middle, rightImage_path);
        } else {
            setImage(viewHolder.left_middle);
            setImage(viewHolder.center_middle);
            setImage(viewHolder.right_middle);
        }


    }


    //设置右下角角标
    private void setRightImage(List<String> rightLft, MyViewHolder viewHolder) {

        if (rightLft.size() == 1) {
            String imagePath = rightLft.get(0);
            setImage(viewHolder.product_right_bottom, imagePath);
        } else {
            setImage(viewHolder.product_right_bottom);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.limitscroller_adapter_layout, null, false);
        MyViewHolder viewHolder = new MyViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        initData(data.get(i), i, myViewHolder);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }


                Intent intent = new Intent();
                intent.putExtra("productCode", data.get(i).getProductCode());
                intent.putExtra("sellingStatus", data.get(i).getSellingStatus());
                if (!"05".equals(data.get(i).getProductSpecialArea())) {
                    intent.putExtra("delegationCode", data.get(i).getDelegationCode());
                    intent.putExtra("leftDays", data.get(i).getLeftDays());
                    intent.putExtra("ifAllBuy", data.get(i).getIfAllBuy());
                    intent.putExtra("delegationId", data.get(i).getDelegationId());
                    intent.putExtra("ifAllBuy", data.get(i).getIfAllBuy());
                    intent.putExtra("delegateNum", data.get(i).getDelegateNum());
                    intent.setClass(mContext, SpvProductDetailActivity.class);
                } else {
                    intent.setClass(mContext, ProductCodeActivity.class);
                }


                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, expectedMaxAnnualRate, deadline, buyerSmallestAmount;
        private RoundProgressBar roundProgressBar;
        private ImageView product_left_top_icon, product_left_bottom_icon;
        private ImageView left_middle, center_middle, right_middle;
        private ImageView product_right_bottom, selling_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            expectedMaxAnnualRate = itemView.findViewById(R.id.expectedMaxAnnualRate);
            deadline = itemView.findViewById(R.id.deadline);
            buyerSmallestAmount = itemView.findViewById(R.id.buyerSmallestAmount);
            roundProgressBar = itemView.findViewById(R.id.roundProgressBar);
            product_right_bottom = itemView.findViewById(R.id.product_right_bottom);
            product_left_top_icon = itemView.findViewById(R.id.product_left_icon);
            product_left_bottom_icon = itemView.findViewById(R.id.product_left_bottom_icon);
            left_middle = itemView.findViewById(R.id.left_middle);
            center_middle = itemView.findViewById(R.id.center_middle);
            right_middle = itemView.findViewById(R.id.right_middle);
            selling_image = itemView.findViewById(R.id.selling_image);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }
}
