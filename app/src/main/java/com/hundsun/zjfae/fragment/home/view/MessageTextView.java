package com.hundsun.zjfae.fragment.home.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

public class MessageTextView extends TextView {

    public MessageTextView(Context context) {
        this(context,null);
    }

    public MessageTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
//        final Resources res =super.getResources();
//        final Configuration config = res.getConfiguration();
//        if (config.fontScale > FONT_SCALE) {
//            config.fontScale=FONT_SCALE;
//        }
//        res.updateConfiguration(config,res.getDisplayMetrics());
    }

    private static final float FONT_SCALE = 1.0f;


}
