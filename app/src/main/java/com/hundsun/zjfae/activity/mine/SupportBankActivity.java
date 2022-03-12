package com.hundsun.zjfae.activity.mine;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.NoSupportBankAdapter;
import com.hundsun.zjfae.activity.mine.adapter.SupportBankAdapter;
import com.hundsun.zjfae.activity.mine.presenter.SupportPresenter;
import com.hundsun.zjfae.activity.mine.view.SupportView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.GridDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.AllAzjProto;

public class SupportBankActivity extends CommActivity implements SupportView{

    private SupportPresenter presenter;
    private List<String> supportListBank;
    private List<String> noSupportListBank;
    private RecyclerView support_bank,no_support_bank;
    private SupportBankAdapter supportBankAdapter;
    private NoSupportBankAdapter noSupportBankAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_support_bank;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.support_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new SupportPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("可支持的银行");
        supportListBank = new ArrayList<>();
        noSupportListBank = new ArrayList<>();
        support_bank = findViewById(R.id.support_bank);
        no_support_bank = findViewById(R.id.no_support_bank);
        support_bank.setLayoutManager(new GridLayoutManager(this,3));
        no_support_bank.setLayoutManager(new GridLayoutManager(this,3));
    }

    @Override
    public void initData() {
        presenter.getSupportBankIcon();
    }

    @Override
    public void onSupportBankBean(AllAzjProto.PEARetBankPic retBankPic) {
        List<AllAzjProto.PBAPPBankPic> listBeans = retBankPic.getBankPicListList();
        for (AllAzjProto.PBAPPBankPic bankPicListBean: listBeans){
            if ( bankPicListBean.getBankIsRecommend().equals("1")){
                //推荐银行
                supportListBank.add(bankPicListBean.getBankIconUrl());
            }
            else if ( bankPicListBean.getBankIsRecommend().equals("0")){
                //其他银行
                noSupportListBank.add(bankPicListBean.getBankIconUrl());
            }
        }
        supportBankAdapter = new SupportBankAdapter(this,supportListBank);

        support_bank.setAdapter(supportBankAdapter);
        noSupportBankAdapter = new NoSupportBankAdapter(this,noSupportListBank);
        no_support_bank.setAdapter(noSupportBankAdapter);

    }
}
