package com.hundsun.zjfae.common.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hundsun.zjfae.fragment.home.bean.ADUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ADSharePre {
    public static final String loginIcon = "loginIcon";//登录图---根据这个查询数据库对应的信息
    public static final String startIcons = "PicstartIcons";//启动图---根据这个查询数据库对应的信息
    public static final String homeProductIcon = "homeProductIcon";//首页产品icon配置---根据这个查询数据库对应的信息
    public static final String subscribeIcon = "subscribeList";//购买产品列表icon配置---根据这个查询数据库对应的信息
    public static final String transferIcon = "transferList";//转让产品列表icon配置---根据这个查询数据库对应的信息
    public static final String highAge = "highAge";//65周岁公告---根据这个查询数据库对应的信息
    public static final String disclaimer = "disclaimer";//免责声明---根据这个查询数据库对应的信息
    public static final String high = "high";//高净值公告---根据这个查询数据库对应的信息
    public static final String homeProductNoDate = "homeProductNoDate";//首页产品没有，或者敬请期待---根据这个查询数据库对应的信息
    public static final String homeAd = "PichomeAd";//首页广告配置---根据这个查询数据库对应的信息

    public static final String subscribeListHomeType = "subscribeListHome";//首页产品列表图标type
    public static final String homeIconsOutLogin = "PichomeIconsOutLogin";//首页banner图下面的缓存数据 登录之前
    public static final String homeIconsLogin = "PichomeIconsLogin";//首页banner图下面的缓存数据 登录以后
    public static final String homeSectionOutLogin = "PichomeSectionOutLogin";//首页理财专区缓存数据 登录之前
    public static final String homeSectionLogin = "PichomeSectionLogin";//首页理财专区缓存数据 登录以后

    public static final Map<String ,Object> iconMap = new HashMap();


    public static void saveConfiguration(String type, ADUtils adUtils) {
        CacheConfiguration configuration = new CacheConfiguration();
        configuration.content = adUtils.getContent();
        configuration.type = adUtils.getType();
        configuration.resTime = adUtils.getResTime();
        CacheConfiguration.putData(configuration);
    }


    public static <T> T getConfiguration(String key, Class<T> clazz) {

        List<CacheConfiguration> configurationList =  CacheConfiguration.getCacheConfig(key);

        if (!configurationList.isEmpty()){
            for (CacheConfiguration cacheConfiguration : configurationList){
                Gson gson = new Gson();
                T result = gson.fromJson(cacheConfiguration.content, clazz);
                return result;
            }
        }

        return null;

    }

    public static <T> List<T> getListConfiguration(String key, Class<T> clazz) {
        ArrayList<T> arrayList = new ArrayList<>();
        Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();
//        if (iconMap.containsKey(key)){
//            arrayList.add((T) iconMap.get(key));
//            return arrayList;
//        }
//        else {
//            Type type = new TypeToken<ArrayList<JsonObject>>() {
//            }.getType();
//            Cursor cursor = ADSQLiteHelperUtils.select(key);
//            List<ADUtils> adUtilsList = ADSQLiteHelperUtils.getAdList(cursor);
//
//            if (!adUtilsList.isEmpty()){
//                for (ADUtils adUtils : adUtilsList) {
//                    ArrayList<JsonObject> jsonObjects = new Gson().fromJson(adUtils.getContent(), type);
//                    if (jsonObjects != null) {
//                        for (JsonObject jsonObject : jsonObjects) {
//                            arrayList.add(new Gson().fromJson(jsonObject, clazz));
//                        }
//                    }
//                }
//                cursor.close();
//            }
//            else {
//               List<CacheConfiguration> configurationList = CacheConfiguration.getCacheConfig(key);
//               for (CacheConfiguration configuration :configurationList ){
//                   ArrayList<JsonObject> jsonObjects = new Gson().fromJson(configuration.content, type);
//                   if (jsonObjects != null) {
//                       for (JsonObject jsonObject : jsonObjects) {
//                           arrayList.add(new Gson().fromJson(jsonObject, clazz));
//                       }
//                   }
//               }
//            }
//        }
        List<CacheConfiguration> configurationList = CacheConfiguration.getCacheConfig(key);
        for (CacheConfiguration configuration :configurationList ){
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(configuration.content, type);
            if (jsonObjects != null) {
                for (JsonObject jsonObject : jsonObjects) {
                    arrayList.add(new Gson().fromJson(jsonObject, clazz));
                }
            }
        }

        return arrayList;
    }


    //首页banner图下缓存图标
    public static <T> List<T> getHomeIcons(boolean isLogin,Class<T> cs){
        ArrayList<T> arrayList = new ArrayList<>();
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        String key = "";

        if (isLogin){
            key = ADSharePre.homeIconsLogin;
        }
        else {
            key = ADSharePre.homeIconsOutLogin;
        }

        List<CacheConfiguration> configurationList = CacheConfiguration.getCacheConfig(key);

        for (CacheConfiguration cacheConfiguration : configurationList){
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(cacheConfiguration.content, type);
            if (jsonObjects != null) {
                for (JsonObject jsonObject : jsonObjects) {
                    arrayList.add(new Gson().fromJson(jsonObject, cs));
                }
            }
        }
//        Cursor cursor = null;
//
//        if (isLogin){
//            cursor = ADSQLiteHelperUtils.select(ADSharePre.homeIconsLogin);
//        }
//        else {
//            cursor = ADSQLiteHelperUtils.select(ADSharePre.homeIconsOutLogin);
//        }
//        List<ADUtils> adUtilsList = ADSQLiteHelperUtils.getAdList(cursor);
//        for (ADUtils adUtils : adUtilsList) {
//            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(adUtils.getContent(), type);
//
//            if (jsonObjects != null) {
//                for (JsonObject jsonObject : jsonObjects) {
//                    arrayList.add(new Gson().fromJson(jsonObject, cs));
//                }
//            }
//        }
//        cursor.close();
        return arrayList;
    }


    //首页理财专区缓存图标
    public static <T> List<T> getHomeManageFinancesIcon(boolean isLogin,Class<T> cs){
        ArrayList<T> arrayList = new ArrayList<>();
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        String key = "";

        if (isLogin){
            key = homeSectionLogin;
        }
        else {
            key = homeSectionOutLogin;
        }
        List<CacheConfiguration> configurationList = CacheConfiguration.getCacheConfig(key);
        for (CacheConfiguration cacheConfiguration : configurationList){
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(cacheConfiguration.content, type);
            if (jsonObjects != null) {
                for (JsonObject jsonObject : jsonObjects) {
                    arrayList.add(new Gson().fromJson(jsonObject, cs));
                }
            }
        }

//        Cursor cursor = null;
//        //已经登录
//        if (isLogin){
//
//            cursor = ADSQLiteHelperUtils.select(homeSectionLogin);
//        }
//        else {
//
//            //未登录
//            cursor = ADSQLiteHelperUtils.select(homeSectionOutLogin);
//        }
//        List<ADUtils> adUtilsList = ADSQLiteHelperUtils.getAdList(cursor);
//        for (ADUtils adUtils : adUtilsList) {
//            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(adUtils.getContent(), type);
//
//            if (jsonObjects != null) {
//                for (JsonObject jsonObject : jsonObjects) {
//                    arrayList.add(new Gson().fromJson(jsonObject, cs));
//                }
//            }
//        }
//        cursor.close();
        return arrayList;

    }



    public static String getResTime(String type) {

        List<CacheConfiguration> configurationList = CacheConfiguration.getCacheConfig(type);

        if (!configurationList.isEmpty()){

            for (CacheConfiguration cacheConfiguration : configurationList){
                return cacheConfiguration.resTime;
            }
        }

        return "0";
    }

}
