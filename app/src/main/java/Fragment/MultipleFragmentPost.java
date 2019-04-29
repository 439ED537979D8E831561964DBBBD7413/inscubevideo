package Fragment;

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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.libaml.android.view.chip.ChipLayout;
import com.test.aashi.inscubenewbuild.AndroidMultiPartEntity;
import com.test.aashi.inscubenewbuild.Config;
import com.test.aashi.inscubenewbuild.CubeCapture_Comments;
import com.test.aashi.inscubenewbuild.R;
import com.test.aashi.inscubenewbuild.StoryPage;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Adapter.HighlightsCommentAdapter;
import Adapter.Multi_image_adapter;
import Adapter.TrendsPostAdapter;
import GetterSetter.Comment_List;
import GetterSetter.CubeCategoeyList;
import GetterSetter.EmoteList;
import GetterSetter.Highlights_PostList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class MultipleFragmentPost extends Fragment {


    ArrayList<String> selected_file = new ArrayList<>(), selected_cubeId = new ArrayList<>();
    ArrayList<Uri> imagesUriList;
    JSONArray cube_ids;
    LinearLayout main, cam_vid, gal_vid, cancel_vid, cancel_img, cam_img, gal_img, topic_img_layout;

    Button hign_post_button;
    TextView story_layout_txt, news_layout_txt, article_layout_txt, idea_layout_txt, curiosity_layout_txt, talent_layout_txt,
            question_layout_txt, moments_layout_txt, txtPercentage, no_cubes, submitted_link_txt, submitted_link;
    RecyclerView recyclerView, images_preview;
    EditText post_story_hastext, post_high_link_edit;
    List<String> keydata = null;
    ChipLayout chip;
    String[] keyarray = {};
    ArrayAdapter<String> keyAdapter;

    String post_text = "", login_user_id, User_Image, Color_code, Ins_name, Cat_Id, Cube_Id, Cube_Name,
            Cube_Image, Link = "", imageURI;
    Multi_image_adapter multi_image_adapter;
    SharedPreferences login;
    List<CubeCategoeyList> cube_list_data = new ArrayList<>();
    FeedAdapterSmallIcon adapter;
    ProgressBar progressBar;
    float totalSize;
    List<File> filelist = new ArrayList<>();
    //    ImageView post_pic, post_video, post_link;
    PopupWindow imgPopupWindoww, emotePopupWindoww, link_popup;
    GridView emote_allgrid;

    TextView viewall;
    Button ok;
    List<CubeCategoeyList> grid_elements;
    DiscoverMyCubeAdapters adapters;
    private final String TAG = MultipleFragmentPost.class.getSimpleName();


    String[] trendKeys = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.multiplepost_fragment2, container, false);

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        login = getActivity().getSharedPreferences("Logged_in_details", MODE_PRIVATE);

        login_user_id = login.getString("_id", "");
        User_Image = login.getString("UserImage", "");
        Color_code = login.getString("ColorCode", "");
        Ins_name = login.getString("UserName", "");
//        post_main_txt = view.findViewById(R.id.post_main_txt);
        chip = view.findViewById(R.id.chipText);
        keyAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, keyarray);
         viewall=(TextView)view.findViewById(R.id.viewalla);
//        post_main_txt.setAdapter(keyAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, keydata);
        chip.setAdapter(adapter);
        chip.callOnClick();

        chip.addLayoutTextChangedListener(textWatcher);
                post_story_hastext = view.findViewById(R.id.post_story_hastext);
        post_high_link_edit = view.findViewById(R.id.post_high_link_edit);

        story_layout_txt = view.findViewById(R.id.story_layout_txt);
        news_layout_txt = view.findViewById(R.id.news_layout_txt);
        article_layout_txt = view.findViewById(R.id.article_layout_txt);
        idea_layout_txt = view.findViewById(R.id.idea_layout_txt);
        curiosity_layout_txt = view.findViewById(R.id.curiosity_layout_txt);
        talent_layout_txt = view.findViewById(R.id.talent_layout_txt);
        question_layout_txt = view.findViewById(R.id.question_layout_txt);
        moments_layout_txt = view.findViewById(R.id.moments_layout_txt);
        no_cubes = view.findViewById(R.id.no_cubes);
        txtPercentage = view.findViewById(R.id.txtPercentage);
        submitted_link_txt = view.findViewById(R.id.submitted_link_txt);
        submitted_link = view.findViewById(R.id.submitted_link);

/*        post_pic = (ImageView) view.findViewById(R.id.post_pic);
        post_video = (ImageView) view.findViewById(R.id.post_video);
        post_link = (ImageView) view.findViewById(R.id.post_file);*/
        topic_img_layout = view.findViewById(R.id.topic_img_layout);


        progressBar = view.findViewById(R.id.progressBar);

        main = view.findViewById(R.id.main);

        hign_post_button = view.findViewById(R.id.hign_post_button);

        recyclerView = view.findViewById(R.id.post_cube_list_multiple);
        images_preview = view.findViewById(R.id.images_preview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        images_preview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        new GetCubelist().execute();


        hign_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cube_ids = new JSONArray();

                Link = post_high_link_edit.getText().toString();

//                post_text = post_main_txt.getText().toString();

                for (int i = 0; i < selected_cubeId.size(); i++) {

                    cube_ids.put(selected_cubeId.get(i));

                }

                if (TextUtils.isEmpty(post_story_hastext.getText().toString())) {
                    Toast.makeText(getActivity(), "Please type your thoughts", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i(TAG, "onClick: " + chip.getText().toString());
                if (chip.getText()==null && chip.getText().size()==0) {
                    Toast.makeText(getActivity(), "Enter Hashtags", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (selected_cubeId.size() != 0) {

                    new Highlight_Post().execute();

                } else if (selected_cubeId.size() == 0) {

                    Toast.makeText(getActivity(), "Please select the cube", Toast.LENGTH_SHORT).show();

                } /*else if (selected_post_type.isEmpty()) {

                    Toast.makeText(getActivity(), "Please select the post type", Toast.LENGTH_SHORT).show();

                }*/


            }
        });
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater)getActivity(). getSystemService(LAYOUT_INFLATER_SERVICE);

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
                ok =(Button) customView.findViewById(R.id.ok);
                ok.setVisibility(View.VISIBLE);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emotePopupWindoww.dismiss();
                    }
                });
                emote_allgrid = (GridView) customView.findViewById(R.id.viewallmembers);
                adapters =new DiscoverMyCubeAdapters(cube_list_data,getContext());
                emote_allgrid.setAdapter(adapters);
            }
        });
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
            viewHolder.back.setVisibility(View.GONE);
            Glide.with(context).load(grid_elements.get(position).getCube_image()).override(200, 200)
                    .into(viewHolder.img);
            emote_allgrid.setNumColumns(4);
            emote_allgrid.setColumnWidth(20);

            if (grid_elements.get(position).getCube_name().length() > 8) {
                viewHolder.name.setText(grid_elements.get(position).getCube_name().subSequence(0, 6) + "..");
            } else {
                viewHolder.name.setText(grid_elements.get(position).getCube_name());
            }


//            holder.main.setOnClickListener(new View.OnClickListener() {
//                                               @Override
//                                               public void onClick(View v) {
//
//                                                   Intent intent = new Intent(con, CubeBasedPost.class);
//                                                   intent.putExtra("cube_id", Cube.get(position).getCube_id());
//                                                   con.startActivity(intent);
//                                               }
//                                           }
//            );
            viewHolder.cube_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    grid_elements.get(position).setSelected(!grid_elements.get(position).isSelected());

                    // holder.view.setBackgroundColor(list.get(position).isSelected() ? getResources().getColor(R.color.Inscube_match) : Color.WHITE);

                    viewHolder.select.setBackgroundResource(grid_elements.get(position).isSelected() ?
                            R.drawable.selection_square : R.drawable.unselection_square);

                    // holder.view.setBackgroundResource(R.drawable.selection_square);

                    if (!grid_elements.get(position).isSelected()) {

                        selected_cubeId.remove(grid_elements.get(position).getCube_id());

                        // Toast.makeText(getActivity(), "removed", Toast.LENGTH_SHORT).show();

                    } else {

                        selected_cubeId.add(grid_elements.get(position).getCube_id());
                        //Toast.makeText(getActivity(), "removed" + selected_cubeId.toString(), Toast.LENGTH_SHORT).show();

                    }
                    // Toast.makeText(getActivity(), " " + selected_cubeId.size(), Toast.LENGTH_SHORT).show();

                }
            });
            return rowView;
        }
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

                            cursor.close();

                        }
                    }
                }

            } else if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {

                imgPopupWindoww.dismiss();


                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri selectedImage = getImageUri(getActivity(), photo);

                // Toast.makeText(getActivity(), " " + selectedImage, Toast.LENGTH_SHORT).show();

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

                Toast.makeText(getActivity(), "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }

            if (selected_file.size() != 0) {

                topic_img_layout.setVisibility(View.VISIBLE);
                multi_image_adapter = new Multi_image_adapter(getActivity(), selected_file, images_preview, multi_image_adapter, 0);
                multi_image_adapter.setAdapter(multi_image_adapter);
                images_preview.setAdapter(multi_image_adapter);
                //multi_image_adapter.notifyDataSetChanged();

            }


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
        private String result;

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
            if (s != null) {

                try {

                    Log.i("Capture Info", "onPostExecute: " + s);
                    JSONObject json = new JSONObject(s);

                    if (json.getString("Output").toString().contains("True")) {

                        getActivity().finish();

                        SharedPreferences.Editor refresh = getActivity().getSharedPreferences("refresh", Context.MODE_PRIVATE)
                                .edit();
                        refresh.putString("ref", "1");
                        refresh.commit();

//                    Intent in = new Intent(getActivity(), StoryPage.class);
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(in);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "onPostExecute: " + "Null Response");
            }
        }
        @Override
        protected String doInBackground(Integer... integers)
        {

            final String[] responseString = {null};

            RequestQueue queue = Volley.newRequestQueue(getContext());

            String url = Config.BASE_URL + "Trends/Cube_Trends_Submit";
            Log.i("Test", "doInBackground: " + url);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.i("Test", "onResponse: " + response);
                            result = response;
                            try {

                                Log.i("Capture Info", "onPostExecute: " + response);
                                JSONObject json = new JSONObject(response);

                                if (json.getString("Output").toString().contains("True")) {

                                    getActivity().finish();

                                    SharedPreferences.Editor refresh = getActivity().getSharedPreferences("refresh", Context.MODE_PRIVATE)
                                            .edit();
                                    refresh.putString("ref", "1");
                                    refresh.commit();

//                    Intent in = new Intent(getActivity(), StoryPage.class);
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(in);

                                }

                            } catch (Exception e) {
                                ///  e.printStackTrace();
                                Log.e(TAG, "onResponse: Excption ", e);
                            }

                        }
                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {

//                    Toast.makeText(getContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                protected Map<String, String> getParams() {

                    Map<String, String> para = new HashMap<String, String>();

//                    String[] a = post_main_txt.getText().toString().split(" ");
                    JSONArray mJsonArray = new JSONArray();
                    for (int i = 0; i < chip.getText().size(); i++) {
//                        a[i] = "#" + a[i];
                        mJsonArray.put("#" + chip.getText().get(i).trim());
                    }

                    para.put("User_Id", login_user_id);
                    para.put("Cube_Ids", cube_ids.toString());
                    //         para.put("Tags", a.toString());
                    para.put("Tags", mJsonArray.toString());
                    //      para.put("Trends_Text", post_high_link_edit.getText().toString());
                    para.put("Trends_Text", post_story_hastext.getText().toString());

                    Log.d(TAG, "getParams: " + mJsonArray.toString());


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


    private class GetCubelist extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute()
        {
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
                                        viewall.setVisibility(View.VISIBLE);
                                        no_cubes.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter = new FeedAdapterSmallIcon(getActivity(), cube_list_data);
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

    public class FeedAdapterSmallIcon extends RecyclerView.Adapter<FeedAdapterSmallIcon.ViewHolder> {


        Context con;
        List<CubeCategoeyList> list;

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

//            holder.main.setOnClickListener(new View.OnClickListener() {
//                                               @Override
//                                               public void onClick(View v) {
//
//                                                   Intent intent = new Intent(con, CubeBasedPost.class);
//                                                   intent.putExtra("cube_id", Cube.get(position).getCube_id());
//                                                   con.startActivity(intent);
//                                               }
//                                           }
//            );
            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)

                {
                    list.get(position).setSelected(!list.get(position).isSelected());

                    // holder.view.setBackgroundColor(list.get(position).isSelected() ? getResources().getColor(R.color.Inscube_match) : Color.WHITE);

                    holder.select.setBackgroundResource(list.get(position).isSelected() ?
                            R.drawable.selection_square : R.drawable.unselection_square);

                    // holder.view.setBackgroundResource(R.drawable.selection_square);

                    if (!list.get(position).isSelected()) {

                        selected_cubeId.remove(list.get(position).getCube_id());

                        // Toast.makeText(getActivity(), "removed", Toast.LENGTH_SHORT).show();

                    } else {

                        selected_cubeId.add(list.get(position).getCube_id());


                    }

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

            public ViewHolder(View itemView) {
                super(itemView);

                cube_type_img = itemView.findViewById(R.id.cube_img);
                cube_name = itemView.findViewById(R.id.cube_name);
                main = itemView.findViewById(R.id.main);
                select = itemView.findViewById(R.id.select);

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

        filelist = new ArrayList<>();


        for (int i = 0; i < selected_file.size(); i++) {

            if (selected_file.get(i).toString().contains(".jpg") || selected_file.get(i).toString().contains(".gif") ||
                    selected_file.get(i).toString().contains(".png") || selected_file.get(i).toString().contains(".jpeg")) {

                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(selected_file.get(i).toString()).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {

                        File f = new File(outfile);
                        filelist.add(f);

                    }
                });

            } else {
                File f = new File(selected_file.get(i).toString());
                filelist.add(f);


            }

        }


    }


    private class Get_Keys extends AsyncTask<String, Object[], String> {
        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);


        }

        @Override
        protected void onPostExecute(String o) {
//            super.onPostExecute(o);
//            Log.i(TAG, "onPostExecute: " + o);
        }

        @Override
        protected String doInBackground(String[] strings) {

            final String[] returndata = {null};
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            String url = Config.BASE_URL + "Trends/Search_Trends_Tag/" + strings[0];
//            String url = Config.BASE_URL + "Trends/Search_Trends_Tag/1";

            Log.i("URL", "doInBackground: " + url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Log.i(TAG, "onResponse: " + response);
                                JSONObject res = new JSONObject(response);

                                if (res.getString("Output").contains("True")) {
                                    if (res.getJSONArray("Response") != null &&
                                            res.getJSONArray("Response").length() != 0) {
                                        JSONArray array = res.getJSONArray("Response");
                                        returndata[0] = res.getString("Response");

                                        keyarray = new String[array.length()];
                                        keydata = new ArrayList<>();
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            keyarray[i] = jsonObject.getString("Tag");
                                            keydata.add(jsonObject.getString("Tag"));
                                        }
                                        if (keyarray.length != 0) {

                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, keydata);
                                            chip.setAdapter(adapter);

//                                            chip.callOnClick();

                                            /*
                                            keyAdapter = new ArrayAdapter<String>(getContext(),
                                                    android.R.layout.simple_list_item_1, keyarray);
                                            post_main_txt.setAdapter(keyAdapter);
                                            post_main_txt.showDropDown();

                                            post_main_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                    String[] data = post_main_txt.getText().toString().split("#");
                                                    post_main_txt.removeTextChangedListener(textWatcher);
                                                    post_main_txt.setText("");
                                                    for (int i = 0; i < data.length - 1; i++) {
                                                        if (i == 0) {
                                                            post_main_txt.setText(data[i]);
                                                        } else {
                                                            post_main_txt.setText(post_main_txt.getText().toString() + "#" + data[i]);
                                                        }
                                                    }
                                                    post_main_txt.setText(post_main_txt.getText().toString() + keyarray[position] + " ");

                                                    post_main_txt.setSelection(post_main_txt.getText().length());
                                                    post_main_txt.addTextChangedListener(textWatcher);
                                                }
                                            });
*/
                                        }
                                    }

                                }
                            } catch (Exception e) {
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

            return returndata[0];
        }
    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                Log.i(TAG, "afterTextChanged: " + s.toString());
                if (s.toString().substring(s.toString().length() - 1).equalsIgnoreCase(" "))
                {

                    chip.removeLayoutTextChangedListener(textWatcher);

                    List<String> a = chip.getText();
//                    a.add(s.toString());
                    Log.i("Test", "afterTextChanged: " + a.toString());
                    chip.removeAllChips();
                    chip.setText(a);
                    Log.i("Test", "afterTextChanged: " + a.toString());
                    chip.callOnClick();

                    chip.addLayoutTextChangedListener(textWatcher);

                }
                else
                {
                    new Get_Keys().execute(s.toString());

                }


/*                String a = s.toString();
                if (a.substring(s.length() - 1).equalsIgnoreCase(" ")) {
                    post_main_txt.setText(s.toString() + "#");
                    post_main_txt.setSelection(post_main_txt.getText().length());
                } else {
                    try {
                        String[] data = post_main_txt.getText().toString().split("#");
                        String lastkeyword = data[data.length - 1];
                        if (lastkeyword != null && !lastkeyword.isEmpty() && !lastkeyword.equalsIgnoreCase(" ")) {
                            String[] myTaskParams = {lastkeyword};
                            new Get_Keys().execute(myTaskParams);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
