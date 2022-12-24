package com.example.duantotnghiep.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
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
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantotnghiep.Activities.ChooseAddressActivity;
import com.example.duantotnghiep.Activities.ChooseStoreActivity;
import com.example.duantotnghiep.Activities.PaymentActivity;
import com.example.duantotnghiep.Activities.ProductDetailActivity;
import com.example.duantotnghiep.Adapter.CartAdapter;
import com.example.duantotnghiep.Adapter.PaymentMethodAdapter;
import com.example.duantotnghiep.Contract.CartContact;
import com.example.duantotnghiep.Model.Cart;
import com.example.duantotnghiep.Model.Order;
import com.example.duantotnghiep.Model.PaymentMethod;
import com.example.duantotnghiep.Presenter.CartPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppConstants;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPaySDK;


public class CartFragment extends Fragment implements CartContact.View {
    private final CartPresenter cartPresenter = new CartPresenter(this);
    private TextView tvCartPrice, tvCartTotalPrice, tvDelivery, tvGoToStore,
            tvAddressCustomerName, tvAddressCustomer, tvStoreName, tvNotificationCart, imgPricePaymentMethod,
            tvStoreAddress, tvPaymentMethodName, tvShippingPrice, tvNameCartPayment, tvShippingTime;
    private ImageView imgChooseStore;
    private List<Cart> mList;
    private EditText edtNote;
    private final String paymentMethod = "Thanh toán khi nhận hàng";
    double sum = 0;
    double mSum = 0;
    private SharedPreferences mSharePrefer = null;
    private SharedPreferences.Editor mEditor;
    private RelativeLayout rlCustomer, rltStore;
    private String note = "null";
    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                // Perform the desired action here
                cartPresenter.onGetCart(Integer.parseInt(LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getUserId()));
            }
        }
    };
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);

        mList = new ArrayList<>();
        mSharePrefer = getContext().getSharedPreferences(AppConstants.CHECK_PERMISSION, 0);

        mEditor = mSharePrefer.edit();

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
        tvShippingTime.setOnClickListener(v -> {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        });
        getCurrentLocation();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(networkChangeReceiver, filter);
        tvDelivery.setOnClickListener(v -> {
            imgChooseStore.setVisibility(View.GONE);
            tvDelivery.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cartTabColor));
            tvGoToStore.setBackgroundColor(Color.TRANSPARENT);
            rlCustomer.setVisibility(View.VISIBLE);
            mSum = sum + 40000;
            tvCartTotalPrice.setText("$" + NumberFormat.getInstance().format(mSum));
            imgPricePaymentMethod.setText("$" + NumberFormat.getInstance().format(mSum));
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
            tvShippingPrice.setText("$" + NumberFormat.getInstance().format(mSum));

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
            String phone = LocalStorage.getInstance(getContext()).getLocalStorageManager().getUserInfo().getPhoneNumber();
            Order order = null;
            if (phone == null) {
                order = new Order(userId, "", 1, mSum, note, time, tvShippingTime.getText().toString().trim(), orderMethod, storeName,
                        customerAddress, tvPaymentMethodName.getText().toString(), mList);
            } else {
                order = new Order(userId, phone, 1, mSum, note, time, tvShippingTime.getText().toString().trim(), orderMethod, storeName,
                        customerAddress, tvPaymentMethodName.getText().toString(), mList);
            }

            Gson gson = new Gson();
            String json = gson.toJson(order);
            Intent i = new Intent(getContext(), PaymentActivity.class);
            Log.d("Order nè", "Order : " + json);
            i.putExtra("OrderInfo", json);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        view.findViewById(R.id.imgChooseStore).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mEditor.putBoolean(AppConstants.iSLocationPermissionRequest, true);
                mEditor.apply();
            }
            boolean isPermissionGranted = mSharePrefer.getBoolean(AppConstants.iSLocationPermissionRequest, false);
            boolean isPermissionGrantedOnetime = mSharePrefer.getBoolean(AppConstants.iSLocationPermissionRequestOnetime, false);
            if (isPermissionGranted || isPermissionGrantedOnetime) {
                Log.d("Permisstion", "a " + isPermissionGranted + "b "+ isPermissionGrantedOnetime);
                launchChooseStore.launch(new Intent(getContext(), ChooseStoreActivity.class));
                getActivity().overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
            } else {
                checkMyPermission();
            }
        });
        view.findViewById(R.id.imgChangePaymentMethod).setOnClickListener(v -> {
            openChoosePaymentMethodDialog(view.findViewById(R.id.imgPaymentMethod), view.findViewById(R.id.tvPaymentMethodName));
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(networkChangeReceiver);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, this, year, month, day);

            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Convert the selected date to a string in the desired format
            String dateString = String.format("%d-%d-%d", day, month + 1, year);

            // Do something with the date string, such as display it in a TextView
            TextView textView = getActivity().findViewById(R.id.tvShippingTime);
            textView.setText(dateString);
        }
    }

    private void openChoosePaymentMethodDialog(ImageView imgView, TextView tv) {
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
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        break;
                    case "Thanh toán bằng ví Zalo Pay":
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.zaloColor));

                        break;
                    default:
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.momoColor));
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
        pmList.add(new PaymentMethod(R.drawable.img_cod, "Thanh toán khi nhận hàng"));
        pmList.add(new PaymentMethod(R.drawable.img_zalo_pay, "Thanh toán bằng ví Zalo Pay"));
        pmList.add(new PaymentMethod(R.drawable.img_momo, "Thanh toán bằng ví Momo"));
        return pmList;
    }


    private void initUI(View view) {
        tvShippingTime = view.findViewById(R.id.tvShippingTime);
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
        Date date = new Date();
        SimpleDateFormat set = new SimpleDateFormat("dd-MM-yyyy");
        tvShippingTime.setText(set.format(date.getTime()));
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mEditor.putBoolean(AppConstants.isWritePermissionRequest, true);
            mEditor.apply();
        }
        boolean isPermissionGranted = mSharePrefer.getBoolean(AppConstants.iSLocationPermissionRequest, false);
        boolean isPermissionGrantedOnetime = mSharePrefer.getBoolean(AppConstants.iSLocationPermissionRequestOnetime, false);
        if (isPermissionGranted || isPermissionGrantedOnetime) {
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
        } else {
            checkMyPermission();
        }
    }

    private void checkMyPermission() {

        Dexter.withContext(getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                getCurrentLocation();
                mEditor.putBoolean(AppConstants.iSLocationPermissionRequest, true);
                mEditor.putBoolean(AppConstants.iSLocationPermissionRequestOnetime, true);
                mEditor.apply();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getContext(), "Please Granted Permission in your Setting", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
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
            sum = sum + (cartList.get(i).getQuantity() * (cartList.get(i).getPrice() * (100 - cartList.get(i).getDiscount()) / 100));
        }
        mSum = sum;
        tvCartPrice.setText("$" + NumberFormat.getInstance().format(sum));
        tvCartTotalPrice.setText("$" + NumberFormat.getInstance().format(sum + 40000));
        imgPricePaymentMethod.setText("$ " + NumberFormat.getInstance().format(sum + 40000));
        CartAdapter cartAdapter = new CartAdapter(getContext(), cartList, new CartAdapter.OnClickListener() {
            @Override
            public void onClick(int productId, int quantity) {
                Intent i = new Intent(getContext(), ProductDetailActivity.class);
                i.putExtra("productId", productId);
                i.putExtra("quantity", quantity);
                getContext().startActivity(i);
                getActivity().overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
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
