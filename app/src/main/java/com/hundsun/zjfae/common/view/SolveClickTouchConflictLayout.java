package com.hundsun.zjfae.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

public class SolveClickTouchConflictLayout extends RelativeLayout {

    private boolean mScrolling;
    private float touchDownX;
    private float touchDownY;
    private float downX;
    private float downY;

    private int width;
    private int height;
    private int screenWidth;
    private int screenHeight;

    public SolveClickTouchConflictLayout(Context context) {
        this(context,null);
    }

    public SolveClickTouchConflictLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SolveClickTouchConflictLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setContentView(View view){
        addView(view);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                touchDownY = event.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownX - event.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop() || Math.abs(touchDownY - event.getY()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    mScrolling = true;
                } else {
                    mScrolling = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=getMeasuredWidth();
        height=getMeasuredHeight();
        screenWidth= getContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight=getContext().getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                final float xDistance = event.getX() - downX;
                final float yDistance = event.getY() - downY;
                int l,r,t,b;
                //当水平或者垂直滑动距离大于10,才算拖动事件

                l = (int) (getLeft() + xDistance);
                r = l+width;
                t = (int) (getTop() + yDistance);
                b = t+height;
                //不划出边界判断,此处应按照项目实际情况,因为本项目需求移动的位置是手机全屏,
                // 所以才能这么写,如果是固定区域,要得到父控件的宽高位置后再做处理
                if(l<0){
                    l=0;
                    r=l+width;
                }else if(r>screenWidth){
                    r=screenWidth;
                    l=r-width;
                }
                if(t<0){
                    t=0;
                    b=t+height;
                }else if(b>screenHeight){
                    b=screenHeight;
                    t=b-height;
                }

                this.layout(l, t, r, b);
                break;
            case MotionEvent.ACTION_UP:
                break;

        }

        return super.onTouchEvent(event);
    }
}
