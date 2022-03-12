package com.hundsun.zjfae.common.utils.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.List;

public class PermissionsUtil {


    public static class Builder {


        private Context context;

        private String permission[];

        private int requestId;

        private static PermissionCallBack callBack;

        private List<String> permissionList;


        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setPermission(String... permission) {
            this.permission = permission;
            return this;
        }

        public Builder setRequestId(int requestId) {
            this.requestId = requestId;
            return this;

        }

        public Builder setPermissionCallBack(PermissionCallBack callBack) {
            this.callBack = callBack;
            return this;
        }

        public void build() {
            permissionList = new ArrayList<>();
            hasPermission();

        }


        public static PermissionCallBack getPermissionCallBack() {
            return callBack;
        }

        /**
         * 查询当前权限是否禁止
         */
        public void hasPermission() {
            for (int i = 0; i < permission.length; i++) {
                boolean checkPermission = ContextCompat.checkSelfPermission(context, permission[i]) != PackageManager.PERMISSION_GRANTED;
                //表示权限被拒绝，说明此权限作用
                if (checkPermission) {
                    permissionList.add(permission[i]);
                }
            }
            if (null != permissionList && !permissionList.isEmpty()) {
                // permissionList集合里面有未授权的权限
                String[] perMissions = permissionList.toArray(new String[permissionList.size()]);
                Intent intent = new Intent(context, PermissionActivity.class);
                intent.putExtra("permission", perMissions);
                intent.putExtra("requestId", requestId);
                context.startActivity(intent);

                //开启activity
                // requestPermissions(context,permissions);
            }
            //权限已经授权
            else {
                callBack.onCheckPermissionResult(true);

            }
        }


    }


    public static boolean writeExtralStorage(Activity activity) {
        final int result = 2001;
        final String[] permission = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        return checkPermission(activity, result, permission, "开启存储空间权限后才能下载");
    }

    public static boolean checkCamera(Activity activity) {//6.0以下无法判断
        final int result = 2002;
        final String[] permission = {
                Manifest.permission.CAMERA
        };

        return checkPermission(activity, result, permission, "开启相机权限后才能使用");
    }

    public static boolean checkMicrophone(Activity activity) {
        final int result = 2003;
        final String[] permission = {
                Manifest.permission.RECORD_AUDIO
        };

        return checkPermission(activity, result, permission, "开启麦克风权限后才能使用");
    }

    public static boolean checkLocation(Activity activity) {
        final int result = 2004;
        final String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        return checkPermission(activity, result, permission, "开启位置信息权限后才能使用");
    }

    public static boolean checkCallPhone(Activity activity) {//6.0以下无法判断
        final int result = 2005;
        final String[] permission = {
                Manifest.permission.CALL_PHONE
        };

        return checkPermission(activity, result, permission, "开启位置信息权限后才能使用");
    }

    public static boolean checkPhoneState(Activity activity) {//6.0以下无法判断
        final int result = 2006;
        final String[] permission = {
                Manifest.permission.READ_PHONE_STATE
        };

        return checkPermission(activity, result, permission, "开启设备信息权限后才能使用");
    }

    private static boolean checkPermission(Activity activity, int result, String[] permission, String tipString) {
        List<String> needApply = new ArrayList<>();
        for (String item : permission) {
            if (ActivityCompat.checkSelfPermission(activity, item) != PackageManager.PERMISSION_GRANTED) {
                needApply.add(item);
            }
        }

        if (needApply.isEmpty()) {
            return true;
        }

        String[] applys = new String[needApply.size()];
        applys = needApply.toArray(applys);

//        Toast.makeText(activity, tipString, Toast.LENGTH_SHORT).show();
//        ActivityCompat.requestPermissions(
//                activity,
//                applys,
//                result
//        );

        return false;
    }

    /**
     * 检查系统相机是否能够打开
     * 用于判断6.0以下相机权限是否开启
     *
     * @return
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }
        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    public static void settingDialog(final Activity context, String info) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setTitle("温馨提示");
        builder.setMessage(info);

        builder.setPositiveButton(R.string.permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startAppInfoActivity(context);
            }
        });


        builder.setNegativeButton(R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.finish();
            }
        });
        builder.create().show();
    }

    /**
     * isExit
     *
     * @param context
     * @param isExit
     */
    public static void settingDialog(final Activity context, String info, final Boolean isExit) {
        final CustomDialog.Builder builder = new CustomDialog.Builder(context);
        builder.setTitle("温馨提示");
        builder.setMessage(info);

        builder.setPositiveButton(R.string.permission, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startAppInfoActivity(context, isExit);
            }
        });


        builder.setNegativeButton(R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isExit) {
                    context.finish();
                }
            }
        });
        builder.create().show();
    }

    private static void startAppInfoActivity(Activity context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(localIntent);
        context.finish();
    }

    private static void startAppInfoActivity(Activity context, Boolean isExit) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(localIntent);
        if (isExit) {
            context.finish();
        }
    }


}
