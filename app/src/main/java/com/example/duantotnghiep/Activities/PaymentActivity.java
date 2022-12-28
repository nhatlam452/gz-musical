package com.example.duantotnghiep.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.CartAdapter;
import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Model.CreateOrder;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.OrderPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppConstants;
import com.example.duantotnghiep.Utilities.AppInfo;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity implements OrderContract.View {
    private TextView tvDeliveryMethodPaymentConfirm, tvStorePaymentConfirm, tvAddressPaymentConfirm, tvPaymentMethodPaymentConfirm, tvNotePaymentConfirm, tvTotalPriceItemPaymentConfirm, tvTotalPricePaymentConfirm;
    private Button btnPay;
    private EditText tvPhonePaymentConfirm;
    private RecyclerView rcvPaymentConfirm;
    private Order order = null;

    private final OrderPresenter orderPresenter = new OrderPresenter(this);
    private LinearLayout llUserAddressPC, llStorePC, llNotePC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initUI();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        findViewById(R.id.imgBackPC).setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        btnPay.setOnClickListener(v -> {
            Order orderConfirm = order;
            if (!AppUtil.ValidateInput.isValidPhoneNumber(tvPhonePaymentConfirm.getText().toString()) && !AppUtil.ValidateInput.isValidPhoneNumber(tvPhonePaymentConfirm.getHint().toString())) {
                Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                return;
            } else if (AppUtil.ValidateInput.isValidPhoneNumber(tvPhonePaymentConfirm.getHint().toString())) {
                if (tvPhonePaymentConfirm.getText().toString().isEmpty()) {
                } else if (!AppUtil.ValidateInput.isValidPhoneNumber(tvPhonePaymentConfirm.getText().toString())) {
                    Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (AppUtil.ValidateInput.isValidPhoneNumber(tvPhonePaymentConfirm.getText(). toString())) {
                    orderConfirm = new Order(order.getUserId(), tvPhonePaymentConfirm.getText().toString(),
                            order.getStatus(), order.getTotal(), orderConfirm.getNote(), order.getCreateDate(), order.getDeliveryTime(),
                            order.getOrderMethod(), order.getdFrom(), order.getdTo(), order.getPaymentMethod(), order.getmList()
                    );
                }
            }
            switch (order.getPaymentMethod()) {
                case "Thanh toán bằng ví Zalo Pay":
                    CreateOrder orderApi = new CreateOrder();
                    try {
                        DecimalFormat format = new DecimalFormat("0.#");
                        Log.d("Zalo Failed ", format.format(order.getTotal()) + "");
                        JSONObject data = orderApi.createOrder(format.format(order.getTotal()));
                        String code = data.getString("return_code");
                        if (code.equals("1")) {
                            String token = data.getString("zp_trans_token");
                            Order finalOrderConfirm = orderConfirm;
                            Gson gson = new Gson();
                            String json = gson.toJson(finalOrderConfirm);
                            Log.d("order nè", "Order : " + json);
                            ZaloPaySDK.getInstance().payOrder(this, token, "demozpdk://app", new PayOrderListener() {
                                @Override
                                public void onPaymentSucceeded(String s, String s1, String s2) {
                                    orderPresenter.onNewOrder(finalOrderConfirm);
                                }

                                @Override
                                public void onPaymentCanceled(String s, String s1) {
                                    Toast.makeText(PaymentActivity.this, "Payment has been canceled", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                    Toast.makeText(PaymentActivity.this, "" + zaloPayError.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.d("Zalo Failed ", e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case "Thanh toán bằng ví Momo":
                    //Momo Api
                    break;
                default:
                    Order finalOrderConfirm = orderConfirm;
                    Gson gson = new Gson();
                    String json = gson.toJson(finalOrderConfirm);
                    Log.d("order nè", "Order : " + json);
                    orderPresenter.onNewOrder(finalOrderConfirm);
                    break;
            }

        });
    }


    private void initUI() {
        tvPhonePaymentConfirm = findViewById(R.id.tvPhonePaymentConfirm);
        llStorePC = findViewById(R.id.llStorePC);
        llUserAddressPC = findViewById(R.id.llUserAddressPC);
        llNotePC = findViewById(R.id.llNotePC);
        tvDeliveryMethodPaymentConfirm = findViewById(R.id.tvDeliveryMethodPaymentConfirm);
        tvStorePaymentConfirm = findViewById(R.id.tvStorePaymentConfirm);
        tvPaymentMethodPaymentConfirm = findViewById(R.id.tvPaymentMethodPaymentConfirm);
        tvAddressPaymentConfirm = findViewById(R.id.tvAddressPaymentConfirm);
        tvNotePaymentConfirm = findViewById(R.id.tvNotePaymentConfirm);
        tvTotalPriceItemPaymentConfirm = findViewById(R.id.tvTotalPriceItemPaymentConfirm);
        tvTotalPricePaymentConfirm = findViewById(R.id.tvTotalPricePaymentConfirm);
        btnPay = findViewById(R.id.btnPay);
        rcvPaymentConfirm = findViewById(R.id.rcvPaymentConfirm);
        String orderJson = getIntent().getStringExtra("OrderInfo");
        Gson gson = new Gson();
        order = gson.fromJson(orderJson, Order.class);
        if (LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getPhoneNumber() != null) {
            tvPhonePaymentConfirm.setHint(LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getPhoneNumber());
        }
        if (order.getOrderMethod() == 0) {
            tvDeliveryMethodPaymentConfirm.setText(getResources().getString(R.string.delivery));
            llStorePC.setVisibility(View.GONE);
        } else {
            tvDeliveryMethodPaymentConfirm.setText(getResources().getString(R.string.go_to_store));
            llUserAddressPC.setVisibility(View.GONE);

        }
        tvStorePaymentConfirm.setText(order.getdFrom());
        tvAddressPaymentConfirm.setText(order.getdTo());
        tvPaymentMethodPaymentConfirm.setText(order.getPaymentMethod());
        if (order.getNote().isEmpty() || order.getNote() == null) {
            llNotePC.setVisibility(View.GONE);
        } else {
            tvNotePaymentConfirm.setText(order.getNote());
        }
        tvTotalPricePaymentConfirm.setText(NumberFormat.getInstance().format(order.getTotal()) + "VND");
        btnPay.setText("PAY : " + NumberFormat.getInstance().format(order.getTotal()) + "VND");
        CartAdapter cartAdapter = new CartAdapter(this, order.getmList(), new CartAdapter.OnClickListener() {
            @Override
            public void onClick(int productId, int quantity) {

            }
        });
        rcvPaymentConfirm.setLayoutManager(new LinearLayoutManager(this));
        rcvPaymentConfirm.setAdapter(cartAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    @Override
    public void onOrderSuccess(List<Order> cartList) {
        Intent i = new Intent(this, SuccessActivity.class);
        i.putExtra("isInMain", true);
        i.putExtra("Notification", "Your order has been received. We will contact to you to confirm once again soon. Thank you for choosing Gz Musical");
        startActivity(i);
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        finish();
        AppUtil.onGetNotification(this, "Your order has been received. We will contact to you to confirm once again soon. Thank you for choosing Gz Musical");

    }

    @Override
    public void onOrderFailure(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOrderResponseFail(Throwable t) {
        Toast.makeText(this, "Unknown Error . Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("Payment Activity", t.getMessage());
    }

    @Override
    public void onCancelOrderSuccess(List<Order> cartList) {

    }

    @Override
    public void onCancelOrderFailure(String msg) {

    }

    @Override
    public void onCancelOrderResponseFail(Throwable t) {

    }
}