package com.test.aashi.inscubenewbuild;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Fragment.MultipleFragment;
import GetterSetter.CubeCategoeyList;

public class Capture_Edit extends AppCompatActivity {

    String Post_Id, Post_User_Id, Post_Category, Post_Text, Post_Link, Post_Ins_Name, Post_User_Image, Time_Ago;
    ArrayList<String> Cube_Id = new ArrayList<>(), Cube_Cat_Id, Cube_name, Cube_Image, File_Name, File_Type, File_Status, All_File;
    MultipleFragment mul = new MultipleFragment();
    ArrayList<String> selected_file = new ArrayList<>(), selected_cubeId = new ArrayList<>();
    ArrayList<Uri> imagesUriList;
    JSONArray cube_ids, old_attachments;
    LinearLayout story_layout, news_layout, article_layout, idea_layout, curiosity_layout, talent_layout,
            question_layout, moments_layout, main, cam_img, gal_img, cancel_img, cam_vid, gal_vid, cancel_vid, topic_img_layout;
    Button hign_post_button;
    TextView story_layout_txt, news_layout_txt, article_layout_txt, idea_layout_txt, curiosity_layout_txt, talent_layout_txt,
            question_layout_txt, moments_layout_txt, txtPercentage, no_cubes;
    RecyclerView recyclerView, images_preview;
    EditText post_main_txt, post_high_link_edit;
    String post_text = "", selected_post_type = "", login_user_id, User_Image, Color_code, Ins_name, Cat_Id, Post_Cube_Id, Cube_Name,
            Post_Cube_Image, Link = "", imageURI;
    EditImageAdapter edit_image_adapter;
    SharedPreferences login;
    List<CubeCategoeyList> cube_list_data = new ArrayList<>();
    FeedAdapterSmallIcon adapter;
    ProgressBar progressBar;
    float totalSize;
    List<File> filelist = new ArrayList<>();
    ImageView post_pic, post_video, post_link;
    PopupWindow imgPopupWindoww, vidPopupWindoww, link_popup;
    JSONArray files;
    ImageView edit_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_edit);

        Post_Id = getIntent().getStringExtra("post_id");

        login = getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        User_Image = login.getString("UserImage", "");
        Color_code = login.getString("ColorCode", "");
        Ins_name = login.getString("UserName", "");

        new Get_Post().execute();
        new GetCubelist().execute();

        post_main_txt = (EditText) findViewById(R.id.post_main_txt);
        post_high_link_edit = (EditText) findViewById(R.id.post_high_link_edit);

        story_layout_txt = (TextView) findViewById(R.id.story_layout_txt);
        news_layout_txt = (TextView) findViewById(R.id.news_layout_txt);
        article_layout_txt = (TextView) findViewById(R.id.article_layout_txt);
        idea_layout_txt = (TextView) findViewById(R.id.idea_layout_txt);
        curiosity_layout_txt = (TextView) findViewById(R.id.curiosity_layout_txt);
        talent_layout_txt = (TextView) findViewById(R.id.talent_layout_txt);
        question_layout_txt = (TextView) findViewById(R.id.question_layout_txt);
        moments_layout_txt = (TextView) findViewById(R.id.moments_layout_txt);
        no_cubes = (TextView) findViewById(R.id.no_cubes);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        post_pic = (ImageView) findViewById(R.id.post_pic);
        post_video = (ImageView) findViewById(R.id.post_video);
        post_link = (ImageView) findViewById(R.id.post_file);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        edit_back = (ImageView) findViewById(R.id.edit_back);

        story_layout = (LinearLayout) findViewById(R.id.story_layout);
        news_layout = (LinearLayout) findViewById(R.id.news_layout);
        article_layout = (LinearLayout) findViewById(R.id.article_layout);
        idea_layout = (LinearLayout) findViewById(R.id.idea_layout);
        curiosity_layout = (LinearLayout) findViewById(R.id.curiosity_layout);
        talent_layout = (LinearLayout) findViewById(R.id.talent_layout);
        question_layout = (LinearLayout) findViewById(R.id.question_layout);
        moments_layout = (LinearLayout) findViewById(R.id.moments_layout);
        main = (LinearLayout) findViewById(R.id.main);
        topic_img_layout = (LinearLayout) findViewById(R.id.topic_img_layout);

        hign_post_button = (Button) findViewById(R.id.hign_post_button);

        recyclerView = (RecyclerView) findViewById(R.id.post_cube_list_multiple);
        images_preview = (RecyclerView) findViewById(R.id.images_preview);
        recyclerView.setLayoutManager(new LinearLayoutManager(Capture_Edit.this, LinearLayoutManager.HORIZONTAL, false));
        images_preview.setLayoutManager(new LinearLayoutManager(Capture_Edit.this, LinearLayoutManager.HORIZONTAL, false));

        story_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Story();

            }
        });
        news_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News();
            }
        });
        article_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article();
            }
        });
        idea_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Idea();
            }
        });
        curiosity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Curiosity();
            }
        });
        talent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Talent();
            }
        });
        question_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question();
            }
        });
        moments_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Moments();
            }
        });

        edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        hign_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cube_ids = new JSONArray();

                post_text = post_main_txt.getText().toString();

                for (int i = 0; i < selected_cubeId.size(); i++) {

                    cube_ids.put(selected_cubeId.get(i));

                }

                if (selected_cubeId.size() != 0 && !selected_post_type.isEmpty()) {

                    filelist.clear();

                    old_attachments = new JSONArray();

                    for (int j = 0; j < File_Name.size(); j++) {

                        old_attachments.put(File_Name.get(j));

                    }

                    for (int i = 0; i < selected_file.size(); i++) {

                        File f = new File(selected_file.get(i).toString());

                        filelist.add(f);

                    }

                    Link = post_high_link_edit.getText().toString();

                    new Highlight_Post().execute();

                } else if (selected_cubeId.size() == 0) {

                    Toast.makeText(getApplicationContext(), "Please select the cube", Toast.LENGTH_SHORT).show();

                } else if (selected_post_type.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Please select the post type", Toast.LENGTH_SHORT).show();

                }


            }
        });

        post_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View mView = inflater.inflate(R.layout.cam_gallery, null);

                imgPopupWindoww = new PopupWindow(
                        mView,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );


                cam_img = (LinearLayout) mView.findViewById(R.id.cam_img);
                gal_img = (LinearLayout) mView.findViewById(R.id.gal_img);
                cancel_img = (LinearLayout) mView.findViewById(R.id.cancel_img);

                cam_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1234);
                        imgPopupWindoww.dismiss();
                    }
                });

                gal_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1000);

                        imgPopupWindoww.dismiss();

                    }
                });

                cancel_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        imgPopupWindoww.dismiss();

                    }
                });


                imgPopupWindoww.setFocusable(true);

                imgPopupWindoww.setAnimationStyle(R.style.popup_window_animation_phone);

                imgPopupWindoww.showAtLocation(main, Gravity.CENTER, 0, 0);

                dimBehind(imgPopupWindoww);


            }
        });

        post_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View customView = inflater.inflate(R.layout.cam_gallery, null);

                vidPopupWindoww = new PopupWindow(
                        customView,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );

                cam_vid = (LinearLayout) customView.findViewById(R.id.cam_img);
                gal_vid = (LinearLayout) customView.findViewById(R.id.gal_img);
                cancel_vid = (LinearLayout) customView.findViewById(R.id.cancel_img);

                cam_vid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(intent, 1000);
                        vidPopupWindoww.dismiss();

                    }
                });

                gal_vid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), 1000);
                        vidPopupWindoww.dismiss();

                    }
                });

                cancel_vid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        vidPopupWindoww.dismiss();

                    }
                });


                vidPopupWindoww.setFocusable(true);
                vidPopupWindoww.setAnimationStyle(R.style.popup_window_animation_phone);
                vidPopupWindoww.showAtLocation(main, Gravity.CENTER, 0, 0);

                dimBehind(vidPopupWindoww);


            }
        });

        post_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                post_high_link_edit.setVisibility(View.VISIBLE);

                //  LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                //   final EditText post_high_link_edit;
                //   Button link_submitb;

//                final View cus = inflater.inflate(R.layout.link_get, null);
//
//                link_popup = new PopupWindow(
//                        cus,
//                        FrameLayout.LayoutParams.MATCH_PARENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT);
//
//                post_high_link_edit = (EditText) cus.findViewById(R.id.post_high_link_edit);
//
//                link_submitb = (Button) cus.findViewById(R.id.link_submitb);

//
//                link_submitb.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Link = post_high_link_edit.getText().toString();
//
//                        submitted_link.setVisibility(View.VISIBLE);
//                        submitted_link_txt.setVisibility(View.VISIBLE);
//                        submitted_link.setText(Link);
//
//
//                        link_popup.dismiss();
//
//                    }
//                });
//
//
//                link_popup.setFocusable(true);
//                link_popup.setAnimationStyle(R.style.popup_window_animation_phone);
//
//                link_popup.showAtLocation(main, Gravity.CENTER, 0, 0);
//
//                dimBehind(link_popup);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == 1000 && resultCode == RESULT_OK
                    && null != data) {

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

                    selected_file.add(getPath(Capture_Edit.this, mImageUri));
                    All_File.add(getPath(Capture_Edit.this, mImageUri));


                } else {

                    if (data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++)
                        {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            selected_file.add(getPath(Capture_Edit.this, uri));
                            All_File.add(getPath(Capture_Edit.this, uri));
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

            } else if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {

                imgPopupWindoww.dismiss();

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri selectedImage = getImageUri(Capture_Edit.this, photo);

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);


                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                String imgDecodableString = cursor.getString(columnIndex);

                cursor.close();

                selected_file.add(getPath(Capture_Edit.this, selectedImage));
                All_File.add(getPath(Capture_Edit.this, selectedImage));


            } else {
                Toast.makeText(Capture_Edit.this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }


            for (int j = 0; j < All_File.size(); j++) {

                File_Status.add("new");

            }

            topic_img_layout.setVisibility(View.VISIBLE);
            edit_image_adapter = new EditImageAdapter(Capture_Edit.this, All_File, File_Status, images_preview, edit_image_adapter, 0);
            edit_image_adapter.setAdapter(edit_image_adapter);
            images_preview.setAdapter(edit_image_adapter);
            edit_image_adapter.notifyDataSetChanged();


        } catch (Exception e) {

            Toast.makeText(Capture_Edit.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }


    }


    private class Highlight_Post extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressBar.setProgress(0);

            hign_post_button.setVisibility(View.GONE);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressBar.setProgress(values[0]);
            // updating percentage value
            txtPercentage.setVisibility(View.VISIBLE);
            txtPercentage.setText(String.valueOf(values[0]) + "%");

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject json = new JSONObject(s);

                if (json.getString("Output").toString().contains("True")) {

                    finish();

                    Intent in = new Intent(Capture_Edit.this, StoryPage.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override


        protected String doInBackground(Integer... integers) {


            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(Config.BASE_URL + "Capture/Cube_Capture_Update");

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

                entity.addPart("Old_Attachments", new StringBody(files.toString()));

                entity.addPart("User_Id", new StringBody(login_user_id));
                entity.addPart("Capture_Id", new StringBody(Post_Id));
                entity.addPart("Cubes_Id", new StringBody(cube_ids.toString()));
                entity.addPart("Capture_Text", new StringBody(post_text));
//                entity.addPart("Post_Category", new StringBody(selected_post_type));
//                entity.addPart("Post_Link", new StringBody(Link));

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

            } catch (ClientProtocolException e) {

                responseString = e.toString();

            } catch (IOException e) {

                responseString = e.toString();

            }

            return responseString;

        }
    }

    private class GetCubelist extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(Capture_Edit.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(Capture_Edit.this);

                final String url = Config.BASE_URL + "Cubes/User_Followed_Cubes/" + login_user_id;

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.toString().contains("True")) {

                                        try {

                                            if (response.getString("Output").contains("True")) {

                                                JSONArray ress = response.getJSONArray("Response");

                                                for (int i = 0; i < ress.length(); i++) {

                                                    JSONObject data = ress.getJSONObject(i);

                                                    Post_Cube_Id = data.getString("_id");
                                                    Cube_Name = data.getString("Name");
                                                    Post_Cube_Image = Config.Cube_Image + data.getString("Image");
                                                    Cat_Id = data.getString("Category_Id");


                                                    cube_list_data.add(new CubeCategoeyList(Cube_Name, "", Post_Cube_Id, Post_Cube_Image,
                                                            "", "", "", Cat_Id, "0"));

                                                }

                                            }

                                        } catch (Exception e) {

                                        }
                                    }

                                    if (Post_Cube_Id.length() != 0) {

                                        no_cubes.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter = new FeedAdapterSmallIcon(Capture_Edit.this, cube_list_data);
                                        recyclerView.setAdapter(adapter);


                                    } else {

                                        recyclerView.setVisibility(View.GONE);
                                        no_cubes.setVisibility(View.VISIBLE);

                                    }

                                    if (pd.isShowing()) {

                                        pd.dismiss();
                                    }

                                } catch (Exception e) {

                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                //  Log.d("req--", error.toString());
                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                queue.add(getRequest);

            } else {

                Toast.makeText(Capture_Edit.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

//    public class Following_cube extends RecyclerView.Adapter<Following_cube.ViewHolder> {
//
//        Context con;
//        List<CubeCategoeyList> list;
//        int a = 1;
//
//        public Following_cube(Context con, List<CubeCategoeyList> list) {
//            this.con = con;
//            this.list = list;
//        }
//
//        @NonNull
//        @Override
//        public Following_cube.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_cube_design, parent, false);
//
//            return new Following_cube.ViewHolder(v);
//
//        }
//
//
//        @Override
//        public void onBindViewHolder(@NonNull final Following_cube.ViewHolder holder, final int position) {
//
//
//            holder.cube_img.setImageBitmap(null);
//
//            Glide.with(con).load(list.get(position).getCube_image()).override(200, 200)
//                    .into(holder.cube_img);
//
//            holder.cube_name.setText(list.get(position).getCube_name());
//
//            holder.main.setBackgroundResource(list.get(position).isSelected() ? R.drawable.selection_square : R.drawable.unselection_square);
//
//            if (a == 1) {
//                for (int i = 0; i < Cube_Id.size(); i++) {
//
//                    for (int j = 0; j < list.size(); j++) {
//
//                        if (Cube_Id.get(i).equals(list.get(j).getCube_id())) {
//
//
//                            list.get(j).setSelected_position("1");
//                            list.get(j).setSelected(true);
//
//                            selected_cubeId.add(list.get(j).getCube_id());
//
//                            a = 0;
//                        }
//                    }
//                }
//            }
//
//            if (list.get(position).getSelected_position().equals("1")) {
//
//                holder.main.setBackgroundResource(list.get(position).isSelected() ? R.drawable.selection_square : R.drawable.unselection_square);
//
//            } else {
//
//                holder.view.setBackgroundColor(0);
//
//            }
//
//
//            holder.main.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    list.get(position).setSelected(!list.get(position).isSelected());
//
//                    //holder.view.setBackgroundResource(list.get(position).isSelected() ? R.drawable.selection_square : R.drawable.unselection_square);
//                    holder.main.setBackgroundResource(list.get(position).isSelected() ? R.drawable.selection_square : R.drawable.unselection_square);
//
//                    if (!list.get(position).isSelected()) {
//
//                        selected_cubeId.remove(list.get(position).getCube_id());
//
//                        // Toast.makeText(getActivity(), "removed", Toast.LENGTH_SHORT).show();
//
//                    } else {
//
//                        selected_cubeId.add(list.get(position).getCube_id());
//                        //Toast.makeText(getActivity(), "removed" + selected_cubeId.toString(), Toast.LENGTH_SHORT).show();
//
//                    }
//                    // Toast.makeText(getActivity(), " " + selected_cubeId.size(), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//
//            return list.size();
//
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            ImageView cube_img;
//            TextView cube_name;
//            LinearLayout main;
//            //LinearLayout select;
//            View view;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//
//                view = itemView;
//                cube_img = (ImageView) itemView.findViewById(R.id.cube_img);
//                cube_name = (TextView) itemView.findViewById(R.id.cube_name);
//                main = (LinearLayout) itemView.findViewById(R.id.main);
//                // select = (LinearLayout) itemView.findViewById(R.id.select);
//            }
//        }
//    }

    public class FeedAdapterSmallIcon extends RecyclerView.Adapter<FeedAdapterSmallIcon.ViewHolder> {


        Context con;
        List<CubeCategoeyList> list;
        int a = 1;

        public FeedAdapterSmallIcon(Context con, List<CubeCategoeyList> list) {
            this.con = con;
            this.list = list;


        }

        @NonNull
        @Override
        public FeedAdapterSmallIcon.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_cube_design, parent, false);

            return new FeedAdapterSmallIcon.ViewHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull final FeedAdapterSmallIcon.ViewHolder holder, final int position) {

            Glide.with(con).load(list.get(position).getCube_image()).override(50, 100)
                    .into(holder.cube_type_img);

            if (list.get(position).getCube_name().length() > 8) {
                holder.cube_name.setText(list.get(position).getCube_name().subSequence(0, 6) + "..");
            } else {
                holder.cube_name.setText(list.get(position).getCube_name());
            }

            if (a == 1) {
                for (int i = 0; i < Cube_Id.size(); i++) {

                    for (int j = 0; j < list.size(); j++) {

                        if (Cube_Id.get(i).equals(list.get(j).getCube_id())) {


                            list.get(j).setSelected_position("1");
                            list.get(j).setSelected(true);

                            selected_cubeId.add(list.get(j).getCube_id());

                            a = 0;
                        }
                    }
                }
            }

            if (list.get(position).getSelected_position().equals("1")) {

                holder.select.setBackgroundResource(list.get(position).isSelected() ? R.drawable.selection_square : R.drawable.unselection_square);

            } else {

                holder.view.setBackgroundColor(0);

            }


            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    list.get(position).setSelected(!list.get(position).isSelected());

                    //holder.view.setBackgroundResource(list.get(position).isSelected() ? R.drawable.selection_square : R.drawable.unselection_square);
                    holder.select.setBackgroundResource(list.get(position).isSelected() ? R.drawable.selection_square : R.drawable.unselection_square);

                    if (!list.get(position).isSelected()) {

                        selected_cubeId.remove(list.get(position).getCube_id());

                        // Toast.makeText(getActivity(), "removed", Toast.LENGTH_SHORT).show();

                    } else {

                        selected_cubeId.add(list.get(position).getCube_id());
                        //Toast.makeText(getActivity(), "removed" + selected_cubeId.toString(), Toast.LENGTH_SHORT).show();

                    }
                    // Toast.makeText(getActivity(), "
                }
            });
        }

        @Override
        public int getItemCount() {

            return list.size();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            PorterShapeImageView cube_type_img;
            TextView cube_name;
            LinearLayout main;
            RelativeLayout select;
            View view;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                cube_type_img = (PorterShapeImageView) itemView.findViewById(R.id.cube_img);
                cube_name = (TextView) itemView.findViewById(R.id.cube_name);
                main = (LinearLayout) itemView.findViewById(R.id.main);
                select = (RelativeLayout) itemView.findViewById(R.id.select);

            }

        }


    }

    private void set_data() {

        if (Post_Category.contains("Story")) {

            Story();

        } else if ((Post_Category.contains("News"))) {
            News();

        } else if ((Post_Category.contains("Article"))) {
            Article();

        } else if ((Post_Category.contains("Idea"))) {

            Idea();
        } else if ((Post_Category.contains("Curiosity"))) {

            Curiosity();
        } else if ((Post_Category.contains("Talent"))) {

            Talent();
        } else if ((Post_Category.contains("Question"))) {
            Question();

        } else if ((Post_Category.contains("Moments"))) {

            Moments();
        }

        post_main_txt.setText(Post_Text);

        topic_img_layout.setVisibility(View.VISIBLE);
        edit_image_adapter = new EditImageAdapter(Capture_Edit.this, All_File, File_Status, images_preview, edit_image_adapter, 0);
        edit_image_adapter.setAdapter(edit_image_adapter);
        images_preview.setAdapter(edit_image_adapter);
        edit_image_adapter.notifyDataSetChanged();


        if (!Post_Link.isEmpty()) {

            post_high_link_edit.setVisibility(View.VISIBLE);
            post_high_link_edit.setText(Post_Link);

        }


    }

    private class Get_Post extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Capture_Edit.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }

        @Override
        protected Object doInBackground(Object[] objects) {

            RequestQueue queue = Volley.newRequestQueue(Capture_Edit.this);

            String url = Config.BASE_URL + "Capture/Cube_Capture_View/" + login_user_id + "/" + Post_Id;

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
//                                            Post_Category = data.getString("Post_Category");
                                            Post_Text = data.getString("Capture_Text");
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
                                            } else {

                                                Cube_Id = new ArrayList<>();
                                                Cube_Cat_Id = new ArrayList<>();
                                                Cube_name = new ArrayList<>();
                                                Cube_Image = new ArrayList<>();

                                            }

                                            files = data.getJSONArray("Capture_Video");

                                            if (files.length() != 0) {

                                                File_Name = new ArrayList<>();
                                                All_File = new ArrayList<>();
                                                File_Type = new ArrayList<>();
                                                File_Status = new ArrayList<>();

                                                for (int j = 0; j < files.length(); j++) {

                                                    JSONObject file_data = files.getJSONObject(j);

                                                    File_Name.add(Config.BASE_URL + "Uploads/Post_Attachments/" + file_data.getString("File_Name"));
                                                    All_File.add(Config.BASE_URL + "Uploads/Post_Attachments/" + file_data.getString("File_Name"));
                                                    File_Type.add(file_data.getString("File_Type"));
                                                    File_Status.add("old");
                                                }

                                            } else {

                                                File_Name = new ArrayList<>();
                                                File_Type = new ArrayList<>();
                                                All_File = new ArrayList<>();
                                                File_Status = new ArrayList<>();
                                            }


                                            Post_Link = data.getString("Post_Link");


                                        }

                                    }

                                    if (pd.isShowing()) {

                                        pd.dismiss();

                                    }

//                                    set_data();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Capture_Edit.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }

    }

    public class EditImageAdapter extends RecyclerView.Adapter<EditImageAdapter.ViewHolder> {

        ArrayList<String> img_name, img_status;
        public Context context;
        EditImageAdapter adapter;
        RecyclerView rv;
        MultipleFragment mul = new MultipleFragment();
        int a;

        public void setAdapter(EditImageAdapter adapter) {

            this.adapter = adapter;

        }


        public EditImageAdapter(Context context, ArrayList<String> img_name, ArrayList<String> img_status, RecyclerView rv, EditImageAdapter adapter, int a) {
            super();

            this.context = context;
            this.img_name = img_name;
            this.img_status = img_status;
            this.rv = rv;
            this.adapter = adapter;
            this.a = a;

        }

        @Override
        public EditImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.multi_imt_design, viewGroup, false);
            EditImageAdapter.ViewHolder viewHolder = new EditImageAdapter.ViewHolder(v);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final EditImageAdapter.ViewHolder viewHolder, final int i) {

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
                        .override(200, 200)
                        .into(viewHolder.img);

            } else if (img_name.get(i).toString().contains(".mp4") || img_name.get(i).toString().contains(".3gp") || img_name.get(i).toString().contains(".flv")
                    || img_name.get(i).toString().contains(".mkv")) {

                viewHolder.img.setVisibility(View.GONE);

                viewHolder.vid.setVisibility(View.VISIBLE);
                viewHolder.vid_play_button.setVisibility(View.VISIBLE);

                // viewHolder.mc = new MediaController(context);

                // viewHolder.vid.setMediaController(viewHolder.mc);

                //viewHolder.u = Uri.parse(img_name.get(i).toString());

//            viewHolder.vid_play_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    viewHolder.vid.setVideoURI(viewHolder.u);
//                    viewHolder.vid.start();
//
//                    viewHolder.vid_play_button.setVisibility(View.GONE);
//                    viewHolder.mc.hide();
//
//                }
//            });


            }

            viewHolder.remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (img_status.get(i).equals("new")) {

                        selected_file.remove(img_name.get(i));
                        img_name.remove(img_name.get(i).toString());

                    } else if (img_status.get(i).equals("old")) {

                        File_Name.remove(img_name.get(i));
                        File_Type.remove(img_name.get(i));
                        files.remove(i);
                        img_name.remove(img_name.get(i).toString());
                    }

                    adapter.notifyDataSetChanged();


                }
            });

            mul.set(selected_file);

        }

        @Override
        public int getItemCount() {


            return img_name.size();


        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView img, vid_play_button, remove_button;
            VideoView vid;
            MediaController mc;
            Uri u;

            public ViewHolder(View itemView) {
                super(itemView);

                vid_play_button = (ImageView) itemView.findViewById(R.id.vid_play_button);
                vid = (VideoView) itemView.findViewById(R.id.vid);
                img = (ImageView) itemView.findViewById(R.id.img);
                remove_button = (ImageView) itemView.findViewById(R.id.remove_button);

            }
        }


    }


    public void Story() {

        selected_post_type = "Story";

        story_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
        news_layout.setBackgroundColor(0);
        article_layout.setBackgroundColor(0);
        idea_layout.setBackgroundColor(0);
        curiosity_layout.setBackgroundColor(0);
        talent_layout.setBackgroundColor(0);
        question_layout.setBackgroundColor(0);
        moments_layout.setBackgroundColor(0);

        post_main_txt.setHint("Share a story");

    }

    public void News() {

        selected_post_type = "News";

        story_layout.setBackgroundColor(0);
        news_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
        article_layout.setBackgroundColor(0);
        idea_layout.setBackgroundColor(0);
        curiosity_layout.setBackgroundColor(0);
        talent_layout.setBackgroundColor(0);
        question_layout.setBackgroundColor(0);
        moments_layout.setBackgroundColor(0);

        post_main_txt.setHint("Share an announcement or news");
    }

    public void Article() {


        selected_post_type = "Article/Blog";

        story_layout.setBackgroundColor(0);
        news_layout.setBackgroundColor(0);
        article_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
        idea_layout.setBackgroundColor(0);
        curiosity_layout.setBackgroundColor(0);
        talent_layout.setBackgroundColor(0);
        question_layout.setBackgroundColor(0);
        moments_layout.setBackgroundColor(0);

        post_main_txt.setHint("Write an article or blog");


    }

    public void Idea() {

        selected_post_type = "Idea";

        story_layout.setBackgroundColor(0);
        news_layout.setBackgroundColor(0);
        article_layout.setBackgroundColor(0);
        idea_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
        curiosity_layout.setBackgroundColor(0);
        talent_layout.setBackgroundColor(0);
        question_layout.setBackgroundColor(0);
        moments_layout.setBackgroundColor(0);

        post_main_txt.setHint("Share an Idea");


    }

    public void Curiosity() {

        selected_post_type = "Curiosity";

        story_layout.setBackgroundColor(0);
        news_layout.setBackgroundColor(0);
        article_layout.setBackgroundColor(0);
        idea_layout.setBackgroundColor(0);
        curiosity_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
        talent_layout.setBackgroundColor(0);
        question_layout.setBackgroundColor(0);
        moments_layout.setBackgroundColor(0);

        post_main_txt.setHint("What if…?");


    }

    public void Talent() {

        selected_post_type = "Talent";

        story_layout.setBackgroundColor(0);
        news_layout.setBackgroundColor(0);
        article_layout.setBackgroundColor(0);
        idea_layout.setBackgroundColor(0);
        curiosity_layout.setBackgroundColor(0);
        talent_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
        question_layout.setBackgroundColor(0);
        moments_layout.setBackgroundColor(0);

        post_main_txt.setHint("Express your talents");


    }

    public void Question() {

        selected_post_type = "Question";

        story_layout.setBackgroundColor(0);
        news_layout.setBackgroundColor(0);
        article_layout.setBackgroundColor(0);
        idea_layout.setBackgroundColor(0);
        curiosity_layout.setBackgroundColor(0);
        talent_layout.setBackgroundColor(0);
        question_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
        moments_layout.setBackgroundColor(0);

        post_main_txt.setHint("Ask a question");


    }

    public void Moments() {

        selected_post_type = "Moments";

        story_layout.setBackgroundColor(0);
        news_layout.setBackgroundColor(0);
        article_layout.setBackgroundColor(0);
        idea_layout.setBackgroundColor(0);
        curiosity_layout.setBackgroundColor(0);
        talent_layout.setBackgroundColor(0);
        question_layout.setBackgroundColor(0);
        moments_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));

        post_main_txt.setHint("Share what’s happening");

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);

    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

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


}
