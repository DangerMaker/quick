package com.hundsun.zjfae.common.utils.stepcounter.bean;

/**
 * @Description:
 * @Author: zhoujianyu
 * @Time: 2019/7/5 16:45
 */
public class StepBeanEntity {
    private String data;//时间戳
    private int stepCount;//计步器的数量

    public StepBeanEntity(String data, int stepCount) {
        this.data = data;
        this.stepCount = stepCount;
    }

    public StepBeanEntity() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
