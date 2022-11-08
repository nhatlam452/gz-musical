package com.example.duantotnghiep.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.duantotnghiep.Adapter.ProductDetailAdapter;
import com.example.duantotnghiep.Adapter.ProductDetailImageAdapter;
import com.example.duantotnghiep.Fragments.ProductDetailFragment;
import com.example.duantotnghiep.Model.Images;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView tvProductDetailName, tvDescriptionDetail;
    private ViewPager2 vpProductDetailImage, vpProductDetail;
    private ScrollView svProductDetail;
    private TabLayout tlProductDetail;
    private Products products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Gson gson = new Gson();
        String json = getIntent().getStringExtra("product");
        products = gson.fromJson(json, Products.class);
        Log.d("Product Detail ",json);
        initUi();
        setUpViewPager();



    }
    public Products getProductId() {
        return products;
    }
    public void setUpImageViewPage(List<Images> mList) {
        ProductDetailImageAdapter productDetailImageAdapter = new ProductDetailImageAdapter(mList, this);
        vpProductDetailImage.setAdapter(productDetailImageAdapter);
    }

    private void setUpViewPager() {
        ProductDetailAdapter productDetailAdapter = new ProductDetailAdapter(this);
        vpProductDetail.setAdapter(productDetailAdapter);
        new TabLayoutMediator(tlProductDetail, vpProductDetail, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Detail");
                        break;
                    case 1:
                        tab.setText("Comment");
                        break;
                }
            }
        }).attach();
    }

    private void initUi() {


        tvProductDetailName = findViewById(R.id.tvProductDetailName);
        vpProductDetailImage = findViewById(R.id.vpProductDetailImage);
        vpProductDetail = findViewById(R.id.vpProductDetail);
        svProductDetail = findViewById(R.id.svProductDetail);
        tlProductDetail = findViewById(R.id.tlProductDetail);
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        tvProductDetailName.setText(products.getProductName());
        tvDescriptionDetail.setText(products.getDescription());
    }

}