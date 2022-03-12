package com.hundsun.zjfae.activity.mine;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.BranchNameAdapter;
import com.hundsun.zjfae.activity.mine.adapter.CityAdapter;
import com.hundsun.zjfae.activity.mine.adapter.ProvinceAdapter;
import com.hundsun.zjfae.activity.mine.presenter.LoadProvincePresenter;
import com.hundsun.zjfae.activity.mine.view.LoadProvinceView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.view.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import onight.zjfae.afront.gens.LoadCity;
import onight.zjfae.afront.gens.LoadProvince;
import onight.zjfae.afront.gens.LoadTmbBankInfo;

public class LoadProvinceActivity extends CommActivity<LoadProvincePresenter> implements LoadProvinceView, View.OnClickListener {
    private ProvinceAdapter adapter;
    private DropDownMenu mDropDownMenu;
    private List<View> popupViews = new ArrayList<>();
    protected String headers[] = {"请选择省份", "请选择城市"};
    private View contentView;
    private CityAdapter cityAdapter;

    /****************contentView*******************/
    private EditText ed_branchName;
    private RecyclerView branchName_recycler;


    private RecyclerView province_recycler, city_recycler;


    private String bankCode = "", branchName = "", branchNo = "";

    //resultCode
    private static final int RESULTCODE = 0x1519;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_load_province;
    }

    @Override
    protected LoadProvincePresenter createPresenter() {
        return new LoadProvincePresenter(this);
    }

    @Override
    public void initView() {
        setTitle("支行选择");
        mTopDefineCancel = true;
        mDropDownMenu = findViewById(R.id.dropDownMenu);
        contentView = getLayoutInflater().inflate(R.layout.province_content_layout, null);

        ed_branchName = contentView.findViewById(R.id.ed_branchName);
        contentView.findViewById(R.id.branchName_button).setOnClickListener(this);
        branchName_recycler = contentView.findViewById(R.id.branchName_recycler);
        branchName_recycler.setLayoutManager(new LinearLayoutManager(this));
        branchName_recycler.addItemDecoration(new DividerItemDecorations());

        province_recycler = new RecyclerView(this);
        province_recycler.setLayoutManager(new LinearLayoutManager(this));
        province_recycler.addItemDecoration(new DividerItemDecorations());
        popupViews.add(province_recycler);
        city_recycler = new RecyclerView(this);
        city_recycler.setLayoutManager(new LinearLayoutManager(this));
        city_recycler.addItemDecoration(new DividerItemDecorations());
        popupViews.add(city_recycler);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
        mDropDownMenu.setOnItemMenuClickListener(new DropDownMenu.OnItemMenuClickListener() {
            @Override
            public void OnItemMenuClick(TextView tabView, int position) {
                if (position == 1 && mDropDownMenu.getTabText(0).equals("请选择省份")) {
                    showDialog("请先选择省份");
                    mDropDownMenu.closeMenu();
                }
            }
        });
    }

    @Override
    protected void topDefineCancel() {
        Intent intent = new Intent();
        intent.putExtra("branchName", branchName);
        intent.putExtra("branchNo", branchNo);
        setResult(RESULTCODE, intent);
        finish();
    }

    @Override
    public void initData() {
        bankCode = getIntent().getStringExtra("bankCode");
        branchName = getIntent().getStringExtra("branchNames");
        branchNo = getIntent().getStringExtra("branchNo");
        presenter.province();
        presenter.queryBranchName(bankCode, ed_branchName.getText().toString(), pno, cno);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.load_province_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    private String pno = "";

    @Override
    public void provinceList(LoadProvince.Ret_PBIFE_chinacity_loadProvince province) {
        final List<LoadProvince.PBIFE_chinacity_loadProvince.TmbprovList> tmbprovLists = province.getData().getTmbprovListList();
        CCLog.e(tmbprovLists.size());
        CCLog.e(tmbprovLists.get(0).getPname());
        adapter = new ProvinceAdapter(this, tmbprovLists);
        province_recycler.setAdapter(adapter);
        adapter.setProvinceNo(new ProvinceAdapter.ProvinceNo() {
            @Override
            public void provinceNumber(int position) {
                pno = tmbprovLists.get(position).getPno();
                if (!pno.equals(mDropDownMenu.getTabText(0))){
                    mDropDownMenu.setTabText(1,headers[1]);
                    cno = "";
                }

                mDropDownMenu.setTabText(tmbprovLists.get(position).getPname());
                mDropDownMenu.closeMenu();
                presenter.city(tmbprovLists.get(position).getPno());

            }
        });
    }

    private String cno = "";

    @Override
    public void cityList(LoadCity.Ret_PBIFE_chinacity_loadCity loadCity) {
        final List<LoadCity.PBIFE_chinacity_loadCity.TmbcityList> tmbcityList = loadCity.getData().getTmbcityListList();
        cityAdapter = new CityAdapter(this, tmbcityList);
        city_recycler.setAdapter(cityAdapter);
        cityAdapter.setCityNo(new CityAdapter.CityNo() {
            @Override
            public void cityNumber(int position) {

                mDropDownMenu.setTabText(tmbcityList.get(position).getCname());
                mDropDownMenu.closeMenu();
                cno = tmbcityList.get(position).getCno();


            }
        });
    }

    @Override
    public void branchNameList(LoadTmbBankInfo.Ret_PBIFE_bankcardmanage_loadTmbBankInfo loadTmbBankInfo) {
        CCLog.e(loadTmbBankInfo.getReturnMsg());
        final List<LoadTmbBankInfo.PBIFE_bankcardmanage_loadTmbBankInfo.TmbsubbankList> tmbsubbankList = loadTmbBankInfo.getData().getTmbsubbankListList();
        BranchNameAdapter adapter = new BranchNameAdapter(this, tmbsubbankList);
        branchName_recycler.setAdapter(adapter);
        adapter.setItemClick(new BranchNameAdapter.BranchNameItemClick() {
            @Override
            public void itemOncLick(int i) {
                Intent intent = new Intent();
                branchName = tmbsubbankList.get(i).getSbname();
                branchNo = tmbsubbankList.get(i).getSbno();
                intent.putExtra("branchName", branchName);
                intent.putExtra("branchNo", branchNo);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.branchName_button:
                presenter.queryBranchName(bankCode, ed_branchName.getText().toString(), pno, cno);
                break;
        }
    }
}
