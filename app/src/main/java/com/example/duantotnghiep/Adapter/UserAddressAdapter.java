package com.example.duantotnghiep.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Model.UserAddress;
import com.example.duantotnghiep.R;

import java.util.List;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.UserAddressViewHolder> {
    private final List<UserAddress> mList;
    private final Context context;

    public UserAddressAdapter(Context context, List<UserAddress> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAddressAdapter.UserAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item_layout, parent, false);
        return new UserAddressAdapter.UserAddressViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull UserAddressAdapter.UserAddressViewHolder holder, int position) {
        UserAddress address = mList.get(position);
        if (address == null) {
            return;
        }
        if (position % 2 == 0) {
            holder.layoutItem.setBackgroundColor(R.color.white);
            holder.tvAddressName.setBackgroundColor(R.color.colorPrimary);
            holder.tvA.setBackgroundColor(R.color.colorPrimary);
            holder.tvAddress.setBackgroundColor(R.color.colorPrimary);
            holder.tvW.setBackgroundColor(R.color.colorPrimary);
            holder.tvWard.setBackgroundColor(R.color.colorPrimary);
            holder.tvD.setBackgroundColor(R.color.colorPrimary);
            holder.tvDistrict.setBackgroundColor(R.color.colorPrimary);
            holder.tvC.setBackgroundColor(R.color.colorPrimary);
            holder.tvCity.setBackgroundColor(R.color.colorPrimary);
            holder.imgBackground.setColorFilter(R.color.colorPrimary);
        }

    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public static class UserAddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddressName, tvA, tvAddress, tvW, tvWard, tvD, tvDistrict, tvC, tvCity;
        RelativeLayout layoutItem;
        ImageView imgBackground;

        public UserAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressName = itemView.findViewById(R.id.tvAddressName);
            tvA = itemView.findViewById(R.id.tvA);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvW = itemView.findViewById(R.id.tvW);
            tvWard = itemView.findViewById(R.id.tvWard);
            tvD = itemView.findViewById(R.id.tvD);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            tvC = itemView.findViewById(R.id.tvC);
            tvCity = itemView.findViewById(R.id.tvCity);
            layoutItem = itemView.findViewById(R.id.layoutItemAddress);
            imgBackground = itemView.findViewById(R.id.imgItemAddressBackground);
        }
    }
}
