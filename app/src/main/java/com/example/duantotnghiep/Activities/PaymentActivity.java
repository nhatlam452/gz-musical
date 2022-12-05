package com.example.duantotnghiep.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.CartAdapter;
import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Model.CreateOrder;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.OrderPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.gson.Gson;

import org.json.JSONObject;

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
    private Order order;
    private OrderPresenter orderPresenter = new OrderPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initUI();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        btnPay.setOnClickListener(v->{
            switch (order.getPaymentMethod()){
                case "Thanh toán bằng ví Zalo Pay" :
                    CreateOrder orderApi = new CreateOrder();
                    try {
                        JSONObject data = orderApi.createOrder("100000");
                        String code = data.getString("return_code");
                        if (code.equals("1")) {
                            String token = data.getString("zp_trans_token");
                            ZaloPaySDK.getInstance().payOrder(this, token, "demozpdk://app", new PayOrderListener() {
                                @Override
                                public void onPaymentSucceeded(String s, String s1, String s2) {

                                }

                                @Override
                                public void onPaymentCanceled(String s, String s1) {

                                }

                                @Override
                                public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "Thanh toán bằng ví Momo":
                    //Momo Api
                    break;
                default:
                    orderPresenter.onNewOrder(order);
                    break;
            }

        });
    }

    private void initUI() {
        tvPhonePaymentConfirm = findViewById(R.id.tvPhonePaymentConfirm);
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
            tvDeliveryMethodPaymentConfirm.setText("Delivery");
        } else {
            tvDeliveryMethodPaymentConfirm.setText("Go to Store");

        }
        tvStorePaymentConfirm.setText(order.getdFrom());
        tvAddressPaymentConfirm.setText(order.getdTo());
        tvPaymentMethodPaymentConfirm.setText(order.getPaymentMethod());
        if (order.getNote().isEmpty() || order.getNote() == null) {
            tvNotePaymentConfirm.setText("Null");
        } else {
            tvNotePaymentConfirm.setText(order.getNote());
        }
        tvTotalPriceItemPaymentConfirm.setText("Total (" + order.getmList().size() + " item(s))");
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
        Intent i = new Intent(this,SuccessActivity.class);
        i.putExtra("isInMain",true);
        i.putExtra("Notification","Your order has been received. We will contact to you to confirm once again . Thank you for choosing Gz Musical");
        startActivity(i);
        overridePendingTransition(R.anim.anim_fadein,R.anim.anim_fadeout);
        finish();
    }

    @Override
    public void onOrderFailure(String msg) {
        Toast.makeText(this, ""+msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOrderResponseFail(Throwable t) {
        Toast.makeText(this, "Unknown Error . Please check your connection", Toast.LENGTH_SHORT).show();
        Log.d("Payment Activity",t.getMessage());
    }
}