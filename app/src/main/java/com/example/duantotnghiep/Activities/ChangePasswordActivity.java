package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Presenter.UserPresenter;
import com.example.duantotnghiep.R;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity implements UserContract.View {
    private Button btnConfirmPassword;
    private UserPresenter userPresenter;
    private EditText edtPassword,edtNewPassword,edtConfirmNewPassword;
    private TextInputLayout tipOldPassword,tipNewPassword,tipConfirmNewPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUI();
        boolean isChangedPassword = getIntent().getBooleanExtra("isChangePassword",false);
        if (isChangedPassword){
            tipOldPassword.setVisibility(View.VISIBLE);
        }
        btnConfirmPassword.setOnClickListener(v->{
            String password = edtPassword.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();
            boolean isValidNewPassword = AppUtil.ValidateInput.isValidPassword(newPassword);
            boolean isValidOldPassword = AppUtil.ValidateInput.isValidPassword(password);
            if (newPassword.isEmpty()){
                tipNewPassword.setError("Please enter your new Password");
            }else {
                tipNewPassword.setError(null);
            }
            if (!newPassword.equals(confirmNewPassword)){
                tipConfirmNewPassword.setError("Your password does not matched");
            }else {
                tipConfirmNewPassword.setError(null);

            }
            if (isValidNewPassword){
                tipNewPassword.setError(null);
            }else {
                tipNewPassword.setError("Invalid password");
            }
            if (!isChangedPassword){
                if (!newPassword.isEmpty() && confirmNewPassword.equals(newPassword) && isValidNewPassword ){
                    String phoneNumber = getIntent().getStringExtra("userPhone");
                    if (phoneNumber == null){
                        Toast.makeText(this, "Unknown  a Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    userPresenter.onChangePassword(phoneNumber,newPassword,null);
                }
            }else {
                Toast.makeText(this, "Change Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        userPresenter = new UserPresenter(this);
        edtPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword);
        tipOldPassword = findViewById(R.id.tipOldPassword);
        tipNewPassword = findViewById(R.id.tipNewPassword);
        tipConfirmNewPassword = findViewById(R.id.tipConfirmNewPassword);
        btnConfirmPassword = findViewById(R.id.btnConfirmPassword);
    }

    @Override
    public void onSuccess(User user) {
        Intent i = new Intent(this, SuccessActivity.class);
        i.putExtra("Notification","Your password has been changed. Now you are ready for an enjoyable moment.");
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
    }

    @Override
    public void onFail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFail(Throwable t) {
        Toast.makeText(this, "Unknown  Error", Toast.LENGTH_SHORT).show();
        Log.d("ChangePasswordActivity",t.getMessage());
    }
}