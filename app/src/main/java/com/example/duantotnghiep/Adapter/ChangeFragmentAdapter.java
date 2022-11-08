package com.example.duantotnghiep.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.duantotnghiep.Fragments.BuyFragment;
import com.example.duantotnghiep.Fragments.CartFragment;
import com.example.duantotnghiep.Fragments.HomeFragment;
import com.example.duantotnghiep.Fragments.PaymentFragment;
import com.example.duantotnghiep.Fragments.StoreFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class ChangeFragmentAdapter extends FragmentStateAdapter {
    public ChangeFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PaymentFragment();
            case 2:
                return new BuyFragment();
            case 3:
                return new CartFragment();
            case 4:
                return new StoreFragment();
            default:
                return new  HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
