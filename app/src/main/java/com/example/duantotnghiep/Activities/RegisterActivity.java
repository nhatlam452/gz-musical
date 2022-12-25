package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duantotnghiep.Adapter.SpinnerAdapter;
import com.example.duantotnghiep.Adapter.SpinnerLocationAdapter;
import com.example.duantotnghiep.Contract.LocationContract;
import com.example.duantotnghiep.Contract.VerifyOtpInterface;
import com.example.duantotnghiep.Model.Location;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.LocationPresenter;
import com.example.duantotnghiep.Presenter.VerifyOtpPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppConstants;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements LocationContract.View, VerifyOtpInterface {
    private LocationPresenter locationPresenter;
    private VerifyOtpPresenter verifyOtpPresenter;
    private ImageView imgBackRegister;
    private TextView tvCheckLength, tvCheckLowercase, tvCheckUppercase, tvCheckDigit, tvCheckSpecialCharacter;
    private Button btnRegister;
    private TextInputLayout tipPassword, tipPhoneNumberRegister, tipConfirmPassword, tipFirstName,
            tipLastName, tipAddress, tipSalutation, tipDate, tipMonth, tipCity, tipDistrict, tipWard;
    private LinearLayout layoutCheckPassword;
    private EditText edtPhoneNumberRegister, edtPasswordRegister, edtConfirmPassword,
            edtFirstName, edtLastName, edtAddress, edtSalutation, edtDate, edtMonth, edtCity,
            edtDistrict, edtWard;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_register);
        initUI();
        imgBackRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, SuccessActivity.class));
            finish();
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        edtSalutation.setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            mList.add("Mr.");
            mList.add("Mrs.");
            mList.add("My Friend ");
            mList.add("Sir ");
            openDialog(mList, edtSalutation, "Salutation");
        });

        edtDate.setOnClickListener(v -> {
            List<String> mList = new ArrayList<>();
            for (int i = 1; i < 31; i++) {
                mList.add(i + "");
            }
            openDialog(mList, edtDate, "Date");
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

            openDialog(mList, edtMonth, "Month");
        });
        edtCity.setOnClickListener(v -> {
            locationPresenter.getCity(edtCity, "YOUR CITY");
            edtDistrict.setText(null);
            edtWard.setText(null);
            mCodeDistrict = null;
        });
        edtDistrict.setOnClickListener(v -> {
            mCodeWard = null;
            locationPresenter.getDistrict(mCodeDistrict, edtDistrict, "YOUR DISTRICT");
            edtWard.setText(null);
        });
        findViewById(R.id.tvTOSRegister).setOnClickListener(v -> {
            Intent i = new Intent(this, WebViewActivity.class);
            i.putExtra("URL", "https://vietthuong.vn/dieu-khoan-su-dung-website");
            startActivity(i);
            overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        });
        edtWard.setOnClickListener(v -> locationPresenter.getWard(mCodeWard, edtWard, "YOUR WARD"));
        btnRegister.setOnClickListener(v -> {
            if (checkValidateInputRegister()) {
                String phoneNumber = edtPhoneNumberRegister.getText().toString().trim();
                String password = edtPasswordRegister.getText().toString().trim();
                String salutation = edtSalutation.getText().toString().trim();
                String firstName = edtFirstName.getText().toString().trim();
                String lastName = edtLastName.getText().toString().trim();
                String dob = getFormatDob();
                String address = edtAddress.getText().toString().trim();
                String city = edtCity.getText().toString().trim();
                String district = edtDistrict.getText().toString().trim();
                String ward = edtWard.getText().toString().trim();
                int isNoti;
                if (cbRegisterNotification.isChecked()) {
                    isNoti = 1;
                } else {
                    isNoti = 0;
                }
                AppUtil.showDialog.show(this);
                User user = new User(address, city, district, ward, "NULL", null, phoneNumber, null, null, null, firstName, lastName, password, dob, salutation, isNoti, 0,0);
                verifyOtpPresenter.sendOtp(this, user);
            }
        });

    }
    private String getFormatDob(){
        String dob = null;
        int selectedDay = Integer.parseInt(edtDate.getText().toString().trim());
        String selectedMonth = edtMonth.getText().toString().trim();
        if (selectedMonth.equalsIgnoreCase("January")) {
            dob = String.format("%02d-01", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("February")) {
            dob = String.format("%02d-02", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("March")) {
            dob = String.format("%02d-03", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("April")) {
            dob = String.format("%02d-04", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("May")) {
            dob = String.format("%02d-05", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("June")) {
            dob = String.format("%02d-06", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("July")) {
            dob = String.format("%02d-07", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("August")) {
            dob = String.format("%02d-08", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("September")) {
            dob = String.format("%02d-09", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("October")) {
            dob = String.format("%02d-10", selectedDay);
        }else if (selectedMonth.equalsIgnoreCase("November")) {
            dob = String.format("%02d-11", selectedDay);
        } else if (selectedMonth.equalsIgnoreCase("December")) {
            dob = String.format("%02d-12", selectedDay);
        }
        return dob;
    }
    private void validateInputField(EditText inputField, TextInputLayout inputLayout, String errorMessage) {
        String input = inputField.getText().toString().trim();
        if (input.isEmpty()) {
            inputLayout.setError(errorMessage);
        } else {
            inputLayout.setError(null);
        }
    }

    private boolean checkValidateInputRegister() {
        boolean isAllValidate = true;

        // Validate phone number
        validateInputField(edtPhoneNumberRegister, tipPhoneNumberRegister, "Phone number is empty");
        if (tipPhoneNumberRegister.getError() == null) {
            String phoneNumber = edtPhoneNumberRegister.getText().toString().trim();
            if (phoneNumber.length() < 10 || !AppUtil.ValidateInput.isValidPhoneNumber(phoneNumber)) {
                tipPhoneNumberRegister.setError("Invalid phone number");
                isAllValidate = false;
            }
        } else {
            isAllValidate = false;
        }
        validateInputField(edtPasswordRegister, tipPassword, "Your password is in valid");
        // Validate confirm password
        validateInputField(edtConfirmPassword, tipConfirmPassword, "Confirm password is empty");
        if (tipConfirmPassword.getError() == null) {
            String confirmPassword = edtConfirmPassword.getText().toString().trim();
            if (!confirmPassword.equals(edtPasswordRegister.getText().toString().trim())) {
                tipConfirmPassword.setError("Password does not match");
                isAllValidate = false;
            }
        } else {
            isAllValidate = false;
        }

// Validate salutation
        validateInputField(edtSalutation, tipSalutation, "Please choose your salutation");
        if (tipSalutation.getError() != null) {
            isAllValidate = false;
        }

// Validate first name
        validateInputField(edtFirstName, tipFirstName, "First name is empty");
        if (tipFirstName.getError() != null) {
            isAllValidate = false;
        }

// Validate last name
        validateInputField(edtLastName, tipLastName, "Last name is empty");
        if (tipLastName.getError() != null) {
            isAllValidate = false;
        }

// Validate date
        validateInputField(edtDate, tipDate, "Please choose your date");
        if (tipDate.getError() != null) {
            isAllValidate = false;
        }

// Validate month
        validateInputField(edtMonth, tipMonth, "Please choose your month");
        if (tipMonth.getError() != null) {
            isAllValidate = false;
        }
        int selectedDay = 0;
        String date = edtDate.getText().toString().trim();
        if (date.isEmpty()) {
            tipDate.setError("Please choose your Date");
            // Display error message for empty date
        } else {
            selectedDay = Integer.parseInt(date);
        }
        String selectedMonth = edtMonth.getText().toString().trim();

        if (selectedMonth.equalsIgnoreCase("February")) {
            if (selectedDay > 29) {
                tipDate.setError("Invalid day for February");
                isAllValidate = false;
            }
        } else if (selectedMonth.equalsIgnoreCase("April") || selectedMonth.equalsIgnoreCase("June") ||
                selectedMonth.equalsIgnoreCase("September") || selectedMonth.equalsIgnoreCase("November")) {
            if (selectedDay > 30) {
                tipDate.setError("Invalid day for this month");
                isAllValidate = false;
            }
        }
// Validate address
        validateInputField(edtAddress, tipAddress, "Address is empty");
        if (tipAddress.getError() != null) {
            isAllValidate = false;
        }


// Validate city
        validateInputField(edtCity, tipCity, "Please choose your city");
        if (tipCity.getError() != null) {
            isAllValidate = false;
        }

// Validate district
        validateInputField(edtDistrict, tipDistrict, "Please choose your district");
        if (tipDistrict.getError() != null) {
            isAllValidate = false;
        }

// Validate ward
        validateInputField(edtWard, tipWard, "Please choose your ward");
        if (tipWard.getError() != null) {
            isAllValidate = false;
        }

        return isAllValidate;
    }

    private void openDialog(List<String> mList, EditText editText, String title) {

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
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, mList, s -> {
            editText.setText(s);
            dialog.dismiss();
        });
        rcvSpinner.setLayoutManager(new LinearLayoutManager(this));
        rcvSpinner.setAdapter(spinnerAdapter);
        dialog.show();
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

    private void initUI() {
        verifyOtpPresenter = new VerifyOtpPresenter(this);
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
        tipPhoneNumberRegister = findViewById(R.id.tipPhoneNumberRegister);
        tipConfirmPassword = findViewById(R.id.tipConfirmPassword);
        tipSalutation = findViewById(R.id.tipSalutation);
        tipFirstName = findViewById(R.id.tipFirstName);
        tipLastName = findViewById(R.id.tipLastName);
        tipDate = findViewById(R.id.tipDate);
        tipMonth = findViewById(R.id.tipMonth);
        tipAddress = findViewById(R.id.tipAddress);
        tipCity = findViewById(R.id.tipCity);
        tipDistrict = findViewById(R.id.tipDistrict);
        tipWard = findViewById(R.id.tipWard);
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
                    tvCheckLength.setText(AppConstants.check);
                    tvCheckLength.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckLength.setText("x");
                    tvCheckLength.setTextColor(getResources().getColor(R.color.red));
                }

                if (isDigit) {
                    tvCheckDigit.setText(AppConstants.check);
                    tvCheckDigit.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckDigit.setText("x");
                    tvCheckDigit.setTextColor(getResources().getColor(R.color.red));
                }
                if (isLower) {
                    tvCheckLowercase.setText(AppConstants.check);
                    tvCheckLowercase.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckLowercase.setText("x");
                    tvCheckLowercase.setTextColor(getResources().getColor(R.color.red));
                }
                if (isUpper) {
                    tvCheckUppercase.setText(AppConstants.check);
                    tvCheckUppercase.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    tvCheckUppercase.setText("x");
                    tvCheckUppercase.setTextColor(getResources().getColor(R.color.red));
                }
                if (isSpecial) {
                    tvCheckSpecialCharacter.setText(AppConstants.check);
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
    public void onGetLocationSuccess(List<Location> mList, EditText editText, String title) {
        openDialogLocation(mList, editText, title);
    }

    @Override
    public void onResponseFail(Throwable t) {
        Log.d("===>", t.getMessage());
    }

    @Override
    public void onSendOtpSuccess(User user, String id) {
        Intent i = new Intent(this, OtpVerifyActivity.class);
        i.putExtra("UserRegister", user);
        i.putExtra("verificationId", id);
        startActivity(i);
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        AppUtil.showDialog.dismiss();


    }

    @Override
    public void onSendOtpFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        AppUtil.showDialog.dismiss();
    }
}