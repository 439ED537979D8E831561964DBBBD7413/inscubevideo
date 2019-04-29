package com.test.aashi.inscubenewbuild;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import Fragment.MultipleFragment;
import Fragment.MultipleFragmentCapture;
import Fragment.SingleFragment;

public class CubeCapturePost2 extends AppCompatActivity {
  TabLayout layout1;
    LinearLayout multiple_cube, single_cube,hideview;
    TextView multiple_cubetxt, single_cubetxt;
    FrameLayout viewPager;
    FragmentTransaction ft;
    FragmentManager fm = getSupportFragmentManager();
    ImageView post_back;
   VideoView videoView;
     Uri file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_capture_post2);
        videoView=findViewById(R.id.myvideo);
        Intent i = getIntent();
        file = Uri.parse(i.getExtras().get("data").toString());
        videoView.setVideoURI(file);
        layout1=(TabLayout) findViewById(R.id.tab_layout);
        layout1.setVisibility(View.VISIBLE);
        single_cubetxt = (TextView) findViewById(R.id.single_cubetxt);
        multiple_cubetxt = (TextView) findViewById(R.id.multiple_cubetxt);
        hideview=(LinearLayout)findViewById(R.id.hide_view);
        hideview.setVisibility(View.GONE);
        single_cube = (LinearLayout) findViewById(R.id.single_cube);
        multiple_cube = (LinearLayout) findViewById(R.id.multiple_cube);
        viewPager = (FrameLayout) findViewById(R.id.post_screen);
        post_back = (ImageView) findViewById(R.id.post_back);
        multiple_cube.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        multiple_cubetxt.setTextColor(getResources().getColor(R.color.White));
        multiple_cubetxt.setTypeface(null, Typeface.BOLD);
        single_cubetxt.setTextColor(getResources().getColor(R.color.Storytxt));
        single_cubetxt.setTypeface(null, Typeface.NORMAL);
        single_cube.setBackgroundColor(0);

        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        ft.replace(R.id.post_screen, new MultipleFragmentCapture(), null);
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
                Bundle bundle=new Bundle();
                MultipleFragmentCapture my=new MultipleFragmentCapture();
                Uri vidFile = file;

                bundle.putString("hello", vidFile.getPath());
                my.setArguments(bundle);
                multiple_cube.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                multiple_cubetxt.setTextColor(getResources().getColor(R.color.White));
                multiple_cubetxt.setTypeface(null, Typeface.BOLD);
                single_cubetxt.setTextColor(getResources().getColor(R.color.Storytxt));
                single_cubetxt.setTypeface(null, Typeface.NORMAL);
                single_cube.setBackgroundColor(0);
                ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.post_screen, my, null);
                ft.commit();


            }
        });


    }

}
