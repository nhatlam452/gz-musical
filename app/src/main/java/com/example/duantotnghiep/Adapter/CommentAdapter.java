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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Activities.ViewImageActivity;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.Comment;
import com.example.duantotnghiep.R;

import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> implements View.OnClickListener {
    private final List<Comment> mList;
    private final Context context;

    public CommentAdapter(Context context, List<Comment> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_comment, parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = mList.get(position);
        if (comment.getAvatar().trim() != null && !comment.getAvatar().trim().isEmpty() && !comment.getAvatar().trim().equals(""))  {
            Glide.with(context).load(comment.getAvatar()).into(holder.imgAvtCmt);
        }
        if (comment.getImg() != null && !comment.getImg().isEmpty()) {
            holder.cvImgFeedback.setVisibility(View.VISIBLE);
            Glide.with(context).load(comment.getImg()).into(holder.imgAvtFeedback);
        }
        holder.imgAvtFeedback.setOnClickListener(v->{
            Intent i = new Intent(context, ViewImageActivity.class);
            i.putExtra("ViewImage",comment.getImg());
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(R.anim.anim_fadein,R.anim.anim_fadein);
        });
        holder.tvNameUserCmt.setText(comment.getName() + "");
        holder.tvDateCmt.setText(comment.getDate() + "");
        holder.tvCmtContent.setText(comment.getContent() + "");
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {

    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvtCmt;
        TextView tvNameUserCmt, tvDateCmt, tvCmtContent;
        ImageView imgAvtFeedback;
        CardView cvImgFeedback;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvtCmt = itemView.findViewById(R.id.imgAvtCmt);
            cvImgFeedback = itemView.findViewById(R.id.cvImgFeedback);
            tvNameUserCmt = itemView.findViewById(R.id.tvNameUserCmt);
            tvDateCmt = itemView.findViewById(R.id.tvDateCmt);
            tvCmtContent = itemView.findViewById(R.id.tvCmtContent);
            imgAvtFeedback = itemView.findViewById(R.id.imgAvtFeedback);
        }
    }
}
