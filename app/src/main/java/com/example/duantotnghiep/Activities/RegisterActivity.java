package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.SpinnerAdapter;
import com.example.duantotnghiep.Adapter.SpinnerLocationAdapter;
import com.example.duantotnghiep.Contract.LocationContract;
import com.example.duantotnghiep.Model.Location;
import com.example.duantotnghiep.Presenter.LocationPresenter;
import com.example.duantotnghiep.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements LocationContract.View {
    private LocationPresenter locationPresenter;
    private ImageView imgBackRegister;
    private TextView tvCheckLength, tvCheckLowercase, tvCheckUppercase, tvCheckDigit, tvCheckSpecialCharacter;
    private Button btnRegister;
    private TextInputLayout tipPassword,tipPhoneNumberRegister,tipConfirmPassword,tipFirstName,tipLastName,tipAddress;
    private LinearLayout layoutCheckPassword;
    private Spinner spnSalutation;
    private EditText edtPhoneNumberRegister, edtPasswordRegister, edtConfirmPassword,
            edtFirstName, edtLastName, edtAddress,edtSalutation,edtDate,edtMonth,edtCity,
            edtDistrict,edtWard;
    private CheckBox cbRegisterNotification;
    private final Pattern specialChar = Pattern.compile("[!@#$%^&*_+.-]");
    private final Pattern digitChar = Pattern.compile("\\d");
    private final Pattern lower = Pattern.compile("[a-z]");
    private final Pattern upper = Pattern.compile("[A-Z]");
    private String mCodeDistrict;
    private String mCodeWard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        imgBackRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, RegisterSuccessActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        edtSalutation.setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            mList.add("Mr.");
            mList.add("Mrs.");
            mList.add("My Friend ");
            mList.add("Sir ");
            openDialog(mList,edtSalutation,"Salutation");
        });

        edtDate.setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            for (int i = 1; i < 31; i++) {
                mList.add(i+"");
            }
            openDialog(mList,edtDate,"Date");
        });
        edtMonth.setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            mList.add("January");
            mList.add("February");
            mList.add("March");
            mList.add("April");
            mList.add("May");
            mList.add("June");
            mList.add("July");
            mList.add("August");
            mList.add("September");
            mList.add("October");
            mList.add("November");
            mList.add("December");

            openDialog(mList,edtDate,"Month");
        });
        edtCity.setOnClickListener(v -> {
            locationPresenter.getCity(edtCity,"YOUR CITY");
            edtDistrict.setText(null);
            edtWard.setText(null);
            mCodeDistrict = null;
        });
        edtDistrict.setOnClickListener(v -> {
            mCodeWard = null;
            locationPresenter.getDistrict(mCodeDistrict,edtDistrict,"YOUR DISTRICT");
            edtWard.setText(null);
        });
        edtWard.setOnClickListener(v -> {
            locationPresenter.getWard(mCodeWard,edtWard,"YOUR WARD");
        });
    }

    private void openDialog(List<String> mList,EditText editText,String title) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_spinner);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        TextView tvTitle = dialog.findViewById(R.id.tvSpinnerTitle);
        RecyclerView rcvSpinner = dialog.findViewById(R.id.rcvSpinner);

        tvTitle.setText(title);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, mList, new SpinnerAdapter.OnClickSetText() {
            @Override
            public void onClickSetText(String s) {
                editText.setText(s);
                dialog.dismiss();
            }
        });
        rcvSpinner.setLayoutManager(new LinearLayoutManager(this));
        rcvSpinner.setAdapter(spinnerAdapter);
        dialog.show();
    }
    private void openDialogLocation(List<Location> mList,EditText editText,String title) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_spinner);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        TextView tvTitle = dialog.findViewById(R.id.tvSpinnerTitle);
        RecyclerView rcvSpinner = dialog.findViewById(R.id.rcvSpinner);

        tvTitle.setText(title);
        SpinnerLocationAdapter spinnerAdapter = new SpinnerLocationAdapter(this, mList, new SpinnerLocationAdapter.OnClickSetText() {
            @Override
            public void onClickSetText(String s,String code) {
                editText.setText(s);
                if (mCodeDistrict == null){
                    mCodeDistrict = code;
                }
                if (mCodeWard == null){
                    mCodeWard = code;
                }
                dialog.dismiss();
            }
        });
        rcvSpinner.setLayoutManager(new LinearLayoutManager(this));
        rcvSpinner.setAdapter(spinnerAdapter);
        dialog.show();
    }

    private void initUI() {
        locationPresenter = new LocationPresenter(this);
        imgBackRegister = findViewById(R.id.imgBackRegister);
        edtDate = findViewById(R.id.edtDate);
        edtDistrict = findViewById(R.id.edtDistrict);
        edtWard = findViewById(R.id.edtWard);
        edtMonth = findViewById(R.id.edtMonth);
        btnRegister = findViewById(R.id.btnRegister);
        edtPhoneNumberRegister = findViewById(R.id.edtPhoneNumberRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtAddress = findViewById(R.id.edtAddress);
        cbRegisterNotification = findViewById(R.id.cbRegisterNotification);
        layoutCheckPassword = findViewById(R.id.layoutCheckPassword);
        tipPassword = findViewById(R.id.tipPassword);
        tvCheckLength = findViewById(R.id.tvCheckLength);
        tvCheckLowercase = findViewById(R.id.tvCheckLowercase);
        edtCity = findViewById(R.id.edtCity);
        tvCheckUppercase = findViewById(R.id.tvCheckUppercase);
        edtSalutation = findViewById(R.id.edtSalutation);
        tvCheckDigit = findViewById(R.id.tvCheckDigit);
        tvCheckSpecialCharacter = findViewById(R.id.tvCheckSpecialCharacter);
        edtPasswordRegister.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                layoutCheckPassword.setVisibility(View.VISIBLE);
            } else {
                layoutCheckPassword.setVisibility(View.GONE);
            }
        });
        edtPasswordRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = edtPasswordRegister.getText().toString().trim();
                boolean isSpecial = specialChar.matcher(text).find();
                boolean isDigit = digitChar.matcher(s).find();
                boolean isLower = lower.matcher(s).find();
                boolean isUpper = upper.matcher(s).find();
                boolean isLength = (s.length() >= 8 && s.length() <= 15);
                if (text.isEmpty()) {
                    tvCheckLength.setText("x");
                    tvCheckLength.setTextColor(getResources().getColor(R.color.red));
                    tvCheckDigit.setText("x");
                    tvCheckDigit.setTextColor(getResources().getColor(R.color.red));
                    tvCheckLowercase.setText("x");
                    tvCheckLowercase.setTextColor(getResources().getColor(R.color.red));
                    tvCheckUppercase.setText("x");
                    tvCheckUppercase.setTextColor(getResources().getColor(R.color.red));
                    tvCheckSpecialCharacter.setText("x");
                    tvCheckSpecialCharacter.setTextColor(getResources().getColor(R.color.red));
                }
                if (isLength) {
                    tvCheckLength.setText(R.string.check);
                    tvCheckLength.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckLength.setText("x");
                    tvCheckLength.setTextColor(getResources().getColor(R.color.red));
                }

                if (isDigit) {
                    tvCheckDigit.setText(R.string.check);
                    tvCheckDigit.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckDigit.setText("x");
                    tvCheckDigit.setTextColor(getResources().getColor(R.color.red));
                }
                if (isLower) {
                    tvCheckLowercase.setText(R.string.check);
                    tvCheckLowercase.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckLowercase.setText("x");
                    tvCheckLowercase.setTextColor(getResources().getColor(R.color.red));
                }
                if (isUpper) {
                    tvCheckUppercase.setText(R.string.check);
                    tvCheckUppercase.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckUppercase.setText("x");
                    tvCheckUppercase.setTextColor(getResources().getColor(R.color.red));
                }
                if (isSpecial) {
                    tvCheckSpecialCharacter.setText(R.string.check);
                    tvCheckSpecialCharacter.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckSpecialCharacter.setText("x");
                    tvCheckSpecialCharacter.setTextColor(getResources().getColor(R.color.red));
                }

                if (isLength && isLower && isUpper && isDigit && isSpecial) {
                    tipPassword.setBoxStrokeColor(getResources().getColor(R.color.colorPrimary));
                    tipPassword.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                } else {
                    tipPassword.setBoxStrokeColor(getResources().getColor(R.color.red));
                    tipPassword.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);

    }

    @Override
    public void onGetLocationSuccess(List<Location> mList,EditText editText,String title) {
        openDialogLocation(mList,editText,title);
    }

    @Override
    public void onResponseFail(Throwable t) {
        Log.d("===>",t.getMessage());
    }
}