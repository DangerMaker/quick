package com.hundsun.zjfae.activity.logingesture;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.logingesture.widget.LockPatternIndicator;
import com.hundsun.zjfae.activity.logingesture.widget.LockPatternUtil;
import com.hundsun.zjfae.activity.logingesture.widget.LockPatternView;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:手势创建页面
 * @Author: zhoujianyu
 * @Time: 2018/12/7 11:07
 */
public class CreateGestureActivity extends BasicsActivity {
    private LockPatternIndicator lockPatternIndicator;
    private LockPatternView lockPatternView;
    private TextView messageTv;
    private TextView account;
    private TextView tv_tips;
    private static final long DELAYTIME = 600L;
    private List<LockPatternView.Cell> mChosenPattern = null;
    private String type = "";
    private int count = 6;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_gesture;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.lin_create_gesture);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        super.initView();
        if (StringUtils.isNotBlank(getIntent().getStringExtra("type"))) {
            type = getIntent().getStringExtra("type");
        }
        if (type.equals("update")) {
            setTitle("修改手势密码");
        } else {
            setTitle("设置手势密码");
        }
        lockPatternIndicator = findViewById(R.id.lockPatterIndicator);
        lockPatternView = findViewById(R.id.lockPatternView);
        messageTv = findViewById(R.id.messageTv);
        account = findViewById(R.id.account);

        boolean isNumber = Utils.isPhone(UserInfoSharePre.getUserName());

        if (isNumber){
            account.setText("账号：" + Utils.parseTelPhone(UserInfoSharePre.getUserName()));
        }
        else {
            account.setText("账号：" + UserInfoSharePre.getUserName());

        }

        tv_tips = findViewById(R.id.tv_tips);
        lockPatternView.setOnPatternListener(patternListener);
    }

    /**
     * 手势监听
     */
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
            //updateStatus(Status.DEFAULT, null);
            lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            //Log.e(TAG, "--onPatternDetected--");

            if (pattern.size() < 6) {
                updateStatus(Status.LESSERROR, pattern);
                return;
            }

            if (mChosenPattern == null && pattern.size() >= 6) {
                mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                updateStatus(Status.CORRECT, pattern);
            } else if (mChosenPattern == null && pattern.size() < 6) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {

                if (mChosenPattern.equals(pattern)) {

                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    if (type.equals("update")) {
                        count--;
                        tv_tips.setText("你还有" + count + "次机会");
                        if (count == 0) {
                            showToast("您已超过最大限制次数");
                            finish();
                        }
                    }
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };

    /**
     * 更新状态
     *
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        messageTv.setTextColor(getResources().getColor(status.colorId));
        messageTv.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
            default:
                break;
        }
    }

    /**
     * 更新 Indicator
     */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null){
            return;
        }

        lockPatternIndicator.setIndicator(mChosenPattern);
    }

//    /**
//     * 重新设置手势
//     */
//    @OnClick(R.id.resetBtn)
//    void resetLockPattern() {
//        mChosenPattern = null;
//        lockPatternIndicator.setDefaultIndicator();
//        updateStatus(Status.DEFAULT, null);
//        lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
//    }

    /**
     * 成功设置了手势密码(结束当前界面)
     */
    private void setLockPatternSuccess() {
        //手势密码设置成功
        if (type.equals("update")) {
            UserInfoSharePre.saveGestureLoginType(true);
            showToast("修改手势密码成功");
        }
        else if (type.equals("Blockchain")){

            showToast("区块链手势密码创建成功");
        }
        else {
            UserInfoSharePre.saveGestureLoginType(true);
            showToast("创建手势密码成功");
        }

        setResult(RESULT_OK);
        finish();
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {

        byte[] bytes = LockPatternUtil.patternToHash(cells);

        StringBuffer buffer = new StringBuffer();

        for (LockPatternView.Cell cell : cells) {
            buffer.append(cell.getIndex());
        }
        //区块链
        if (type.equals("Blockchain")){

            UserInfoSharePre.setBlockchainGessturePwd(new String(bytes));

            UserInfoSharePre.setBlockchainGessturePwdBefore(buffer.toString());
        }
        else {
            UserInfoSharePre.setGessturePwd(new String(bytes));

            UserInfoSharePre.setFingerprintPassWordBefore(buffer.toString());
        }

    }

    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
        //连接的点数小于6（二次确认的时候就不再提示连接的点数小于6，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }
}
