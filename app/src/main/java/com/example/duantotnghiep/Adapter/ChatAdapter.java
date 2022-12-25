package com.example.duantotnghiep.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duantotnghiep.Model.ChatMsg;
import com.example.duantotnghiep.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<ChatMsg> chatMsgList;
    private final String sendId;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVED = 2;

    public ChatAdapter(Context context, List<ChatMsg> chatMsgList, String sendId) {
        this.context = context;
        this.chatMsgList = chatMsgList;
        this.sendId = sendId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND) {
            view = LayoutInflater.from(context).inflate(R.layout.item_send_inbox, parent, false);
            return new SendViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_receive_inbox, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SEND) {
            ((SendViewHolder) holder).tvChatContent.setText(chatMsgList.get(position).getMessage());
            ((SendViewHolder) holder).tvChatTime.setText(chatMsgList.get(position).getDatetime());
        } else {
            ((ReceivedViewHolder) holder).tvChatContent.setText(chatMsgList.get(position).getMessage());
            ((ReceivedViewHolder) holder).tvChatTime.setText(chatMsgList.get(position).getDatetime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        String isSend = chatMsgList.get(position).getId_send();
        Log.d("issend",isSend+"");

        if (isSend == null || isSend.isEmpty()){
            return TYPE_SEND;
        }
        if (!isSend.equals(sendId)) {
            return TYPE_RECEIVED;
        } else {
            return TYPE_SEND;

        }

    }

    @Override
    public int getItemCount() {
        if (chatMsgList == null || chatMsgList.isEmpty()) {
            return 0;
        }
        return chatMsgList.size();
    }

    public static class SendViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatContent, tvChatTime;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatContent = itemView.findViewById(R.id.tvChatContentSend);
            tvChatTime = itemView.findViewById(R.id.tvTimeChatSend);
        }
    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatContent, tvChatTime;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatContent = itemView.findViewById(R.id.tvChatContentReceived);
            tvChatTime = itemView.findViewById(R.id.tvTimeChatReceived);
        }
    }
}
