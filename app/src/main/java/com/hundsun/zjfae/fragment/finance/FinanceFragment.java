package com.hundsun.zjfae.fragment.finance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.cache.app.AppCache;
import com.hundsun.zjfae.common.view.FinanceButton;
import com.hundsun.zjfae.fragment.BaseFragment;

public class FinanceFragment extends BaseFragment implements View.OnClickListener {

    private FinanceButton product_button,transfer_button;


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_left:
                ((HomeActivity) mActivity).showMenu();
                break;
            case R.id.product_button:
                doSelect(product_button);
                selectNavigationButton(product_button);
                break;
            case R.id.transfer_button:
                doSelect(transfer_button);
                selectNavigationButton(transfer_button);
                break;
            default:
                break;
        }

    }


    private void selectNavigationButton(FinanceButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment != null && fragment instanceof ProductFragment){
            ProductFragment productFragment = (ProductFragment) fragment;
            productFragment.selectNavigation();
        }

        else if (fragment != null && fragment instanceof TransferFragment){
            TransferFragment transferFragment = (TransferFragment) fragment;
            transferFragment.initData();
        }
        else {
            showDialog("数据异常");
        }

    }

    @Override
    public void initData() {

        if (product_button == null|| !BaseActivity.isLogin){
            return;
        }
        TransferFragment transferFragment = (TransferFragment) transfer_button.getFragment();
        if (transferFragment != null){
            transferFragment.setNullKeywordName("");
        }

        if (HomeActivity.isTransferList){
            HomeActivity.isTransferList = false;
            doSelect(transfer_button);
            transferFragment.initData();

        }
        else {
            doSelect(product_button);
            ProductFragment  fragment = (ProductFragment) product_button.getFragment();
            fragment.initData();

        }






    }



    private FinanceButton mCurrentNavButton;
    @Override
    protected void initWidget() {
        findViewById(R.id.img_left).setOnClickListener(this);
        product_button = (FinanceButton) findViewById(R.id.product_button);
        product_button.setOnClickListener(this);

        transfer_button = (FinanceButton) findViewById(R.id.transfer_button);
        transfer_button.setOnClickListener(this);
        initFragment();
    }


    private void initFragment(){

        product_button.init(R.drawable.fince_title_product, AppCache.INSTANCE.getProductTitle(),ProductFragment.class);
        transfer_button.init(R.drawable.fince_title_transfer,R.string.transferTitle,TransferFragment.class);

        if (HomeActivity.isTransferList){
            doSelect(transfer_button);
        }
        else {
            doSelect(product_button);
        }

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
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mActivity,
                        newNavButton.getClx().getName(), null);
                ft.add(R.id.finance_container, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commitAllowingStateLoss();
    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.finance_fragment_layout;
    }

    @Override
    protected void resetLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.finance_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


}
