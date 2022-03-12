package com.hundsun.zjfae.common.utils.dbutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.hundsun.zjfae.activity.logingesture.widget.ACache;
import com.hundsun.zjfae.activity.logingesture.widget.LockPatternUtil;
import com.hundsun.zjfae.common.user.AssetProof;
import com.hundsun.zjfae.common.user.ChangeCard;
import com.hundsun.zjfae.common.user.PhoneInfo;
import com.hundsun.zjfae.common.user.UnBindCard;
import com.hundsun.zjfae.common.user.UserInfo;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserSetting;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.PhoneInfoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;

public class Updata {

    private static final String local_storage = "Local_Storage";

    private static final String localFileName = "file__0.localstorage";

    private static boolean isUpData = false;

    private static final String NAME = "zjfae_user_info";

    private static final String USER_KEY = "userInfo";


    public static void upData(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);


        PhoneInfo phoneInfo = PhoneInfo.getPhoneInfo();
        String imei = phoneInfo.deviceId;

        String newImei = new PhoneInfoUtils(context).getUniquePsuedoID();
        CCLog.i("Updata", "imei = " + imei);
        CCLog.i("Updata", "newImei = " + newImei);
        CCLog.i("Updata", "imei ：newImei = " + (!TextUtils.isEmpty(imei) && !",".equals(imei) && !"null,null".equals(imei) && !imei.equals(newImei)));
        //设备id不为空并且设备Id不一致，清除数据
        if (imei != null && !",".equals(imei) && !"null,null".equals(imei) && !imei.equals(newImei)) {
            UserInfoSharePre.deleteData();

            phoneInfo.deviceId = newImei;
            PhoneInfo.putData(phoneInfo);
        }

        //是否读取过数据/true-读取，false-未读取
        isUpData = preferences.getBoolean("isUpData", false);
        if (!isUpData) {
            UserSetting userSetting = UserSetting.getUserSettingInfo();
            String userData = preferences.getString(USER_KEY, "");
            //userData为空，没有数据
            if (userData != null && !userData.equals("")) {

                try {
                    JSONObject object = new JSONObject(userData);

                    //指纹
                    String loginFinger = object.optString("login_finger");

                    //设置指纹
                    if (loginFinger.equals("true")) {
                        userSetting.fingerprint_state = true;

                    }
                    //没有设置指纹
                    else {
                        userSetting.fingerprint_state = false;
                    }

                    //手势
                    String loginPattern = object.optString("login_pattern");
                    //设置手势锁
                    if (loginPattern != null && !loginPattern.equals("")) {
                        userSetting.gesture_state = true;
                        byte[] bytes = LockPatternUtil.patternStringToHash(loginPattern);
                        ACache aCache = ACache.get(context);
                        aCache.put("GesturePassword", bytes);
                    }
                    // 没有设置手势锁
                    else {
                        userSetting.gesture_state = false;
                    }
                    //是否记住用户名
                    String rememberMe = object.optString("rememberMe");
                    //记住用户名
                    if (rememberMe.equals("true")) {
                        userSetting.isCheckUserName = true;
                    } else {
                        userSetting.isCheckUserName = false;
                    }

                    UserSetting.putData(userSetting);

                    UserInfo userInfo = UserInfo.getUserData();
                    String user = object.optString("user");
                    JSONObject userObject = new JSONObject(user);

                    String userName = userObject.optString("username");
                    userInfo.loginName = userName;

                    String passWord = userObject.optString("password");
                    userInfo.passWord = passWord;

                    String moBile = userObject.optString("mobile");
                    userInfo.mobile = moBile;

                    UserInfo.putData(userInfo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

                loadFileData(context);
            }

            SharedPreferences.Editor editor = preferences.edit();

            editor.putBoolean("isUpData", true);

            editor.commit();

        }


    }


    private static void loadFileData(Context context) {


        String databasePath = context.getExternalFilesDir(null).getPath();

        UserInfo userInfo = UserInfo.getUserData();
        UserSetting userSetting = UserSetting.getUserSettingInfo();

        File file = new File(databasePath);

        if (file.exists()) {

            File localPath = new File(file, local_storage);

            if (localPath.exists()) {

                File dataFile = new File(localPath, localFileName);

                if (dataFile.exists()) {

                    final String sql = "select * from ItemTable";

                    SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dataFile, null);
                    Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

                    //返回中行数
                    int count = cursor.getCount();
                    Log.e("count", count + "");

                    try {

                        if (cursor != null && cursor.moveToFirst()) {

                            do {

                                byte[] key = cursor.getBlob(cursor.getColumnIndex("key"));

                                byte[] value = cursor.getBlob(cursor.getColumnIndex("value"));

                                String keyName = new String(key, "UTF-8").trim();

                                if (keyName.equals("user")) {

                                    String userData = new String(value, "UTF-16LE");

                                    JSONObject object = new JSONObject(userData);

                                    String userName = object.optString("username");
                                    String passWord = object.optString("password");
                                    String mobile = object.optString("mobile");

                                    userInfo.loginName = userName;
                                    userInfo.passWord = passWord;
                                    userInfo.mobile = mobile;

                                }
                                //是否指纹登录
                                else if (keyName.equals("login_finger")) {

                                    String loginFingerType = new String(value, "UTF-16LE");

                                    userSetting.fingerprint_state = Boolean.parseBoolean(loginFingerType);

                                }
                                //手势密码
                                else if (keyName.equals("login_pattern")) {
                                    String gesture = new String(value, "UTF-16LE");
                                    if (!gesture.equals("")) {
                                        userSetting.gesture_state = true;
                                        //添加手势密码
                                        byte[] bytes = LockPatternUtil.patternStringToHash(gesture);
                                        ACache aCache = ACache.get(context);
                                        aCache.put("GesturePassword", bytes);
                                    } else {
                                        userSetting.gesture_state = false;
                                    }
                                }
                                //是否记住密码
                                else if (keyName.equals("rememberMe")) {

                                    String rememberMe = new String(value, "UTF-16LE");

                                    if (rememberMe.equals("true")) {

                                        userSetting.isCheckUserName = true;
                                    } else {
                                        userSetting.isCheckUserName = false;
                                    }

                                }

                            }
                            while (cursor.moveToNext());
                        }
                        UserInfo.putData(userInfo);
                        UserSetting.putData(userSetting);
                    } catch (Exception e) {

                        Log.e("Exception", e.getMessage());
                    } finally {
                        cursor.close();
                        dataFile.delete();

                    }


                }

            }


        }
    }

    /**
     * @param slotId slotId为卡槽Id，它的值为 0、1；
     * @return
     */
    private static String getIMEI(Context context, int slotId) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = manager.getClass().getMethod("getImei", int.class);
            String imei = (String) method.invoke(manager, slotId);
            return imei;
        } catch (Exception e) {
            return "";
        }
    }
}
