package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Model.PaymentMethod;
import com.example.duantotnghiep.R;

import java.util.List;


public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.SpinnerViewHolder> {
    private final List<PaymentMethod> mList;
    private final OnClickSetText onClickSetText;
    private final Context context;

    public interface OnClickSetText {
        void onClickSetText(int imgSrc, String s);
    }

    public PaymentMethodAdapter(Context context, List<PaymentMethod> mList, OnClickSetText onClickSetText) {
        this.mList = mList;
        this.context = context;
        this.onClickSetText = onClickSetText;
    }

    @NonNull
    @Override
    public PaymentMethodAdapter.SpinnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method_layout, parent, false);
        return new PaymentMethodAdapter.SpinnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodAdapter.SpinnerViewHolder holder, int position) {
        PaymentMethod s = mList.get(position);
        if (s == null) {
            return;
        }
        holder.tvItem.setText(s.getNamePayment());
        holder.imgMethod.setImageResource(s.getImgPayment());
        holder.itemView.findViewById(R.id.llPaymentMethodItem).setOnClickListener(v -> {
            onClickSetText.onClickSetText(s.getImgPayment(), s.getNamePayment());
        });
        switch (s.getNamePayment()) {
            case "Thanh toán khi nhận hàng":
                holder.tvItem.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
                break;
            case "Thanh toán bằng ví Zalo Pay" :
                holder.tvItem.setTextColor(ContextCompat.getColor(context,R.color.zaloColor));

                break;
            default:
                holder.tvItem.setTextColor(ContextCompat.getColor(context,R.color.momoColor));
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

    public static class SpinnerViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        ImageView imgMethod;

        public SpinnerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvPaymentMethodNameItem);
            imgMethod = itemView.findViewById(R.id.imgPaymentMethodItem);
        }
    }
}
