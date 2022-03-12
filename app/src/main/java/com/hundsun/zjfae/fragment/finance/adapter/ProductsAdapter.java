package com.hundsun.zjfae.fragment.finance.adapter;

import android.annotation.SuppressLint;
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
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew;
import onight.zjfae.afront.gens.v5.ProductList;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {


    private List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.ProductTradeInfoNewObject> listBeans;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public ProductsAdapter(Context context) {
        this.mContext = context;
    }


    public ProductsAdapter(Context context, List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.ProductTradeInfoNewObject> list) {
        this.listBeans = list;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void refresh(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.ProductTradeInfoNewObject> listBeans) {
        this.listBeans = listBeans;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.products_adapter_layout, viewGroup, false);

        ProductsViewHolder holder = new ProductsViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsViewHolder productsViewHolder, @SuppressLint("RecyclerView") final int position) {
        List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.ProductTradeInfoNewObject.IconsList> iconsList
                = listBeans.get(position).getIconsListList();
        setImage(iconsList, productsViewHolder, position);
        productsViewHolder.productName.setText(listBeans.get(position).getProductName());
        productsViewHolder.expectedMaxAnnualRate.setText(listBeans.get(position).getExpectedMaxAnnualRate());
        productsViewHolder.deadline.setText(listBeans.get(position).getLeftDays());

        if (!"05".equals(listBeans.get(position).getProductSpecialArea())) {

            productsViewHolder.buyerSmallestAmount.setText(MoneyUtil.formatMoney2(listBeans.get(position).getLeastTranAmount()) + "元起购");
        } else {

            productsViewHolder.buyerSmallestAmount.setText(MoneyUtil.formatMoney2(listBeans.get(position).getBuyerSmallestAmount()) + "元起购");

        }


        productState(position, productsViewHolder);
        productsViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listBeans.size();
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
            case "4":
                productsViewHolder.selling_image.setVisibility(View.VISIBLE);
                productsViewHolder.roundProgressBar.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.confirming).into(productsViewHolder.selling_image);
                break;
            default:
                productsViewHolder.selling_image.setVisibility(View.GONE);
                productsViewHolder.roundProgressBar.setVisibility(View.VISIBLE);
                float progress = Float.valueOf(listBeans.get(position).getProcess().equals("") ? "0" : listBeans.get(position).getProcess());
                productsViewHolder.roundProgressBar.setProgress((int) progress);
                break;
        }

    }


    private void setImage(List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.ProductTradeInfoNewObject.IconsList> iconsList, ProductsViewHolder productsViewHolder, int position) {

        if (iconsList == null || iconsList.isEmpty()) {
            setImage(productsViewHolder.product_left_top_icon);
            setImage(productsViewHolder.product_left_bottom_icon);
            setImage(productsViewHolder.left_middle);
            setImage(productsViewHolder.center_middle);
            setImage(productsViewHolder.right_middle);
            setImage(productsViewHolder.product_right_bottom);
            return;
        } else {
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
    }

    public void setImage(ImageView imageView, String fileName) {
        if (ADSharePre.getListConfiguration(ADSharePre.subscribeIcon, BaseCacheBean.class) == null) {
            imageView.setVisibility(View.INVISIBLE);
            return;
        }
        List<BaseCacheBean> imageList = ADSharePre.getListConfiguration(ADSharePre.subscribeIcon, BaseCacheBean.class);
        if (imageList.isEmpty()) {
            imageView.setVisibility(View.INVISIBLE);
            return;
        } else {
            imageView.setVisibility(View.VISIBLE);
            for (BaseCacheBean imageBean : imageList) {
                if (fileName.equals(imageBean.getUuid())) {
                    if (StringUtils.isNotBlank(imageBean.getIconsAddress())) {
                        ImageLoad.getImageLoad().LoadImage(mContext, imageBean.getIconsAddress(), imageView);

                    }
                }
            }
        }

    }


    public void setImage(ImageView imageView) {
        imageView.setVisibility(View.INVISIBLE);
        //ImageLoad.getImageLoad().LoadImage(mContext, colorId, imageView);
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
            String fileName = rightLft.get(0);
//            File imageFile = FileUtil.getIconFile(mContext,fileName);
//            CCLog.e("23123123",imageFile);
            setImage(productsViewHolder.product_right_bottom, fileName);
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
        void onItemClick(int position);

    }

}
