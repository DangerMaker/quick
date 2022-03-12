package com.hundsun.zjfae.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hundsun.zjfae.common.base.BaseApplication;

/**
 * 手机内存储存数据
 *
 * @ClassName SharedPreferenceAccesser
 * @author laolang(借鉴小米)
 */
public class SharedPreferenceAccesser {

	private static final String PREFERENCE_NAME = "ZJFAE";
	private static SharedPreferences mSP = null;
	private static Context context = BaseApplication.getInstance();

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
