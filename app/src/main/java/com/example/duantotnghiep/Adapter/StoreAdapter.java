package com.example.duantotnghiep.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.Store;
import com.example.duantotnghiep.R;

import java.text.NumberFormat;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> implements View.OnClickListener {
    private final List<Store> mListStore;
    private final Context context;
    private final OnClickListener onClickListener;

    public interface OnClickListener {
        void onClickListener(double latitude, double longitude);
    }

    public StoreAdapter(Context context, List<Store> mListStore, OnClickListener onClickListener) {
        this.mListStore = mListStore;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public StoreAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_layout, parent, false);
        return new StoreAdapter.StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.StoreViewHolder holder, int position) {
        Store store = mListStore.get(position);
        holder.tvStoreAddress.setText(store.getStoreAddress());
        holder.tvStoreName.setText(store.getStoreName());
        holder.itemView.findViewById(R.id.imgStoreInfo).setOnClickListener(v->{

        });
        holder.layoutStore.setOnClickListener(v -> onClickListener.onClickListener(store.getLatitude(), store.getLongitude()));
    }

    @Override
    public int getItemCount() {
        if (mListStore != null) {
            return mListStore.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStoreName;
        private final TextView tvStoreAddress;
        private final RelativeLayout layoutStore;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreAddress = itemView.findViewById(R.id.tvStoreAddress);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            layoutStore = itemView.findViewById(R.id.layoutStore);

        }
    }
}
