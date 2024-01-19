package com.example.travelblog_educative.repository;

import com.example.travelblog_educative.http.Blog;

import java.util.List;

public interface DataFromNetworkCallback {
    void onSuccess(List<Blog> blogList);

    void onError();
}