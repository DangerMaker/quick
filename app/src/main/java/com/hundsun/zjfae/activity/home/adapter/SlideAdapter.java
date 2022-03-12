package com.hundsun.zjfae.activity.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.bean.UpLoadPicImageBean;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.ViewHolder> {

    private List<UpLoadPicImageBean> loadPicImageBeanList;
    private Context context;
    private LayoutInflater inflater;
    private ItemDeleteClickListener itemDeleteClickListener;
    private ItemOnClickListener itemOnClickListener;

    private int selected = -1;

    private static Map<Integer,Integer> selectMap;

    private int isUploadPosition = -1;

    public SlideAdapter(Context context,List<UpLoadPicImageBean> loadPicImageBeanList){
        this.context = context;
        this.loadPicImageBeanList = loadPicImageBeanList;
        inflater = LayoutInflater.from(context);
        selectMap = new HashMap<>();
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener){
        this.itemOnClickListener = itemOnClickListener;
    }

    public void setItemDeleteClickListener(ItemDeleteClickListener itemDeleteClickListener){
        this.itemDeleteClickListener = itemDeleteClickListener;
    }

    public void setSelected(int selected){
        this.selected = selected;
        notifyDataSetChanged();
    }

    public void rest(List<UpLoadPicImageBean> loadPicImageBeanList){
        this.loadPicImageBeanList = loadPicImageBeanList;
        notifyDataSetChanged();
    }


    public void setIsSelect(int isSelect) {
        CCLog.e("isSelect",isSelect);
        selectMap.put(isSelect,isSelect);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.slide_adapter_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(rootView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {


        if (isUploadPosition == i){
            viewHolder.Pro_uploading.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.Pro_uploading.setVisibility(View.GONE);
        }




        if (selectMap != null &&!selectMap.isEmpty() && selectMap.get(i) != null && selectMap.get(i)== i){
            viewHolder.tv_select.setText("已上传");
        }
        else {
            viewHolder.tv_select.setText("已选择");
        }


        if (selected == 0){
            viewHolder.delete_image.setVisibility(View.VISIBLE);
            viewHolder.btnDelete.setVisibility(View.GONE);
        }

        else if (selected == -1){
            viewHolder.delete_image.setVisibility(View.GONE);
            viewHolder.btnDelete.setVisibility(View.GONE);
        }

        else if (selected == -2){
            viewHolder.delete_image.setVisibility(View.VISIBLE);
            viewHolder.btnDelete.setVisibility(View.GONE);
        }
        else {
            viewHolder.delete_image.setVisibility(View.GONE);
            viewHolder.btnDelete.setVisibility(View.GONE);
        }
        viewHolder.delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.btnDelete.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null){

                    if (loadPicImageBeanList.size() == 1){
                        itemDeleteClickListener.onItemOnly();
                    }
                    else {
                        itemDeleteClickListener.onItemDelete(i);
                    }


                }
            }
        });



        viewHolder.tv_content.setText(loadPicImageBeanList.get(i).getDynamicValue());

        viewHolder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null){



                    itemOnClickListener.onItemClick(i);
                }
            }
        });

    }





    public void setUploading( int isUploading){
        selected = -1;
        this.isUploadPosition = isUploading;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return loadPicImageBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView delete_image;
        TextView tv_content,tv_select;
        Button btnDelete;
        ProgressBar Pro_uploading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            delete_image = itemView.findViewById(R.id.delete_image);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_select = itemView.findViewById(R.id.tv_select);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            Pro_uploading = itemView.findViewById(R.id.Pro_uploading);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }



    public interface ItemDeleteClickListener{
        void onItemDelete(int i);

        void onItemOnly();
    }

    public interface ItemOnClickListener{
        void onItemClick(int i);
    }



    //ItemOnClickListener
}
