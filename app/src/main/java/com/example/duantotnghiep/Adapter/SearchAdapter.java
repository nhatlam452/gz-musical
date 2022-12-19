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
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.R;

import java.text.NumberFormat;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CartViewHolder>  {
    private final List<Products> mList;
    private final Context context;

    public SearchAdapter(Context context, List<Products> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_search, parent, false);
        return new SearchAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.CartViewHolder holder, int position) {
        Products products = mList.get(position);
        holder.tvName.setText(products.getProductName());
        holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvPrice.setText(NumberFormat.getInstance().format(products.getPrice()) + " đ");
        holder.tvPriceDiscount.setText(NumberFormat.getInstance().format(products.getPrice() * (100 - products.getDiscount()) / 100) + " đ");
        Glide.with(context).load(products.getUrl()).into(holder.imgProduct);
        holder.itemSearchLayout.setOnClickListener(v->{
            Intent i  = new Intent(context, ProductDetailActivity.class);
            i.putExtra("productId",products.getProductID());
            context.startActivity(i);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice,tvPriceDiscount,tvName;
        ImageView imgProduct;
        LinearLayout itemSearchLayout;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemSearchName);
            tvPrice = itemView.findViewById(R.id.tvPriceSearch);
            tvPriceDiscount = itemView.findViewById(R.id.tvPriceDiscountSearch);
            imgProduct = itemView.findViewById(R.id.imgSearchItem);
            itemSearchLayout = itemView.findViewById(R.id.itemSearchLayout);
        }
    }
}
