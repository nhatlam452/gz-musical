package com.example.duantotnghiep.Presenter;

import android.util.Log;

import com.example.duantotnghiep.Contract.StoreContact;
import com.example.duantotnghiep.Model.Response.StoreResponse;
import com.example.duantotnghiep.Service.StoreService;

public class StorePresenter implements StoreContact.Presenter, StoreContact.StoreModel.OnStoreListener {
    private final StoreContact.StoreModel storeModel;
    private final StoreContact.View view;

    public StorePresenter(StoreContact.View view) {
        this.storeModel = new StoreService();
        this.view = view;
    }

    @Override
    public void getProduct() {
        storeModel.getAllProduct(this);
    }

    @Override
    public void onStoreFinished(StoreResponse storeResponse) {
        if (storeResponse.getResponseCode() == 1){
            view.setStore(storeResponse.getData());
        }
        Log.d("Store Finished", "Response Code : " + storeResponse.getMessage() + "msg : " + storeResponse.getMessage());
    }

    @Override
    public void onStoreFailure(Throwable t) {
        view.onResponseFail(t);
    }
}
