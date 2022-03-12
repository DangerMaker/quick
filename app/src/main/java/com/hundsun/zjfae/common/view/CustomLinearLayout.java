package com.hundsun.zjfae.common.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;

public class CustomLinearLayout extends LinearLayout {

    //布局背景颜色
    private int titleLayoutBackground;
    //左上角返回
    private Drawable backIcon;
    //标题
    private int titleText;

    private int titleTextSize;

    private int titleTextColor;

    /**
     * 是否隐藏左上角返回按钮
     * */
    private boolean isHideBack = false;

    /**
     * 默认title
     * */
    private int titleLayoutId;


    private RelativeLayout rl_commonn_title_menu_;
    private LinearLayout ll_commonn_title_back;

    private ImageView iv_commonn_title_setting;

    private TextView tv_commonn_title_text;


    private boolean hideTitleLayout;


    public CustomLinearLayout(Context context) {
        this(context,null);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CustomLinearLayout);
        titleLayoutBackground = array.getColor(R.styleable.CustomLinearLayout_titleBackground,getResources().getColor(R.color.colorRed));
        backIcon =  array.getDrawable(R.styleable.CustomLinearLayout_backIcon);
        titleText = array.getResourceId(R.styleable.CustomLinearLayout_titleText,R.string.app_name);
        titleTextSize = array.getDimensionPixelOffset(R.styleable.CustomLinearLayout_titleTextSize,(int)getResources().getDimension(R.dimen.titleTextSize));
        titleTextColor = array.getColor(R.styleable.CustomLinearLayout_titleTextColor,getResources().getColor(android.R.color.white));
        isHideBack = array.getBoolean(R.styleable.CustomLinearLayout_hideBack,false);
        titleLayoutId = array.getResourceId(R.styleable.CustomLinearLayout_layout,R.layout.new_base_title);
        hideTitleLayout = array.getBoolean(R.styleable.CustomLinearLayout_hideTitleLayout,false);
        array.recycle();
        initView(context);
    }
    private void initView(Context context){
        if (!hideTitleLayout){
            View rootView  =  LayoutInflater.from(context).inflate(titleLayoutId,this,true);
            ll_commonn_title_back = findViewById(R.id.ll_commonn_title_back);
            //ll_commonn_title_back.setOnClickListener(this);
            iv_commonn_title_setting = findViewById(R.id.iv_commonn_title_setting);
            tv_commonn_title_text = findViewById(R.id.tv_commonn_title_text);
            rl_commonn_title_menu_ = findViewById(R.id.rl_commonn_title_menu_);
            initData();
        }

    }

    private void initData(){
        if (isHideBack){
            ll_commonn_title_back.setVisibility(GONE);
        }
        else {
            ll_commonn_title_back.setVisibility(VISIBLE);
        }
        tv_commonn_title_text.setText(titleText);
        tv_commonn_title_text.setTextColor(titleTextColor);
    }


    /**
     * 设置title文字
     * */
    public void setTitleText(int titleText) {
        if (tv_commonn_title_text != null){
            tv_commonn_title_text.setText(titleText);
        }
    }

    /**
     * 设置title文字
     * **/
    public void setTitleText(String titleText) {
        if (tv_commonn_title_text != null){
            tv_commonn_title_text.setText(titleText);
        }
    }




    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    private static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
