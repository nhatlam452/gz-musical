package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.Comment;
import com.example.duantotnghiep.Model.Response.AddressResponse;
import com.example.duantotnghiep.Model.Response.CommentResponse;
import com.example.duantotnghiep.Model.UserAddress;

import java.util.List;

public interface CommentContact {
    interface CommentModel {
        interface OnCommentFinished {
            void onCommentSuccess(CommentResponse commentResponse);

            void onCommentFailure(Throwable t);
        }

        void getComment(OnCommentFinished onCommentFinished, int productId);
        void addComment(OnCommentFinished onCommentFinished, Comment cmt);
    }


    interface View {
        void onGetCmtSuccess(List<Comment> cmtList);

        void onGetCmtFailure(String msg);

        void onGetCmtResponseFail(Throwable t);
    }

    interface Presenter {
        void onGetComment(int productId);
        void onAddComment(Comment comment);
    }

}
