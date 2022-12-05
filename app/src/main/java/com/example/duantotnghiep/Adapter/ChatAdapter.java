package com.example.duantotnghiep.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.Model.ChatMsg;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.R;

import java.text.NumberFormat;
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
        if (!chatMsgList.get(position).getId_send().equals(sendId)) {
            return TYPE_SEND;
        } else {
            return TYPE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return chatMsgList.size();
    }

    public static class SendViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatContent, tvChatTime, tvChatStatus;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatContent = itemView.findViewById(R.id.tvChatContentSend);
            tvChatTime = itemView.findViewById(R.id.tvTimeChatSend);
        }
    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatContent, tvChatTime, tvChatStatus;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatContent = itemView.findViewById(R.id.tvChatContentReceived);
            tvChatTime = itemView.findViewById(R.id.tvTimeChatReceived);
        }
    }
}
