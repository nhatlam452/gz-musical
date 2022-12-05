package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Model.CreateOrder;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.OrderHistory;
import com.example.duantotnghiep.R;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private final List<Order> mList;
    private final Context context;

    public OrderHistoryAdapter(List<Order> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history_layout, parent, false);
        return new OrderHistoryAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderViewHolder holder, int position) {
        Order order = mList.get(position);
        holder.tvItemPaymentMethod.setText(order.getPaymentMethod());
        holder.tvItemOrderHistoryDate.setText(order.getCreateDate());
        holder.tvTotalOrderHistory.setText(NumberFormat.getInstance().format(order.getTotal()) + "VND");
        switch (order.getPaymentMethod()) {
            case "Thanh toán bằng ví Zalo Pay":
                holder.cimgPaymentMethod.setImageResource(R.drawable.img_zalo_pay);
                break;
            case "Thanh toán bằng ví Momo":
                holder.cimgPaymentMethod.setImageResource(R.drawable.img_momo);
                //Momo Api
                break;
            default:
                holder.cimgPaymentMethod.setImageResource(R.drawable.img_cod);

                break;
        }
        switch (order.getStatus()) {
            /*
             * status :
             * 0 : đang chờ xác nhận - yellow
             * 1 : đang chuẩn bị đơn hàng - blue
             * 2 : đang giao hàng - red
             * 3 : đã giao hàng - green
             * 4 : đã hủy - grey
             * */

            case 1:
                holder.imgStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.zaloColor));
                break;
            case 2:
                holder.imgStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red));

                //Momo Api
                break;
            case 3:
                holder.imgStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                //Momo Api
                break;
            case 4:
                holder.imgStatus.setBackgroundColor(ContextCompat.getColor(context, com.google.android.libraries.places.R.color.quantum_grey));
                //Momo Api
                break;
            default:
                holder.imgStatus.setBackgroundColor(ContextCompat.getColor(context, com.google.android.libraries.places.R.color.quantum_orange));

                break;
        }

    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }


    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView cimgPaymentMethod;
        private final TextView tvItemPaymentMethod;
        private final TextView tvItemOrderHistoryDate;
        private final TextView tvTotalOrderHistory;
        private final ImageView imgStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cimgPaymentMethod = itemView.findViewById(R.id.cimgPaymentMethod);
            tvItemPaymentMethod = itemView.findViewById(R.id.tvItemPaymentMethod);
            tvItemOrderHistoryDate = itemView.findViewById(R.id.tvItemOrderHistoryDate);
            tvTotalOrderHistory = itemView.findViewById(R.id.tvTotalOrderHistory);
            imgStatus = itemView.findViewById(R.id.imgStatus);
        }
    }
}
