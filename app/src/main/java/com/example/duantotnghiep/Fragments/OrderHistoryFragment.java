package com.example.duantotnghiep.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.OrderDaterHistoryAdapter;
import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.OrderHistory;
import com.example.duantotnghiep.Presenter.OrderPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.LocalStorage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderHistoryFragment extends Fragment {
    private RecyclerView rcvODH;
    private List<Order> oList ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PaymentFragment paymentFragment = new PaymentFragment();
        oList = paymentFragment.getmList();
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        rcvODH = view.findViewById(R.id.rcvOrderDateHistory);
        if (oList == null){
            Toast.makeText(getContext(), "Lisst null", Toast.LENGTH_SHORT).show();
        }else {
            List<OrderHistory> ohList = new ArrayList<>();
            List<Order> todayList = new ArrayList<>();
            List<Order> yesterdayList = new ArrayList<>();
            List<Order> otherList = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date now = new Date();
            String tDay = format.format(now.getTime());
            String yDay = format.format(now.getTime() - (1000 * 60 * 60 * 24));
            for (int i = 0; i < oList.size(); i++) {
                if (oList.get(i).getCreateDate().equals(tDay)) {
                    todayList.add(oList.get(i));
                } else if (oList.get(i).getCreateDate().equals(yDay)) {
                    yesterdayList.add(oList.get(i));
                } else {
                    otherList.add(oList.get(i));
                }
            }

            ohList.add(new OrderHistory("Today", todayList));
            ohList.add(new OrderHistory("Yesterday", yesterdayList));
            ohList.add(new OrderHistory("Previous days", otherList));

            OrderDaterHistoryAdapter historyAdapter = new OrderDaterHistoryAdapter(ohList, getContext());
            rcvODH.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvODH.setAdapter(historyAdapter);
        }


        return view;
    }


}