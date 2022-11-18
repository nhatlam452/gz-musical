package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.Products;
import com.example.duantotnghiep.Model.Response.ProductListResponse;
import com.example.duantotnghiep.Model.Response.StoreResponse;
import com.example.duantotnghiep.Model.Store;

import java.util.List;

public interface StoreContact {
    interface StoreModel{
        interface  OnStoreListener{
            void  onStoreFinished(StoreResponse storeResponse);
            void  onStoreFailure(Throwable t);
        }
        void getAllProduct(OnStoreListener onStoreListener);
    }
    interface View{
        void setStore(List<Store> mListStore);
        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void getProduct();
    }
}
