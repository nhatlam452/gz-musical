package com.example.duantotnghiep.Contract;

import android.widget.EditText;

import com.example.duantotnghiep.Model.Location;
import com.example.duantotnghiep.Model.Response.LocationResponse;

import java.util.List;

public interface LocationContract {
    interface Model{
        interface  OnFinishedListener{
            void  onFinished(LocationResponse locationResponse, EditText editText, String string);
            void  onFailure(Throwable t);
        }
        void getCityFromApi(OnFinishedListener onFinishedListener,EditText editText,String title);
        void getDistrictFromApi(OnFinishedListener onFinishedListener,String code,EditText editText,String title);
        void getWardFromApi(OnFinishedListener onFinishedListener,String code,EditText editText,String title);
    }
    interface View{
        void onGetLocationSuccess(List<Location> mList,EditText editText,String msg);
        void onResponseFail(Throwable t);
    }

    interface Presenter {
        void getCity(EditText editText,String title);
        void getDistrict(String id, EditText editText,String title);
        void getWard(String id,EditText editText,String title);

    }
}
