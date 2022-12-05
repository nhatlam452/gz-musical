package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.OrderHistory;
import com.example.duantotnghiep.R;

import java.text.NumberFormat;
import java.util.List;

public class OrderDaterHistoryAdapter extends RecyclerView.Adapter<OrderDaterHistoryAdapter.OrderHistoryViewHolder> {
    private final List<OrderHistory> mList;
    private final Context context;

    public OrderDaterHistoryAdapter(List<OrderHistory> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDaterHistoryAdapter.OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_date_history, parent, false);
        return new OrderDaterHistoryAdapter.OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDaterHistoryAdapter.OrderHistoryViewHolder holder, int position) {
        OrderHistory orderHistory = mList.get(position);
        if (orderHistory.getmList() == null || orderHistory.getmList().isEmpty()){
            holder.tvDateHistory.setVisibility(View.GONE);
        }
        holder.tvDateHistory.setText(orderHistory.getDate());
        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(orderHistory.getmList(),context);
        holder.rcvOrderHistory.setLayoutManager(new LinearLayoutManager(context));
        holder.rcvOrderHistory.setAdapter(orderHistoryAdapter);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }


    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDateHistory;
        private final RecyclerView rcvOrderHistory;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateHistory = itemView.findViewById(R.id.tvDateHistory);
            rcvOrderHistory = itemView.findViewById(R.id.rcvOrderHistory);

        }
    }
}
