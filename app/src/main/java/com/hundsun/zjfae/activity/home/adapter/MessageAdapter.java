package com.hundsun.zjfae.activity.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private Context context;
    private List<onight.zjfae.afront.AllAzjProto.PBAPPIcons> listBeanList;
    private LayoutInflater inflater;
    private MessageOnclick onclick;

    public MessageAdapter(Context context, List<onight.zjfae.afront.AllAzjProto.PBAPPIcons> listBeanList) {
        this.context = context;
        this.listBeanList = listBeanList;
        inflater = LayoutInflater.from(context);
    }

    public void setMessageOnclick(MessageOnclick onclick) {
        this.onclick = onclick;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = inflater.inflate(R.layout.message_adapter_layout, viewGroup, false);
        MessageViewHolder viewHolder = new MessageViewHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, final int i) {

        messageViewHolder.userMessageName.setText(listBeanList.get(i).getIconsName());
        if (listBeanList.get(i).getRemark() != null) {
            if (listBeanList.get(i).getRemark().equals("0")||listBeanList.get(i).getRemark().equals("")) {
                messageViewHolder.unread_count.setVisibility(View.GONE);
            } else {
                messageViewHolder.unread_count.setText(listBeanList.get(i).getRemark());
                messageViewHolder.unread_count.setVisibility(View.VISIBLE);
            }
        } else {
            messageViewHolder.unread_count.setVisibility(View.GONE);
        }

        ImageLoad.getImageLoad().LoadImage(context, listBeanList.get(i).getIconsAddress(), messageViewHolder.userMessageIcon);

        messageViewHolder.userMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclick != null) {
                    onclick.itemOnclick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeanList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout userMessageLayout;
        ImageView userMessageIcon;
        TextView userMessageName, unread_count;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageLayout = itemView.findViewById(R.id.user_message_layout);
            userMessageIcon = itemView.findViewById(R.id.message_icon);
            userMessageName = itemView.findViewById(R.id.message_name);
            unread_count = itemView.findViewById(R.id.unread_count);
            SupportDisplay.resetAllChildViewParam((LinearLayout) itemView.findViewById(R.id.message_lin_layout));
        }
    }

    public interface MessageOnclick {
        void itemOnclick(int i);
    }

}
