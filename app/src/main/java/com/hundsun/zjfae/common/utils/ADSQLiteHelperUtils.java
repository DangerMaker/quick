package com.hundsun.zjfae.common.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hundsun.zjfae.fragment.home.bean.ADUtils;

import java.util.ArrayList;
import java.util.List;

public class ADSQLiteHelperUtils {

    private static final MyDatabaseHelper databaseHelper = SQLiteUtils.getSQLiteOpenHelper();
    //------表名
    private static final String AD_TAB = "ad";

    private static final String TYPE = "type";

    private static final String CONTENT = "content";

    private static final String RESTIME = "resTime";



    public static final String CREATE_AD_TAB =  "create table "+AD_TAB+" ( "
            +TYPE+" text ,"
            +CONTENT +" text ,"
            +RESTIME +" text )";

    /**
     * 查询
     * */
    public static Cursor select(ADUtils homeAD ){

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        //查询 select * from 表名 where 条件
        Cursor cursor =	database.rawQuery("select * from "+AD_TAB+" where "+TYPE+" = '"+homeAD.getType()+"'",null);
        return cursor;
    }

    /**
     * 查询
     * */
    public static Cursor select(String type ){

        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        //查询 select * from 表名 where 条件
        Cursor cursor =	database.rawQuery("select * from "+AD_TAB+" where "+TYPE+" = '"+type+"'",null);
        return cursor;
    }

    /**
     * 插入
     *
     * */

    public static void insert(ADUtils homeAD){

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String sql = " insert into "+AD_TAB+" ( "+TYPE+" ,"+CONTENT+" ,"+RESTIME+" ) values ( '"+homeAD.getType()+"','"+homeAD.getContent()+"','"+homeAD.getResTime()+"' ) ";



        database.execSQL(sql);
    }


    public static void update (ADUtils homeAD){
        //update 表名 set 更改值 where 条件
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String sql = "update "+AD_TAB+" set "+CONTENT+" = '"+ homeAD.getContent() +"',"+RESTIME+" = '"+homeAD.getResTime()+"' where "+TYPE+" = '"+homeAD.getType()+"'";
        database.execSQL(sql);
    }

    public static List<ADUtils> getAdList(Cursor cursor){
        List<ADUtils> adUtilsList = new ArrayList<>();
        try {

            if (cursor!=null&&cursor.moveToFirst()){

                do {
                    ADUtils home = new ADUtils();
                    home.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                    home.setContent(cursor.getString(cursor.getColumnIndex(CONTENT)));
                    home.setResTime(cursor.getString(cursor.getColumnIndex(RESTIME)));
                    adUtilsList.add(home);
                }
                while (cursor.moveToNext());
            }


            return adUtilsList;
        }
        catch (Exception e){

        }
        finally {
            cursor.close();
        }

        return adUtilsList;
    }


    public static void deleteData(){

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String sql = " delete from " + AD_TAB;
        database.execSQL(sql);

    }


}
