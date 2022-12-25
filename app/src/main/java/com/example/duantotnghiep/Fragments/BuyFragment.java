package com.example.duantotnghiep.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.duantotnghiep.Adapter.ProductsAdapter;
import com.example.duantotnghiep.Adapter.SearchAdapter;
import com.example.duantotnghiep.Adapter.SpinnerAdapter;
import com.example.duantotnghiep.Contract.ProductContract;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Presenter.ProductPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class BuyFragment extends Fragment implements ProductContract.View {
    private RecyclerView rcvProducts, rcvSearch;
    private EditText edtSearch;
    private RelativeLayout layoutSearch;
    private ProductPresenter presenter = new ProductPresenter(this);
    private final String TAG = "BUY_FRAGMENT";
    private List<Products> mList;
    private int currentType;
    private ProductsAdapter adapter;
    private ImageView imgTypeDisplay;
    private String brandName = "null";
    private String order = "null";
    private String sortType = "";
    private SearchAdapter searchAdapter;
    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                // Perform the desired action here
                presenter.getProduct();
            }
        }
    };
    private float fPrice = 0, sPrice = 1000000000;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvProducts = view.findViewById(R.id.rcvProducts);
        imgTypeDisplay = view.findViewById(R.id.imgTypeDisplay);
        edtSearch = view.findViewById(R.id.edtSearch);
        rcvSearch = view.findViewById(R.id.rcvSearch);
        layoutSearch = view.findViewById(R.id.layoutSearch);
        AppUtil.showDialog.show(getContext());
        presenter.getProduct();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(networkChangeReceiver, filter);

        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationMain);
            rcvProducts.setOnTouchListener(new TranslateAnimation(getActivity(), bottomNavigationView));

        }
        imgTypeDisplay.setOnClickListener(v -> {
            if (currentType == Products.TYPE_GRID) {
                imgTypeDisplay.setImageResource(R.drawable.ic_baseline_list_24);
                rcvProducts.setAdapter(adapter);
                rcvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
                setTypeDisplay(Products.TYPE_LIST);
            } else {
                imgTypeDisplay.setImageResource(R.drawable.ic_baseline_grid_on_24);
                rcvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
                rcvProducts.setAdapter(adapter);
                setTypeDisplay(Products.TYPE_GRID);
            }
            adapter.notifyDataSetChanged();
        });
        view.findViewById(R.id.imgOpenSearch).setOnClickListener(v->{
            Transition transition = new Slide(Gravity.START);
            transition.setDuration(600);
            transition.addTarget(R.id.layoutSearch);

            TransitionManager.beginDelayedTransition((ViewGroup) view.getRootView(), transition);
            layoutSearch.setVisibility(View.VISIBLE);
        });
        view.findViewById(R.id.imgCloseSearch).setOnClickListener(v->{
            Transition transition = new Slide(Gravity.START);
            transition.setDuration(600);
            transition.addTarget(R.id.layoutSearch);

            TransitionManager.beginDelayedTransition((ViewGroup) view.getRootView(), transition);
            layoutSearch.setVisibility(View.GONE);
        });

        view.findViewById(R.id.tvPriceFilter).setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            mList.add("Tất cả");
            mList.add("Giá dưới 1.000.000 đ");
            mList.add("1.000.000 đ - 2.000.000 đ");
            mList.add("2.000.000 đ - 3.000.000 đ");
            mList.add("3.000.000 đ - 5.000.000 đ");
            mList.add("5.000.000 đ - 10.000.000 đ");
            mList.add("Giá trên 10.000.000 đ");
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_spinner);
            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);

            TextView tvTitle = dialog.findViewById(R.id.tvSpinnerTitle);
            RecyclerView rcvSpinner = dialog.findViewById(R.id.rcvSpinner);

            tvTitle.setText(getResources().getString(R.string.price));
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), mList, s -> {
                switch (s) {
                    case "Tất cả":
                        fPrice = 0;
                        sPrice = 1000000000;
                        break;
                    case "Giá dưới 1.000.000 đ":
                        fPrice = 0;
                        sPrice = 1000000;
                        break;
                    case "1.000.000 đ - 2.000.000 đ":
                        fPrice = 1000000;
                        sPrice = 2000000;
                        break;
                    case "2.000.000 đ - 3.000.000 đ":
                        fPrice = 2000000;
                        sPrice = 3000000;
                        break;
                    case "3.000.000 đ - 5.000.000 đ":
                        fPrice = 3000000;
                        sPrice = 5000000;
                        break;
                    case "5.000.000 đ - 10.000.000 đ":
                        fPrice = 5000000;
                        sPrice = 10000000;
                        break;
                    case "Giá trên 10.000.000 đ":
                        fPrice = 10000000;
                        sPrice = 1000000000;
                        break;
                }
                presenter.onGetProductByPrice(fPrice, sPrice, brandName, order, sortType);
                Log.d("product ===>", fPrice + "" + sPrice + "" + "'" + brandName + "'" + "" + order + "" + sortType);
                AppUtil.showDialog.show(getContext());
                dialog.dismiss();
                TextView tv = view.findViewById(R.id.tvPriceFilter);
                tv.setText(s);

            });
            rcvSpinner.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvSpinner.setAdapter(spinnerAdapter);

            dialog.show();
        });
        view.findViewById(R.id.tvTypeFilter).setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            mList.add("Tất cả");
            mList.add("Yamaha");
            mList.add("Martin");
            mList.add("Cordoba");
            mList.add("Taylor");
            mList.add("Enya");
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_spinner);
            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);

            TextView tvTitle = dialog.findViewById(R.id.tvSpinnerTitle);
            RecyclerView rcvSpinner = dialog.findViewById(R.id.rcvSpinner);

            tvTitle.setText(getResources().getString(R.string.brand));
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), mList, s -> {
                switch (s) {
                    case "Tất cả":
                        brandName = "null";
                        break;
                    case "Yamaha":
                        brandName = "'" + "Yamaha" + "'";
                        break;
                    case "Martin":
                        brandName = "'" + "Martin" + "'";
                        break;
                    case "Cordoba":
                        brandName = "'" + "Cordoba" + "'";
                        break;
                    case "Taylor":
                        brandName = "'" + "Taylor" + "'";
                        break;
                    case "Enya":
                        brandName = "'" + "Enya" + "'";
                        break;

                }
                presenter.onGetProductByPrice(fPrice, sPrice, brandName, order, sortType);
                Log.d("product ===>", fPrice + "" + sPrice + "" + "'" + brandName + "'" + "" + order + "" + sortType);

                AppUtil.showDialog.show(getContext());
                dialog.dismiss();
                TextView tv = view.findViewById(R.id.tvTypeFilter);
                tv.setText(s);

            });
            rcvSpinner.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvSpinner.setAdapter(spinnerAdapter);

            dialog.show();
        });
        view.findViewById(R.id.tvSort).setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            mList.add("Hàng mới nhất");
            mList.add("Hàng cũ nhất");
            mList.add("Tên : A -> Z");
            mList.add("Tên : Z -> A");
            mList.add("Giá tăng dần");
            mList.add("Giá giảm dần");
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_spinner);
            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);

            TextView tvTitle = dialog.findViewById(R.id.tvSpinnerTitle);
            RecyclerView rcvSpinner = dialog.findViewById(R.id.rcvSpinner);

            tvTitle.setText("Sort");
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), mList, s -> {
                switch (s) {
                    case "Hàng mới nhất":
                        order = "productId";
                        sortType = "desc";
                        break;
                    case "Hàng cũ nhất":
                        order = "productId";
                        sortType = "asc";
                        break;
                    case "Tên : A -> Z":
                        order = "productName";
                        sortType = "asc";
                        break;
                    case "Tên : Z -> A":
                        order = "productName";
                        sortType = "desc";
                        break;
                    case "Giá tăng dần":
                        order = "price";
                        sortType = "asc";
                        break;
                    case "Giá giảm dần":
                        order = "price";
                        sortType = "desc";
                        break;

                }
                presenter.onGetProductByPrice(fPrice, sPrice, brandName, order, sortType);
                AppUtil.showDialog.show(getContext());
                dialog.dismiss();
                TextView tv = view.findViewById(R.id.tvSort);
                tv.setText(s);

            });
            rcvSpinner.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvSpinner.setAdapter(spinnerAdapter);

            dialog.show();
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buy, container, false);

    }

    @Override
    public void setProductList(List<Products> mListProduct) {
        mList = new ArrayList<>();
        mList = mListProduct;
        setTypeDisplay(2);
        adapter = new ProductsAdapter(getContext(), mListProduct);
        rcvProducts.setAdapter(adapter);
        rcvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvProducts.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Products> sList = new ArrayList<>();
                if (s.toString().isEmpty()) {
                    rcvSearch.setVisibility(View.GONE);
                } else {
                    rcvSearch.setVisibility(View.VISIBLE);

                    for (Products products : mListProduct) {
                        if (products.getProductName().toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
                            sList.add(products);
                        }
                    }
                    searchAdapter = new SearchAdapter(getContext(), sList);
                    rcvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
                    rcvSearch.setAdapter(searchAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        AppUtil.showDialog.dismiss();

    }

    private void setTypeDisplay(int typeDisplay) {
        currentType = typeDisplay;
        for (Products products : mList) {
            products.setTypeDisplay(typeDisplay);
        }
    }

    @Override
    public void onResponseFail(Throwable t) {
        AppUtil.showDialog.dismiss();
        Log.d(TAG, t.getMessage());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter = new ProductPresenter(this);
    }
}