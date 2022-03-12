package com.hundsun.zjfae.fragment.more;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.BuildConfig;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.List;

import onight.zjfae.afront.gensazj.UrlParams;

/**
 * @Description:更多列表适配器
 * @Author: zhoujianyu
 * @Time: 2019/1/15 10:53
 */
public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {
    private List<UrlParams.PBAPP_urlparams.listMore> mList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public MoreAdapter(Context context, List<UrlParams.PBAPP_urlparams.listMore> list) {
        this.mList = list;
        this.mContext = context;
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_morelist, viewGroup, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder mViewHolder, final int position) {
        ImageLoad.getImageLoad().LoadImage(mContext, mList.get(position).getIconImg(), mViewHolder.img_left);
        mViewHolder.tv_1.setText(mList.get(position).getListName());

        if (mList.get(position).getKeyWord().equals("TheCurrentVersion")) {//当前版本
            mViewHolder.img_right.setVisibility(View.INVISIBLE);
            mViewHolder.tv_2.setText(BuildConfig.VERSION_NAME);
            mViewHolder.tv_2.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.img_right.setVisibility(View.VISIBLE);
            mViewHolder.tv_2.setVisibility(View.GONE);
        }
        mViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });

        if(mList.get(position).getKeyWord().equals("FriendsShare") && UserInfoSharePre.getUserType().equals("company")){

            mViewHolder.item_layout.setVisibility(View.GONE);
        }
        else {
            mViewHolder.item_layout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_layout;
        TextView tv_1, tv_2;
        ImageView img_left, img_right;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_1 = itemView.findViewById(R.id.title);
            tv_2 = itemView.findViewById(R.id.tv_2);
            img_left = itemView.findViewById(R.id.img_left);
            img_right = itemView.findViewById(R.id.img_right);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

    }
}
