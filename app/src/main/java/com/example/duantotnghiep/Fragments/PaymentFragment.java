package com.example.duantotnghiep.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.OrderDaterHistoryAdapter;
import com.example.duantotnghiep.Adapter.PaymentAdapter;
import com.example.duantotnghiep.Adapter.ProductDetailAdapter;
import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.OrderHistory;
import com.example.duantotnghiep.Presenter.OrderPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.common.util.concurrent.ListenableScheduledFuture;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PaymentFragment extends Fragment implements OrderContract.View {
    private TabLayout tlPayment;
    private ViewPager2 vpPayment;
    private final OrderPresenter orderPresenter = new OrderPresenter(this);
    private TextView tvTotalOrder, tvTotalPayment;
    private static List<Order> mList;
    private View view = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment, container, false);
        tlPayment = view.findViewById(R.id.tlPayment);
        vpPayment = view.findViewById(R.id.vpPayment);
        tvTotalOrder = view.findViewById(R.id.tvTotalOrder);
        tvTotalPayment = view.findViewById(R.id.tvTotalPayment);
        orderPresenter.onGetOrder(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
        return view;
    }

    @Override
    public void onOrderSuccess(List<Order> oList) {
        mList = oList;
        tvTotalOrder.setText(oList.size() + "");
        double sum = 0;
        for (int i = 0; i < oList.size(); i++) {
            sum = sum + oList.get(i).getTotal();
        }
        tvTotalPayment.setText(NumberFormat.getInstance().format(sum));
        setUpViewPager();
    }

    private void setUpViewPager() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
        PaymentAdapter paymentAdapter = new PaymentAdapter(getActivity());
        vpPayment.setAdapter(paymentAdapter);
        new TabLayoutMediator(tlPayment, vpPayment, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("History");
                    break;
                case 1:
                    tab.setText("Inbox");
                    break;
            }
        }).attach();
        tlPayment.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "History":
                        Transition transition = new Slide(Gravity.BOTTOM);
                        transition.setDuration(600);
                        transition.addTarget(bottomNavigationView);

                        TransitionManager.beginDelayedTransition((ViewGroup) view, transition);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        Transition transition1 = new Slide(Gravity.BOTTOM);
                        transition1.setDuration(600);
                        transition1.addTarget(bottomNavigationView);
                        TransitionManager.beginDelayedTransition((ViewGroup) view, transition1);
                        bottomNavigationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onOrderFailure(String msg) {

    }

    @Override
    public void onOrderResponseFail(Throwable t) {

    }

    public List<Order> getmList() {
        return mList;
    }

}