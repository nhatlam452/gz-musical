package com.example.duantotnghiep.Presenter;

import android.util.Log;

import com.example.duantotnghiep.Contract.CommentContact;
import com.example.duantotnghiep.Model.Comment;
import com.example.duantotnghiep.Model.Response.CommentResponse;
import com.example.duantotnghiep.Service.CommentService;
import com.example.duantotnghiep.Service.UserService;

public class CommentPresenter implements CommentContact.Presenter,CommentContact.CommentModel.OnCommentFinished {
    private CommentContact.View mCommentView;
    private CommentContact.CommentModel mCommentModel;

    public CommentPresenter(CommentContact.View mCommentView) {
        this.mCommentView = mCommentView;
        mCommentModel = new CommentService();
    }

    @Override
    public void onCommentSuccess(CommentResponse commentResponse) {
        if (commentResponse.getResponseCode() == 1){
            mCommentView.onGetCmtSuccess(commentResponse.getData());
        }else {
            mCommentView.onGetCmtFailure(commentResponse.getMessage());
        }

    }

    @Override
    public void onCommentFailure(Throwable t) {
        mCommentView.onGetCmtResponseFail(t);
        Log.d("Comment Presenter ","Error : " + t.getMessage());
    }

    @Override
    public void onGetComment(int productId) {
        mCommentModel.getComment(this,productId);
    }

    @Override
    public void onAddComment(Comment comment) {
        mCommentModel.addComment(this,comment);
    }
}
