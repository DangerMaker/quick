package com.hundsun.zjfae.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.LimitScrollerView;
import com.hundsun.zjfae.common.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;


public class MyLimitScrollAdapter implements LimitScrollerView.LimitScrollAdapter {


    private List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> data;

    private Context mContext;

    private LayoutInflater inflater;

    public MyLimitScrollAdapter(Context context, List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> data) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }


    private TextView productName, expectedMaxAnnualRate, deadline, buyerSmallestAmount;
    private RoundProgressBar roundProgressBar;
    private ImageView product_left_top_icon, product_left_bottom_icon;
    private ImageView left_middle, center_middle, right_middle;
    private ImageView product_right_bottom, selling_image;

    @Override
    public View getView(int index) {
        View itemView = inflater.inflate(R.layout.limitscroller_adapter_layout, null, false);
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
        itemView.setTag(data.get(index));
        initData(data.get(index), index);
        return itemView;
    }


    private void initData(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject deta, int index) {

        List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject.IconsList> iconsList
                = deta.getIconsListList();
        setImage(iconsList);

        productName.setText(deta.getProductName());
        expectedMaxAnnualRate.setText(deta.getExpectedMaxAnnualRate());
        deadline.setText(deta.getLeftDays());
        buyerSmallestAmount.setText(MoneyUtil.formatMoney2(deta.getBuyerSmallestAmount()) + "元起购");


        productState(deta);

    }


    //设置产品状态
    private void productState(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject deta) {
        String state = deta.getSellingStatus();
        //0-敬请期待，1-售卖中，2-已售罄，3-已结束
        switch (state) {
            case "0":
                selling_image.setVisibility(View.VISIBLE);
                roundProgressBar.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.stay_tuned, selling_image);
                // Glide.with(mContext).load(R.drawable.stay_tuned).into(selling_image);
                break;
            case "2":
                selling_image.setVisibility(View.VISIBLE);
                roundProgressBar.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.sold_out, selling_image);
                //Glide.with(mContext).load(R.drawable.sold_out).into(selling_image);
                break;
            case "3":
                selling_image.setVisibility(View.VISIBLE);
                roundProgressBar.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.ended, selling_image);
                // Glide.with(mContext).load(R.drawable.ended).into(selling_image);
                break;
            case "4":
                selling_image.setVisibility(View.VISIBLE);
                roundProgressBar.setVisibility(View.GONE);
                ImageLoad.getImageLoad().LoadImage(mContext, R.drawable.confirming, selling_image);
                break;
            default:
                selling_image.setVisibility(View.GONE);
                roundProgressBar.setVisibility(View.VISIBLE);
                float progress = Float.valueOf(deta.getProcess());
                roundProgressBar.setProgress((int) progress);
                break;
        }

    }


    private void setImage(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject.IconsList> iconsList) {

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
        setLeftImage(leftList);
        setMiddleImage(middleList);
        setRightImage(rightList);
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
    private void setLeftImage(List<String> leftList) {

        if (leftList.size() == 1) {
            setImage(product_left_top_icon, leftList.get(0));
            setImage(product_left_bottom_icon);
        } else if (leftList.size() == 2) {
            setImage(product_left_top_icon, leftList.get(0));

            setImage(product_left_bottom_icon, leftList.get(1));
        } else {
            setImage(product_left_top_icon);
            setImage(product_left_bottom_icon);
        }

    }


    //设置中间的角标
    private void setMiddleImage(List<String> middleList) {
        //只有一张图
        if (middleList.size() == 1) {
            String leftImage_path = middleList.get(0);
            setImage(left_middle, leftImage_path);
            setImage(center_middle);
            setImage(right_middle);
        }
        //只有两张图
        else if (middleList.size() == 2) {
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            setImage(left_middle, leftImage_path);
            setImage(center_middle, centerImage_path);
            setImage(right_middle);
        }
        //三张图
        else if (middleList.size() == 3) {
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            String rightImage_path = middleList.get(2);
            setImage(left_middle, leftImage_path);
            setImage(center_middle, centerImage_path);
            setImage(right_middle, rightImage_path);
        } else {
            setImage(left_middle);
            setImage(center_middle);
            setImage(right_middle);
        }


    }


    //设置右下角角标
    private void setRightImage(List<String> rightLft) {

        if (rightLft.size() == 1) {
            String imagePath = rightLft.get(0);
            setImage(product_right_bottom, imagePath);
        } else {
            setImage(product_right_bottom);
        }
    }

}
