package com.hundsun.zjfae.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.bean.ConfigurationUtils;
import com.hundsun.zjfae.activity.mine.presenter.FaceAuthorisationPresenter;
import com.hundsun.zjfae.activity.mine.view.FaceAuthorisationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

import onight.zjfae.afront.gensazj.FaceProtol;


/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine
 * @ClassName:      FaceAuthorisationActivity
 * @Description:     人脸识别授权书
 * @Author:         moran
 * @CreateDate:     2019/6/17 14:58
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/17 14:58
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class FaceAuthorisationActivity extends CommActivity<FaceAuthorisationPresenter> implements View.OnClickListener,FaceAuthorisationView {



    private WebView noticeContent_webView;
    private CheckBox accredit_check;
    private Button accredit_button;
    private ConfigurationUtils faceConfiguration = null;
    @Override
    public void getFaceProtol(FaceProtol.Ret_PBAPP_faceprotol faceprotol) {

        if (faceprotol.getReturnCode().equals("0000")){

            setResult(RESULT_OK);
            finish();

        }
        else {
            showDialog(faceprotol.getReturnMsg());
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accredit_layout:

                accredit_check.setChecked(!accredit_check.isChecked());

                if (accredit_check.isChecked()){
                    accredit_button.setBackgroundResource(R.drawable.face_authorisation_button_checked_shape);
                }
                else {
                    accredit_button.setBackgroundResource(R.drawable.face_authorisation_button_normal_shape);
                }

                break;
            case R.id.accredit_finish:
                finish();
                break;
            case R.id.accredit_button:
                if (accredit_check.isChecked()){
                    presenter.getFaceProtol(faceConfiguration.getIdCard(),faceConfiguration.getMobile());
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void initData() {
        Bundle bundle = getIntent().getBundleExtra("faceBundle");
        faceConfiguration = bundle.getParcelable("face");
        setTitle(faceConfiguration.getNoticeTitle());
        noticeContent_webView.loadDataWithBaseURL(null, getHtmlData(faceConfiguration.getNoticeContent()), "text/html", "UTF-8", null);
    }




    @Override
    public void initView() {
        noticeContent_webView = findViewById(R.id.noticeContent_webView);
        accredit_check = findViewById(R.id.accredit_check);
        accredit_button = findViewById(R.id.accredit_button);
        accredit_button.setOnClickListener(this);
        findViewById(R.id.accredit_layout).setOnClickListener(this);
        findViewById(R.id.accredit_finish).setOnClickListener(this);
    }

    @Override
    protected FaceAuthorisationPresenter createPresenter() {
        return  new FaceAuthorisationPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.face_authorisation_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_authorisation;
    }
}
