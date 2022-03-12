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

public class BankCardManageAdapter extends RecyclerView.Adapter<BankCardManageAdapter.BankCardManageViewHolder> {

    private List<onight.zjfae.afront.gens.LoadTcBindingBankInfo.PBIFE_bankcardmanage_loadTcBindingBankInfo.TcBindingBankInfoList> tcBindingBankInfoList;
    private Context context;
    private LayoutInflater layoutInflater;
    public BankCardManageAdapter(Context context, List<onight.zjfae.afront.gens.LoadTcBindingBankInfo.PBIFE_bankcardmanage_loadTcBindingBankInfo.TcBindingBankInfoList> tcBindingBankInfoList){
        this.context = context;
        this.tcBindingBankInfoList = tcBindingBankInfoList;
        layoutInflater = LayoutInflater.from(context);
    }



    @NonNull
    @Override
    public BankCardManageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = layoutInflater.inflate(R.layout.bank_card_item_layout,viewGroup,false);
        BankCardManageViewHolder holder = new BankCardManageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BankCardManageViewHolder bankCardManageViewHolder, int i) {

        bankCardManageViewHolder.gmtCreate.setText(tcBindingBankInfoList.get(i).getGmtCreate());
        bankCardManageViewHolder.bankName.setText(tcBindingBankInfoList.get(i).getBankName());
        bankCardManageViewHolder.bankCard.setText(tcBindingBankInfoList.get(i).getBankCard());
        if (tcBindingBankInfoList.get(i).getStatus().equals("fail")){
            bankCardManageViewHolder.status.setText("失败");
        }
        else {
            bankCardManageViewHolder.status.setText("成功");
        }

        if (tcBindingBankInfoList.get(i).getStatus().equals("unbind")){
            bankCardManageViewHolder.status_type.setText("解绑");
        }
        else if (tcBindingBankInfoList.get(i).getStatus().equals("modify")) {
            bankCardManageViewHolder.status_type.setText("修改签约信息");
        }
        else {
            bankCardManageViewHolder.status_type.setText("绑卡");
        }

        if(com.hundsun.zjfae.common.utils.StringUtils.isNotBlank(tcBindingBankInfoList.get(i).getRemark())) {
            bankCardManageViewHolder.remark.setText(tcBindingBankInfoList.get(i).getRemark());
        }else{
            bankCardManageViewHolder.remark.setText("--");
        }

    }

    @Override
    public int getItemCount() {
        return tcBindingBankInfoList.size();
    }

    public class BankCardManageViewHolder extends RecyclerView.ViewHolder{
        TextView gmtCreate,bankName,bankCard,status,status_type,remark;
        LinearLayout item_bank_layout;
        public BankCardManageViewHolder(@NonNull View itemView) {
            super(itemView);
            gmtCreate = itemView.findViewById(R.id.gmtCreate);
            bankName = itemView.findViewById(R.id.bankName);
            bankCard = itemView.findViewById(R.id.bankCard);
            status = itemView.findViewById(R.id.status);
            status_type = itemView.findViewById(R.id.status_type);
            remark = itemView.findViewById(R.id.remark);
            item_bank_layout = itemView.findViewById(R.id.item_bank_layout);
            SupportDisplay.resetAllChildViewParam(item_bank_layout);

        }
    }
}
