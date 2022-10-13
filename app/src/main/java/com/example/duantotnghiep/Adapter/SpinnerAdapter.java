package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.R;

import java.util.List;


public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.SpinnerViewHolder>{
    private final List<String> mList;
    private final Context context;
    private OnClickSetText onClickSetText;
    public interface OnClickSetText{
         void onClickSetText(String s);
    }
    public SpinnerAdapter(Context context, List<String> mList,OnClickSetText onClickSetText) {
        this.mList = mList;
        this.context = context;
        this.onClickSetText = onClickSetText;
    }

    @NonNull
    @Override
    public SpinnerAdapter.SpinnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_spinner,parent,false);
        return new SpinnerAdapter.SpinnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpinnerAdapter.SpinnerViewHolder holder, int position) {
        String s = mList.get(position);
        if(s == null){
            return;
        }
        holder.tvItem.setText(s);
        holder.tvItem.setOnClickListener(v -> {
            onClickSetText.onClickSetText(s);
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
        }
        return 0;
    }

    public static class SpinnerViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        public SpinnerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItemSpinner);
        }
    }
}
