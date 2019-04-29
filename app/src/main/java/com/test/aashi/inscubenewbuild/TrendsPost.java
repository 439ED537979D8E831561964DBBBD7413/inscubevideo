package com.test.aashi.inscubenewbuild;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import Adapter.PostCaptureMultiple;
import GetterSetter.Category_GridList;

public class TrendsPost extends AppCompatActivity {

    RecyclerView post_has_recycler_view;
    PostCaptureMultiple captureAdapter;
    ArrayList<Category_GridList> grid_element = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends_post);

        post_has_recycler_view = (RecyclerView) findViewById(R.id.post_has_recycler_view);


        post_has_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        grid_element.add(new Category_GridList("College", R.drawable.college));
        grid_element.add(new Category_GridList("Events", R.drawable.event));
        grid_element.add(new Category_GridList("Company", R.drawable.company));
        grid_element.add(new Category_GridList("Community/Organisation", R.drawable.community));
        grid_element.add(new Category_GridList("Entertainment", R.drawable.entertainment));
        grid_element.add(new Category_GridList("Family/Friends", R.drawable.family));
        grid_element.add(new Category_GridList("Personality", R.drawable.personality));
        grid_element.add(new Category_GridList("Movies", R.drawable.movies));
        grid_element.add(new Category_GridList("Product/Brand", R.drawable.product));

        grid_element.add(new Category_GridList("Music", R.drawable.music));
        grid_element.add(new Category_GridList("Art", R.drawable.art));
        grid_element.add(new Category_GridList("Sport/Fitness", R.drawable.sports));
        grid_element.add(new Category_GridList("Media", R.drawable.news));
        grid_element.add(new Category_GridList("Learning", R.drawable.learning));

        grid_element.add(new Category_GridList("Career", R.drawable.career));
        grid_element.add(new Category_GridList("Cause/Campaign", R.drawable.cause));

        grid_element.add(new Category_GridList("Food", R.drawable.food));
        grid_element.add(new Category_GridList("Pets", R.drawable.pets));
        grid_element.add(new Category_GridList("Travel", R.drawable.travel));
        grid_element.add(new Category_GridList("Entrepreneurship", R.drawable.entre));


        captureAdapter = new PostCaptureMultiple(TrendsPost.this, grid_element);

        post_has_recycler_view.setAdapter(captureAdapter);


    }

}
