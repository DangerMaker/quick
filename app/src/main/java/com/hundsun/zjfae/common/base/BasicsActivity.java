package com.hundsun.zjfae.common.base;


import android.os.Bundle;
import android.support.annotation.Nullable;



public abstract class BasicsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }
}
