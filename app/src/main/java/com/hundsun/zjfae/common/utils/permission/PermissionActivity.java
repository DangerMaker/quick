package com.hundsun.zjfae.common.utils.permission;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    private String [] permission;
    private int requestId;
    private List<String> permissionRefusedList;
    private PermissionCallBack callBack;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callBack = PermissionsUtil.Builder.getPermissionCallBack();
        permissionRefusedList = new ArrayList<>();
        permission = getIntent().getStringArrayExtra("permission");
        for (String s : permission){
            CCLog.e(s);
        }
        requestId = getIntent().getIntExtra("requestId",1024);


        if (permission != null && requestId != 1024){
            requestPermission(permission,requestId);
        }

    }


    //权限申请
    public void requestPermission(String[] permission,int requestId){
        ActivityCompat.requestPermissions(this, permission, requestId);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults [i] != PackageManager.PERMISSION_GRANTED){
                //判断是否勾选禁止后不再询问
                boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);

                if (showRequestPermission) {
                    permissionRefusedList.add(permissions[i]);
                    //权限重新申请
                    break;
                } else {
                    //权限被禁止，且不再询问
                    settingDialog();
                    break;
                }
            }
            else {
                //权限申请通过
                callBack.onCheckPermissionResult(true);
                finish();
                break;

            }

        }

        if (!permissionRefusedList.isEmpty()){
            String[] requestperission = permissionRefusedList.toArray(new String[permissionRefusedList.size()]);
            showPremission(requestperission);
            //第一次申请权限还有被拒，弹框提示用户

        }


    }



    public void showPremission(final String [] permission){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("为了您能正常使用APP,请授权");

        builder.setPositiveButton("授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestPermission(permission,requestId);
            }
        });


        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callBack.onCheckPermissionResult(false);
                finish();
            }
        });
        builder.create().show();
    }



    public void settingDialog(){
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("为了您能正常使用APP,请在设置中打开APP权限");

        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startAppInfoActivity();
            }
        });


        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callBack.onCheckPermissionResult(false);
                finish();
            }
        });
        builder.create().show();
    }




    private void startAppInfoActivity() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
        finish();
    }

}
