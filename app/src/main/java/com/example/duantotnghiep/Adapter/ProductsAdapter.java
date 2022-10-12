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
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.LoginActivity;
import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private final List<Products> mListProduct;
    private final Context context;
    public ProductsAdapter(Context context,List<Products> mListProduct) {
        this.mListProduct = mListProduct;
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
        Products products = mListProduct.get(position);
        if(products == null){
            return;
        }
        Toast.makeText(context, ""+mListProduct.size(), Toast.LENGTH_SHORT).show();
        Glide.with(context).load(products.getUrl()).into(holder.imgProduct);
        holder.layoutProduct.startAnimation(AnimationUtils.loadAnimation(holder.layoutProduct.getContext(),R.anim.anim_rcv_product));
        holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvPrice.setText(products.getPrice());
        holder.tvProductName.setText(products.getProductName().trim());
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null){
            return mListProduct.size();
        }
        return 0;
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        LinearLayout layoutProduct;
        TextView tvPrice,tvProductName;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProducts);
            layoutProduct = itemView.findViewById(R.id.layoutProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvProductName = itemView.findViewById(R.id.tvProductName);
        }
    }
}
