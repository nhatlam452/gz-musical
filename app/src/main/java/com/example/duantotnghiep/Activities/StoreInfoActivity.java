package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.duantotnghiep.R;

public class StoreInfoActivity extends AppCompatActivity {
    private TextView tvStoreInfoName,tvStoreInfoAddress;
    private Button btnChooseStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        tvStoreInfoAddress = findViewById(R.id.tvStoreInfoAddress);
        tvStoreInfoName = findViewById(R.id.tvStoreInfoName);
        btnChooseStore = findViewById(R.id.btnChooseStore);

        tvStoreInfoName.setText("Gz " + getIntent().getStringExtra("storeName"));
        tvStoreInfoAddress.setText(getIntent().getStringExtra("storeAddress"));
        if (!getIntent().getBooleanExtra("isChooseStore", true)){
            btnChooseStore.setVisibility(View.GONE);
        }
        findViewById(R.id.btnChooseStore).setOnClickListener(v->{
            Intent intent = new Intent();
            intent.putExtra("storeInfoName",tvStoreInfoName.getText().toString());
            intent.putExtra("storeInfoAddress",tvStoreInfoAddress.getText().toString());
            setResult(RESULT_OK,intent);
            finish();
        });
        findViewById(R.id.imgHotlineStoreInfo).setOnClickListener(v->{
            String phone = "0909916020";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        });
    }
}