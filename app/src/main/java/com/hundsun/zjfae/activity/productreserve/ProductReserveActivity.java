package com.hundsun.zjfae.activity.productreserve;

import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

/**
 * @Description:产品预约(我的账户点击产品预约进入)
 * @Author: zhoujianyu
 * @Time: 2018/9/12 9:15
 */
public class ProductReserveActivity extends BasicsActivity {


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_product_reserve);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_reserve;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("产品定制化预约");
        //我的个性化预约
        findViewById(R.id.lin_my_reserve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(ProductReserveActivity.this,MyReserveActivity.class);

            }
        });
        //马上预约点击
        findViewById(R.id.lin_go_reserve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(ProductReserveActivity.this,ReserveListActivity.class);
            }
        });
        //个性化预约
        findViewById(R.id.lin_special_reserve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(ProductReserveActivity.this,SpecialReserveActivity.class);
            }
        });
    }

}
