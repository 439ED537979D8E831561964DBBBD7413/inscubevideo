package com.test.aashi.inscubenewbuild;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.TabAdapter;
import Fragment.CubeCaptureFragment2;
import Fragment.HighlightsFragment;
import Fragment.TrendsFragment2;

public class StoryPage extends AppCompatActivity {

    ImageView seachLL;
    TabLayout tab_layout;
    ViewPager viewPager;
    TabAdapter tabAdapter;
    TabItem tabItem1;
    TabItem tabItem2;
    TabItem tabItem3;
    ImageView notification;
    LinearLayout profile_settings, pro_settings_popup, popupview, rocket;
    LayoutInflater layoutInflater;
//    ProgressBar progressBar;
private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_page);

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        tabItem1 = (TabItem) findViewById(R.id.high);
        tabItem2 = (TabItem) findViewById(R.id.capture);
        tabItem3 = (TabItem) findViewById(R.id.trend);
        int fragmentId = getIntent().getIntExtra("FRAGMENT_ID", 0);
        if (fragmentId == 0 ) {
            viewPager.setCurrentItem(1);
        }





       seachLL = (ImageView) findViewById(R.id.search_1);

        seachLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(StoryPage.this, SearchActivity.class));
            }
        });
        notification = (ImageView) findViewById(R.id.notification_icon);
        notification.setVisibility(View.VISIBLE);
        rocket = (LinearLayout) findViewById(R.id.rocket);
        profile_settings = (LinearLayout) findViewById(R.id.profile_settings);
        popupview = (LinearLayout) findViewById(R.id.main);
        pro_settings_popup=findViewById(R.id.linearLayout);
        pro_settings_popup.setVisibility(View.VISIBLE);
       tabAdapter = new TabAdapter(getSupportFragmentManager(), tab_layout.getTabCount());
        viewPager.setAdapter(tabAdapter);

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(StoryPage.this, Notification.class);
                startActivity(in);

            }

        });


        rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(StoryPage.this, DiscoverCube.class);
                in.putExtra("feeds","1");
                startActivity(in);
            }

        });

        profile_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Point p;
//                int[] location = new int[2];
//
//                profile_settings.getLocationOnScreen(location);
//
//                p = new Point();
//                p.x = location[0];
//                p.y = location[1];
//
//                View layout = layoutInflater.inflate(R.layout.settings_popup, popupview);
//
//                // Creating the PopupWindow
//                final PopupWindow popup = new PopupWindow(StoryPage.this);
//                popup.setContentView(layout);
//                popup.setFocusable(true);
//                popup.setWidth(FrameLayout.LayoutParams.WRAP_CONTENT);
//                popup.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
//                popup.setAnimationStyle(R.style.popup_window_animation_phone);
//                // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
//                int OFFSET_X = -10;
//                int OFFSET_Y = 25;
//
//                // Displaying the popup at the specified location, + offsets.
//                popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
//
//
//                pro_settings_popup = (LinearLayout) layout.findViewById(R.id.pro_settings_popup);
//
//                pro_settings_popup.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {

                Intent in = new Intent(StoryPage.this, ProfileView.class);
                startActivity(in);
                //  popup.dismiss();

                //      }
                //  });
            }
        });
        new Highlight_Post().execute();
    }

    private class Highlight_Post extends AsyncTask<Integer, Integer, String> {
        private String result;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            final String[] responseString = {null};

            RequestQueue queue = Volley.newRequestQueue(StoryPage.this);

            String url = Config.BASE_URL + "Signin_Signup/User_App_Entry";
            Log.i("Test", "doInBackground:fb " + url);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Test", "onResponse:fb " + response);

                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {

//                    Toast.makeText(getContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("Test", "onErrorResponse: " + error.getMessage());
                }
            })

            {
                protected Map<String, String> getParams() {

                    SharedPreferences login = StoryPage.this.getSharedPreferences("Logged_in_details", MODE_PRIVATE);
                    String DeviceToken;
                    SharedPreferences sharedPreferences = getSharedPreferences("firebase", MODE_PRIVATE);
                    DeviceToken = sharedPreferences.getString("refreshToken", "");

                    if (DeviceToken.equalsIgnoreCase("")) {
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("refreshToken", FirebaseInstanceId.getInstance().getToken());
                        edit.commit();
                        DeviceToken = FirebaseInstanceId.getInstance().getToken();
                    }

                    Log.i("Test", "getParams:fb " + DeviceToken);
                    Log.i("Test", "getParams:fb " + login.getString("_id", ""));
                    Map<String, String> para = new HashMap<String, String>();
                    para.put("User_Id", login.getString("_id", ""));
                    para.put("Firebase_Token", DeviceToken);
                    return para;
                }
            };
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new

                    DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);


            return result;

        }
    }

    }

