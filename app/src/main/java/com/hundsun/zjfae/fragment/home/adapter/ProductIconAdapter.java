package com.hundsun.zjfae.fragment.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.List;

public class ProductIconAdapter extends RecyclerView.Adapter<ProductIconAdapter.ViewHolder> {

    private List<BaseCacheBean> iconsList;
    private Context context;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public ProductIconAdapter(Context context, List<BaseCacheBean> iconsList) {
        this.context = context;
        this.iconsList = iconsList;
        inflater = LayoutInflater.from(context);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.home_product_icon_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams layoutParams = viewHolder.icon_image.getLayoutParams();
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) viewHolder.icon_image.getLayoutParams();
        layoutParams.width = (dm.widthPixels - 50) / 4;
        layoutParams.height = (int) (((dm.widthPixels - 50) / 4) * 1.34);
        if (i == 0) {
            p.setMargins(10, 5, 5, 5);
        } else if (i == getItemCount() - 1) {
            p.setMargins(5, 5, 10, 5);
        } else {
            p.setMargins(5, 5, 5, 5);
        }
        viewHolder.icon_image.requestLayout();
        viewHolder.icon_image.setLayoutParams(layoutParams);
        CCLog.e("理财专区图片显示时间戳" + iconsList.get(i).getResTime());
        CCLog.e("理财专区图片显示地址" + iconsList.get(i).getIconsAddress());
        if ( iconsList.get(i).getFuncIcons().equals("")){
            viewHolder.icon_image.setVisibility(View.GONE);
        }
        else {
            viewHolder.icon_image.setVisibility(View.VISIBLE);
            ImageLoad.getImageLoad().LoadImageWithSignature(context, iconsList.get(i).getFuncIcons(), viewHolder.icon_image, iconsList.get(i).getResTime());
        }

        viewHolder.icon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(i);
                }
            }
        });
    }

    public void refresh(List<BaseCacheBean> iconsLists){
        this.iconsList = iconsLists;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return iconsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_image = itemView.findViewById(R.id.icon_image);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
