package com.hundsun.zjfae.activity.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.OpenAttachmentActivity;
import com.hundsun.zjfae.activity.product.bean.AttachmentEntity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.UserAgreementSetting;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.fragment.BaseDialogfragment;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.login
 * @ClassName: LoginDialogFragment
 * @Description: 登录弹框
 * @Author: moran
 * @CreateDate: 2019/8/2 16:15
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/8/2 16:15
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class LoginDialogFragment extends BaseDialogfragment implements View.OnClickListener, DialogInterface.OnKeyListener {


    private ImageView login_buttom_img, zjIntroduction;

    private EditText userName, passWord, edit_accountCode;

    private ImageView clean_user, clean_pass, img_accountCode;

    private LinearLayout accountCode_layout;

    private CheckBox rememberUserNameCheckBox, password_state, agreement_check;


    private LoginInfoListener loginInfoListener;

    private String needValidateAuthCode = "0";
    private String agreementVersion = "";


    private TextView block_chain_system;

    private TextView tv_agreement;

    private boolean isImageCodeShow;


    @Override
    public void initView() {
        zjIntroduction = findViewById(R.id.zjIntroduction);
        login_buttom_img = findViewById(R.id.login_buttom_img);
        //注册
        findViewById(R.id.register).setOnClickListener(this);
        //忘记密码
        findViewById(R.id.fo_get_password).setOnClickListener(this);
        //是否记住用户名
        findViewById(R.id.keep_account_state).setOnClickListener(this);
        //是否勾选隐私协议
        findViewById(R.id.keep_agreement_state).setOnClickListener(this);
        //登录
        findViewById(R.id.login_button).setOnClickListener(this);

        findViewById(R.id.login_layout).setOnClickListener(this);

        //区块链查询入口
        block_chain_system = findViewById(R.id.block_chain_system);
        block_chain_system.setOnClickListener(this);

        userName = findViewById(R.id.username);
        userName.setOnFocusChangeListener(new FocusChange());
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkAggrementVersion();
            }
        });
        passWord = findViewById(R.id.passWord);
        passWord.setOnFocusChangeListener(new FocusChange());
        clean_user = findViewById(R.id.clean_user);
        clean_user.setOnClickListener(this);
        clean_pass = findViewById(R.id.clean_pass);
        clean_pass.setOnClickListener(this);
        password_state = findViewById(R.id.password_state);
        password_state.setOnClickListener(this);
        accountCode_layout = findViewById(R.id.accountCode_layout);
        rememberUserNameCheckBox = findViewById(R.id.keep_user);
        edit_accountCode = findViewById(R.id.edit_accountCode);
        img_accountCode = findViewById(R.id.img_accountCode);
        img_accountCode.setOnClickListener(this);


        setLayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


        agreement_check = findViewById(R.id.agreement_check);
        tv_agreement = findViewById(R.id.tv_agreement);

        String startText = "我已阅读并同意以下协议";

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
                loginInfoListener.startWebActivity("https://www.zjfae.com/api/account_agreement.php");

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
                loginInfoListener.startWebActivity("https://www.zjfae.com/api/agreement.php");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };


        SpannableString spannableString = new SpannableString(buffer);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                if (agreement_check.isChecked()) {
                    agreement_check.setChecked(false);
                } else {
                    agreement_check.setChecked(true);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
            }
        }, 0, startText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
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

    @Override
    protected void initData() {


        if (UserInfoSharePre.getBlockchainState()) {
            block_chain_system.setVisibility(View.VISIBLE);
        } else {
            block_chain_system.setVisibility(View.GONE);
        }


        rememberUserNameCheckBox.setChecked(UserInfoSharePre.getUserNameType());
        if (rememberUserNameCheckBox.isChecked()) {
            userName.setText(UserInfoSharePre.getUserName());
        }


        //有登录页面的缓存
        if (ADSharePre.getListConfiguration(ADSharePre.loginIcon, BaseCacheBean.class) != null) {

            List<BaseCacheBean> imageList = ADSharePre.getListConfiguration(ADSharePre.loginIcon, BaseCacheBean.class);
            if (imageList.isEmpty()) {
                //后台请求回来的登录页面的缓存数据为空
                login_buttom_img.setBackgroundResource(R.drawable.login_buttom);
                zjIntroduction.setBackgroundResource(R.drawable.introduction);
                return;
            }
            for (BaseCacheBean imageBean : imageList) {
                if (imageBean.getIconsPosition().equals("bottom")) {

                    ImageLoad.getImageLoad().LoadImage(getMContext(), imageBean.getIconsAddress(), login_buttom_img, R.drawable.login_buttom, R.drawable.login_buttom);
                } else if (imageBean.getIconsPosition().equals("middle")) {
                    ImageLoad.getImageLoad().LoadImage(getMContext(), imageBean.getIconsAddress(), zjIntroduction, R.drawable.introduction, R.drawable.introduction);
                }
            }
        } else {
            //没有登录页面的缓存
            login_buttom_img.setBackgroundResource(R.drawable.login_buttom);
            zjIntroduction.setBackgroundResource(R.drawable.introduction);
        }


    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_login_layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.getDialog().setOnKeyListener(this);
    }

    @Override
    protected boolean isCancel() {

        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //登录
            case R.id.login_button:
                if (Utils.isViewEmpty(userName)) {
                    loginInfoListener.loginErrorInfo("请输入用户名");
                } else if (Utils.isViewEmpty(passWord)) {
                    loginInfoListener.loginErrorInfo("请输入密码");
                } else if (accountCode_layout.getVisibility() == View.VISIBLE && Utils.isViewEmpty(edit_accountCode)) {
                    loginInfoListener.loginErrorInfo("请输入图形验证码");
                } else if (!agreement_check.isChecked()) {
                    loginInfoListener.loginErrorInfo("您需要阅读《浙江金融资产交易中心个人会员服务协议》 及《浙金中心隐私权政策》后勾选同意");
                } else {

                    //登录
                    loginInfoListener.login(userName.getText().toString(), EncDecUtil.AESEncrypt(passWord.getText().toString()), "0", edit_accountCode.getText().toString(), needValidateAuthCode);
                }
                break;
            case R.id.password_state:
                if (password_state.isChecked()) {
                    passWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;

            case R.id.img_accountCode:
                loginInfoListener.refreshImageCode();
                break;

            case R.id.register:
                loginInfoListener.onRegister();
                break;
            case R.id.fo_get_password:
                loginInfoListener.forgetPassWord();
                break;

            case R.id.clean_user:
                userName.setText("");
                break;
            case R.id.clean_pass:
                passWord.setText("");
                break;
            case R.id.keep_account_state:
                if (rememberUserNameCheckBox.isChecked()) {
                    rememberUserNameCheckBox.setChecked(false);
                } else {
                    rememberUserNameCheckBox.setChecked(true);
                }
                break;
            case R.id.keep_agreement_state:
                if (agreement_check.isChecked()) {
                    agreement_check.setChecked(false);
                } else {
                    agreement_check.setChecked(true);
                }
                break;

            case R.id.login_layout:
                close();
                break;
            case R.id.block_chain_system:
                loginInfoListener.startWebActivity(BasePresenter.APP_BLOCK_URL);

                break;
            default:
                break;
        }

    }

    //登录回调
    public void setLoginInfoListener(LoginInfoListener loginInfoListener) {
        this.loginInfoListener = loginInfoListener;
    }

    public void setNeedValidateAuthCode(String needValidateAuthCode) {
        this.needValidateAuthCode = needValidateAuthCode;
    }

    public void onImageCode(byte[] bytes) {
        if (accountCode_layout.getVisibility() == View.GONE) {
            accountCode_layout.setVisibility(View.VISIBLE);
        }

        ImageLoad.getImageLoad().LoadImage(getMContext(), bytes, img_accountCode);
    }


    /**
     * EditText焦点监听
     */
    private class FocusChange implements View.OnFocusChangeListener {


        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {

                if (v == userName) {
                    clean_user.setVisibility(View.VISIBLE);
                    clean_pass.setVisibility(View.GONE);
                } else {
                    clean_pass.setVisibility(View.VISIBLE);
                    clean_user.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 判断协议是否需要勾选
     */
    private void checkAggrementVersion() {
        if (!Utils.isViewEmpty(userName)) {
            String account = userName.getText().toString();
            agreement_check.setChecked(agreementVersion.equals(UserAgreementSetting.getAgreementVersion(account)));
        } else {
            agreement_check.setChecked(false);
        }
    }


    public void showDialog(FragmentManager manager) {
        super.show(manager, "LoginDialogFragment");
//        loginInfoListener.onLocationPermissions();
    }

    /**
     * 是否记住用户名
     */
    public boolean getRememberUserName() {

        return rememberUserNameCheckBox.isChecked();
    }

    /**
     * 获取用户名
     *
     * @return userName
     */
    public String getUserName() {

        return userName.getText().toString();
    }

    /**
     * 获取用户密码
     *
     * @return passWord
     */
    public String getPassWord() {

        return passWord.getText().toString();
    }

    /**
     * 获取协议版本号
     */
    public String getAgreementVersion() {

        return agreementVersion;
    }

    /**
     * 设置协议版本号
     */
    public void setAgreementVersion(String version) {
        agreementVersion = version;
        checkAggrementVersion();
    }

    public boolean isImageCodeShow() {
        return accountCode_layout.getVisibility() == View.VISIBLE;
    }

    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(getContext(), "请再按一次退出程序", Toast.LENGTH_LONG).show();
                touchTime = currentTime;
                return true;
            } else {
                loginInfoListener.onFinish();
                return true;
            }
        }
        return false;
    }

    private void close() {
        View view = getDialog().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
