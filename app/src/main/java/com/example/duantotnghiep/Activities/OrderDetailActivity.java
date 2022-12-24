package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.OrderDetailProductAdapter;
import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Contract.OrderDetailContract;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.OrderDetail;
import com.example.duantotnghiep.Presenter.OrderDetailPresenter;
import com.example.duantotnghiep.Presenter.OrderPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.NumberFormat;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailContract.View, OrderContract.View {
    private final OrderDetailPresenter orderDetailPresenter = new OrderDetailPresenter(this);
    private TextView tvOrderDetailCode, tvOrderDetailStatus, tvDeliveryMethodOrderDetail, tvStoreOrderDetail, tvAddressOrderDetail, tvPaymentMethodOrderDetail, tvNoteOrderDetail, tvTotalPriceItemOD, tvTotalPriceOD;
    private RecyclerView rcvOrderDetail;
    private LinearLayout llStoreOD, llUserAddressOD, llNoteOD;
    private Button btnCancelOrder;
    private final OrderPresenter orderPresenter = new OrderPresenter(this);
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initUI();
        orderId = getIntent().getIntExtra("orderDetailId", 1);
        orderDetailPresenter.onGetOrderDetail(orderId);
        AppUtil.showDialog.show(this);
        btnCancelOrder.setOnClickListener(v -> {
            openDialogCancel();
        });
    }

    private void openDialogCancel() {
        // Create a new BottomSheetDialog
        BottomSheetDialog bottomDialog = new BottomSheetDialog(this);

// Inflate the layout for the bottom dialog
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_dialog, null);

// Set the inflated layout as the content view for the bottom dialog
        bottomDialog.setContentView(dialogView);

// Find the views in the layout and set up any event listeners or data
        Button buttonCancel = dialogView.findViewById(R.id.btnNoCancel);
        Button buttonConfirm = dialogView.findViewById(R.id.btnConfirmCancel);
        EditText edtProblem = dialogView.findViewById(R.id.edtProblem);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String problem = edtProblem.getText().toString();
                Toast.makeText(OrderDetailActivity.this, orderId+"", Toast.LENGTH_SHORT).show();
                if (problem.isEmpty()) {
                    Toast.makeText(OrderDetailActivity.this, "Please tell us your reason " + LocalStorage.getInstance(OrderDetailActivity.this).getLocalStorageManager().getUserInfo().getSalutation(), Toast.LENGTH_SHORT).show();
                    return;
                }
                orderPresenter.onCancelOrder(5, orderId, edtProblem.getText().toString());
                bottomDialog.dismiss();

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something when the button is clicked
                bottomDialog.dismiss();
            }
        });

// Show the bottom dialog
        bottomDialog.show();

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
        if (orderDetails.getNote().isEmpty()) {
            llNoteOD.setVisibility(View.GONE);
        } else {
            tvNoteOrderDetail.setText(orderDetails.getNote() + "");

        }
        if (orderDetails.getOrderMethod() == 0) // 0 : delivery - 1 : go to store
        {
            tvDeliveryMethodOrderDetail.setText("Delivery");
            llStoreOD.setVisibility(View.GONE);
        } else {
            tvDeliveryMethodOrderDetail.setText("Go to store");
            llUserAddressOD.setVisibility(View.GONE);
        }
        tvOrderDetailCode.setText("GZOD" + orderDetails.getOrderId());
        switch (orderDetails.getStatus()) {
            case 1:
                tvOrderDetailStatus.setText("Waiting for Confirm");
                tvOrderDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.zaloColor));
                break;
            case 2:
                tvAddressOrderDetail.setText("Preparing Order");
                tvAddressOrderDetail.setTextColor(ContextCompat.getColor(this, R.color.teal_700));
                break;
            case 3:
                tvAddressOrderDetail.setText("On Delivery");
                tvAddressOrderDetail.setTextColor(ContextCompat.getColor(this, com.google.android.libraries.places.R.color.quantum_orange));
                break;
            case 4:
                tvAddressOrderDetail.setText("Delivery Success");
                tvAddressOrderDetail.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;
            case 5:
                tvAddressOrderDetail.setText("Re-Delivery");
                tvAddressOrderDetail.setTextColor(ContextCompat.getColor(this, R.color.momoColor));
                break;
            case 6:
                tvAddressOrderDetail.setText("Delivery Falied");
                tvAddressOrderDetail.setTextColor(ContextCompat.getColor(this, R.color.red));
                break;
            default:
                tvAddressOrderDetail.setText("Order cancel");
                tvAddressOrderDetail.setTextColor(ContextCompat.getColor(this, com.google.android.libraries.places.R.color.quantum_grey));
                break;
        }
        OrderDetailProductAdapter orderDetailProductAdapter = new OrderDetailProductAdapter(this, orderDetails.getListProduct());
        rcvOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        rcvOrderDetail.setAdapter(orderDetailProductAdapter);
        if (orderDetails.getStatus() != 0) {
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

    @Override
    public void onOrderSuccess(List<Order> cartList) {

    }

    @Override
    public void onOrderFailure(String msg) {

    }

    @Override
    public void onOrderResponseFail(Throwable t) {

    }

    @Override
    public void onCancelOrderSuccess(List<Order> cartList) {
        onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }

    @Override
    public void onCancelOrderFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelOrderResponseFail(Throwable t) {
        Toast.makeText(this, "Client Error", Toast.LENGTH_SHORT).show();
        Log.d("CancelOrder", t.getMessage());
    }
}