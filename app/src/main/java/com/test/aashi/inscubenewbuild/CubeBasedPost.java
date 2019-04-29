package com.test.aashi.inscubenewbuild;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adapter.HighlightPostAdapter;
import GetterSetter.EmoteList;
import GetterSetter.Highlights_PostList;

public class CubeBasedPost extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView cube_post_rv;
    SharedPreferences login;
    String Ins_Name, login_user_Id, User_Email, User_Image, Color_Code;
    TextView no_post_txt;
    List<Highlights_PostList> highlights_postLists = new ArrayList<>();
    HighlightPostAdapter highlightPostAdapter;
    String Post_Id, Post_User_Id, Post_Category, Post_Text, Post_Link, Post_Ins_Name, Post_User_Image, Time_Ago, cube_id = "",
            cube_name, cube_type, cube_cat, cube_member, cube_img, cube_user_id, PostLink_Title = "",
            PostLink_Description = "", PostLink_Image = "", PostLink_Url = "", PostLinkInfo = "";
    ArrayList<String> Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image,
            File_Name, File_Type, Emote_Id, Emote_Text, Emote_Count, Emote_Ids;
    TextView cube_namee, cube_members, cube_category;
    ImageView cube_type_img, Cube_image, cube_view, cube_view_back;
    LinearLayout cube_det,main;
    ArrayList<EmoteList> emoteLists;
    SwipeRefreshLayout refresh_layout;
    String sharedpost,ownername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_based_post);

        cube_id = getIntent().getStringExtra("cube_id");


        cube_namee = (TextView) findViewById(R.id.cube_name);
        cube_members = (TextView) findViewById(R.id.cube_members);
        cube_category = (TextView) findViewById(R.id.cube_cat);

        cube_type_img = (ImageView) findViewById(R.id.cube_type);
        Cube_image = (ImageView) findViewById(R.id.cube_img);
        cube_view = (ImageView) findViewById(R.id.cube_view);
        cube_view_back = (ImageView) findViewById(R.id.cube_view_back);

        cube_det = (LinearLayout) findViewById(R.id.cube_det);
        main = (LinearLayout) findViewById(R.id.main);
        login = getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_Id = login.getString("_id", "");
        Ins_Name = login.getString("UserName", "");
        User_Email = login.getString("UserEmail", "");
        User_Image = login.getString("UserImage", "");
        Color_Code = login.getString("ColorCode", "");
        refresh_layout =(SwipeRefreshLayout)findViewById(R.id.cube_referesh);

        cube_post_rv = (RecyclerView) findViewById(R.id.cube_post_rv);
        cube_post_rv.setLayoutManager(new LinearLayoutManager(CubeBasedPost.this, LinearLayoutManager.VERTICAL, false));
        no_post_txt = (TextView) findViewById(R.id.no_post_txt);


        refresh_layout.setOnRefreshListener(this);
        refresh_layout.post(new Runnable() {
                                @Override
                                public void run() {

                                    refresh_layout.setRefreshing(true);
                                    new GetCubeDetails().execute();
                                    new Get_post().execute();
                                }
                            }
        );
        cube_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(CubeBasedPost.this, CubeView.class);
                intent.putExtra("Feeds","1");
                intent.putExtra("Cube_Id", cube_id);
                intent.putExtra("Cube_User_Id", cube_user_id);
                intent.putExtra("Cube_Type",cube_type);
                startActivity(intent);
            }
        });

        cube_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

    }



    public void setcube_info() {

        if (cube_type.contains("Open")) {
            cube_type_img.setImageResource(R.drawable.globe);
        } else {
            cube_type_img.setImageResource(R.drawable.lock);
        }

        if (cube_name.length() > 23) {

            cube_namee.setText(cube_name.subSequence(0, 23) + "...");

        } else {

            cube_namee.setText(cube_name);
        }


        if (cube_cat.length() > 17) {

            cube_category.setText(cube_cat.subSequence(0, 14) + "..");

        } else {

            cube_category.setText(cube_cat);
        }

        cube_members.setText(cube_member);

        Glide.with(CubeBasedPost.this).load(cube_img).override(200, 200)
                .into(Cube_image);

        cube_det.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {

        new Get_post().execute();
    }

    private class Get_post extends AsyncTask {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CubeBasedPost.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            RequestQueue queue = Volley.newRequestQueue(CubeBasedPost.this);

            String url = Config.BASE_URL + "Posts/Cube_Based_Post_List/" + cube_id + "/" + login_user_Id;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject res = new JSONObject(response);

                                if (res.getString("Output").contains("True")) {

                                    JSONArray respon = res.getJSONArray("Response");

                                    if (respon.length() != 0) {


                                        for (int i = 0; i < respon.length(); i++) {

                                            JSONObject data = respon.getJSONObject(i);

                                            Post_Id = data.getString("_id");
                                            Post_User_Id = data.getString("User_Id");
                                            Post_Category = data.getString("Post_Category");
                                            Post_Text = data.getString("Post_Text");
                                            Post_Ins_Name = data.getString("User_Name");
                                            Post_User_Image = Config.Images + "Users/" + data.getString("User_Image");
                                            Time_Ago = data.getString("Time_Ago");
                                            sharedpost = data.getString("Shared_Post");
                                            if (sharedpost.equals("True"))
                                            {
                                                ownername = data.getString("Post_Owner_Name");
                                            }
                                            else
                                            {
                                                ownername = "1";
                                            }
                                            JSONArray cubes_info = data.getJSONArray("Cubes_Info");

                                            Cube_Id = new ArrayList<>();
                                            Cube_Cat_Id = new ArrayList<>();
                                            Cube_name = new ArrayList<>();
                                            Cube_Image = new ArrayList<>();

                                            if (cubes_info.length() != 0) {

                                                for (int j = 0; j < cubes_info.length(); j++) {


                                                    JSONObject cubes_data = cubes_info.getJSONObject(j);

                                                    Cube_Id.add(cubes_data.getString("_id"));
                                                    Cube_Cat_Id.add(cubes_data.getString("Category_Id"));
                                                    Cube_name.add(cubes_data.getString("Name"));
                                                    Cube_Image.add(Config.Cube_Image + cubes_data.getString("Image"));

                                                }

                                            } else
                                             {

                                                Cube_Id = new ArrayList<>();
                                                Cube_Cat_Id = new ArrayList<>();
                                                Cube_name = new ArrayList<>();
                                                Cube_Image = new ArrayList<>();

                                            }

                                            JSONArray files = data.getJSONArray("Attachments");

                                            if (files.length() != 0) {

                                                File_Name = new ArrayList<>();
                                                File_Type = new ArrayList<>();

                                                for (int j = 0; j < files.length(); j++) {

                                                    JSONObject file_data = files.getJSONObject(j);

                                                    File_Name.add(Config.BASE_URL + "Uploads/Post_Attachments/" + file_data.getString("File_Name"));
                                                    File_Type.add(file_data.getString("File_Type"));

                                                }

                                            } else {

                                                File_Name = new ArrayList<>();
                                                File_Type = new ArrayList<>();
                                            }

                                            JSONArray emote = data.getJSONArray("Emotes");

                                            if (emote.length() != 0) {

                                                emoteLists = new ArrayList<>();

                                                for (int j = 0; j < emote.length(); j++) {

                                                    JSONObject emote_data = emote.getJSONObject(j);

                                                    JSONArray emoteid = emote_data.getJSONArray("User_Ids");

                                                    if (emoteid != null) {

                                                        Emote_Ids = new ArrayList<>();


                                                        for (int k = 0; k < emoteid.length(); k++) {

                                                            Emote_Ids.add(emoteid.getString(k));

                                                        }

                                                    }

                                                    emoteLists.add(new EmoteList(emote_data.getString("_id"), emote_data.getString("Emote_Text")
                                                            , emote_data.getString("Count"), emote_data.getString("Post_Id"), Emote_Ids));

                                                    Collections.reverse(emoteLists);

                                                }

                                            } else {

                                                Emote_Id = new ArrayList<>();
                                                Emote_Text = new ArrayList<>();
                                                Emote_Count = new ArrayList<>();
                                                emoteLists = new ArrayList<>();
                                            }


                                            Post_Link = data.getString("Post_Link");

                                            if (Post_Link.length() > 1) {

                                                if (!Post_Link.contains("www.youtube.com") && !Post_Link.contains("youtu.be")) {

                                                    try {

                                                        JSONObject PostLinkInfo = data.getJSONObject("Post_Link_Info");


                                                        if (PostLinkInfo.length() > 1) {

                                                            // JSONObject Linkarray = new JSONObject(PostLinkInfo);

                                                            PostLink_Title = PostLinkInfo.getString("title");
                                                            PostLink_Description = PostLinkInfo.getString("description");
                                                            PostLink_Image = PostLinkInfo.getString("image");
                                                            PostLink_Url = "<a href='" + PostLinkInfo.getString("url") + "'>" + PostLinkInfo.getString("url") + "</a>";

                                                        }
                                                    } catch (Exception e) {

                                                        PostLink_Title = "";
                                                        PostLink_Description = "";
                                                        PostLink_Image = "";
                                                        PostLink_Url = "<a href='" + data.getString("Post_Link") + "'>" + data.getString("Post_Link") + "</a>";

                                                    }

                                                } else {

                                                    if (Post_Link.contains("www.youtube.com")) {

                                                        PostLink_Title = "Youtube Video";

                                                        PostLink_Description = "";

                                                        PostLink_Image = "https://img.youtube.com/vi/"
                                                                + data.getString("Post_Link").replace("https://www.youtube.com/watch?v=", "").trim() + "/1.jpg";

                                                        PostLink_Url = "<a href='" + data.getString("Post_Link") + "'>" + data.getString("Post_Link") + "</a>";


                                                    } else if (Post_Link.contains("youtu.be")) {

                                                        PostLink_Title = "Youtube Video";

                                                        PostLink_Description = "";

                                                        PostLink_Image = "https://img.youtube.com/vi/"
                                                                + data.getString("Post_Link").replace("https://youtu.be/", "").trim() + "/1.jpg";

                                                        PostLink_Url = "<a href='" + data.getString("Post_Link") + "'>" + data.getString("Post_Link") + "</a>";

                                                    }
                                                    else
                                                        {

                                                        PostLink_Title = "";
                                                        PostLink_Description = "";
                                                        PostLink_Image = "";
                                                        PostLink_Url = "<a href='" + data.getString("Post_Link") + "'>" + data.getString("Post_Link") + "</a>";

                                                    }

                                                }

                                            } else {

                                                PostLink_Title = "";
                                                PostLink_Description = "";
                                                PostLink_Image = "";
                                                PostLink_Url = "";

                                            }
                                            highlights_postLists.add(new Highlights_PostList(Post_Id, Post_User_Id, Post_Category,
                                                    Post_Text, Post_Link, Post_Ins_Name, Post_User_Image, Time_Ago,
                                                    Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image,
                                                    File_Name, File_Type, Emote_Id, Emote_Text, Emote_Count, emoteLists, Post_Link, PostLink_Title, PostLink_Description, PostLink_Image,
                                                    PostLink_Url,null,ownername));

                                        }

                                    } else {

                                        no_post_txt.setVisibility(View.VISIBLE);
                                        cube_post_rv.setVisibility(View.GONE);

                                    }

                                    if (pd.isShowing()) {

                                        pd.dismiss();

                                    }

                                    highlightPostAdapter = new HighlightPostAdapter(CubeBasedPost.this, highlights_postLists);
                                    cube_post_rv.setAdapter(highlightPostAdapter);
                                    highlightPostAdapter.setA(highlightPostAdapter);
                                    main.setVisibility(View.VISIBLE);
                                    refresh_layout.setRefreshing(false);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(CubeBasedPost.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }
    }


    private class GetCubeDetails extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(CubeBasedPost.this);
            pd.setMessage("Loading...");
        //    pd.show();

        }

        @Override
        protected void onPostExecute(Object o) {

            if (pd.isShowing()) {
                pd.dismiss();
            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final String url = Config.BASE_URL + "Cubes/View_Cube/" + cube_id + "/" + login_user_Id;
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.toString().contains("True")) {

                                        try {

                                            if (response.getString("Output").contains("True")) {

                                                JSONObject ress = response.getJSONObject("Response");


                                                cube_name = ress.getString("Name");
                                                cube_member = ress.getString("Members");
                                                cube_user_id = ress.getString("User_Id");
                                                cube_img = Config.Cube_Image + ress.getString("Image");

                                                cube_type = ress.getString("Security");
                                                cube_cat = ress.getString("Category_Name");

//                                                Cube_Description = ress.getString("Description");
//                                                Cube_location = ress.getString("Location");
//                                                Cube_mail = ress.getString("Mail");
//                                                Cube_Contact = ress.getString("Contact");
//                                                Cube_Web = ress.getString("Web");
//
//                                                JSONArray Topic = ress.getJSONArray("Topics");
//
//                                                if (Topic.length() != 0) {
//
//                                                    for (int j = 0; j < Topic.length(); j++) {
//
//                                                        JSONObject data = Topic.getJSONObject(j);
//
//                                                        Topic_name = data.getString("Name");
//
//                                                        Topic_Description = data.getString("Description");
//
//                                                        JSONArray attach = data.getJSONArray("Attachments");
//
//                                                        filee = new ArrayList<>();
//
//                                                        if (attach.length() != 0) {
//
//                                                            //Log.d("Size--", String.valueOf(attach.length()));
//
//                                                            for (int a = 0; a < attach.length(); a++) {
//
//                                                                JSONObject file = attach.getJSONObject(a);
//
//                                                                file_name = new ArrayList<>();
//                                                                file_type = new ArrayList<>();
//
//
//                                                                Topic_File = file.getString("File_Name");
//                                                                Topic_FileType = file.getString("File_Type");
//
//                                                                file_name.add(Config.BASE_URL + "Uploads/Topic_Attachments/" + Topic_File);
//                                                                file_type.add(Topic_FileType);
//
//                                                                filee.addAll(file_name);
//                                                                //Log.d("Sizeee--", file_name.toString());
//                                                            }
//
//                                                            //  Log.d("Sizeee--", file_name.toString());
//
//                                                        } else {
//
//                                                            file_name = new ArrayList<>();
//                                                            file_type = new ArrayList<>();
//
//                                                        }
//                                                        topicLists.add(new TopicList(Topic_name, Topic_Description, filee, file_type));
//                                                    }
//
//
//                                                } else {
//
//                                                    topic_list.setVisibility(View.GONE);
//                                                    NoTopic.setVisibility(View.VISIBLE);
//
//                                                }
                                                refresh_layout.setRefreshing(false);
                                                setcube_info();
                                            }


                                        } catch (Exception e) {

                                        }
                                    }
                                } catch (Exception e) {


                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                //Log.d("req--", error.toString());

                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                queue.add(getRequest);


            } else {

                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }
    @Override
    public void onBackPressed() {

        Intent _result = new Intent();
        setResult(Activity.RESULT_OK, _result);
        finish();
    }
}
