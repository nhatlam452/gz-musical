package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.ProductDetailAdapter;
import com.example.duantotnghiep.Adapter.ProductDetailImageAdapter;
import com.example.duantotnghiep.Contract.DetailContract;
import com.example.duantotnghiep.Model.Images;
import com.example.duantotnghiep.Model.ProductDetail;
import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Presenter.ProductDetailPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity implements DetailContract.View {
    private ViewPager2 vpProductDetailImage, vpProductDetail;
    private TabLayout tlProductDetail;
    private ProductDetail productDetail;
    private TextView tvDescriptionDetail, tvProductDetailName;
    private ProductDetailPresenter detailPresenter;
    private int productId;
    public static int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_product_detail);
        detailPresenter = new ProductDetailPresenter(this);
        initUi();
        productId = getIntent().getIntExtra("productId", 0);
        quantity = getIntent().getIntExtra("quantity", 1);

        detailPresenter.onGetProductDetail(productId);
        findViewById(R.id.imgBackProductDetail).setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        findViewById(R.id.imgBackProductDetail).setOnClickListener(v -> onBackPressed());


    }

    private void setUpViewPager() {
        ProductDetailAdapter productDetailAdapter = new ProductDetailAdapter(this);
        vpProductDetail.setAdapter(productDetailAdapter);
        new TabLayoutMediator(tlProductDetail, vpProductDetail, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Detail");
                    break;
                case 1:
                    tab.setText("Video");
                    break;
                case 2:
                    tab.setText("Comment");
                    break;

            }
        }).attach();
    }

    private void initUi() {
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        tvProductDetailName = findViewById(R.id.tvProductDetailName);
        vpProductDetailImage = findViewById(R.id.vpProductDetailImage);
        vpProductDetail = findViewById(R.id.vpProductDetail);
        tlProductDetail = findViewById(R.id.tlProductDetail);


    }

    public int getProductId() {
        return productId;
    }

    public ProductDetail getDetail() {
        return productDetail;
    }

    @Override
    public void getProductDetailSuccess(ProductDetail productDetails) {
        this.productDetail = productDetails;
        setUpViewPager();
        vpProductDetailImage.setOffscreenPageLimit(3);
        vpProductDetailImage.setClipToPadding(false);
        vpProductDetailImage.setClipChildren(false);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        vpProductDetailImage.setPageTransformer(compositePageTransformer);
        ProductDetailImageAdapter productDetailImageAdapter = new ProductDetailImageAdapter(productDetails.getListImage(), this);
        vpProductDetailImage.setAdapter(productDetailImageAdapter);
        tvProductDetailName.setText(productDetail.getProductName());
        tvDescriptionDetail.setText(productDetail.getDescription());
        AppUtil.showDialog.dismiss();

    }


    @Override
    public void getProductDetailFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        AppUtil.showDialog.dismiss();
        Log.d("Product Detail Fragment", "Error : " + msg);
    }

    @Override
    public void getProductResponseFail(Throwable t) {
        Toast.makeText(this, "Unknown Error. Please check your network", Toast.LENGTH_SHORT).show();
        Log.d("Product Detail Fragment", "Error : " + t.getMessage());
        AppUtil.showDialog.dismiss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }
}