package com.hundsun.zjfae.common.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.hundsun.zjfae.common.base.BaseApplication;
import com.hundsun.zjfae.common.utils.AESCrypt;

/**
  * @Package:        com.hundsun.zjfae.common.user
  * @ClassName:      UserShowTimeSharedPre
  * @Description:    存储用户弹框信息
  * @Author:         moran
  * @CreateDate:     2019-11-01 15:26
  * @UpdateUser:     更新者：moran
  * @UpdateDate:     2019-11-01 15:26
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class UserShowTimeSharedPre {



    private static SharedPreferences mSP = null;
    private static Context context = BaseApplication.getInstance();

    private static UserShowTimeSharedPre userShowTimeSharedPre;

    public static UserShowTimeSharedPre getInstance(String account){

        synchronized (UserShowTimeSharedPre.class){

            if (userShowTimeSharedPre == null){
                userShowTimeSharedPre = new UserShowTimeSharedPre();
            }
        }
        mSP = context.getSharedPreferences(account,
                Context.MODE_PRIVATE);

        return userShowTimeSharedPre;
    }



     /**
      * 存储用户每月一弹的等级广告时间
      * @method
      * @date: 2019-11-01 15:31
      * @author: moran
      * @param levelTime 用户每月一弹的等级广告时间
      * @return
      */
    public void saveUserLevelShowTime(String levelTime){

        saveStringData("userLevelShowTime",levelTime);

    }


     /**
      * 获取 用户每月一弹的等级广告时间
      * @method
      * @date: 2019-11-01 15:33
      * @author: moran
      * @param
      * @return
      */
    public String getUserLevelShowTime(){

        String levelTime = getStringData("userLevelShowTime");

        if (levelTime == null){

            return "";

        }
        return levelTime;

    }



    /**
     * 存储用户每月一弹的等级广告时间
     * @method
     * @date: 2019-11-01 15:31
     * @author: moran
     * @param dayTime 用户每月一弹的等级广告时间
     * @return
     */
    public void saveUserDayShowTime(String dayTime){

        saveStringData("userDayShowTime",dayTime);

    }


    /**
     * 获取 用户每月一弹的等级广告时间
     * @method
     * @date: 2019-11-01 15:33
     * @author: moran
     * @param
     * @return
     */
    public String getUserDayShowTime(){

        String dayTime = getStringData("userDayShowTime");

        if (dayTime == null){

            return "";

        }
        return dayTime;

    }




    /**
     * 存储每日一弹时间
     * @method
     * @date: 2019-11-01 14:58
     * @author: moran
     * @param time 每日一弹时间
     * @return
     */
    public  void saveDayShowTime(String time){

        saveStringData("dayTime",time);

    }


    /**
     * 获取每日一弹时间
     * @method
     * @date: 2019-11-01 14:59
     * @author: moran
     * @return 每日一弹时间
     */
    public  String getDayShowTime(){

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
    public  void saveLevelShowTime(String time){

        saveStringData("levelTime",time);

    }



    /**
     * 获取每月一弹等级时间
     * @method
     * @date: 2019-11-01 15:03
     * @author: moran
     * @return 每月一弹等级时间
     */
    public  String getLevelShowTime(){

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
    public  void saveDayRetTime(String retTime){

        saveStringData("dayRetTime",retTime);


    }




    /**
     * 获取每日一弹请求时间
     * @method
     * @date: 2019-11-01 15:03
     * @author: moran
     * @return 每日一弹请求时间
     */
    public  String getDayRetTime(){

        String dayRetTime = getStringData("dayRetTime");

        if (dayRetTime == null){


            return "";
        }

        return dayRetTime;
    }





    /**
     * 存储每月一弹请求时间
     * @method
     * @date: 2019-11-01 15:04
     * @author: moran
     * @param retTime 每月一弹请求时间
     */
    public  void saveLevelRetTime(String retTime){

        saveStringData("levelRetTime",retTime);


    }




    /**
     * 获取每月一弹请求时间
     * @method
     * @date: 2019-11-01 15:03
     * @author: moran
     * @return 每月一弹请求时间
     */
    public  String getLevelRetTime(){

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
    public  void saveIsShowState(String isShow){

        saveStringData("isShow",isShow);

    }


    /**
     * 获取弹框展示状态
     * @method
     * @date: 2019-11-01 15:10
     * @author: moran
     * @return isShow
     */
    public  String getIsShowState(){

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
    public  void saveIsUpdateSate(String isUpdate){

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
    public  String getIsUpdate(){

        String isUpdate = getStringData("isUpdate");

        if (isUpdate == null){


            return "1";
        }

        return isUpdate;
    }




    /**
     * 存储首页弹框广告信息
     * @method
     * @date: 2019-11-01 16:45
     * @author: moran
     * @param adJson 首页弹框广告信息
     * @return
     */
    public  void setHomeAdContent(String adJson){

        saveStringData("adJson",adJson);

    }


    public  String  getHomeAdContent(){


        return getStringData("adJson");

    }







    private   boolean saveStringData(Context context, String key,
                                         String value) {
        if (context == null || key == null || value == null) {
            return false;
        }

        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, AESCrypt.encrypt(value));
        return editor.commit();
    }

    private   boolean saveStringData( String key,
                                          String value) {
        if (context == null || key == null || value == null) {
            return false;
        }

        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(key, AESCrypt.encrypt(value));
        return editor.commit();
    }

    private static String getStringData(Context context, String key) {
        String value = "";
        if (context == null || key == null) {
            return value;
        }

        if (mSP.getString(key, null) != null) {
            value = AESCrypt.decrypt(mSP.getString(key, null));
        }
        return value;

    }

    private static String getStringData(String key) {
        String value = "";
        if (context == null || key == null) {
            return value;
        }

        if (mSP.getString(key, null) != null) {
            value = AESCrypt.decrypt(mSP.getString(key, null));
        }
        return value;

    }




}
