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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.List;

import onight.zjfae.afront.gensazj.UnReadMes;

public class LoginIconAdapter extends RecyclerView.Adapter<LoginIconAdapter.TitleAdapterViewHolder> {

    private Context mContext;
    private List<BaseCacheBean> iconsListBeans;

    private ItemOnclickListener itemOnclickListener;

    private LayoutInflater inflater;

    private List<UnReadMes.PBAPP_unreadMes.UnreadMes> unreadMesList;

    public LoginIconAdapter(Context context, List<BaseCacheBean> iconsListBeans) {
        this.mContext = context;
        this.iconsListBeans = iconsListBeans;
        inflater = LayoutInflater.from(context);
    }


    public void setItemOnclickListener(ItemOnclickListener itemOnclickListener) {
        this.itemOnclickListener = itemOnclickListener;
    }


    public void refresh(List<BaseCacheBean> iconsListBeans){
        this.iconsListBeans = iconsListBeans;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public TitleAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.titleadapter_layout, viewGroup, false);

        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        params.width = getScreenWidth(mContext) / 4;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        rootView.setLayoutParams(params);
        TitleAdapterViewHolder viewHolder = new TitleAdapterViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TitleAdapterViewHolder titleAdapterViewHolder, final int i) {

        ImageLoad.getImageLoad().LoadImageWithSignature(mContext, iconsListBeans.get(i).getFuncIcons(), titleAdapterViewHolder.image_Icon, iconsListBeans.get(i).getResTime());
        titleAdapterViewHolder.icon_name.setText(iconsListBeans.get(i).getTitle());
        if (iconsListBeans.get(i).getIntervalSwitch().equals("1")){
            titleAdapterViewHolder.msg_count.setVisibility(View.VISIBLE);
            if (unreadMesList != null && !unreadMesList.isEmpty()){
                for (UnReadMes.PBAPP_unreadMes.UnreadMes unreadMes: unreadMesList){
                    if (iconsListBeans.get(i).getUuid().equals(unreadMes.getIconsuuid())){
                        if (!unreadMes.getMesCount().equals("0")){
                            if (unreadMes.getMesCount().length() > 2){
                                titleAdapterViewHolder.msg_count.setText("•••");
                            }
                            else {
                                titleAdapterViewHolder.msg_count.setText(unreadMes.getMesCount());
                            }
                        }
                        else {
                            titleAdapterViewHolder.msg_count.setVisibility(View.GONE);
                        }
                    }
                }
            }
            else {
                titleAdapterViewHolder.msg_count.setVisibility(View.GONE);
            }
        }
        else {
            titleAdapterViewHolder.msg_count.setVisibility(View.GONE);
        }

        titleAdapterViewHolder.icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnclickListener != null) {
                    itemOnclickListener.onItemClickListener(iconsListBeans.get(i));
                }



            }
        });
    }


    public void setUnreadMesList(List<UnReadMes.PBAPP_unreadMes.UnreadMes> unreadMesList) {
        this.unreadMesList = unreadMesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return iconsListBeans.size();
    }


    public class TitleAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView image_Icon;
        TextView icon_name,msg_count;
        RelativeLayout icon_layout;

        public TitleAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            image_Icon = itemView.findViewById(R.id.image_icon);
            icon_name = itemView.findViewById(R.id.icon_name);
            icon_layout = itemView.findViewById(R.id.icon_layout);
            msg_count = itemView.findViewById(R.id.msg_count);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }


    public interface ItemOnclickListener {
        void onItemClickListener(BaseCacheBean baseCacheBean);
    }



    private int getScreenWidth(Context context){

        DisplayMetrics displayMetrics = new DisplayMetrics();


        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(displayMetrics);


        return displayMetrics.widthPixels;

    }
}
