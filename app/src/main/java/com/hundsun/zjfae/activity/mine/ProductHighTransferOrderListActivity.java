package com.hundsun.zjfae.activity.mine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.bean.CardInfo;
import com.hundsun.zjfae.activity.mine.fragment.ProductHighFragment;
import com.hundsun.zjfae.activity.mine.fragment.TransferOrderHighFragment;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.FinanceButton;

import java.util.List;


public class ProductHighTransferOrderListActivity extends BasicsActivity implements View.OnClickListener {

    private FinanceButton nav_item_high_product,nav_item_high_transfer;

    private FragmentManager mFragmentManager;

    private FinanceButton mCurrentNavButton;


    private CardInfo cardInfo;


    @Override
    public void initView() {
        mFragmentManager = getSupportFragmentManager();
        nav_item_high_product = findViewById(R.id.nav_item_high_product);
        nav_item_high_product.setOnClickListener(this);
        nav_item_high_transfer = findViewById(R.id.nav_item_high_transfer);
        nav_item_high_transfer.setOnClickListener(this);
        initFragment();
    }

    @Override
    public void initData() {

        cardInfo = getIntent().getParcelableExtra("cardInfo");

        if (cardInfo != null){
            ProductHighFragment fragment = (ProductHighFragment) nav_item_high_product.getFragment();
            fragment.setCardInfo(cardInfo);
        }
    }

    private void initFragment(){

        nav_item_high_product.init(R.drawable.fince_title_product,"募集", ProductHighFragment.class);
        nav_item_high_transfer.init(R.drawable.fince_title_transfer,"转让", TransferOrderHighFragment.class);

        clearOldFragment();

        doSelect(nav_item_high_product);
    }




    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0)
            return;
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit)
            transaction.commitNow();
    }

    private void doSelect(FinanceButton newNavButton) {
        FinanceButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        mCurrentNavButton = newNavButton;
    }

    private void doTabChanged(FinanceButton oldNavButton, FinanceButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(this,
                        newNavButton.getClx().getName(), null);
                ft.add(R.id.container, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_high_transfer_order_list;
    }




    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.high_product_layout);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    public void onClick(View v) {
        if (v instanceof FinanceButton) {

            FinanceButton nav = (FinanceButton) v;
            if (nav != mCurrentNavButton) {
                doSelect(nav);

                Fragment fragment = nav.getFragment();

                if (fragment instanceof ProductHighFragment){
                    ProductHighFragment productHighFragment = (ProductHighFragment) fragment;
                    productHighFragment.initData();
                }
                else if (fragment instanceof TransferOrderHighFragment){

                    TransferOrderHighFragment transferOrderHighFragment = (TransferOrderHighFragment) fragment;
                    transferOrderHighFragment.initData();
                }

            }

        }
    }
}
