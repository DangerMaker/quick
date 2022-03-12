package com.hundsun.zjfae.activity.productreserve;

import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

/**
 * @Description:个性化预约
 * @Author: zhoujianyu
 * @Time: 2018/9/17 15:42
 */
public class SpecialReserveActivity extends CommActivity {

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_special_reserve);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_special_reserve;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("个性化预约");
        findViewById(R.id.tv_rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(SpecialReserveActivity.this, SpecialReserveRuleActivity.class);
            }
        });
    }
}
