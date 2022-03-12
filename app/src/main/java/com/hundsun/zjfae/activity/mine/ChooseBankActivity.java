package com.hundsun.zjfae.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.AddBankListBankAdapter;
import com.hundsun.zjfae.activity.mine.bean.AddBanInfo;
import com.hundsun.zjfae.activity.mine.presenter.ChooseBankPresenter;
import com.hundsun.zjfae.activity.mine.view.ChooseBankView;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gens.QueryBankInfo;

public class ChooseBankActivity extends CommActivity<ChooseBankPresenter> implements ChooseBankView {


    private RecyclerView choose_recycler_view;
    private static final int  RESULT_CODE = 0x780;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_bank;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.bank_choose_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("选择银行");
        choose_recycler_view = findViewById(R.id.choose_recycler_view);
    }



    @Override
    public void initData() {

        presenter.getCheckBankType();
    }

    @Override
    protected ChooseBankPresenter createPresenter() {
        return new ChooseBankPresenter(this);
    }

    @Override
    public void onBindingBankBean(QueryBankInfo.Ret_PBIFE_bankcardmanage_queryBankInfo queryBankInfo) {

        if (queryBankInfo.getReturnCode().equals("0000")){
            List<QueryBankInfo.PBIFE_bankcardmanage_queryBankInfo.TcBankDitchList> ditchLists = queryBankInfo.getData().getTcBankDitchListList();

            AddBankListBankAdapter adapter = new AddBankListBankAdapter(this,ditchLists);
            choose_recycler_view.setLayoutManager(new LinearLayoutManager(this));
            choose_recycler_view.setAdapter(adapter);
            adapter.setBankListItemOnclick(new AddBankListBankAdapter.BankListItemOnclick() {
                @Override
                public void bankInfo(String bankName, String bankCodeNo) {
                    AddBanInfo banInfo = new AddBanInfo();
                    banInfo.setBankName(bankName);
                    banInfo.setBankCodeNo(bankCodeNo);
                    Intent data = new Intent();
                    Bundle dataBundle = new Bundle();
                    dataBundle.putParcelable("bankInfo",banInfo);
                    data.putExtra("bankInfoBundle",dataBundle);
                    setResult(RESULT_CODE,data);
                    finish();
                }
            });
        }
        else {
            showDialog(queryBankInfo.getReturnMsg());
        }
    }
}
