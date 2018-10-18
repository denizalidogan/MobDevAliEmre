package com.pxl.teamy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class NewPostActivity extends AppCompatActivity {


    private Toolbar toolbarNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        toolbarNewPost = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(toolbarNewPost);
        getSupportActionBar().setTitle("Add New Post");

    }
}
