package Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.aashi.inscubenewbuild.Config;
import com.test.aashi.inscubenewbuild.R;
import com.test.aashi.inscubenewbuild.StoryPage;
import com.test.aashi.inscubenewbuild.TrendsPost2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.TrendsPostAdapter;
import GetterSetter.EmoteList;
import GetterSetter.Highlights_PostList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Trends_filter extends android.app.Fragment implements SwipeRefreshLayout.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private FragmentActivity myContext;
    FrameLayout frameLayout;
    TextView textView;
    ImageView back;
    RecyclerView highlight_posts;
    TabLayout tabLayout;
    LinearLayout post_highlights;
    SharedPreferences login, refresh;
    TextView no_post_txt;
    List<Highlights_PostList> highlights_postLists = new ArrayList<>();
    TrendsPostAdapter highlightPostAdapter;
    String Ins_Name, login_user_Id, User_Email, User_Image, Color_Code;
    JSONObject PostLinkInfo;
    String Post_Id, Post_User_Id, Post_Category, Post_Text, Post_Link, Post_Ins_Name, Post_User_Image, Time_Ago, PostLink_Title = "",
            PostLink_Description = "", PostLink_Image = "", PostLink_Url = "", loader = "0";
    ArrayList<String> Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image,
            File_Name, File_Type, Emote_Id, Emote_Text, Emote_Count, Emote_Ids,Trends_text;
    SwipeRefreshLayout refresh_layout;
    ArrayList<EmoteList> emoteLists;
    String mytags,forview="";
    String sharedpost,ownername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_trends_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        forview=getArguments().getString("forview");
        login = getActivity().getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_Id = login.getString("_id", "");
        Ins_Name = login.getString("UserName", "");
        User_Email = login.getString("UserEmail", "");
        User_Image = login.getString("UserImage", "");
        Color_Code = login.getString("ColorCode", "");
        mytags = getArguments().getString("my_tags", "");



        SharedPreferences.Editor refresh = getActivity().getSharedPreferences("refresh", Context.MODE_PRIVATE).edit();
        refresh.clear();
        refresh.commit();


        refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout1);
        textView =view.findViewById(R.id.mytag);
        textView.setText(mytags);

      //  textView.setVisibility(View.GONE);

        //   post_highlights = (LinearLayout) view.findViewById(R.id.post_highlights);
        highlight_posts = (RecyclerView) view.findViewById(R.id.highlight_posts2);
       back=view.findViewById(R.id.back2);
        no_post_txt = (TextView) view.findViewById(R.id.no_post_txt1);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        highlight_posts.setLayoutManager(layoutManager);
        post_highlights=getActivity().findViewById(R.id.linearLayout);
        tabLayout=getActivity().findViewById(R.id.tab_layout);



        if(forview.equals("0"))
        {
           post_highlights.setVisibility(View.GONE);
           tabLayout.setVisibility(View.INVISIBLE);

            back.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().remove(Trends_filter.this).commit();
                    tabLayout.setVisibility(View.VISIBLE);
                    post_highlights.setVisibility(View.VISIBLE);
                }
            });

        }
        else if (forview.equals("1"))
        {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().remove(Trends_filter.this).commit();
                }
            });

        }



        //highlight_posts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        refresh_layout.post(new Runnable() {
                                @Override
                                public void run() {

                                    refresh_layout.setRefreshing(true);

                                    new Get_post().execute();

                                }
                            }
        );
        refresh_layout.setOnRefreshListener(this);


    }


    @Override
    public void onRefresh() {

        //loader = "1";

        new Get_post().execute();

    }
    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        refresh = getActivity().getSharedPreferences("refresh", Context.MODE_PRIVATE);

        if (refresh.getString("ref", "").contains("1")) {

            new Get_post().execute();

        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                           if (forview.equals("1"))
                           {
                               tabLayout=(TabLayout)getActivity().findViewById(R.id.tab_layout);
                               //tabLayout.setVisibility(View.VISIBLE);
                               post_highlights.setVisibility(View.VISIBLE);
                               getFragmentManager().beginTransaction().remove(Trends_filter.this).commit();
                           }
                           else if (forview.equals("0"))
                           {
                               tabLayout=(TabLayout)getActivity().findViewById(R.id.tab_layout);
                               tabLayout.setVisibility(View.VISIBLE);
                               post_highlights.setVisibility(View.VISIBLE);
                               getFragmentManager().beginTransaction().remove(Trends_filter.this).commit();
                           }


                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {

    }

    private class Get_post extends AsyncTask {
        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            highlights_postLists = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            final String url = Config.BASE_URL + "Trends/Cube_Trends_Filter";
            final String mytoken=mytags;

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("Test", "onResponse: " + response);

                                JSONObject json = new JSONObject(response);
                                if (json.getString("Status").contains("True")) {

                                    if (json.getString("Output").contains("True")) {


                                        JSONArray respon = json.getJSONArray("Response");
                                        Log.i("Test", "Response: " + respon);

                                        if (respon.length() > 0) {
                                       //     Toast.makeText(getActivity(),"success", Toast.LENGTH_SHORT).show();

                                            for (int i = 0; i < respon.length(); i++) {
                                                String trendTags = "";

                                                JSONObject data = respon.getJSONObject(i);

                                                JSONArray trendArray = data.optJSONArray("Trends_Tags");
                                                if (trendArray != null) {
                                                    for (int k = 0; k < trendArray.length(); k++) {
                                                        trendTags = trendTags + trendArray.get(k) + " ";
                                                    }
                                                }
                                                Trends_text=new ArrayList<>();

                                                if (trendArray != null) {
                                                    int len = trendArray.length();
                                                    for (int j=0;j<len;j++){
                                                        Trends_text.add(trendArray.get(j).toString().trim());
                                                    }
                                                }
                                                Post_Id = data.getString("_id");
                                                Post_User_Id = data.getString("User_Id");
                                                Post_Category = data.optString("Shared_Trends", "");
                                                Post_Text = data.optString("Trends_Text", "");
                                                Post_Ins_Name = data.getString("User_Name");
                                                Post_User_Image = Config.Images + "Users/" + data.getString("User_Image");
                                                Time_Ago = data.getString("Time_Ago");
                                                sharedpost = data.getString("Shared_Trends");
                                                if (sharedpost.equals("True"))
                                                {
                                                    ownername = data.getString("Trends_Owner_Name");
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
                                                } else {

                                                    Cube_Id = new ArrayList<>();
                                                    Cube_Cat_Id = new ArrayList<>();
                                                    Cube_name = new ArrayList<>();
                                                    Cube_Image = new ArrayList<>();

                                                }
                                                JSONArray files = null;


                                                if (data.has("Capture_Video")) {
                                                    files = data.getJSONArray("Capture_Video");
                                                }

                                                if (files != null && files.length() != 0) {

                                                    File_Name = new ArrayList<>();
                                                    File_Type = new ArrayList<>();

                                                    for (int j = 0; j < files.length(); j++) {

                                                        JSONObject file_data = files.getJSONObject(j);
                                                        File_Name.add(Config.BASE_URL + "Uploads/Capture_Attachments/" + file_data.getString("File_Name"));
                                                        File_Type.add(file_data.getString("File_Type"));

                                                    }

                                                } else {

                                                    File_Name = new ArrayList<>();
                                                    File_Type = new ArrayList<>();
                                                }

                                                JSONArray emote = null;
                                                if (data.has("Emotes")) {
                                                    emote = data.getJSONArray("Emotes");
                                                }

                                                if (emote != null && emote.length() != 0) {

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

                                                } else {

                                                    Emote_Id = new ArrayList<>();
                                                    Emote_Text = new ArrayList<>();
                                                    Emote_Count = new ArrayList<>();
                                                    emoteLists = new ArrayList<>();
                                                }

                                                if (data.has("Post_Link")) {
                                                    Post_Link = data.getString("Post_Link");
                                                }
                                                if (Post_Link != null && Post_Link.length() > 1) {

                                                    if (!Post_Link.contains("www.youtube.com") && !Post_Link.contains("youtu.be")) {

                                                        try {

                                                            PostLinkInfo = data.getJSONObject("Post_Link_Info");


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


                                                highlights_postLists.add(new Highlights_PostList(Post_Id, Post_User_Id, Post_Category,
                                                        Post_Text, Post_Link, Post_Ins_Name, Post_User_Image, Time_Ago,
                                                        Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image,
                                                        File_Name, File_Type, Emote_Id, Emote_Text, Emote_Count, emoteLists, Post_Link, PostLink_Title, PostLink_Description, PostLink_Image,
                                                        PostLink_Url, Trends_text,ownername));

                                            }

                                        } else {


                                            no_post_txt.setVisibility(View.VISIBLE);
                                            highlight_posts.setVisibility(View.GONE);

                                        }

                                        if (pd.isShowing()) {

                                            pd.dismiss();

                                        }
                                    }


                                }

                                highlightPostAdapter = new TrendsPostAdapter(getActivity(), highlights_postLists);
                                highlight_posts.setAdapter(highlightPostAdapter);
                                highlightPostAdapter.notifyDataSetChanged();
                                highlightPostAdapter.setA(highlightPostAdapter);
                                refresh_layout.setRefreshing(false);
                                loader = "0";
                                if (highlights_postLists.size() > 3) {
                                    //  highlight_posts.smoothScrollToPosition(3);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getActivity(), "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=utf-8";
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getParams();
                }
                protected Map<String, String> getParams()
                {
                    Map<String, String> para = new HashMap<String, String>();
                    para.put("User_Id", login_user_Id);
                    //para.put("Skip_Count","");
                    para.put("Trends_Tag",mytags.trim());
                    return para;
                }
            };
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);
            return null;
        }
    }
}