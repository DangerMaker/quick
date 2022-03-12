package com.hundsun.zjfae.activity.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.List;

public class NoSupportBankAdapter extends RecyclerView.Adapter<NoSupportBankAdapter.NoSupportBankViewHolder> {

    private List<String> supportListBank;
    private Context context;
    private LayoutInflater inflater;

    public NoSupportBankAdapter(Context context, List<String> supportListBank){
        inflater = LayoutInflater.from(context);
        this.supportListBank = supportListBank;
        this.context = context;
    }


    @NonNull
    @Override
    public NoSupportBankViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.support_bank_item_layout,viewGroup,false);
        NoSupportBankViewHolder viewHolder = new NoSupportBankViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoSupportBankViewHolder supportBankViewHolder, int i) {
        ImageLoad.getImageLoad().LoadImage(context,supportListBank.get(i),supportBankViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return supportListBank.size();
    }

    public class NoSupportBankViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        LinearLayout layout;
        public NoSupportBankViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bank_icon);
            layout = itemView.findViewById(R.id.support_item);
            SupportDisplay.resetAllChildViewParam(layout);
        }
    }
}
