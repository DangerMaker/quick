package com.hundsun.zjfae.common.utils;

import android.content.Context;

public class SQLiteUtils {

    private static MyDatabaseHelper databaseHelper;

    private static final String DATE_NAME = "zjfae";
    private static final int DATE_VERSION = 1;

    public static void initSQL(Context context){

        databaseHelper = new MyDatabaseHelper(context,DATE_NAME,null,DATE_VERSION);
    }


    public static MyDatabaseHelper getSQLiteOpenHelper(){

        return databaseHelper;
    }
}
