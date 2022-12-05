package com.example.duantotnghiep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Model.UserAddress;
import com.example.duantotnghiep.R;

import java.util.List;

public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.UserAddressViewHolder> {
    private final List<UserAddress> mList;
    private OnClickListener onClickListener;
    private final Context context;
    public interface OnClickListener{
        void onClickListener(String addressName,String address,String ward,String district , String city );
    }
    public ChooseAddressAdapter(Context context, List<UserAddress> mList,OnClickListener onClickListener) {
        this.mList = mList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ChooseAddressAdapter.UserAddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_address, parent, false);
        return new ChooseAddressAdapter.UserAddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseAddressAdapter.UserAddressViewHolder holder, int position) {
        UserAddress address = mList.get(position);
        if (address == null) {
            return;
        }
        if (address.getAddressName() == null || address.getAddress().equalsIgnoreCase("null")){
            holder.tvAddressName.setText("Your Address " + (position+1));
        }else {
            holder.tvAddressName.setText(address.getAddressName());
        }
        holder.tvAddress.setText(address.getAddress() + " " + address.getWard() + " " + address.getDistrict() + " " + address.getCity());
        holder.layoutItem.setOnClickListener(v->{
            onClickListener.onClickListener(holder.tvAddressName.getText().toString(),address.getAddress(),address.getWard(),address.getDistrict(),address.getCity());
        });


    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public static class UserAddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddressName, tvAddress;
        RelativeLayout layoutItem;

        public UserAddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddressName = itemView.findViewById(R.id.tvAddressNameChooseAddress);
            tvAddress = itemView.findViewById(R.id.tvAddressInfoChooseAddress);

            layoutItem = itemView.findViewById(R.id.rltChooseAddress);
        }
    }
}
