package com.hundsun.zjfae.activity.mymessage;

import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

/**
 * @Description:我的消息详情
 * @Author: zhoujianyu
 * @Time: 2018/12/4 10:52
 */
public class MyMessageDetailActivity extends BasicsActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message_detail;
    }


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.lin_mymessage_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("我的消息");
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_content = findViewById(R.id.tv_content);
        tv_title.setText(getIntent().getStringExtra("title"));
        tv_content.setText(Html.fromHtml(getIntent().getStringExtra("content")));
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
