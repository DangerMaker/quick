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
import com.hundsun.zjfae.activity.product.bean.RadEnvelopeBean;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
  * @ProjectName:
  * @Package:        com.hundsun.zjfae.activity.product.adapter
  * @ClassName:      ChooseBaoAdapter
  * @Description:     红包适配器
  * @Author:         moran
  * @CreateDate:     2019/6/19 16:26
  * @UpdateUser:     更新者：
  * @UpdateDate:     2019/6/19 16:26
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class ChooseBaoAdapter extends RecyclerView.Adapter<ChooseBaoAdapter.ChooseBaoViewHolder>{


    private List<RadEnvelopeBean> baoList;

    private Context context;

    private LayoutInflater inflater;

    private static HashSet<String> isSelected;


    private String quanDetailsId;

    private ItemOnClick itemOnClick;

    private Map<Integer,Boolean> booleanMap;

    private Map<Integer ,Integer> oldHeightMap;
    public ChooseBaoAdapter(Context context,List<RadEnvelopeBean> baoList){
        this.context = context;
        this.baoList = baoList;
        inflater = LayoutInflater.from(context);
        init();
    }
    public void setItemOnClick(ItemOnClick itemOnClick){
        this.itemOnClick = itemOnClick;
    }
    private void init(){
        isSelected = new HashSet<>();
        oldHeightMap = new HashMap<>();
        booleanMap = new HashMap<>();

    }

    public void setQuanDetailsId(String quanDetailsId) {
        isSelected.add(quanDetailsId);
        notifyDataSetChanged();
    }

    public boolean getSelectedType(String quanDetailsId ){

        return isSelected.contains(quanDetailsId);
    }

    public void  removeQuanDetailsId(String quanDetailsId){
        isSelected.remove(quanDetailsId);
    }

    @NonNull
    @Override
    public ChooseBaoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View rootView = inflater.inflate(R.layout.bao_item_layout,viewGroup,false);
        ChooseBaoViewHolder viewHolder = new ChooseBaoViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChooseBaoViewHolder chooseBaoViewHolder, final int position) {

        chooseBaoViewHolder.quanName.setText(baoList.get(position).getQuanName());
        chooseBaoViewHolder.quanValue.setText(baoList.get(position).getQuanValue());
        String splitRemark = baoList.get(position).getQuanCatalogRemark();
        StringBuffer buffer  = new StringBuffer();


        //是否可叠加,String,不校验,是,0-不可叠加;1-可叠加
        if (baoList.get(position).getQuanCanStack().equals("0")){
            buffer.append("◇不可叠加").append("\n");
        }
        else if (baoList.get(position).getQuanCanStack().equals("1")){
            buffer.append("◇可叠加").append("\n");
        }

        if (!splitRemark.equals("")) {
            String [] remark = splitRemark.split("\\n");
            if (remark.length > 1){
                for (int i = 0; i < remark.length; i++) {
                    buffer.append("◇").append(remark[i]).append("\n");
                }
            }
            else {
                buffer.append("◇").append(splitRemark.replaceAll("\\n","").trim()).append("\n");
            }

        }

        buffer.append("◇有效期至:").append(baoList.get(position).getQuanValidityEnd()).append("\n");
        chooseBaoViewHolder.quanDetailsCode.setText("NO."+baoList.get(position).getQuanDetailsId());
        chooseBaoViewHolder.quanCanStack.setText(buffer.toString());
        CCLog.e(buffer);


        if (getSelectedType(baoList.get(position).getQuanDetailsId())){
            ImageLoad.getImageLoad().LoadImage(context,R.drawable.account_checked, chooseBaoViewHolder.selectedType);
        }
        else {
            ImageLoad.getImageLoad().LoadImage(context,R.drawable.account_normal, chooseBaoViewHolder.selectedType);

        }
        if (itemOnClick != null){
            chooseBaoViewHolder.bao_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClick.onItemClick(position);
                }
            });
        }


        chooseBaoViewHolder.more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!oldHeightMap.containsKey(position)){
                    oldHeightMap.put(position,chooseBaoViewHolder.quanCanStack.getHeight());
                }

                if (!booleanMap.containsKey(position)){
                    booleanMap.put(position,false);
                }



                if (!booleanMap.get(position)){

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    chooseBaoViewHolder.quanCanStack.setLayoutParams(params);
                    chooseBaoViewHolder.more_tv.setText("收起");
                    ImageLoad.getImageLoad().LoadImage(context,R.drawable.red_refresh,chooseBaoViewHolder.more_iv);

                    booleanMap.put(position,true);
                }
                else {
                    booleanMap.put(position,false);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,oldHeightMap.get(position));
                    chooseBaoViewHolder.quanCanStack.setLayoutParams(params);
                    chooseBaoViewHolder.more_tv.setText("查看全部");
                    ImageLoad.getImageLoad().LoadImage(context,R.drawable.red_more,chooseBaoViewHolder.more_iv);
                }
            }
        });

    }





    @Override
    public int getItemCount() {
        return baoList.size();
    }

    public class ChooseBaoViewHolder extends RecyclerView.ViewHolder{
        LinearLayout content_layout, bao_item_layout,more_layout;
        TextView quanValue,quanName,quanDetailsCode,quanCanStack,more_tv;
        ImageView selectedType,more_iv;
        public ChooseBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            bao_item_layout = itemView.findViewById(R.id.bao_item_layout);
            quanValue = itemView.findViewById(R.id.quanValue);
            quanName = itemView.findViewById(R.id.quanName);
            quanDetailsCode = itemView.findViewById(R.id.quanDetailsCode);
            quanCanStack = itemView.findViewById(R.id.quanCanStack);
            selectedType = itemView.findViewById(R.id.selectedType);

            content_layout = itemView.findViewById(R.id.content_layout);
            more_layout = itemView.findViewById(R.id.more_layout);
            more_tv = itemView.findViewById(R.id.more_tv);
            more_iv = itemView.findViewById(R.id.more_iv);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }
    public interface ItemOnClick{
        void onItemClick( int position);
    }



}
