package com.example.duantotnghiep.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.R;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder> {
    private final List<Photo> mListPhoto;

    public OnBoardingAdapter(List<Photo> mListPhoto) {
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_on_boarding_screen,parent,false);
        return new OnBoardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        Photo photo = mListPhoto.get(position);
        if(photo == null){
            return;
        }
        holder.tvSloganItemOnBoarding.setText(photo.getSlogan());
        holder.imgItemOnBoarding.setImageResource(photo.getResourceId());
    }

    @Override
    public int getItemCount() {
        if (mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    public static class OnBoardingViewHolder extends RecyclerView.ViewHolder {
        TextView tvSloganItemOnBoarding;
        ImageView imgItemOnBoarding;
        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSloganItemOnBoarding = itemView.findViewById(R.id.tvSloganItemOnBoarding);
            imgItemOnBoarding = itemView.findViewById(R.id.imgItemOnBoarding);
        }
    }
}
