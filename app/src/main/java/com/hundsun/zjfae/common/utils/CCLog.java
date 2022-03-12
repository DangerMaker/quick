package com.hundsun.zjfae.common.utils;




import android.util.Log;

import com.hundsun.zjfae.BuildConfig;
import com.hundsun.zjfae.common.base.GlobalConstant;
import com.hundsun.zjfae.fragment.home.bean.LoginUtils;


/**
 * 应用程序调试LOG
 *
 * @ClassName: TMLog
 * @Description: (Release模式，正式发布时关闭log输出)
 */
public class CCLog {
	public static final String TAG = "吴波清";
	public static final boolean RELEASE = !BuildConfig.DEBUG;
	public static void e(String tag, Object msg) {
		if (RELEASE) {
			return;
		}

		Log.e(tag, msg.toString());
	}

	public static void e(Object msg) {
		if (RELEASE) {
			return;
		}
		Log.e(TAG, msg.toString());
	}

	public static void d(String tag, Object msg) {
		if (RELEASE) {
			return;
		}
		Log.d(tag, msg.toString());
	}

	public static void d(Object msg) {
		if (RELEASE) {
			return;
		}
		CCLog.d(TAG, msg.toString());
	}

	public static void w(String tag, Object msg) {
		if (RELEASE) {
			return;
		}
		Log.w(tag, msg.toString());
	}

	public static void w(Object msg) {
		if (RELEASE) {
			return;
		}
		CCLog.w(TAG, msg);
	}

	public static void v(String tag, Object msg) {
		if (RELEASE) {
			return;
		}
		Log.v(tag, msg.toString());
	}

	public static void v(String msg) {
		if (RELEASE) {
			return;
		}
		CCLog.v(TAG, msg);
	}

	public static void i(String tag, Object msg) {
		if (RELEASE) {
			return;
		}
		Log.i(tag, msg.toString());
	}

	public static void i(Object msg) {
		if (RELEASE) {
			return;
		}
		CCLog.i(TAG,  msg);
	}

}
