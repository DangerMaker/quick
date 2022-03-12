package com.hundsun.zjfae.common.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 字符串操作工具包
 *
 * @author wubq
 * @version 1.0
 * @created 2016-06-01
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    // ^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
    // private final static SimpleDateFormat dateFormater = new
    // SimpleDateFormat(
    // ^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$

    // "yyyy-MM-dd HH:mm:ss");
    // private final static SimpleDateFormat dateFormater2 = new
    // SimpleDateFormat(
    // "yyyy-MM-dd");
    private final static String REPLACE_PATTERN = "＜br\\s*/?＞|＜p\\s*/?＞|[\\s\\n]";

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @SuppressLint("SimpleDateFormat")
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 获取某月的天数
     *
     * @param year
     * @param month
     * @return
     */

    public static int getLastDayOfMonth(int year, int month) {
        int day = 1;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        int last = cal.getActualMaximum(Calendar.DATE);
        return last;

    }

    /**
     * 将float类型转换成 0.00格式
     *
     * @param flo
     * @return
     */
    public static String ReTurnMoney(float flo) {
        String reult = "";
        DecimalFormat df2 = new DecimalFormat("###.00");
        reult = df2.format(flo);
        if (reult.startsWith(".")) {
            reult = "0" + reult;
        }
        return reult;
    }

    /**
     * 将float类型转换成 0.00格式
     *
     * @param dou
     * @return
     */
    public static String ReTurnMoney(double dou) {
        String reult = "";
        DecimalFormat df2 = new DecimalFormat("###.00");
        reult = df2.format(dou);
        if (reult.startsWith(".")) {
            reult = "0" + reult;
        }
        return reult;
    }

    /**
     * 将数值较大的钱以万显示
     *
     * @param flo
     * @return
     */
    public static String ReTurnBingMoney(int flo) {
        String result = "";
        if (flo >= 0 && flo < 10000) {
            result = flo + "";
        } else if (flo >= 10000 && flo < 10000000) {
            result = flo / 10000.00 + "万";
        } else if (flo >= 10000000) {
            result = flo / 10000000.00 + "千万";
        }
        return result;
    }

    /**
     * 获取字符串中想要的字符
     *
     * @param source
     * @param type   0 数字;1字母;2汉字
     * @return
     */
    public static String getWantString(String source, int type) {

        return source;
    }

    public static String replaceBlank(String source) {
        String result = null;
        if (!Utils.isStringEmpty(source)) {
            result = source.replaceAll(REPLACE_PATTERN, "");

        } else {
            result = null;
        }

        return result;
    }

    /**
     * 字符串转JSON
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public static JSONObject toJSONObject(String json) {
        if (!isEmpty(json)) {
            if (json.indexOf("[") == 0) {
                json = json.substring(1, json.length());
            }
            if (json.lastIndexOf("]") == json.length()) {
                json = json.substring(0, json.length() - 1);
            }
            try {
                return new JSONObject(json);
            } catch (JSONException e) {
                CCLog.e("json异常");
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 字符串转JSON
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public static JSONArray toJSONArray(String json) throws JSONException {
        if (!isEmpty(json)) {
            if (json.indexOf("[") == 0) {
                json = json.substring(1, json.length());
            }
            if (json.lastIndexOf("]") == json.length()) {
                json = json.substring(0, json.length() - 1);
            }
        }
        return new JSONArray(json);
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取倒计时时间
     *
     * @param countTime
     * @return
     */
    public static String formatCancelCountDown(long countTime) {
        StringBuffer timeBuf = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("00");

        int hour = (int) (Math.floor(countTime / (60 * 60)));
        timeBuf.append(decimalFormat.format(hour)).append(":");

        int minute = (int) (Math.floor((countTime / 60) % 60));
        timeBuf.append(decimalFormat.format(minute)).append(":");

        int seconds_time = (int) (countTime % 60);

        timeBuf.append(decimalFormat.format(seconds_time));
        return timeBuf.toString();
    }

//	/**
//	 * 获取手机IMEI
//	 *
//	 * @return
//	 */
//	public static String getIMEI() {
//		TelephonyManager telephonyManager = (TelephonyManager) BaseApplacation
//				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
//		return telephonyManager.getDeviceId();
//	}

    /**
     * 时间戳转换
     *
     * @param timestampString
     * @return
     */
    public static String TimeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        return dateFormater.get().format(new Date(timestamp));
    }

    /**
     * 以友好的方式显示距离
     *
     * @param distance
     * @return
     */
    public static String friendly_Distance(double distance) {
        String dis = "";
        if (distance >= 1000) {
            if (distance / 1000 >= 1000) {
                dis = "<100km";
            } else if (distance / 1000 >= 100) {
                dis = "<100km";
            } else if (distance / 1000 >= 10) {
                dis = "<10km";
            } else if (distance / 1000 >= 10) {
                dis = "<1km";
            } else {
                dis = "<1km";
                // dis = String.format("%.1f", (distance / 1000)) + "km";
            }
        } else {
            dis = "<1km";
            // dis = distance + "m";
        }
        // Log.v("以友好的方式显示距离", distance + "   " + dis);
        return dis;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 时间戳转换
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentTime_Today(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        return sdf.format(new Date(date * 1000));
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @return
     */
    public static String dataOne() {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = sdfs.format(new Date());
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }


    /***
     *
     * 字母排序
     * String [] str;
     * */
    public static String Sort(String[] str, Map<String, String> map) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length - 1; i++) {
            for (int j = i + 1; j < str.length; j++) {
                int intTemp = str[i].compareToIgnoreCase(str[j]);
                String strTemp = "";
                if (intTemp > 0) {
                    strTemp = str[j];
                    str[j] = str[i];
                    str[i] = strTemp;
                }
            }
        }
        for (String s : str) {
            buffer.append(map.get(s));
        }
        return buffer.toString();
    }

    /**
     * 转换服务器数据格式（中文）
     *
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toServerUtf8(String value) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }


    /**
     * 当前string替换内容
     **/

    public static String replaceAll(String value, String regex, String replacement) {
        if (value != null && value != "") {
            value.replaceAll(regex, replacement);
        }
        return value;
    }

    /**
     * 不为空 返回真
     *
     * @param str 字符串
     * @return 如果字符串不为空且长度大于1 返回真 ，其他返回假
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().equals("") && !str.equals("null");
    }
}
