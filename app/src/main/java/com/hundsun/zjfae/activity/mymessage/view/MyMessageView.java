package com.hundsun.zjfae.activity.mymessage.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gensazj.ListMessagePB;

/**
 * @Description:我的消息（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface MyMessageView extends BaseView {
    void loadData(List<ListMessagePB.PBIFE_messagemanage_listMessage.MessageListList> list, String unreadMessageCount);
    void setReadMessageResponse(String code,String message);

}
