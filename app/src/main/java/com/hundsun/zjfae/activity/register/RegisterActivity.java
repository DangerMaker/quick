package com.hundsun.zjfae.activity.register;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.BaseSharedPreferences;
import com.hundsun.zjfae.common.user.PhoneInfo;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserLevelShowTimeSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;
import com.hundsun.zjfae.common.view.dialog.RegisterImageCodeDialog;
import com.zjfae.captcha.CustomCaptchaUtils;

import org.w3c.dom.Text;

import onight.zjfae.afront.gens.Register;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class RegisterActivity extends CommActivity<RegisterPresenter> implements View.OnClickListener, RegisterView {

    private EditText register_phone, phone_number, recommendMobile, login_passWord, login_passWord_state;

    private CheckBox passWord_state, password_state, register_check;

    private Button mobile_sms_code;
    private CustomCountDownTimer countDownTimer;

    private NestedScrollView register_scroll;

    private Button register;

    //注册
    private static final int REGISTER_REQUEST_CODE = 0X191;
    //随便逛逛
    private static final int CASULA_CODE = 0x759;
    //当网易盾不开启时 显示的图片验证码
    private String imagecode = "";
    //记录网易盾请求失败原因
    private int count = 0;
    //判断是网易盾还是图形验证码
    private String paraValue = "";

    private TextView tv_agreement;

    @Override
    public void mobileSMSCode(String returnCode, String returnMsg) {
        if (returnCode != null && returnCode.equals("0000")) {
            showToast(returnMsg);
            countDownTimer.start();
        } else if (returnCode != null && returnCode.equals("i50002")) {
            showDialog(returnMsg);
        } else {
            showDialog(returnMsg);
        }
    }

    @Override
    public void registerState(String returnCode, String returnMsg, String mobile) {
        if (returnCode.equals("0000")) {

            presenter.onUserInfo(mobile);

        } else if (returnCode.equals("1010")) {
            showDialog(returnMsg);
        } else if (returnCode.equals("2101")) {
            showDialog(returnMsg);
        } else {
            showDialog(returnMsg);
        }

    }

    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String mobile) {
        showToast("注册成功");
        BaseSharedPreferences.saveTradeIsUpdateSate("1");
        isLogin = true;
        BaseActivity.isRegister = true;
        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfoData = userDetailInfo.getData();
        //用户类型，personal(个人)，company(机构)
        String userType = userDetailInfoData.getUserType();
        UserInfoSharePre.saveUserType(userType);

        UserInfoSharePre.setUserName(register_phone.getText().toString());
        UserInfoSharePre.setTradeAccount(userDetailInfoData.getTradeAccount());
        UserInfoSharePre.setAccount(userDetailInfoData.getAccount());
        UserInfoSharePre.setFundAccount(userDetailInfoData.getFundAccount());
        UserLevelShowTimeSharePre.saveUserAccount(userDetailInfoData.getAccount());
        UserInfoSharePre.setMobile(mobile);
        String isRealInvestor = userDetailInfo.getData().getIsRealInvestor();

        Intent intent = new Intent(this,
                RegisterStateActivity.class);
        intent.putExtra("isRealInvestor", isRealInvestor);
        startActivityForResult(intent, REGISTER_REQUEST_CODE);
    }

    @Override
    public void initCaptcha(String paraValue) {
        this.paraValue = paraValue;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == CASULA_CODE) {
            setResult(CASULA_CODE);
            finish();
        } else if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            setResult(RESULT_OK);
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.passWord_state:

                if (passWord_state.isChecked()) {
                    login_passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    login_passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;

            case R.id.password_state:
                if (password_state.isChecked()) {
                    login_passWord_state.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    login_passWord_state.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;


            case R.id.register:
                if (Utils.isViewEmpty(register_phone)) {
                    showDialog("请输入手机号");
                } else if (!Utils.isPhone(register_phone.getText().toString())) {
                    showDialog("请输入正确的手机号");
                } else if (Utils.isViewEmpty(phone_number)) {
                    showDialog("请输入短信验证码");
                } else if (Utils.isViewEmpty(login_passWord)) {
                    showDialog("请设置登录密码");
                } else if (Utils.isViewEmpty(login_passWord_state)) {
                    showDialog("请再次设置登录密码");
                } else if (!login_passWord.getText().toString().equals(login_passWord_state.getText().toString())) {
                    showDialog("登录密码不一致");
                } else if (!register_check.isChecked()) {
                    register_scroll.fullScroll(NestedScrollView.FOCUS_DOWN);
                    showDialog("请先同意并阅读浙江金融资产交易中心开户协议");
                    return;
                } else {
                    PhoneInfo phoneInfo = PhoneInfo.getPhoneInfo();
                    Register.REQ_PBIFE_reg_register_new.Builder registerBuilder =
                            Register.REQ_PBIFE_reg_register_new.newBuilder();
                    registerBuilder.setChannelNo("12");
                    registerBuilder.setCheckCode(phone_number.getText().toString());
                    registerBuilder.setGetStatus(phoneInfo.phoneNumberStatus);
                    registerBuilder.setLoginPassword(EncDecUtil.AESEncrypt(login_passWord.getText().toString()));
                    registerBuilder.setLoginPasswordSure(EncDecUtil.AESEncrypt(login_passWord_state.getText().toString()));
                    registerBuilder.setMobile(register_phone.getText().toString());
                    registerBuilder.setPhoneNum(phoneInfo.phoneNumber);
                    registerBuilder.setRoutingAddress(phoneInfo.routingAddress);
                    registerBuilder.setLocationInfo(os.toString());
                    registerBuilder.setImageCode(imagecode);
                    registerBuilder.setVersion("1.0.1");
                    if (!Utils.isViewEmpty(recommendMobile) && !Utils.isPhone(recommendMobile.getText().toString())) {
                        showDialog("请输入正确的推荐人手机号");
                        return;
                    }
                    registerBuilder.setRecommendMobile(recommendMobile.getText().toString());
                    //注册
                    presenter.register(registerBuilder.build().toByteArray());
                }
                break;

            case R.id.register_check:


                if (register_check.isChecked()) {
                    register.setClickable(true);
                    register.setEnabled(true);
                    register.setText("提交注册");
                    register.setBackgroundResource(R.drawable.product_buy_clickable);
                } else {
                    register.setClickable(false);
                    register.setEnabled(false);
                    register.setBackgroundResource(R.drawable.product_buy);
                    register.setText("必须同意协议才能注册");
                }
                break;
            case R.id.mobile_sms_code:
                if (Utils.isViewEmpty(register_phone)) {
                    showDialog("请输入手机号");
                } else if (!Utils.isPhone(register_phone.getText().toString())) {
                    showDialog("请输入正确的手机号");
                } else {
                    if (paraValue.equals("01")) {
                        initCaptcha();
                    } else {
                        //图形验证码
                        initRegisterImageView();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initCaptcha() {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            initRegisterImageView();
            return;
        }
        CustomCaptchaUtils captchaUtils = new CustomCaptchaUtils(this);
        //captchaUtils.setCaptchaId(paraValue);
        captchaUtils.setCaptchaListener(new CustomCaptchaUtils.CaptListener() {
            @Override
            public void onSuccess(final String token) {
                CCLog.e("网易盾验证成功---->");
                presenter.mobileNumberCode(register_phone.getText().toString(), "", token);
            }

            @Override
            public void onError(String error) {
                CCLog.e("网易盾加载出错---->" + error);
//                count++;
//                if (count < 3) {
//                    initCaptcha();
//                } else {
//                    initRegisterImageView();
//                }

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onClose() {

            }
        });
        captchaUtils.start();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initData() {
        presenter.initCaptcha();//最开始获取网易盾是否开启
        countDownTimer = new CustomCountDownTimer(60000, 1000, mobile_sms_code);

        String startText = "我已阅读并同意";

        String notice = "《浙江金融资产交易中心个人会员服务协议》";

        String andText = "及";

        String agreement = "《浙金中心隐私权政策》";

        StringBuffer buffer = new StringBuffer();
        buffer.append(startText);
        buffer.append(notice);
        buffer.append(andText);
        buffer.append(agreement);


        //开户协议
        ClickableSpan noticeClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startWebActivity("https://www.zjfae.com/api/account_agreement.php");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        //隐私协议
        ClickableSpan agreementClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startWebActivity("https://www.zjfae.com/api/agreement.php");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };


        SpannableString spannableString = new SpannableString(buffer);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.black)), 0, startText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), startText.length(), startText.length() + notice.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(noticeClickableSpan, startText.length(), startText.length() + notice.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        int start = startText.length() + notice.length() + andText.length();
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.black)), startText.length() + notice.length(), start, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        int end = startText.length() + notice.length() + andText.length() + agreement.length();
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(agreementClickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
        tv_agreement.setHighlightColor(Color.TRANSPARENT);
        tv_agreement.setText(spannableString);
    }

    public void initRegisterImageView() {

        RegisterImageCodeDialog.Builder builder = new RegisterImageCodeDialog.Builder(this);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCodeCallback("确认", new RegisterImageCodeDialog.ImageCodeCallback() {
            @Override
            public void imageCodeDate(DialogInterface dialog, String imageCode) {


                if (Utils.isStringEmpty(imageCode)) {
                    showDialog("请输入图形验证码");
                } else {
                    dialog.dismiss();
                    imagecode = imageCode;
                    presenter.mobileNumberCode(register_phone.getText().toString(), imageCode, "");
                }


            }
        });
        builder.create().show();
    }

    @Override
    public void initView() {
        setTitle("注册");
        register_phone = findViewById(R.id.register_phone);
        phone_number = findViewById(R.id.phone_number);
        mobile_sms_code = findViewById(R.id.mobile_sms_code);
        mobile_sms_code.setOnClickListener(this);
        recommendMobile = findViewById(R.id.recommendMobile);
        register_check = findViewById(R.id.register_check);
        register_check.setOnClickListener(this);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        register_scroll = findViewById(R.id.register_scroll);
        tv_agreement = findViewById(R.id.tv_agreement);
        login_passWord = findViewById(R.id.login_passWord);
        login_passWord_state = findViewById(R.id.login_passWord_state);
        passWord_state = findViewById(R.id.passWord_state);
        passWord_state.setOnClickListener(this);
        password_state = findViewById(R.id.password_state);
        password_state.setOnClickListener(this);
        //findViewById(R.id.tv_notice).setOnClickListener(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.register_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }


    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void initAMapLocationNeed() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
