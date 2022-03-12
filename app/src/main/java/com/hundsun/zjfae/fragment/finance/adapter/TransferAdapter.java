package com.hundsun.zjfae.fragment.finance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.v4.TransferList;


/**
 * 工作行事历适配器
 */
public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {


    private List<TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList> productTradeInfoList;
    private Context context;
    private List<TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList.IconsList> iconsLists;
    public TransferAdapter(Context context,List<TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList> productTradeInfoList){
        this.context = context;
        this.productTradeInfoList = productTradeInfoList;
        iconsLists = new ArrayList<>();
    }

    public void refresh(List<TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList> productTradeInfoList){
        this.productTradeInfoList = productTradeInfoList;
        notifyDataSetChanged();

    }
    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int position);
    }


    private ItemClickCallBack clickCallBack;


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transfer_product, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        if (iconsLists != null && !iconsLists.isEmpty()){
            iconsLists.clear();
        }
        iconsLists.addAll( productTradeInfoList.get(position).getIconsListList());
        if (iconsLists.isEmpty()){
            //没有数据
            CCLog.e("非高净值","非高净值");
            viewHolder.type.setVisibility(View.GONE);
        }
        else {
            for (TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList.IconsList type : iconsLists){
                if (type.getIconsPosition().equals("right")){
                    String uuids = type.getUuid();
                    viewHolder.type.setVisibility(View.VISIBLE);
//                    ImageLoad.getImageLoad().LoadImage(context, FileUtil.getIconFile(context,uuids), viewHolder.type);
                    if(ADSharePre.getListConfiguration(ADSharePre.transferIcon, BaseCacheBean.class)==null){
                        return;
                    }
                    List<BaseCacheBean> imageList = ADSharePre.getListConfiguration(ADSharePre.transferIcon, BaseCacheBean.class);
                    if(imageList.size()==0){
                        return;
                    }
                    for (BaseCacheBean imageBean : imageList) {
                        if (uuids.equals(imageBean.getUuid())) {
                            if (StringUtils.isNotBlank(imageBean.getIconsAddress())) {
                                ImageLoad.getImageLoad().LoadImageWithSignatureAnimate(context, imageBean.getIconsAddress(), viewHolder.type, imageBean.getResTime());
                                viewHolder.type.setVisibility(View.VISIBLE);
                            } else {
                                viewHolder.type.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        }

        if (UserInfoSharePre.getTradeAccount().equals( productTradeInfoList.get(position).getTradeAccount())){
            viewHolder.my_entry.setVisibility(View.VISIBLE);
            //我的挂单
        }
        else {
            viewHolder.my_entry.setVisibility(View.GONE);
        }
        viewHolder.productName.setText(productTradeInfoList.get(position).getProductName());
        viewHolder.delegateAmount.setText(productTradeInfoList.get(position).getDelegateAmount());
        viewHolder.targetRate.setText(productTradeInfoList.get(position).getTargetRate()+"%");
        viewHolder.leastTranAmount.setText(productTradeInfoList.get(position).getLeastTranAmount());
        viewHolder.leftDays.setText(productTradeInfoList.get(position).getLeftDays());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.onItemClick(position);
                }

            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return productTradeInfoList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,targetRate,leastTranAmount,leftDays,delegateAmount;
        ImageView my_entry,type;
        public ViewHolder(View view) {
            super(view);
            productName = itemView.findViewById(R.id.productName);
            targetRate = itemView.findViewById(R.id.targetRate);
            leastTranAmount = itemView.findViewById(R.id.leastTranAmount);
            leftDays = itemView.findViewById(R.id.leftDays);
            delegateAmount = itemView.findViewById(R.id.delegateAmount);
            my_entry = itemView.findViewById(R.id.my_entry);
            type = itemView.findViewById(R.id.type);
            //SupportDisplay.resetAllChildViewParam((ViewGroup) view);
        }
    }
}





















