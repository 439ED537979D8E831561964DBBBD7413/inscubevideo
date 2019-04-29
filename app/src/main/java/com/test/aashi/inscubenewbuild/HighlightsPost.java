package com.test.aashi.inscubenewbuild;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Fragment.MultipleFragment;
import Fragment.SingleFragment;

public class HighlightsPost extends AppCompatActivity {

    LinearLayout multiple_cube, single_cube,hide_view;
    TextView multiple_cubetxt, single_cubetxt;
    FrameLayout viewPager;
    FragmentTransaction ft;
    FragmentManager fm = getSupportFragmentManager();
    ImageView post_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights_post);


        single_cubetxt = (TextView) findViewById(R.id.single_cubetxt);
        multiple_cubetxt = (TextView) findViewById(R.id.multiple_cubetxt);

        single_cube = (LinearLayout) findViewById(R.id.single_cube);
        multiple_cube = (LinearLayout) findViewById(R.id.multiple_cube);
        hide_view =(LinearLayout)findViewById(R.id.hide_view);
        viewPager = (FrameLayout) findViewById(R.id.post_screen);
        hide_view.setVisibility(View.GONE);
        post_back = (ImageView) findViewById(R.id.post_back);

//        single_cube.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//        single_cubetxt.setTextColor(getResources().getColor(R.color.White));
//        single_cubetxt.setTypeface(null, Typeface.BOLD);
//
//        multiple_cubetxt.setTextColor(getResources().getColor(R.color.Storytxt));
//        multiple_cubetxt.setTypeface(null, Typeface.NORMAL);
//        multiple_cube.setBackgroundColor(0);
//
//        ft = fm.beginTransaction();
//        ft.add(R.id.post_screen, new SingleFragment(), null);
//        ft.commit();


        multiple_cube.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        multiple_cubetxt.setTextColor(getResources().getColor(R.color.White));
        multiple_cubetxt.setTypeface(null, Typeface.BOLD);

        single_cubetxt.setTextColor(getResources().getColor(R.color.Storytxt));
        single_cubetxt.setTypeface(null, Typeface.NORMAL);
        single_cube.setBackgroundColor(0);

        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        ft.replace(R.id.post_screen, new MultipleFragment(), null);
        ft.commit();


        post_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        single_cube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                single_cube.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                single_cubetxt.setTextColor(getResources().getColor(R.color.White));
                single_cubetxt.setTypeface(null, Typeface.BOLD);

                multiple_cubetxt.setTextColor(getResources().getColor(R.color.Storytxt));
                multiple_cubetxt.setTypeface(null, Typeface.NORMAL);
                multiple_cube.setBackgroundColor(0);

                ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                ft.replace(R.id.post_screen, new SingleFragment(), null);
                ft.commit();


            }
        });

        multiple_cube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                multiple_cube.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                multiple_cubetxt.setTextColor(getResources().getColor(R.color.White));
                multiple_cubetxt.setTypeface(null, Typeface.BOLD);

                single_cubetxt.setTextColor(getResources().getColor(R.color.Storytxt));
                single_cubetxt.setTypeface(null, Typeface.NORMAL);
                single_cube.setBackgroundColor(0);

                ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.post_screen, new MultipleFragment(), null);
                ft.commit();


            }
        });


    }

}
