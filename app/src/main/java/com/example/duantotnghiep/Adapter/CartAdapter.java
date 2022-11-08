package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.R;

import java.text.NumberFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> implements View.OnClickListener {
    private final List<Cart> mListCart;
    private final Context context;
    public CartAdapter(Context context, List<Cart> mListCart) {
        this.mListCart = mListCart;
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
        Cart cart = mListCart.get(position);
        if(cart == null){
            return;
        }
        Glide.with(context).load(cart.getUrl()).into(holder.imgCartItem);

        holder.tvCartItemPrice.setText(NumberFormat.getInstance().format(cart.getPrice())+"");
        holder.tvCartItemName.setText(cart.getProductName().trim());

        holder.tvCartItemQuantity.setText(" x " + cart.getQuantity());

        holder.tvCartItemTotalPrice.setText(NumberFormat.getInstance().format(cart.getPrice() * cart.getQuantity()) +"");
    }

    @Override
    public int getItemCount() {
        if (mListCart != null){
            return mListCart.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCartItem;
        TextView tvCartItemQuantity,tvCartItemPrice,tvCartItemName,tvCartItemTotalPrice;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCartItem = itemView.findViewById(R.id.imgCartItem);
            tvCartItemQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
            tvCartItemTotalPrice = itemView.findViewById(R.id.tvCartItemTotalPrice);
        }
    }
}
