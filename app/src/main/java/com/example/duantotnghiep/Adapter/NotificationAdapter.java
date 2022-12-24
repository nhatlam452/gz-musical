package com.example.duantotnghiep.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.example.duantotnghiep.Model.MyNotification;
import com.example.duantotnghiep.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<MyNotification> notifications;

    public NotificationAdapter(List<MyNotification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyNotification notification = notifications.get(position);
        holder.tvBody.setText(notification.getBody());
        holder.tvTime.setText(notification.getTime());
    }

    @Override
    public int getItemCount() {
        if (notifications != null) {
            return notifications.size();
        }
        return 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvBody;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBody = itemView.findViewById(R.id.tvBodyNoti);
            tvTime = itemView.findViewById(R.id.tvTimeNoti);

        }
    }


}
