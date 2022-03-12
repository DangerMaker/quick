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

import onight.zjfae.afront.gens.QueryBankInfo;

public class AddBankListBankAdapter extends RecyclerView.Adapter<AddBankListBankAdapter.AddBankListViewHolder> {


    private Context context;

    private List<QueryBankInfo.PBIFE_bankcardmanage_queryBankInfo.TcBankDitchList> ditchLists;

    private LayoutInflater inflater;

    private BankListItemOnclick bankListItemOnclick;

    public AddBankListBankAdapter(Context context,List<QueryBankInfo.PBIFE_bankcardmanage_queryBankInfo.TcBankDitchList> ditchLists){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.ditchLists = ditchLists;
    }


    public void setBankListItemOnclick(BankListItemOnclick bankListItemOnclick) {
        this.bankListItemOnclick = bankListItemOnclick;
    }

    @NonNull
    @Override
    public AddBankListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.addbanklist_item_layout,viewGroup,false);
        AddBankListViewHolder viewHolder = new AddBankListViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddBankListViewHolder addBankListViewHolder, final int i) {

        addBankListViewHolder.bank_name.setText(ditchLists.get(i).getBankName());
        addBankListViewHolder.bank_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankListItemOnclick != null){
                    bankListItemOnclick.bankInfo(ditchLists.get(i).getBankName(),ditchLists.get(i).getBankCode());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ditchLists.size();
    }

    public class AddBankListViewHolder extends RecyclerView.ViewHolder{
        TextView bank_name;
        LinearLayout add_bank_layout,bank_item_layout;
        public AddBankListViewHolder(@NonNull View itemView) {
            super(itemView);
            bank_name = itemView.findViewById(R.id.bank_name);
            add_bank_layout = itemView.findViewById(R.id.add_bank_layout);
            bank_item_layout = itemView.findViewById(R.id.bank_item_layout);
            SupportDisplay.resetAllChildViewParam(add_bank_layout);
        }
    }

    public  interface BankListItemOnclick{
        void bankInfo (String bankName,String bankCodeNo);
    }
}
