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
import com.example.duantotnghiep.Activities.StoreInfoActivity;
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
        void onCLickChooseStore(String storeName,String storeAddress);
        void onClickListener(double latitude, double longitude);
        void onGetDistance(TextView textView,double latitude, double longitude);
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
        onClickListener.onGetDistance(holder.tvDistance,store.getLatitude(), store.getLongitude());

        holder.layoutStore.setOnClickListener(v -> onClickListener.onClickListener(store.getLatitude(), store.getLongitude()));
        holder.itemView.findViewById(R.id.imgStoreInfo).setOnClickListener(v->{
            onClickListener.onCLickChooseStore(store.getStoreName(),store.getStoreAddress());
        });
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
        private  TextView tvStoreAddress,tvDistance,tvStoreName;
        private final RelativeLayout layoutStore;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreAddress = itemView.findViewById(R.id.tvStoreAddress);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);
            layoutStore = itemView.findViewById(R.id.layoutStore);

        }
    }
}
