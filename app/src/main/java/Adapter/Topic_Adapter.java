package Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.aashi.inscubenewbuild.Config;
import com.test.aashi.inscubenewbuild.CubeView;
import com.test.aashi.inscubenewbuild.Highlight_Edit;
import com.test.aashi.inscubenewbuild.R;
import com.test.aashi.inscubenewbuild.Topic_edit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GetterSetter.TopicList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.test.aashi.inscubenewbuild.CubeView.getPath;

/**
 * Created by AASHI on 11-04-2018.
 */

public class Topic_Adapter extends RecyclerView.Adapter<Topic_Adapter.ViewHolder> {

    private static final int RESULT_OK = 1  ;
    PopupWindow deletePopupWindoww;
    Context context;
    List<TopicList> topicLists;
    LinearLayout reportuser, reportpost, editt, delete, reportview, editview;
    SharedPreferences login;
    String login_user_id;
    String cube_id;
    Topic_Adapter a;
    String topic_ids;


    public Topic_Adapter(Context context, List<TopicList> topicLists,Topic_Adapter adapter) {
        this.context = context;
        this.topicLists = topicLists;
        this.a = adapter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_view_design, parent, false);
        login = context.getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.topic_name.setText(topicLists.get(position).getTopic_Name());
        holder.topic_des.setText(topicLists.get(position).getTopic_Description());

        if (topicLists.get(position).getFile_name().size() == 0) {
            holder.cube_img_list.setVisibility(View.GONE);
            //holder.no_filetxt.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.cube_img_list.setVisibility(View.VISIBLE);
            holder.no_filetxt.setVisibility(View.GONE);
            holder.img_adpt = new CubeView.ImageAdapter(context,
                    topicLists.get(position).getFile_name(), holder.cube_img_list, holder.img_adpt, 1);
            holder.cube_img_list.setAdapter(holder.img_adpt);
            holder.img_adpt.notifyDataSetChanged();
            if (topicLists.get(position).getTopic_user().equals(login_user_id))
            {
                holder.more_option.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.more_option.setVisibility(View.GONE);

            }
        }
            if (topicLists.get(position).getTopic_user().equals(login_user_id))
                {

                }
                else
                  {
                   holder.more_option.setVisibility(View.GONE);
                   }
                holder.more_option.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
                        final Point p;
                        int[] location = new int[2];
                        holder.more_option.getLocationOnScreen(location);
                        p = new Point();
                        p.x = location[0];
                        p.y = location[1];

                        final View layout = holder.layoutInflater.inflate(R.layout.edit_popup, holder.viewGroup);

                        // Creating the PopupWindow
                        final PopupWindow popup = new PopupWindow(context);
                        popup.setContentView(layout);
                        popup.setFocusable(true);
                        popup.setWidth(FrameLayout.LayoutParams.WRAP_CONTENT);
                        popup.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
                        popup.setAnimationStyle(R.style.popup_window_animation_phone);
                        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
                        final int OFFSET_X = -10;
                        final int OFFSET_Y = 25;

                        // Displaying the popup at the specified location, + offsets.



                                popup.showAtLocation(holder.main, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);



                        reportview = (LinearLayout) layout.findViewById(R.id.report_view);
                        editview = (LinearLayout) layout.findViewById(R.id.edit_view);


                        editt = (LinearLayout) layout.findViewById(R.id.edit);
                        delete = (LinearLayout) layout.findViewById(R.id.delete);
                        reportpost = (LinearLayout) layout.findViewById(R.id.reportpost);
                        reportuser = (LinearLayout) layout.findViewById(R.id.reportuser);
                        reportpost.setVisibility(View.GONE);
                        reportuser.setVisibility(View.GONE);


                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Button no, yes;

                                popup.dismiss();


                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                                final View mView = inflater.inflate(R.layout.delete_conform, null);

                                deletePopupWindoww = new PopupWindow(
                                        mView,
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );


                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });


                                no = (Button) mView.findViewById(R.id.no);
                                yes = (Button) mView.findViewById(R.id.yes);

                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);

                                        String u = Config.BASE_URL + "Cubes/Delete_Topic";
                                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u,
                                                new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONObject json = new JSONObject(response);
                                                    //Toast.makeText(context, " " + response, Toast.LENGTH_SHORT).show();
                                                    if (json.getString("Output").contains("True")) {

                                                        Toast.makeText(context, "Post Deleted",
                                                                Toast.LENGTH_SHORT).show();

                                                        topicLists.remove(position);
                                                          notifyDataSetChanged();


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
                                        }) {
                                            protected Map<String, String> getParams() {
                                                Map<String, String> para = new HashMap<String, String>();
                                                para.put("Topic_Id", topicLists.get(position).getTopic_id());
                                                para.put("Cube_Id", topicLists.get(position).getTopic_cube_id());
                                                para.put("User_Id", topicLists.get(position).getTopic_user());
                                                return para;
                                            }
                                        };

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

                                deletePopupWindoww.showAtLocation(holder.main, Gravity.CENTER, 0, 0);

                                dimBehind(deletePopupWindoww);


                            }
                        });

                        editt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent intent = new Intent(context, Topic_edit.class);
                                intent.putExtra("cubeid",  topicLists.get(position).getTopic_cube_id());
                                intent.putExtra("topicid", topicLists.get(position).getTopic_id());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                            }
                        });
                    }
                });
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
    public int getItemCount() {
        if(topicLists!=null){
            return topicLists.size();
        } else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView cube_img_list;
        TextView topic_name, topic_des, no_filetxt;
        CubeView.ImageAdapter img_adpt;
        Post_MultiImage_Adapter post_multiImage_adapter;

        ImageView more_option;
        LinearLayout viewGroup, main;
        LayoutInflater layoutInflater;

        public ViewHolder(View itemView) {
            super(itemView);

            topic_name = (TextView) itemView.findViewById(R.id.topic_name);
            topic_des = (TextView) itemView.findViewById(R.id.topic_description);
            no_filetxt = (TextView) itemView.findViewById(R.id.no_filetxt);
            more_option = (ImageView) itemView.findViewById(R.id.more_option1);
            main = (LinearLayout) itemView.findViewById(R.id.main3);
            layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cube_img_list = (RecyclerView) itemView.findViewById(R.id.cube_img_list);

            cube_img_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        }

    }

}



