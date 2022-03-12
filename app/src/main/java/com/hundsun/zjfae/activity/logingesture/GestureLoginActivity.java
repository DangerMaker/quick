package com.hundsun.zjfae.activity.logingesture;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.logingesture.widget.LockPatternUtil;
import com.hundsun.zjfae.activity.logingesture.widget.LockPatternView;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;

import java.util.List;


/**
 * @Description:手势登录界面 以及关闭手势登陆界面
 * @Author: zhoujianyu
 * @Time: 2018/12/7 11:07
 */
public class GestureLoginActivity extends BasicsActivity {
    private LockPatternView lockPatternView;
    private TextView messageTv, account, forgetGestureBtn, cancle, tv_tips;
    private RelativeLayout rel_bottom;
    private static final long DELAYTIME = 600L;
    //close的时候为关闭手势密码 login的时候为手势登陆界面,Blockchain,为验证区块链
    private String type = "";
    private int count = 6;

    //resultCode
    private static final int RESULT_ERROR_CODE = 0x9245;




    @Override
    protected int getLayoutId() {
        return R.layout.activity_gesture_login;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.lin_gesture_login);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        super.initView();
        rel_bottom = findViewById(R.id.rel_bottom);
        if (StringUtils.isNotBlank(getIntent().getStringExtra("type"))) {
            type = getIntent().getStringExtra("type");
        }
        if (type.equals("login")) {
            setTitle("手势密码登录");
            rel_bottom.setVisibility(View.VISIBLE);
        } else if (type.equals("close")) {
            setTitle("关闭手势密码");
            rel_bottom.setVisibility(View.GONE);
        } else if (type.equals("update")) {
            setTitle("验证旧手势密码");
            rel_bottom.setVisibility(View.GONE);
        }
        else if (type.equals("Blockchain")){
            setTitle("验证手势密码");
        }
        tv_tips = findViewById(R.id.tv_tips);
        lockPatternView = findViewById(R.id.lockPatternView);
        messageTv = findViewById(R.id.messageTv);
        forgetGestureBtn = findViewById(R.id.forgetGestureBtn);
        forgetGestureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //忘记手势密码 关闭手势密码
                UserInfoSharePre.setGessturePwd("");
                UserInfoSharePre.setFingerprintPassWordBefore("");
                UserInfoSharePre.saveGestureLoginType(false);
                finish();
            }
        });
        cancle = findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        account = findViewById(R.id.account);
        boolean isNumber = Utils.isPhone(UserInfoSharePre.getUserName());

        if (isNumber){
            account.setText("账号：" + Utils.parseTelPhone(UserInfoSharePre.getUserName()));
        }
        else {
            account.setText("账号：" + UserInfoSharePre.getUserName());

        }
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if (pattern != null) {


                if (pattern.size() < 6){

                    updateStatus(Status.LESSERROR);

                }
                else {
                    if (type.equals("Blockchain")){

                        if (LockPatternUtil.checkPatternBlockchain(pattern)){
                            updateStatus(Status.CORRECT);
                            showToast("区块链手势密码验证成功");
                            Intent data = new Intent();
                            data.putExtra("gestureCode", UserInfoSharePre.getBlockchainGessturePwdBefore());
                            setResult(RESULT_OK,data);
                            finish();
                        }

                        else {
                            updateStatus(Status.ERROR);
                        }

                    }
                    else {
                        if (LockPatternUtil.checkPattern(pattern)){
                            updateStatus(Status.CORRECT);
                            loginGestureSuccess();
                        }
                        else {
                            count--;
                            tv_tips.setText("你还有" + count + "次机会");
                            if (count == 0) {
                                if (type.equals("login")) {
                                    showToast("已5次错误，请账号登录后再重置手势密码");
                                    setResult(RESULT_ERROR_CODE);
                                } else {
                                    showToast("您已超过最大限制次数");
                                }
                                finish();
                            }
                            updateStatus(Status.ERROR);
                        }
                    }


                }

//                //区块链
//                if (type.equals("Blockchain")){
//
//                    if (pattern.size() < 6){
//                        updateStatus(Status.LESSERROR);
//                    }
//                    else {
//                        StringBuffer buffer = new StringBuffer();
//                        for (LockPatternView.Cell cell : pattern) {
//                            buffer.append(cell.getIndex());
//                        }
//                        lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
//                        Intent data = new Intent();
//                        data.putExtra("gestureCode",buffer.toString());
//                        setResult(RESULT_OK,data);
//                        finish();
//                    }
//                }
//                //手势密码锁状态
//                else {
//                    if (LockPatternUtil.checkPattern(pattern)) {
//                        updateStatus(Status.CORRECT);
//                    } else if (pattern.size() < 6) {
//                        updateStatus(Status.LESSERROR);
//                    } else {
//                        if (type.equals("login")) {
//                            count--;
//                            tv_tips.setText("你还有" + count + "次机会");
//                            if (count == 0) {
//                                if (type.equals("login")) {
//                                    showToast("已5次错误，请账号登录后再重置手势密码");
//                                    setResult(RESULT_ERROR_CODE);
//                                } else {
//                                    showToast("您已超过最大限制次数");
//                                }
//                                finish();
//                            }
//                        }
//                        updateStatus(Status.ERROR);
//
//                    }
//                }


            }
        }
    };

    /**
     * 更新状态
     *
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);

                break;

                default:
                    break;
        }
    }

    /**
     * 手势验证成功
     */
    private void loginGestureSuccess() {
        if (type.equals("login")) {
            showToast("手势密码验证成功");
            setResult(RESULT_OK);
        } else if (type.equals("close")) {
            //这里预备回传 可是AccountCenterActivity的 onresum方法里面有判断指纹以及手势密码是否已经开启的方法了
//            setResult(AccountCenterActivity.ACCOUNTGESTURELOGIN_REQUEST_CODE);
            showToast("手势密码关闭成功");
            //手势密码数据清空
            UserInfoSharePre.setGessturePwd("");
            UserInfoSharePre.setFingerprintPassWordBefore("");
            UserInfoSharePre.saveGestureLoginType(false);

        } else if (type.equals("update")) {
            Intent intent = new Intent(this, CreateGestureActivity.class);
            intent.putExtra("type", "update");
            startActivity(intent);
        }
        finish();
    }


    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //连接的点数小于6（二次确认的时候就不再提示连接的点数小于6，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }

}
