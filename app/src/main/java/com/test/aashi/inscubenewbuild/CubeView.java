package com.test.aashi.inscubenewbuild;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.HighlightPostAdapter;
import Adapter.ImageAdapter;
import Adapter.Multi_image_adapter;
import Adapter.Topic_Adapter;
import Fragment.MultipleFragment;
import GetterSetter.CreateCubeCatList;
import GetterSetter.Members;
import GetterSetter.TopicList;
import uk.co.jakelee.vidsta.VidstaPlayer;


public class CubeView extends AppCompatActivity
{
    View view;
    Button gto;
    RecyclerView topic_list;
    TextView CubeName, CubeMembers, CubeCat, CubeDescription, NoTopic, Location, Mail, Web, Contact, txtPercentage,cubeid,cube_userid;
    SharedPreferences login;
    String login_user_id, Cube_Id, Color_code, imageURI, Add_topic_name = "", Add_topic_description = "";
    String topic_id,topic_cube_id,topic_user_id;
    Button add_topic, follow_cube, addmedia_button, addtopic_button, cancel, mcancle, leave_cube, edit_cube, delete_cube;
    ArrayList<TopicList> topicLists = new ArrayList<>();
    String Cube_Name = "", Cube_User_Id = "", Cube_Type = "", Position = "", Cube_Code = "", Cube_members, Cube_Image, Cube_type, Cube_Description, Cube_location, Cube_mail, Cube_Contact, Cube_Web, Cube_Catname,
            Topic_name = "", Topic_Description = "", Topic_File = "", Topic_FileType = "",feeds ="";
    ArrayList<String> file_name, file_type, selected_file, filee = new ArrayList<>();
    ArrayList<Members> cube_members =new ArrayList<>();
    String membername,memberimage,memberid;
    MemberAdapter memberAdapter;
    GridView members,emote_allgrid;
    ArrayList<File> filelist;
    ArrayList<Uri> imagesUriList;
    ImageView CubeImage, CubeType;
    PopupWindow addtopic_popup, security_code_popup;
    RelativeLayout main;
    EditText topic_name_txt, topic_des_txt;
    RecyclerView topic_img_list;
    float totalSize;
    SharedPreferences cube;
    String cubecountry;
    TextView viewall;
    PopupWindow emotePopupWindoww;
   ;
  Multi_image_adapter multi_image_adapter;
    ProgressBar progressBar;
    LinearLayout button_layout, topic_img_layout, cube_info, cube_details, topic_lay, cube_button_layout, edit_cubelayout;
    Topic_Adapter topic_adapter;
    PopupWindow deletePopupWindoww;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_view);
        cube =getSharedPreferences("cube",MODE_PRIVATE);
        cubecountry = cube.getString("country","");

        view=(View)findViewById(R.id.invisible);
        cubeid=(TextView)findViewById(R.id.cube_id);
        cube_userid=(TextView)findViewById(R.id.cube_userid);
        Cube_Id = getIntent().getStringExtra("Cube_Id");
        Cube_User_Id=getIntent().getStringExtra("Cube_User_Id");
        feeds =getIntent().getStringExtra("Feeds");
        Cube_Type =getIntent().getStringExtra("Cube_Type");
        cubeid.setText(Cube_Id);
        cube_userid.setText(Cube_User_Id);
        gto=(Button)findViewById(R.id.gto);
        if(feeds .contains("1"))
        {
            gto.setText("Go to Feed");
        }

        gto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(feeds.contains("1"))
                {
                    Intent intent=new Intent(CubeView.this,StoryPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
             {
                 Intent intent=new Intent(CubeView.this,StoryPage.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
                 Toast.makeText(getApplicationContext(),"you can see your cube in settings",Toast.LENGTH_LONG).show();
             }

            }
        });


        new GetCubeDetails().execute();
        new Cube_MembersList().execute();


        selected_file = new ArrayList<>();
        filelist = new ArrayList<>();
        viewall =(TextView)findViewById(R.id.viewall);
        members=(GridView)findViewById(R.id.memberlist);
        login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        Color_code = login.getString("ColorCode", "");
        topic_list = (RecyclerView) findViewById(R.id.topic_rv);
        topic_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        add_topic = (Button) findViewById(R.id.add_topic);
        follow_cube = (Button) findViewById(R.id.follow_cube);
        mcancle = (Button) findViewById(R.id.mcancle);
        leave_cube = (Button) findViewById(R.id.leave_cube);
        edit_cube = (Button) findViewById(R.id.edit_cube);
        delete_cube = (Button) findViewById(R.id.delete_cube);
        CubeName = (TextView) findViewById(R.id.cube_name);
        CubeMembers = (TextView) findViewById(R.id.cube_members);
        CubeCat = (TextView) findViewById(R.id.cube_cat);
        CubeDescription = (TextView) findViewById(R.id.cube_descriptiontxt);
        NoTopic = (TextView) findViewById(R.id.no_topic_txt);
        Location = (TextView) findViewById(R.id.cube_locationtxt);
        Web = (TextView) findViewById(R.id.cube_linktxt);
        Contact = (TextView) findViewById(R.id.cube_contacttxt);
        Mail = (TextView) findViewById(R.id.cube_mailtxt);


        main = (RelativeLayout) findViewById(R.id.main);

     // follow_cube.setVisibility(View.GONE);
//        add_topic.setVisibility(View.VISIBLE);

        cube_info = (LinearLayout) findViewById(R.id.cube_info);
        cube_details = (LinearLayout) findViewById(R.id.cube_details);
        topic_lay = (LinearLayout) findViewById(R.id.topic_lay);
        cube_button_layout = (LinearLayout) findViewById(R.id.cube_button_layout);
        edit_cubelayout = (LinearLayout) findViewById(R.id.edit_cubelayout);


        CubeImage = (ImageView) findViewById(R.id.cube_img);
        CubeType = (ImageView) findViewById(R.id.cube_type);
       // NoTopic.setVisibility(View.GONE);



        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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

                memberAdapter = new MemberAdapter(cube_members,CubeView.this);

                emote_allgrid.setAdapter(memberAdapter);

            }
        });




        edit_cube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(CubeView.this, CreateCube.class);
                i.putExtra("cube_update", "1");
                i.putExtra("cube_id", Cube_Id);
                i.putExtra("cube_name", Cube_Name);
                i.putExtra("cube_image", Cube_Image);
                i.putExtra("cube_cat", Cube_Catname);
                i.putExtra("cube_type", Cube_type);
                i.putExtra("cube_code", Cube_Code);
                i.putExtra("cube_location", Cube_location);
                i.putExtra("cube_mail", Cube_mail);
                i.putExtra("cube_web", Cube_Web);
                i.putExtra("cube_contact", Cube_Contact);
                i.putExtra("cube_des", Cube_Description);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        delete_cube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button no, yes;
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View mView = inflater.inflate(R.layout.delete_conform, null);
                deletePopupWindoww = new PopupWindow(
                        mView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                no = (Button) mView.findViewById(R.id.no);
                yes = (Button) mView.findViewById(R.id.yes);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        final String url = Config.BASE_URL + "Cubes/Delete_Cube";
                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string.
                                        if (response.contains("True"))
                                        {
                                            try
                                            {

                                                JSONObject json = new JSONObject(response);

                                                if (json.getString("Output").contains("True")) {

                                                    if (json.getString("Message").contains("Success"))
                                                    {

                                                        deletePopupWindoww.dismiss();
                                                        Intent in = new Intent(CubeView.this, ProfileView.class);
                                                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(in);
                                                        finish();
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

                no.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {

                        deletePopupWindoww.dismiss();

                    }
                });

                deletePopupWindoww.setFocusable(true);

                deletePopupWindoww.setAnimationStyle(R.style.popup_window_animation_phone);

                deletePopupWindoww.showAtLocation(mView, Gravity.CENTER, 0, 0);

                dimBehind(deletePopupWindoww);

            }


        });

        leave_cube.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final String url = Config.BASE_URL + "Cubes/UnFollow_Cube";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                if (response.contains("True"))
                                {
                                    try {
                                        JSONObject json = new JSONObject(response);

                                        if (json.getString("Output").contains("True")) {

                                            // Toast.makeText(CubeView.this, "Leaved", Toast.LENGTH_SHORT).show();
                                            follow_cube.setVisibility(View.VISIBLE);
                                            delete_cube.setVisibility(View.GONE);
                                            leave_cube.setVisibility(View.GONE);

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

        follow_cube.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {


                if (Cube_Type.contains("Open"))
                {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    final String url = Config.BASE_URL + "Cubes/Follow_Cube";

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

                                                Toast.makeText(getApplicationContext(), "Joined",
                                                        Toast.LENGTH_SHORT).show();

                                                follow_cube.setVisibility(View.GONE);
                                                leave_cube.setVisibility(View.VISIBLE);
                                                delete_cube.setVisibility(View.GONE);


                                            } else {

                                                // success_value = 0;

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
                else
                {

                    final EditText popup_cube_code;
                    Button popup_joinb;

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                    final View customVie = inflater.inflate(R.layout.securitucode_layout_design, null);

                    security_code_popup = new PopupWindow(
                            customVie,
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );

                    popup_cube_code = (EditText) customVie.findViewById(R.id.popup_cube_code);
                    popup_joinb = (Button) customVie.findViewById(R.id.popup_joinb);

                    security_code_popup.setFocusable(true);
                    security_code_popup.setAnimationStyle(R.style.popup_window_animation_phone);
                    security_code_popup.setOutsideTouchable(true);
                    security_code_popup.showAtLocation(main, Gravity.CENTER, 0, 0);

                    dimBehind(security_code_popup);

                    popup_joinb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String cube_code;

                            cube_code = popup_cube_code.getText().toString();

                            if (cube_code.isEmpty()) {

                                Toast.makeText(getApplicationContext(), "Enter Cube Code", Toast.LENGTH_SHORT).show();

                            } else {

                                if (cube_code.equals(Cube_Code)) {

                                    RequestQueue queue = Volley.newRequestQueue(CubeView.this);

                                    final String url = Config.BASE_URL + "Cubes/Follow_Cube";

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

                                                                Toast.makeText(getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();

                                                                follow_cube.setVisibility(View.GONE);
                                                                delete_cube.setVisibility(View.GONE);
                                                                leave_cube.setVisibility(View.VISIBLE);

                                                            } else {

                                                                //  success_value = 0;

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

                                    security_code_popup.dismiss();

                                } else {

                                    Toast.makeText(getApplicationContext(), "Wrong Cube Code", Toast.LENGTH_SHORT).show();
                                    security_code_popup.dismiss();
                                }

                            }
                        }
                    });
                }
            }
        });

            mcancle.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v)
                {
                    if (mcancle.getText().toString().equals("Invite")) {
                        Intent in = new Intent(CubeView.this, Invite.class);
                        in.putExtra("Cube_Id", Cube_Id);
                        in.putExtra("Cube_User_Id", login_user_id);
                        in.putExtra("Name",Cube_Name);
                        in.putExtra("value", "1");
                        startActivity(in);
                    }
                    else
                    {
                        finish();
                    }
                }
            });
        add_topic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selected_file.clear();
                filelist.clear();
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View customVie = inflater.inflate(R.layout.addtopic_layout_design, null);
                addtopic_popup = new PopupWindow
                (
                        customVie,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );
                topic_name_txt = (EditText) customVie.findViewById(R.id.topic_name_txt);
                topic_des_txt = (EditText) customVie.findViewById(R.id.topic_des_txt);
                txtPercentage = (TextView) customVie.findViewById(R.id.txtPercentage);
                progressBar = (ProgressBar) customVie.findViewById(R.id.progressBar);
                topic_img_list = (RecyclerView) customVie.findViewById(R.id.topic_img_list);
                button_layout = (LinearLayout) customVie.findViewById(R.id.button_layout);
                topic_img_layout = (LinearLayout) customVie.findViewById(R.id.topic_img_layout);
                topic_img_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                addmedia_button = (Button) customVie.findViewById(R.id.addmedia_button);
                addtopic_button = (Button) customVie.findViewById(R.id.addtopic_button);
                cancel = (Button) customVie.findViewById(R.id.cancel);
                addtopic_popup.setFocusable(true);
                addtopic_popup.setAnimationStyle(R.style.popup_window_animation_phone);
                addtopic_popup.showAtLocation(main, Gravity.CENTER, 0, 0);
                dimBehind(addtopic_popup);
                addmedia_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1000);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        addtopic_popup.dismiss();
                        selected_file.clear();
                        filelist.clear();
                    }
                });
                addtopic_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Add_topic_name = topic_name_txt.getText().toString();
                        Add_topic_description = topic_des_txt.getText().toString();
                        if (!Add_topic_description.isEmpty() && !Add_topic_name.isEmpty()) {

                            new Add_Topic().execute();

                        } else if (Add_topic_description.isEmpty()) {

                            Toast.makeText(getApplicationContext(), "Enter Topic Description", Toast.LENGTH_SHORT).show();

                        } else if (Add_topic_name.isEmpty()) {

                            Toast.makeText(getApplicationContext(), "Enter Topic Name", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            // When an Image is picked
            if (requestCode == 1000 && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesUriList = new ArrayList<Uri>();

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageURI = cursor.getString(columnIndex);
                    cursor.close();

                    selected_file.add(getPath(getApplicationContext(), mImageUri));

                } else {

                    if (data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            selected_file.add(getPath(getApplicationContext(), uri));
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageURI = cursor.getString(columnIndex);

                            cursor.close();

                        }
                    }
                }
                topic_img_layout.setVisibility(View.VISIBLE);
                multi_image_adapter = new
                        Multi_image_adapter(getApplicationContext(),
                        selected_file, topic_img_list, multi_image_adapter, 1);
                topic_img_list.setAdapter(multi_image_adapter);
                multi_image_adapter.notifyDataSetChanged();

            }
            else
            {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        for (int i = 0; i < selected_file.size(); i++) {

            File f = new File(selected_file.get(i).toString());

            filelist.add(f);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private class GetCubeDetails extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute()
        {

            pd = new ProgressDialog(CubeView.this);
           pd.setMessage("Loading...");
           pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final String url = Config.BASE_URL + "Cubes/View_Cube/" + Cube_Id + "/" + login_user_id;

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.toString().contains("True")) {

                                        try {
                                            if (response.getString("Output").contains("True"))
                                            {
                                                JSONObject ress = response.getJSONObject("Response");
                                                Cube_Name = ress.getString("Name");
                                                Cube_User_Id = ress.getString("User_Id");
                                                Cube_members = ress.getString("Members");
                                                Cube_Image = Config.Cube_Image + ress.getString("Image");
                                                Cube_type = ress.getString("Security");
                                                Cube_Code = ress.getString("Security_Code");
                                                Cube_Description = ress.getString("Description");
                                                Cube_location = ress.getString("Country");
                                                Cube_mail = ress.getString("Mail");
                                                Cube_Contact = ress.getString("Contact");
                                                Cube_Web = ress.getString("Web");
                                                Cube_Catname = ress.getString("Category_Name");
                                                JSONArray Topic = ress.getJSONArray("Topics");
                                                if (Topic.length() != 0)
                                                {
                                                    for (int j = 0; j < Topic.length(); j++) {

                                                        JSONObject data = Topic.getJSONObject(j);

                                                        Topic_name = data.getString("Name");

                                                        Topic_Description = data.getString("Description");
                                                        topic_id =data.getString("_id");
                                                        topic_cube_id = data.getString("Cube_Id");
                                                        topic_user_id =data.getString("User_Id");

                                                        JSONArray attach = data.getJSONArray("Attachments");
                                                        filee = new ArrayList<>();
                                                        if (attach.length() != 0)
                                                        {
                                                            for (int a = 0; a < attach.length(); a++)
                                                            {
                                                                JSONObject file = attach.getJSONObject(a);
                                                                file_name = new ArrayList<>();
                                                                file_type = new ArrayList<>();
                                                                Topic_File = file.getString("File_Name");
                                                                Topic_FileType = file.getString("File_Type");
                                                                file_name.add(Config.BASE_URL + "Uploads/Topic_Attachments/"
                                                                        + Topic_File);
                                                                file_type.add(Topic_FileType);
                                                                filee.addAll(file_name);
                                                            }
                                                        }
                                                        else
                                                        {
                                                            file_name = new ArrayList<>();
                                                            file_type = new ArrayList<>();
                                                        }
                                                        topicLists.add(new TopicList(Topic_name,Topic_Description,topic_id,
                                                                topic_cube_id,topic_user_id,
                                                                filee, file_type));
                                                    }


                                                }
                                                else
                                                {

                                                    topic_list.setVisibility(View.GONE);
                                                    topic_lay.setVisibility(View.GONE);

                                                }
                                                SetData();
                                            }
                                            cube_info.setVisibility(View.VISIBLE);
                                            cube_details.setVisibility(View.VISIBLE);

                                            if (pd.isShowing()) {
                                                pd.dismiss();
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
                            public void onErrorResponse(VolleyError error)
                            {
                                //Log.d("req--", error.toString());
                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                queue.add(getRequest);


            }
            else {

                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    private void SetData()
    {
        float factor = getResources().getDisplayMetrics().density;
        CubeName.setText(Cube_Name);
        if (topicLists.size() != 0) {

            topic_lay.getLayoutParams().height = (int) (220 * factor);

            topic_lay.setVisibility(View.VISIBLE);
            topic_adapter = new Topic_Adapter(CubeView.this, topicLists,topic_adapter);
            topic_list.setAdapter(topic_adapter);
            topic_adapter.notifyDataSetChanged();

        }
        else
        {
            view.setVisibility(View.GONE);

            NoTopic.setVisibility(View.GONE);
            topic_lay.getLayoutParams().height = (int) (20 * factor);
            topic_lay.setVisibility(View.VISIBLE);

        }

        CubeCat.setText(Cube_Catname);

        CubeMembers.setText(Cube_members);

        CubeDescription.setText(Cube_Description);

        Glide.with(getApplicationContext())
                .load(Cube_Image).override(200, 200)
                .into(CubeImage);

        if (Cube_type.contains("Open")) {

            CubeType.setImageResource(R.drawable.globe);

        }
        else
        {
            CubeType.setImageResource(R.drawable.lock);
        }


        if (Cube_location.isEmpty()) {
            Location.setText("-");
        } else {
            Location.setText(cubecountry);
        }
        if (Cube_Web.isEmpty())
        {
            Web.setText("-");
        } else {
            Web.setText(Cube_Web);
        }
        if (Cube_mail.isEmpty()) {
            Mail.setText("-");
        } else {
            Mail.setText(Cube_mail);
        }
        if (Cube_Contact.isEmpty()) {
            Contact.setText("-");
        } else {
            Contact.setText(Cube_Contact);
        }

        main.setVisibility(View.VISIBLE);


    }

    private boolean isNetworkConnected()
    {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }


    private class Add_Topic extends AsyncTask<Integer, Integer, String> {
        @Override
        protected void onPreExecute() {

            progressBar.setProgress(0);

            button_layout.setVisibility(View.GONE);

        }
        @Override
        protected void onPostExecute(String s) {

            try {

                JSONObject ress = new JSONObject(s);

                if (ress.getString("Output").contains("True")) {

                    JSONObject res_data = ress.getJSONObject("Response");

                    Topic_name = res_data.getString("Name");

                    Topic_Description = res_data.getString("Description");
                    topic_user_id =res_data.getString("User_Id");
                    topic_id=res_data.getString("_id");
                    topic_cube_id=res_data.getString("Cube_Id");


                    JSONArray attach = res_data.getJSONArray("Attachments");

                    filee = new ArrayList<>();

                    if (attach.length() != 0) {

                        for (int a = 0; a < attach.length(); a++) {

                            JSONObject file = attach.getJSONObject(a);

                            file_name = new ArrayList<>();
                            file_type = new ArrayList<>();


                            Topic_File = file.getString("File_Name");
                            Topic_FileType = file.getString("File_Type");

                            file_name.add(Config.BASE_URL + "Uploads/Topic_Attachments/" + Topic_File);
                            file_type.add(Topic_FileType);

                            filee.addAll(file_name);
                        }

                    }
                    else {

                        file_name = new ArrayList<>();
                        file_type = new ArrayList<>();

                    }

                    topicLists.add(new TopicList(Topic_name, Topic_Description,topic_id,topic_cube_id,topic_user_id,
                            filee, file_type));
                    topic_list.setVisibility(View.VISIBLE);
                    NoTopic.setVisibility(View.GONE);
                    topic_adapter = new Topic_Adapter(CubeView.this, topicLists,topic_adapter);
                    topic_list.setAdapter(topic_adapter);
                    topic_adapter.notifyDataSetChanged();
                    float factor = getResources().getDisplayMetrics().density;
                    topic_lay.getLayoutParams().height = (int) (220 * factor);

                }


            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            addtopic_popup.dismiss();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(values[0]);

            // updating percentage value
            txtPercentage.setVisibility(View.VISIBLE);
            txtPercentage.setText(String.valueOf(values[0]) + "%");

        }

        @Override
        protected String doInBackground(Integer... integers) {


            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(Config.BASE_URL + "Cubes/Add_Cube_Topic");

            try {

                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (filelist.size() != 0) {

                    for (int i = 0; i < filelist.size(); i++) {

                        entity.addPart("attachments", new FileBody(filelist.get(i)));

                    }
                }

                entity.addPart("User_Id", new StringBody(login_user_id));
                entity.addPart("Cube_Id", new StringBody(Cube_Id));
                entity.addPart("Name", new StringBody(Add_topic_name));
                entity.addPart("Description", new StringBody(Add_topic_description));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);

                } else {

                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (
                    ClientProtocolException e)

            {

                responseString = e.toString();

            } catch (IOException e)

            {

                responseString = e.toString();

            }

            return responseString;


        }
    }

    private class Cube_MembersList extends AsyncTask
    {
        ProgressDialog pd = new ProgressDialog(CubeView.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading...");
            pd.show();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            if (isNetworkConnected()) {


                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                final String url = Config.BASE_URL + "Cubes/Cube_Members/" + Cube_Id;
                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.toString().contains("True")) {

                                        try {
                                            ArrayList<String> member =new ArrayList<>();

                    /*                        if (response.getString("Output").contains("True")) {

                                                JSONArray ress = response.getJSONArray("Response");

                                                for (int i = 0; i < ress.length(); i++) {
                                                    JSONObject data = ress.getJSONObject(i);
                                                    memberid = data.getString("_id");
                                                    memberimage =data.getString("Image");
                                                    membername =data.getString("Inscube_Name");
                                                    if (cube_members.size() != 0)
                                                    {
                                                        cube_members.add(new Members(membername,memberimage,memberid));
                                                        members.setVisibility(View.VISIBLE);

                                                    }
                                                    else
                                                    {
                                                        members.setVisibility(View.GONE);
                                                     cube_members=new ArrayList<>();
                                                    }
                                                    memberAdapter =new MemberAdapter(cube_members,CubeView.this);
                                                    members.setAdapter(memberAdapter);


                                                    */
                                            if (response.getString("Output").contains("True"))
                                            {

                                                JSONArray ress = response.getJSONArray("Response");

                                                for (int i = 0; i < ress.length(); i++) {
                                                    JSONObject data = ress.getJSONObject(i);
                                                    memberid = data.getString("_id");
                                                    memberimage =data.getString("Image");
                                                    membername =data.getString("Inscube_Name");
                                                    member.add(data.getString("_id"));

                                                    Log.e("MyId",memberid+membername+memberimage);
                                                    cube_members.add(new Members
                                                            (membername,Config.UserImages+ memberimage,memberid));
                                                    if (cube_members.size() != 0)
                                                    {
                                                        members.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        members.setVisibility(View.GONE);
                                                        cube_members=new ArrayList<>();
                                                    }
                                                    if (login_user_id.equals(Cube_User_Id))
                                                    {

                                                        add_topic.setVisibility(View.VISIBLE);
                                                        leave_cube.setVisibility(View.GONE);
                                                        follow_cube.setVisibility(View.GONE);
                                                        edit_cube.setVisibility(View.VISIBLE);
                                                        edit_cubelayout.setVisibility(View.VISIBLE);
                                                        delete_cube.setVisibility(View.VISIBLE);
                                                        mcancle.setText("Invite");



                                                    }
                                                    else
                                                    {
                                                        for (int j = 0; j < member.size(); j++) {


                                                            if (login_user_id.equals(member.get(j)))
                                                            {


                                                                add_topic.setVisibility(View.GONE);
                                                                leave_cube.setVisibility(View.VISIBLE);
                                                                follow_cube.setVisibility(View.GONE);
                                                                edit_cube.setVisibility(View.GONE);
                                                                edit_cubelayout.setVisibility(View.GONE);
                                                                delete_cube.setVisibility(View.GONE);
                                                                break;

                                                            }
                                                            else
                                                            {

                                                                leave_cube.setVisibility(View.GONE);
                                                                follow_cube.setVisibility(View.VISIBLE);
                                                                add_topic.setVisibility(View.GONE);
                                                                delete_cube.setVisibility(View.GONE);
                                                                edit_cube.setVisibility(View.GONE);
                                                                edit_cubelayout.setVisibility(View.GONE);


                                                            }
                                                        }
                                                    }

                                                    memberAdapter =new MemberAdapter(cube_members,CubeView.this);
                                                    members.setAdapter(memberAdapter);

                                                }
                                            }
                                            main.setVisibility(View.VISIBLE);
                                            cube_button_layout.setVisibility(View.VISIBLE);





                                        } catch (Exception e) {

                                        }
                                    }
                                }
                                catch (Exception e)
                                {

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


            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

    public class MemberAdapter extends BaseAdapter {


        class ViewHolder
        {
            PorterShapeImageView img;
            TextView name;
            ImageView imag,back;
            LinearLayout cube_category;
            RelativeLayout select;
        }
        public Context context;
        @Override

        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        public MemberAdapter (ArrayList<Members> apps, Context context) {
            cube_members = apps;
            this.context = context;
        }
        @Override
        public int getCount()
        {


            return cube_members.size();
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

            if (rowView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.cube_designs, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.imag =(ImageView)rowView.findViewById(R.id.unfollow);
                viewHolder.img = (PorterShapeImageView) rowView.findViewById(R.id.cube_img1);
                viewHolder.name = (TextView) rowView.findViewById(R.id.cube_name1);
                viewHolder.cube_category = (LinearLayout) rowView.findViewById(R.id.main1);
                viewHolder.select=(RelativeLayout)rowView.findViewById(R.id.select);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (cube_members.get(position).getName().length() < 10)
            {
                viewHolder.name.setText(cube_members.get(position).getName());
            }
          else
            {
                viewHolder.name.setText(cube_members.get(position).getName().subSequence(1,10)+"..");
            }

            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.imag.setVisibility(View.GONE);


            if (cube_members.size() <= 5)
            {
                viewall.setVisibility(View.GONE);

            }
            else
            {
                viewall.setVisibility(View.VISIBLE);
            }
            Log.i("Tes", "getView: " + cube_members.get(position).getImage());

            Glide.with(context).load(cube_members.get(position).getImage())
                    .override(200, 200)
//                    .placeholder(R.drawable.loading_spinner)
                    .into(viewHolder.img);
            viewHolder.cube_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {


                }
            });

            return rowView;
        }

    }
   public static class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        MultipleFragment mul = new MultipleFragment();
        ArrayList<String> img_name;
        public Context context;
        ImageAdapter adapter;
        RecyclerView rv;
        int a;


        public ImageAdapter(Context context, ArrayList<String> img_name, RecyclerView rv,
                            ImageAdapter adapter, int a) {
            super();
            this.context = context;
            this.img_name = img_name;

            this.rv = rv;
            this.adapter = adapter;
            this.a = a;
        }

        @NonNull
        @Override
        public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.multi_imt_design, parent, false);
         ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ImageAdapter. ViewHolder viewHolder, final int i) {
            if (a == 1) {

                viewHolder.remove_button.setVisibility(View.GONE);

            }


            if (img_name.get(i).toString().contains(".jpg") || img_name.get(i).toString().contains(".gif") || img_name.get(i).toString().contains(".jpeg")
                    || img_name.get(i).toString().contains(".png")) {

                viewHolder.img.setVisibility(View.VISIBLE);

                viewHolder.vid.setVisibility(View.GONE);
                viewHolder.vid_play_button.setVisibility(View.GONE);

                Glide.with(context)
                        .load(img_name.get(i))
                        .override(300, 300)
                        .placeholder(R.drawable.loader)
                        .into(viewHolder.img);

               viewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView imageView;

                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = layoutInflater.inflate(R.layout.test, viewHolder.imagecontainer);
                        //      layout.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        final PhotoView imageView1 = new PhotoView(context);
                        imageView=layout.findViewById(R.id.loadimage);
                        // Creating the PopupWindow
                        viewHolder. changeSortPopUp = new PopupWindow(context);
                        viewHolder. changeSortPopUp.setContentView(layout);
                        viewHolder. changeSortPopUp.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                        viewHolder. changeSortPopUp.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                        viewHolder. changeSortPopUp.setFocusable(true);
                        viewHolder.changeSortPopUp.setContentView(imageView1);
                        viewHolder.changeSortPopUp.setOutsideTouchable(true);
                        viewHolder.changeSortPopUp.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
                        int OFFSET_X = -20;
                        int OFFSET_Y = 95;

                        // Clear the default translucent background
                        viewHolder.  changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
                        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);

                        mAttacher.setZoomable(true);

                        // Displaying the popup at the specified location, + offsets.
                        viewHolder.changeSortPopUp.showAtLocation(layout, Gravity.CENTER,0,0);
                        Glide.with(context)
                                .load(img_name.get(i))
                                .placeholder(R.drawable.loader)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView1);
                        //      new AsyncTaskShowImage().execute(img_name.get(i));

                        //  zoomImageFromThumb(img_name.get(i),R.drawable.wall,viewHolder.expandedImageView,viewHolder.imagecontainer);


                    }
                });


            }
            else if (img_name.get(i).toString().contains(".mp4") ||
                    img_name.get(i).toString().contains(".3gp") || img_name.get(i).toString().contains(".flv")
                    || img_name.get(i).toString().contains(".mkv")) {

                viewHolder.vid_thumb.setVisibility(View.VISIBLE);

                viewHolder.img.setVisibility(View.GONE);
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(img_name.get(i).toString(),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                viewHolder.vid.setVisibility(View.GONE);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(thumbnail);
                viewHolder.vid.setBackgroundDrawable(bitmapDrawable);

                try {
                /*Bitmap bitmap = retriveVideoFrameFromVideo(img_name.get(i).toString());
                viewHolder.vid_thumb.setImageBitmap(bitmap);
*/
                    new LoadImage(viewHolder.vid_thumb, img_name.get(i).toString()).execute();

                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }


                viewHolder.vid_play_button.setVisibility(View.VISIBLE);

            }

            viewHolder.remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    img_name.remove(img_name.get(i).toString());

                    adapter.notifyDataSetChanged();

                    mul.set(img_name);

                }
            });
            viewHolder.vid_play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                    final View mView = inflater.inflate(R.layout.video, null);

                    viewHolder.vidPopupWindoww = new PopupWindow(
                            mView,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );

                    final VidstaPlayer player;
                    ImageView closeb;

                    player = (VidstaPlayer) mView.findViewById(R.id.vid);
                    closeb = (ImageView) mView.findViewById(R.id.closeb);

                    player.setVideoSource(img_name.get(i).toString());
                    player.setFullScreenButtonVisible(false);
                    closeb.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            player.stop();
                            viewHolder.vidPopupWindoww.dismiss();
                        }
                    });

                    viewHolder.vidPopupWindoww.setFocusable(true);

                    viewHolder.vidPopupWindoww.setAnimationStyle(R.style.popup_window_animation_phone);


                    viewHolder.vidPopupWindoww.showAtLocation(mView, Gravity.CENTER, 0, 0);

                    dimBehind(viewHolder.vidPopupWindoww);

                }
            });


        }


        @Override
        public int getItemCount() {
            return img_name.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView img, vid_play_button, remove_button, vid_thumb;
            VideoView vid;
            MediaController mc;
            Uri u;
            FrameLayout imagecontainer;
            PopupWindow changeSortPopUp;
            PopupWindow vidPopupWindoww;
            public ViewHolder(View itemView) {
                super(itemView);
                vid_thumb = (ImageView) itemView.findViewById(R.id.vid1);
                vid_play_button = (ImageView) itemView.findViewById(R.id.vid_play_button);
                vid = (VideoView) itemView.findViewById(R.id.vid);
                img = (ImageView) itemView.findViewById(R.id.img);
                remove_button = (ImageView) itemView.findViewById(R.id.remove_button);
                imagecontainer=itemView.findViewById(R.id.image_container);

            }
        }
    }
    public static void dimBehind(PopupWindow popupWindow) {
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
}
