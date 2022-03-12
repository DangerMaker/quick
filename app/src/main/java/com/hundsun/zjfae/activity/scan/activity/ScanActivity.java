package com.hundsun.zjfae.activity.scan.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.scan.camera.CameraManager;
import com.hundsun.zjfae.activity.scan.decoding.InactivityTimer;
import com.hundsun.zjfae.activity.scan.decoding.ScanActivityHandler;
import com.hundsun.zjfae.activity.scan.view.ViewfinderView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.permission.PermissionsUtil;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.io.IOException;
import java.util.Vector;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


/**
 * @Description:扫码界面
 * @Author: zhoujianyu
 * @Time: 2018/9/19 9:24
 */
@RuntimePermissions
public class ScanActivity extends CommActivity implements Callback {

    private ScanActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new  InactivityTimer(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!PermissionsUtil.checkCamera(this)) {
            if (Build.VERSION.SDK_INT > 22) {
                //动态申请CAMERA权限
                ScanActivityPermissionsDispatcher.checkCameraPermissionWithPermissionCheck(ScanActivity.this);
            }
        } else {
            if (PermissionsUtil.cameraIsCanUse()) {
                initCamera();
            } else {
                PermissionsUtil.settingDialog(this,getString(R.string.camera_permission_hint));
            }
        }

    }

    private void initCamera() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
//        initBeepSound(); // 注释该代码，防止初始化一个空的提示音
        vibrate = true;
        getViewfinderView().setOnFlashLightStateChangeListener(new ViewfinderView.onFlashLightStateChangeListener() {
            @Override
            public void openFlashLight(boolean open) {
                CameraManager.get().turnOnFlashLight(open);
                getViewfinderView().reDraw();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void resetLayout() {
        FrameLayout layout = findViewById(R.id.layout_scan);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    /**
     * Handler scan result 获取扫码完成以后的结果处理
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        final String resultString = result.getText();
        CCLog.e("json=" + resultString);
//        intent.putExtra("url", resultString);
//        intent.putExtra("shareItem", "");
//        intent.putExtra("isShare", "0");
        if (resultString.contains("zjfae.com") || resultString.contains("weixin")) {
            ShareBean shareBean = new ShareBean();
            shareBean.setFuncUrl(resultString);
            startWebActivity(shareBean);
            finish();
        } else {
            showDialog("您扫描的二维码存在安全隐患，无法打开!", "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        }
    }


    void closeCamera() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();

//        // 关闭摄像头
//        cameraManager.closeDriver();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new ScanActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    void restartCamera() {

        viewfinderView.setVisibility(View.VISIBLE);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        initCamera(surfaceHolder);

        // 恢复活动监控器
        inactivityTimer.onResume();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    @SuppressLint("MissingPermission")
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 相机动态权限
     */

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.VIBRATE})
    void checkCameraPermission() {
        //权限检查通过以后
        closeCamera();
        restartCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ScanActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.VIBRATE})
    void onCameraDenied() {
        //权限获取失败
        PermissionsUtil.settingDialog(this,getString(R.string.camera_permission_hint));
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.VIBRATE})
    void onCameraNeverAskAgain() {
        //点击不再提示
        PermissionsUtil.settingDialog(this,getString(R.string.camera_permission_hint));
    }

}