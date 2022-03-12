package com.hundsun.zjfae.activity.mine;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.presenter.BankCardCertificationPresenter;
import com.hundsun.zjfae.activity.mine.view.CertificationView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.zjfae.captcha.face.IDCardResult;
import com.zjfae.captcha.face.TencentOCRCallBack;
import com.zjfae.captcha.face.TencentWbCloudOCRSDK;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author moran
 */
@RuntimePermissions
public class BankCardCertificationActivity extends CommActivity<BankCardCertificationPresenter> implements View.OnClickListener, CertificationView {

    private ImageView frontFullImage, backFullImage;

    private ImageView frontFullImage_camera, backFullImage_camera;

    private ImageView frontFullImage_q_code, backFullImage_q_code;

    private ImageView watermark_full, watermark_backfull;

    private TextView tv_name, tv_idCard, tv_validDate;

//    private EditText ed_address;
//
//    private Button updata_address;


    @Override
    protected BankCardCertificationPresenter createPresenter() {
        return new BankCardCertificationPresenter(this);
    }

    @Override
    protected void initView() {
        frontFullImage = findViewById(R.id.frontFullImage);
        backFullImage = findViewById(R.id.backFullImage);
        frontFullImage_camera = findViewById(R.id.frontFullImage_camera);
        backFullImage_camera = findViewById(R.id.backFullImage_camera);
        frontFullImage_q_code = findViewById(R.id.frontFullImage_q_code);
        backFullImage_q_code = findViewById(R.id.backFullImage_q_code);
        watermark_full = findViewById(R.id.watermark_full);
        watermark_backfull = findViewById(R.id.watermark_backfull);
        tv_name = findViewById(R.id.tv_name);
        tv_idCard = findViewById(R.id.tv_idCard);
        tv_validDate = findViewById(R.id.tv_validDate);
//        ed_address = findViewById(R.id.ed_address);
//        updata_address = findViewById(R.id.updata_address);
//        updata_address.setOnClickListener(this);
        findViewById(R.id.ln_id_card_layout).setOnClickListener(this);
        findViewById(R.id.backFullImage_layout).setOnClickListener(this);
        findViewById(R.id.comit_button).setOnClickListener(this);

//        ed_address.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() == 0) {
//                    updata_address.setVisibility(View.GONE);
//                } else {
//                    updata_address.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    @Override
    protected void initData() {
        setTitle("证件信息核对");

    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.certification_layout);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bankcard_certification;
    }

    private boolean ocrTypeMode;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ln_id_card_layout:
                ocrTypeMode = true;
                BankCardCertificationActivityPermissionsDispatcher.onCameraWithPermissionCheck(this);
                break;

            case R.id.backFullImage_layout:
                ocrTypeMode = false;
                BankCardCertificationActivityPermissionsDispatcher.onCameraWithPermissionCheck(this);
                break;
            case R.id.comit_button:
//                if (!TextUtils.isEmpty(ed_address.getText().toString())) {
//                    IDCardResult.getInstance().setAddress(ed_address.getText().toString());
                    presenter.onUserIdCardUpload(IDCardResult.getInstance());
//                } else {
//                    showDialog("请确认信息完整且正确");
//                }
                //startActivityForResult(new Intent(this,CareerSelsectActivity.class),REQUEST_CODE);
                break;
            case R.id.updata_address:
//                if (!ed_address.getText().toString().equals("")) {
//                    ed_address.setFocusable(true);
//                    ed_address.setFocusableInTouchMode(true);
//                    ed_address.requestFocus();
//                    InputMethodManager inputManager =
//                            (InputMethodManager) ed_address.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputManager.showSoftInput(ed_address, 0);
//                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onTencentOcrId(final String orderNO, String appid, String nonce, String userID, String sign, final ImageView imageView, final boolean ocrTypeMode) {
        TencentWbCloudOCRSDK.getInstance().init(this).execute(orderNO, appid, nonce, userID, sign, new TencentOCRCallBack() {
            @Override
            public void onSuccess(IDCardResult idCardResult) {
                tv_name.setText(idCardResult.getName());
                tv_idCard.setText(idCardResult.getCardNum());
                tv_validDate.setText(idCardResult.getValidDate());
//                ed_address.setText(idCardResult.getAddress());

//                boolean isAddress = idCardResult.getAddress() != null && !idCardResult.getAddress().equals("") ? true : false;
//                ed_address.setEnabled(isAddress);
//                ed_address.setClickable(isAddress);
//                updata_address.setClickable(isAddress);
//                updata_address.setEnabled(isAddress);
//                if (isAddress) {
//                    updata_address.setVisibility(View.VISIBLE);
//                }

                boolean isFrontFullImageSrc = idCardResult.getFrontFullImageSrc() != null && !idCardResult.getFrontFullImageSrc().equals("") ? true : false;

                if (isFrontFullImageSrc) {
                    frontFullImage_camera.setVisibility(View.GONE);
                    frontFullImage_q_code.setVisibility(View.VISIBLE);
                    watermark_full.setVisibility(View.VISIBLE);
                }

                boolean isBackFullImageSrc = idCardResult.getBackFullImageSrc() != null && !idCardResult.getBackFullImageSrc().equals("") ? true : false;

                if (isBackFullImageSrc) {
                    backFullImage_camera.setVisibility(View.GONE);
                    backFullImage_q_code.setVisibility(View.VISIBLE);
                    watermark_backfull.setVisibility(View.VISIBLE);
                }
                ImageLoad.getImageLoad().LoadImage(BankCardCertificationActivity.this, ocrTypeMode ? idCardResult.getFrontFullImageSrc() : idCardResult.getBackFullImageSrc(), imageView, System.currentTimeMillis() + "");

            }

            @Override
            public void onError(String errorMsg) {
                showDialog(errorMsg);

            }

            @Override
            public void onLoginTimeOut(String errorMsg) {
                showDialog(errorMsg, "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.clean();
                    }
                });
            }

            @Override
            public void onTencentCallBack(String orderNo) {
                presenter.onTencentFaceCallback(orderNo, "cardUpload");

            }
        }, ocrTypeMode);
    }

    @Override
    public void onUserIdCardUpload(String returnMsg) {
        Intent intent = new Intent(BankCardCertificationActivity.this, FaceDeleteBankActivity.class);
        intent.putExtra("faceBundle", getIntent().getBundleExtra("faceBundle"));
        baseStartActivity(intent);

    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCamera() {
        if (ocrTypeMode) {
            presenter.getTencentOcrInfo(frontFullImage, ocrTypeMode);
        } else {
            presenter.getTencentOcrInfo(backFullImage, ocrTypeMode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BankCardCertificationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraDenied() {
        showDialog(R.string.camera_permission_hint, R.string.permission, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BankCardCertificationActivityPermissionsDispatcher.onCameraWithPermissionCheck(BankCardCertificationActivity.this);


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraAgain() {
        showDialog(R.string.camera_permission_hint, R.string.setting, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startAppInfoActivity(BankCardCertificationActivity.this);


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IDCardResult.getInstance().recycle();
    }
}
