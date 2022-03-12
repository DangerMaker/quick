package com.hundsun.zjfae.common.user;


import android.content.Context;
import android.content.SharedPreferences;

import com.hundsun.zjfae.common.base.BaseApplication;
import com.hundsun.zjfae.common.utils.AESCrypt;

/**
  * @Package:        com.hundsun.zjfae.common.user
  * @ClassName:      BaseSharedPreferences
  * @Description:    基类缓存
  * @Author:         moran
  * @CreateDate:     2019-11-01 14:56
  * @UpdateUser:     更新者：moran
  * @UpdateDate:     2019-11-01 14:56
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class BaseSharedPreferences {


    private static final String PREFERENCE_NAME = "BaseSharedPresences";


    private static SharedPreferences mSP = null;
    private static Context context = BaseApplication.getInstance();


     /**
      * 存储每日一弹时间
      * @method
      * @date: 2019-11-01 14:58
      * @author: moran
      * @param time 每日一弹时间
      * @return
      */
    public static void saveDayShowTime(String time){

        saveStringData("dayTime",time);

    }


     /**
      * 获取每日一弹时间
      * @method
      * @date: 2019-11-01 14:59
      * @author: moran
      * @return 每日一弹时间
      */
    public static String getDayShowTime(){

        String time = getStringData("dayTime");

        if (time == null){


            return"";
        }

        return time;
    }



     /**
      * 储存每月一弹等级时间
      * @method
      * @date: 2019-11-01 15:01
      * @author: moran
      * @param time 每月一弹等级时间
      */
    public static void saveLevelShowTime(String time){

        saveStringData("levelTime",time);

    }



     /**
      * 获取每月一弹等级时间
      * @method
      * @date: 2019-11-01 15:03
      * @author: moran
      * @return 每月一弹等级时间
      */
    public static String getLevelShowTime(){

        String levelTime = getStringData("levelTime");

        if (levelTime == null){


            return "";
        }

        return levelTime;
    }



     /**
      * 存储每日一弹请求时间
      * @method
      * @date: 2019-11-01 15:04
      * @author: moran
      * @param retTime 每日一弹请求时间
      */
    public static void saveDayRetTime(String retTime){

        saveStringData("dayRetTime",retTime);


    }




    /**
     * 获取每日一弹请求时间
     * @method
     * @date: 2019-11-01 15:03
     * @author: moran
     * @return 每日一弹请求时间
     */
    public static String getDayRetTime(){

        String dayRetTime = getStringData("dayRetTime");

        if (dayRetTime == null){


            return "";
        }

        return dayRetTime;
    }

    /**
     * 存储交易专区请求时间
     * @method
     * @date: 2019-11-01 15:04
     * @author: moran
     * @param retTime 每日一弹请求时间
     */
    public static void saveTradeIconRetTime(String retTime){
        saveStringData("tradeIconRetTime",retTime);
    }


    /**
     * 获取交易专区请求时间
     * @method
     * @date: 2019-11-01 15:03
     * @author: moran
     * @return 每日一弹请求时间
     */
    public static String getTradeIconRetTime(){

        String dayRetTime = getStringData("tradeIconRetTime");

        if (dayRetTime == null){
            return "";
        }

        return dayRetTime;
    }

    /**
     * 起息日提示
     * @method
     * @date: 2019-11-01 15:04
     * @author: moran
     * @param tips 起息日提示
     */
    public static void saveManageStartDateTips(String tips){
        saveStringData("manageStartDateTips",tips);
    }

    /**
     * 获取起息日提示
     * @method
     * @date: 2019-11-01 15:03
     * @author: moran
     * @return 起息日提示
     */
    public static String getManageStartDateTips(){

        String tips = getStringData("manageStartDateTips");

        if (tips == null){
            return "";
        }

        return tips;
    }


    /**
     * 存储每月一弹请求时间
     * @method
     * @date: 2019-11-01 15:04
     * @author: moran
     * @param retTime 每月一弹请求时间
     */
    public static void saveLevelRetTime(String retTime){

        saveStringData("levelRetTime",retTime);


    }




    /**
     * 获取每月一弹请求时间
     * @method
     * @date: 2019-11-01 15:03
     * @author: moran
     * @return 每月一弹请求时间
     */
    public static String getLevelRetTime(){

        String dayRetTime = getStringData("levelRetTime");

        if (dayRetTime == null){


            return "";
        }

        return dayRetTime;
    }



     /**
      * 弹框会展示
      * @method
      * @date: 2019-11-01 15:09
      * @author: moran
      * @param isShow 是否展示（0-不展示 ，1-展示）
      * @return
      */
    public static void saveIsShowState(String isShow){

        saveStringData("isShow",isShow);

    }


     /**
      * 获取弹框展示状态
      * @method
      * @date: 2019-11-01 15:10
      * @author: moran
      * @return isShow
      */
    public static String getIsShowState(){

        String isShow = getStringData("isShow");

        if (isShow == null){


            return "";
        }

        return isShow;
    }



     /**
      * 是否更新
      * @method
      * @date: 2019-11-01 15:54
      * @author: moran
      * @param
      * @return
      */
    public static void saveIsUpdateSate(String isUpdate){

        saveStringData("isUpdate",isUpdate);

    }


     /**
      * 获取是否更新
      * @method
      * @date: 2019-11-01 15:55
      * @author: moran
      * @param
      * @return
      */
    public static String getIsUpdate(){

        String isUpdate = getStringData("isUpdate");

        if (isUpdate == null){


            return "1";
        }

        return isUpdate;
    }


    /**
     * 交易专区图片是否更新
     * @method
     * @date: 2019-11-01 15:54
     * @author: moran
     * @param
     * @return
     */
    public static void saveTradeIsUpdateSate(String isUpdate){
        saveStringData("isTradeUpdate",isUpdate);
    }


    /**
     * 交易专区图片获取是否更新
     * @method
     * @date: 2019-11-01 15:55
     * @author: moran
     * @param
     * @return
     */
    public static String getTradeIsUpdate(){
        String isUpdate = getStringData("isTradeUpdate");
        if (isUpdate == null){
            return "1";
        }
        return isUpdate;
    }

    public static boolean saveStringData(Context context, String key,
                                         String value) {
        if (context == null || key == null || value == null) {
            return false;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, AESCrypt.encrypt(value));
        return editor.commit();
    }

    public static boolean saveStringData( String key,
                                          String value) {
        if (context == null || key == null || value == null) {
            return false;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, AESCrypt.encrypt(value));
        return editor.commit();
    }

    public static boolean saveBooleanData( String key,
                                           boolean value) {
        if (context == null || key == null) {
            return false;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putBoolean(key, value);
        return editor.commit();

    }

    public static boolean getBooleanData(Context context, String key) {
        boolean value = false;
        if (context == null || key == null) {
            return value;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        value = mSP.getBoolean(key, false);
        return value;

    }

    public static boolean getBooleanData( String key,boolean defValue) {
        boolean value = false;
        if (context == null || key == null) {
            return value;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        value = mSP.getBoolean(key, defValue);
        return value;

    }

    public static String getStringData(Context context, String key) {
        String value = null;
        if (context == null || key == null) {
            return value;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        if (mSP.getString(key, null) != null) {
            value = AESCrypt.decrypt(mSP.getString(key, null));
        }
        return value;

    }

    public static String getStringData(String key) {
        String value = null;
        if (context == null || key == null) {
            return value;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        if (mSP.getString(key, null) != null) {
            value = AESCrypt.decrypt(mSP.getString(key, null));
        }
        return value;

    }

    public static boolean deleteData(String key) {
        if (context == null || key == null) {
            return false;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean clearAllData() {
        if (context == null) {
            return false;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSP.edit();
        return editor.clear().commit();
    }



    public static boolean saveLong(String key, long value){
        if (context == null || key == null) {
            return false;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSP.edit();
        editor.putLong(key,value);
        return editor.commit();
    }

    public static long getLong(String key){
        long value = 0;
        if (context == null || key == null) {
            return value;
        }
        if (mSP == null) {
            mSP = context.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        value = mSP.getLong(key,0);
        return value;
    }

}
