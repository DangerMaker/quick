package com.zjfae.jpush;


import android.content.Context;

/**
 * @ProjectName:
 * @Package:        com.zjfae.jpush
 * @ClassName:      OpenJPushMessage
 * @Description:    极光推送回调
 * @Author:         moran
 * @CreateDate:     2019/6/24 8:44
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/24 8:44
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public  interface OpenJPushMessage {


    /**
     * 极光推送回调
     * @param context 上下文对象
     * @param result json对象
     * */
    void openMessage(Context context, Extras result);




}
