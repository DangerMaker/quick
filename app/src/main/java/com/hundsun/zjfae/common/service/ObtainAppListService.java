package com.hundsun.zjfae.common.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObtainAppListService extends IntentService {

    public ObtainAppListService(){

        super(ObtainAppListService.class.getName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ObtainAppListService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    private void postRequestAppListInfo(String appListData){

    }

}
