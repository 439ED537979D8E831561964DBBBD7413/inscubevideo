package Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.aashi.inscubenewbuild.Config;
import com.test.aashi.inscubenewbuild.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Adapter.CubeCapturePostAdapter;
import GetterSetter.EmoteList;
import GetterSetter.Highlights_PostList;

import static android.app.Activity.RESULT_OK;

public class CubeCaptureFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
 TabLayout tabLayout;
    RecyclerView highlight_posts;
    LinearLayout post_highlights;
    SharedPreferences login, refresh;
    TextView no_post_txt;
    List<Highlights_PostList> highlights_postLists = new ArrayList<>();
    CubeCapturePostAdapter highlightPostAdapter;
    String Ins_Name, login_user_Id, User_Email, User_Image, Color_Code;
    JSONObject PostLinkInfo;
    String Post_Id, Post_User_Id, Post_Category, Post_Text, Post_Link, Post_Ins_Name, Post_User_Image, Time_Ago, PostLink_Title = "",
            PostLink_Description = "", PostLink_Image = "", PostLink_Url = "", loader = "0";
    ArrayList<String> Cube_Id, Cube_Cat_Id, Cube_name, Cube_Image,
            File_Name, File_Type, Emote_Id, Emote_Text, Emote_Count, Emote_Ids;
    SwipeRefreshLayout refresh_layout;
    ArrayList<EmoteList> emoteLists;
    String ownername,sharedpost;



    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
  public   String CAMERA_PERMISSION = android.Manifest.permission.CAMERA;
   public String READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE;
     public String WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.capture_fragment2, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tabLayout=(TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.VISIBLE);
        login = getActivity().getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_Id = login.getString("_id", "");
        Ins_Name = login.getString("UserName", "");
        User_Email = login.getString("UserEmail", "");
        User_Image = login.getString("UserImage", "");
        Color_Code = login.getString("ColorCode", "");

        SharedPreferences.Editor refresh = getActivity().getSharedPreferences("refresh", Context.MODE_PRIVATE).edit();
        refresh.clear();
        refresh.apply();

        refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        post_highlights = (LinearLayout) view.findViewById(R.id.post_highlights);
        highlight_posts = (RecyclerView) view.findViewById(R.id.highlight_posts);

        no_post_txt = (TextView) view.findViewById(R.id.no_post_txt);

        highlight_posts.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        refresh_layout.post(new Runnable() {
                                @Override
                                public void run() {
                                    refresh_layout.setRefreshing(true);

                                    new Get_post().execute();

                                }
                            }
        );
        refresh_layout.setOnRefreshListener(this);

        post_highlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   getFragmentManager().beginTransaction().replace(R.id.transaction,new MultipleFragmentCapture()).addToBackStack(null).commit();

                checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, getActivity());


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Call your camera here.
                } else {
                    boolean showRationale1 = shouldShowRequestPermissionRationale(CAMERA_PERMISSION);
                    boolean showRationale2 = shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE_PERMISSION);
                    boolean showRationale3 = shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE_PERMISSION);
                    if (showRationale1 && showRationale2 && showRationale3) {

                    } else {

                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
            startActivityForResult(intent, 1000);

        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000)
            {
                FragmentManager manager = getFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction().addToBackStack("tag");
                Uri vidFile = data.getData();
                Bundle bundle = new Bundle();
                bundle.putString("data", vidFile.toString());
                TabLayout tab_layout;
                tab_layout=getActivity().findViewById(R.id.tab_layout);
                tab_layout.setVisibility(View.GONE);
                MultipleFragmentCapture fragobj = new MultipleFragmentCapture();
                fragobj.setArguments(bundle);
                //startActivity(intent);
                transaction.add(R.id.transaction,fragobj);
                transaction.commit();
            }
        }
    }

    @Override
    public void onRefresh()
    {

        new Get_post().execute();

    }


    @Override
    public void onResume() {
        super.onResume();

        refresh = getActivity().getSharedPreferences("refresh", Context.MODE_PRIVATE);

        if (refresh.getString("ref", "").contains("1")) {

            new Get_post().execute();
        }


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

            String url = Config.BASE_URL + "Capture/Cube_Capture_List/" + login_user_Id +"/0";
//            String url = Config.BASE_URL + "Capture/Cube_Capture_List/" + "5acc5d6e1295332c28f7e205/0";
//            String url = "http://206.189.92.174:4000/API/Capture/Cube_Capture_List/5acc5d6e1295332c28f7e205/0";
            Log.i("URL", "doInBackground: " + url);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.i("Test", "onResponse: " + response);
                                JSONObject res = new JSONObject(response);

                                if (res.getString("Output").contains("True")) {

                                    JSONArray respon = res.getJSONArray("Response");

                                    if (respon.length() != 0) {


                                        for (int i = 0; i < respon.length(); i++) {

                                            JSONObject data = respon.getJSONObject(i);

                                            Post_Id = data.getString("_id");
                                            Post_User_Id = data.getString("User_Id");
                                            Post_Category = data.getString("Shared_Capture");
                                            Post_Text = data.getString("Capture_Text");
                                            Post_Ins_Name = data.getString("User_Name");
                                            Post_User_Image = Config.Images + "Users/" + data.getString("User_Image");
                                            Time_Ago = data.getString("Time_Ago");
                                            sharedpost = data.getString("Shared_Capture");
                                            if (sharedpost.equals("True"))
                                            {
                                                ownername = data.getString("Capture_Owner_Name");
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
                                                            , emote_data.getString("Count"), emote_data.getString("Capture_Id"), Emote_Ids));

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
                                            if (Post_Link!=null && Post_Link.length() > 1) {

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

                                                } else
                                                    {

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
                                                    PostLink_Url,null,ownername));

                                        }

                                    } else {

                                        no_post_txt.setVisibility(View.VISIBLE);
                                        highlight_posts.setVisibility(View.GONE);

                                    }

                                    if (pd.isShowing()) {

                                        pd.dismiss();

                                    }
                                }

                                highlightPostAdapter = new CubeCapturePostAdapter(getActivity(), highlights_postLists);
                                highlight_posts.setAdapter(highlightPostAdapter);
                                highlightPostAdapter.notifyDataSetChanged();
                                highlightPostAdapter.setA(highlightPostAdapter);
                                refresh_layout.setRefreshing(false);
                                loader = "0";
                                if (highlights_postLists.size() > 3) {
                                    //  highlight_posts.smoothScrollToPosition(3);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getActivity(), "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }
    }

//  //  private String getTitile(String post_link) {
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//        String url = "https://www.youtube.com/oembed?url=" + Post_Link + "&format=json";
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//
//                            JSONObject res = new JSONObject(response);
//
//                            titile = res.getString("title");
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//            }
//        });
//        // Add the request to the RequestQueue.
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
//        queue.add(stringRequest);
//
//        return titile;
//    }

}
