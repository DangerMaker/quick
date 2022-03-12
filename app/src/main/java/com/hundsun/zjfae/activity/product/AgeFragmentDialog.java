package com.hundsun.zjfae.activity.product;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.view.dialog.BaseDialogFragment;


/**
  *  @ProjectName:
  * @Package:        com.hundsun.zjfae.activity.product
  * @ClassName:      AgeFragmentDialog
  * @Description:     65周岁授权协议弹框
  * @Author:         moran
  * @CreateDate:     2019/6/17 19:09
  * @UpdateUser:     更新者：
  * @UpdateDate:     2019/6/17 19:09
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class AgeFragmentDialog extends BaseDialogFragment implements View.OnClickListener,DialogInterface.OnKeyListener {

    private WebView content_webView;
    private CheckBox prompt;
    private TextView title_tv;

    private OnClickListener onClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void resetLayout() {
        SupportDisplay.resetAllChildViewParam((ViewGroup) mRootView);
    }


    @Override
    public void initView() {
        content_webView = findViewById(R.id.content_webView);
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.not_prompt).setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        prompt = findViewById(R.id.prompt);
        title_tv = findViewById(R.id.tv_commonn_title_text);

    }

    @Override
    protected void initData() {
        if (ADSharePre.getConfiguration(ADSharePre.highAge, BaseCacheBean.class) != null) {
            BaseCacheBean baseCacheBean = ADSharePre.getConfiguration(ADSharePre.highAge, BaseCacheBean.class);
            String title = baseCacheBean.getTitle();
            title_tv.setText(title);
            String content = baseCacheBean.getContent();
            content_webView.loadDataWithBaseURL(null, getHtmlData(content), "text/html", "UTF-8", null);
        }
    }


    /**
     * 加载html标签
     *
     * @param bodyHTML
     * @return
     */
    protected String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_age;
    }





    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.setOnKeyListener(this);
    }

    @Override
    protected boolean isCancel() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.not_prompt:
                prompt.setChecked(!prompt.isChecked());
                break;
            case R.id.ok:
                if (onClickListener != null){
                    dismissDialog();
                    onClickListener.isReadProtocol(prompt.isChecked());
                }
                break;
            case R.id.finish:
                if (onClickListener != null){
                    dismissDialog();
                    onClickListener.cancel();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (onClickListener != null){
                onClickListener.cancel();
            }
            return true;
        }

        return false;
    }

    @Override
    protected void baseInitLayoutParams() {
        if(dialog==null){
            dialog = getDialog();
        }
        Window window = dialog.getWindow();
        if(window!=null){
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = getDimAmount();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }

    public interface OnClickListener{

        /**
         * 阅读65周岁协议
         * @param isRead 是否阅读
         * */
        void isReadProtocol(boolean isRead);

        /**
         * 取消阅读
         * */
        void cancel();

    }


}
