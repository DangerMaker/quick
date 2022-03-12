package com.hundsun.zjfae.activity.home;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.adapter.OccupationorEducationAdapter;
import com.hundsun.zjfae.activity.home.presenter.OccupationorEducationPresenter;
import com.hundsun.zjfae.activity.home.view.OccupationorEducationView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.CareerEnumTypeCom;

/**
 * @Description:职业与学历选择界面
 * @Author: zhoujianyu
 * @Time: 2018/10/17 16:12
 */
public class OccupationorEducationActivity extends CommActivity<OccupationorEducationPresenter> implements OccupationorEducationView {
    private RecyclerView mRecyclerView;//列表控件
    private List<String> list_education = new ArrayList<String>() {{
        add("高中及以下");
        add("大专");
        add("本科");
        add("硕士");
        add("博士");
        add("博士后");
    }};//学历
    private String type = "";//1为职业 2为学历
    private OccupationorEducationAdapter adapter;
    private String data = "";


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_occupationor_education);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_occupationor_education;
    }

    @Override
    protected void initData() {
        type = getIntent().getStringExtra("type");
        data = getIntent().getStringExtra("data");
        if (type.equals("1")) {
            setTitle("选择职业");
            presenter.onCareerEnumTypeCom();

        } else if (type.equals("2")) {
            setTitle("选择学历");
            adapter = new OccupationorEducationAdapter(this, list_education, new OccupationorEducationAdapter.onItemClick() {
                @Override
                public void onItemClick(String data) {
                    setResult(RESULT_OK, new Intent().putExtra("data", data));
                    finish();
                }
            }, data);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecorations());
    }

    @Override
    protected OccupationorEducationPresenter createPresenter() {
        return new OccupationorEducationPresenter(this);
    }

    @Override
    public void onCareerEnumTypeComList(List<CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData> enumDataList) {

        List<String> list_occupation = new ArrayList<>();

        for (CareerEnumTypeCom.PBIFE_userbaseinfo_getEnumTypeCom.EnumData enumData : enumDataList){
            list_occupation.add(enumData.getEnumName());
        }

        adapter = new OccupationorEducationAdapter(this, list_occupation, new OccupationorEducationAdapter.onItemClick() {
            @Override
            public void onItemClick(String data) {
                setResult(RESULT_OK, new Intent().putExtra("data", data));
                finish();
            }
        }, data);
        mRecyclerView.setAdapter(adapter);
    }
}
