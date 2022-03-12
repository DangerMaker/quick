package com.hundsun.zjfae.activity.accountcenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.presenter.AccountCenterPresenter;
import com.hundsun.zjfae.activity.accountcenter.view.AccountCenterView;
import com.hundsun.zjfae.activity.home.ActivityUserInfo;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.login.FingerprintDialogFragment;
import com.hundsun.zjfae.activity.logingesture.CreateGestureActivity;
import com.hundsun.zjfae.activity.logingesture.GestureLoginActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.FirstPlayPassWordActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.FingerprintUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.dbutils.Updata;
import com.hundsun.zjfae.common.utils.permission.PermissionsUtil;
import com.zjfae.library.update.PermissionUtils;

import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

//账户中心
public class AccountCenterActivity extends CommActivity<AccountCenterPresenter> implements View.OnClickListener, AccountCenterView {

    private CheckBox fingerprint_checkbox, account_checkbox;
    private ImageView mHead, img_level, img_touzizhe;
    private TextView mAccount;
    private TextView mAccountCapital;
    private TextView mNameReal;
    private TextView mAuthenticationIdentity;
    private TextView mUserType;
    private LinearLayout mFirstSetPassword, ll_id_card_layout, ll_risk_assessment, ll_authentication, ll_verified_layout, ll_setting_security_issues, ll_reset_transaction_password;
    private String picUrl = "";
    /**
     * 是否设置安保问题
     */
    private String isSecuritySet;
    /**
     * 证件类型
     */
    private String certificateType;

    private boolean isClickAccountLevel = false;

    /**
     * 是否实名
     */
    private String verifyName = "";


    /**
     * 是否设置交易密码
     */
    private String isFundPassword = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_center;
    }


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_account_center);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected AccountCenterPresenter createPresenter() {
        return new AccountCenterPresenter(this);
    }

    @Override
    protected void initData() {
        presenter.onUserData();
    }

    @Override
    public void initView() {
        setTitle("账户中心");
        ll_authentication = findViewById(R.id.ll_authentication);

        ll_authentication.setOnClickListener(this);
        findViewById(R.id.ll_modify_login_password).setOnClickListener(this);
        findViewById(R.id.ll_modify_transaction_password).setOnClickListener(this);
        ll_risk_assessment = findViewById(R.id.ll_risk_assessment);
        ll_risk_assessment.setOnClickListener(this);
        ll_id_card_layout = findViewById(R.id.ll_id_card_layout);
        ll_verified_layout = findViewById(R.id.ll_verified_layout);
        ll_reset_transaction_password = findViewById(R.id.ll_reset_transaction_password);
        ll_reset_transaction_password.setOnClickListener(this);
        ll_setting_security_issues = findViewById(R.id.ll_setting_security_issues);
        ll_setting_security_issues.setOnClickListener(this);
        findViewById(R.id.fingerprint_layout).setOnClickListener(this);
        findViewById(R.id.gesture_layout).setOnClickListener(this);
        findViewById(R.id.first_play_pw).setOnClickListener(this);
        findViewById(R.id.account_level_layout).setOnClickListener(this);
        findViewById(R.id.lin_update_gesture).setOnClickListener(this);
        mFirstSetPassword = findViewById(R.id.first_play_pw);
        mUserType = findViewById(R.id.user_type);
        fingerprint_checkbox = findViewById(R.id.fingerprint_checkbox);
        account_checkbox = findViewById(R.id.account_check);
        mHead = (ImageView) findViewById(R.id.head);
        img_level = findViewById(R.id.img_level);
        mAccount = (TextView) findViewById(R.id.account);
        mAccountCapital = (TextView) findViewById(R.id.capital_account);
        mNameReal = (TextView) findViewById(R.id.real_name);
        mAuthenticationIdentity = (TextView) findViewById(R.id.identity_authentication);
        img_touzizhe = findViewById(R.id.img_touzizhe);
        findViewById(R.id.rl_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountCenterActivity.this, ActivityUserInfo.class);
                intent.putExtra("picurl", picUrl);
                baseStartActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isClickAccountLevel) {
            presenter.onUserData();
        }

        if (!FingerprintUtil.callFingerPrint(this)) {
            UserInfoSharePre.saveFingerprintLoginType(false);
        }
        //获取指纹登录或者手势登录的标识
        if (UserInfoSharePre.getFingerprintLogin()) {
            fingerprint_checkbox.setChecked(true);
        } else {
            fingerprint_checkbox.setChecked(false);
        }
        if (UserInfoSharePre.getGestureLoginType()) {
            account_checkbox.setChecked(true);
        } else {
            account_checkbox.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //风险评测
            case R.id.ll_risk_assessment:
                if (verifyName.equals("1")) {
                    baseStartActivity(this, RiskAssessmentActivity.class);
                } else {
                    showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //去绑卡
                            dialog.dismiss();
                            baseStartActivity(AccountCenterActivity.this, AddBankActivity.class);
                        }
                    });
                }
                break;
            //更换手机号
            case R.id.ll_authentication:
                Intent intent2 = new Intent(this, AuthenticationActivity.class);
                intent2.putExtra("isSecuritySet", isSecuritySet);
                intent2.putExtra("certificateType", certificateType);
                intent2.putExtra("type", "bind_phone");
                baseStartActivity(intent2);
                break;
            //修改登录密码
            case R.id.ll_modify_login_password:
                Intent intent = new Intent(this, ModifyPasswordActivity.class);
                intent.putExtra("type", "login");
                baseStartActivity(intent);
                break;
            //修改交易密码
            case R.id.ll_modify_transaction_password:
                if (isFundPassword.equals("true")) {
                    Intent intent1 = new Intent(this, ModifyPasswordActivity.class);
                    intent1.putExtra("type", "transaction");
                    baseStartActivity(intent1);
                } else {
                    showDialog("您还尚未设置交易密码哦，去设置~", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //去设置交易密码
                            dialog.dismiss();
                            Intent intent1 = new Intent(AccountCenterActivity.this, FirstPlayPassWordActivity.class);
                            baseStartActivity(intent1);
                        }
                    });
                }
                break;
            //重置交易密码
            case R.id.ll_reset_transaction_password:
                if (isFundPassword.equals("true")) {
                    Intent intent5 = new Intent(this, AuthenticationActivity.class);
                    intent5.putExtra("isSecuritySet", isSecuritySet);
                    intent5.putExtra("certificateType", certificateType);
                    intent5.putExtra("type", "reset_transaction");
                    baseStartActivity(intent5);
                } else {
                    showDialog("您还尚未设置交易密码哦，去设置~", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //去设置交易密码
                            dialog.dismiss();
                            Intent intent1 = new Intent(AccountCenterActivity.this, FirstPlayPassWordActivity.class);
                            baseStartActivity(intent1);
                        }
                    });
                }
                break;
            //设置安保问题
            case R.id.ll_setting_security_issues:
                if (isFundPassword.equals("true")) {
                    Intent intent3 = new Intent(this, SettingSecurityIssuesAuthenticationActivity.class);
                    intent3.putExtra("certificateType", certificateType);
                    baseStartActivity(intent3);
                } else {
                    showDialog("您还尚未设置交易密码哦，去设置~", "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //去设置交易密码
                            dialog.dismiss();
                            Intent intent1 = new Intent(AccountCenterActivity.this, FirstPlayPassWordActivity.class);
                            baseStartActivity(intent1);
                        }
                    });
                }
                break;
            //指纹设置
            case R.id.fingerprint_layout:
                //判断用户有没有记住密码 如果没有提示返回
                if (!UserInfoSharePre.getUserNameType()) {
                    showDialog("您在登录的时候暂未记住用户名,无法开启指纹登录功能");
                    return;
                }
                FingerprintUtil.callFingerPrint(this, new FingerprintUtil.OnCallBackListenr() {

                    FingerprintDialogFragment fingerprintDialogFragment = new FingerprintDialogFragment();

                    @Override
                    public void onSupportFailed() {
                        showToast("当前设备不支持指纹");
                    }

                    @Override
                    public void onInsecurity() {
                        showToast("请到设置中设置指纹");
                    }

                    @Override
                    public void onEnrollFailed() {
                        showToast("请到设置中设置指纹");
                    }

                    @Override
                    public void onAuthenticationStart() {

                        fingerprintDialogFragment.showDialog(getSupportFragmentManager());
                    }

                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        fingerprintDialogFragment.dismissDialog();
                        // 错误次数太多 暂不可用
                        showToast(errString.toString());
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        showToast("校验失败");
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {

                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                        fingerprintDialogFragment.dismissDialog();
                        if (fingerprint_checkbox.isChecked() == false) {
                            PermissionUtils.init(AccountCenterActivity.this);

                            boolean granted = PermissionUtils.isGranted(Manifest.permission.READ_PHONE_STATE);
                            if (granted) {
                                //开启指纹登录本地保存指纹登录
                                fingerprint_checkbox.setChecked(true);
                                UserInfoSharePre.saveFingerprintLoginType(true);
                                showDialog("您已成功开启指纹登录");
                            } else {

                                @SuppressLint("WrongConstant") PermissionUtils permission = PermissionUtils.permission(Manifest.permission.READ_PHONE_STATE);
                                permission.callback(new PermissionUtils.SimpleCallback() {
                                    @Override
                                    public void onGranted() {
                                        Updata.upData(AccountCenterActivity.this);
                                        //开启指纹登录本地保存指纹登录
                                        fingerprint_checkbox.setChecked(true);
                                        UserInfoSharePre.saveFingerprintLoginType(true);
                                        showDialog("您已成功开启指纹登录");
                                    }

                                    @Override
                                    public void onDenied() {
                                        PermissionsUtil.settingDialog(AccountCenterActivity.this, getString(R.string.read_phone_permission_hint), false);
                                    }
                                });
                                permission.request();
                            }

                        } else {
                            //关闭指纹登录
                            fingerprint_checkbox.setChecked(false);
                            UserInfoSharePre.saveFingerprintLoginType(false);
                            showDialog("您已成功关闭指纹登录");
                        }
                    }
                });
                break;
            case R.id.gesture_layout:
                //判断用户有没有记住密码 如果没有提示返回
                if (!UserInfoSharePre.getUserNameType()) {
                    showDialog("您在登录的时候暂未记住用户名,无法开启手势密码登录功能");
                    return;
                }
                if (UserInfoSharePre.getGestureLoginType()) {
                    //说明手势登录已经开启
                    Intent intent_closegesture = new Intent(this, GestureLoginActivity.class);
                    intent_closegesture.putExtra("type", "close");
                    startActivity(intent_closegesture);
                } else {
                    baseStartActivity(this, CreateGestureActivity.class);
                }

                break;
            //修改指纹
            case R.id.lin_update_gesture:
                if (UserInfoSharePre.getGestureLoginType()) {
                    //说明手势登录已经开启
                    Intent intent_closegesture = new Intent(this, GestureLoginActivity.class);
                    intent_closegesture.putExtra("type", "update");
                    startActivity(intent_closegesture);
                } else {
                    showDialog("您还没有开启手势密码，开启手势密码后才能修改手势密码");
                }
                break;
            case R.id.first_play_pw:
                Intent intent1 = new Intent(this, FirstPlayPassWordActivity.class);
                baseStartActivity(intent1);
                break;
            //账户等级
            case R.id.account_level_layout:
                isClickAccountLevel = true;
                startWebActivity(BasePresenter.accountLevelUrl);
//                baseStartActivity(this, AccountLevelActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    public void getUserHighNetWorthInfo(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo highNetWorthInfo, final String isRealInvestor) {
        StringBuffer buffer = new StringBuffer();
        for (UserHighNetWorthInfo.DictDynamic dynamic : highNetWorthInfo.getData().getDictDynamicListList()) {
            if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {
                buffer.append(dynamic.getAuditComment()).append("\n");
            }
        }
        showDialog(buffer.toString() + "".trim(), "重新上传", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Intent intent = new Intent();
                intent.putExtra("isRealInvestor", isRealInvestor);
                intent.setClass(AccountCenterActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);

            }
        });
    }

    @Override
    public void onUserPortrait(String pic, String fix) {

        String iconUrl = pic + UserInfoSharePre.getAccount() + fix;
        picUrl = iconUrl;
        String updateTime = String.valueOf(System.currentTimeMillis());

        RequestOptions options = new RequestOptions().centerCrop().transform(new CircleCrop()).error(R.drawable.head).signature(new ObjectKey(updateTime));

        Glide.with(AccountCenterActivity.this).load(iconUrl).apply(options).into(mHead);
    }


    @Override
    public void onUserData(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {


        String userType = userDetailInfo.getData().getUserType();

        if (userType.equals("personal")) {

            ll_risk_assessment.setVisibility(View.VISIBLE);
            ll_authentication.setVisibility(View.VISIBLE);
            ll_id_card_layout.setVisibility(View.VISIBLE);
            ll_verified_layout.setVisibility(View.VISIBLE);
            ll_setting_security_issues.setVisibility(View.VISIBLE);
            ll_reset_transaction_password.setVisibility(View.VISIBLE);

        }

        isFundPassword = userDetailInfo.getData().getIsFundPasswordSet();

        if (userDetailInfo.getData().getUserType().equals("personal")) {
            mAccount.setText(userDetailInfo.getData().getMobile());

        } else {
            mAccount.setText(userDetailInfo.getData().getFundAccount());
        }


        if (!userDetailInfo.getData().getUserGroup().equals("0")) {
            mUserType.setText("高净值" + userDetailInfo.getData().getUserGroup());
        }

        if (userDetailInfo.getData().getIsAccreditedInvestor().equals("") || userDetailInfo.getData().getIsAccreditedInvestor().equals("0")) {
            if (!userDetailInfo.getData().getIsShow().equals("2")) {
                mUserType.setText("申请成为合格投资者");
                mUserType.setVisibility(View.GONE);
                img_touzizhe.setBackgroundResource(R.drawable.shenqingtouzizhe);
                img_touzizhe.setVisibility(View.VISIBLE);
            } else {
                mUserType.setVisibility(View.GONE);
                img_touzizhe.setVisibility(View.GONE);
            }
        } else {
            if (userDetailInfo.getData().getUserGroup().equals("0")) {
                mUserType.setVisibility(View.GONE);
                img_touzizhe.setBackgroundResource(R.drawable.touzizhe);
                img_touzizhe.setVisibility(View.VISIBLE);
            } else {
                mUserType.setText("高净值" + userDetailInfo.getData().getUserGroup());
            }
        }
        doInvestor(userDetailInfo.getData());

        mAccountCapital.setText(userDetailInfo.getData().getFundAccount());
        verifyName = userDetailInfo.getData().getVerifyName();
        if ("0".equals(userDetailInfo.getData().getVerifyName())) {
            mNameReal.setText("未实名");
        } else {
            mNameReal.setText(userDetailInfo.getData().getName());
        }
        mAuthenticationIdentity.setText(userDetailInfo.getData().getCertificateCode());
        isSecuritySet = userDetailInfo.getData().getIsSecuritySet();
        certificateType = userDetailInfo.getData().getCertificateType();
        if ("false".equals(userDetailInfo.getData().getIsFundPasswordSet())) {
            mFirstSetPassword.setVisibility(View.VISIBLE);
        } else {
            mFirstSetPassword.setVisibility(View.GONE);
        }
        //加载用户等级图片 图片文件选择规则为 “ip_L”+“9-cifsel”
        if (StringUtils.isNotBlank(userDetailInfo.getData().getCifsel())) {
            int cifsel = Integer.parseInt(userDetailInfo.getData().getCifsel());
            switch (9 - cifsel) {
                case 0:
                    img_level.setImageDrawable(this.getResources().getDrawable(R.drawable.vip_0));
                    break;
                case 1:
                    img_level.setImageDrawable(this.getResources().getDrawable(R.drawable.vip_1));
                    break;
                case 2:
                    img_level.setImageDrawable(this.getResources().getDrawable(R.drawable.vip_2));
                    break;
                case 3:
                    img_level.setImageDrawable(this.getResources().getDrawable(R.drawable.vip_3));
                    break;
                case 4:
                    img_level.setImageDrawable(this.getResources().getDrawable(R.drawable.vip_4));
                    break;
                case 5:
                    img_level.setImageDrawable(this.getResources().getDrawable(R.drawable.vip_5));
                    break;
                default:
                    break;
            }
        }

    }


    private void doInvestor(final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

        findViewById(R.id.lin_usertype).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userDetailInfo.getIsShow().equals("2")) {
                    return;
                }
                String userType = userDetailInfo.getUserType();

                if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {
                    showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //去绑卡
                            dialog.dismiss();
                            baseStartActivity(AccountCenterActivity.this, AddBankActivity.class);
                        }
                    });
                }
                //合格投资者审核中
                else if (userDetailInfo.getHighNetWorthStatus().equals("-1")) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。";
                    if (userDetailInfo.getIsAccreditedInvestor().equals("") || userDetailInfo.getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "您的合格投资者认定材料正在审核中";
                    }

                    showDialog(pmtInfo);
                } else {
                    // 合格投资者审核不通过
                    if (userDetailInfo.getHighNetWorthStatus().equals("0")) {
                        presenter.getUserHighNetWorthInfo(userDetailInfo.getIsRealInvestor());
                    }
                    //非合格投资者，合格投资者，高净值统一弹框
                    else {

                        if (userDetailInfo.getUserType().equals("personal")) {
                            showUserLevelDialog("000", userDetailInfo.getIsRealInvestor());
                        } else if (userDetailInfo.getUserType().equals("company")) {
                            showUserLevelDialog("020", userDetailInfo.getIsRealInvestor());
                        }
                    }
                }
            }
        });
    }

}
