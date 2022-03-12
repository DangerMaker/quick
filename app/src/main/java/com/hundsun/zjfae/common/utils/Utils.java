package com.hundsun.zjfae.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hundsun.zjfae.common.base.GlobalConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class Utils {

    /**
     * 获取系统时间戳
     */
    public static long getCurTimeLong() {
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * 文字太长了，点击显示全部
     *
     * @param context  Toast形式表现
     * @param textView
     */
    public static void canShowComplete(final Context context,
                                       final TextView textView) {
        if (textView == null) {
            return;
        }

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TextPaint paint = textView.getPaint();
                float textWidth = Layout.getDesiredWidth(textView.getText(),
                        paint);
                int textViewWidth = textView.getMeasuredWidth();

                if (textWidth > textViewWidth) {
                    Toast toast = Toast.makeText(context, textView.getText(),
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    /**
     * 去除制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources()
                        .getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * EditText是否为空的判断
     */
    public static boolean isViewEmpty(TextView view) {

        if (isStringEmpty(strFromView(view))) {
            return true;
        }
        return false;
    }

    /**
     * 从TextView或者EditText组件中获得内容
     */
    public static String strFromView(View view) {
        String strText = "";
        if (null != view) {
            if ((view instanceof TextView)) {
                strText = ((TextView) view).getText().toString().trim();
            } else if (view instanceof EditText) {
                strText = ((EditText) view).getText().toString().trim();
            }
        }
        return strText;
    }


    /***
     *  String rule = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$";
     *  正则判断密码是否纯数字或字母
     *
     * */
    private static String rule = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$";

    public static boolean isRule(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches() ? true : false;
    }


    /**
     * 两个string 是否相等
     */
    public static boolean isEqual(String str1, String str2) {
        if (null == str2) {
            return false;
        }
        if (str1.equals(str2)) {
            return true;
        }
        return false;
    }

    /**
     * String 是否为空判断
     */
    public static boolean isStringEmpty(String str) {

        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isViewEmpty(View view) {
        if (view == null) {
            return true;
        }
        String strText = "";
        if ((view instanceof TextView)) {
            strText = ((TextView) view).getText().toString().trim();
        } else if (view instanceof EditText) {
            strText = ((EditText) view).getText().toString().trim();
        }
        return isStringEmpty(strText);
    }

    /**
     * bitmap To base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream byteArrayoutStream = null;
        try {
            if (bitmap != null) {
                byteArrayoutStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        byteArrayoutStream);

                byteArrayoutStream.flush();
                byteArrayoutStream.close();

                byte[] bitmapBytes = byteArrayoutStream.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);

            }
        } catch (IOException e) {
        } finally {
            try {
                if (byteArrayoutStream != null) {
                    byteArrayoutStream.flush();
                    byteArrayoutStream.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }

    /**
     * base64 To bitmap
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (OutOfMemoryError e) {
        } catch (IllegalArgumentException e) {
        }

        return bitmap;
    }

    public static Bitmap stringToBitmap(String data) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = data.getBytes("UTF-8");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (OutOfMemoryError e) {
        } catch (IllegalArgumentException e) {
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * UTF-8格式化
     */
    public static String strURLEncoder(String strValue) {
        String newValue = "";
        try {
            newValue = URLEncoder.encode(strValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return newValue;
    }

    public static boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = activityManager
                .getRunningTasks(100);
        boolean isAppRunning = false;
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfoList) {
            if (runningTaskInfo.topActivity.getPackageName().equals(
                    GlobalConstant.PACKAGE_NAME)
                    || runningTaskInfo.baseActivity.getPackageName().equals(
                    GlobalConstant.PACKAGE_NAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * Returns a list of application processes that are running on the device
     *
     * @return true : App running onForeground <br>
     * false : App not running onForeground <br>
     */
    public static boolean isAppOnForeground(Context context) {
        boolean isAppOnForeground = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            isAppOnForeground = false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(GlobalConstant.PACKAGE_NAME)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                isAppOnForeground = true;
            }
        }

        return isAppOnForeground;
    }

    /**
     * drawable 对象转为 bitmap
     *
     * @param drawable
     * @return　Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 保存图片 bitmap
     *
     * @param picName 　　file name
     * @param bm      bitmap
     */
    public static void saveBitmap(String picName, Bitmap bm) {
        File f = new File(GlobalConstant.DESIGN_DEFAULT_IMAGE_PATH);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /**
     * 从文件路径下取得图片
     *
     * @param filepath
     * @return bitmap
     */
    public static Bitmap getImageFromSDCard(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(filepath);
            return bm;
        }
        return null;
    }

    /**
     * format expires date
     *
     * @param strExpires
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String formatExpires(String strExpires) {
        Date date = new Date(strExpires);
        return String.valueOf(date.getTime());
    }

    /**
     * 判断数组是否越界
     *
     * @param mCookieValues
     * @return
     */
    public static boolean isArrayIndexOutOfBounds(String[] mCookieValues) {
        return mCookieValues.length > 1;
    }

    /**
     * 日期格式化
     *
     * @param strNoticeDate
     * @param oldFormat
     * @param newFormat
     * @return
     */

    public static String formatDate(String strNoticeDate, String oldFormat,
                                    String newFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
            SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
            strNoticeDate = sdf2.format(sdf.parse(strNoticeDate));
        } catch (ParseException e) {
        }
        return strNoticeDate;
    }

    private static long lastClickTime;

    /**
     * 快速连击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 文件最后的修改时间
     */
    public static String getFileLastModifiedTime(String filePath) {
        String path = filePath.toString();
        File f = new File(path);
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat(
                GlobalConstant.PUSH_DATE_FOEMAT + " "
                        + GlobalConstant.NOTICE_TIME_FOEMAT);
        cal.setTimeInMillis(time);

        return formatter.format(cal.getTime());
    }

    /**
     * 判断指定的文件名是否存在
     */
    public static boolean fileIsExists(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Drawable bitmapToDrawable(Resources resources, Bitmap bitmap) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
        Drawable drawable = bitmapDrawable.getCurrent();
        return drawable;
    }

    // use for camera
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    public static float getScreenRate(Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                b.getHeight(), matrix, false);
        return rotaBitmap;
    }

    // use for camera
    public static byte[] compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length > 1024 * 500) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 1;
        }
        return baos.toByteArray();
    }

    /**
     * bytes to bitmap
     */
    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static String getCheckResult(boolean isChecked) {
        String checkMsg = GlobalConstant.EMPTY_STRING;
        if (isChecked) {
            checkMsg = GlobalConstant.VALID_VALUE;
        } else {
            checkMsg = GlobalConstant.INVALID_VALUE;
        }
        return checkMsg;
    }

    public static String resetCity(String lbsCity) {
        String reset = GlobalConstant.EMPTY_STRING;
        if (!isStringEmpty(lbsCity)) {
            if (lbsCity.contains("市")) {
                reset = lbsCity.replace("市", "");
            } else {
                reset = lbsCity;
            }
        }
        return reset;
    }

    public static String resetDistrict(String district) {
        String reset = GlobalConstant.EMPTY_STRING;
        if (!isStringEmpty(district)) {
            if (district.contains("区")) {
                reset = district.replace("区", "");
            } else {
                reset = district;
            }
        }
        return reset;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本号code
     */
    public static String getVersionCode(Context context) {
        String versionCode = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
            return versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            // options.inSampleSize = 4;
            image = BitmapFactory.decodeStream(is, null, options);
            // image = BitmapFactory.decodeStream(is);
            is.close();

            // if (!image.isRecycled()) {
            // image.recycle(); // 回收图片所占的内存
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }


    public static String getYearMonthTimeDate(){
        final Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        buffer.append(year);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        buffer.append(month);

        return buffer.toString();

    }


    /**
     * 处理真实姓名：前一位显示， 中间部分*号表示
     */
    public static String parseRelName(String originalRelName) {
        String name = "";
        if (Utils.isStringEmpty(originalRelName)) {
            return name;
        }
        if (originalRelName.length() > 1) {

            try {
                String pre = originalRelName.substring(0, 1);
                name = pre + "**";
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

        return name;
    }

    /**
     * 处理身份证号码 ：前3后3位显示， 中间部分*号表示
     */
    public static String parseIDCard(String originalID) {
        String idString = "";
        if (Utils.isStringEmpty(originalID)) {
            return idString;
        }
        int length = originalID.length();
        if (length > 6) {
            try {
                String preOriString = originalID;
                String pre = preOriString.substring(0, 3);
                String after = originalID.substring(length - 3, length);
                StringBuffer sBuffer = new StringBuffer();
                String mid = "";
                for (int i = 0; i < length - 6; i++) {
                    mid = sBuffer.append("*").toString();
                }
                idString = pre + mid + after;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

        return idString;
    }

    /**
     * 处理手机号码：前3后3位显示， 中间部分*号表示
     */
    public static String parseTelPhone(String originalTel) {
        String tel = "";
        if (Utils.isStringEmpty(originalTel)) {
            return tel;
        }
        if (originalTel.length() > 8) {

            try {
                String pre = originalTel.substring(0, 3);
                String after = originalTel.substring(7);
                tel = pre + "****" + after;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

        return tel;
    }

    /**
     * 处理邮箱
     */
    public static String parseEmail(String email) {
        String tel = "";
        if (Utils.isStringEmpty(email)) {
            return tel;
        }

        if (email.length() > 1) {
            String pre = email.substring(0, 1);
            String after = email.substring(7);
            tel = pre + "*****" + after;
        }

        return tel;

    }

    /***
     * 校验银行卡是否规范
     *
     * **/
    public static String isBankNumber(String bankno) {
        CCLog.e(bankno.length() + ":" + bankno);
        final String SUCCESS = "true";
        final String BAD_LENGTH = "银行卡号长度必须在16到19之间";


        final String NOT_NUMBER = "银行卡必须全部为数字";


        final String ILLEGAL_NUMBER = "银行卡不符合规则";

        if (bankno.length() < 16 || bankno.length() > 19) {
            return BAD_LENGTH;
        }


        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher match = pattern.matcher(bankno);
        if (match.matches() == false) {
            return NOT_NUMBER;
        }
        int lastNum = Integer.parseInt(bankno.substring(bankno.length() - 1,
                bankno.length()));// 取出最后一位（与luhm进行比较）


        String first15Num = bankno.substring(0, bankno.length() - 1);// 前15或18位
        // System.out.println(first15Num);
        char[] newArr = new char[first15Num.length()]; // 倒叙装入newArr
        char[] tempArr = first15Num.toCharArray();
        for (int i = 0; i < tempArr.length; i++) {
            newArr[tempArr.length - 1 - i] = tempArr[i];
        }
        // System.out.println(newArr);


        int[] arrSingleNum = new int[newArr.length]; // 奇数位*2的积 <9
        int[] arrSingleNum2 = new int[newArr.length];// 奇数位*2的积 >9
        int[] arrDoubleNum = new int[newArr.length]; // 偶数位数组


        for (int j = 0; j < newArr.length; j++) {
            if ((j + 1) % 2 == 1) {// 奇数位
                if ((int) (newArr[j] - 48) * 2 < 9)
                    arrSingleNum[j] = (int) (newArr[j] - 48) * 2;
                else
                    arrSingleNum2[j] = (int) (newArr[j] - 48) * 2;
            } else
                // 偶数位
                arrDoubleNum[j] = (int) (newArr[j] - 48);
        }


        int[] arrSingleNumChild = new int[newArr.length]; // 奇数位*2 >9
        // 的分割之后的数组个位数
        int[] arrSingleNum2Child = new int[newArr.length];// 奇数位*2 >9
        // 的分割之后的数组十位数


        for (int h = 0; h < arrSingleNum2.length; h++) {
            arrSingleNumChild[h] = (arrSingleNum2[h]) % 10;
            arrSingleNum2Child[h] = (arrSingleNum2[h]) / 10;
        }


        int sumSingleNum = 0; // 奇数位*2 < 9 的数组之和
        int sumDoubleNum = 0; // 偶数位数组之和
        int sumSingleNumChild = 0; // 奇数位*2 >9 的分割之后的数组个位数之和
        int sumSingleNum2Child = 0; // 奇数位*2 >9 的分割之后的数组十位数之和
        int sumTotal = 0;
        for (int m = 0; m < arrSingleNum.length; m++) {
            sumSingleNum = sumSingleNum + arrSingleNum[m];
        }


        for (int n = 0; n < arrDoubleNum.length; n++) {
            sumDoubleNum = sumDoubleNum + arrDoubleNum[n];
        }


        for (int p = 0; p < arrSingleNumChild.length; p++) {
            sumSingleNumChild = sumSingleNumChild + arrSingleNumChild[p];
            sumSingleNum2Child = sumSingleNum2Child + arrSingleNum2Child[p];
        }


        sumTotal = sumSingleNum + sumDoubleNum + sumSingleNumChild
                + sumSingleNum2Child;


        // 计算Luhm值
        int k = sumTotal % 10 == 0 ? 10 : sumTotal % 10;
        int luhm = 10 - k;


        if (lastNum == luhm) {
            return SUCCESS;// 验证通过
        } else {
            return ILLEGAL_NUMBER;
        }
    }


    /*********************************** 身份证验证开始 ****************************************/

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public static String IDCardValidate(String IDStr) {
        /*********************************** 身份证验证开始 ****************************************/
        /**
         * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
         * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
         * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
         * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
         * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
         * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
         * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
         * （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
         */

        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "true";
        }
        // =====================(end)=====================
        return "true";
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings("unchecked")
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }
    /*********************************** 身份证验证结束 ****************************************/
    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 时间戳转换
     *
     * @return
     */
    public static String getCurrentTime_Today(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        return sdf.format(new Date(date * 1000));
    }


    public static String getEncodeURIComponent(String var) {
        try {
            //return URLDecoder.decode(var, "UTF-8");
            return URLEncoder.encode(var,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getDeCodeURIComponent(String var) {
        try {
            return URLDecoder.decode(var, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    /********************************金额大小写转换开始**********************************************/

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾",
            "佰", "仟"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;


    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param money 输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(String money) {
        money = money.replaceAll(",", "");
        if (money.length() == 1 && money.equals(".")) {
            money = "0.";
        }
        money = money.trim();
//        if (money.length() > 13) {
//            return "超出计算范围";
//        }
        BigDecimal numberOfMoney = new BigDecimal(money);
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }


    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param money 输入的金额
     * @return 对应的汉语大写
     */
    public static String numberCNMontrayUnit(String money) {
        money = money.replaceAll(",", "");
        if (money.length() == 1 && money.equals(".")) {
            money = "0.";
        }
        money = money.trim();
        if (money.length() >= 13) {
            return "超出计算范围";
        }
        BigDecimal numberOfMoney = new BigDecimal(money);
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        return sb.toString();
    }

    /********************************金额大小写转换结束**********************************************/


    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     *
     * @return
     */
    public static int getDay() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.DATE);
    }


    private static long hour = 24L;//24小时

    private static long minute = 60L;//一小时

    private static long second = 60L;//一分钟

    private static long day = hour * minute * second;

    //86400
    //1440
    public static void transitionTime(long countTime, TextView textView) {
        //减去一分钟
        countTime = countTime - second;
        //大于一天
        if (countTime - day > 0) {
            StringBuffer timeBuf = new StringBuffer();
            //有几天
            long day_time = countTime / day;
            CCLog.e("day_time", day_time);
            timeBuf.append(day_time).append("天");
            //几小时
            long minute_time = (countTime % day) / (minute * second);
            CCLog.e("minute_time++++", minute_time);
            CCLog.e("minute_time", minute_time * day);
            timeBuf.append(minute_time).append("小时");
            textView.setText(timeBuf.toString());
        }
        //小于一天
        else {

            //小于1小时
            if (countTime < (minute * second)) {
                countDown(countTime, textView);
//                StringBuffer timeBuf = new StringBuffer();
//                //154
//                //显示分/秒
//                long minute_time =countTime / second;
//                timeBuf.append(minute_time).append("分钟");
//                CCLog.e("这里有问题");
//                long second_item = (countTime % second) /second;
//                timeBuf.append(second_item).append("秒");
            } else {
                StringBuffer timeBuf = new StringBuffer();
                //几小时
                long minute_time = countTime / (minute * second);
                timeBuf.append(minute_time).append("小时");
                //几分钟
                long second_item = countTime % (minute * second) / second;
                timeBuf.append(second_item).append("分钟");
                textView.setText(timeBuf.toString());
            }


        }
    }

    private static void countDown(long time, final TextView textView) {
        textView.setEnabled(false);
        Flowable.intervalRange(0, time, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        StringBuffer timeBuf = new StringBuffer();
                        long minute_time = aLong / second;
                        timeBuf.append(minute_time).append("分钟");
                        long second_item = (aLong % second) / second;
                        timeBuf.append(second_item).append("秒");
                        textView.setText(timeBuf.toString());
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //倒计时完毕置为可点击状态
                        textView.setEnabled(true);
                        textView.setText("立即购买");
                    }
                })
                .subscribe();

    }

    /**
     * 校检手机号
     */
    public static boolean isPhone(String str) {

        if (str.length() < 11) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(1[0-9])\\d{9}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 校检url网址是否合法
     **/
    public static boolean checkUrl(String url) {

        String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
                + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
                + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
                + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
                + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
                + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
                + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
                + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();

    }

    /**
     * 用于textview显示 一行右对齐 多行左对齐
     *
     * @param tv
     */
    public static void setTextViewGravity(final TextView tv) {
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (tv.getLineCount() == 1) {
                    tv.setGravity(Gravity.RIGHT | Gravity.CENTER);
                } else {
                    tv.setGravity(Gravity.LEFT | Gravity.CENTER);
                }
                return true;
            }
        });
    }


}
