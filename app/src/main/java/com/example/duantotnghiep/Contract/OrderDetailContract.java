package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.OrderDetail;
import com.example.duantotnghiep.Model.Response.OrderDetailResponse;
import com.example.duantotnghiep.Model.Response.OrderResponse;

import java.util.List;

public interface OrderDetailContract {
    interface OrderDetailModel{
        interface  OnOrderDetailFinishedListener{
            void  onOrderDetailFinished(OrderDetailResponse orderDetailResponse);
            void  onOrderDetailFailure(Throwable t);
        }
        void onGetOrderDetail(OnOrderDetailFinishedListener orderDetailFinishedListener,int orderId);
    }
    interface View{
        void onOrderDetailSuccess(OrderDetail orderDetail);
        void onOrderDetailFailure(String msg);
        void onOrderDetailResponseFail(Throwable t);
    }

    interface Presenter {
        void onGetOrderDetail(int orderId);
    }
}
