package com.hundsun.zjfae.activity.home;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.presenter.DescriptionPresenter;
import com.hundsun.zjfae.activity.home.view.DescriptionView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gensazj.DictDynamics;

/*详细说明*/
public class DescriptionActivity extends CommActivity implements DescriptionView {

    private TabLayout title_tabLayout;

    private ViewPager content_viewPager;

    private DescriptionPresenter presenter;

    private List<Fragment> fragmentList;
    private List<String> tabList;

    @Override
    public void initData() {
        presenter.dictDynamicState();
    }

    @Override
    public void dictState(DictDynamics.Ret_PBAPP_dictDynamic dictDynamicState) {
        List<DictDynamics.PBAPP_dictDynamic.DictDynamic> dynamicList = dictDynamicState.getData().getDictDynamicListList();

        for (int i = 0; i < dynamicList.size(); i++) {
            CCLog.e("title",dynamicList.get(i).getTitle());
            tabList.add(dynamicList.get(i).getTitle());
            title_tabLayout.addTab(title_tabLayout.newTab().setText(tabList.get(i)));
            fragmentList.add(new DescriptionFragment().setContent(dynamicList.get(i).getRemark()));
        }
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList,tabList);
        content_viewPager.setAdapter(adapter);
        title_tabLayout.setupWithViewPager(content_viewPager);
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter{

        private List<Fragment> fragments;
        private List<String> tabList;
        public FragmentAdapter(FragmentManager fm,List<Fragment> fragments,List<String> tabList) {
            super(fm);
            this.fragments = fragments;
            this.tabList = tabList;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }
    }








    @Override
    protected int getLayoutId() {
        return R.layout.activity_description;
    }

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new DescriptionPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.description_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("详细说明");
        title_tabLayout = findViewById(R.id.title_tabLayout);
        content_viewPager = findViewById(R.id.content_viewPager);
        fragmentList = new ArrayList<>();
        tabList = new ArrayList<>();
    }
}
