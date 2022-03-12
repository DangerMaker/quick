package com.hundsun.zjfae.common.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hundsun.zjfae.BuildConfig;
import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.WebActivity;
import com.hundsun.zjfae.activity.home.X5WebActivity;
import com.hundsun.zjfae.activity.mymessage.MyMessageDetailActivity;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
import com.hundsun.zjfae.common.http.api.CustomProgressFragmentDialog;
import com.hundsun.zjfae.common.http.cookies.CookieParcelable;
import com.hundsun.zjfae.common.http.cookies.PersistentCookieStore;
import com.hundsun.zjfae.common.user.UserInfo;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StatusBarUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.view.NavigationButton;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;
import com.hundsun.zjfae.fragment.finance.bean.ProductDate;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.zjfae.jpush.Extras;
import com.zjfae.jpush.JPush;
import com.zjfae.jpush.OpenJPushMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;


/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.common.base
 * @ClassName: BaseActivity
 * @Description: Acticity基类
 * @Author: moran
 * @CreateDate: 2019/6/10 13:44
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/10 13:44
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {


    public static boolean isLogin = false;

    public static boolean upDateShow = true;

    public static boolean noticeShow = true;

    public static boolean isRegister = false;

    /**
     * 是否使用自定义返回键
     */
    protected boolean mTopDefineCancel = false;


    public static final int WEB_ACTIVITY_REQUEST_CODE = 0x9213;

    protected BaseApplication mBaseApplication;

    protected TextView mTvTopTitle;

    /**
     * 顶部返回按钮
     */
    protected LinearLayout mLlTopTitleBack;

    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateConfiguration();
        setContentView(getLayoutId());
        mBaseApplication = (BaseApplication) this.getApplication();
        mBaseApplication.add(this);
        initJpush(mBaseApplication);
        setStatusBar();
        SupportDisplay.initLayoutSetParams(this);
        getBackLayoutId();
        resetLayout();
        saveBundleData(savedInstanceState);


        List<View> viewList = new ArrayList<>();
        View rootView = this.getWindow().getDecorView();

        viewList =  getAllChildViews(rootView);


        Log.i("CLASS_NAME", this.getClass().getSimpleName());

    }




    private List<View> getAllChildViews (View rootView){

        List<View> allChild = new ArrayList<>();
        if (rootView instanceof ViewGroup){

            ViewGroup group = (ViewGroup) rootView;

            for (int i = 0; i < group.getChildCount(); i++) {
                View childView = group.getChildAt(i);
                allChild.add(childView);
                allChild.addAll(getAllChildViews(childView));
                CCLog.e(this.getClass().getSimpleName()+"childView",childView);
            }
        }

        return allChild;

    }


    private void saveBundleData(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            List<CookieParcelable> listCookies = savedInstanceState.getParcelableArrayList("cookies");
            List<Cookie> cookieList = new ArrayList<>();
            Cookie.Builder baseBuild = new Cookie.Builder();
            for (CookieParcelable cookie : listCookies) {
                baseBuild.domain(cookie.getDomain());
                baseBuild.name(cookie.getName());
                baseBuild.value(cookie.getValue());
                baseBuild.path(cookie.getPath());
                cookieList.add(baseBuild.build());
            }
            PersistentCookieStore.getCookieStore().addCookie(cookieList);
            isLogin = savedInstanceState.getBoolean("loginState");
            upDateShow = savedInstanceState.getBoolean("upDateShow");
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<CookieParcelable> listCookies = new ArrayList<>();
        List<Cookie> cookies = PersistentCookieStore.getCookieStore().getCookies();
        for (Cookie cookie : cookies) {
            CookieParcelable cookieParcelable = new CookieParcelable();
            cookieParcelable.setDomain(cookie.domain());
            cookieParcelable.setName(cookie.name());
            cookieParcelable.setValue(cookie.value());
            cookieParcelable.setPath(cookie.path());
            listCookies.add(cookieParcelable);
        }
        outState.putParcelableArrayList("cookies", (ArrayList<? extends Parcelable>) listCookies);
        outState.putBoolean("loginState", isLogin);
        outState.putBoolean("upDateShow", upDateShow);
    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected void setStatusBar() {

        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
    }

    /**
     * 自定义标题栏返回操作
     */
    protected void topDefineCancel() {

    }


    /**
     * 启动WebActivity
     **/
    public void startWebActivity(Intent intent) {
        if (isLogin) {
            startActivityForResult(intent, WEB_ACTIVITY_REQUEST_CODE);
        } else {
            baseStartActivity(intent);
        }
    }

    /**
     * 启动WebActivity
     **/
    public void startWebActivity(String funcUrl) {
        ShareBean shareBean = new ShareBean();
        shareBean.setFuncUrl(funcUrl);
        startWebActivity(shareBean);
    }

    /**
     * 启动WebActivity
     **/
    public void startWebActivity(ShareBean shareBean) {
        Intent intent = new Intent();

        if (StringUtils.isNotBlank(UserInfoSharePre.getSkip()) && UserInfoSharePre.getSkip().equals(UserInfo.SKIPTENCENT)) {

            CCLog.e("信息","X5");
            intent.setClass(this, X5WebActivity.class);
        } else {
            CCLog.e("信息","原生");
            intent.setClass(this, WebActivity.class);
        }
        intent.putExtra("shareBean", shareBean);

        startWebActivity(intent);

    }


    private String title = "";
    /**
     * 设置标题字
     *
     * @param title_str
     */
    protected void setTitle(String title_str) {
        this.title = title_str;
        if (findViewById(R.id.tv_commonn_title_text) != null) {
            mTvTopTitle = (TextView) findViewById(R.id.tv_commonn_title_text);
            mTvTopTitle.setText(title_str);
        }

    }

    protected String getTitles(){

        if (title.equals("")){

            title = getResources().getString(R.string.app_name);
        }


        return title;
    }


    /**
     * 设置没有返回键
     */
    protected void setNoBack() {
        if (findViewById(R.id.ll_commonn_title_back) != null) {
            mLlTopTitleBack = (LinearLayout) findViewById(R.id.ll_commonn_title_back);
            mLlTopTitleBack.setVisibility(View.GONE);
        }
    }

    private void getBackLayoutId() {
        if (findViewById(R.id.rl_commonn_title_menu_) != null) {
            mLlTopTitleBack = (LinearLayout) findViewById(R.id.ll_commonn_title_back);
            mTvTopTitle = (TextView) findViewById(R.id.tv_commonn_title_text);
            mLlTopTitleBack.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (!mTopDefineCancel) {
                        BaseActivity.this.finish();
                    } else {
                        topDefineCancel();
                    }

                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        SupportDisplay.initLayoutSetParams(BaseActivity.this);

        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        SupportDisplay.initLayoutSetParams(BaseActivity.this);
        super.onResume();
    }


    protected Map<String, NavigationButton> fragmentMap;


    public void addFragment(NavigationButton navigationButton) {
        if (fragmentMap == null) {
            fragmentMap = new HashMap<>();
        }
        if (navigationButton != null) {
            boolean isAdd = fragmentMap.containsValue(navigationButton);
            if (!isAdd) {
                fragmentMap.put(navigationButton.getClx().getName(), navigationButton);
            }
        }
    }


    public void baseStartActivity(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        baseStartActivity(intent);
    }

    public void baseStartActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }


    //安全退出
    public void outLogin(Activity activity) {
        isLogin = false;
        PersistentCookieStore.getCookieStore().cleanCookie();
        HomeActivity.show(activity, HomeActivity.HomeFragmentType.HOME_FRAGMENT);
    }


    /**
     * @param s
     */
    public void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(this,
                    s,
                    Toast.LENGTH_SHORT);
        } else {
            try {
                toast.setText(s);
            } catch (Exception e) {
                toast = Toast.makeText(this,
                        s,
                        Toast.LENGTH_SHORT);
            }
        }
        toast.show();
    }


    private CustomProgressFragmentDialog.Builder builder = null;

    public void showErrorDialog(String msg) {


        if (builder == null) {
            builder = new CustomProgressFragmentDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage(msg);
            builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setMessage(msg);
        }


        builder.create().show();
    }


    /**
     * @param msg dialog提示信息
     **/
    public void showDialog(String msg) {

        if (msg == null) {
            return;

        }
        CustomDialog.Builder builder = new CustomDialog.Builder(BaseActivity.this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * @param msg             dialog提示信息
     * @param confirm_btnText dialog 确定button文字
     **/
    public void showDialog(String msg, String confirm_btnText, DialogInterface.OnClickListener listener) {

        if (msg == null) {
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(BaseActivity.this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton(confirm_btnText, listener);
        builder.create().show();
    }


    /**
     * @param msg            dialog提示信息
     * @param cancel_btnText dialog 取消button文字
     **/
    public void showDialog(String msg, String cancel_btnText) {

        if (msg == null) {
            return;

        }
        CustomDialog.Builder builder = new CustomDialog.Builder(BaseActivity.this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setNegativeButton(cancel_btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * @param msg             dialog提示信息
     * @param confirm_btnText dialog 确定button文字
     * @param cancel_btnText  dialog 取消button文字
     **/
    public void showDialog(String msg, String confirm_btnText, String cancel_btnText) {

        if (msg == null) {
            return;

        }
        CustomDialog.Builder builder = new CustomDialog.Builder(BaseActivity.this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton(confirm_btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(cancel_btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * @param msg                 dialog提示信息
     * @param confirm_btnText     dialog 确定button文字
     * @param cancel_btnText      dialog 取消button文字
     * @param postDialogInterface 确定button回调
     **/
    public void showDialog(String msg, String confirm_btnText, String cancel_btnText, DialogInterface.OnClickListener postDialogInterface) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton(confirm_btnText, postDialogInterface);
        builder.setNegativeButton(cancel_btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * @param msg                 dialog提示信息
     * @param confirm_btnText     dialog 确定button文字
     * @param cancel_btnText      dialog 取消button文字
     * @param postDialogInterface 确定button回调
     **/
    public void showDialog(SpannableString msg, String confirm_btnText, String cancel_btnText, DialogInterface.OnClickListener postDialogInterface) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton(confirm_btnText, postDialogInterface);
        builder.setNegativeButton(cancel_btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }



    /**
     * @param msg                    dialog提示信息
     * @param confirm_btnText        dialog 确定button文字
     * @param cancel_btnText         dialog 取消button文字
     * @param confirmDialogInterface 确定button回调
     * @param cancel_DialogInterface 取消button回调
     **/
    public void showDialog(String msg, String confirm_btnText, String cancel_btnText, DialogInterface.OnClickListener confirmDialogInterface, DialogInterface.OnClickListener cancel_DialogInterface) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton(confirm_btnText, confirmDialogInterface);
        builder.setNegativeButton(cancel_btnText, cancel_DialogInterface);
        builder.create().show();
    }


    /**
     * @param msg                    dialog提示信息
     * @param confirm_btnText        dialog 确定button文字
     * @param cancel_btnText         dialog 取消button文字
     * @param confirmDialogInterface 确定button回调
     * @param cancel_DialogInterface 取消button回调
     **/
    public void showDialog(int msg, String confirm_btnText, String cancel_btnText, DialogInterface.OnClickListener confirmDialogInterface, DialogInterface.OnClickListener cancel_DialogInterface) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton(confirm_btnText, confirmDialogInterface);
        builder.setNegativeButton(cancel_btnText, cancel_DialogInterface);
        builder.create().show();
    }


    /**
     * @param msg                    dialog提示信息
     * @param confirm_btnText        dialog 确定button文字
     * @param cancel_btnText         dialog 取消button文字
     * @param confirmDialogInterface 确定button回调
     * @param cancel_DialogInterface 取消button回调
     **/
    public void showDialog(int msg, int confirm_btnText, int cancel_btnText, DialogInterface.OnClickListener confirmDialogInterface, DialogInterface.OnClickListener cancel_DialogInterface) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton(confirm_btnText, confirmDialogInterface);
        builder.setNegativeButton(cancel_btnText, cancel_DialogInterface);
        builder.create().show();
    }


    /**
     * @param title                    dialog标题
     * @param msg                      dialog提示信息
     * @param confirm_btnText          dialog 确定button文字
     * @param cancel_btnText           dialog 取消button文字
     * @param confirm_btnTextInterface 确定button回调
     * @param cancel_DialogInterface   取消button回调
     **/
    public void showDialog(String title, String msg, String confirm_btnText, String cancel_btnText, DialogInterface.OnClickListener confirm_btnTextInterface, DialogInterface.OnClickListener cancel_DialogInterface) {

        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(confirm_btnText, confirm_btnTextInterface);
        builder.setNegativeButton(cancel_btnText, cancel_DialogInterface);
        builder.create().show();

    }

    public static void startAppInfoActivity(Activity activity) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getApplication().getPackageName(), null));
        activity.startActivity(intent);
        activity.finish();
    }


    /**
     * 标准字体
     */
    private static final float FONT_SCALE = 1.0f;


    private void updateConfiguration() {
        final Resources res = getResources();
        final Configuration config = res.getConfiguration();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            if (config.fontScale > FONT_SCALE) {
                //1 设置正常字体大小的倍数
                config.fontScale = 1.3f;
                res.updateConfiguration(config, res.getDisplayMetrics());
            }

        }
    }


    /**
     * 初始化极光推送p
     */
    private void initJpush(Application application) {
        JPush.getInstance(application, BuildConfig.DEBUG).setOpenJpushMessage(jPushMessage);
    }

    /**
     * 极光推送封装实体类
     */
    protected static Extras extras = null;
    private OpenJPushMessage jPushMessage = new OpenJPushMessage() {
        @Override
        public void openMessage(Context context, Extras result) {
            extras = result;
            if (result.getNeedLogin().equals("1")) {
                if (isLogin) {
                    openJpushActivity(context);
                } else {
                    //如果APP是在后台运行
                    if (!isRunningForeground(context)) {
                        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(context, HomeActivity.class);
                        context.startActivity(intent);
                    }
                }
            }
            else {
                openJpushActivity(context);
            }
        }
    };


    private static final String URL = "url";

    private static final String MESSAGE = "message";

    private static final String PRODUCT_LIST = "productList";

    private static final String PRODUCT_DETAIL = "productDetail";
    //groupChat
    public static final String GROUP_CHAT = "groupChat";

    /**
     * Jpush推送打开的界面
     */
    public void openJpushActivity(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (extras.getType().equals(URL) || extras.getType().equals(GROUP_CHAT)) {
            //打开链接
            intent.setClass(context, WebActivity.class);
            ShareBean shareBean = new ShareBean();
            shareBean.setFuncUrl(extras.getContent());
            intent.putExtra("shareBean", shareBean);
            context.startActivity(intent);

        }
        //打开具体信息
        else if (extras.getType().equals(MESSAGE)) {
            intent.setClass(context, MyMessageDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("title", extras.getTitle());
            intent.putExtra("content", extras.getContent());

            context.startActivity(intent);
        }

        //产品列表
        else if (extras.getType().equals(PRODUCT_LIST)) {

            ProductDate.rest();
            ProductDate.productName = extras.getContent();
            HomeActivity.showJPush(context, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT, true);
        }


        //打开产品详情
        else if (extras.getType().equals(PRODUCT_DETAIL)) {

            intent.setClass(context, ProductCodeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("productCode", extras.getProductCode());
            intent.putExtra("sellingStatus", "1");
            context.startActivity(intent);
        }

        extras = null;
    }


    /**
     * 屏幕适配
     */
    protected   void resetLayout(){

    };


    /**
     * 获取布局ID
     *
     * @param
     * @return int 布局ID
     * @description 获取布局Id
     * @date: 2019/6/10 13:42
     * @author: moran
     */
    protected abstract int getLayoutId();


    public boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                    return true;
                }
            }
        }
        return false;
    }


}
