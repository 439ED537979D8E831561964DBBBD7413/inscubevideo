package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.test.aashi.inscubenewbuild.Config;
import com.test.aashi.inscubenewbuild.Highlight_Edit;
import com.test.aashi.inscubenewbuild.Highlights_Comments;
import com.test.aashi.inscubenewbuild.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GetterSetter.Comment_List;
import GetterSetter.Highlights_PostList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by AASHI on 12-04-2018.
 */

public class HighlightsCommentAdapter extends RecyclerView.Adapter<HighlightsCommentAdapter.ViewHolder> {

    SharedPreferences login;
    String login_user_id, User_Image, Color_code, Ins_name;
    LinearLayout reportuser, reportpost, editt, delete, reportview, editview;
    List<Comment_List> list = new ArrayList<>();
    Context context;
    PopupWindow deletePopupWindoww, reportuser_popup, reportpost_popup, comment_edit_popup;
    RecyclerView rv;

    HighlightsCommentAdapter a;

    public void setA(HighlightsCommentAdapter a) {
        this.a = a;
    }

    public HighlightsCommentAdapter(Context context, List<Comment_List> list, RecyclerView rv) {
        this.list = list;
        this.context = context;
        this.rv = rv;

    }

    @NonNull
    @Override
    public HighlightsCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        login = context.getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        User_Image = login.getString("UserImage", "");
        Color_code = login.getString("ColorCode", "");
        Ins_name = login.getString("UserName", "");


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_design, parent, false);

        return new HighlightsCommentAdapter.ViewHolder(v);
    }

    @Override

    public void onBindViewHolder(@NonNull final HighlightsCommentAdapter.ViewHolder holder, final int position) {

        Glide.with(context).load(list.get(position).getComment_User_Image())
                .override(100, 100)
                .into(holder.pimg);

        holder.name.setText(list.get(position).getComment_User_Name());

        holder.time.setText(list.get(position).getComment_Time());

        holder.text.setText(list.get(position).getComment_Text());


        holder.more_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Point p;
                int[] location = new int[2];

                holder.more_option.getLocationOnScreen(location);

                p = new Point();
                p.x = location[0];
                p.y = location[1];

                View layout = holder.layoutInflater.inflate(R.layout.comment_edit_popup, holder.viewGroup);

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

                if (list.get(position).getComment_User_Id().equals(login_user_id)) {

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


                        final EditText comment_edit_input;

                        Button comment_update;

                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                        final View mView = inflater.inflate(R.layout.comment_edit_design, null);

                        comment_edit_popup = new PopupWindow(
                                mView,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        comment_edit_popup.setFocusable(true);

                        comment_edit_popup.setAnimationStyle(R.style.popup_window_animation_phone);

                        comment_edit_popup.showAtLocation(mView, Gravity.CENTER, 0, 0);

                        dimBehind(comment_edit_popup);


                        comment_edit_input = (EditText) mView.findViewById(R.id.edit_comment);

                        comment_update = (Button) mView.findViewById(R.id.comment_update);

                        if (!list.get(position).getComment_Text().isEmpty()) {
                            comment_edit_input.setText(list.get(position).getComment_Text());
                        }


                        comment_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (comment_edit_input.getText().toString().isEmpty()) {

                                    Toast.makeText(context, "Please Enter Something", Toast.LENGTH_SHORT).show();

                                } else {

                                    RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                                    String u = Config.BASE_URL + "Posts/Comment_Update";

                                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {

                                                JSONObject json = new JSONObject(response);

                                                if (json.getString("Output").contains("True")) {

                                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

                                                    comment_edit_popup.dismiss();

                                                } else {

                                                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();

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

                                            para.put("Comment_Id", list.get(position).getComment_Id());
                                            para.put("Comment_Text", comment_edit_input.getText().toString());

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

                                String u = Config.BASE_URL + "Posts/Comment_Delete/" + list.get(position).getComment_Id();

                                StringRequest MyStringRequest = new StringRequest(Request.Method.GET, u, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {

                                            JSONObject json = new JSONObject(response);

                                            // Toast.makeText(context, " " + response, Toast.LENGTH_SHORT).show();

                                            if (json.getString("Output").contains("True")) {

                                                Toast.makeText(context, "Comment Deleted", Toast.LENGTH_SHORT).show();

                                                list.remove(position);

                                                if (list.size() != 0) {

                                                    a.notifyDataSetChanged();

                                                } else {

                                                    rv.setVisibility(View.GONE);
                                                }

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


                reportpost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popup.dismiss();

                        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                        String u = Config.BASE_URL + "Posts/Report_Comment_Check";

                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject json = new JSONObject(response);

                                    //  Toast.makeText(context, " " + response, Toast.LENGTH_SHORT).show();

                                    if (json.getString("Available").contains("True")) {

                                        // Toast.makeText(context, " " + json.getString("ValidReport"), Toast.LENGTH_SHORT).show();

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

                                                String u = Config.BASE_URL + "Posts/Report_Comment";

                                                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        try {

                                                            JSONObject json = new JSONObject(response);

                                                            if (json.getString("Output").contains("True")) {

                                                                Toast.makeText(context, "Comment Reported", Toast.LENGTH_SHORT).show();

                                                                reportpost_popup.dismiss();

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

                                                        // Toast.makeText(context, " " + error, Toast.LENGTH_SHORT).show();

                                                        Toast.makeText(context, "Connection Error!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                                    protected Map<String, String> getParams() {
                                                        Map<String, String> para = new HashMap<String, String>();

                                                        para.put("Comment_Id", list.get(position).getComment_Id());
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

                                para.put("Comment_Id", list.get(position).getComment_Id());
                                para.put("User_Id", login_user_id);


                                return para;
                            }
                        };

                        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

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
                                                        para.put("To_User_Id", list.get(position).getComment_User_Id());
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

                                para.put("To_User_Id", list.get(position).getComment_User_Id());
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


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView more_option;
        LayoutInflater layoutInflater;
        LinearLayout viewGroup;
        TextView name, text, time;
        CircularImageView pimg;

        public ViewHolder(View itemView) {
            super(itemView);


            name = (TextView) itemView.findViewById(R.id.name);
            text = (TextView) itemView.findViewById(R.id.comment_text);
            time = (TextView) itemView.findViewById(R.id.time);

            more_option = (ImageView) itemView.findViewById(R.id.comment_more_option);

            viewGroup = (LinearLayout) itemView.findViewById(R.id.shrpopup);

            pimg = (CircularImageView) itemView.findViewById(R.id.comment_pimg);


            layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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


