package com.hundsun.zjfae.common.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.harmonycloud.apm.android.HarmonycloudAPM;
import com.hundsun.zjfae.BuildConfig;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.user.UserInfo;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.ushare.UShare;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.CrashUtils;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.dbutils.ObjectBox;
import com.hundsun.zjfae.common.utils.useroperation.UserOperation;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.zjfae.cockroach.Cockroach;
import com.zjfae.cockroach.ExceptionHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends MultiDexApplication {
    private static final String TAG = "BaseApplication---";
    private static BaseApplication mInstance = null;
    private List<Activity> mActivityList;


    @Override
    public void onCreate() {
        super.onCreate();
        if (mInstance == null) {
            mInstance = this;
        }
        //友盟分享注册
        UShare.ShareInit(this);
        //数据库初始化
        //SQLiteUtils.initSQL(this);
        ObjectBox.init(this);

        UserOperation.getInstance().initFile(this);

        //谐云APM工具
//        //浙金token
        HarmonycloudAPM.getInstance(this).withToken("33038A75E14BA8C8483C45AEAFB556385AC041F606D151DCD5DF7E197632BAA38C213D19DAC9A0CA99528DF5062E5E4A")
                .withLoggingEnabled(true).withLogLevel(5)
                .withNativeHook(true)
                .withChannel("zjfae_Android")
                //服务器地址
                //http://124.160.17.4:9001 -生产地址
                //http://124.160.56.92:9001 -测试、并行
                .usingCollectorAddress(BuildConfig.collectorAddressHosts)
                .start();

        CrashUtils.getInstance().init(this);
        final Thread.UncaughtExceptionHandler sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler();
        //app全局捕捉异常
        Cockroach.install(this, new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                CrashUtils.getInstance().saveCrashFile(throwable);

            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            protected void onEnterSafeMode() {
            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                super.onMayBeBlackScreen(e);
                Thread thread = Looper.getMainLooper().getThread();
                //黑屏时建议直接杀死app
                sysExcepHandler.uncaughtException(thread, new RuntimeException("black screen"));
            }
        });

        if (Build.MANUFACTURER.equalsIgnoreCase("OPPO") && Build.DEVICE.startsWith("R9")) {
            fixOPPO_R9();
        }
        //初始化腾讯X5内核
        preInitX5Core();
    }

    public static BaseApplication getInstance() {

        return mInstance;
    }


    public void add(Activity baseActivity) {
        if (mActivityList == null) {
            mActivityList = new ArrayList<>();
        }
        mActivityList.add(baseActivity);
    }

    public void remove(BaseActivity activity) {
        if (mActivityList == null) {
            return;
        }
        if (mActivityList.contains(activity)) {
            mActivityList.remove(activity);
        }
    }

    public List<Activity> getActivityList() {
        return mActivityList;
    }

    public void finishAll() {
        for (int i = 0; i < mActivityList.size(); i++) {
            mActivityList.get(i).finish();
        }
        mActivityList.clear();
    }


    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, android.R.color.black
                );//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    private void fixOPPO_R9() {
        try {
            Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
            Method method = clazz.getSuperclass().getDeclaredMethod("stop");
            method.setAccessible(true);
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            method.invoke(field.get(null));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private void preInitX5Core() {

        CCLog.e("数据信息", UserInfoSharePre.getSkip() + "：+++++");
        if (StringUtils.isNotBlank(UserInfoSharePre.getSkip()) &&
                UserInfoSharePre.getSkip().equals(UserInfo.SKIPTENCENT)) {
            // 设置X5初始化完成的回调接口
            CCLog.e("回调信息", QbSdk.isTbsCoreInited());
            if (!QbSdk.isTbsCoreInited()) {
                QbSdk.preInit(getApplicationContext(), null);
            }
            QbSdk.initX5Environment(getApplicationContext(), null);
        }


    }
}
