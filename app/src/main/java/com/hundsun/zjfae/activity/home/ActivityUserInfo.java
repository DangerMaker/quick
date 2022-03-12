package com.hundsun.zjfae.activity.home;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.presenter.UserInfoPresenter;
import com.hundsun.zjfae.activity.home.view.UserInfoView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.utils.permission.PermissionsUtil;
import com.hundsun.zjfae.common.view.UserInfoInputDialog;
import com.hundsun.zjfae.common.view.dialog.CustomProgressDialog;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;

import onight.zjfae.afront.gens.UserBaseInfoPB;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * @Description:个人基本信息界面
 * @Author: zhoujianyu
 * @Time: 2018/10/16 14:37
 */
@RuntimePermissions
public class ActivityUserInfo extends CommActivity implements UserInfoView, TakePhoto.TakeResultListener, InvokeListener {
    private LinearLayout lin_head, lin_contact, lin_phone, lin_address, lin_work, lin_wechat, lin_education;
    private ImageView img_head;
    private TextView tv_contact, tv_phone, tv_address, tv_work, tv_wechat, tv_education;
    private int type = 0;
    //    private ChooseImagePopupWindow mChooseImagePopupWindow;//点击头像 拍照或者相册中获取弹框
    private BottomSheetDialog bottomSheetDialog = null;//点击头像 拍照或者相册中获取弹框
    public String mCameraFilePath = null;
    public static final int REQUEST_FILE_PICKER = 10001;
    public static final int TAKE_PHOTO = 10011;
    public static final int REQUEST_CROP_VIEW = 10012;
    private UserInfoPresenter mPresenter;
    private String base64;
    private String picUrl;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CustomProgressDialog myCustomProgressDialog;

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_user_info);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new UserInfoPresenter(this);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("个人基本信息");
        myCustomProgressDialog = new CustomProgressDialog(ActivityUserInfo.this, strMessage);
        myCustomProgressDialog.setCanceledOnTouchOutside(false);
        myCustomProgressDialog.setCancelable(true);
        picUrl = getIntent().getStringExtra("picurl");
        final UserInfoInputDialog.Builder builder = new UserInfoInputDialog.Builder(ActivityUserInfo.this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (type) {
                    case 1:
                        mPresenter.modifyUserInfoData(1, builder.getEtData());
//                        tv_contact.setText(builder.getEtData());
                        break;
                    case 2:
                        mPresenter.modifyUserInfoData(2, builder.getEtData());
//                        tv_phone.setText(builder.getEtData());
                        break;
                    case 3:
                        mPresenter.modifyUserInfoData(3, builder.getEtData());
//                        tv_address.setText(builder.getEtData());
                        break;
                    case 4:
                        mPresenter.modifyUserInfoData(4, builder.getEtData());
//                        tv_wechat.setText(builder.getEtData());
                        break;
                    default:
                        break;

                }
                dialogInterface.dismiss();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        lin_head = findViewById(R.id.lin_head);
        lin_contact = findViewById(R.id.lin_contact);
        lin_phone = findViewById(R.id.lin_phone);
        lin_address = findViewById(R.id.lin_address);
        lin_work = findViewById(R.id.lin_work);
        lin_wechat = findViewById(R.id.lin_wechat);
        lin_education = findViewById(R.id.lin_education);
        img_head = findViewById(R.id.img_head);
        tv_contact = findViewById(R.id.tv_contact);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_work = findViewById(R.id.tv_work);
        tv_wechat = findViewById(R.id.tv_wechat);
        tv_education = findViewById(R.id.tv_education);
        bottomSheetDialog = new BottomSheetDialog(this);
        View rootView = getLayoutInflater().inflate(R.layout.bottom_sheet_sialog_layout, null);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootView.findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUserInfoPermissionsDispatcher.checkStoragePermissionWithPermissionCheck(ActivityUserInfo.this, 1);
                bottomSheetDialog.dismiss();
            }
        });
        rootView.findViewById(R.id.camera_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUserInfoPermissionsDispatcher.checkStoragePermissionWithPermissionCheck(ActivityUserInfo.this, 2);
                bottomSheetDialog.dismiss();
            }
        });
        rootView.findViewById(R.id.dismiss_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        SupportDisplay.resetAllChildViewParam((ViewGroup) rootView);
        bottomSheetDialog.setContentView(rootView);
        //头像点击
        lin_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
        //紧急联系人
        lin_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 1;
                builder.setTitle("请输入紧急联系人");
                builder.setMessage(tv_contact.getText().toString());
                builder.create().show();
            }
        });
        //联系电话
        lin_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 2;
                builder.setTitle("请输入联系电话");
                builder.setMessage(tv_phone.getText().toString());
                builder.create().show();
            }
        });
        //居住地
        lin_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 3;
                builder.setTitle("请输入居住地");
                builder.setMessage(tv_address.getText().toString());
                builder.create().show();
            }
        });
        //职业
        lin_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityUserInfo.this, OccupationorEducationActivity.class);
                intent.putExtra("data", tv_work.getText().toString());
                intent.putExtra("type", "1");
                startActivityForResult(intent, 1);
            }
        });
        //微信号
        lin_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = 4;
                builder.setTitle("请输入微信号");
                builder.setMessage(tv_wechat.getText().toString());
                builder.create().show();
            }
        });
        //学历
        lin_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityUserInfo.this, OccupationorEducationActivity.class);
                intent.putExtra("data", tv_education.getText().toString());
                intent.putExtra("type", "2");
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mPresenter.getUserInfoData();
        if (picUrl.equals("")) {
            mPresenter.getDictionary();
        } else {
            loadPic(picUrl);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    mPresenter.modifyUserInfoData(5, data.getStringExtra("data"));
                    break;
                case 2:
                    mPresenter.modifyUserInfoData(6, data.getStringExtra("data"));
                    break;
//                case REQUEST_FILE_PICKER: {
////                    Intent intent = new Intent(this, CropViewActivity.class);
////                    CropViewActivity.source = data.getData();
////                    startActivityForResult(intent, REQUEST_CROP_VIEW);
//                    mPresenter.uploadPic(data.getData());
//                }
//                break;
//                case TAKE_PHOTO:
//                    CCLog.e("图片地址" + mCameraFilePath);
//                    File cameraFile = new File(mCameraFilePath);
//                    if (cameraFile.exists()) {
//                        CCLog.e("图片文件存在");
////                        Intent intent = new Intent(this, CropViewActivity.class);
////                        CropViewActivity.source = Uri.fromFile(cameraFile);
////                        startActivityForResult(intent, REQUEST_CROP_VIEW);
//                        mPresenter.uploadPic(mCameraFilePath);
//                    }
//                    break;
//                case REQUEST_CROP_VIEW:
////                    if (data != null) {
//////                        CCLog.e("上传图片");
////                        base64 = data.getStringExtra(CropViewActivity.EXTRA_BASE64);
////                        mPresenter.uploadPic(base64); //这里执行图片上传逻辑
////                    }
//                    break;
                default:
                    break;
            }
        }
    }

//    private void showChooseImagePopupWindow() {
//        if (mChooseImagePopupWindow == null) {
//            mChooseImagePopupWindow = new ChooseImagePopupWindow(this, new ChooseImagePopupWindow.OnPopWindowOnClickListener() {
//                @Override
//                public void onCameraClick() {
//                    ActivityUserInfoPermissionsDispatcher.checkStoragePermissionWithPermissionCheck(ActivityUserInfo.this, 1);
//                }
//
//                @Override
//                public void onChooseImageClick() {
//                    .checkStoragePermissionWithPermissionCheck(ActivityUserInfo.this, 2);
//                }
//
//                @Override
//                public void onDismiss() {
//
//                }
//            });
//        }
//        NestedScrollView llContent = (NestedScrollView) findViewById(R.id.ll_content);
//        mChooseImagePopupWindow.showPopupWindow(llContent);
//    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void checkStoragePermission(int type) {
        if (type == 1) {
            //去选择图片
//            Intent intent = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, REQUEST_FILE_PICKER);
            getTakePhoto().onPickMultiple(1);
        } else {
            ActivityUserInfoPermissionsDispatcher.checkCameraPermissionWithPermissionCheck(ActivityUserInfo.this);
        }
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorage() {
        PermissionsUtil.settingDialog(this, getString(R.string.read_write_camera_permission_hint), false);
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorage() {
        PermissionsUtil.settingDialog(this, getString(R.string.read_write_camera_permission_hint), false);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void checkCameraPermission() {
//        startActivityForResult(Intent.createChooser(createCameraIntent(), "Image Browser"), TAKE_PHOTO);
        File file = FileUtil.createFile(this, "take", FileUtil.getFileName() + ".png");
        Uri uri = Uri.fromFile(file);
        takePhoto.onPickFromCapture(uri);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        PermissionsUtil.settingDialog(this, getString(R.string.camera_permission_hint), false);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        PermissionsUtil.settingDialog(this, getString(R.string.camera_permission_hint), false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityUserInfoPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File externalDataDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        CCLog.e("externalDataDir:" + externalDataDir);
        File cameraDataDir = new File(externalDataDir.getAbsolutePath()
                + File.separator + "browser-photo");
        cameraDataDir.mkdirs();
        mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator
                + System.currentTimeMillis() + ".jpg";
        CCLog.e("mcamerafilepath:" + mCameraFilePath);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA,
                    new File(mCameraFilePath).getAbsolutePath());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));
        } else {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(mCameraFilePath)));
        }
        return cameraIntent;
    }

    @Override
    public void getUserInfoData(UserBaseInfoPB.PBIFE_userinfomanage_userBaseInfo data) {
        tv_contact.setText(data.getContent());
        tv_phone.setText(data.getContentMobile());
        tv_address.setText(data.getDomicile());
        tv_work.setText(data.getProfession());
        tv_wechat.setText(data.getWechat());
        tv_education.setText(data.getEducation());
    }

    @Override
    public void modifyUserInfoData(String code, String message, int type, String data) {
        showToast(message);
        if (code != null && code.equals("0000")) {
            switch (type) {
                case 1:
                    tv_contact.setText(data);
                    break;
                case 2:
                    tv_phone.setText(data);
                    break;
                case 3:
                    tv_address.setText(data);
                    break;
                case 4:
                    tv_wechat.setText(data);
                    break;
                case 5:
                    tv_work.setText(data);
                    break;
                case 6:
                    tv_education.setText(data);
                    break;
                default:
                    break;

            }
        }

    }

    //获取头像头尾地址
    @Override
    public void getPicDictionary(String pic, String fix) {
        String iconUrl = pic + UserInfoSharePre.getAccount() + fix;
        loadPic(iconUrl);
    }

    //头像上传通知
    @Override
    public void upLoadPicImage(String code, String msg) {
        myCustomProgressDialog.dismiss();
        if (code != null && code.equals("0000")) {
            mPresenter.setUserHeader();
        } else {
            showToast(msg);
        }
    }

    //后台截屏通知结果
    @Override
    public void setUserHeader(String code, String msg) {
        if (code != null && code.equals("0000")) {
            mPresenter.getDictionary();
            UserInfoSharePre.saveHeadPic(true);
        } else {
            showToast(msg);
        }

    }

    private void loadPic(String iconUrl) {
        CCLog.e(iconUrl);


        String updateTime = String.valueOf(System.currentTimeMillis());

        RequestOptions options = new RequestOptions().centerCrop().transform(new CircleCrop()).error(R.drawable.head).signature(new ObjectKey(updateTime));

        Glide.with(ActivityUserInfo.this).load(iconUrl).apply(options).into(img_head);
//        Glide.with(ActivityUserInfo.this).load(iconUrl).asBitmap().error(R.drawable.head).centerCrop().signature(new StringSignature(updateTime)).into(new BitmapImageViewTarget(img_head) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable =
//                        RoundedBitmapDrawableFactory.create(getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                img_head.setImageDrawable(circularBitmapDrawable);
//            }
//        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        //takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);
        return takePhoto;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void takeSuccess(TResult result, TImage.FromType fromType) {
        TImage image = result.getImage();
        String originalPath = image.getOriginalPath();
        CCLog.e("图片地址:" + originalPath);
        String photoPath = originalPath.substring(originalPath.lastIndexOf("/") + 1, originalPath.length());

        String paths = FileUtil.getTakePath(this);

        File file = new File(paths, photoPath);

        if (!file.exists()) {
            file = new File(originalPath);
        }
        myCustomProgressDialog.show();
        mPresenter.uploadPic(file);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

}
