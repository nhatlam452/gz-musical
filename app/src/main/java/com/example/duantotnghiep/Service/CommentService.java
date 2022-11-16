package com.example.duantotnghiep.Service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.duantotnghiep.Contract.CommentContact;
import com.example.duantotnghiep.Model.Comment;
import com.example.duantotnghiep.Model.Response.CommentResponse;
import com.example.duantotnghiep.Retrofit.ApiClient;
import com.example.duantotnghiep.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentService implements CommentContact.CommentModel {
    private final String TAG = "COMMENT_SERVICE";
    private final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void getComment(OnCommentFinished onCommentFinished, int productId) {
        Call<CommentResponse> call = apiInterface.get_all_comment(productId);
        onCartCallback(call,onCommentFinished);
    }

    @Override
    public void addComment(OnCommentFinished onCommentFinished, Comment cmt) {
        Call<CommentResponse> call = apiInterface.add_comment(cmt);
        onCartCallback(call,onCommentFinished);
    }
    private void onCartCallback (Call<CommentResponse> call, OnCommentFinished onCommentFinished){
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommentResponse> call, @NonNull Response<CommentResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Log.d(TAG, "Response code : " + response.code() + "----" + response.body().getMessage());
                    onCommentFinished.onCommentSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommentResponse> call, @NonNull Throwable t) {
                onCommentFinished.onCommentFailure(t);
                Log.d(TAG, "Error : " + t.getMessage());
            }
        });
    }
}
