package com.hundsun.zjfae.common.base;

import com.hundsun.zjfae.BuildConfig;

/**
 * 常量定义
 * 
 * @ClassName GlobalConstant
 * @Description 全局常量类
 * @author laolang
 */
public class GlobalConstant {
	

	// LOG 输出的tag名字
	public static final String TAG = "Lucky";
	// 应用程序的包名
	public static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;
	// 进度对话框默认的加载文字
	public static final String PROGRESS_DIALOG_MESSAGE = "Loading";
	public static final String COPYRIGHT_EN = "@2017 zsjr Network Technology CO.,Ltd";

	public static String VALID_VALUE = "1";
	public static String INVALID_VALUE = "0";

	// ******** 经常使用的字符 开始 ********* //
	public static final String HYPHEN = "-";
	public static final String COMMA = "、";
	public static String EMPTY_STRING = "";
	public static String SPACE = " ";
	public static String DATE_BAR = "/";
	public static String DASH = "～";
	public static final String LEFT_BRACKET = "（";
	public static final String RIGHT_BRACKET = "）";
	public static String UTF_8_CODE = "utf-8";
	public static String HTTP = "http";
	public static String HTTPS = "https";
	public static String TLS = "TLS";
	// ******** 经常使用的字符 结束 ********* //

	// ******** 时间日期格式 开始 ********* //
	public static String PUSH_DATE_FOEMAT = "yyyy年MM月dd日";
	public static String TRANSFER_DATE_FOEMAT = "yyyy/MM/dd";
	public static String NOTICE_TIME_DATE_FOEMAT = "dd HH:mm";
	public static String NOTICE_TIME_FOEMAT = "HH:mm";
	// ******** 时间日期格式 结束 ********* //

	public static String CHECKBOX_ON = "ON";
	public static String CHECKBOX_OFF = "OFF";
	public static String DESIGN_DEFAULT_IMAGE_PATH = "";//app文件路径



}
