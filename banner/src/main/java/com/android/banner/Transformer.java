package com.android.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.android.banner.transformer.AccordionTransformer;
import com.android.banner.transformer.BackgroundToForegroundTransformer;
import com.android.banner.transformer.CubeInTransformer;
import com.android.banner.transformer.CubeOutTransformer;
import com.android.banner.transformer.DefaultTransformer;
import com.android.banner.transformer.DepthPageTransformer;
import com.android.banner.transformer.FlipHorizontalTransformer;
import com.android.banner.transformer.FlipVerticalTransformer;
import com.android.banner.transformer.ForegroundToBackgroundTransformer;
import com.android.banner.transformer.RotateDownTransformer;
import com.android.banner.transformer.RotateUpTransformer;
import com.android.banner.transformer.ScaleInOutTransformer;
import com.android.banner.transformer.StackTransformer;
import com.android.banner.transformer.TabletTransformer;
import com.android.banner.transformer.ZoomInTransformer;
import com.android.banner.transformer.ZoomOutSlideTransformer;
import com.android.banner.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
