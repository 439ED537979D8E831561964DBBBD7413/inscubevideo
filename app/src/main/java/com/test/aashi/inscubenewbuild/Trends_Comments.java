package com.test.aashi.inscubenewbuild;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.CaptureCommentAdapter;
import Adapter.FeedAdapterSmallIcon;
import Adapter.PostCommentAdapter;
import Adapter.Post_MultiImage_Adapter;
import GetterSetter.Comment_List;
import GetterSetter.EmoteList;

public class Trends_Comments extends AppCompatActivity {

    TextView Story_user_name, post_type, time_agotxt, post_text, nocommentstxt, any_link, link_des, link_url, link_title;
    RecyclerView multi_img_rv, cube_recycler_view, comment_list_view;
    LayoutInflater layoutInflater;
    String login_user_id, User_Image, Color_code, Ins_name;
    FeedAdapterSmallIcon smallIcon_adapter;
    SharedPreferences login;
    Post_MultiImage_Adapter multi_image_adapter;
//    CaptureCommentAdapter highlightsCommentAdapter;
    PostCommentAdapter highlightsCommentAdapter;
    CircularImageView user_img;
    String Post_Id, Post_User_Id, Post_Category, Post_Text, Post_Link, Post_Ins_Name, Post_User_Image, Time_Ago,
            PostLink_Title = "",
            PostLink_Description = "", PostLink_Image = "", PostLink_Url = "";
    ArrayList<String> Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image,
            File_Name, File_Type, Emote_Id, Emote_Text, Emote_Count, Emote_Ids;
    ArrayList<EmoteList> emoteLists;
    ImageView more_option, link_img, comment_submit;
    LinearLayout viewGroup, link_design, any_link_design;
    LinearLayout reportuser, reportpost, editt, delete, reportview, editview;
    EditText comment_txt;
    ArrayList<EmoteList> emoteList,new_emote;
    GridView emote_view;
    ImageView high_emote,high_comment;
    PopupWindow emote_popup;
   EmoteGridAdapter emoteGridAdapter;
   LinearLayout emotelayout;
    List<Comment_List> comment_listList = new ArrayList<>();
    String Comment_User_Id, Comment_User_Name, Comment_Text, Comment_Post_Id, Comment_User_Image, Comment_Id, Comment_Time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights__comments);
        emoteLists =new ArrayList<>();
        Post_Id = getIntent().getStringExtra("post_id");
        login = getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        User_Image = login.getString("UserImage", "");
        Color_code = login.getString("ColorCode", "");
        Ins_name = login.getString("UserName", "");
        new Get_Post().execute();
        new Get_Comments().execute();

        emotelayout=(LinearLayout)findViewById(R.id.emote_layout1);
        emote_view =(GridView)findViewById(R.id.emote_grida);
        emotelayout.setVisibility(View.GONE);
        high_comment=(ImageView)findViewById(R.id.high_comment);
        high_emote=(ImageView)findViewById(R.id.high_emote) ;

        Story_user_name = (TextView) findViewById(R.id.Story_user_name);
        time_agotxt = (TextView) findViewById(R.id.time_agotxt);
        post_type = (TextView) findViewById(R.id.post_type);
        post_text = (TextView) findViewById(R.id.post_text);
        nocommentstxt = (TextView) findViewById(R.id.nocommentstxt);
        any_link = (TextView) findViewById(R.id.any_link);
        any_link.setMovementMethod(LinkMovementMethod.getInstance());
        link_title = (TextView) findViewById(R.id.link_title);
        link_url = (TextView) findViewById(R.id.link_url);
        link_url.setMovementMethod(LinkMovementMethod.getInstance());
        link_des = (TextView) findViewById(R.id.link_des);
        comment_txt = (EditText) findViewById(R.id.comment_txt);
        more_option = (ImageView) findViewById(R.id.more_option);
        link_img = (ImageView) findViewById(R.id.link_img);
        comment_submit = (ImageView) findViewById(R.id.comment_submit_b11);
        user_img = (CircularImageView) findViewById(R.id.pimg);
        multi_img_rv = (RecyclerView) findViewById(R.id.multi_img_rv);
        cube_recycler_view = (RecyclerView) findViewById(R.id.cube_recycler_view);
        comment_list_view = (RecyclerView) findViewById(R.id.comment_list_view11);
        viewGroup = (LinearLayout) findViewById(R.id.shrpopup);
        link_design = (LinearLayout) findViewById(R.id.link_design);
        any_link_design = (LinearLayout) findViewById(R.id.any_link_design);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        multi_img_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        cube_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        comment_list_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        high_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emotelayout.setVisibility(View.GONE);
                comment_list_view.setVisibility(View.VISIBLE);
                nocommentstxt.setText("no Comments");
            }
        });

        high_emote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_list_view.setVisibility(View.GONE);
                if (emoteLists.size() == 0) {

                    nocommentstxt.setVisibility(View.VISIBLE);
                    nocommentstxt.setText("No Emotes found");
                    emotelayout.setVisibility(View.GONE);


                }
                else
                {
                    emotelayout.setVisibility(View.VISIBLE);

                    nocommentstxt.setVisibility(View.GONE);

                }
                final EditText emote_input;

                Button emote_submit;


                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View mView = inflater.inflate(R.layout.get_emote_design, null);

                emote_popup = new PopupWindow(
                        mView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                emote_popup.setFocusable(true);
                emote_popup.setAnimationStyle(R.style.popup_window_animation_phone);
                emote_popup.showAtLocation(mView, Gravity.CENTER, 0, 0);
                dimBehind(emote_popup);
                emote_input = (EditText) mView.findViewById(R.id.emote_input);

                emote_submit = (Button) mView.findViewById(R.id.emote_submit);

                emote_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (emote_input.getText().toString().isEmpty()) {

                            Toast.makeText(getApplicationContext(), "Please Enter Something", Toast.LENGTH_SHORT).show();

                        } else {

                            RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());

                            String u = Config.BASE_URL + "Trends/Trends_Emote_Submit";

                            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        JSONObject json = new JSONObject(response);

                                        Log.i("Test", "onResponse: " + json);
                                        if (json.getString("Output").contains("True")) {

                                            new_emote = new ArrayList<>();

                                            try {

                                                ArrayList<String> emote_user_ids = new ArrayList<>();

                                                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();

                                                JSONObject resdata = json.getJSONObject("Response");

                                                JSONArray res = resdata.getJSONArray("User_Ids");

                                                if (res != null) {

                                                    emote_user_ids = new ArrayList<>();

                                                    for (int k = 0; k < res.length(); k++) {

                                                        emote_user_ids.add(res.getString(k));


                                                    }

                                                }
                                                new_emote.addAll(emoteLists);
                                                new_emote.add(0, new EmoteList(resdata.getString("_id"),
                                                        resdata.getString("Emote_Text")
                                                        , resdata.getString("Count"), resdata.getString
                                                        ("Trends_Id"), emote_user_ids));

                                                emoteLists.clear();

                                                //  list.get(position).setEmoteLists(new_emote);
                                                emoteLists.addAll(new_emote);

                                                emoteGridAdapter = new EmoteGridAdapter(getApplicationContext(),
                                                        emoteLists, emote_view);

                                                emote_view.setAdapter(emoteGridAdapter);

                                                emote_view.setVisibility(View.VISIBLE);
                                                emotelayout.setVisibility(View.VISIBLE);

                                            } catch (Exception e) {

                                            }

                                       /*     if (emoteLists.size() <= 2) {

                                                float factor = getApplicationContext().getResources().getDisplayMetrics().density;

                                                emote_view.getLayoutParams().height = (int) (42 * factor);

                                            } else {

                                                float factor = getApplicationContext().getResources().getDisplayMetrics().density;

                                             emote_view.getLayoutParams().height = (int) (73 * factor);
                                            }

                                            if (emoteLists.size() > 4) {

                                                view_all.setVisibility(View.VISIBLE);

                                            } else {

                                               view_all.setVisibility(View.GONE);
                                            }

                                            */

                                            emote_popup.dismiss();

                                        }
                                        else
                                        {

                                            Toast.makeText(getApplicationContext(), "Already Present!", Toast.LENGTH_SHORT).show();

                                        }


                                    } catch (JSONException e)

                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Connection Error!!", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                protected Map<String, String> getParams()
                                {
                                    Map<String, String> para = new HashMap<String, String>();

                                    para.put("Trends_Id", Post_Id );
                                    para.put("User_Id", login_user_id);
                                    para.put("Emote_Text", emote_input.getText().toString());

                                    return para;
                                }
                            };

                            MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                            MyRequestQueue.add(MyStringRequest);
                        }
                    }
                });
            }
        });
        more_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Point p;
                int[] location = new int[2];

                more_option.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];

                View layout = layoutInflater.inflate(R.layout.edit_popup, viewGroup);

                // Creating the PopupWindow
                final PopupWindow popup = new PopupWindow(getApplicationContext());
                popup.setContentView(layout);
                popup.setFocusable(true);
                popup.setWidth(FrameLayout.LayoutParams.WRAP_CONTENT);
                popup.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
                popup.setAnimationStyle(R.style.popup_window_animation_phone);
                // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
                int OFFSET_X = -10;
                int OFFSET_Y = 25;

                // Displaying the popup at the specified location, + offsets.
                popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);


                reportview = (LinearLayout) layout.findViewById(R.id.report_view);
                editview = (LinearLayout) layout.findViewById(R.id.edit_view);

                editt = (LinearLayout) layout.findViewById(R.id.edit);
                delete = (LinearLayout) layout.findViewById(R.id.delete);
                reportpost = (LinearLayout) layout.findViewById(R.id.reportpost);
                reportuser = (LinearLayout) layout.findViewById(R.id.reportuser);


                if (Post_User_Id.equals(login_user_id)) {

                    reportview.setVisibility(View.GONE);
                    editview.setVisibility(View.VISIBLE);

                } else {

                    reportview.setVisibility(View.VISIBLE);
                    editview.setVisibility(View.GONE);

                }

                editt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popup.dismiss();
/*
                        Intent edit = new Intent(getApplicationContext(), Highlight_Edit.class);
                        edit.putExtra("post_id", Post_Id);
                        edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(edit);*/
                    }

                });


            }
        });

        comment_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (comment_txt.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Please Enter the comment", Toast.LENGTH_SHORT).show();

                } else {
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    new PostComment().execute();

                }


            }
        });


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
    private class Get_Post extends AsyncTask {

        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Trends_Comments.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }

        @Override
        protected Object doInBackground(Object[] objects) {

            RequestQueue queue = Volley.newRequestQueue(Trends_Comments.this);

            String url = Config.BASE_URL + "Trends/Cube_Trends_View/" + login_user_id + "/" + Post_Id;

            Log.i("test", "doInBackground: " + url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Log.i("test", "doInBackground: " + response);

                                JSONObject res = new JSONObject(response);

                                if (res.getString("Output").contains("True")) {

                                    JSONArray respon = res.getJSONArray("Response");

                                    if (respon.length() != 0) {


                                        for (int i = 0; i < respon.length(); i++) {

                                            JSONObject data = respon.getJSONObject(i);

                                            Post_Id = data.getString("_id");
                                            Post_User_Id = data.getString("User_Id");
//                                            Post_Category = data.getString("Post_Category");
                                            Post_Text = data.getString("Trends_Text");
                                            Post_Ins_Name = data.getString("User_Name");
                                            Post_User_Image = Config.Images + "Users/" + data.getString("User_Image");
                                            Time_Ago = data.getString("Time_Ago");

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
                                            }
                                            else
                                            {
                                                Cube_Id = new ArrayList<>();
                                                Cube_Cat_Id = new ArrayList<>();
                                                Cube_name = new ArrayList<>();
                                                Cube_Image = new ArrayList<>();

                                            }
                                            JSONArray files = null;
                                            if (data.has("Capture_Video")) {
                                                files = data.getJSONArray("Capture_Video");
                                            }

                                            if (files != null && files.length() != 0)
                                            {

                                                File_Name = new ArrayList<>();
                                                File_Type = new ArrayList<>();

                                                for (int j = 0; j < files.length(); j++) {

                                                    JSONObject file_data = files.getJSONObject(j);

                                                    File_Name.add(Config.BASE_URL + "Uploads/Capture_Attachments/" +
                                                            file_data.getString("File_Name"));
                                                    File_Type.add(file_data.getString("File_Type"));

                                                }

                                            }
                                            else {

                                                File_Name = new ArrayList<>();
                                                File_Type = new ArrayList<>();
                                            }

                                            JSONArray emote = null;

                                            if (data.has("Emotes")) {
                                                emote = data.getJSONArray("Emotes");
                                            }
                                            if (emote!=null && emote.length() != 0) {

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
                                                            , emote_data.getString("Count"), emote_data.getString("Trends_Id"), Emote_Ids));

                                                    Collections.reverse(emoteLists);

                                                }
                                                emoteGridAdapter=new EmoteGridAdapter(Trends_Comments.this ,
                                                        emoteLists,emote_view);
                                                emote_view.setAdapter(emoteGridAdapter);
                                            } else {

                                                Emote_Id = new ArrayList<>();
                                                Emote_Text = new ArrayList<>();
                                                Emote_Count = new ArrayList<>();
                                                emoteLists = new ArrayList<>();
                                            }


                                            if(data.has(Post_Link)) {

                                                Post_Link = data.getString("Post_Link");
                                            }

                                            if (Post_Link!=null && !Post_Link.isEmpty()) {

                                                if (!Post_Link.contains("www.youtube.com") &&
                                                        !Post_Link.contains("youtu.be")) {

                                                    JSONObject PostLinkInfo = data.getJSONObject("Post_Link_Info");

                                                    if (PostLinkInfo.length() > 1) {

                                                        // JSONObject Linkarray = new JSONObject(PostLinkInfo);

                                                        PostLink_Title = PostLinkInfo.getString("title");
                                                        PostLink_Description = PostLinkInfo.getString("description");
                                                        PostLink_Image = PostLinkInfo.getString("image");
                                                        PostLink_Url = "<a href='" + PostLinkInfo.getString("url") + "'>" + PostLinkInfo.getString("url") + "</a>";

                                                    }

                                                } else {

                                                    if (Post_Link.contains("www.youtube.com")) {

                                                        PostLink_Title = "Youtube Video";

                                                        PostLink_Description = "";

                                                        PostLink_Image = "https://img.youtube.com/vi/"
                                                                + data.getString("Post_Link").
                                                                replace("https://www.youtube.com/watch?v=",
                                                                        "").trim() + "/1.jpg";

                                                        PostLink_Url = "<a href='" +
                                                                data.getString("Post_Link") + "'>" +
                                                                data.getString("Post_Link") + "</a>";


                                                    } else if (Post_Link.contains("youtu.be")) {

                                                        PostLink_Title = "Youtube Video";

                                                        PostLink_Description = "";

                                                        PostLink_Image = "https://img.youtube.com/vi/"
                                                                + data.getString("Post_Link").replace("https://youtu.be/", "").trim() + "/1.jpg";

                                                        PostLink_Url = "<a href='" + data.getString("Post_Link") + "'>" + data.getString("Post_Link") + "</a>";

                                                    } else {

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


                                        }

                                    }

                                    if (pd.isShowing()) {

                                        pd.dismiss();

                                    }

                                    set_data();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Trends_Comments.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }

    }

    private void set_data() {

        Glide.with(getApplicationContext()).load(User_Image)
                .override(100, 100)
                .into(user_img);

        Story_user_name.setText(Post_Ins_Name);
        time_agotxt.setText(Time_Ago);
        post_type.setText(Post_Category);

        if (!Post_Text.isEmpty()) {

            post_text.setVisibility(View.VISIBLE);
            post_text.setText(Post_Text);

        } else {

            post_text.setVisibility(View.GONE);

        }

        if (Cube_name.size() != 0) {

            smallIcon_adapter = new FeedAdapterSmallIcon(Trends_Comments.this, Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image);

            cube_recycler_view.setAdapter(smallIcon_adapter);

            smallIcon_adapter.notifyDataSetChanged();

        }

        if (File_Name.size() != 0) {

            multi_img_rv.setVisibility(View.VISIBLE);

            multi_image_adapter = new Post_MultiImage_Adapter(Trends_Comments.this, File_Name, multi_img_rv, multi_image_adapter, 1);
            multi_img_rv.setAdapter(multi_image_adapter);
            multi_image_adapter.notifyDataSetChanged();

        } else {

            multi_img_rv.setVisibility(View.GONE);

        }

        if (Post_Link!=null && Post_Link.length() > 1) {

            if (PostLink_Url.length() > 3) {

                if (PostLink_Title.length() > 2) {

                    link_design.setVisibility(View.VISIBLE);
                    any_link_design.setVisibility(View.GONE);

                    Glide.with(getApplicationContext())
                            .load(PostLink_Image)
                            .override(120, 120)
                            .into(link_img);

                    link_title.setText(PostLink_Title);

                    link_des.setText(PostLink_Description);

                    link_url.setText(Html.fromHtml(PostLink_Url));

                } else {

                    link_design.setVisibility(View.GONE);
                    any_link_design.setVisibility(View.VISIBLE);

                    any_link.setText(Html.fromHtml(PostLink_Url));

                }

            }

        } else {

            link_design.setVisibility(View.GONE);

        }


    }


    private class Get_Comments extends AsyncTask {

        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Trends_Comments.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }

        @Override
        protected Object doInBackground(Object[] objects) {


            RequestQueue queue = Volley.newRequestQueue(Trends_Comments.this);

            String url = Config.BASE_URL + "Trends/Trends_Comment_List/" + Post_Id;
            Log.i("Test", "doInBackground: " + url);
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

                                            Comment_Id = data.getString("_id");
                                            Comment_User_Id = data.getString("User_Id");
                                            Comment_User_Name = data.getString("User_Name");
                                            Comment_Text = data.getString("Comment_Text");
                                            Comment_Post_Id = data.getString("Trends_Id");
                                            Comment_User_Image = Config.Images + "Users/" + data.getString("User_Image");
                                            Comment_Time = data.getString("Time_Ago");

                                            comment_listList.add(new Comment_List(Comment_User_Id, Comment_User_Name, Comment_Text, Comment_Post_Id,
                                                    Comment_User_Image, Comment_Id, Comment_Time));

                                        }

                                        comment_list_view.setVisibility(View.VISIBLE);
                                        highlightsCommentAdapter = new PostCommentAdapter(Trends_Comments.this, comment_listList, comment_list_view);
                                        comment_list_view.setAdapter(highlightsCommentAdapter);
                                        highlightsCommentAdapter.setA(highlightsCommentAdapter);

                                    } else {



                                        nocommentstxt.setVisibility(View.VISIBLE);
                                        comment_list_view.setVisibility(View.GONE);
                                    }


                                    if (pd.isShowing()) {
                                        pd.dismiss();
                                    }


                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Trends_Comments.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);


            return null;
        }
    }

    public class PostComment extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Trends_Comments.this);
            emotelayout.setVisibility(View.GONE);
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            comment_list_view.setVisibility(View.VISIBLE);
            if (pd.isShowing()) {

                pd.dismiss();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {


            RequestQueue queue = Volley.newRequestQueue(Trends_Comments.this);

            String url = Config.BASE_URL + "Trends/Trends_Comment_Submit";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.i("Test", "onResponse: " + response);
                                JSONObject res = new JSONObject(response);
                                if (res.getString("Output").contains("True")) {

                                    JSONObject data = res.getJSONObject("Response");

                                    Comment_Id = data.getString("_id");
                                    Comment_User_Id = data.getString("User_Id");
                                    Comment_User_Name = data.getString("User_Name");
                                    Comment_Text = data.getString("Comment_Text");
                                    Comment_Post_Id = data.getString("Trends_Id");
                                    Comment_User_Image = Config.UserImages + data.getString("User_Image");
                                    Comment_Time = data.getString("Time_Ago");


                                    comment_listList.add(0, new Comment_List(Comment_User_Id, Comment_User_Name, Comment_Text, Comment_Post_Id,
                                            Comment_User_Image, Comment_Id, Comment_Time));

                                    comment_txt.setText("");

                                    comment_list_view.setVisibility(View.VISIBLE);
                                    nocommentstxt.setVisibility(View.GONE);
                                    highlightsCommentAdapter = new PostCommentAdapter(Trends_Comments.this, comment_listList, comment_list_view);
                                    comment_list_view.setAdapter(highlightsCommentAdapter);
                                    highlightsCommentAdapter.notifyDataSetChanged();

                                }


                            } catch (Exception e) {

                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Trends_Comments.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            })

            {
                protected Map<String, String> getParams() {

                    Map<String, String> para = new HashMap<String, String>();

                    para.put("User_Id", login_user_id);
                    para.put("Trends_Id", Post_Id);
                    para.put("Comment_Text", comment_txt.getText().toString());

                    return para;
                }
            };
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new

                    DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);


            return null;
        }
    }

    public class EmoteGridAdapter extends BaseAdapter {

        Context context;
        GridView emote_views;


        public EmoteGridAdapter(Context context, ArrayList<EmoteList> emote_name_list, GridView emote_view) {
            this.context = context;
            emoteList = emote_name_list;
            this.emote_views = emote_view;

        }

        public class ViewHolder {

            TextView emote_name, emote_count;
            LinearLayout emote_layout, lay;
            LayoutInflater layoutInflater;

        }

        @Override
        public int getCount() {
            return emoteList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            final EmoteGridAdapter.ViewHolder viewHolder;
            if (rowView == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.emote_grid_design, viewGroup, false);

                // configure view holder
                viewHolder = new EmoteGridAdapter.ViewHolder();

                viewHolder.emote_name = (TextView) rowView.findViewById(R.id.emote_name);

                viewHolder.emote_count = (TextView) rowView.findViewById(R.id.emote_count);

                viewHolder.emote_layout = (LinearLayout) rowView.findViewById(R.id.emote_layout);

                viewHolder.layoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                viewHolder.lay = (LinearLayout) rowView.findViewById(R.id.shrpopup);

                rowView.setTag(viewHolder);

            } else {

                viewHolder = (EmoteGridAdapter.ViewHolder) rowView.getTag();
            }


            viewHolder.emote_name.setText(emoteList.get(i).getEmote_Text());
            viewHolder.emote_count.setText(emoteList.get(i).getEmote_Count());


            if (emoteList.get(i).getEmote_User_Ids().contains(login_user_id)) {

                viewHolder.emote_layout.setBackground(context.getResources().getDrawable(R.drawable.emote_outline_design_selected));
                viewHolder.emote_layout.setEnabled(false);

            } else {

                viewHolder.emote_layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.emote_outline_design));
                viewHolder.emote_layout.setEnabled(true);
            }


            viewHolder.emote_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                    String u = Config.BASE_URL + "Trends/Trends_Emote_Update";

                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject json = new JSONObject(response);

                                if (json.getString("Output").contains("True")) {

                                    Log.d("count---", response);

                                    JSONObject data = json.getJSONObject("Response");

                                    viewHolder.emote_count.setText(data.getString("Count"));

                                    emoteList.get(i).setEmote_Count(data.getString("Count"));

                                    viewHolder.emote_layout.setBackground(context.getResources().getDrawable(R.drawable.emote_outline_design_selected));
                                    viewHolder.emote_layout.setEnabled(false);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Connection Error!!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        protected Map<String, String> getParams()
                        {
                            Map<String, String> para = new HashMap<String, String>();
                            Log.i("Test", "getParams: " + emoteList.get(i).toString());
                            para.put("Trends_Id", emoteList.get(i).getEmote_post_Id());
                            para.put("User_Id", login_user_id);
                            para.put("Emote_Id", emoteList.get(i).getEmote_Id());
                            return para;
                        }
                    };

                    MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                    MyRequestQueue.add(MyStringRequest);
                }
            });


            return rowView;
        }
    }

}



