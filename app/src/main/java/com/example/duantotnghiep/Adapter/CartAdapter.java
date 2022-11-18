package com.example.duantotnghiep.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.ProductDetailActivity;
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
        holder.tvCartItemPrice.setPaintFlags(holder.tvCartItemPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tvCartPriceDiscount.setText( "Price : " +NumberFormat.getInstance().format(cart.getPrice() * (100-cart.getDiscount())/100)+"");
        holder.tvCartItemPrice.setText( NumberFormat.getInstance().format(cart.getPrice()));
        holder.tvCartItemName.setText(cart.getProductName().trim());

        holder.tvCartItemQuantity.setText(" x " + cart.getQuantity());
        float totalPrice = (float) (cart.getPrice() * (100-cart.getDiscount())/100);
        holder.tvCartItemTotalPrice.setText("Total : "+NumberFormat.getInstance().format(totalPrice * cart.getQuantity()) +"VND");
        holder.itemCartLayout.setOnClickListener(v->{
            Intent i = new Intent(context, ProductDetailActivity.class);
            i.putExtra("productId",cart.getProductId());
            i.putExtra("quantity",cart.getQuantity());
            context.startActivity(i);
            ((Activity)context).overridePendingTransition(R.anim.anim_fadein,R.anim.anim_fadeout);
        });
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
        private ImageView imgCartItem;
        private LinearLayout itemCartLayout;
        private TextView tvCartItemQuantity,tvCartItemPrice,tvCartItemName,tvCartItemTotalPrice,tvCartPriceDiscount;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCartItem = itemView.findViewById(R.id.imgCartItem);
            itemCartLayout = itemView.findViewById(R.id.itemCartLayout);
            tvCartItemQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvCartPriceDiscount = itemView.findViewById(R.id.tvCartItemPriceDiscount);
            tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
            tvCartItemTotalPrice = itemView.findViewById(R.id.tvCartItemTotalPrice);
        }
    }
}
