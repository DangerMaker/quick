package com.hundsun.zjfae.activity.mine.adapter;

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

import onight.zjfae.afront.gens.LoadTmbBankInfo;

public class BranchNameAdapter extends RecyclerView.Adapter<BranchNameAdapter.BranchNameViewHolder> {

    private Context context;
    private List<LoadTmbBankInfo.PBIFE_bankcardmanage_loadTmbBankInfo.TmbsubbankList> tmbsubbankList;
    private LayoutInflater inflater;
    private BranchNameItemClick itemClick;
    public BranchNameAdapter (Context context,List<LoadTmbBankInfo.PBIFE_bankcardmanage_loadTmbBankInfo.TmbsubbankList> tmbsubbankList){
        this.context = context;
        this.tmbsubbankList =tmbsubbankList;
        inflater = LayoutInflater.from(context);
    }

    public void setItemClick(BranchNameItemClick itemClick){
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public BranchNameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.province_item_layout,viewGroup,false);
        BranchNameViewHolder viewHolder = new BranchNameViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BranchNameViewHolder branchNameViewHolder,final int i) {
        branchNameViewHolder.branchName.setText(tmbsubbankList.get(i).getSbname());
        branchNameViewHolder.branchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClick != null){
                    itemClick.itemOncLick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tmbsubbankList.size();
    }

    public class BranchNameViewHolder extends RecyclerView.ViewHolder{
        TextView branchName;
        LinearLayout province_layout;
        public BranchNameViewHolder(@NonNull View itemView) {
            super(itemView);
            branchName = itemView.findViewById(R.id.province_name);
            province_layout = itemView.findViewById(R.id.province_layout);
            SupportDisplay.resetAllChildViewParam(province_layout);
        }
    }

    public interface BranchNameItemClick{
        void itemOncLick(int i);
    }
}
