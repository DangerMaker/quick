package com.hundsun.zjfae.fragment.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;


public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ProductsViewHolder> {

    private Context mContext;

    private List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> listBeans;

    private LayoutInflater inflater;


    private OnItemClickListener onItemClickListener;

    private int colorId ;

    private int status;//产品状态

    public HomeProductAdapter(Context context, List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> list) {
        this.mContext = context;
        this.listBeans = list;
        inflater = LayoutInflater.from(context);
        colorId = context.getResources().getColor(R.color.white);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.products_adapter_layout, viewGroup, false);
        ProductsViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ProductsViewHolder(rootView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder productsViewHolder, final int position) {
        List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject.IconsList> iconsList
                = listBeans.get(position).getIconsListList();
        setImage(iconsList, productsViewHolder, position);
        if (!listBeans.get(position).getSellingStatus().equals("1")) {
            status = status + 1;
        }

        if (status == listBeans.size()) {
            //所有产品敬请期待
            if (null != onItemClickListener) {
                onItemClickListener.onProductStatus();
            }
        }

        productsViewHolder.productName.setText(listBeans.get(position).getProductName());
        productsViewHolder.expectedMaxAnnualRate.setText(listBeans.get(position).getExpectedMaxAnnualRate() + "%");
        productsViewHolder.deadline.setText(listBeans.get(position).getDeadline());
        productsViewHolder.buyerSmallestAmount.setText(listBeans.get(position).getBuyerSmallestAmount() + "元起");


        productState(position, productsViewHolder);
        productsViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(listBeans.get(position).getProductCode(), listBeans.get(position).getSellingStatus());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeans.size();
    }


    public void refresh(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> listBeans) {
        this.listBeans = listBeans;
        notifyDataSetChanged();
    }


    //设置产品状态
    private void productState(int position, ProductsViewHolder productsViewHolder) {
        String state = listBeans.get(position).getSellingStatus();
        //0-敬请期待，1-售卖中，2-已售罄，3-已结束
        switch (state) {
            case "0":
                productsViewHolder.selling_image.setVisibility(View.VISIBLE);
                productsViewHolder.roundProgressBar.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.stay_tuned).into(productsViewHolder.selling_image);
                break;
            case "1":
                productsViewHolder.selling_image.setVisibility(View.GONE);
                productsViewHolder.roundProgressBar.setVisibility(View.VISIBLE);
                float progress = Float.valueOf(listBeans.get(position).getProcess());
                productsViewHolder.roundProgressBar.setProgress((int) progress);
                break;
            case "2":
                productsViewHolder.selling_image.setVisibility(View.VISIBLE);
                productsViewHolder.roundProgressBar.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.sold_out).into(productsViewHolder.selling_image);
                break;
            case "3":
                productsViewHolder.selling_image.setVisibility(View.VISIBLE);
                productsViewHolder.roundProgressBar.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.ended).into(productsViewHolder.selling_image);
                break;
            default:
                productsViewHolder.selling_image.setVisibility(View.VISIBLE);
                productsViewHolder.roundProgressBar.setVisibility(View.GONE);
                break;
        }
    }


    private void setImage(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject.IconsList> iconsList, ProductsViewHolder productsViewHolder, int position) {

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
        setLeftImage(leftList, productsViewHolder, position);
        setMiddleImage(middleList, productsViewHolder, position);
        setRightImage(rightList, productsViewHolder, position);
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
        }
        else {

            for (BaseCacheBean imageBean : imageList) {
                if (fileName.equals(imageBean.getUuid())) {


                    ImageLoad.getImageLoad().LoadImage(mContext, imageBean.getIconsAddress(), imageView);

                }

            }

        }


    }


    public void setImage(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }


    //设置左边的角标
    private void setLeftImage(List<String> leftList, ProductsViewHolder productsViewHolder, int position) {

        if (leftList.size() == 1) {
            setImage(productsViewHolder.product_left_top_icon, leftList.get(0));
            setImage(productsViewHolder.product_left_bottom_icon);
        } else if (leftList.size() == 2) {
            setImage(productsViewHolder.product_left_top_icon, leftList.get(0));

            setImage(productsViewHolder.product_left_bottom_icon, leftList.get(1));
        } else {
            setImage(productsViewHolder.product_left_top_icon);
            setImage(productsViewHolder.product_left_bottom_icon);
        }

    }


    //设置中间的角标
    private void setMiddleImage(List<String> middleList, ProductsViewHolder productsViewHolder, int position) {
        //只有一张图
        if (middleList.size() == 1) {
            String leftImage_path = middleList.get(0);
            setImage(productsViewHolder.left_middle, leftImage_path);
            setImage(productsViewHolder.center_middle);
            setImage(productsViewHolder.right_middle);
        }
        //只有两张图
        else if (middleList.size() == 2) {
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            setImage(productsViewHolder.left_middle, leftImage_path);
            setImage(productsViewHolder.center_middle, centerImage_path);
            setImage(productsViewHolder.right_middle);
        }
        //三张图
        else if (middleList.size() == 3) {
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            String rightImage_path = middleList.get(2);
            setImage(productsViewHolder.left_middle, leftImage_path);
            setImage(productsViewHolder.center_middle, centerImage_path);
            setImage(productsViewHolder.right_middle, rightImage_path);
        } else {
            setImage(productsViewHolder.left_middle);
            setImage(productsViewHolder.center_middle);
            setImage(productsViewHolder.right_middle);
        }


    }


    //设置右下角角标
    private void setRightImage(List<String> rightLft, ProductsViewHolder productsViewHolder, int position) {

        if (rightLft.size() == 1) {
            String imagePath = rightLft.get(0);
            setImage(productsViewHolder.product_right_bottom, imagePath);
        } else {
            setImage(productsViewHolder.product_right_bottom);
        }


    }


    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        TextView productName, expectedMaxAnnualRate, deadline, buyerSmallestAmount;
        RoundProgressBar roundProgressBar;
        LinearLayout item_layout;
        ImageView product_left_top_icon, product_left_bottom_icon;
        ImageView left_middle, center_middle, right_middle;
        ImageView product_right_bottom, selling_image;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            expectedMaxAnnualRate = itemView.findViewById(R.id.expectedMaxAnnualRate);
            deadline = itemView.findViewById(R.id.deadline);
            buyerSmallestAmount = itemView.findViewById(R.id.buyerSmallestAmount);
            roundProgressBar = itemView.findViewById(R.id.roundProgressBar);
            item_layout = itemView.findViewById(R.id.item_layout);
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





    public interface OnItemClickListener {
        void onItemClick(String productCode, String sellingStatus);

        void onProductStatus();

    }

}
