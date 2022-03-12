package com.hundsun.zjfae.activity.home;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.adapter.MessageAdapter;
import com.hundsun.zjfae.activity.home.presenter.MessagePresenter;
import com.hundsun.zjfae.activity.home.view.MessageView;
import com.hundsun.zjfae.activity.mymessage.MyMessageActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.util.List;

public class MessageActivity extends CommActivity<MessagePresenter> implements MessageView {


    private RecyclerView message_RecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected MessagePresenter createPresenter() {
        return presenter = new MessagePresenter(this);
    }

    @Override
    public void initView() {
        setTitle("消息中心");
        message_RecyclerView = findViewById(R.id.message_RecyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.requestMessageIcon();
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.message_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void userMessageIcon(final List<onight.zjfae.afront.AllAzjProto.PBAPPIcons> listBeanList) {
        MessageAdapter adapter = new MessageAdapter(MessageActivity.this, listBeanList);
        message_RecyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        //message_RecyclerView.addItemDecoration(new DividerItemDecorations());
        message_RecyclerView.setAdapter(adapter);
        adapter.setMessageOnclick(new MessageAdapter.MessageOnclick() {
            @Override
            public void itemOnclick(int i) {
                if (listBeanList.get(i).getLinkLocation().equals("myMessage")) {
                    Intent intent = new Intent(MessageActivity.this, MyMessageActivity.class);
                    baseStartActivity(intent);
                } else {
                    ShareBean shareBean = new ShareBean();
                    StringBuffer buffer = new StringBuffer(listBeanList.get(i).getIconsLink());
                    buffer.append("?terminal=2");
                    shareBean.setFuncUrl(buffer.toString());
                    startWebActivity(shareBean);
                }
            }
        });
    }
}
