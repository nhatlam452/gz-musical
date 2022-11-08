package com.example.duantotnghiep.Adapter;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.R;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private final List<Products> mListProduct;
    private final Context context;

    public ProductsAdapter(Context context, List<Products> mListProduct) {
        this.mListProduct = mListProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case Products.TYPE_GRID:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_layout_grid
                        , parent, false);
                break;
            case Products.TYPE_LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_layout, parent, false);
                break;
        }
        return new ProductsAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ProductsViewHolder holder, int position) {
        Products products = mListProduct.get(position);
        if (products == null) {
            return;
        }
        Glide.with(context).load(products.getUrl()).into(holder.imgProduct);
        holder.layoutProduct.startAnimation(AnimationUtils.loadAnimation(holder.layoutProduct.getContext(), R.anim.anim_rcv_product));
        holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvPrice.setText(NumberFormat.getInstance().format(products.getPrice()) + " đ");
        holder.tvPriceDiscount.setText(NumberFormat.getInstance().format(products.getPrice() * (100 - products.getDiscount()) / 100) + " đ");
        holder.tvProductName.setText(products.getProductName() + "");
        holder.imgProduct.setOnClickListener(v->{
            Gson gson = new Gson();
            String json = gson.toJson(products);
            Intent i  = new Intent(context,ProductDetailActivity.class);
            i.putExtra("product",json);
            context.startActivity(i);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }

    @Override
    public int getItemViewType(int position) {
        Products products = mListProduct.get(0);
        return products.getTypeDisplay();
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        return 0;
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        LinearLayout layoutProduct;
        TextView tvPrice, tvProductName, tvPriceDiscount;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProducts);
            tvPriceDiscount = itemView.findViewById(R.id.tvPriceDiscount);
            layoutProduct = itemView.findViewById(R.id.layoutProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvProductName = itemView.findViewById(R.id.tvProductName);
        }
    }
}
