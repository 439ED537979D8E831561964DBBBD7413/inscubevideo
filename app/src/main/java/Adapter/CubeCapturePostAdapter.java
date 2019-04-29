package Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.test.aashi.inscubenewbuild.Capture_Edit;
import com.test.aashi.inscubenewbuild.Config;
import com.test.aashi.inscubenewbuild.CubeCapture_Comments;
import com.test.aashi.inscubenewbuild.Highlight_Edit;
import com.test.aashi.inscubenewbuild.Highlights_Comments;
import com.test.aashi.inscubenewbuild.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GetterSetter.CreateCubeCatList;
import GetterSetter.EmoteList;
import GetterSetter.Highlights_PostList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by AASHI on 04-04-2018.
 */

public class CubeCapturePostAdapter extends RecyclerView.Adapter<CubeCapturePostAdapter.ViewHolder> {

    List<Highlights_PostList> list = new ArrayList<>();
    Context context;
    SharedPreferences login;
    String login_user_id, User_Image, Color_code, Ins_name;
    PopupWindow deletePopupWindoww, reportuser_popup, reportpost_popup, emote_popup, emotePopupWindoww;
    LinearLayout reportuser, reportpost, editt, delete, reportview, editview;
    ArrayList<EmoteList> emoteList = new ArrayList<>(), new_emote;
    EmoteGridAdapter emoteGridAdapter;
    CubeCapturePostAdapter a;
    String  result,inputLine;
    GridView cube_gridview,cube_follow;
    ArrayList<String> selectedids=new ArrayList<>();
    JSONArray cubeid;
    DiscoverMyCubeAdapters adapters;
   DiscoverMyCubeAdapter adapter;
    String post_id;
    public ArrayList<CreateCubeCatList> grid_element;
    public ArrayList<CreateCubeCatList> grid_elements;
    EmoteList currentEmote = null;

    public void setA(CubeCapturePostAdapter a) {
        this.a = a;
    }

    public CubeCapturePostAdapter(Context context, List<Highlights_PostList> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        login = context.getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        User_Image = login.getString("UserImage", "");
        Color_code = login.getString("ColorCode", "");
        Ins_name = login.getString("UserName", "");

        View view = LayoutInflater.from(context).inflate(R.layout.high_post_design, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        // holder.setIsRecyclable(true);

        holder.lines.setVisibility(View.GONE);
        holder.images.setVisibility(View.GONE);
        Log.i("Test", "onBindViewHolder: " + list.get(position).getPost_User_Image());
        Glide.with(context).load(list.get(position).getPost_User_Image())
                .override(100, 100)
                .into(holder.user_img);

        if (!list.get(position).getPost_Text().isEmpty()) {

            holder.post_text.setVisibility(View.VISIBLE);
            holder.post_text.setText(list.get(position).getPost_Text());

        } else
            {
            holder.post_text.setVisibility(View.GONE);
            }
        if (list.get(position).getOwnername().equals("1"))
        {
            holder.shared.setVisibility(View.GONE);
        }
        else  if (list.get(position).getOwnername().equals(list.get(position).getPost_Ins_Name()))
        {
            holder.shared.setText(" shared his own Capture");
        }
        else
        {
            if (list.get(position).getOwnername().length() >12)
            {
                holder.shared.setText(" shared "+ list.get(position).getOwnername().substring(0,12)+"..." + " Capture");
            }
            holder.shared.setText(" shared "+ list.get(position).getOwnername()+ "'s Capture");
        }
        holder.Story_user_name.setText(list.get(position).getPost_Ins_Name());

        if (list.get(position).getPost_Ins_Name().length() > 23) {
            holder.Story_user_name.setText(list.get(position).getPost_Ins_Name().subSequence(0, 21) + "...");
        } else {
            holder.Story_user_name.setText(list.get(position).getPost_Ins_Name());
        }

        holder.post_type.setText(list.get(position).getPost_Category());

        holder.time_agotxt.setText(list.get(position).getTime_Ago());

        holder.tags.setText(list.get(position).getTags());
      post_id=list.get(position).getPost_Id();
        if (list.get(position).getCube_name().size() != 0)
        {

            holder.smallIcon_adapter = new FeedAdapterSmallIcon(context, list.get(position).getCube_Id(),
                    list.get(position).getCube_Cat_Id(), list.get(position).getCube_name(),
                    list.get(position).getCube_Image());
            holder.cube_recycler_view.setAdapter(holder.smallIcon_adapter);
        }
        if (list != null && list.get(position).getFile_Name() != null && list.get(position).getFile_Name().size() != 0)
        {
            holder.multi_img_rv.setVisibility(View.VISIBLE);
            holder.multi_image_adapter = new Post_MultiImage_Adapter(context, list.get(position).getFile_Name(),
                    holder.multi_img_rv,
                    holder.multi_image_adapter, 1);
            holder.multi_img_rv.setAdapter(holder.multi_image_adapter);
        }
        else
        {
            holder.multi_img_rv.setVisibility(View.GONE);
        }
        if (list != null && list.get(position).getPost_Link() != null && list.get(position).getPost_Link().length() > 1)
        {

            if (list.get(position).getPostLink_Url().length() > 3) {

                if (list.get(position).getPostLink_Title().length() > 2) {

                    holder.link_design.setVisibility(View.VISIBLE);
                    holder.any_link_design.setVisibility(View.GONE);

                    Glide.with(context)
                            .load(list.get(position).getPostLink_Image())
                            .override(150, 150)
                            .error(R.drawable.erroe_loading)
                            .into(holder.link_img);

                    holder.link_title.setText(list.get(position).getPostLink_Title());

                    holder.link_des.setText(list.get(position).getPostLink_Description());

                    holder.link_url.setText(Html.fromHtml(list.get(position).getPostLink_Url()));

                }
                else
                {
                    holder.link_design.setVisibility(View.GONE);
                    holder.any_link_design.setVisibility(View.VISIBLE);
                    holder.any_link.setText(Html.fromHtml(list.get(position).getPostLink_Url()));
                }
            }
        }
        else
        {
            holder.link_design.setVisibility(View.GONE);

        }
        if (list != null && list.get(position).getEmoteLists() != null &&
                list.get(position).getEmoteLists().size() != 0) {

            holder.emote_view.setVisibility(View.VISIBLE);
            holder.emote_layout.setVisibility(View.VISIBLE);

            if (list.get(position).getEmoteLists().size() <= 4) {

                holder.view_all.setVisibility(View.GONE);

            } else {

                holder.view_all.setVisibility(View.VISIBLE);
            }

            if (list.get(position).getEmoteLists().size() <= 2) {

                float factor = holder.itemView.getContext().getResources().getDisplayMetrics().density;

                holder.emote_view.getLayoutParams().height = (int) (42 * factor);

            } else {

                float factor = holder.itemView.getContext().getResources().getDisplayMetrics().density;

                holder.emote_view.getLayoutParams().height = (int) (73 * factor);
            }

            emoteGridAdapter = new EmoteGridAdapter(context, list.get(position).getEmoteLists(), holder.emote_view, position);
            holder.emote_view.setAdapter(emoteGridAdapter);

        } else {

            holder.view_all.setVisibility(View.GONE);
            holder.emote_view.setVisibility(View.GONE);
            holder.emote_layout.setVisibility(View.GONE);

        }


        holder.more_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Point p;
                int[] location = new int[2];

                holder.more_option.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];

                View layout = holder.layoutInflater.inflate(R.layout.edit_popup, holder.viewGroup);

                // Creating the PopupWindow
                final PopupWindow popup = new PopupWindow(context);
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


                if (list.get(position).getPost_User_Id().equals(login_user_id)) {

                    reportview.setVisibility(View.GONE);
                    editview.setVisibility(View.VISIBLE);

                } else {

                    reportview.setVisibility(View.VISIBLE);
                    editview.setVisibility(View.GONE);

                }

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Button no, yes;

                        popup.dismiss();


                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

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

                                RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                                String u = Config.BASE_URL + "Capture/Cube_Capture_Delete/" + list.get(position).getPost_Id();

                                StringRequest MyStringRequest = new StringRequest(Request.Method.GET, u, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {

                                            JSONObject json = new JSONObject(response);

                                            //Toast.makeText(context, " " + response, Toast.LENGTH_SHORT).show();

                                            if (json.getString("Output").contains("True")) {

                                                Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();

                                                list.remove(position);

                                                a.notifyDataSetChanged();

                                                deletePopupWindoww.dismiss();

                                            }

                                        } catch (JSONException e) {

                                            e.printStackTrace();

                                        }
                                    }

                                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        // Toast.makeText(context, " " + error, Toast.LENGTH_SHORT).show();

                                        Toast.makeText(context, "Connection Error!!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                                MyRequestQueue.add(MyStringRequest);


                            }
                        });

                        no.setOnClickListener(new View.OnClickListener() {
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

                editt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popup.dismiss();


                        Intent edit = new Intent(context, Capture_Edit.class);
                        edit.putExtra("post_id", list.get(position).getPost_Id());
                        edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(edit);



                    }
                });


                reportpost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popup.dismiss();

                        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                        String u = Config.BASE_URL + "Capture/Report_Capture_Check";

                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject json = new JSONObject(response);

                                    if (json.getString("Available").contains("True")) {

                                        Button report_post_submit;

                                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                                        final View mView = inflater.inflate(R.layout.report_post_design, null);

                                        reportpost_popup = new PopupWindow(
                                                mView,
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );

                                        reportpost_popup.setFocusable(true);

                                        reportpost_popup.setAnimationStyle(R.style.popup_window_animation_phone);

                                        reportpost_popup.showAtLocation(mView, Gravity.CENTER, 0, 0);

                                        dimBehind(reportpost_popup);

                                        report_post_submit = (Button) mView.findViewById(R.id.reportpost_submit);

                                        report_post_submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final RadioGroup rg;
                                                final RadioButton rb;
                                                int selectedID;
                                                final EditText report_post_explain;


                                                rg = (RadioGroup) mView.findViewById(R.id.reportpost_radiogrp);
                                                report_post_explain = (EditText) mView.findViewById(R.id.reportpost_explain);
                                                selectedID = rg.getCheckedRadioButtonId();

                                                rb = (RadioButton) mView.findViewById(selectedID);

                                                RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                                                String u = Config.BASE_URL + "Capture/Report_Capture";

                                                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        try {

                                                            JSONObject json = new JSONObject(response);

                                                            // Toast.makeText(context, " "+json.toString(), Toast.LENGTH_SHORT).show();

                                                            if (json.getString("Output").contains("True")) {

                                                                Toast.makeText(context, "Caputre Reported", Toast.LENGTH_SHORT).show();

                                                                reportpost_popup.dismiss();

                                                            } else {

                                                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

                                                            }


                                                        } catch (JSONException e) {

                                                            e.printStackTrace();

                                                        }
                                                    }

                                                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                        // Toast.makeText(context, " " + error, Toast.LENGTH_SHORT).show();

                                                        Toast.makeText(context, "Connection Error!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                                    protected Map<String, String> getParams() {
                                                        Map<String, String> para = new HashMap<String, String>();

                                                        para.put("Post_Id", list.get(position).getPost_Id());
                                                        para.put("User_Id", login_user_id);
                                                        para.put("Report_Type", rb.getText().toString());
                                                        para.put("Report_Text", report_post_explain.getText().toString());

                                                        return para;
                                                    }
                                                };

                                                MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                                                MyRequestQueue.add(MyStringRequest);

                                            }
                                        });


                                    } else {

                                        Toast.makeText(context, "Already Reported", Toast.LENGTH_SHORT).show();

                                    }


                                } catch (JSONException e) {

                                    e.printStackTrace();

                                }
                            }

                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                // Toast.makeText(context, " " + error, Toast.LENGTH_SHORT).show();

                                Toast.makeText(context, "Connection Error!!", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> para = new HashMap<String, String>();

                                para.put("Post_Id", list.get(position).getPost_Id());
                                para.put("User_Id", login_user_id);


                                return para;
                            }
                        };

                        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(80000, 3, 2));

                        MyRequestQueue.add(MyStringRequest);


                    }
                });


                reportuser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popup.dismiss();

                        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                        String u = Config.BASE_URL + "Posts/Report_User_Check";

                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject json = new JSONObject(response);

                                    // Toast.makeText(context, " " + response, Toast.LENGTH_SHORT).show();

                                    if (json.getString("Available").contains("True")) {

                                        // Toast.makeText(context, " " + json.getString("Available"), Toast.LENGTH_SHORT).show();
                                        Button report_user_submit;

                                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                                        final View mView = inflater.inflate(R.layout.report_user_design, null);

                                        reportuser_popup = new PopupWindow(
                                                mView,
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );

                                        reportuser_popup.setFocusable(true);

                                        reportuser_popup.setAnimationStyle(R.style.popup_window_animation_phone);

                                        reportuser_popup.showAtLocation(mView, Gravity.CENTER, 0, 0);

                                        dimBehind(reportuser_popup);

                                        report_user_submit = (Button) mView.findViewById(R.id.reportuser_submit);

                                        report_user_submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final RadioGroup rg;
                                                final RadioButton rb;
                                                int selectedID;
                                                final EditText report_user_explain;


                                                rg = (RadioGroup) mView.findViewById(R.id.reportuser_radiogrp);
                                                report_user_explain = (EditText) mView.findViewById(R.id.reportuser_explain);
                                                selectedID = rg.getCheckedRadioButtonId();
                                                rb = (RadioButton) mView.findViewById(selectedID);

                                                RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                                                String u = Config.BASE_URL + "Posts/Report_User";

                                                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        try {

                                                            JSONObject json = new JSONObject(response);

                                                            if (json.getString("Output").contains("True")) {

                                                                Toast.makeText(context, "User Reported", Toast.LENGTH_SHORT).show();

                                                                reportuser_popup.dismiss();

                                                            } else {

                                                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

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
                                                    protected Map<String, String> getParams() {
                                                        Map<String, String> para = new HashMap<String, String>();

                                                        para.put("User_Id", login_user_id);
                                                        para.put("To_User_Id", list.get(position).getPost_User_Id());
                                                        para.put("Report_Type", rb.getText().toString());
                                                        para.put("Report_Text", report_user_explain.getText().toString());

                                                        return para;
                                                    }
                                                };
                                                MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                                                MyRequestQueue.add(MyStringRequest);
                                            }
                                        });
                                    } else {

                                        Toast.makeText(context, "Already Reported", Toast.LENGTH_SHORT).show();

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
                            protected Map<String, String> getParams() {

                                Map<String, String> para = new HashMap<String, String>();

                                para.put("To_User_Id", list.get(position).getPost_User_Id());
                                para.put("User_Id", login_user_id);

                                return para;
                            }
                        };

                        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                        MyRequestQueue.add(MyStringRequest);

                    }
                });

            }
        });


        holder.high_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent edit = new Intent(context, CubeCapture_Comments.class);
                edit.putExtra("post_id", list.get(position).getPost_Id());
                edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(edit);

            }
        });

        holder.high_emote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText emote_input;

                Button emote_submit;


                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

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

                            Toast.makeText(context, "Please Enter Something", Toast.LENGTH_SHORT).show();

                        } else {


                            InputMethodManager imm = (InputMethodManager) v.getContext()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                            String u = Config.BASE_URL + "Capture/Capture_Emote_Submit";

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

                                                Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show();

                                                JSONObject resdata = json.getJSONObject("Response");

                                                JSONArray res = resdata.getJSONArray("User_Ids");

                                                if (res != null) {

                                                    emote_user_ids = new ArrayList<>();

                                                    for (int k = 0; k < res.length(); k++) {

                                                        emote_user_ids.add(res.getString(k));


                                                    }

                                                }

                                                new_emote.addAll(list.get(position).getEmoteLists());

                                                new_emote.add(0, new EmoteList(resdata.getString("_id"), resdata.getString("Emote_Text")
                                                        , resdata.getString("Count"), resdata.getString
                                                        ("Capture_Id"), emote_user_ids));

                                                list.get(position).getEmoteLists().clear();

                                                list.get(position).setEmoteLists(new_emote);

                                                emoteGridAdapter = new EmoteGridAdapter(context, list.get(position).getEmoteLists(), holder.emote_view, position);

                                                holder.emote_view.setAdapter(emoteGridAdapter);

                                                holder.emote_view.setVisibility(View.VISIBLE);
                                                holder.emote_layout.setVisibility(View.VISIBLE);

                                            } catch (Exception e) {

                                            }

                                            if (list.get(position).getEmoteLists().size() <= 2) {

                                                float factor = holder.itemView.getContext().getResources().getDisplayMetrics().density;

                                                holder.emote_view.getLayoutParams().height = (int) (42 * factor);

                                            } else {

                                                float factor = holder.itemView.getContext().getResources().getDisplayMetrics().density;

                                                holder.emote_view.getLayoutParams().height = (int) (73 * factor);
                                            }

                                            if (list.get(position).getEmoteLists().size() > 4) {

                                                holder.view_all.setVisibility(View.VISIBLE);

                                            } else {

                                                holder.view_all.setVisibility(View.GONE);
                                            }

                                            emote_popup.dismiss();

                                        } else {

                                            Toast.makeText(context, "Already Present!", Toast.LENGTH_SHORT).show();

                                        }


                                    } catch (JSONException e)

                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, "Connection Error!!", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                protected Map<String, String> getParams() {

                                    Map<String, String> para = new HashMap<String, String>();

                                    para.put("Capture_Id", list.get(position).getPost_Id());
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
        holder.high_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                grid_element=new ArrayList<>();
                grid_elements=new ArrayList<>();


                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                final View mView = inflater.inflate(R.layout.custontab, null);

                reportuser_popup = new PopupWindow(
                        mView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );

                reportuser_popup.setFocusable(true);
                reportuser_popup.setAnimationStyle(R.style.popup_window_animation_phone);
                cube_gridview =mView.findViewById(R.id.share_cubes);
                cube_follow =mView.findViewById(R.id.share_cube);
                //    text=(TextView)mView.findViewById(R.id.text4);
                Button share_pos=(Button)mView.findViewById(R.id.share_post);
                share_pos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cubeid=new JSONArray();
                        for (int i = 0; i < selectedids.size(); i++) {

                            cubeid.put(selectedids.get(i));
                        }
                        if (selectedids.size() != 0) {

                            new SharePost().execute(this);

                        } else if (selectedids.size() == 0) {

                            Toast.makeText(context, "Please select the cube", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                new GetCategory().execute(this);
                new GetCategorys().execute(this);
                reportuser_popup.showAtLocation(mView, Gravity.CENTER, 0, 0);
             TextView   viewall=(TextView)mView.findViewById(R.id.viewa);
                viewall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {


                        LayoutInflater inflater = (LayoutInflater)context. getSystemService(LAYOUT_INFLATER_SERVICE);

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
                        Button ok   ;
                        ok =(Button) customView.findViewById(R.id.ok);
                        ok.setVisibility(View.VISIBLE);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                emotePopupWindoww.dismiss();
                            }
                        });
                        GridView emote_allgrid;
                        emote_allgrid = (GridView) customView.findViewById(R.id.viewallmembers);
                        adapter =new DiscoverMyCubeAdapter(grid_element,context);
                        emote_allgrid.setAdapter(adapter);


                    }
                });
                TextView view =(TextView)mView.findViewById(R.id.view_all1);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = (LayoutInflater)context. getSystemService(LAYOUT_INFLATER_SERVICE);

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
                        Button ok   ;
                        ok =(Button) customView.findViewById(R.id.ok);
                        ok.setVisibility(View.VISIBLE);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                emotePopupWindoww.dismiss();
                            }
                        });
                        GridView emote_allgrid;
                        emote_allgrid = (GridView) customView.findViewById(R.id.viewallmembers);
                        adapters =new DiscoverMyCubeAdapters(grid_elements,context);
                        emote_allgrid.setAdapter(adapters);
                    }
                });
            }
        });
        holder.view_all.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                GridView emote_allgrid;

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                final View customView = inflater.inflate(R.layout.emote_viewall, null);

                emotePopupWindoww = new PopupWindow(
                        customView,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );

                emotePopupWindoww.setFocusable(true);
                emotePopupWindoww.setAnimationStyle(R.style.popup_window_animation_phone);
                emotePopupWindoww.showAtLocation(customView, Gravity.CENTER, 0, 0);

                dimBehind(emotePopupWindoww);

                emote_allgrid = (GridView) customView.findViewById(R.id.emote_allgrid);

                emoteGridAdapter = new EmoteGridAdapter(context, list.get(position).getEmoteLists(), holder.emote_view, position);

                emote_allgrid.setAdapter(emoteGridAdapter);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView   shared, lines, Story_user_name, post_type, time_agotxt, view_all, post_text, any_link, link_des, link_url, link_title, tags;
        LayoutInflater layoutInflater;
        ImageView more_option, high_like, high_emote, high_comment, high_share, link_img;
        LinearLayout viewGroup, emote_layout, link_design, any_link_design;
        RecyclerView multi_img_rv, cube_recycler_view;
        GridView images;
        GridView emote_view;
        FeedAdapterSmallIcon smallIcon_adapter;
        Post_MultiImage_Adapter multi_image_adapter;

        CircularImageView user_img;


        public ViewHolder(View itemView) {
            super(itemView);

            shared =(TextView)itemView.findViewById(R.id.Story_share);
            lines =(TextView)itemView.findViewById(R.id.lines);
            Story_user_name = (TextView) itemView.findViewById(R.id.Story_user_name);
            time_agotxt = (TextView) itemView.findViewById(R.id.time_agotxt);
            view_all = (TextView) itemView.findViewById(R.id.view_all_post);
            post_type = (TextView) itemView.findViewById(R.id.post_type);
            post_text = (TextView) itemView.findViewById(R.id.post_text);
            any_link = (TextView) itemView.findViewById(R.id.any_link);
            any_link.setMovementMethod(LinkMovementMethod.getInstance());
            link_title = (TextView) itemView.findViewById(R.id.link_title);
            link_url = (TextView) itemView.findViewById(R.id.link_url);
            link_url.setMovementMethod(LinkMovementMethod.getInstance());
            link_des = (TextView) itemView.findViewById(R.id.link_des);
            emote_view = (GridView) itemView.findViewById(R.id.emote_grid);
            more_option = (ImageView) itemView.findViewById(R.id.more_option);
            high_like = (ImageView) itemView.findViewById(R.id.high_like);
            high_emote = (ImageView) itemView.findViewById(R.id.high_emote);
            high_comment = (ImageView) itemView.findViewById(R.id.high_commenticon);
            high_share = (ImageView) itemView.findViewById(R.id.high_share);
            link_img = (ImageView) itemView.findViewById(R.id.link_img);
            user_img = (CircularImageView) itemView.findViewById(R.id.pimg);
            multi_img_rv = (RecyclerView) itemView.findViewById(R.id.multi_img_rv);
            cube_recycler_view = (RecyclerView) itemView.findViewById(R.id.cube_recycler_view);
            viewGroup = (LinearLayout) itemView.findViewById(R.id.shrpopup);
            emote_layout = (LinearLayout) itemView.findViewById(R.id.emote_layout);
            link_design = (LinearLayout) itemView.findViewById(R.id.link_design);
            any_link_design = (LinearLayout) itemView.findViewById(R.id.any_link_design);
            images=(GridView) itemView.findViewById(R.id.images);
            tags = itemView.findViewById(R.id.tags_text);
            layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            multi_img_rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            cube_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
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
        }
        else
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {

                container = (View) popupWindow.getContentView().getParent().getParent();

            }
            else
            {

                container = (View) popupWindow.getContentView().getParent();

            }

        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container , p);

    }

    private class GetCategory extends AsyncTask

    {

        ProgressDialog pd;
        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.show();
        }
        @Override
        protected void onPostExecute(Object o)
        {
            adapter = new DiscoverMyCubeAdapter(grid_element, context);
            cube_follow.setAdapter(adapter);
            if (pd.isShowing())
            {
                pd.dismiss();
            }
        }
        public void removeItem(int position){
            grid_element .remove(position);
            notifyDataSetChanged();
        }

        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null;

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (isNetworkConnected()) {
                try {
                    URL myUrl = new URL(Config.BASE_URL + "Cubes/User_Cubes/" + login_user_id);
                    //Create a connection
                    HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                    //Set methods and timeouts
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(10000);
                    //Connect to our url
                    connection.connect();
                    //Create a new InputStreamReader
                    InputStreamReader streamReader = new
                            InputStreamReader(connection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                    try {
                        JSONObject json = new JSONObject(result);
                        if (result.contains("True"))
                        {
                            JSONArray res = json.getJSONArray("Response");
                            for (int j = 0; j < res.length(); j++)
                            {
                                JSONObject data = res.getJSONObject(j);


                                grid_element.add(new CreateCubeCatList(data.getString("Name"),
                                        data.getString("_id")
                                        ,Config.Cube_Image + data.getString("Image")));

                            }

                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, " " + e, Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e) {
                }
            }
            else

            {

                Toast.makeText(context, "Please Check the Internet Connection ", Toast.LENGTH_LONG).show();

            }


            return null;
        }

    }
    public class DiscoverMyCubeAdapters extends BaseAdapter {


        class ViewHolder
        {
            PorterShapeImageView img;
            TextView name;
            ImageView imag,back;
            LinearLayout cube_category;
            RelativeLayout select;


        }

        public Context context;


        public DiscoverMyCubeAdapters(ArrayList<CreateCubeCatList> apps, Context context) {
            grid_elements = apps;
            this.context = context;
        }

        @Override

        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getCount()
        {



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
                viewHolder =new ViewHolder();
                viewHolder.back =(ImageView)rowView.findViewById(R.id.unfollow);
                viewHolder.imag =(ImageView)rowView.findViewById(R.id.back3);
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


            viewHolder.select.setBackgroundResource(grid_elements.get(position).isSelected() ?
                    R.drawable.selection_square : R.drawable.unselection_square);
            viewHolder.name.setText(grid_elements.get(position).getName());
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.back.setVisibility(View.GONE);
            Log.i("Tes", "getView: " + grid_elements.get(position).getImg());
            Glide.with(context).load(grid_elements.get(position).getImg())
                    .override(200, 200)
//                    .placeholder(R.drawable.loading_spinner)
                    .into(viewHolder.img);
            viewHolder.cube_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    grid_elements.get(position).setSelect(!grid_elements.get(position).isSelect());
                    // holder.view.setBackgroundColor(list.get(position).isSelected() ? getResources().getColor(R.color.Inscube_match) : Color.WHITE);
                    viewHolder.select.setBackgroundResource(grid_elements.get(position).isSelect() ?
                            R.drawable.selection_square : R.drawable.unselection_square);
                    // holder.view.setBackgroundResource(R.drawable.selection_square);
                    if (!grid_elements.get(position).isSelect())
                    {
                        selectedids.remove(grid_elements.get(position).getCat_id());
                    }
                    else
                    {
                        selectedids.add(grid_elements.get(position).getCat_id());
                    }

                }
            });
            return rowView;
        }

    }
    public class DiscoverMyCubeAdapter extends BaseAdapter {


        class ViewHolder {


            PorterShapeImageView img;
            TextView name;
            ImageView imag,back;
            LinearLayout cube_category;
            RelativeLayout select;
        }



        public Context context;



        public DiscoverMyCubeAdapter(ArrayList<CreateCubeCatList> apps, Context context) {
            grid_element = apps;
            this.context = context;
        }
        @Override
        public int getCount()
        {


            return grid_element.size();
        }
        @Override

        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
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
                viewHolder = new DiscoverMyCubeAdapter.ViewHolder();
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
            viewHolder.select.setBackgroundResource(grid_element.get(position).isSelected() ?
                    R.drawable.selection_square : R.drawable.unselection_square);
            viewHolder.name.setText(grid_element.get(position).getName());
            viewHolder.img.setVisibility(View.VISIBLE);
            viewHolder.name.setVisibility(View.VISIBLE);
            viewHolder.imag.setVisibility(View.GONE);

            Log.i("Tes", "getView: " + grid_element.get(position).getImg());

            Glide.with(context).load(grid_element.get(position).getImg())
                    .override(200, 200)
//                    .placeholder(R.drawable.loading_spinner)
                    .into(viewHolder.img);
            viewHolder.cube_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    grid_element.get(position).setSelected(!grid_element.get(position).isSelected());
                    // holder.view.setBackgroundColor(list.get(position).isSelected() ? getResources().getColor(R.color.Inscube_match) : Color.WHITE);
                    viewHolder.select.setBackgroundResource(grid_element.get(position).isSelected() ?
                            R.drawable.selection_square : R.drawable.unselection_square);
                    // holder.view.setBackgroundResource(R.drawable.selection_square);
                    if (!grid_element.get(position).isSelected()) {

                        selectedids.remove(grid_element.get(position).getCat_id());

                        // Toast.makeText(getActivity(), "removed", Toast.LENGTH_SHORT).show();

                    } else {

                        selectedids.add(grid_element.get(position).getCat_id());


                    }

                }
            });

            return rowView;
        }

    }
    private class GetCategorys extends AsyncTask {


        ProgressDialog pd;

        @Override
        protected void onPreExecute()
        {

            pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.show();
        }
        public void removeItem(int position){
            grid_elements .remove(position);
            notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(Object o) {

            adapters = new DiscoverMyCubeAdapters(grid_elements, context);


            cube_gridview.setAdapter(adapters);
            if (pd.isShowing())
            {
                pd.dismiss();
            }
        }
        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (isNetworkConnected()) {
                try {

                    URL myUrl = new URL(Config.BASE_URL + "Cubes/User_Followed_Cubes/"  + login_user_id);

                    //Create a connection
                    HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                    //Set methods and timeouts

                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(10000);
                    //Connect to our url
                    connection.connect();
                    //Create a new InputStreamReader
                    InputStreamReader streamReader = new
                            InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    reader.close();
                    streamReader.close();
                    result = stringBuilder.toString();
                    try {
                        JSONObject json = new JSONObject(result);
                        if (result.contains("True")) {


                            JSONArray res = json.getJSONArray("Response");

                            for (int j = 0; j < res.length(); j++) {

                                JSONObject data = res.getJSONObject(j);

                                grid_elements.add(new CreateCubeCatList(data.getString("Name"),
                                        data.getString("_id")
                                        ,Config.Cube_Image + data.getString("Image")));

                            }

                        }

                    } catch (JSONException e) {
                        Toast.makeText(context, " " + e, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                }
            } else
            {

                Toast.makeText(context, "Please Check the Internet Connection ", Toast.LENGTH_LONG).show();

            }


            return null;
        }
    }


    private class SharePost extends AsyncTask {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(context);
            pd.setMessage("Sharing...");
            pd.show();
        }
        @Override
        protected void onPostExecute(Object o) {


            if (pd.isShowing()) {

                pd.dismiss();

            }
        }
        @Override
        protected Object doInBackground(Object[] objects)
        {
            RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

            String u = Config.BASE_URL + "Capture/Cube_Capture_Share";

            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        Log.i("TAG", "onResponse: " + response);
                        JSONObject res = new JSONObject(response);


                        if (res.getString("Output").contains("True"))
                        {
                            SharedPreferences.Editor refresh = context.getSharedPreferences("refresh",
                                    Context.MODE_PRIVATE)
                                    .edit();
                            refresh.putString("ref", "1");
                            refresh.commit();
                            reportuser_popup.dismiss();
                            Toast.makeText(context,"Post Shared successfully",Toast.LENGTH_LONG).show();


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
                protected Map<String, String> getParams() {
                    Map<String, String> para = new HashMap<String, String>();
                    para.put("User_Id", login_user_id);
                    para.put("Cube_Ids", cubeid.toString());
                    para.put("Capture_Id",post_id );
                    return para;
                }
            };
            MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            MyRequestQueue.add(MyStringRequest);
            return null;

        }
    }


    public class EmoteGridAdapter extends BaseAdapter {

        Context context;
        GridView emote_view;
        int viewPosition;

        public EmoteGridAdapter(Context context, ArrayList<EmoteList> emote_name_list, GridView emote_view, int viewPosition) {
            this.context = context;
            emoteList = emote_name_list;
            this.emote_view = emote_view;
            this.viewPosition = viewPosition;
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
        public View getView(final int i, View view, final ViewGroup viewGroup) {

            View rowView = view;
            final ViewHolder viewHolder;

            Log.i("Test", "EmoteGridAdapter: " + i);
            Log.i("Test", "EmoteGridAdapter: " + new Gson().toJson(emoteList.get(i)));
            Log.i("Test", "EmoteGridAdapter: " + new Gson().toJson(emoteList));

            final int currentposition = i;

            if (rowView == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.emote_grid_design, viewGroup, false);

                // configure view holder
                viewHolder = new ViewHolder();

                viewHolder.emote_name = (TextView) rowView.findViewById(R.id.emote_name);

                viewHolder.emote_count = (TextView) rowView.findViewById(R.id.emote_count);

                viewHolder.emote_layout = (LinearLayout) rowView.findViewById(R.id.emote_layout);

                viewHolder.layoutInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                viewHolder.lay = (LinearLayout) rowView.findViewById(R.id.shrpopup);

                rowView.setTag(viewHolder);

            } else {

                viewHolder = (ViewHolder) rowView.getTag();

            }

            viewHolder.emote_name.setText(emoteList.get(i).getEmote_Text());
            viewHolder.emote_count.setText(emoteList.get(i).getEmote_Count());
            currentEmote = emoteList.get(i);

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


//                    Log.i("TEST", "getParams:C 1" + currentEmote.getEmote_Text());
//                    Log.i("TEST", "getParams:C 1" + currentEmote.getEmote_Count());
                    Log.i("Test", "onClick: " + new Gson().toJson(list.get(viewPosition).getEmoteLists()));

                    RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                    String u = Config.BASE_URL + "Capture/Capture_Emote_Update";

                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Log.i("Test", "onResponse: " + "Emote Update");
                                JSONObject json = new JSONObject(response);

                                if (json.getString("Output").contains("True")) {

                                    Log.d("count---", response);

                                    JSONObject data = json.getJSONObject("Response");

                                    viewHolder.emote_count.setText(data.getString("Count"));


                                    viewHolder.emote_layout.setBackground(context.getResources().getDrawable
                                            (R.drawable.emote_outline_design_selected));
                                    viewHolder.emote_layout.setEnabled(false);

                                    list.get(viewPosition).getEmoteLists().get(i).setEmote_Count(data.getString
                                            ("Count"));
                                    CubeCapturePostAdapter.this.notifyDataSetChanged();
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
                        protected Map<String, String> getParams() {

                            Map<String, String> para = new HashMap<String, String>();
                            para.put("Capture_Id", list.get(viewPosition).getEmoteLists().get(i).getEmote_post_Id());
                            para.put("User_Id", login_user_id);
                            para.put("Emote_Id", list.get(viewPosition).getEmoteLists().get(i).getEmote_Id());

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
