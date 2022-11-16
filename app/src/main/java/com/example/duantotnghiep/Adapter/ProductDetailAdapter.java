package com.example.duantotnghiep.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.duantotnghiep.Fragments.CommentFragment;
import com.example.duantotnghiep.Fragments.ProductDetailFragment;
import com.example.duantotnghiep.Fragments.ProductDetailVideoFragment;

public class ProductDetailAdapter extends FragmentStateAdapter {
    public ProductDetailAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new ProductDetailVideoFragment();
            case 2 :
                return new CommentFragment();
            default:
                return new ProductDetailFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
