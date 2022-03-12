package com.hundsun.zjfae.activity.home;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.PictureUtil;
import com.hundsun.zjfae.common.view.cropview.CropView;

/**
 * @Description:照片选择好以后 选择区域界面 类似裁剪
 * @Author: zhoujianyu
 * @Time:  2018/10/18 9:57
 */
public class CropViewActivity extends CommActivity implements View.OnClickListener{
    public static final String EXTRA_BASE64 = "extra_base64";
    private CropView mCropView;
    private ImageView mIvResult;
    private TextView doneBtn;
    private TextView cancelBtn;

    public static Uri source;
    @Override
    public void resetLayout() {
        RelativeLayout layout = findViewById(R.id.layout_crop_view);
        SupportDisplay.resetAllChildViewParam(layout);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crop_view;
    }

    @Override
    public void initView() {
        super.initView();
        mCropView = (CropView) findViewById(R.id.cropView);
        mIvResult = (ImageView) findViewById(R.id.resultIv);
        doneBtn = (TextView) findViewById(R.id.tv_choose);
        cancelBtn = (TextView) findViewById(R.id.tv_cancel);

        doneBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        mCropView.of(source).asSquare().withOutputSize(400, 400).initialize(CropViewActivity.this);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_choose) {

            mCropView.setVisibility(View.GONE);
            mIvResult.setVisibility(View.VISIBLE);

            new Thread() {
                public void run() {
                    Intent intent = new Intent();
                    String base64 = PictureUtil.compressBmpToFile(mCropView.getOutput(), 500);
                    intent.putExtra(EXTRA_BASE64,base64);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }.start();
        } else if (id == R.id.tv_cancel) {
            source = null;
            finish();
        }
    }

}
