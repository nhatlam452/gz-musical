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

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.WebViewActivity;
import com.example.duantotnghiep.Model.News;
import com.example.duantotnghiep.R;

import java.util.List;

public class LoginPromotionAdapter extends RecyclerView.Adapter<LoginPromotionAdapter.LoginPromotionViewHolder> {
    private final List<News> mListNews;
    private final Context context;

    public LoginPromotionAdapter(List<News> mListNews, Context context) {
        this.mListNews = mListNews;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        News news = mListNews.get(position);

        return news.getType();
    }

    @NonNull
    @Override
    public LoginPromotionAdapter.LoginPromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;

        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_login_screen, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion_login_screen, parent, false);
        }
        return new LoginPromotionAdapter.LoginPromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoginPromotionAdapter.LoginPromotionViewHolder holder, int position) {
        News news = mListNews.get(position);
        if (news == null) {
            return;
        }
        Glide.with(context).load(news.getImage()).into(holder.imgItemPromotion);

        holder.tvDescription.setText(news.getTitle());

        holder.imgItemPromotion.setOnClickListener(v -> {
            Intent i = new Intent(context,WebViewActivity.class);
            i.putExtra("URL",news.getUrl());
            context.startActivity(i);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
    }

    @Override
    public int getItemCount() {
        if (mListNews != null) {
            return mListNews.size();
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
