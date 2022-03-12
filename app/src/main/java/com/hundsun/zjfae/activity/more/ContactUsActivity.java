package com.hundsun.zjfae.activity.more;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

/**
 * @Description:联系我们
 * @Author: zhoujianyu
 * @Time: 2018/9/18 13:46
 */
public class ContactUsActivity extends BasicsActivity {
    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_contact_my);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_us;
    }


    @Override
    public void initView() {
        super.initView();
        setTitle("联系我们");
        findViewById(R.id.lin_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4009999000"));
                startActivity(dialIntent);
            }
        });
    }
}
