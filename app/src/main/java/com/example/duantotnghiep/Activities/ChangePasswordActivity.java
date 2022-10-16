package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.duantotnghiep.R;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {
    private Button btnConfirmPassword;
    private EditText edtPassword,edtNewPassword,edtConfirmNewPassword;
    private TextInputLayout tipOldPassword,tipNewPassword,tipConfirmNewPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUI();

    }

    private void initUI() {
        edtPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword);
        tipOldPassword = findViewById(R.id.tipOldPassword);
        tipNewPassword = findViewById(R.id.tipNewPassword);
        tipConfirmNewPassword = findViewById(R.id.tipConfirmNewPassword);
        btnConfirmPassword = findViewById(R.id.btnConfirmPassword);
    }
}