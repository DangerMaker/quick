package com.hundsun.zjfae.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewSwitcher;

public class ProductViewSwitcher extends ViewSwitcher {
    public ProductViewSwitcher(Context context) {
        super(context);
    }

    public ProductViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setFactory(ViewFactory factory) {
        super.setFactory(factory);
    }
}
