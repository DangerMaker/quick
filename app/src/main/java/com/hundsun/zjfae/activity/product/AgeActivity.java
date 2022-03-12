package com.hundsun.zjfae.activity.product;

import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.presenter.AgePresenter;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;

/**     
  * @ProjectName:    浙金中心
  * @Package:        com.hundsun.zjfae.activity.product
  * @ClassName:      AgeActivity
  * @Description:     65周岁授权书
  * @Author:         moran
  * @CreateDate:     2019/6/10 12:14
  * @UpdateUser:     更新者：
  * @UpdateDate:     2019/6/10 12:14
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class AgeActivity extends CommActivity implements View.OnClickListener {
    private WebView content_webView;
    private CheckBox prompt;
    private AgePresenter presenter;


    @Override
    public void initData() {
        if (ADSharePre.getConfiguration(ADSharePre.highAge, BaseCacheBean.class) != null) {
            BaseCacheBean baseCacheBean = ADSharePre.getConfiguration(ADSharePre.highAge, BaseCacheBean.class);
            String title = baseCacheBean.getTitle();
            setTitle(title);
            String content = baseCacheBean.getContent();
            content_webView.loadDataWithBaseURL(null, getHtmlData(content), "text/html", "UTF-8", null);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.not_prompt:
                prompt.setChecked(!prompt.isChecked());
                break;
            case R.id.ok:
                if (prompt.isChecked()) {

                    presenter.ageRequest();
                }
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.finish:
                setResult(RESULT_CANCELED);
                finish();
                break;
                default:
                    break;
        }

    }



    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.age_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new AgePresenter(this);
    }

    @Override
    public void initView() {
        setNoBack();
        content_webView = findViewById(R.id.content_webView);
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.not_prompt).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        prompt = findViewById(R.id.prompt);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_age;
    }
}
