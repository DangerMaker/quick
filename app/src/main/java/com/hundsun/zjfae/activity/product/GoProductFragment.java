package com.hundsun.zjfae.activity.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.WebActivity;
import com.hundsun.zjfae.activity.product.bean.GoActivityBean;
import com.hundsun.zjfae.activity.productreserve.ReserveProductDetailActivity;
import com.hundsun.zjfae.common.view.dialog.BaseDialogFragment;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.util.ArrayList;

/**
 * @Description:首页用户已经预约了产品 可以购买时 弹框跳转去购买页
 * @Author: zhoujianyu
 * @Time: 2018/11/27 15:12
 */
public class GoProductFragment extends BaseDialogFragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ArrayList<GoActivityBean> list = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = getArguments().getParcelableArrayList("data");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_go_product;
    }

    @Override
    protected boolean isCancel() {
        return false;
    }

    @Override
    public void resetLayout() {
//        RelativeLayout layout = findViewById(R.id.goproduct_layout);
//        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    public void initView() {
        findViewById(R.id.cancel_img).setOnClickListener(this);
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        initWidth(frameLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (dialog == null) {
            dialog = getDialog();
        }
    }

    @Override
    protected void baseInitLayoutParams() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_img:
                dismissDialog();
                break;
        }
    }

    //计算顶部的高度
    private void initWidth(FrameLayout frameLayout) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        ViewGroup.LayoutParams lp = frameLayout.getLayoutParams();
        lp.width = (int) (width * 0.98);
        frameLayout.setLayoutParams(lp);
    }

}
