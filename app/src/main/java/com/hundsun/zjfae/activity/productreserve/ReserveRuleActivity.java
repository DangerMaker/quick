package com.hundsun.zjfae.activity.productreserve;

import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

/**
 * @Description:产品预约规则界面
 * @Author: zhoujianyu
 * @Time: 2018/10/23 15:45
 */
public class ReserveRuleActivity extends BasicsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reserve_rule;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.lin_reserve_rule);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("产品预约规则");
    }
}
