package com.example.duantotnghiep.Presenter;

import android.util.Log;
import android.widget.EditText;

import com.example.duantotnghiep.Contract.LocationContract;
import com.example.duantotnghiep.Model.Response.LocationResponse;
import com.example.duantotnghiep.Service.LocationService;

public class LocationPresenter implements LocationContract.Model.OnFinishedListener,LocationContract.Presenter {

    private LocationContract.View locationView;
    private LocationContract.Model locationModel;

    public LocationPresenter(LocationContract.View locationView) {
        this.locationView = locationView;
        locationModel = new LocationService();
    }

    @Override
    public void onFinished(LocationResponse locationResponse, EditText editText, String string) {
        locationView.onGetLocationSuccess(locationResponse.getResults(),editText,string);
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d("Location Presenter" ,t.getMessage());
    }

    @Override
    public void getCity(EditText editText,String title) {
        if (locationView == null){
            Log.d("LocationPresenter","View is null");
        }
        locationModel.getCityFromApi(this,editText,title);

    }

    @Override
    public void getDistrict(String code,EditText editText,String title) {
        if (locationView == null){
            Log.d("LocationPresenter","View is null");
        }
        locationModel.getDistrictFromApi(this,code,editText,title);

    }

    @Override
    public void getWard(String code,EditText editText,String title) {
        if (locationView == null){
            Log.d("LocationPresenter","View is null");
        }
        locationModel.getWardFromApi(this,code,editText,title);
    }
}
