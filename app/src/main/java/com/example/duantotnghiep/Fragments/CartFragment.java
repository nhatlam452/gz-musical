package com.example.duantotnghiep.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Activities.ChooseAddressActivity;
import com.example.duantotnghiep.Activities.ChooseStoreActivity;
import com.example.duantotnghiep.Activities.PaymentActivity;
import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.Adapter.CartAdapter;
import com.example.duantotnghiep.Adapter.PaymentMethodAdapter;
import com.example.duantotnghiep.Adapter.SpinnerAdapter;
import com.example.duantotnghiep.Contract.CartContact;
import com.example.duantotnghiep.Contract.NewsInterface;
import com.example.duantotnghiep.Contract.OrderContract;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.CreateOrder;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.PaymentMethod;
import com.example.duantotnghiep.Presenter.CartPresenter;
import com.example.duantotnghiep.Presenter.OrderPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;


public class CartFragment extends Fragment implements CartContact.View {
    private final CartPresenter cartPresenter = new CartPresenter(this);
    private TextView tvCartPrice, tvCartTotalPrice, tvDelivery, tvGoToStore,
            tvAddressCustomerName, tvAddressCustomer, tvStoreName, tvNotificationCart,imgPricePaymentMethod,
            tvStoreAddress, tvPaymentMethodName, tvShippingPrice,tvNameCartPayment;
    private ImageView imgChooseStore;
    private List<Cart> mList;
    private EditText edtNote;
    private String paymentMethod = "Thanh toán khi nhận hàng";
    float sum = 0;
    float mSum = 0;
    private RelativeLayout rlCustomer, rltStore;
    private String note = "null";
    private final ActivityResultLauncher<Intent> launchChooseStore = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        tvStoreName.setText(data.getStringExtra("storeInfoName"));
                        tvStoreAddress.setText(data.getStringExtra("storeInfoAddress"));
                    }
                }
            });
    private final ActivityResultLauncher<Intent> launchChooseAddress = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getBooleanExtra("currentLocation", false)) {
                            getCurrentLocation();
                        } else {
                            tvAddressCustomerName.setText(data.getStringExtra("addressName"));
                            tvAddressCustomer.setText(data.getStringExtra("address"));
                        }

                    }
                }
            });
    private RecyclerView rcvCartFragment;


    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);

        mList = new ArrayList<>();
        //zalo pay
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
            view.findViewById(R.id.svCart).setOnTouchListener(new TranslateAnimation(getActivity(), bottomNavigationView));

        }

        getCurrentLocation();

        tvDelivery.setOnClickListener(v -> {
            imgChooseStore.setVisibility(View.GONE);
            tvDelivery.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cartTabColor));
            tvGoToStore.setBackgroundColor(Color.TRANSPARENT);
            rlCustomer.setVisibility(View.VISIBLE);
            mSum = sum + 400000;
            tvCartTotalPrice.setText("$" + NumberFormat.getInstance().format(mSum));
            tvShippingPrice.setText("$40.000");
        });
        tvGoToStore.setOnClickListener(v -> {
            tvGoToStore.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cartTabColor));
            tvDelivery.setBackgroundColor(Color.TRANSPARENT);
            imgChooseStore.setVisibility(View.VISIBLE);
            rlCustomer.setVisibility(View.GONE);
            tvShippingPrice.setText("$0");
            mSum = sum;
            tvCartTotalPrice.setText("$" + NumberFormat.getInstance().format(mSum));

        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                cartPresenter.onRemoveCart(mList.get(position).getCartId());
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvCartFragment);
        view.findViewById(R.id.imgChooseAddress).setOnClickListener(v -> {
            Intent i = new Intent(getContext(), ChooseAddressActivity.class);
            i.putExtra("addressHint", tvAddressCustomer.getText().toString());
            launchChooseAddress.launch(i);
            getActivity().overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });

        view.findViewById(R.id.btnPayment).setOnClickListener(v -> {

            if (mList.size() == 0) {
                Toast.makeText(getContext(), "You don't have any items in cart", Toast.LENGTH_SHORT).show();
                return;
            }
            int userId = Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId());
            String storeName = tvStoreName.getText().toString();
            String customerAddress = tvAddressCustomer.getText().toString();
            note = edtNote.getText().toString().trim();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String time = format.format(date);
            int orderMethod = 0; // 0 : delivery - 1 : go to store
            if (rlCustomer.getVisibility() == View.GONE) {
                orderMethod = 1;
            }
            Order order = new Order(userId, 0, mSum, note, time, orderMethod, storeName,
                    customerAddress, tvPaymentMethodName.getText().toString(), mList);
            Gson gson = new Gson();
            String json = gson.toJson(order);
            Intent i = new Intent(getContext(), PaymentActivity.class);
            i.putExtra("OrderInfo",json);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.anim_fadein,R.anim.anim_fadeout);
        });
        view.findViewById(R.id.imgChooseStore).setOnClickListener(v -> {
            launchChooseStore.launch(new Intent(getContext(), ChooseStoreActivity.class));
            getActivity().overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        view.findViewById(R.id.imgChangePaymentMethod).setOnClickListener(v->{
            openChoosePaymentMethodDialog(view.findViewById(R.id.imgPaymentMethod),view.findViewById(R.id.tvPaymentMethodName));
        });
    }

    private void openChoosePaymentMethodDialog(ImageView imgView ,TextView tv) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_spinner);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        List<PaymentMethod> pList = getPaymentListData();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        TextView tvTitle = dialog.findViewById(R.id.tvSpinnerTitle);
        RecyclerView rcvSpinner = dialog.findViewById(R.id.rcvSpinner);

        tvTitle.setText("Payment Method");
        PaymentMethodAdapter paymentMethodAdapter = new PaymentMethodAdapter(getContext(), pList, new PaymentMethodAdapter.OnClickSetText() {
            @Override
            public void onClickSetText(int imgSrc, String s) {
                imgView.setImageResource(imgSrc);
                tv.setText(s);
                dialog.dismiss();
                switch (s) {
                    case "Thanh toán khi nhận hàng":
                        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                        break;
                    case "Thanh toán bằng ví Zalo Pay" :
                        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.zaloColor));

                        break;
                    default:
                        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.momoColor));
                        break;
                }
            }
        });
        rcvSpinner.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSpinner.setAdapter(paymentMethodAdapter);
        dialog.show();
    }

    private List<PaymentMethod> getPaymentListData() {
        List<PaymentMethod> pmList = new ArrayList<>();
        pmList.add(new PaymentMethod(R.drawable.img_cod,"Thanh toán khi nhận hàng"));
        pmList.add(new PaymentMethod(R.drawable.img_zalo_pay,"Thanh toán bằng ví Zalo Pay"));
        pmList.add(new PaymentMethod(R.drawable.img_momo,"Thanh toán bằng ví Momo"));
        return  pmList;
    };


    private void initUI(View view) {
        tvCartPrice = view.findViewById(R.id.tvCartPrice);
        edtNote = view.findViewById(R.id.edtNoteOrder);
        rcvCartFragment = view.findViewById(R.id.rcvCartFragment);
        tvPaymentMethodName = view.findViewById(R.id.tvPaymentMethodName);
        tvShippingPrice = view.findViewById(R.id.tvShippingPrice);
        imgChooseStore = view.findViewById(R.id.imgChooseStore);
        tvStoreAddress = view.findViewById(R.id.tvStoreAddressCart);
        tvStoreName = view.findViewById(R.id.tvStoreNameCart);
        tvNotificationCart = view.findViewById(R.id.tvNotificationCart);
        tvAddressCustomerName = view.findViewById(R.id.tvAddressCustomerName);
        tvAddressCustomer = view.findViewById(R.id.tvAddressCustomer);
        rlCustomer = view.findViewById(R.id.rlCustomer);
        rltStore = view.findViewById(R.id.rltStore);
        tvCartTotalPrice = view.findViewById(R.id.tvCartTotalPrice);
        tvGoToStore = view.findViewById(R.id.tvGoToStore);
        tvDelivery = view.findViewById(R.id.tvDelivery);
        tvNameCartPayment = view.findViewById(R.id.tvNameCartPayment);
        imgPricePaymentMethod = view.findViewById(R.id.imgPricePaymentMethod);
        tvNameCartPayment.setText(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getFirstName() + " "
        + LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getLastName()
        );

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                tvAddressCustomerName.setText("Your Current Location");
                                tvAddressCustomer.setText(addresses.get(0).getAddressLine(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onCartSuccess(List<Cart> cartList) {
        if (cartList == null) {
            cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
            return;
        }
        mList = cartList;

        if (cartList.size() <= 0) {
            rcvCartFragment.setVisibility(View.GONE);
            tvNotificationCart.setText("* You don't have any items in cart");
        } else {
            rcvCartFragment.setVisibility(View.VISIBLE);
            tvNotificationCart.setText("* Swipe to left to delete item");
        }

        sum = 0;
        for (int i = 0; i < cartList.size(); i++) {
            sum = sum + (cartList.get(i).getQuantity() * cartList.get(i).getPrice());
        }
        mSum = sum;
        tvCartPrice.setText("$" + NumberFormat.getInstance().format(sum));
        tvCartTotalPrice.setText("$" + NumberFormat.getInstance().format(sum + 40000));
        imgPricePaymentMethod.setText("$ " + NumberFormat.getInstance().format(sum + 40000));
        CartAdapter cartAdapter = new CartAdapter(getContext(), cartList, new CartAdapter.OnClickListener() {
            @Override
            public void onClick(int productId, int quantity) {
                Intent i = new Intent(getContext(), ProductDetailActivity.class);
                i.putExtra("productId",productId);
                i.putExtra("quantity",quantity);
                getContext().startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_fadein,R.anim.anim_fadeout);
            }
        });
        rcvCartFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCartFragment.setAdapter(cartAdapter);

    }

    @Override
    public void onCartFailure(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartResponseFail(Throwable t) {
        Log.d("Cart Fragment", t.getMessage());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
    }






}
