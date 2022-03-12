package com.hundsun.zjfae.activity.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.adapter.MaterialsAdapter;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnbindReasonAdapter extends RecyclerView.Adapter<UnbindReasonAdapter.UnbindReasonViewHolder> {

    private ItemOnClickListener listener;
    private LayoutInflater inflater;
    private List<UnbindCardInfo> cardInfoList;

    public UnbindReasonAdapter(Context context){
        cardInfoList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        String [] unbind_cardReason = context.getResources().getStringArray(R.array.unbind_cardReason);
        String [] unbind_reasonDetails = context.getResources().getStringArray(R.array.unbind_reasonDetails);
        for (int i = 0; i < unbind_cardReason.length; i++) {
            UnbindCardInfo cardInfo = new UnbindCardInfo();
            cardInfo.unbind_cardReason = unbind_cardReason[i];
            cardInfo.unbind_reasonDetails = unbind_reasonDetails[i];
            cardInfoList.add(cardInfo);
        }
    }


    public void setItemOnClickListener(ItemOnClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public UnbindReasonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.mater_layout_item,viewGroup,false);
        UnbindReasonViewHolder holder = new UnbindReasonViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UnbindReasonViewHolder unbindReasonViewHolder,final int i) {
        unbindReasonViewHolder.dynamicValue_name.setText(cardInfoList.get(i).unbind_reasonDetails);
        unbindReasonViewHolder.dynamicValue_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemClick(cardInfoList.get(i).unbind_cardReason,cardInfoList.get(i).unbind_reasonDetails);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardInfoList.size();
    }

    public class UnbindReasonViewHolder extends RecyclerView.ViewHolder{
        TextView dynamicValue_name;
        public UnbindReasonViewHolder(@NonNull View itemView) {
            super(itemView);
            dynamicValue_name = itemView.findViewById(R.id.dynamicValue_name);
            SupportDisplay.resetAllChildViewParam((ViewGroup) itemView);
        }
    }


    public interface ItemOnClickListener{

        void onItemClick(String unbind_cardReason,String unbind_reasonDetails);
    }



    private class UnbindCardInfo{



        public String unbind_cardReason;
        public String unbind_reasonDetails;

    }
}
