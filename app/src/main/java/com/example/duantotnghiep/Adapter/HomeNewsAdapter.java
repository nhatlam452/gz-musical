package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Activities.WebViewActivity;
import com.example.duantotnghiep.Model.Photo;
import com.example.duantotnghiep.R;

import java.util.List;

public class HomeNewsAdapter extends RecyclerView.Adapter<HomeNewsAdapter.HomeNewsViewHolder> {
    private final List<Photo> mListPhoto;
    private Context context;

    public HomeNewsAdapter(List<Photo> mListPhoto,Context context) {
        this.mListPhoto = mListPhoto;
        this.context=context;
    }

    @NonNull
    @Override
    public HomeNewsAdapter.HomeNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_login_screen,parent,false);
        return new HomeNewsAdapter.HomeNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeNewsAdapter.HomeNewsViewHolder holder, int position) {
        Photo photo = mListPhoto.get(position);
        if(photo == null){
            return;
        }
        holder.imgItemPromotion.setImageResource(photo.getResourceId());
        holder.tvDescription.setText(photo.getSlogan());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = photo.getUrl();
                Log.d("=====>>","url:"+url);
                Intent i = new Intent(context, WebViewActivity.class);
                i.putExtra("url",url);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    public static class HomeNewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItemPromotion;
        TextView tvDescription;
        CardView cardView;
        public HomeNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemPromotion = itemView.findViewById(R.id.imgItemNews);
            tvDescription = itemView.findViewById(R.id.tvDescriptionNews);
            cardView = itemView.findViewById(R.id.cvCard);
        }
    }
}
