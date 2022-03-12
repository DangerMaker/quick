package com.hundsun.zjfae.fragment.home.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class PageAdapterViewHolder extends RecyclerView.ViewHolder{

    public ImageView icon_image;

    public PageAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        icon_image = itemView.findViewById(R.id.icon_image);
        icon_image.setScaleType(ImageView.ScaleType.FIT_XY);
        SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
    }
}
