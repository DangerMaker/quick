package com.hundsun.zjfae.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;

import java.util.List;

public class DropDownMenu extends LinearLayout {


    //顶部菜单布局
    private LinearLayout tabMenuView;

    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //弹出菜单父布局
    private FrameLayout popupMenuViews;

    //底部容器，包含 maskView,contentView
    private FrameLayout containerView;

    public DropDownMenu(Context context) {
        this(context, null);
    }

    private List<String> tabList;


    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView);


        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        addView(containerView);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams
                .MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        //addView(maskView);
        maskView.setVisibility(GONE);//遮罩层隐藏


        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
    }


    public void setDropDownMenu(List<String> tabList, @NonNull List<View> popupViews,
                                @NonNull View contentView) {
        this.tabList = tabList;
        if (popupMenuViews.getChildCount() == 0) {
            containerView.addView(contentView, 0);
            containerView.addView(maskView, 1);
            containerView.addView(popupMenuViews, 2);
            for (int i = 0; i < tabList.size(); i++) {
                addTab(tabList, i);
            }
            for (int i = 0; i < popupViews.size(); i++) {
                if (popupViews.get(i).getLayoutParams() == null) {
                    popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                            .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                View childView = popupViews.get(i);
                childView.setVisibility(GONE);
                popupMenuViews.addView(childView, i);
            }
        }

    }

    public interface OnItemMenuClickListener {
        void OnItemMenuClick(TextView tabView, int position);
    }


    OnItemMenuClickListener itemMenuClickListener;

    public void setOnItemMenuClickListener(OnItemMenuClickListener listener) {
        itemMenuClickListener = listener;
    }


    public void addTab(List<String> tabTexts, final int i) {

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.tab_item, null);
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(15, 10, 15, 10);
        rootView.setLayoutParams(params);
        final TextView textView = getTabTextView(rootView);
        textView.setText(tabTexts.get(i));
        setTextDrawables(getTabImageView(rootView), true);
        tabMenuView.addView(rootView);
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(v);
                if (itemMenuClickListener != null) {
                    itemMenuClickListener.OnItemMenuClick(textView, i);
                }

            }
        });
    }


    private void switchMenu(View view) {
        for (int i = 0; i < tabMenuView.getChildCount(); i++) {

            if (view == tabMenuView.getChildAt(i)) {
                Log.e("current_tab_position", current_tab_position + "");
                if (current_tab_position == i) {
                    closeMenu();
                    Log.e("关闭", "关闭");
                } else {
                    Log.e("打开", "打开");
                    openMenu(i);
                }
            } else {
                rest(i);
            }
        }

    }


    //打开菜单时，同时打开遮罩层
    public void openMenu(int i) {
        current_tab_position = i;
        LinearLayout layout = getLinearLayout(tabMenuView.getChildAt(i));
        TextView textView = getTabTextView(tabMenuView.getChildAt(i));
        Log.e("textView", textView + "");
        if (textView != null) {
            textView.setTextColor(Color.WHITE);
            setTextDrawables(getTabImageView(tabMenuView.getChildAt(i)), false);
        }
        layout.setBackgroundResource(R.drawable.tab_item_shape_check);
        //TextView textView = (TextView) layout.getChildAt(i);
        for (int j = 0; j < popupMenuViews.getChildCount(); j++) {
            popupMenuViews.getChildAt(j).setVisibility(GONE);

        }
        popupMenuViews.setVisibility(GONE);
        View menuView = popupMenuViews.getChildAt(i);
        popupMenuViews.setVisibility(VISIBLE);
        menuView.setVisibility(View.VISIBLE);
        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R
                .anim.dd_menu_in));
        maskView.setVisibility(VISIBLE);
        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim
                .dd_mask_in));
    }

    //关闭菜单时，同时关闭遮罩层
    public void closeMenu() {
        if (current_tab_position != -1) {
            LinearLayout layout = getLinearLayout(tabMenuView.getChildAt(current_tab_position));
            layout.setBackgroundResource(R.drawable.tab_item_shape_normal);
            TextView textView = getTabTextView(tabMenuView.getChildAt(current_tab_position));
            if (textView != null) {
                textView.setTextColor(Color.BLACK);
            }
            setTextDrawables(getTabImageView(tabMenuView.getChildAt(current_tab_position)), true);

            View menuView = popupMenuViews.getChildAt(current_tab_position);
            popupMenuViews.setVisibility(GONE);
            menuView.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim
                    .dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            current_tab_position = -1;


        }
    }

    public void rest(int i) {
        LinearLayout layout = getLinearLayout(tabMenuView.getChildAt(i));

        layout.setBackgroundResource(R.drawable.tab_item_shape_normal);
        TextView textView = getTabTextView(tabMenuView.getChildAt(i));
        if (textView != null) {
            textView.setTextColor(Color.BLACK);
            setTextDrawables(getTabImageView(tabMenuView.getChildAt(i)), true);
        }
    }


    private Drawable unSelectedDrawable = getResources().getDrawable(R.drawable.trandownbalck);
    private Drawable selectedDrawable = getResources().getDrawable(R.drawable.up);

    public void setTextDrawables(ImageView imageView, boolean close) {

        imageView.setImageDrawable(getRight(close));
    }

    public Drawable getRight(boolean close) {
        return close ? unSelectedDrawable : selectedDrawable;
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            getTabTextView(tabMenuView.getChildAt(current_tab_position)).setText(text);
        }
    }

    public String getTabText(int index) {
        return getTabTextView(tabMenuView.getChildAt(index)).getText().toString();
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(int index, String text) {
        if (index != -1) {
            getTabTextView(tabMenuView.getChildAt(index)).setText(text);
        }
    }

    public void resetSetTabText() {
        for (int i = 0; i < tabList.size(); i++) {
            getTabTextView(tabMenuView.getChildAt(i)).setText(tabList.get(i));
        }
    }

    /**
     * 获取tabView中id为tv_tab的textView
     *
     * @param tabView
     * @return
     */
    private TextView getTabTextView(View tabView) {
        TextView tabtext = tabView.findViewById(R.id.tv_tab);
        return tabtext;
    }

    /**
     * 获取tabView中id为tv_layout的LinearLayout
     *
     * @param tabView
     * @return
     */
    private LinearLayout getLinearLayout(View tabView) {
        LinearLayout layout = tabView.findViewById(R.id.tv_layout);
        return layout;
    }

    private ImageView getTabImageView(View tabView) {
        ImageView imageView = tabView.findViewById(R.id.right_image);
        return imageView;
    }
}
