package com.example.duantotnghiep.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Model.Images;
import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailImageAdapter extends RecyclerView.Adapter<ProductDetailImageAdapter.OnBoardingViewHolder> {
    private final List<Images> mListPhoto;
    private Context context;

    public ProductDetailImageAdapter(List<Images> mListPhoto, Context context) {
        this.mListPhoto = mListPhoto;
        this.context = context;
    }


    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_detail_image,parent,false);
        return new OnBoardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        Images photo = mListPhoto.get(position);
        if(photo == null){
            return;
        }
        Glide.with(context).load(photo.getUrl()).into(holder.circleImageView);

    }

    @Override
    public int getItemCount() {
        if (mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    public static class OnBoardingViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.imgProductDetail);
        }
    }
}
