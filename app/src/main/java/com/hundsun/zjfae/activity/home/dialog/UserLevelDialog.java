package com.hundsun.zjfae.activity.home.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.view.dialog.BaseDialogFragment;


/**
 * 合格投资者弹框
 *
 * @author moran
 */
public class UserLevelDialog implements View.OnClickListener {


    private WebView mWebView;
    private TextView notice_title, cancel_tv;
    private Button data_up, approve_up;
    private LinearLayout becomeInvestor_layout, ll_layout;
    private Button becomeInvestor_button, bt_close;

    private OnClickListener onClickListener;

    private String content = "", title = "", data = "", type = "", isRealInvestor = "";

    /**
     * 0线上，1线下, 2不弹出
     */
    private String isShow = "";

    private Context context;

    private Dialog dialog = null;

    private View rootView = null;

    public UserLevelDialog(@NonNull Context context) {

        this.context = context;

        dialog = new Dialog(context, R.style.mystyle);

        rootView = LayoutInflater.from(context).inflate(getLayoutId(), null);


        initView();
    }


    public void initView() {
        mWebView = rootView.findViewById(R.id.notice_webView);
        notice_title = rootView.findViewById(R.id.notice_title);
        data_up = rootView.findViewById(R.id.data_up);
        data_up.setOnClickListener(this);
        approve_up = rootView.findViewById(R.id.approve_up);
        approve_up.setOnClickListener(this);
        cancel_tv = rootView.findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(this);
        rootView.findViewById(R.id.cancel_img).setOnClickListener(this);
        ll_layout = rootView.findViewById(R.id.ll_layout);
        becomeInvestor_button = rootView.findViewById(R.id.becomeInvestor_button);
        becomeInvestor_button.setOnClickListener(this);
        becomeInvestor_layout = rootView.findViewById(R.id.becomeInvestor_layout);
        bt_close = rootView.findViewById(R.id.bt_close);
        bt_close.setOnClickListener(this);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        WebSettings webSetting = mWebView.getSettings();
        //支持JS，此项必不可少
        webSetting.setJavaScriptEnabled(true);
        //1.网上说是设置此选项提高渲染的优先级，
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //2.首先阻塞图片，让图片不显示
        webSetting.setBlockNetworkImage(true);
        //3.页面加载好以后，在放开图片：
        webSetting.setBlockNetworkImage(false);
        // 设置缓存模式（下面会详细介绍缓存）
        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowFileAccess(true);
        //开启 database storage API 功能
        webSetting.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        webSetting.setAppCacheEnabled(true);
        //此设置是否保存H5表单数据，发现一个蛋疼的问题，在小米手机上当H5 input框设置为search后，当点击input框的时候//竟然会有历史的搜索记录，而且样式十分难看，设置此属性可以取消历史搜索记录
        webSetting.setSaveFormData(false);

    }

    public int getLayoutId() {
        return R.layout.activity_eligible_invest_guide;
    }


    public Dialog createDialog() {

        Window window = dialog.getWindow();
        window.getDecorView().setPadding(15, 0, 15, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1f;
        lp.gravity = Gravity.CENTER;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.setCancelable(isCancel());
//        dialog.addContentView(rootView, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        dialog.setContentView(rootView);

        initData();

        return dialog;


    }


    public void onDmiss() {
        dialog.dismiss();
    }


    protected void initData() {
        ll_layout.setVisibility(View.VISIBLE);
        cancel_tv.setVisibility(View.VISIBLE);
        bt_close.setVisibility(View.GONE);
        if (isShow.equals("1")) {
            ll_layout.setVisibility(View.GONE);
            cancel_tv.setVisibility(View.GONE);
            bt_close.setVisibility(View.VISIBLE);
        }

        notice_title.setText(title);
        data_up.setText(content);

        if (type.equals("004")) {
            //恭喜成为合格投资者弹框
            becomeInvestor_layout.setVisibility(View.GONE);
            becomeInvestor_button.setVisibility(View.VISIBLE);
        } else if (type.equals("020")) {
            bt_close.setVisibility(View.VISIBLE);
            becomeInvestor_layout.setVisibility(View.GONE);
        }




        mWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);

    }


    protected boolean isCancel() {

        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //材料上传
            case R.id.data_up:
                if (onClickListener != null) {
                    onClickListener.onMaterial(isRealInvestor);
                }
                break;
            //充值
            case R.id.approve_up:

                if (onClickListener != null) {
                    onClickListener.onRecharge();
                }
                break;

            case R.id.cancel_tv:
            case R.id.cancel_img:
            case R.id.becomeInvestor_button:
            case R.id.bt_close:
                onDmiss();
                break;

            default:
                break;

        }

    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {

        /**
         * 材料上传
         *
         * @param isRealInvestor 是否是合格投资者
         */
        void onMaterial(String isRealInvestor);

        /**
         * 充值
         */
        void onRecharge();

    }


    public void setType(String type) {

        this.type = type;

    }


    public void loadDataWithBaseURL(String data) {
        this.data = data;

    }


    public void setNotice_title(String title) {
        this.title = title;

    }

    public void setDataUpContent(String content) {
        this.content = content;

    }


    public void setIsRealInvestor(String isRealInvestor) {
        this.isRealInvestor = isRealInvestor;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
