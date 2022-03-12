package com.hundsun.zjfae.activity.home;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.fragment.BaseFragment;

public class DescriptionFragment extends BaseFragment {

    private static DescriptionFragment fragment = null;

    private WebView contextWebViw;
    private String contentUrl;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void resetLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.description_fragment_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    public static DescriptionFragment newInstance(){
        if (fragment == null){
            fragment = new DescriptionFragment();
        }
        return fragment;
    }

    public DescriptionFragment setContent(String contentUrl){
        this.contentUrl = contentUrl;
        return this;
    }


    @Override
    protected void initWidget(View root) {
        contextWebViw = (WebView) findViewById(R.id.contentWebViw);
    }

    @Override
    public void initData() {
        contextWebViw.loadDataWithBaseURL(null, getHtmlData(contentUrl), "text/html", "UTF-8", null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.description_fragment_layout;
    }



}
