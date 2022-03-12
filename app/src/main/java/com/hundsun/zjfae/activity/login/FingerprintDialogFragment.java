package com.hundsun.zjfae.activity.login;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.FingerprintUtil;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.fragment.BaseDialogfragment;

public class FingerprintDialogFragment extends BaseDialogfragment implements View.OnClickListener {


    private TextView account ;


    @Override
    public void initView() {

//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 0.7f;
//        window.setAttributes(lp);
        account = findViewById(R.id.tv_number);
        findViewById(R.id.cancelFingerBtn).setOnClickListener(this);

    }


    @Override
    protected void initData() {
        boolean isNumber = Utils.isPhone(UserInfoSharePre.getUserName());

        if (isNumber){
            account.setText("账号：" + Utils.parseTelPhone(UserInfoSharePre.getUserName()));
        }
        else {
            account.setText("账号：" + UserInfoSharePre.getUserName());

        }
    }

    @Override
    public int getLayoutId() {

        return R.layout.layout_fingerprint;
    }


    public void showDialog(FragmentManager fragmentManager) {
        super.showDialog(fragmentManager, "FingerprintDialogFragment");
    }

    @Override
    protected boolean isCancel() {
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cancelFingerBtn:
                FingerprintUtil.cancel();
                dismissDialog();
                break;
                default:
                    break;

        }
    }


}
