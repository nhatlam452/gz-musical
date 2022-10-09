package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> implements View.OnClickListener {
    private final List<Products> mListProduct;
    private final Context context;
    public CartAdapter(Context context, List<Products> mListProduct) {
        this.mListProduct = mListProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout,parent,false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Products products = mListProduct.get(position);
        if(products == null){
            return;
        }
        Glide.with(context).load(products.getUrl()).into(holder.imgCartItem);
        holder.tvCartItemPrice.setText(products.getPrice());
        holder.tvCartItemName.setText(products.getProductName().trim());
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null){
            return mListProduct.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCartItem;
        TextView tvQuantity,tvCartItemPrice,tvCartItemName;
        ImageButton btnMinus,btnPlus;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCartItem = itemView.findViewById(R.id.imgCartItem);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
        }
    }
}
