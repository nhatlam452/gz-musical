package com.example.duantotnghiep.Contract;


import com.example.duantotnghiep.Model.Response.AddressResponse;
import com.example.duantotnghiep.Model.UserAddress;

import java.util.List;


public interface AddressContact {
    interface AddressModel {
        interface OnAddressFinished {
            void onAddressSuccess(AddressResponse addressResponse);

            void onAddressFailure(Throwable t);
        }

        void getUserAddress(OnAddressFinished onAddressFinished,String userId);
        void addAddress(OnAddressFinished onAddressFinished,UserAddress userAddress);
    }


    interface View {
        void onGetAddressSuccess(List<UserAddress> userAddressList);

        void onGetAddressFailure(String msg);

        void onGetAddressResponseFail(Throwable t);
    }

    interface Presenter {
        void onGetAddress(String userId);
        void onAddAddress(UserAddress address);
    }
}
