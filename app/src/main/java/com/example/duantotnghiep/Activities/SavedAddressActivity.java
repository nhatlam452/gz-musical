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
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.SpinnerLocationAdapter;
import com.example.duantotnghiep.Adapter.UserAddressAdapter;
import com.example.duantotnghiep.Contract.AddressContact;
import com.example.duantotnghiep.Contract.LocationContract;
import com.example.duantotnghiep.Model.Location;
import com.example.duantotnghiep.Model.UserAddress;
import com.example.duantotnghiep.Presenter.AddressPresenter;
import com.example.duantotnghiep.Presenter.LocationPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.example.duantotnghiep.Utilities.TranslateAnimation;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.List;


public class SavedAddressActivity extends AppCompatActivity implements AddressContact.View, LocationContract.View {
    private AddressPresenter presenter;
    private LocationPresenter locationPresenter;
    private RecyclerView rcvAddress;
    private String mCodeDistrict;
    private String mCodeWard;
    private final String userId = LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);
        rcvAddress = findViewById(R.id.rcvAddress);
        locationPresenter = new LocationPresenter(this);
        presenter = new AddressPresenter(this);
        AppUtil.showDialog.show(this);
        presenter.onGetAddress(userId);
        findViewById(R.id.btnAddAddress).setOnClickListener(v -> openDialogAddAddress());
        rcvAddress.setOnTouchListener(new TranslateAnimation(this, findViewById(R.id.btnAddAddress)));
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


        edtAddCity.setOnClickListener(v -> {
            locationPresenter.getCity(edtAddCity, "YOUR CITY");
            edtAddCity.setText(null);
            edtAddWard.setText(null);
            mCodeDistrict = null;
        });
        edtAddDistrict.setOnClickListener(v -> {
            mCodeWard = null;
            locationPresenter.getDistrict(mCodeDistrict, edtAddDistrict, "YOUR DISTRICT");
            edtAddWard.setText(null);
        });
        edtAddWard.setOnClickListener(v -> locationPresenter.getWard(mCodeWard, edtAddWard, "YOUR WARD"));

        window.findViewById(R.id.btnSavedAddress).setOnClickListener(v1 -> {
            AppUtil.showDialog.show(this);
            if (checkInputInfo(tipAddressTitle, edtAddressTitle) && checkInputInfo(tipAddAddress, edtAddAddress)
                    && checkInputInfo(tipAddCity, edtAddCity) && checkInputInfo(tipAddDistrict, edtAddDistrict) && checkInputInfo(tipAddWard, edtAddWard)
            ) {
                String addressName = edtAddressTitle.getText().toString();
                String address = edtAddAddress.getText().toString();
                String city = edtAddCity.getText().toString();
                String district = edtAddDistrict.getText().toString();
                String ward = edtAddWard.getText().toString();

                UserAddress userAddress = new UserAddress(null, address, ward, district, city, userId, addressName);
                Gson gson = new Gson();
                String json = gson.toJson(userAddress);
                Log.d("====> ", "User : " + json + "User id :" + userId);
                presenter.onAddAddress(userAddress);
                addressDialog.dismiss();
            }
        });

        addressDialog.show();
    }

    private void openDialogLocation(List<Location> mList, EditText editText, String title) {
        final Dialog dialog = new Dialog(this);
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

        tvTitle.setText(title);
        SpinnerLocationAdapter spinnerAdapter = new SpinnerLocationAdapter(this, mList, (s, code) -> {
            editText.setText(s);
            if (mCodeDistrict == null) {
                mCodeDistrict = code;
            }
            if (mCodeWard == null) {
                mCodeWard = code;
            }
            dialog.dismiss();
        });
        rcvSpinner.setLayoutManager(new LinearLayoutManager(this));
        rcvSpinner.setAdapter(spinnerAdapter);
        dialog.show();
    }

    private boolean checkInputInfo(TextInputLayout tip, EditText edt) {
        if (edt.getText().toString().isEmpty()) {
            tip.setError("This filed can be empty");
            return false;
        }
        return true;
    }

    @Override
    public void onGetAddressSuccess(List<UserAddress> userAddressList) {
        if (userAddressList == null) {
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

    @Override
    public void onGetLocationSuccess(List<Location> mList, EditText editText, String title) {
        openDialogLocation(mList, editText, title);
    }

    @Override
    public void onResponseFail(Throwable t) {
        Log.d("===>", t.getMessage());
    }
}