package com.example.duantotnghiep.Presenter;

import com.example.duantotnghiep.Contract.DetailContract;
import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.Response.OrderResponse;
import com.example.duantotnghiep.Service.OrderService;
import com.example.duantotnghiep.Service.ProductDetailService;

public class OrderPresenter implements OrderContract.Presenter, OrderContract.OrderModel.OnOrderFinishedListener,OrderContract.OrderModel.OnCancelOrderListener {
    private final OrderContract.View view;
    private final OrderContract.OrderModel model;

    public OrderPresenter(OrderContract.View view) {
        this.view = view;
        model = new OrderService();
    }

    @Override
    public void onOrderFinished(OrderResponse orderResponse) {
        if (orderResponse.getResponseCode() == 1) {
            view.onOrderSuccess(orderResponse.getData());
        } else {
            view.onOrderFailure(orderResponse.getMessage());
        }
    }

    @Override
    public void onOrderFailure(Throwable t) {
        view.onOrderResponseFail(t);

    }

    @Override
    public void onNewOrder(Order order) {
        model.newOrder(this, order);
    }

    @Override
    public void onGetOrder(int userId) {
        model.getOrder(this, userId);
    }

    @Override
    public void onCancelOrder(int status, int orderId, String problem) {
        model.cancelOrder(this, status, orderId,problem);
    }

    @Override
    public void onCancelOrderSuccess(OrderResponse orderResponse) {
        if (orderResponse.getResponseCode() == 1) {
            view.onCancelOrderSuccess(orderResponse.getData());
        } else {
            view.onCancelOrderFailure(orderResponse.getMessage());
        }
    }

    @Override
    public void onCancelOrderFailure(Throwable t) {
        view.onCancelOrderResponseFail(t);

    }
}
