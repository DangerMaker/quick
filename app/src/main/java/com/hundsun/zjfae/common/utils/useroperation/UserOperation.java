package com.hundsun.zjfae.common.utils.useroperation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.utils.PhoneInfoUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 * @Description:保存用户操作过程中 接口报错等问题日志
 * @Author: zhoujianyu
 * @Time: 2019/2/25 15:01
 */
public class UserOperation {

    private static volatile UserOperation mInstance;
    private File operationFile = null;
    private Context mContext;

    private final String operation = "operation.txt";

    public static UserOperation getInstance() {
        if (mInstance == null) {
            synchronized (UserOperation.class){
                mInstance = new UserOperation();
            }

        }
        return mInstance;
    }

    public void initFile(Context context) {
        this.mContext = context;
    }

    /**
     * 保存接口报错信息到文件中(业务逻辑错误)
     *
     * @param pbName
     * @param errorCode
     * @param msg
     * @return
     */
    public void saveErrorToFile(String pbName, String errorCode, String msg) {
        StringBuffer operationInfo = new StringBuffer();
        operationInfo.append("\n").append("/*********************************用户操作信息**************************************/").append("\n");
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            operationInfo.append(key).append(" = ").append(value).append("\n");
        }
        operationInfo.append("pbName:").append(pbName).append("\n");
        operationInfo.append("returnCode:").append(errorCode).append("\n");
        operationInfo.append("returnMsg:").append(msg).append("\n");

        if (operationFile == null){
            operationFile =  FileUtil.createFile(mContext,operation);
        }

        if (!operationFile.exists()){
            operationFile = FileUtil.createFile(mContext,operation);
        }
        if (operationFile != null && operationFile.exists()){
            FileWriter writer = null;
            try {
                writer = new FileWriter(operationFile,true);
                writer.write(operationInfo.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (writer != null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public  void deleteCreateFile() {
        if (operationFile != null && operationFile.exists()){
            operationFile.delete();
        }
    }

    /**
     * 获取文件内容
     *
     * @return
     */
    public String readTxtFile() {
        StringBuffer sBuffer = new StringBuffer();

        if (operationFile == null){
            operationFile =  FileUtil.createFile(mContext,operation);
        }
        if (operationFile != null && operationFile.exists()){
            try {
                InputStream inputStream = new FileInputStream(operationFile);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                try {
                    //分行读取
                    while ((line = bufferedReader.readLine()) != null) {
                        sBuffer.append(line + "\n");
                    }
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return sBuffer.toString();
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private  String obtainExceptionInfo(Throwable throwable) {
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
    private  HashMap<String, String>  obtainSimpleInfo(Context context) {
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
        map.put("记录时间", getAssignTime("yyyy-MM-dd-HH:mm"));
        //奔溃版本名字
        map.put("versionName", mPackageInfo.versionName);
        //奔溃版本号
        map.put("versionCode", mPackageInfo.versionCode + "");
        //用户名称
        map.put("用户账号user_id", UserInfoSharePre.getAccount());
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //获取本机号码
//        map.put("phone本机号码", tm.getLine1Number());
        map.put("机型", Build.MANUFACTURER);
        map.put("型号",Build.MODEL);
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
    private  String getAssignTime(String dateFormatStr) {
        DateFormat dataFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(currentTime);
    }


}
