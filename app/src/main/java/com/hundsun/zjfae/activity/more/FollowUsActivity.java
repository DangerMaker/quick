package com.hundsun.zjfae.activity.more;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

/**
 * @Description:关注我们界面
 * @Author: zhoujianyu
 * @Time: 2019/1/15 15:15
 */
public class FollowUsActivity extends BasicsActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow_us;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_follow_us);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("关注我们");
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", "浙金中心");
        cm.setPrimaryClip(clip);
    }
}
