package com.hundsun.zjfae.activity.splash;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.widget.LinearLayout;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.logingesture.widget.ACache;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserSetting;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StatusBarUtil;
import com.hundsun.zjfae.common.utils.dbutils.Updata;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


//@RuntimePermissions
public class StartActivity extends CommActivity<StartPresenter> implements StartView {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData() {

        //第一次安装
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        updateImage();
//        StartActivityPermissionsDispatcher.updateImageWithPermissionCheck(this);
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this, 0);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.start_activity_layout);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected StartPresenter createPresenter() {
        return new StartPresenter(this);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void showError(String msg) {

        CCLog.e("启动错误信息", msg);

        baseStartActivity(StartActivity.this, HomeActivity.class);
        finish();
    }

    @Override
    public void onSuccess() {
        BaseCacheBean baseCacheBean = ADSharePre.getConfiguration(ADSharePre.startIcons, BaseCacheBean.class);
        if (baseCacheBean != null) {
            CCLog.e("起屏页配置信息", "---" + baseCacheBean.getIconsAddress());
            //1为显示启动广告页，其他情况不显示
            if (baseCacheBean.getIs_show().equals("1")) {
                //存在
                baseStartActivity(StartActivity.this, SplashActivity.class);
                finish();
            } else {
                baseStartActivity(StartActivity.this, HomeActivity.class);
                finish();
            }
        } else {
            baseStartActivity(StartActivity.this, HomeActivity.class);
            finish();
        }
    }

    @Override
    public void onError() {
        baseStartActivity(StartActivity.this, HomeActivity.class);
        finish();
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        StartActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }


    //    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @RequiresApi(api = Build.VERSION_CODES.M)
    void updateImage() {

        //新/老版数据迁移
        Updata.upData(this);
        //迁移之前的手势密码存储
        ACache aCache = ACache.get(this);
        if (aCache.getAsBinary("GesturePassword") != null) {
            UserInfoSharePre.setGessturePwd(new String(aCache.getAsBinary("GesturePassword")));
            aCache.remove("GesturePassword");
            aCache.clear();
        }
        readPhoneState();
        presenter.isImageUpdate();
        if (!UserSetting.getCrachUpLoadTime()) {
            presenter.statistticsDataUpload();
        }
//        StartActivityPermissionsDispatcher.checkPhoneStateWithPermissionCheck(this);
    }

    //    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void updateImageDenied() {

        showDialog(R.string.read_write_permission_hint, R.string.permission, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                StartActivityPermissionsDispatcher.updateImageWithPermissionCheck(StartActivity.this);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                baseStartActivity(StartActivity.this, HomeActivity.class);
            }
        });
    }


    //    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void updateImageAgain() {


        showDialog(R.string.read_write_permission_hint, R.string.setting, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startAppInfoActivity(StartActivity.this);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }


//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
//    void checkPhoneState() {
//        readPhoneState();
//        presenter.isImageUpdate();
//        if (!UserSetting.getCrachUpLoadTime()) {
//            presenter.statistticsDataUpload();
//        }
//
//    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
//    void checkPhoneStateDenied() {
//        readPhoneState();
//        presenter.isImageUpdate();
//        if (!UserSetting.getCrachUpLoadTime()) {
//            presenter.statistticsDataUpload();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
//    void checkPhoneStateNeverAskAgain() {
//        readPhoneState();
//        presenter.isImageUpdate();
//        if (!UserSetting.getCrachUpLoadTime()) {
//            presenter.statistticsDataUpload();
//        }
//    }
}
