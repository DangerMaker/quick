package com.hundsun.zjfae.activity.mymessage.presenter;

import com.hundsun.zjfae.activity.mymessage.view.MyMessageView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import onight.zjfae.afront.gensazj.ListMessagePB;
import onight.zjfae.afront.gensazj.ReadMessagePB;

/**
 * @Description:我的消息（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class MyMessagePresenter extends BasePresenter<MyMessageView> {

    public MyMessagePresenter(MyMessageView baseView) {
        super(baseView);
    }

    /**
     * 获取消息列表数据
     */
    public void getMyMessageListData(int pageIndex) {

        ListMessagePB.REQ_PBIFE_messagemanage_listMessage.Builder builder = ListMessagePB.REQ_PBIFE_messagemanage_listMessage.newBuilder();
        builder.setPageIndex(pageIndex + "");
        builder.setPageSize("18");
        builder.setVersion("1.0.0");
        Map<String, String> hashmap = getRequestMap();
        hashmap.put("version", "v2");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.MyMessage, hashmap);
        addDisposable(apiServer.getMyMessageListData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ListMessagePB.Ret_PBIFE_messagemanage_listMessage>(baseView) {
            @Override
            public void onSuccess(ListMessagePB.Ret_PBIFE_messagemanage_listMessage data) {
                baseView.loadData(data.getData().getMessageListListList(), data.getData().getUnreadMessageCount());
            }

        });
    }

    /**
     * 修改未读消息个数方法
     * id:[id1|00][id2|01]
     */
    public void setReadMessage(String id) {
        ReadMessagePB.REQ_PBIFE_messagemanage_readMessageBatch.Builder builder = ReadMessagePB.REQ_PBIFE_messagemanage_readMessageBatch.newBuilder();
        builder.setTotalId(id);
        builder.setVersion("1.0.0");
        Map<String, String> hashmap = getRequestMap();
        hashmap.put("version", "v2");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.MyMessageRead, hashmap);
        addDisposable(apiServer.setReadMessage(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ReadMessagePB.Ret_PBIFE_messagemanage_readMessageBatch>(baseView) {
            @Override
            public void onSuccess(ReadMessagePB.Ret_PBIFE_messagemanage_readMessageBatch ret_pbife_messagemanage_readMessageBatch) {
                baseView.setReadMessageResponse(ret_pbife_messagemanage_readMessageBatch.getReturnCode(), ret_pbife_messagemanage_readMessageBatch.getReturnMsg());
            }


        });

    }
}
