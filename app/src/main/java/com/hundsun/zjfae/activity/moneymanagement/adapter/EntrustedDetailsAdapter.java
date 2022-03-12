package com.hundsun.zjfae.activity.moneymanagement.adapter;

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

import onight.zjfae.afront.gens.EntrustedDetails;

public class EntrustedDetailsAdapter extends RecyclerView.Adapter<EntrustedDetailsAdapter.EntrustedDetailsViewHolder> {

    private Context context;
    private  List<EntrustedDetails.PBIFE_prdquery_getQueryEntrustedDetails.TcProductSeriesList> list;

    private OnItemListener onItemClickListener;

    public EntrustedDetailsAdapter(Context context, List<EntrustedDetails.PBIFE_prdquery_getQueryEntrustedDetails.TcProductSeriesList> list){
        this.context = context;
        this.list = list;
    }
    public void setOnItemClickListener(OnItemListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EntrustedDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View rootView = LayoutInflater.from(context).inflate(R.layout.attachment_adapter_layout,viewGroup,false);
        EntrustedDetailsViewHolder viewHolder = new EntrustedDetailsViewHolder(rootView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EntrustedDetailsViewHolder entrustedDetailsViewHolder,final int i) {
        entrustedDetailsViewHolder.attachment_tv.setText(list.get(i).getEntrustedReport());
        entrustedDetailsViewHolder.attach_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener){
                    onItemClickListener.onItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EntrustedDetailsViewHolder extends RecyclerView.ViewHolder{

        TextView attachment_tv;
        LinearLayout attach_layout;

        public EntrustedDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            attachment_tv = itemView.findViewById(R.id.attachment_tv);
            attach_layout = itemView.findViewById(R.id.attach_layout);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }


    public  interface OnItemListener{
        void onItemClick(int position);

    }
}
