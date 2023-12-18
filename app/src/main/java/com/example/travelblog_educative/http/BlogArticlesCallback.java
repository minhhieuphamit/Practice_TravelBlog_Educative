package com.example.travelblog_educative.http;

import java.util.List;

public interface BlogArticlesCallback {
    void onSuccess(List<Blog> blogList);

    void onError();
}
