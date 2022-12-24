package com.example.duantotnghiep.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.example.duantotnghiep.Utilities.LocalStorage;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity implements UserContract.View {
    private Button btnConfirmPassword;
    private UserPresenter userPresenter;
    private EditText edtPassword, edtNewPassword, edtConfirmNewPassword;
    private TextInputLayout tipOldPassword, tipNewPassword, tipConfirmNewPassword;
    private boolean isChangedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_change_password);
        initUI();
        //isChangedPassword or Forgot Password
        isChangedPassword = getIntent().getBooleanExtra("isChangePassword", false);
        if (isChangedPassword) {
            tipOldPassword.setVisibility(View.VISIBLE);
        }
        btnConfirmPassword.setOnClickListener(v -> {
            onChangePassword();
        });
    }

    private void onChangePassword() {
        // Get the values of the password fields
        String password = edtPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();

        // Check if the new password is valid
        boolean isValidNewPassword = AppUtil.ValidateInput.isValidPassword(newPassword);

        // Validate that the new password field is not empty
        if (newPassword.isEmpty()) {
            tipNewPassword.setError("Please enter your new Password");
            return;
        }

        // Validate that the confirm new password field matches the new password field
        if (!newPassword.equals(confirmNewPassword)) {
            tipConfirmNewPassword.setError("Your password does not matched");
            return;
        }

        // Validate that the new password is valid
        if (!isValidNewPassword) {
            tipNewPassword.setError("Invalid password");
            return;
        }

        // Clear any errors that may have been displayed for the new password and confirm new password fields
        tipNewPassword.setError(null);
        tipConfirmNewPassword.setError(null);

        // Get the phone number from the intent
        String phoneNumber = getIntent().getStringExtra("userPhone");

        // Check if the phone number was not found in the intent
        if (phoneNumber == null) {
            Toast.makeText(this, "Unknown  a Error", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the dialog
        AppUtil.showDialog.show(this);

        // If the password is not being changed, call the onChangePassword method with a null password
        if (!isChangedPassword) {
            userPresenter.onChangePassword(phoneNumber, newPassword, null);
        }
        // If the password is being changed, call the onChangePassword method with the current password
        else {
            userPresenter.onChangePassword(LocalStorage.getInstance(this).getLocalStorageManager().getUserInfo().getPhoneNumber(), newPassword, password);
        }

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
        i.putExtra("Notification", "Your password has been changed. Now you are ready for an enjoyable moment.");
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        AppUtil.showDialog.dismiss();
        AppUtil.onGetNotification(this, "Your password has been changed. Now you are ready for an enjoyable moment.");

    }

    @Override
    public void onFail(String msg) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponseFail(Throwable t) {
        AppUtil.showDialog.dismiss();
        Toast.makeText(this, "Unknown  Error", Toast.LENGTH_SHORT).show();
        Log.d("ChangePasswordActivity", t.getMessage());
    }
}