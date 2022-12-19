package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.OrderDetailProductAdapter;
import com.example.duantotnghiep.Contract.OrderDetailContract;
import com.example.duantotnghiep.Model.OrderDetail;
import com.example.duantotnghiep.Presenter.OrderDetailPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;

import java.text.NumberFormat;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailContract.View {
    private final OrderDetailPresenter orderDetailPresenter = new OrderDetailPresenter(this);
    private TextView tvOrderDetailCode, tvOrderDetailStatus, tvDeliveryMethodOrderDetail, tvStoreOrderDetail, tvAddressOrderDetail, tvPaymentMethodOrderDetail, tvNoteOrderDetail, tvTotalPriceItemOD, tvTotalPriceOD;
    private RecyclerView rcvOrderDetail;
    private LinearLayout llStoreOD,llUserAddressOD,llNoteOD;
    private Button btnCancelOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initUI();
        int orderId = getIntent().getIntExtra("orderDetailId", 1);
        orderDetailPresenter.onGetOrderDetail(orderId);
        AppUtil.showDialog.show(this);
    }

    private void initUI() {
        tvOrderDetailCode = findViewById(R.id.tvOrderDetailCode);
        llStoreOD = findViewById(R.id.llStoreOD);
        llNoteOD = findViewById(R.id.llNoteOD);
        llUserAddressOD = findViewById(R.id.llUserAddressOD);
        tvOrderDetailStatus = findViewById(R.id.tvOrderDetailStatus);
        tvDeliveryMethodOrderDetail = findViewById(R.id.tvDeliveryMethodOrderDetail);
        tvStoreOrderDetail = findViewById(R.id.tvStoreOrderDetail);
        tvAddressOrderDetail = findViewById(R.id.tvAddressOrderDetail);
        tvPaymentMethodOrderDetail = findViewById(R.id.tvPaymentMethodOrderDetail);
        tvNoteOrderDetail = findViewById(R.id.tvNoteOrderDetail);
        tvTotalPriceItemOD = findViewById(R.id.tvTotalPriceItemOD);
        tvTotalPriceOD = findViewById(R.id.tvTotalPriceOD);
        rcvOrderDetail = findViewById(R.id.rcvOrderDetail);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
    }

    @Override
    public void onOrderDetailSuccess(OrderDetail orderDetails) {
        tvOrderDetailCode.setText("GZOD" + orderDetails.getOrderId());
        tvStoreOrderDetail.setText(orderDetails.getdFrom());
        tvAddressOrderDetail.setText(orderDetails.getdTo());
        tvPaymentMethodOrderDetail.setText(orderDetails.getPaymentMethod());
        tvTotalPriceItemOD.setText("Total (" + orderDetails.getListProduct().size() + " item(s))");
        tvTotalPriceOD.setText(NumberFormat.getInstance().format(orderDetails.getTotal()) + "VND");
        if (orderDetails.getNote().isEmpty()){
            llNoteOD.setVisibility(View.GONE);
        }else {
            tvNoteOrderDetail.setText(orderDetails.getNote()+"");

        }
        if (orderDetails.getOrderMethod() == 0) // 0 : delivery - 1 : go to store
        {
            tvDeliveryMethodOrderDetail.setText("Delivery");
            llStoreOD.setVisibility(View.GONE);
        }else{
            tvDeliveryMethodOrderDetail.setText("Go to store");
            llUserAddressOD.setVisibility(View.GONE);
        }
        tvOrderDetailCode.setText("GZOD" + orderDetails.getOrderId());
        switch (orderDetails.getStatus()) {
            case 1:
                tvOrderDetailStatus.setText("Preparing Order");
                tvOrderDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.zaloColor));
                break;
            case 2:
                tvOrderDetailStatus.setText("On Delivery");
                tvOrderDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.red));
                break;
            case 3:
                tvOrderDetailStatus.setText("Delivery Success");
                tvOrderDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;
            case 4:
                tvOrderDetailStatus.setText("Order Cancel");
                tvOrderDetailStatus.setTextColor(ContextCompat.getColor(this,  com.google.android.libraries.places.R.color.quantum_grey));
                break;
            default:
                tvOrderDetailStatus.setText("Waiting for Confirm");
                tvOrderDetailStatus.setTextColor(ContextCompat.getColor(this, com.google.android.libraries.places.R.color.quantum_orange));
                break;
        }
        OrderDetailProductAdapter orderDetailProductAdapter = new OrderDetailProductAdapter(this,orderDetails.getListProduct());
        rcvOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        rcvOrderDetail.setAdapter(orderDetailProductAdapter);
        if (orderDetails.getStatus() != 0){
            btnCancelOrder.setVisibility(View.GONE);
        }
        AppUtil.showDialog.dismiss();
    }

    @Override
    public void onOrderDetailFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        AppUtil.showDialog.dismiss();

    }

    @Override
    public void onOrderDetailResponseFail(Throwable t) {
        Toast.makeText(this, "Client Error . Please check your connection", Toast.LENGTH_SHORT).show();
        AppUtil.showDialog.dismiss();

    }
}