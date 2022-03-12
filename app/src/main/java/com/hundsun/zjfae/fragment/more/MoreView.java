package com.hundsun.zjfae.fragment.more;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gensazj.UrlParams;

/**
 * @Description:更多view
 * @Author: zhoujianyu
 * @Time: 2019/1/15 10:52
 */
public interface MoreView extends BaseView {

    /**
     * 获取更多列表数据
     * @param data 更多列表数据
     * */
    void loadData(UrlParams.Ret_PBAPP_urlparams data);

    /**
     * 意见反馈跳转之前请求brokerNo
     * @param brokerNo 拼接参数
     * */
    void getBrokerNo(String brokerNo);

    /**
     * 意见跳转之前Url
     * @param url 跳转urll
     * @param isShare 是否分享
     * */
    void getFeedbackUrl(String url,String isShare);
}
