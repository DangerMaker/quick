package com.hundsun.zjfae.activity.productreserve;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.productreserve.fragment.LongReserveListFragment;
import com.hundsun.zjfae.activity.productreserve.fragment.ShortReserveListFragment;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:长期预约与短期预约界面
 * @Author: zhoujianyu
 * @Time: 2018/9/17 10:15
 */
public class ReserveListActivity extends CommActivity implements View.OnClickListener {
    private LongReserveListFragment longReserveListFragment;
    private ShortReserveListFragment shortReserveListFragment;
    private List<Fragment> fragmentList = null;
    private RadioButton button_1, button_2;
    private FrameLayout frameLayout;

    @Override
    public void resetLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.reserve_list_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reserve_list;
    }

    @Override
    public void initView() {
        fragmentList = new ArrayList<>();
        longReserveListFragment = new LongReserveListFragment();
        fragmentList.add(longReserveListFragment);
        shortReserveListFragment = new ShortReserveListFragment();
        fragmentList.add(shortReserveListFragment);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        button_1 = (RadioButton) findViewById(R.id.button_1);
        button_2 = (RadioButton) findViewById(R.id.button_2);
        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        celectTop(0);

        findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //规则
        findViewById(R.id.tv_rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(ReserveListActivity.this, ReserveRuleActivity.class);
            }
        });
    }

    /**
     * 设置选中项
     */
    private void celectTop(int index) {
        switch (index) {
            case 0:
                button_1.setChecked(true);
                switchFragment(0);
                break;
            case 1:
                button_2.setChecked(true);
                switchFragment(1);
                break;
            default:
                break;
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_1:

                celectTop(0);
                break;
            case R.id.button_2:
                celectTop(1);
                break;

            default:
                break;
        }
    }

    private Fragment currentFragment;
    private Bundle args;//数据交互

    private void switchFragment(int whichFragment) {
        Fragment fragment = fragmentList.get(whichFragment);
        int frameLayoutId = frameLayout.getId();
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (fragment.isAdded()) {

                if (currentFragment != null) {

                    if (currentFragment == fragment) {

                        return;
                    } else {
                        transaction.hide(currentFragment).show(fragment);
                        currentFragment.onPause();
                        fragment.onResume();
                    }

                } else {

                    transaction.show(fragment);
                    fragment.onResume();
                }
            } else {

                if (currentFragment != null) {

                    transaction.hide(currentFragment).add(frameLayoutId, fragment);
                    currentFragment.onPause();
                } else {
                    transaction.add(frameLayoutId, fragment);
                }
            }
            fragment.setArguments(args != null ? args : null);
            currentFragment = fragment;
            transaction.commit();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
