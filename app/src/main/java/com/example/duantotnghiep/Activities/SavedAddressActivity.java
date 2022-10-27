package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.UserAddressAdapter;
import com.example.duantotnghiep.Contract.AddressContact;
import com.example.duantotnghiep.Model.UserAddress;
import com.example.duantotnghiep.Presenter.AddressPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;


public class SavedAddressActivity extends AppCompatActivity implements AddressContact.View {
    private AddressPresenter presenter;
    private RecyclerView rcvAddress;
    private final String userId = LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getUserId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_address);
        rcvAddress = findViewById(R.id.rcvAddress);
        presenter = new AddressPresenter(this);
        AppUtil.showDialog.show(this);
        presenter.onGetAddress(userId);
        findViewById(R.id.btnAddAddress).setOnClickListener(v-> openDialogAddAddress());
    }

    private void openDialogAddAddress() {
        Dialog addressDialog = new Dialog(this);
        addressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addressDialog.setContentView(R.layout.dialog_add_address);
        Window window = addressDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        TextInputLayout tipAddressTitle = window.findViewById(R.id.tipAddressTitle);
        TextInputLayout tipAddAddress = window.findViewById(R.id.tipAddAddress);
        TextInputLayout tipAddCity = window.findViewById(R.id.tipAddCity);
        TextInputLayout tipAddDistrict = window.findViewById(R.id.tipAddDistrict);
        TextInputLayout tipAddWard = window.findViewById(R.id.tipAddWard);

        EditText edtAddressTitle = window.findViewById(R.id.edtAddressTitle);
        EditText edtAddAddress = window.findViewById(R.id.edtAddAddress);
        EditText edtAddCity = window.findViewById(R.id.edtAddCity);
        EditText edtAddDistrict = window.findViewById(R.id.edtAddDistrict);
        EditText edtAddWard = window.findViewById(R.id.edtAddWard);

        window.findViewById(R.id.btnSavedAddress).setOnClickListener(v1 ->{
            AppUtil.showDialog.show(this);
            if (checkInputInfo(tipAddressTitle,edtAddressTitle) && checkInputInfo(tipAddAddress,edtAddAddress)
            && checkInputInfo(tipAddCity,edtAddCity) && checkInputInfo(tipAddDistrict,edtAddDistrict) && checkInputInfo(tipAddWard,edtAddWard)
            ){
                String addressName = edtAddressTitle.getText().toString();
                String address = edtAddressTitle.getText().toString();
                String city = edtAddressTitle.getText().toString();
                String district = edtAddressTitle.getText().toString();
                String ward = edtAddressTitle.getText().toString();
                UserAddress userAddress = new UserAddress(null,address,ward,district,city,userId,addressName);
                presenter.onAddAddress(userAddress);
            }
        });

        addressDialog.show();
    }

    private boolean checkInputInfo(TextInputLayout tip, EditText edt) {
        if (edt.getText().toString().isEmpty()){
            tip.setError("This filed can be empty");
            return false;
        }
        return true;
    }

    @Override
    public void onGetAddressSuccess(List<UserAddress> userAddressList) {
        if (userAddressList == null){
            presenter.onGetAddress(userId);
            return;
        }
        UserAddressAdapter addressAdapter = new UserAddressAdapter(this, userAddressList);
        rcvAddress.setLayoutManager(new LinearLayoutManager(this));
        rcvAddress.setAdapter(addressAdapter);
        AppUtil.showDialog.dismiss();
    }

    @Override
    public void onGetAddressFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetAddressResponseFail(Throwable t) {
        Toast.makeText(this, "Sorry for this inconvenience . We have some unknown error ", Toast.LENGTH_SHORT).show();
        Log.d("Saved Address Activity", t.getMessage());
    }
}