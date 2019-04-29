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
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

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
import java.util.List;

import Adapter.Multi_image_adapter;
import Adapter.Post_MultiImage_Adapter;
import Adapter.Topic_Adapter;
import Fragment.MultipleFragment;
import GetterSetter.TopicList;

public class Topic_edit extends AppCompatActivity {
    EditImageAdapter editImageAdapter;
SharedPreferences  login;
    ArrayList<File> filelist;
    String Cube_Id, Color_code, imageURI, Add_topic_name = "", Add_topic_description = "";
    String Cube_Name = "", Cube_User_Id = "", Cube_Type = "", Position = "", Cube_Code = "", Cube_members, Cube_Image, Cube_type, Cube_Description, Cube_location, Cube_mail, Cube_Contact, Cube_Web, Cube_Catname,
            Topic_name = "", Topic_Description = "", Topic_File = "", Topic_FileType = "";
    ArrayList<String> file_name, file_type, selected_file, filee = new ArrayList<>(), cube_members = new ArrayList<>();
    ArrayList<String> File_Name, File_Type,File_Status,All_File;
    String topic_id, topic_cube_id, topic_user_id;
    Button add_topic, addmedia_button, addtopic_button, cancel;
    EditText topic_name_txt, topic_des_txt;
    LinearLayout button_layout, topic_img_layout;
    RecyclerView topic_img_list;
    ProgressBar progressBar;
    Multi_image_adapter image_adapter;
    String login_user_id;
    String cube_id,topic_ids;
    List<TopicList> topicLists;
    ArrayList<Uri> imagesUriList;
    JSONArray files,attach,old_attachments;
    float totalSize;
    Topic_Adapter topic_adapter;
    TextView CubeName, CubeMembers, CubeCat, CubeDescription, NoTopic, Location, Mail, Web, Contact,
            txtPercentage, cubeid, cube_userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_edit);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(params);
        selected_file=new ArrayList<>();
        filelist =new ArrayList<>();
        File_Status =new ArrayList<>();
        All_File=new ArrayList<>();
        new mytpoic().execute(this);
        login = getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        cube_id =getIntent().getStringExtra("cubeid");
        topic_ids=getIntent().getStringExtra("topicid");
        Log.i("cubeid",topic_ids);
        topic_name_txt = (EditText) findViewById(R.id.topic_name_txt);
        topic_des_txt = (EditText) findViewById(R.id.topic_des_txt);
        txtPercentage = (TextView)findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        topic_img_list = (RecyclerView)findViewById(R.id.topic_img_list);
        button_layout = (LinearLayout) findViewById(R.id.button_layout);
        topic_img_layout = (LinearLayout)findViewById(R.id.topic_img_layout);
        topic_img_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addmedia_button = (Button) findViewById(R.id.addmedia_button);
        addtopic_button = (Button) findViewById(R.id.addtopic_button);
        cancel = (Button) findViewById(R.id.cancel);
        addmedia_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                     Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                         intent.setType("*/*");
                                       startActivityForResult(Intent.createChooser(intent,
                                                "Select Picture")
                                        ,1000);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              finish();
            }
        });
        addtopic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_topic_name = topic_name_txt.getText().toString();
                Add_topic_description = topic_des_txt.getText().toString();
                old_attachments = new JSONArray();

                for (int j = 0; j < File_Name.size(); j++) {

                    old_attachments.put(File_Name.get(j));

                }

                for (int i = 0; i < selected_file.size(); i++) {

                    File f = new File(selected_file.get(i).toString());
                    filelist.add(f);

                }

                if (!Add_topic_description.isEmpty() && !Add_topic_name.isEmpty())
                {

                   new Highlight_Post().execute();

                } else if (Add_topic_description.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Enter Topic Description", Toast.LENGTH_SHORT).show();

                } else if (Add_topic_name.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Enter Topic Name", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }

    private class Highlight_Post extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressBar.setProgress(0);

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

                if (json.getString("Output").contains("True")) {

                    finish();
                    Intent in = new Intent(Topic_edit.this, CubeView.class);
                    in.putExtra("Cube_Id",topic_cube_id);
                    in.putExtra("Cube_User_Id",Cube_User_Id);
                    in.putExtra("Cube_Type",Cube_Type);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

            HttpPost httppost = new HttpPost(Config.BASE_URL + "Cubes/Update_Cube_Topic");

            try {

                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener()
                        {
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

                entity.addPart("Old_Attachments", new StringBody(attach.toString()));

                entity.addPart("User_Id", new StringBody(login_user_id));
                entity.addPart("Topic_Id", new StringBody(topic_ids));
                entity.addPart("Cube_Id", new StringBody(topic_cube_id));
                entity.addPart("Name", new StringBody(Add_topic_name));
                entity.addPart("Description", new StringBody(Add_topic_description));
               // entity.addPart("Old_Attachments", new StringBody();

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
    public class mytpoic extends AsyncTask
    {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {


                RequestQueue queue = Volley.newRequestQueue(Topic_edit.this);

                final String url = Config.BASE_URL + "Cubes/View_Cube/" + cube_id + "/" + login_user_id;

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.toString().contains("True"))
                                    {
                                        try {
                                            if
                                                (response.getString("Output").contains("True")) {
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
                                                    for (int j = 0; j < Topic.length(); j++)
                                                    {
                                                        JSONObject data = Topic.getJSONObject(j);
                                                        Topic_name = data.getString("Name");

                                                        Topic_Description = data.getString("Description");
                                                        topic_id = data.getString("_id");
                                                        Log.i("Topicname", topic_id );
                                                        if (topic_id.equals(topic_ids)) {
                                                            topic_name_txt.setText( Topic_name);
                                                            topic_cube_id = data.getString("Cube_Id");
                                                            topic_user_id = data.getString("User_Id");

                                                            attach = data.getJSONArray("Attachments");
                                                            filee = new ArrayList<>();
                                                            if (attach.length() != 0) {
                                                                for (int a = 0; a < attach.length(); a++) {
                                                                    JSONObject file = attach.getJSONObject(a);
                                                                    file_name = new ArrayList<>();
                                                                    file_type = new ArrayList<>();
                                                                    File_Type = new ArrayList<>();
                                                                    File_Name = new ArrayList<>();
                                                                    Topic_File = file.getString("File_Name");
                                                                    Topic_FileType = file.getString("File_Type");
                                                                    File_Type.add(file.getString("File_Type"));
                                                                    File_Name.add(Config.BASE_URL + "Uploads/Topic_Attachments/"
                                                                            + Topic_File);
                                                                    All_File.add(Config.BASE_URL + "Uploads/Topic_Attachments/"
                                                                            + Topic_File);
                                                                    file_type.add(Topic_FileType);
                                                                    filee.addAll(file_name);
                                                                    topic_des_txt.setText(Topic_Description);
                                                                    topic_img_layout.setVisibility(View.VISIBLE);
                                                                    editImageAdapter = new EditImageAdapter(
                                                                            Topic_edit.this,
                                                                            All_File, File_Status, topic_img_list,
                                                                            editImageAdapter,
                                                                            0);
                                                                    topic_img_list.setAdapter(editImageAdapter);
                                                                    File_Status.add("old");

                                                                }
                                                            } else {
                                                                File_Status = new ArrayList<>();
                                                                All_File = new ArrayList<>();
                                                                file_name = new ArrayList<>();
                                                                file_type = new ArrayList<>();
                                                            }
                                                        }
                                                    }
                                                } else {

                                                }

                                            }
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

    public void dimBehind(PopupWindow popupWindow)
    {
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked

            if (requestCode == 1000 && resultCode == RESULT_OK &&  null != data) {

                // Get the Image from data


                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                imagesUriList = new ArrayList<Uri>();

                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getApplicationContext(). getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageURI = cursor.getString(columnIndex);
                    cursor.close();

                    selected_file.add(getPath(getApplicationContext(), mImageUri));
                    All_File.add(getPath(getApplicationContext(),mImageUri));

                } else {

                    if (data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            selected_file.add(getPath(getApplicationContext(), uri));
                            All_File.add(getPath(getApplicationContext(),uri));
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


            }
            else
            {
                Toast.makeText(getApplicationContext(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();

            }
            for (int j = 0; j < All_File.size(); j++) {

                File_Status.add("new");

            }
            topic_img_layout.setVisibility(View.VISIBLE);
            editImageAdapter = new EditImageAdapter(Topic_edit.this, All_File, File_Status,
                    topic_img_list, editImageAdapter, 0);
            //     edit_image_adapter.setAdapter(edit_image_adapter);
            topic_img_list.setAdapter(editImageAdapter);
            editImageAdapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
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
                viewHolder.imageview.setVisibility(View.VISIBLE);
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(img_name.get(i),
                        MediaStore.Images.Thumbnails.MINI_KIND);

                BitmapDrawable bitmapDrawable = new BitmapDrawable(thumbnail);
                viewHolder.vid.setBackgroundDrawable(bitmapDrawable);

                try {
                    viewHolder.vid.setVisibility(View.GONE);

                /*Bitmap bitmap = retriveVideoFrameFromVideo(img_name.get(i).toString());
                viewHolder.vid_thumb.setImageBitmap(bitmap);
*/
                    new LoadImage(viewHolder.imageview , img_name.get(i).toString()).execute();

                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Bitmap bitmap = null;
                try {
//                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    bitmap   = ThumbnailUtils.createVideoThumbnail(img_name.get(i), MediaStore.Video.Thumbnails.MINI_KIND);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                viewHolder.img.setImageBitmap(bitmap);


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

                    if (img_status.get(i).equals("new"))
                    {

                        selected_file.remove(img_name.get(i));
                        img_name.remove(img_name.get(i).toString());

                    } else if (img_status.get(i).equals("old")) {

                        File_Name.remove(img_name.get(i));
                        File_Type.remove(img_name.get(i));
                        attach.remove(i);
                        img_name.remove(img_name.get(i).toString());

                    }

                    editImageAdapter .notifyDataSetChanged();

                }
            });

            mul.set(selected_file);

        }

        @Override
        public int getItemCount() {


            return img_name.size();


        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView img, vid_play_button, remove_button,imageview;
            VideoView vid;
            MediaController mc;
            Uri u;

            public ViewHolder(View itemView) {
                super(itemView);

                vid_play_button = (ImageView) itemView.findViewById(R.id.vid_play_button);
                vid = (VideoView) itemView.findViewById(R.id.vid);
                img = (ImageView) itemView.findViewById(R.id.img);
                remove_button = (ImageView) itemView.findViewById(R.id.remove_button);
                imageview=(ImageView)itemView.findViewById(R.id.vid1);

            }
        }


    }


}
