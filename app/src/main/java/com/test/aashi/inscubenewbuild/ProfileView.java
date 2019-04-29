package com.test.aashi.inscubenewbuild;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.CreateCubeCategoryAdapter;
import Adapter.FeedAdapterSmallIcon;
import Fragment.MultipleFragment;
import Fragment.MultipleFragmentPost;
import GetterSetter.Category_GridList;
import GetterSetter.CreatedCubeList;
import GetterSetter.CubeCategoeyList;

public class ProfileView extends AppCompatActivity
{
    RecyclerView my_cubes, following_cubes;
    ArrayList<Category_GridList> grid_element = new ArrayList<>();
    Cube adapter;
    Cube1 adapter1;
    SharedPreferences login;
    PopupWindow privacy_set_win, chng_pass_win;
    Button privacy_set, accny_settings, logout_button;
    String login_user_id, UserName, UserEmail, UserImage, UserId, ColorCode, DOB, City, Country, State, Gender,
            Cat_Id, Cube_Id, Cube_Name, Cube_Type, Cube_Member, Cube_Image, privacy = "", Hash_Tag_1 = "", Hash_Tag_2 = "",
            Hash_Tag_3 = "", Position = "";
    LinearLayout relative_layout, main1;
    ImageView profile_view_back;
    CircularImageView main_profile_img;
    TextView Pro_Name, no_cubes, no_cubes1, update_profile;
    List<CubeCategoeyList> cube_list_data, cube_list_data1;
    List<CreatedCubeList> created_list = new ArrayList<>();
    int selectedID;
    RadioGroup rg;
    RadioButton rb;
    List<CubeCategoeyList> following_list = new ArrayList<>();
    SharedPreferences.Editor edit;
  TextView viewall;
  TextView viewall1;
  PopupWindow emotePopupWindoww;
  GridView emote_allgrid;
  DiscoverMyCubeAdapters adapters;
  List<CubeCategoeyList> grid_elements, grid_element1;
  DiscoverMyCubeAdapter adapters1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        edit = getSharedPreferences("Logged_in_details", MODE_PRIVATE).edit();
        login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
       viewall=(TextView)findViewById(R.id.viewalls);
       viewall1=(TextView)findViewById(R.id.viewalla);
        my_cubes = (RecyclerView) findViewById(R.id.my_cubes);
        following_cubes = (RecyclerView) findViewById(R.id.following_cubes);
        privacy_set = (Button) findViewById(R.id.privacy_set);
        accny_settings = (Button) findViewById(R.id.accny_settings);
        logout_button = (Button) findViewById(R.id.logout_button);
        relative_layout = (LinearLayout) findViewById(R.id.main1);
        main1 = (LinearLayout) findViewById(R.id.p_info);
        Pro_Name = (TextView) findViewById(R.id.Profile_name);
        no_cubes = (TextView) findViewById(R.id.no_cubes);
        no_cubes1 = (TextView) findViewById(R.id.no_cubes1);
        update_profile = (TextView) findViewById(R.id.update_profile);

        profile_view_back = (ImageView) findViewById(R.id.profile_view_back);

        main_profile_img = (CircularImageView) findViewById(R.id.main_profile_img);


        my_cubes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        following_cubes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ProfileView.this, ProfileCompletion.class);
                intent.putExtra("UserName", UserName);
                intent.putExtra("UserImage", UserImage);
                intent.putExtra("ColorCode", ColorCode);
                intent.putExtra("DOB", DOB);
                intent.putExtra("City", City);
                intent.putExtra("State", State);
                intent.putExtra("Country", Country);
                intent.putExtra("Gender", Gender);
                intent.putExtra("Hash_Tag_1", Hash_Tag_1);
                intent.putExtra("Hash_Tag_2", Hash_Tag_2);
                intent.putExtra("Hash_Tag_3", Hash_Tag_3);
                intent.putExtra("update", "1");
                startActivity(intent);
                finish();

            }
        });

        privacy_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Switch shw_mail;
                Button privacy_sub;
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View custom = inflater.inflate(R.layout.change_privacy_design, null);

                privacy_set_win = new PopupWindow(
                        custom,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );

                privacy_sub = (Button) custom.findViewById(R.id.privacy_sub);
                final RadioButton rb1, rb2, rb3;
                rb1 = (RadioButton) custom.findViewById(R.id.rb1);
                rb2 = (RadioButton) custom.findViewById(R.id.rb2);
                rb3 = (RadioButton) custom.findViewById(R.id.rb3);


                RequestQueue queue = Volley.newRequestQueue(ProfileView.this);

                final String url = Config.BASE_URL + "Signin_Signup/User_Info/" + login_user_id;

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    if (response.getString("Output").contains("True")) {

                                        JSONObject data = response.getJSONObject("Response");


                                        privacy = data.getString("Show_Profile_To");

                                        if (privacy.contains("Everyone")) {

                                            rb1.setChecked(true);

                                        } else if (privacy.contains("Nobody")) {

                                            rb3.setChecked(true);

                                        } else if (privacy.contains("Cube members")) {

                                            rb2.setChecked(true);

                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                queue.add(getRequest);

                privacy_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rg = (RadioGroup) custom.findViewById(R.id.settings_radiogrp);
                        selectedID = rg.getCheckedRadioButtonId();
                        rb = (RadioButton) custom.findViewById(selectedID);

                        //  Toast.makeText(ProfileView.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();

                        RequestQueue MyRequestQueue = Volley.newRequestQueue(ProfileView.this);

                        String u = Config.BASE_URL + "Signin_Signup/Privacy_Update";
                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.contains("False")) {


                                } else {

                                    try {

                                        JSONObject res = new JSONObject(response);

                                        if (res.getString("Output").contains("True")) {

                                            Toast.makeText(ProfileView.this, "Updated Successfully ", Toast.LENGTH_SHORT).show();

                                            privacy_set_win.dismiss();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(ProfileView.this, " " + error, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> para = new HashMap<String, String>();

                                para.put("User_Id", login_user_id);
                                para.put("Show_Profile_To", rb.getText().toString());


                                return para;
                            }
                        };

                        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                        MyRequestQueue.add(MyStringRequest);


                    }
                });
                privacy_set_win.setFocusable(true);
                privacy_set_win.setAnimationStyle(R.style.popup_window_animation_phone);

                privacy_set_win.showAtLocation(relative_layout, Gravity.CENTER, 0, 0);

                dimBehind(privacy_set_win);

            }


        });

        logout_button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();


                SharedPreferences.Editor loggedin = getSharedPreferences("login_session", MODE_PRIVATE).edit();
                loggedin.putString("val", "0");
                loggedin.apply();


                Intent intent = new Intent(ProfileView.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


        accny_settings.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Button chng_pass_button;

                final EditText old_pass_txt, new_pass_txt;

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View customView = inflater.inflate(R.layout.change_pass_design, null);

                chng_pass_win = new PopupWindow(
                        customView,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );

                old_pass_txt = (EditText) customView.findViewById(R.id.old_pass);
                new_pass_txt = (EditText) customView.findViewById(R.id.new_pass);

                chng_pass_button = (Button) customView.findViewById(R.id.change_pass_button);

                chng_pass_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RequestQueue MyRequestQueue = Volley.newRequestQueue(ProfileView.this);

                        String u = Config.BASE_URL + "Signin_Signup/Password_Change";
                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject res = new JSONObject(response);

                                    if (res.getString("Output").contains("True")) {

                                        Toast.makeText(ProfileView.this, " " + res.getString("Message"), Toast.LENGTH_SHORT).show();

                                        chng_pass_win.dismiss();

                                    } else {

                                        Toast.makeText(ProfileView.this, " " + res.getString("Message"), Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(ProfileView.this, " " + error, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> para = new HashMap<String, String>();

                                para.put("User_Id", login_user_id);
                                para.put("Old_Password", old_pass_txt.getText().toString());
                                para.put("New_Password", new_pass_txt.getText().toString());

                                return para;
                            }
                        };

                        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                        MyRequestQueue.add(MyStringRequest);


                    }
                });

                chng_pass_win.setFocusable(true);
                chng_pass_win.setAnimationStyle(R.style.popup_window_animation_phone);

                chng_pass_win.showAtLocation(relative_layout, Gravity.CENTER, 0, 0);

                dimBehind(chng_pass_win);

            }
        });

        profile_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View customView = inflater.inflate(R.layout.viewallmembers, null);

                emotePopupWindoww = new PopupWindow(
                        customView,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                emotePopupWindoww.setFocusable(true);
                emotePopupWindoww.setAnimationStyle(R.style.popup_window_animation_phone);
                emotePopupWindoww.showAtLocation(customView, Gravity.CENTER, 0, 0);

                dimBehind(emotePopupWindoww);

                emote_allgrid = (GridView) customView.findViewById(R.id.viewallmembers);
                adapters1 =new DiscoverMyCubeAdapter(cube_list_data1,ProfileView.this);
                emote_allgrid.setAdapter(adapters1);
            }
        });
        viewall1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View customView = inflater.inflate(R.layout.viewallmembers, null);

                emotePopupWindoww = new PopupWindow(
                        customView,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                emotePopupWindoww.setFocusable(true);
                emotePopupWindoww.setAnimationStyle(R.style.popup_window_animation_phone);
                emotePopupWindoww.showAtLocation(customView, Gravity.CENTER, 0, 0);

                dimBehind(emotePopupWindoww);

                emote_allgrid = (GridView) customView.findViewById(R.id.viewallmembers);
                adapters =new DiscoverMyCubeAdapters(cube_list_data,ProfileView.this);
                emote_allgrid.setAdapter(adapters);
            }
        });
    }
    public class DiscoverMyCubeAdapter extends BaseAdapter {


        class ViewHolder {
            PorterShapeImageView img;
            TextView name;
            ImageView imag, back;
            LinearLayout cube_category;
            RelativeLayout select;

        }


        @Override

        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        public Context context;


        public DiscoverMyCubeAdapter(List<CubeCategoeyList> apps, Context context) {
            grid_element1 = apps;
            this.context = context;
        }


        @Override
        public int getCount() {


            return grid_element1.size();
        }


        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            final ViewHolder viewHolder;

            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.cube_designs, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.back = (ImageView) rowView.findViewById(R.id.unfollow);
                viewHolder.imag = (ImageView) rowView.findViewById(R.id.back3);
                viewHolder.img = (PorterShapeImageView) rowView.findViewById(R.id.cube_img1);
                viewHolder.name = (TextView) rowView.findViewById(R.id.cube_name1);
                viewHolder.cube_category = (LinearLayout) rowView.findViewById(R.id.main1);
                viewHolder.select = (RelativeLayout) rowView.findViewById(R.id.select);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.img.setVisibility(View.VISIBLE);
            Glide.with(context).load(grid_element1.get(position).getCube_image()).override(200, 200)
                    .into(viewHolder.img);
            emote_allgrid.setNumColumns(4);
            emote_allgrid.setColumnWidth(20);

            if (grid_element1.get(position).getCube_name().length() > 8) {
                viewHolder.name.setText(grid_element1.get(position).getCube_name().subSequence(0, 6) + "..");
            } else {
                viewHolder.name.setText(grid_element1.get(position).getCube_name());
            }
            final View finalRowView = rowView;
            viewHolder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView unfolow=(ImageView) finalRowView. findViewById(R.id.unfollow);
                    unfolow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ImageView unfolow=(ImageView) finalRowView. findViewById(R.id.unfollow);

                            unfolow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(ProfileView.this);
                                    builder.setMessage("Are you sure you want to delete the cube");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {

                                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                                            final String url = Config.BASE_URL + "Cubes/Delete_Cube";

                                            // Request a string response from the provided URL.
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            // Display the first 500 characters of the response string.
                                                            if (response.contains("True")) {

                                                                try {

                                                                    JSONObject json = new JSONObject(response);

                                                                    if (json.getString("Output").contains("True")) {

                                                                        if (json.getString("Message").contains("Success")) {

                                                                            dialog.dismiss();
                                                                            grid_element1.remove(position);
                                                                            notifyDataSetChanged();

                                                                        }
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            } else {

                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                    Toast.makeText(getApplicationContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();
                                                }
                                            }) {
                                                protected Map<String, String> getParams() {

                                                    Map<String, String> para = new HashMap<String, String>();

                                                    para.put("Cube_Id", Cube_Id);
                                                    para.put("User_Id", login_user_id);
                                                    return para;
                                                }
                                            };
                                            // Add the request to the RequestQueue.
                                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
                                            queue.add(stringRequest);



                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    }).show();
                                }
                            });

                        }
                    });
                }

            });


            viewHolder.cube_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, CubeBasedPost.class);
                    intent.putExtra("cube_id", grid_element1.get(position).getCube_id());
                    context.startActivity(intent);
                }
            });
            return rowView;
        }
    }

    public class DiscoverMyCubeAdapters extends BaseAdapter {


        class ViewHolder {
            PorterShapeImageView img;
            TextView name;
            ImageView imag, back;
            LinearLayout cube_category;
            RelativeLayout select;

        }


        @Override

        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        public Context context;


        public DiscoverMyCubeAdapters(List<CubeCategoeyList> apps, Context context) {
            grid_elements = apps;
            this.context = context;
        }


        @Override
        public int getCount() {
            return grid_elements.size();
        }


        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            final ViewHolder viewHolder;

            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.cube_design, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.back = (ImageView) rowView.findViewById(R.id.unfollow1);
                viewHolder.imag = (ImageView) rowView.findViewById(R.id.back3);
                viewHolder.img = (PorterShapeImageView) rowView.findViewById(R.id.cube_img);
                viewHolder.name = (TextView) rowView.findViewById(R.id.cube_name);
                viewHolder.cube_category = (LinearLayout) rowView.findViewById(R.id.main);
                viewHolder.select = (RelativeLayout) rowView.findViewById(R.id.select);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.img.setVisibility(View.VISIBLE);
            Glide.with(context).load(grid_elements.get(position).getCube_image()).override(200, 200)
                    .into(viewHolder.img);
            emote_allgrid.setNumColumns(4);
            emote_allgrid.setColumnWidth(20);

            if (grid_elements.get(position).getCube_name().length() > 8) {
                viewHolder.name.setText(grid_elements.get(position).getCube_name().subSequence(0, 6) + "..");
            } else {
                viewHolder.name.setText(grid_elements.get(position).getCube_name());
            }
            final View finalRowView = rowView;
            viewHolder.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog.Builder builder=new AlertDialog.Builder(ProfileView.this);
                    builder.setMessage("Are you sure you want to leave this cube?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unfolloow();
                            dialog.dismiss();
                            grid_elements.remove(position);
                            notifyDataSetChanged();

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();



                }
            });


            viewHolder.cube_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, CubeBasedPost.class);
                    intent.putExtra("cube_id", grid_elements.get(position).getCube_id());
                    context.startActivity(intent);
                }
            });
            return rowView;
        }
    }



    @Override
    protected void onResume()
    {
        super.onResume();
        new Get_Profile_Info().execute();

        new GetCreatedCube().execute();

        new GetFollowCube().execute();

    }

    public class GetFollowCube extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(ProfileView.this);
            pd.setMessage("Loading...");
            pd.show();

            cube_list_data = new ArrayList<>();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(ProfileView.this);

                final String url = Config.BASE_URL + "Cubes/User_Followed_Cubes/" + login_user_id;
                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("Output").contains("True")) {

                                        JSONArray ress = response.getJSONArray("Response");

                                        for (int i = 0; i < ress.length(); i++) {

                                            JSONObject data = ress.getJSONObject(i);

                                            Cube_Id = data.getString("_id");
                                            Cube_Name = data.getString("Name");
                                            Cube_Image = Config.Cube_Image + data.getString("Image");
                                            Cat_Id = data.getString("Category_Id");
                                            cube_list_data.add(new CubeCategoeyList(Cube_Name, "",
                                                    Cube_Id, Cube_Image,
                                                    "",
                                                    "", "", Cat_Id, "0"));

                                        }
                                    }


                                    if (cube_list_data.size() != 0) {
                                        viewall.setVisibility(View.VISIBLE);

                                        no_cubes.setVisibility(View.GONE);
                                        following_cubes.setVisibility(View.VISIBLE);
                                        adapter1=new Cube1(ProfileView.this,cube_list_data);
                                        following_cubes.setAdapter(adapter1);


                                    } else {

                                        following_cubes.setVisibility(View.GONE);
                                        no_cubes.setVisibility(View.VISIBLE);

                                    }


                                } catch (Exception e) {

                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                queue.add(getRequest);

            } else
                {

                Toast.makeText(ProfileView.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (pd.isShowing()) {

                pd.dismiss();
            }
        }
    }
    public class Cube1 extends RecyclerView.Adapter<Cube1.ViewHolder> {

        Context con;
        List<CubeCategoeyList> Cube1;

        public Cube1(Context con, List<CubeCategoeyList> Cube1) {
            this.con = con;
            this.Cube1 = Cube1;

        }

        @NonNull
        @Override
        public Cube1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cube_designs, parent, false);

            return new Cube1.ViewHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull Cube1.ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);

            Glide.with(con).load(Cube1.get(position).getCube_image()).override(200, 200)
                    .into(holder.cube_type_img);

            if (Cube1.get(position).getCube_name().length() > 8) {
                holder.cube_name.setText(Cube1.get(position).getCube_name().subSequence(0, 6) + "..");
            } else {
                holder.cube_name.setText(Cube1.get(position).getCube_name());
            }

            holder.main.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v)
                                               {

                                                   Intent intent = new Intent(con, CubeBasedPost.class);
                                                   intent.putExtra("cube_id", Cube1.get(position).getCube_id());
                                                   con.startActivity(intent);
                                               }
                                           }
            );

            holder.cube_type_img.setVisibility(View.VISIBLE);
            holder.cube_name.setVisibility(View.VISIBLE);
            holder.unfolow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(ProfileView.this);
                    builder.setMessage("Are you sure you want to leave this cube?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unfolloow();
                            notifyDataSetChanged();
                            Cube1.remove(position);


                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });

        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public int getItemCount() {

            return Cube1.size();

        }

        public class ViewHolder
                extends RecyclerView.ViewHolder {


            PorterShapeImageView cube_type_img;
            TextView cube_name;
            LinearLayout main;
            ImageView   unfolow;

            public ViewHolder(View itemView) {
                super(itemView);

                cube_type_img = (PorterShapeImageView) itemView.findViewById(R.id.cube_img1);
                cube_name = (TextView) itemView.findViewById(R.id.cube_name1);
                main = (LinearLayout) itemView.findViewById(R.id.main1);
                 unfolow=(ImageView)itemView.findViewById(R.id.unfollow);

            }
        }
    }

public void unfolloow()
{

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

    final String url = Config.BASE_URL + "Cubes/UnFollow_Cube";

    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    if (response.contains("True")) {

                        try {

                            JSONObject json = new JSONObject(response);

                            if (json.getString("Output").contains("True")) {
                                 Toast.makeText(getApplicationContext(), "Success",Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            Toast.makeText(getApplicationContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();
        }
    }) {
        protected Map<String, String> getParams() {

            Map<String, String> para = new HashMap<String, String>();

            para.put("Cube_Id", Cube_Id);
            para.put("User_Id", login_user_id);
            return para;
        }
    };
    // Add the request to the RequestQueue.
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
    queue.add(stringRequest);
}

    public class Cube extends RecyclerView.Adapter<Cube.ViewHolder> {

        Context con;
        List<CubeCategoeyList> Cube;

        public Cube(Context con, List<CubeCategoeyList> Cube) {
            this.con = con;
            this.Cube = Cube;

        }

        @NonNull
        @Override
        public Cube.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cube_design, parent, false);
            return new Cube.ViewHolder(v);

        }
        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public void onBindViewHolder(@NonNull Cube.ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);

            Glide.with(con).load(Cube.get(position).getCube_image()).override(200, 200)
                    .into(holder.cube_type_img);

            if (Cube.get(position).getCube_name().length() > 8) {
                holder.cube_name.setText(Cube.get(position).getCube_name().subSequence(0, 6) + "..");
            } else {
                holder.cube_name.setText(Cube.get(position).getCube_name());
            }

            holder.main.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   Intent intent = new Intent(con, CubeBasedPost.class);
                                                   intent.putExtra("cube_id", Cube.get(position).getCube_id());
                                                   con.startActivity(intent);
                                               }
                                           }
            );

            holder.cube_type_img.setVisibility(View.VISIBLE);
            holder.cube_name.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ProfileView.this);
                    builder.setMessage("Are you sure you want to delete the cube");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                            final String url = Config.BASE_URL + "Cubes/Delete_Cube";

                            // Request a string response from the provided URL.
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the first 500 characters of the response string.
                                            if (response.contains("True")) {

                                                try {

                                                    JSONObject json = new JSONObject(response);

                                                    if (json.getString("Output").contains("True")) {

                                                        if (json.getString("Message").contains("Success")) {


                                                            dialog.dismiss();
                                                            notifyDataSetChanged();
                                                            Cube.remove(position);
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            } else {

                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(getApplicationContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                protected Map<String, String> getParams() {

                                    Map<String, String> para = new HashMap<String, String>();

                                    para.put("Cube_Id", Cube_Id);
                                    para.put("User_Id", login_user_id);
                                    return para;
                                }
                            };
                            // Add the request to the RequestQueue.
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
                            queue.add(stringRequest);



                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).show();
                }
            });

        }

        @Override
        public int getItemCount() {

            return Cube.size();

        }

        public class ViewHolder
                extends RecyclerView.ViewHolder {


            PorterShapeImageView cube_type_img;
            TextView cube_name;
            LinearLayout main;
            ImageView delete;

            public ViewHolder(View itemView) {
                super(itemView);

                cube_type_img = (PorterShapeImageView) itemView.findViewById(R.id.cube_img);
                cube_name = (TextView) itemView.findViewById(R.id.cube_name);
                main = (LinearLayout) itemView.findViewById(R.id.main);
                delete=(ImageView)itemView.findViewById(R.id.unfollow1);


            }

        }


    }

    public void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);
    }

    private class Get_Profile_Info extends AsyncTask {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileView.this);
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected Object doInBackground(Object[] objects) {


            RequestQueue queue = Volley.newRequestQueue(ProfileView.this);

            final String url = Config.BASE_URL + "Signin_Signup/User_Info/" + login_user_id;

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                Log.i("Test", "onResponse: " + response);
                                if (response.getString("Output").contains("True")) {

                                    JSONObject data = response.getJSONObject("Response");

                                    UserName = data.getString("Inscube_Name");
                                    UserEmail = data.getString("Email");
                                    UserImage = Config.User_profile_Image + data.getString("Image");
                                    UserId = data.getString("_id");
                                    ColorCode = data.getString("Color_Code");
                                    DOB = data.getString("DOB");

                                    try {
                                        if (data.getString("City") != null) {
                                            City = data.getString("City");
                                        }
                                    } catch (Exception e) {
                                        City = "";
                                    }

                                    try {
                                        if (data.getString("Country") != null) {
                                            Country = data.getString("Country");
                                        }
                                    } catch (Exception e) {
                                        Country = "";
                                    }

                                    try {
                                        if (data.getString("State") != null) {
                                            State = data.getString("State");
                                        }

                                    } catch (Exception e) {
                                        State = "";
                                    }

                                    Gender = data.getString("Gender");
                                    privacy = data.getString("Show_Profile_To");

                                    if (data.toString().contains("Hash_Tag_1")) {

                                        Hash_Tag_1 = data.getString("Hash_Tag_1");

                                    }
                                    if (data.toString().contains("Hash_Tag_2")) {

                                        Hash_Tag_2 = data.getString("Hash_Tag_2");

                                    }
                                    if (data.toString().contains("Hash_Tag_3")) {

                                        Hash_Tag_3 = data.getString("Hash_Tag_3");

                                    }

                                    if (UserName.length() > 23) {

                                        Pro_Name.setText(UserName.subSequence(0, 21) + "...");

                                    } else {

                                        Pro_Name.setText(UserName);

                                    }


                                    Glide.with(ProfileView.this).load(UserImage)
                                            .override(150, 150).into(main_profile_img);
                                    main1.setVisibility(View.VISIBLE);
                                }

                                if (pd.isShowing()) {

                                    pd.dismiss();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                        }
                    }
            );

            getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

            queue.add(getRequest);


            return null;
        }

    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }


    private class GetCreatedCube extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(ProfileView.this);
            pd.setMessage("Loading...");
            pd.show();

            cube_list_data1 = new ArrayList<>();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(ProfileView.this);

                final String url = Config.BASE_URL + "Cubes/User_Cubes/" + login_user_id;

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    if (response.getString("Output").contains("True")) {

                                        JSONArray ress = response.getJSONArray("Response");


                                        for (int i = 0; i < ress.length(); i++) {
                                            JSONObject data = ress.getJSONObject(i);

                                            Cube_Id = data.getString("_id");
                                            Cube_Name = data.getString("Name");
                                            Cube_Image = Config.Cube_Image + data.getString("Image");
                                            Cat_Id = data.getString("Category_Id");


                                            cube_list_data1.add(new CubeCategoeyList(Cube_Name, "", Cube_Id, Cube_Image, "",
                                                    "", "", Cat_Id, "0"));

                                        }

                                        if (cube_list_data1.size() != 0) {

                                            // Log.d("sa--", String.valueOf(cube_list_data1.size()));
                                            viewall1.setVisibility(View.VISIBLE);

                                            no_cubes1.setVisibility(View.GONE);
                                            my_cubes.setVisibility(View.VISIBLE);
                                            adapter = new Cube(ProfileView.this, cube_list_data1);
                                            my_cubes.setAdapter(adapter);

                                        }
                                        else
                                        {

                                            my_cubes.setVisibility(View.GONE);
                                            no_cubes1.setVisibility(View.VISIBLE);

                                        }

                                    }
                                } catch (Exception e) {

                                }
                                if (pd.isShowing()) {

                                    pd.dismiss();
                                }

                            }
                        },
                        new Response.ErrorListener()

                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );

                getRequest.setRetryPolicy(new

                        DefaultRetryPolicy(8000, 3, 2));

                queue.add(getRequest);

            } else

            {

                Toast.makeText(ProfileView.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }


    }


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 90:
//                if (resultCode == RESULT_OK) {
//
//                    SharedPreferences sp = getSharedPreferences("remove", MODE_PRIVATE);
//                    SharedPreferences sp1 = getSharedPreferences("remove_type", MODE_PRIVATE);
//
//                    if (sp.getString("remove", "").contains("1")) {
//
//                        if (sp1.getString("remove_type", "").contains("create")) {
//
//                            cube_list_data1.remove(Integer.parseInt(Position));
//                            adapter.notifyDataSetChanged();
//
//                        } else if (sp1.getString("remove_type", "").contains("follow")) {
//                            cube_list_data.remove(Integer.parseInt(Position));
//                            adapter.notifyDataSetChanged();
//                        }
//
//                    }
//                    SharedPreferences.Editor loggedin = getSharedPreferences("remove", MODE_PRIVATE).edit();
//                    loggedin.putString("remove", "0");
//                    loggedin.apply();
//                    Position = "";
//
//
//                }
//                break;
//        }
//    }
}

