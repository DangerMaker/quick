package com.hundsun.zjfae.activity.mine.bean;

import com.hundsun.zjfae.common.utils.SharedPreferenceAccesser;



@Deprecated
public class UnbindBankImageSharedPreference extends SharedPreferenceAccesser {


    private static String MODEL = "UnbindBankModel";
    private static String PATH = "UnbindBankIMAGE_PATH";
    private static String DYNAMICKEY = "UnbindBankDynamicKey";
    private static String DYNAMICVALUE = "UnbindBankDynamicValue";

    /***
     * 存储图片当前对应的model
     *
     * */
    public static void saveImageModel(int key,String value){
        saveStringData(MODEL+key,value);
    }


    /**
     * 存储当前图片路径
     * **/
    public static void saveImagePath(int key,String value){
        saveStringData(PATH+key,value);
    }

    /**
     * 存储当前图片key
     *
     * **/

    public static void saveDynamicKey(int key,String value){
        saveStringData(DYNAMICKEY+key,value);
    }

    /**
     * 存储当前图片value
     *
     * **/
    public static void saveDynamicValue(int key,String value){

        saveStringData(DYNAMICVALUE+key,value);
    }

    /**
     * 获取图片地址
     * **/
    public static String getImagePath(int key){

        return getStringData(PATH+key);
    }


    /**
     * 获取model
     * **/
    public static String getImageModel(int key){
        return getStringData(MODEL+key);
    }
    /**
     * 获取图片key
     *
     * **/
    public static String getDynamicKey(int key){
        return getStringData(DYNAMICKEY+key);
    }


    /**
     * 获取图片value
     * **/
    public static String getDynamicValue(int key){
        return getStringData(DYNAMICVALUE+key);
    }


    /**
     * 删除图片路径
     * */

    public static void deleteKey(int key){
        deleteData(MODEL+key);
        deleteData(PATH+key);
        deleteData(DYNAMICKEY+key);
        deleteData(DYNAMICVALUE+key);
    }

}
