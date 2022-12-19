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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.OrderDetailProduct;
import com.example.duantotnghiep.R;

import java.text.NumberFormat;
import java.util.List;

public class OrderDetailProductAdapter extends RecyclerView.Adapter<OrderDetailProductAdapter.CartViewHolder> implements View.OnClickListener {
    private final List<OrderDetailProduct> mList;
    private final Context context;

    public OrderDetailProductAdapter(Context context, List<OrderDetailProduct> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailProductAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout,parent,false);
        return new OrderDetailProductAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailProductAdapter.CartViewHolder holder, int position) {
        OrderDetailProduct orderDetailProduct = mList.get(position);
        if(orderDetailProduct == null){
            return;
        }
        Glide.with(context).load(orderDetailProduct.getUrl()).into(holder.imgCartItem);
        holder.tvCartItemPrice.setPaintFlags(holder.tvCartItemPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tvCartPriceDiscount.setText( "Price : " +NumberFormat.getInstance().format(orderDetailProduct.getPrice() * (100-orderDetailProduct.getDiscount())/100)+"");
        holder.tvCartItemPrice.setText( NumberFormat.getInstance().format(orderDetailProduct.getPrice()));
        holder.tvCartItemName.setText(orderDetailProduct.getProductName().trim());

        holder.tvCartItemQuantity.setText(" x " + orderDetailProduct.getQuantity());
        float totalPrice = (float) (orderDetailProduct.getPrice() * (100-orderDetailProduct.getDiscount())/100);
        holder.tvCartItemTotalPrice.setText("Total : "+NumberFormat.getInstance().format(totalPrice * orderDetailProduct.getQuantity()) +"VND");
        holder.itemCartLayout.setOnClickListener(v->{
            Intent i  = new Intent(context, ProductDetailActivity.class);
            i.putExtra("productId",orderDetailProduct.getProductId());
            context.startActivity(i);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
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
