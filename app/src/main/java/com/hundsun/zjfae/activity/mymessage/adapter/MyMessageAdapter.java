package com.hundsun.zjfae.activity.mymessage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mymessage.MyMessageDetailActivity;
import com.hundsun.zjfae.activity.mymessage.bean.MyMessageBean;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Description:我的消息 Recyclerview列表适配器
 * @Author: zhoujianyu
 * @Time: 2018/9/12 10:02
 */
public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.ViewHolder> {
    private List<MyMessageBean.BodyBean.DataBean.MessageListListBean> mList;
    private Context mContext;
    private Boolean isDelete = false;//是否批量删除
    private HashMap<Integer, String> selected = new HashMap<Integer, String>();//用于存放选中的id以及消息类型
    private HashMap<Integer, String> selectedALL = new HashMap<Integer, String>();//用于获取所有的未读的id 与selected比较大小判断是否全部选中
    private onCheckBoxChangelistener onCheckBoxChange;
    private onGoDetailListener onGoDetailListener;

    public MyMessageAdapter(Context context, List<MyMessageBean.BodyBean.DataBean.MessageListListBean> list) {
        this.mList = list;
        this.mContext = context;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void setCheckBoxChangelistener(onCheckBoxChangelistener onCheckBoxChange) {
        this.onCheckBoxChange = onCheckBoxChange;
    }

    public void setOnGoDetailListener(onGoDetailListener onGoDetailListener) {
        this.onGoDetailListener = onGoDetailListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_my_message, viewGroup, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder mViewHolder, final int position) {
        if (position == 0) {
            mViewHolder.view.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.view.setVisibility(View.GONE);
        }
        TextPaint tp = mViewHolder.tv_title.getPaint();
        if (mList.get(position).getReadStatus().equals("1")) {
            mViewHolder.message_tip.setVisibility(View.GONE);
            tp.setFakeBoldText(false);
        } else {
            mViewHolder.message_tip.setVisibility(View.VISIBLE);
            tp.setFakeBoldText(true);
        }
        mViewHolder.tv_title.setText(mList.get(position).getTitle());
        mViewHolder.tv_date.setText(mList.get(position).getCreateTime());
        mViewHolder.tv_content.setText(mList.get(position).getContentFilter());
        if (isDelete) {
            mViewHolder.checkBox.setVisibility(View.VISIBLE);
            mViewHolder.checkBox.setOnCheckedChangeListener(null);
            if (mList.get(position).getCheck() == true) {
                mViewHolder.checkBox.setChecked(true);
                selected.put(position, mList.get(position).getId() + "|" + mList.get(position).getMsgPublishType());
            } else {
                mViewHolder.checkBox.setChecked(false);
//                if (selected.contains(mList.get(position).getId())) {
                selected.remove(position);
//                }
            }
            mViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        mList.get(position).setCheck(true);
                        selected.put(position, mList.get(position).getId() + "|" + mList.get(position).getMsgPublishType());
                    } else {
                        mList.get(position).setCheck(false);
                        selected.remove(position);
                    }
                    onCheckBoxChange.onCheckBoxChange();

                }
            });
        } else {
            mViewHolder.checkBox.setVisibility(View.GONE);
        }
        mViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewHolder.checkBox.getVisibility() == View.VISIBLE) {//判断checkbox是否显示
                    if (mViewHolder.checkBox.isChecked()) {
                        mViewHolder.checkBox.setChecked(false);
                    } else {
                        mViewHolder.checkBox.setChecked(true);
                    }
                } else {
                    //点击跳转消息详情
                    if (mList.get(position).getReadStatus().equals("1")) {
                        Intent intent = new Intent(mContext, MyMessageDetailActivity.class);
                        intent.putExtra("title", mList.get(position).getTitle());
                        intent.putExtra("content", mList.get(position).getContent());
                        mContext.startActivity(intent);
                    } else {
                        onGoDetailListener.goMessageDetail(position, mList.get(position).getId(), mList.get(position).getMsgPublishType(), mList.get(position).getTitle(), mList.get(position).getContent());
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_layout;
        Button message_tip;
        CheckBox checkBox;
        TextView tv_title, tv_date, tv_content;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            checkBox = itemView.findViewById(R.id.checkbox);
            message_tip = itemView.findViewById(R.id.btn_message_tips);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_content = itemView.findViewById(R.id.tv_content);
            view = itemView.findViewById(R.id.top_view);
            SupportDisplay.resetAllChildViewParam(item_layout);
        }
    }

    /**
     * 获取所有未读的消息的数量
     */
    public HashMap<Integer, String> getUnReadCount() {
        selectedALL.clear();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getReadStatus().equals("0")) {//代表未读
                selectedALL.put(i, mList.get(i).getId());
            }
        }
        return selectedALL;
    }

    /**
     * 添加所有未读的消息为全选
     */
    public void selectOfSelectAll() {
        selected.clear();
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setCheck(true);
            selected.put(i, mList.get(i).getId() + "|" + mList.get(i).getMsgPublishType());
        }
        notifyDataSetChanged();
    }

    /**
     * 移除所有未读的消息
     */
    public void selectOfRemoveAll() {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setCheck(false);
        }
        selected.clear();
        notifyDataSetChanged();
    }

    public HashMap<Integer, String> getSelectList() {
        return selected;
    }

    //拼接所有选中的id变成字符串
    public String getSelectListString() {
        if (selected.size() == 0) return "";
        String temp = "";
        Collection<String> values = selected.values();
        Iterator<String> iter = values.iterator();
        while (iter.hasNext()) {
            temp = temp + "[" + iter.next() + "]";
        }
//        if (temp.substring(temp.length() - 1).equals(",")) {
//            return temp.substring(0, temp.length() - 1);
//        } else {
        return temp;
//        }
    }

    public interface onCheckBoxChangelistener {
        void onCheckBoxChange();
    }

    public interface onGoDetailListener {
        void goMessageDetail(int position, String id, String msgtype, String title, String content);
    }
}
