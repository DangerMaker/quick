package com.zjfae.captcha.face;

/**
 * @ProjectName:
 * @Package:        com.zjfae.captcha.face
 * @ClassName:     腾讯人脸识别回调
 * @Description:     java类作用描述
 * @Author:         moran
 * @CreateDate:     2019/7/15 16:40
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/15 16:40
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface TencentFaceCallBack {


    /**
     * SDK返回封装结果
     * @param faceStatus 识别成功返回结果
     * */
    void onSuccess(TencentFaceStatus faceStatus);

    /**
     * SDK识失败结果
     * @param faceError 识别失败返回错误信息
     * */
    void onError(TencentFaceError faceError);

    /**
     * SDK初始化失败
     * */
    void onError();


    /**
     * SDK初始化结束
     * */
    void onComplete();
}
