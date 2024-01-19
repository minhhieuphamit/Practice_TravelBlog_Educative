package com.example.travelblog_educative.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.travelblog_educative.http.Blog;

@Database(entities = {Blog.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BlogDAO blogDao();
}
