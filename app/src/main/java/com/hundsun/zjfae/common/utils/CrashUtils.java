package com.hundsun.zjfae.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.hundsun.zjfae.common.user.UserInfoSharePre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName:
 * @Package:        com.hundsun.zjfae.common.utils
 * @ClassName:      CrashUtils
 * @Description:     记录奔溃信息及关键接口异常信息
 * @Author:         moran
 * @CreateDate:     2019/7/5 9:52
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/5 9:52
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class CrashUtils {

    private static CrashUtils crashUtils = null;
    private Context mContext;
    private File crashFile = null;
    private final String crash = "crash.txt";

    public void init(Context context) {
        this.mContext = context;
    }


    public static CrashUtils getInstance() {
        if (crashUtils == null) {
            crashUtils = new CrashUtils();
        }
        return crashUtils;
    }

    private CrashUtils() {

    }

    public void saveCrashFile(Throwable ex) {
        String fileName = "";
        StringBuffer crashInfo = new StringBuffer();
        crashInfo.append("\n").append("/*********************************奔溃日志**************************************/").append("\n");
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            crashInfo.append(key).append(" = ").append(value).append("\n");
        }
        crashInfo.append(obtainExceptionInfo(ex)).append("\n");

        if (crashFile == null) {
            crashFile = FileUtil.createFile(mContext, crash);
        }

        if (!crashFile.exists()) {
            crashFile = FileUtil.createFile(mContext, crash);
        }
        if (crashFile != null && crashFile.exists()) {
            fileName = crashFile.getAbsolutePath();
            FileWriter writer = null;
            try {
                writer = new FileWriter(fileName, true);
                writer.write(crashInfo.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public void saveCrashFile(String errorInfo) {
        String fileName = "";
        StringBuffer crashInfo = new StringBuffer();
        crashInfo.append("\n").append("/*********************************奔溃日志**************************************/").append("\n");
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            crashInfo.append(key).append(" = ").append(value).append("\n");
        }
        crashInfo.append(errorInfo).append("\n");

        if (crashFile == null) {
            crashFile = FileUtil.createFile(mContext, crash);
        }

        if (!crashFile.exists()) {
            crashFile = FileUtil.createFile(mContext, crash);
        }
        if (crashFile != null && crashFile.exists()) {
            fileName = crashFile.getAbsolutePath();
            FileWriter writer = null;
            try {
                writer = new FileWriter(fileName, true);
                writer.write(crashInfo.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 读取文件内容
     *
     * @return
     */
    public String readTxtFile() {
        StringBuffer sBuffer = new StringBuffer();
        if (crashFile == null) {
            crashFile = FileUtil.createFile(mContext, crash);
        }
        if (crashFile != null && crashFile.exists()) {
            if (!crashFile.isDirectory()) {
                try {
                    InputStream instream = new FileInputStream(crashFile);
                    if (instream != null) {
                        InputStreamReader inputreader = new InputStreamReader(instream);
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line;
                        //分行读取
                        while ((line = buffreader.readLine()) != null) {
                            sBuffer.append(line + "\n");
                        }
                        instream.close();
                    }
                } catch (java.io.FileNotFoundException e) {

                    CCLog.e("文件不存在", e.getMessage());
                } catch (IOException e) {
                    CCLog.e("IO读取错误", e.getMessage());
                }
            }
        }

        return sBuffer.toString();
    }


    /**
     * 奔溃日志上传成功以后删除缓存文件
     */
    public void deleteCrashFile() {
        //测试环境 先不删除文件
        if (crashFile != null && crashFile.exists()) {
            crashFile.delete();
        }
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }


    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //奔溃时间
        map.put("奔溃时间Crash_Time", getAssignTime("yyyy-MM-dd-HH:mm"));
        //奔溃版本名字
        map.put("versionName", mPackageInfo.versionName);
        //奔溃版本号
        map.put("versionCode", mPackageInfo.versionCode + "");
        //用户名称
        map.put("用户账号user_id", UserInfoSharePre.getAccount());
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //获取本机号码
//        map.put("phone本机号码", tm.getLine1Number());
        map.put("机型", Build.MANUFACTURER);
        map.put("型号", Build.MODEL);
        //android版本
        map.put("安卓版本", Build.VERSION.RELEASE);
        //SDK版本
        map.put("SDK_INT", Build.VERSION.SDK_INT + "");
        //获取智能设备唯一编号
        map.put("IMEI", new PhoneInfoUtils(context).getUniquePsuedoID());
        return map;
    }

    /**
     * 返回当前日期根据格式
     **/
    private String getAssignTime(String dateFormatStr) {
        DateFormat dataFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }


}
