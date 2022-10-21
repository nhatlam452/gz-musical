package com.example.duantotnghiep.Presenter;

import com.example.duantotnghiep.Contract.NewsInterface;
import com.example.duantotnghiep.Model.Response.NewsResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsPresenter {
    private final String TAG = "News Presenter :";

    private final NewsInterface newsInterface;

    public NewsPresenter(NewsInterface newsInterface) {
        this.newsInterface = newsInterface;
    }
    public void getAllNews(){
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsResponse> call = apiInterface.get_all_news();
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getResponseCode() == 1){
                        newsInterface.onNewsSuccess(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                newsInterface.onNewsFailure(t);
            }
        });
    }
}
