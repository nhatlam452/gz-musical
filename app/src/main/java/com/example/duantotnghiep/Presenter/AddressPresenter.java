package com.example.duantotnghiep.Presenter;

import android.util.Log;

import com.example.duantotnghiep.Contract.AddressContact;
import com.example.duantotnghiep.Model.Response.AddressResponse;
import com.example.duantotnghiep.Model.UserAddress;
import com.example.duantotnghiep.Service.UserService;
import com.example.duantotnghiep.Utilities.AppUtil;
import com.example.duantotnghiep.Utilities.LocalStorage;

public class AddressPresenter implements AddressContact.AddressModel.OnAddressFinished, AddressContact.Presenter {
    private final String TAG = "ADDRESS PRESENTER";
    private final AddressContact.View mView;
    private final AddressContact.AddressModel model;

    public AddressPresenter(AddressContact.View mView) {
        this.mView = mView;
        this.model = new UserService();
    }

    @Override
    public void onGetAddress(String userId) {
        if (userId == null) {
            mView.onGetAddressFailure("Unable to get your Address .");
            return;
        }
        model.getUserAddress(this, userId);

    }

    @Override
    public void onAddAddress(UserAddress address) {
        if (address == null) {
            mView.onGetAddressFailure("Unable to add your Address .");
            return;
        }
        model.addAddress(this,address);
    }

    @Override
    public void onAddressSuccess(AddressResponse addressResponse) {
        Log.d(TAG, " code : " + addressResponse.getResponseCode() + " Msg : " + addressResponse.getMessage());
        if (addressResponse.getData() == null){
            mView.onGetAddressSuccess(null);
        }
        if (addressResponse.getResponseCode() == 1) {
            mView.onGetAddressSuccess(addressResponse.getData());
        } else {
            mView.onGetAddressFailure(addressResponse.getMessage());
        }

    }

    @Override
    public void onAddressFailure(Throwable t) {
        mView.onGetAddressResponseFail(t);
    }
}
