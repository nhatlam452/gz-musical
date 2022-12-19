package com.example.duantotnghiep.Presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Contract.OrderDetailContract;
import com.example.duantotnghiep.Model.Response.OrderDetailResponse;
import com.example.duantotnghiep.Service.OrderDetailService;

public class OrderDetailPresenter implements  OrderDetailContract.Presenter, OrderDetailContract.OrderDetailModel.OnOrderDetailFinishedListener {

    private final OrderDetailContract.View view;
    private final OrderDetailContract.OrderDetailModel model;

    public OrderDetailPresenter(OrderDetailContract.View view) {
        this.view = view;
        model = new OrderDetailService();
    }

    @Override
    public void onOrderDetailFinished(OrderDetailResponse orderDetailResponse) {
        if (orderDetailResponse.getResponseCode() == 1){
            view.onOrderDetailSuccess(orderDetailResponse.getData().get(0));
        }else {
            view.onOrderDetailFailure(orderDetailResponse.getMessage());
        }
    }

    @Override
    public void onOrderDetailFailure(Throwable t) {
        view.onOrderDetailFailure("Cilent Error");
        Log.d("OrderDetaul Presenter",t.getMessage());
    }

    @Override
    public void onGetOrderDetail(int orderId) {
        model.onGetOrderDetail(this,orderId);
    }
}
