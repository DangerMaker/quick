package com.hundsun.zjfae.activity.myinvitation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gensazj.RecommendDetailsInfoPB;

/**
 * @Description:推荐明细 Recyclerview列表适配器
 * @Author: zhoujianyu
 * @Time: 2018/9/12 10:02
 */
public class InvitationListAdapter extends RecyclerView.Adapter<InvitationListAdapter.ViewHolder> {
    private List<RecommendDetailsInfoPB.PBIFE_friendsrecommend_recommendDetailsInfo.RecommendDetailsList> mList;
    private Context mContext;

    public InvitationListAdapter(Context context, List<RecommendDetailsInfoPB.PBIFE_friendsrecommend_recommendDetailsInfo.RecommendDetailsList> list) {
        this.mList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_invitation_list, viewGroup, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder mViewHolder, final int position) {
        mViewHolder.tv_gmtRecommend.setText(mList.get(position).getGmtRecommend());
        mViewHolder.tv_frmobile.setText(mList.get(position).getFrmobile());
        if (mList.get(position).getIsBindCard() == null || mList.get(position).getIsBindCard().equals("")) {
            mViewHolder.tv_isBindCard.setText("未认证");
        } else {
            if (mList.get(position).getIsBindCard().equals("0")) {
                mViewHolder.tv_isBindCard.setText("未认证");
            } else if (mList.get(position).getIsBindCard().equals("1")) {
                mViewHolder.tv_isBindCard.setText("已认证");
            }
        }
        if (mList.get(position).getIsInvest() == null || mList.get(position).getIsInvest().equals("")) {
            mViewHolder.tv_isInvest.setText("未完成");
        } else {
            if (mList.get(position).getIsInvest().equals("0")) {
                mViewHolder.tv_isInvest.setText("未完成");
            } else if (mList.get(position).getIsInvest().equals("1")) {
                mViewHolder.tv_isInvest.setText("完成");
            }
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_layout;
        TextView tv_gmtRecommend, tv_frmobile, tv_isBindCard, tv_isInvest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_gmtRecommend = itemView.findViewById(R.id.tv_gmtRecommend);
            tv_frmobile = itemView.findViewById(R.id.tv_frmobile);
            tv_isBindCard = itemView.findViewById(R.id.tv_isBindCard);
            tv_isInvest = itemView.findViewById(R.id.tv_isInvest);
            SupportDisplay.resetAllChildViewParam(item_layout);

        }
    }
}
