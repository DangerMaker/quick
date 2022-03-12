package com.hundsun.zjfae.common.utils.stepcounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.stepcounter.bean.StepBeanEntity;
import com.hundsun.zjfae.common.utils.stepcounter.bean.StepEntity;
import com.hundsun.zjfae.common.utils.stepcounter.db.StepDataDao;
import com.hundsun.zjfae.common.utils.stepcounter.utils.PreferencesHelper;
import com.hundsun.zjfae.common.utils.stepcounter.utils.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * @Description:
 * @Author: zhoujianyu
 * @Time: 2019/6/28 17:53
 */
public class StepServiceClass implements SensorEventListener {

    //当前日期
    private static String CURRENT_DATE;
    //当前计步器数值 每一次变化都重新赋值
    private int CURRENT_STEP = 0;

    //需要保存的计步器当前时刻的数据
    private int stepCount = 0;

    //传感器
    private SensorManager sensorManager;
    //数据库
    private StepDataDao stepDataDao;
    //计步传感器类型 0-counter 1-detector
    private static int stepSensor = -1;
    //广播接收
    private BroadcastReceiver mInfoReceiver;
    //是否有当天的记录
    private boolean hasRecord;
    //未记录之前的步数
    private int hasStepCount;
    //下次记录之前的步数
    private int previousStepCount;

    private Context mContext;


    public void init(Context context) {
        mContext = context;
        initBroadcastReceiver();
        new Thread(new Runnable() {
            public void run() {
                getStepDetector();
            }
        }).start();
        initTodayData();
    }


    /**
     * 初始化广播
     */
    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        mInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    // 屏幕灭屏广播
                    case Intent.ACTION_SCREEN_OFF:
                        //屏幕熄灭改为10秒一存储
                        break;
                    //关机广播，保存好当前数据
                    case Intent.ACTION_SHUTDOWN:
//                        if (stepCount == CURRENT_STEP) {
//                            return;
//                        }
//                        stepCount = CURRENT_STEP;
//                        saveStepData();
                        break;
                    // 屏幕解锁广播
                    case Intent.ACTION_USER_PRESENT:
                        break;
                    // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
                    // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
                    // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
                    case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:
//                        if (stepCount == CURRENT_STEP) {
//                            return;
//                        }
//                        stepCount = CURRENT_STEP;
//                        saveStepData();
                        break;
                    //监听日期变化
                    case Intent.ACTION_DATE_CHANGED:
                    case Intent.ACTION_TIME_CHANGED:
                    case Intent.ACTION_TIME_TICK:
                        isNewDay();
                        break;
                    default:
                        break;
                }
            }
        };
        //注册广播
        mContext.registerReceiver(mInfoReceiver, filter);
    }

    /**
     * 初始化当天数据
     */
    private void initTodayData() {
//        获取当前时间
        CURRENT_DATE = TimeUtil.getCurrentDate();
//        获取数据库
        stepDataDao = new StepDataDao(mContext);
        //获取当天的数据，用于展示
//        StepEntity entity = stepDataDao.getCurDataByDate(CURRENT_DATE);
        //为空则说明还没有该天的数据，有则说明已经开始当天的计步了
//        if (entity == null) {
//            CURRENT_STEP = 0;
//        } else {
//            CURRENT_STEP = Integer.parseInt(entity.getSteps());
//        }
//        if (PreferencesHelper.getStepToday(mContext).equals("")) {
//            PreferencesHelper.setStepToday(mContext, CURRENT_DATE);//保存当前的日子
//        }
//        if (PreferencesHelper.getStepTodayCurrentTime(mContext).equals("") || PreferencesHelper.getStepTodayCurrentTime(mContext).equals(new Date().getTime() + "")) {
//            //如果之前的日子数据为空 说明首次安装  如果跟现在的时间不一样 那不能覆盖之前的 必须先处理之前的步数
//            PreferencesHelper.setStepTodayCurrentTime(mContext, new Date().getTime() + "");
//        }
    }


    /**
     * 监听晚上0点变化初始化数据
     */
    private void isNewDay() {
        //如果两个日期不一样 就判断跨天
        if (!CURRENT_DATE.equals(TimeUtil.getCurrentDate())) {
            initTodayData();
            stepCount = 0;
            saveStepData();
        } else {
            //说明是同一天 6点 12点 18点 需要保存一下数据
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
            Date now = null;
            Date time1 = null;
            Date time2 = null;
            Date time3 = null;
            Date time4 = null;
            Date time5 = null;
            try {
                now = df.parse(df.format(new Date()));
                time1 = df.parse("00:00");
                time2 = df.parse("06:00");
                time3 = df.parse("12:00");
                time4 = df.parse("18:00");
                time5 = df.parse("23:00");
                if (now.getTime() == time1.getTime() || now.getTime() == time2.getTime() || now.getTime() == time3.getTime() || now.getTime() == time4.getTime() || now.getTime() == time5.getTime()) {
                    if (stepCount == CURRENT_STEP) {
                        return;
                    }
                    stepCount = CURRENT_STEP;
                    saveStepData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取传感器实例
     */
    private void getStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) mContext
                .getSystemService(SENSOR_SERVICE);
        //android4.4以后可以使用计步传感器
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        }
    }


    /**
     * 添加传感器监听
     */
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensor = 0;
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensor = 1;
            sensorManager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    /**
     * 由传感器记录当前用户运动步数，注意：该传感器只在4.4及以后才有，并且该传感器记录的数据是从设备开机以后不断累加，
     * 只有当用户关机以后，该数据才会清空，所以需要做数据保护
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensor == 0) {
            int tempStep = (int) event.values[0];
            CURRENT_STEP = tempStep;
            Log.e("StepServiceClass", "当前计步器数量----" + tempStep);
            if (stepCount == 0) {
                stepCount = tempStep;
                saveStepData();
            }
//            if (!hasRecord) {
//                Log.e("StepService", "当天没有历史起始步数记录----");
//                //当天没有历史起始步数记录
//                int before = (int) PreferencesHelper.getLastSensorStep(mContext);
//                Log.e("StepService", "之前保存的最后的计步器数量----" + before);
//                if (PreferencesHelper.getStepToday(mContext).equals(CURRENT_DATE)) {
//                    Log.e("StepService", "同一天");
//                    //如果是同一天的话
//                    if (before == 0) {//说明是首次安装
//                        Log.e("StepService", "首次安装");
//                        PreferencesHelper.setLastSensorStep(mContext, tempStep);
//                        PreferencesHelper.setStepTodayCurrentTime(mContext, new Date().getTime() + "");
//                    } else {
//                        if (tempStep >= before) {
//                            //计步器后面的数值大于之前的说明关掉应用已经走过了
//                            CURRENT_STEP += tempStep - before;
//                            Log.e("StepService", "当前步数--当前计步器大于上次的计步器数量--" + CURRENT_STEP);
//                        } else {
//                            //计步器后面的数值小于之前的说明手机重启过了
//                            CURRENT_STEP += tempStep;
//                            Log.e("StepService", "当前步数--当前计步器小于上次的计步器数量--" + CURRENT_STEP);
//                        }
//                    }
//                } else {//如果不是同一天的话
//                    Log.e("StepService", "不是同一天----");
//                    Log.e("StepService", "上次保存的时间----" + PreferencesHelper.getStepTodayCurrentTime(mContext));
//                    Log.e("StepService", "现在保存的时间----" + new Date().getTime() + "");
//                    if (TimeUtil.daysBetween2(PreferencesHelper.getStepTodayCurrentTime(mContext), new Date().getTime() + "") > 24) {
//                        //如果超过24小时了 就废弃不用
//                        Log.e("StepService", "大于24小时");
//                        PreferencesHelper.setStepTodayCurrentTime(mContext, new Date().getTime() + "");
//                    } else if (TimeUtil.daysBetween2(PreferencesHelper.getStepTodayCurrentTime(mContext), new Date().getTime() + "") > 0) {
//                        //小于24小时 保存为昨天的数据
//                        Log.e("StepService", "小于24小时大于0");
//                        int step = 0;
//                        if (tempStep >= before) {
//                            //计步器后面的数值大于之前的说明关掉应用已经走过了
//                            step = tempStep - before;
//                        } else {
//                            //计步器后面的数值小于之前的说明手机重启过了
//                            step = tempStep;
//                        }
//                        saveLastDayStepData(step);
//                        PreferencesHelper.setStepTodayCurrentTime(mContext, new Date().getTime() + "");
//                    } else {
//                        PreferencesHelper.setStepTodayCurrentTime(mContext, new Date().getTime() + "");
//                        Log.e("StepService", "小于0");
//                    }
//
//                }
//                hasRecord = true;
//                hasStepCount = tempStep;
//                PreferencesHelper.setStepToday(mContext, CURRENT_DATE);//保存当前的日子
//                Log.e("StepService", "保存当前的日子----" + CURRENT_DATE);
//            } else {
//                //当天存在历史起始步数记录
//                int thisStepCount = tempStep - hasStepCount;
//                CURRENT_STEP += (thisStepCount - previousStepCount);
//                previousStepCount = thisStepCount;
//            }
//            PreferencesHelper.setLastSensorStep(mContext, tempStep);
//            Log.e("StepService", "设置最后的计步器数量----" + tempStep);

        } else if (stepSensor == 1) {
            if (event.values[0] == 1.0) {
                //这里说明走了超过一步 增加计步器数据
                Log.e("StepServiceClass", "当前计步器----数值+1");
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * 保存当天的数据到数据库中，并去刷新通知栏
     */
    private void saveStepData() {
        CCLog.e("StepServiceClass----保存数据----stepCount----" + stepCount);
        if (stepCount == 0) {
            return;
        }
        //查询数据库中的数据
        StepEntity entity = stepDataDao.getCurDataByDate(CURRENT_DATE);
        StepBeanEntity stepBeanEntity = new StepBeanEntity(new Date().getTime() + "", stepCount);
        //为空则说明还没有该天的数据，有则说明已经开始当天的计步了
        if (entity == null) {
            //没有则新建一条数据
            entity = new StepEntity();
            entity.setCurDate(CURRENT_DATE);
            List<StepBeanEntity> list = new ArrayList<>();
            list.add(stepBeanEntity);
            Type type = new TypeToken<List<StepBeanEntity>>() {
            }.getType();
            JsonArray jsonArray = new Gson().toJsonTree(list, type).getAsJsonArray();
            CCLog.e("StepServiceClass----保存数据，当天没有记录----" + jsonArray.toString());


            entity.setSteps(new Gson().toJson(jsonArray));

            stepDataDao.addNewData(entity);
        } else {
            //有则更新当前的数据
            try {//防止解析出错
                Type type = new TypeToken<List<StepBeanEntity>>() {
                }.getType();
                List<StepBeanEntity> stepBeanList = new Gson().fromJson(entity.getSteps(), type);
                CCLog.e("StepServiceClass----保存数据，获取之前的记录----" + entity.getSteps());
                stepBeanList.add(stepBeanEntity);
                JsonArray jsonArray = new Gson().toJsonTree(stepBeanList, type).getAsJsonArray();
                entity.setSteps(new Gson().toJson(jsonArray));
                CCLog.e("StepServiceClass----保存数据，加上数据以后----" + entity.getSteps());
                stepDataDao.updateCurData(entity);
            } catch (Exception e) {
                CCLog.e("StepServiceClass----" + e.toString());
            }
        }
    }

    public int getStepData() {
        if (stepCount == 0) {
            return 0;
        }
        StepDataDao stepDataDao = new StepDataDao(mContext);
        StepEntity lastentity = stepDataDao.getCurDataByDate(TimeUtil.getLastDate());
        if (lastentity == null || !StringUtils.isNotBlank(lastentity.getSteps())) {//昨天没有数据
            return 0;
        }
        try {//防止解析出错
            Type type = new TypeToken<List<StepBeanEntity>>() {
            }.getType();
            List<StepBeanEntity> stepBeanList = new Gson().fromJson(lastentity.getSteps(), type);
            int lastposition = -1;
            CCLog.e("StepServiceClass----昨天的记录----" + lastentity.getSteps());
            for (int i = 0; i < stepBeanList.size(); i++) {
                CCLog.e("StepServiceClass----相差的时间----" + TimeUtil.daysBetween2(stepBeanList.get(i).getData(), new Date().getTime() + ""));
                if (TimeUtil.daysBetween2(stepBeanList.get(i).getData(), new Date().getTime() + "") > 0 && TimeUtil.daysBetween2(stepBeanList.get(i).getData(), new Date().getTime() + "") <= 24) {
                    lastposition = i;
                    break;
                }
            }
            if (lastposition >= 0) {
                if (stepCount >= stepBeanList.get(lastposition).getStepCount()) {
                    return stepCount - stepBeanList.get(lastposition).getStepCount();
                } else {
                    return stepCount;
                }
            }
        } catch (Exception e) {
            CCLog.e("StepServiceClass----" + e.toString());
        }
        return 0;
    }

    /**
     * 保存当天的数据到数据库中，并去刷新通知栏
     */
    private void saveLastDayStepData(int steps) {
        //查询数据库中的数据
        StepEntity entity = stepDataDao.getCurDataByDate(TimeUtil.getLastDate());
        //为空则说明还没有该天的数据，有则说明已经开始当天的计步了
        if (entity == null) {
            //没有则新建一条数据
            entity = new StepEntity();
            entity.setCurDate(TimeUtil.getLastDate());
            entity.setSteps(String.valueOf(steps));

            stepDataDao.addNewData(entity);
        } else {
            //有则更新当前的数据
            int before = Integer.parseInt(entity.getSteps());
            entity.setSteps(String.valueOf(before + steps));
            stepDataDao.updateCurData(entity);
        }
    }

    public void removeRegister() {
        if (mContext != null && mInfoReceiver != null) {
            mContext.unregisterReceiver(mInfoReceiver);
        }
    }
}