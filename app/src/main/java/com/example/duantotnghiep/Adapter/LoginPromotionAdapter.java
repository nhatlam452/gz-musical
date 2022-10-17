package com.example.duantotnghiep.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Activities.WebViewActivity;
import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.R;

import java.util.List;

public class LoginPromotionAdapter extends RecyclerView.Adapter<LoginPromotionAdapter.LoginPromotionViewHolder> {
    private final List<Photo> mListPhoto;
    private Context context;

    public LoginPromotionAdapter(List<Photo> mListPhoto, Context context) {
        this.mListPhoto = mListPhoto;
        this.context = context;
    }

    @NonNull
    @Override
    public LoginPromotionAdapter.LoginPromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion_login_screen,parent,false);
        return new LoginPromotionAdapter.LoginPromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginPromotionAdapter.LoginPromotionViewHolder holder, int position) {
        Photo photo = mListPhoto.get(position);
        if(photo == null){
            return;
        }
        holder.imgItemPromotion.setImageResource(photo.getResourceId());
        if (photo.getSlogan() == null){
            holder.tvDescription.setVisibility(View.GONE);
        }else {
            holder.tvDescription.setText(photo.getSlogan());
        }
        holder.imgItemPromotion.setOnClickListener(v->{
            context.startActivity(new Intent(context, WebViewActivity.class));
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }

    @Override
    public int getItemCount() {
        if (mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    public static class LoginPromotionViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemPromotion;
        TextView tvDescription;
        public LoginPromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemPromotion = itemView.findViewById(R.id.imgItemPromotion);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
