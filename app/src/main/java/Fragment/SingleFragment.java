package Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.test.aashi.inscubenewbuild.AndroidMultiPartEntity;
import com.test.aashi.inscubenewbuild.Config;
import com.test.aashi.inscubenewbuild.CreateCube;
import com.test.aashi.inscubenewbuild.R;
import com.test.aashi.inscubenewbuild.SplashActivity;
import com.test.aashi.inscubenewbuild.StoryPage;

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

import Adapter.CreateCubeCategoryAdapter;
import Adapter.Multi_image_adapter;
import Adapter.PostCubeAdapterSingle;
import GetterSetter.Category_GridList;
import GetterSetter.CubeCategoeyList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AASHI on 03-04-2018.
 */

public class SingleFragment extends android.support.v4.app.Fragment {


    ArrayList<String> selected_file = new ArrayList<>(), selected_cubeId = new ArrayList<>();
    ArrayList<Uri> imagesUriList;
    JSONArray cube_ids;
    LinearLayout story_layout, news_layout, article_layout, idea_layout, curiosity_layout, talent_layout,
            question_layout, moments_layout, main, cam_img, gal_img, cancel_img, cam_vid, gal_vid, cancel_vid, topic_img_layout;
    Button hign_post_button;
    TextView story_layout_txt, news_layout_txt, article_layout_txt, idea_layout_txt, curiosity_layout_txt, talent_layout_txt,
            question_layout_txt, moments_layout_txt, txtPercentage, no_cubes, cat_name;
    RecyclerView recyclerView, images_preview;
    EditText post_main_txt;
    String post_text = "", selected_post_type = "", login_user_id, User_Image, Color_code, Ins_name, Cat_Id, Cube_Id, Cube_Name,
            Cube_Image, Link = "", imageURI;
    Multi_image_adapter multi_image_adapter;
    SharedPreferences login;
    List<CubeCategoeyList> cube_list_data = new ArrayList<>();
    Following_cubeSingle_Selection adapter;
    ProgressBar progressBar;
    float totalSize;
    List<File> filelist = new ArrayList<>();
    ImageView post_pic, post_video, post_link;
    PopupWindow imgPopupWindoww, vidPopupWindoww;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.multiplepost_fragment, container, false);

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        login = getActivity().getSharedPreferences("Logged_in_details", MODE_PRIVATE);

        login_user_id = login.getString("_id", "");
        User_Image = login.getString("UserImage", "");
        Color_code = login.getString("ColorCode", "");
        Ins_name = login.getString("UserName", "");


        post_main_txt = (EditText) view.findViewById(R.id.post_main_txt);

        story_layout_txt = (TextView) view.findViewById(R.id.story_layout_txt);
        news_layout_txt = (TextView) view.findViewById(R.id.news_layout_txt);
        article_layout_txt = (TextView) view.findViewById(R.id.article_layout_txt);
        idea_layout_txt = (TextView) view.findViewById(R.id.idea_layout_txt);
        curiosity_layout_txt = (TextView) view.findViewById(R.id.curiosity_layout_txt);
        talent_layout_txt = (TextView) view.findViewById(R.id.talent_layout_txt);
        question_layout_txt = (TextView) view.findViewById(R.id.question_layout_txt);
        moments_layout_txt = (TextView) view.findViewById(R.id.moments_layout_txt);
        no_cubes = (TextView) view.findViewById(R.id.no_cubes);
        txtPercentage = (TextView) view.findViewById(R.id.txtPercentage);
        cat_name = (TextView) view.findViewById(R.id.cat_name);

        post_pic = (ImageView) view.findViewById(R.id.post_pic);
        post_video = (ImageView) view.findViewById(R.id.post_video);
        post_link = (ImageView) view.findViewById(R.id.post_file);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        story_layout = (LinearLayout) view.findViewById(R.id.story_layout);
        news_layout = (LinearLayout) view.findViewById(R.id.news_layout);
        article_layout = (LinearLayout) view.findViewById(R.id.article_layout);
        idea_layout = (LinearLayout) view.findViewById(R.id.idea_layout);
        curiosity_layout = (LinearLayout) view.findViewById(R.id.curiosity_layout);
        talent_layout = (LinearLayout) view.findViewById(R.id.talent_layout);
        question_layout = (LinearLayout) view.findViewById(R.id.question_layout);
        moments_layout = (LinearLayout) view.findViewById(R.id.moments_layout);
        main = (LinearLayout) view.findViewById(R.id.main);
        topic_img_layout = (LinearLayout) view.findViewById(R.id.topic_img_layout);

        hign_post_button = (Button) view.findViewById(R.id.hign_post_button);

        recyclerView = (RecyclerView) view.findViewById(R.id.post_cube_list_multiple);
        images_preview = (RecyclerView) view.findViewById(R.id.images_preview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        images_preview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        new GetCubelist().execute();

        post_link.setVisibility(View.GONE);

        story_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
        news_layout.setBackgroundColor(0);
        article_layout.setBackgroundColor(0);
        idea_layout.setBackgroundColor(0);
        curiosity_layout.setBackgroundColor(0);
        talent_layout.setBackgroundColor(0);
        question_layout.setBackgroundColor(0);
        moments_layout.setBackgroundColor(0);
        selected_post_type = "Story";

        post_main_txt.setHint("Share a story");

        story_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        news_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        article_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        idea_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        });

        curiosity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        talent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected_post_type = "Talent";

                story_layout.setBackgroundColor(0);
                news_layout.setBackgroundColor(0);
                article_layout.setBackgroundColor(0);
                idea_layout.setBackgroundColor(0);
                curiosity_layout.setBackgroundColor(0);
                talent_layout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
                question_layout.setBackgroundColor(0);
                moments_layout.setBackgroundColor(0);

                post_main_txt.setHint("Express your talent");
            }
        });

        question_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        moments_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        hign_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cube_ids = new JSONArray();

                post_text = post_main_txt.getText().toString();

                for (int i = 0; i < selected_cubeId.size(); i++) {

                    cube_ids.put(selected_cubeId.get(i));

                }

                if (!post_text.isEmpty() && selected_cubeId.size() != 0 && !selected_post_type.isEmpty()) {

                    new Highlight_Post().execute();

                } else if (post_text.isEmpty()) {

                    Toast.makeText(getActivity(), "Please enter some text", Toast.LENGTH_SHORT).show();

                } else if (selected_cubeId.size() == 0) {

                    Toast.makeText(getActivity(), "Please select the cube", Toast.LENGTH_SHORT).show();

                } else if (selected_post_type.isEmpty()) {

                    Toast.makeText(getActivity(), "Please select the post type", Toast.LENGTH_SHORT).show();

                }


            }
        });

        post_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

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

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

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

                        reqcam();

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


            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                    Cursor cursor = getActivity().getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageURI = cursor.getString(columnIndex);
                    cursor.close();

                    selected_file.add(getPath(getActivity(), mImageUri));

                } else {

                    if (data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            selected_file.add(getPath(getActivity(), uri));
                            // Get the cursor
                            Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageURI = cursor.getString(columnIndex);

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                            //  profile_img.setImageBitmap(bitmap);
//                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//                            encodedImageList.add(encodedImage);

                            cursor.close();

                        }
                    }
                }

            } else if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {

                imgPopupWindoww.dismiss();

                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri selectedImage = getImageUri(getActivity(), photo);

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                String imgDecodableString = cursor.getString(columnIndex);

                cursor.close();

                selected_file.add(getPath(getActivity(), selectedImage));

            } else {

                Toast.makeText(getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();

            }

            topic_img_layout.setVisibility(View.VISIBLE);
            multi_image_adapter = new Multi_image_adapter(getActivity(), selected_file, images_preview, multi_image_adapter, 0);
            multi_image_adapter.setAdapter(multi_image_adapter);
            images_preview.setAdapter(multi_image_adapter);
            multi_image_adapter.notifyDataSetChanged();


        } catch (Exception e) {

            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

        set(selected_file);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);

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

                    getActivity().finish();

                    Intent in = new Intent(getActivity(), StoryPage.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

            HttpPost httppost = new HttpPost(Config.BASE_URL + "Posts/Cube_Post_Submit");

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
                entity.addPart("Cubes_Id", new StringBody(cube_ids.toString()));
                entity.addPart("Post_Text", new StringBody(post_text));
                entity.addPart("Post_Category", new StringBody(selected_post_type));
                entity.addPart("Post_Link", new StringBody(Link));

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

            } catch (
                    IOException e)

            {

                responseString = e.toString();

            }

            return responseString;


        }
    }


    private class GetCubelist extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(getActivity());

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

                                                    Cube_Id = data.getString("_id");
                                                    Cube_Name = data.getString("Name");
                                                    Cube_Image = Config.Cube_Image + data.getString("Image");
                                                    Cat_Id = data.getString("Category_Id");


                                                    cube_list_data.add(new CubeCategoeyList(Cube_Name, "", Cube_Id, Cube_Image, "",
                                                            "", "", Cat_Id, "0"));

                                                }

                                            }

                                        } catch (Exception e) {

                                        }
                                    }

                                    if (cube_list_data.size() != 0) {

                                        no_cubes.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter = new Following_cubeSingle_Selection(getActivity(), cube_list_data);
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

                Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

    void reqcam() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.CAMERA},
                        11);

            }
        } else {
        }

    }


    public class Following_cubeSingle_Selection extends RecyclerView.Adapter<Following_cubeSingle_Selection.ViewHolder> {


        Context con;
        List<CubeCategoeyList> list;

        private int lastSelectedPosition = -1;


        public Following_cubeSingle_Selection(Context con, List<CubeCategoeyList> list) {
            this.con = con;
            this.list = list;


        }

        @NonNull
        @Override
        public Following_cubeSingle_Selection.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_cube_design, parent, false);

            return new Following_cubeSingle_Selection.ViewHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull final Following_cubeSingle_Selection.ViewHolder holder, final int position) {

            Glide.with(con).load(list.get(position).getCube_image()).override(200, 200)
                    .into(holder.cube_img);

            holder.cube_name.setText(list.get(position).getCube_name());

            holder.view.setBackgroundColor(list.get(position).isSelected() ? getResources().getColor(R.color.Inscube_match) : Color.WHITE);

            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selected_cubeId.clear();

                    cat_name.setText(list.get(position).getCube_name());

                    selected_cubeId.add(list.get(position).getCube_id());


                }
            });


        }

        @Override
        public int getItemCount() {

            return list.size();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            ImageView cube_img;
            TextView cube_name;
            LinearLayout main;
            View view;

            public ViewHolder(View itemView) {
                super(itemView);

                view = itemView;
                cube_img = (ImageView) itemView.findViewById(R.id.cube_img);
                cube_name = (TextView) itemView.findViewById(R.id.cube_name);
                main = (LinearLayout) itemView.findViewById(R.id.main);

            }

        }

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


    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

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

    public void set(ArrayList<String> selected_file) {

        filelist.clear();

        for (int i = 0; i < selected_file.size(); i++) {

            File f = new File(selected_file.get(i).toString());

            filelist.add(f);

        }
//
//        if (filelist.size() == 0) {
//
//            images_preview.setVisibility(View.VISIBLE);
//        }

        // Log.d("check--", String.valueOf(filelist.size()));


    }
}
