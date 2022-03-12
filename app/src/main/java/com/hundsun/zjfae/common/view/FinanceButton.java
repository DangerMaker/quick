package com.hundsun.zjfae.common.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;

public class FinanceButton extends FrameLayout {
    private Fragment mFragment = null;
    private Class<?> mClx;
    private TextView mTitleView;
    private String mTag;
    public FinanceButton( Context context) {
        super(context);
        init();
    }

    public FinanceButton( Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FinanceButton( Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.finance_title_layout, this, true);
        mTitleView = findViewById(R.id.title_view);
    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mTitleView.setSelected(selected);
    }
    public void init(@DrawableRes int resId, @StringRes int strId, Class<?> clx) {
        mTitleView.setBackgroundResource(resId);
        mTitleView.setText(strId);
        mClx = clx;
        mTag = mClx.getName();
    }

    public void init(@DrawableRes int resId, String str, Class<?> clx) {
        mTitleView.setBackgroundResource(resId);
        mTitleView.setText(str);
        mClx = clx;
        mTag = mClx.getName();
    }


    public Class<?> getClx() {
        return mClx;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    public void setmTag(String tag){
        this.mTag = tag;
    }

    public String getTag() {
        return mTag;
    }
}
