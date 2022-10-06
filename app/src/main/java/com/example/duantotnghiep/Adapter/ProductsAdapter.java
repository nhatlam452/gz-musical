package com.example.duantotnghiep.Adapter;

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


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Activities.LoginActivity;
import com.example.duantotnghiep.Models.Photo;
import com.example.duantotnghiep.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private final List<Photo> mListPhoto;
    private final Context context;
    public ProductsAdapter(Context context,List<Photo> mListPhoto) {
        this.mListPhoto = mListPhoto;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_layout,parent,false);
        return new ProductsAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductsViewHolder holder, int position) {
        Photo photo = mListPhoto.get(position);
        if(photo == null){
            return;
        }
        holder.imgProduct.setImageResource(photo.getResourceId());
        holder.imgProduct.setOnClickListener(v -> context.startActivity(new Intent(context,LoginActivity.class)));
        holder.layoutProduct.startAnimation(AnimationUtils.loadAnimation(holder.layoutProduct.getContext(),R.anim.anim_rcv_product));
        holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvPrice.setText("2.500.000 d");
    }

    @Override
    public int getItemCount() {
        if (mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        LinearLayout layoutProduct;
        TextView tvPrice;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProducts);
            layoutProduct = itemView.findViewById(R.id.layoutProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
