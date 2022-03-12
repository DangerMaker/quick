package com.hundsun.zjfae.activity.mine.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.adapter.BaseAdapter;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.ProductHighTransferOrderList;

public class ProductHighAdapter extends BaseAdapter<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList> {


    private static int colorId = android.R.color.white;

    public ProductHighAdapter(Context context, List<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList> list) {
        super(context, list);
    }

    @Override
    protected int getLayoutId(int position) {

        return R.layout.high_product_item_layout;
    }


    @Override
    protected void bindData(final ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList productTradeInfoList, final int position) {

        TextView productName = holder.getView(R.id.productName);

        TextView targetRate = holder.getView(R.id.targetRate);
        TextView leastTranAmount = holder.getView(R.id.leastTranAmount);
        TextView leftDays = holder.getView(R.id.leftDays);
        TextView delegateAmount = holder.getView(R.id.delegateAmount);

        productName.setText(productTradeInfoList.getProductName());

        delegateAmount.setText(productTradeInfoList.getDelegateAmount());
        targetRate.setText(productTradeInfoList.getTargetRate()+"%");
        leastTranAmount.setText(productTradeInfoList.getLeastTranAmount());
        leftDays.setText(productTradeInfoList.getLeftDays());

        productState(productTradeInfoList);

        setImage(productTradeInfoList,position);


        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListner != null){
                    onItemClickListner.onItemClickListener( holder.getRootView(),productTradeInfoList,position);
                }
            }
        });
    }





    //设置产品状态
    private void productState(ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList productTradeInfoList){
        String state = productTradeInfoList.getSellingStatus();

        ImageView selling_image = holder.getView(R.id.selling_image);
        //0-敬请期待，1-售卖中，2-已售罄，3-已结束
        switch (state){
            case "0":
                ImageLoad.getImageLoad().LoadImage(mContext,R.drawable.stay_tuned,selling_image);
                break;
            case "1":
                ImageLoad.getImageLoad().LoadImage(mContext,colorId,selling_image);
                break;
            case "2":
                ImageLoad.getImageLoad().LoadImage(mContext,R.drawable.sold_out,selling_image);
                break;
            case "3":
                ImageLoad.getImageLoad().LoadImage(mContext,R.drawable.ended,selling_image);
                break;
            default:
                ImageLoad.getImageLoad().LoadImage(mContext,colorId,selling_image);
                break;
        }
    }


    private void setImage(ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList productTradeInfoList,int position){

        List<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList.IconsList> iconsList
                = productTradeInfoList.getIconsListList();

        ImageView left_middle = holder.getView(R.id.left_middle);
        ImageView center_middle = holder.getView(R.id.center_middle);
        ImageView right_middle = holder.getView(R.id.right_middle);
        ImageView product_right_bottom = holder.getView(R.id.product_right_bottom);

        if (iconsList == null || iconsList.isEmpty()){
            CCLog.e("iconsList为空");
            setImage(left_middle);
            setImage(center_middle);
            setImage(right_middle);
            setImage(product_right_bottom);
        }

        else {
            List<String> middleList = new ArrayList<>();
            List<String> rightList = new ArrayList<>();

            for (int i = 0; i < iconsList.size(); i++) {
                String type = iconsList.get(i).getIconsPosition();
                CCLog.e(type);
                CCLog.e(iconsList.get(i).getUuid());
                if (type.equals("l_middle")){
                    middleList.add(iconsList.get(i).getUuid());
                }
                else if (type.equals("right_lower")){
                    rightList.add(iconsList.get(i).getUuid());

                }
            }
            setMiddleImage(middleList,position);
            setRightImage(rightList,position);
        }
    }

    public void setImage(ImageView imageView, int position, String fileName){
        File imageFile = FileUtil.getIconFile(mContext,fileName);

        if (!imageFile.exists()){
            imageView.setImageDrawable(null);
            imageView.setTag(imageView.getId(),position);
        }
        else {
            Object tag = imageView.getTag(imageView.getId());
            if (tag!=null&&(int) tag!= position) {
                imageView.setImageDrawable(null);
            }
            ImageLoad.getImageLoad().LoadImage(mContext,imageFile.getPath(),imageView);
            imageView.setTag(imageView.getId(),position);

        }

    }


    public void setImage(ImageView imageView){
        ImageLoad.getImageLoad().LoadImage(mContext,colorId,imageView);
    }





    //设置中间的角标
    private void setMiddleImage(List<String> middleList,  int position){
        ImageView left_middle = holder.getView(R.id.left_middle);
        ImageView center_middle = holder.getView(R.id.center_middle);
        ImageView right_middle = holder.getView(R.id.right_middle);
        //只有一张图
        if (middleList.size() == 1){
            String leftImage_path = middleList.get(0);
            setImage(left_middle,position,leftImage_path);
            setImage(center_middle);
            setImage(right_middle);
        }
        //只有两张图
        else if (middleList.size() == 2){
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            setImage(left_middle,position,leftImage_path);
            setImage(center_middle,position,centerImage_path);
            setImage(right_middle);
        }
        //三张图
        else if (middleList.size() == 3){
            String leftImage_path = middleList.get(0);
            String centerImage_path = middleList.get(1);
            String rightImage_path = middleList.get(2);
            setImage(left_middle,position,leftImage_path);
            setImage(center_middle,position,centerImage_path);
            setImage(right_middle,position,rightImage_path);
        }
        else {
            setImage(left_middle);
            setImage(center_middle);
            setImage(right_middle);
        }



    }


    //设置右下角角标
    private void setRightImage(List<String> rightLft,  int position){
        ImageView product_right_bottom = holder.getView(R.id.product_right_bottom);
        if (rightLft.size() == 1){
            String fileName = rightLft.get(0);
            File imageFile = FileUtil.getIconFile(mContext,fileName);
            setImage( product_right_bottom,position,fileName);
        }
        else {
            setImage(product_right_bottom);
        }


    }
}
