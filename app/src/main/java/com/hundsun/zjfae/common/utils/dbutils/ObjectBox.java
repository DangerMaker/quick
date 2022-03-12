package com.hundsun.zjfae.common.utils.dbutils;

import android.content.Context;

import com.hundsun.zjfae.BuildConfig;
import com.hundsun.zjfae.common.user.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class ObjectBox {

    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder().androidContext(context).build();
        if (BuildConfig.DEBUG) {
            //new AndroidObjectBrowser(boxStore).start(context);
        }
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }


}
