package com.example.duantotnghiep.Presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;


import com.example.duantotnghiep.Contract.UserContract;
import com.example.duantotnghiep.Model.Response.AddressResponse;
import com.example.duantotnghiep.Model.Response.UserResponse;
import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.Service.UserService;
import com.example.duantotnghiep.Utilities.AppUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class UserPresenter implements UserContract.Presenter, UserContract.Model.OnFinishedListener {

    private final String TAG = "USER PRESENTER";
    private final UserContract.View mView;
    private final UserContract.Model model;



    public UserPresenter(UserContract.View mView, UserContract.Model model) {
        this.mView = mView;
        this.model = model;
    }

    public UserPresenter(UserContract.View mView) {
        this.mView = mView;
        model = new UserService();
    }

    @Override
    public void onLogin(String phone, String password) {
        if (phone.isEmpty() || password.isEmpty()) {
            mView.onFail("Phone Number or Password is Empty");
            return;
        } else if (!AppUtil.ValidateInput.isValidPhoneNumber(phone) || !AppUtil.ValidateInput.isValidPassword(password)) {
            mView.onFail("Invalid phone number or password");
            return;
        }
        model.getLogin(this, phone, password);
    }


    @Override
    public void onRegister(User user, String otp, String id, Activity activity) {
        if (user.getPhoneNumber() == null || user.getPhoneNumber().length() < 10 || !AppUtil.ValidateInput.isValidPhoneNumber(user.getPhoneNumber())) {
            mView.onFail("Invalid phone number");
            return;
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            mView.onFail("Your password is in valid");
            return;
        }
        if (user.getSalutation() == null ) {
            mView.onFail("Please choose your salutation");
            return;
        }
        if (user.getFirstName() == null ) {
            mView.onFail("First name is empty");
            return;
        }
        if (user.getLastName() == null ) {
            mView.onFail("Last name is empty");
            return;
        }
        if (user.getDob() == null ) {
            mView.onFail("Please choose your date of birth");
            return;
        }
        if (user.getAddress() == null ) {
            mView.onFail("Please choose your Address");
            return;
        }
        if (user.getCity() == null ) {
            mView.onFail("Please choose your City");
            return;
        }
        if (user.getDistrict() == null ) {
            mView.onFail("Please choose your District");
            return;
        }

        if (user.getWard() == null ) {
            mView.onFail("Please choose your Ward");
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, otp);
        signInWithPhoneAuthCredential(credential, user, this, activity);
    }

        public void signInWithPhoneAuthCredential(PhoneAuthCredential credential, User user, UserContract.Model.OnFinishedListener onFinishedListener, Activity activity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        model.getRegister(onFinishedListener, user);
                    } else {
                        mView.onFail("\"Incorrect OTP\"");
                        // Sign in failed, display a message and update the UI
                        Log.w("========>", "signInWithCredential:failure", task.getException());
                    }
                });
    }

    @Override
    public void onSocialRegister(User user, Activity activity) {
        if (user == null) {
            mView.onFail("Please try again");
            return;
        }

        model.getSocialRegister(this, user);
    }

    @Override
    public void onCheckExits(String phone) {
        if (phone == null) {
            mView.onFail("Please check your input");
            return;
        }
        model.checkExitsUser(this, phone);
    }

    @Override
    public void onUpdateInfo(String avt, String firstName, String lastName, String salutations, String userId) {

        model.updateUser(this, avt, firstName, lastName, salutations, userId);
    }


    @Override
    public void onChangePassword(String phoneNumber, String newPassword, String oldPassword) {
        if (phoneNumber == null || newPassword == null) {
            mView.onFail("Please check your input");
        }
        model.changePassword(this, phoneNumber, newPassword, oldPassword);
    }

    @Override
    public void onUpdateNotification(int notification, String userId) {
        model.updateNotification(this, notification, userId);
    }

    @Override
    public void onFinished(UserResponse userResponse) {
        if (userResponse.getResponseCode() == 1) {
            {
                if (userResponse.getData() != null) {
                    mView.onSuccess(userResponse.getData().get(0));
                } else {
                    mView.onSuccess(null);
                }
            }
            Log.d(TAG, userResponse.getMessage() + "");
        } else {
            mView.onFail(userResponse.getMessage());
            Log.d(TAG, " code : " + userResponse.getResponseCode() + " Msg : " + userResponse.getMessage());
        }

    }

    @Override
    public void onFailure(Throwable t) {
        mView.onResponseFail(t);
    }


}
