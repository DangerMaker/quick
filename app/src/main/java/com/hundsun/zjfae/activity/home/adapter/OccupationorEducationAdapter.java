package com.hundsun.zjfae.activity.home.adapter;

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
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

/**
 * @Description:选择学历以及职业适配器
 * @Author: zhoujianyu
 * @Time: 2018/10/17 16:19
 */
public class OccupationorEducationAdapter extends RecyclerView.Adapter<OccupationorEducationAdapter.MessageViewHolder> {


    private List<String> listBeanList;
    private LayoutInflater inflater;
    private onItemClick mOnItemClick;
    private String data;

    public OccupationorEducationAdapter(Context context, List<String> listBeanList, onItemClick onItemClick, String data) {
        this.listBeanList = listBeanList;
        this.mOnItemClick = onItemClick;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.occupationoreducation_adapter_layout, viewGroup, false);
        MessageViewHolder viewHolder = new MessageViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder mViewHolder, final int i) {
        if(listBeanList.get(i).equals(data)){
            mViewHolder.image.setVisibility(View.VISIBLE);
        }else{
            mViewHolder.image.setVisibility(View.GONE);
        }
        mViewHolder.tv.setText(listBeanList.get(i));
        mViewHolder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClick.onItemClick(listBeanList.get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listBeanList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin;
        ImageView image;
        TextView tv;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            lin = itemView.findViewById(R.id.lin);
            image = itemView.findViewById(R.id.image);
            tv = itemView.findViewById(R.id.tv);
            SupportDisplay.resetAllChildViewParam(lin);
        }
    }

    public interface onItemClick {
        void onItemClick(String data);
    }

}
