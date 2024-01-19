package com.example.travelblog_educative.repository;

import com.example.travelblog_educative.http.Blog;

import java.util.List;

public interface DataFromDatabaseCallback {
    void onSuccess(List<Blog> blogList);
}

