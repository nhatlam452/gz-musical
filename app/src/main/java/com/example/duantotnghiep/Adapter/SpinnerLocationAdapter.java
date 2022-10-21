package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Model.Location;
import com.example.duantotnghiep.R;

import java.util.List;


public class SpinnerLocationAdapter extends RecyclerView.Adapter<SpinnerLocationAdapter.SpinnerViewHolder>{
    private final List<Location> mList;
    private final Context context;
    private final OnClickSetText onClickSetText;
    public interface OnClickSetText{
         void onClickSetText(String s,String code);
    }
    public SpinnerLocationAdapter(Context context, List<Location> mList, OnClickSetText onClickSetText) {
        this.mList = mList;
        this.context = context;
        this.onClickSetText = onClickSetText;
    }

    @NonNull
    @Override
    public SpinnerLocationAdapter.SpinnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_spinner,parent,false);
        return new SpinnerLocationAdapter.SpinnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpinnerLocationAdapter.SpinnerViewHolder holder, int position) {
        Location location = mList.get(position);
        if(location == null){
            return;
        }
        holder.tvItem.setText(location.getName());
        holder.tvItem.setOnClickListener(v -> onClickSetText.onClickSetText(location.getName(),location.getCode()));
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
