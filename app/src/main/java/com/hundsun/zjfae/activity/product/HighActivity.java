package com.hundsun.zjfae.activity.product;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;

public class HighActivity extends BasicsActivity implements View.OnClickListener {

    private WebView content_webView;

    @Override
    public void initData() {

        if (ADSharePre.getConfiguration(ADSharePre.high, BaseCacheBean.class) != null) {
            BaseCacheBean baseCacheBean = ADSharePre.getConfiguration(ADSharePre.high, BaseCacheBean.class);
            String title = baseCacheBean.getTitle();
            setTitle(title);
            String content = baseCacheBean.getContent();
            content_webView.loadDataWithBaseURL(null, getHtmlData(content), "text/html", "UTF-8", null);

        }


    }

    @Override
    public void initView() {
        content_webView = findViewById(R.id.content_webView);
        findViewById(R.id.finish).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_high;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.high_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        content_webView.destroy();
    }

    /**
     * 加载html标签
     *
     * @param bodyHTML
     * @return
     */
    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
                finish();
                break;
        }
    }
}
