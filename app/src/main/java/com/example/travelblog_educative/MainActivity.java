package com.example.travelblog_educative;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.travelblog_educative.adapter.MainAdapter;
import com.example.travelblog_educative.http.Blog;
import com.example.travelblog_educative.http.BlogArticlesCallback;
import com.example.travelblog_educative.http.BlogHttpClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int SORT_TITLE = 0;
    private static final int SORT_DATE = 1;

    private int currentSort = SORT_DATE;

    private MainAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort) {
                onSortClicked();
            }
            return false;
        });

        adapter = new MainAdapter(blog -> BlogDetailsActivity.startBlogDetailsActivity(this, blog));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this::loadData);

        loadData();
    }

    private void onSortClicked() {
        String[] items = {"Title", "Date"};
        new MaterialAlertDialogBuilder(this).setTitle("Sort order").setSingleChoiceItems(items, currentSort, (dialog, which) -> {
            dialog.dismiss();
            currentSort = which;
            sortData();
        }).show();
    }

    private void sortData() {
        if (currentSort == SORT_TITLE) {
            adapter.sortByTitle();
        } else if (currentSort == SORT_DATE) {
            adapter.sortByDate();
        }
    }

    private void loadData() {
        refreshLayout.setRefreshing(true);
        BlogHttpClient.INSTANCE.loadBlogArticles(new BlogArticlesCallback() {
            @Override
            public void onSuccess(List<Blog> blogList) {
                runOnUiThread(() -> {
                    refreshLayout.setRefreshing(false);
                    adapter.submitList(blogList);
                    sortData();
                });
            }

            @Override
            public void onError() {
                runOnUiThread(() -> {
                    refreshLayout.setRefreshing(false);
                    showErrorSnackbar();
                });
            }
        });
    }

    private void showErrorSnackbar() {
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, "Error during loading blog articles", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.orange500));
        snackbar.setAction("Retry", v -> {
            loadData();
            snackbar.dismiss();
        });
        snackbar.show();
    }
}