package com.hundsun.zjfae.activity.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gensazj.DictDynamics;

public class MaterialsAdapter extends RecyclerView.Adapter<MaterialsAdapter.ViewHolder> {


    private List<DictDynamics.PBAPP_dictDynamic.KeyAndValue> valueList;
    private LayoutInflater inflater;

    private ItemOnClickListener listener;


    public MaterialsAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }


    public void setValueList(List<DictDynamics.PBAPP_dictDynamic.KeyAndValue> valueList) {
        this.valueList = valueList;
        notifyDataSetChanged();
    }

    public MaterialsAdapter(Context context, List<DictDynamics.PBAPP_dictDynamic.KeyAndValue> valueList){
        this.valueList = valueList;
        inflater = LayoutInflater.from(context);
    }


    public void setItemOnClickListener(ItemOnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.mater_layout_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.dynamicValue_name.setText(valueList.get(i).getDynamicValue());
        viewHolder.dynamicValue_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemClick(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return valueList != null && !valueList.isEmpty()? valueList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView dynamicValue_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dynamicValue_name = itemView.findViewById(R.id.dynamicValue_name);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }

    public interface ItemOnClickListener{

        void onItemClick(int i);
    }

}
