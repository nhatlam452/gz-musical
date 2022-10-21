package com.example.duantotnghiep.Contract;

import com.example.duantotnghiep.Model.News;

import java.util.List;

public interface NewsInterface {
    void onNewsSuccess(List<News> listNews);
    void onNewsFailure(Throwable t);
}
