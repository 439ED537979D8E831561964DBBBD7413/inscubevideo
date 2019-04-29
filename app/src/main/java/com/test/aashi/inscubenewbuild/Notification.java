package com.test.aashi.inscubenewbuild;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import GetterSetter.NotificationCubeList;
import GetterSetter.NotificationList;
public class Notification extends AppCompatActivity implements View.OnClickListener
{
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    List<NotificationCubeList> list = new ArrayList<>();
    ListView notification_list_view;

    MyListAdapter adapter;

    ArrayList<NotificationList> notificationlist_element;

    ImageView notification_back;

    SharedPreferences login;
    String login_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);

        login_user_id = login.getString("_id", "");

/*        for (int i = 0; i < 10; i++) {

            list.add(new NotificationCubeList("College", R.drawable.personality, 0, "2"));
        }*/

        mRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
/*
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
*/

//        mAdapter = new NotificationAdapter(this, list);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setVisibility(View.GONE);

        notification_list_view  = (ListView) findViewById(R.id.notification_list_view);


        notification_back = (ImageView) findViewById(R.id.notification_back);
        notification_back.setOnClickListener(this);

        notificationlist_element = new ArrayList<>();

        /*
        JSONArray ress = response.getJSONArray("Response");

        for (int i = 0; i < ress.length(); i++) {

            JSONObject data = ress.getJSONObject(i);

            notificationlist_element.add(new NotificationList("Arun", "10", R.drawable.college, "trend"));


        }
*/
//        notificationlist_element.add(new NotificationList("Arun", "10", R.drawable.college, "trend"));


/*
        notification_list_view = (ListView) findViewById(R.id.notification_list_view);
        adapter = new MyListAdapter(notificationlist_element, Notification.this);
        notification_list_view.setAdapter(adapter);
*/

        new GetNotificationlist().execute();

    }

    @Override
    public void onClick(View v) {

        if (v == notification_back) {

            Intent i = new Intent(Notification.this, StoryPage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            this.finish();


        }
    }

    public class MyListAdapter extends BaseAdapter {

        public class ViewHolder {
            TextView notification_name, notification_time, notification_type, time_agotxt;
            ImageView notification_img;
            android.widget.LinearLayout layout;

        }

        public ArrayList<NotificationList> notificationlist_element;

        public Context context;


        private MyListAdapter(ArrayList<NotificationList> apps, Context context) {
            this.notificationlist_element = apps;
            this.context = context;

            Log.i("Test", "MyListAdapter: " + new Gson().toJson(notificationlist_element));
        }

        @Override
        public int getCount() {
            return notificationlist_element.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            View rowView = convertView;
            MyListAdapter.ViewHolder viewHolder;

            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.notification_list_design, parent, false);
                // configure view holder
                viewHolder = new MyListAdapter.ViewHolder();
                viewHolder.notification_img = (ImageView) rowView.findViewById(R.id.notification_icon);
                viewHolder.notification_name = (TextView) rowView.findViewById(R.id.notification_name);
                viewHolder.time_agotxt = (TextView) rowView.findViewById(R.id.time_agotxt);
                viewHolder.layout = (android.widget.LinearLayout) rowView.findViewById(R.id.layout);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (MyListAdapter.ViewHolder) convertView.getTag();
            }


//            viewHolder.notification_name.setText(notificationlist_element.get(position).getTrendsText());

            if (position > -1) {


                final NotificationList notificationList = notificationlist_element.get(position);
                Log.i("TAG", "getView: " + new Gson().toJson(notificationList.getUpdatedAt()));
                String notiType = "";

                notiType = notificationList.getNotify_Type();

                Glide.with(context)
                        .load(Config.User_profile_Image + notificationList.getUser_Image())
                        .override(150, 150)
                        .error(R.drawable.erroe_loading)
                        .into(viewHolder.notification_img);


                try {
//                    2018-06-19T18:33:17.839Z
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date past = format.parse(notificationList.getUpdatedAt());
                    Date now = new Date();
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                    long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                    long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

                    if (seconds < 60)
                    {
                        viewHolder.time_agotxt.setText(seconds + " seconds ago");
                        System.out.println(seconds + " seconds ago");
                    } else if (minutes < 60) {
                        viewHolder.time_agotxt.setText(minutes + " minutes ago");
                        System.out.println(minutes + " minutes ago");
                    } else if (hours < 24) {
                        viewHolder.time_agotxt.setText(hours + " hours ago");
                        System.out.println(hours + " hours ago");
                    } else {
                        viewHolder.time_agotxt.setText(days + " days ago");
                        System.out.println(days + " days ago");
                    }

                } catch (Exception e) {
                    viewHolder.time_agotxt.setText("");

                }
                switch (notiType)
                {
                    case "New_Post":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " posted " + notificationList.getTextAddon() + " " + notificationList.getPost_Type() + " in " + notificationList.getCube_Name());


                        break;
                    case "Shared_Post":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared " + notificationList.getTextAddon() + " " + notificationList.getPost_Type() + " in " +
                                notificationList.getCube_Name());

                        break;
                    case "Shared":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared your " + notificationList.getPost_Type());

                        break;
                    case "Opinion":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared an opinion on your " + notificationList.getPost_Type() + " in " + notificationList.getCube_Name());

                        break;
                    case "Emote":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " commented " + notificationList.getEmote_Text() + " to your " + notificationList.getPost_Type() + " in " + notificationList.getCube_Name());

                        break;

                    case "New_Capture":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " posted a new cube capture in " + notificationList.getCube_Name());

                        break;
                    case "Capture_Shared":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared your capture in " + notificationList.getCube_Name());

                        break;
                    case "Shared_Capture":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared a capture in " + notificationList.getCube_Name());

                        break;
                    case "Capture_Opinion":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared an opinion on your cube capture in " + notificationList.getCube_Name());

                        break;
                    case "Capture_Emote":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " commented " + notificationList.getEmote_Text() + " to your cube capture in " + notificationList.getPost_Type() + " in " + notificationList.getCube_Name());
                        break;


                    case "New_Trends":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " posted a new #campaign in " + notificationList.getCube_Name());
                        break;

                    case "Trends_Shared":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared your #campaign in " + notificationList.getCube_Name());
                        break;

                    case "Shared_Trends":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared a #campaign in " + notificationList.getCube_Name());
                        break;

                    case "Trends_Opinion":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " shared an opinion on your #campaign in " + notificationList.getCube_Name());
                        break;

                    case "Trends_Emote":
                        viewHolder.notification_name.setText(notificationList.getUser_Name() + " commented " + notificationList.getEmote_Text() + " to your #campaign in " + notificationList.getCube_Name());
                        break;

                }

                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (notificationList.getPost_Id() != null && !notificationList.getPost_Id().equalsIgnoreCase("")) {
                            Intent edit = new Intent(context, Highlights_Comments.class);
                            edit.putExtra("post_id", notificationList.getPost_Id());
                            edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(edit);
                        }else if (notificationList.getCapture_Id() != null && !notificationList.getCapture_Id().equalsIgnoreCase("")) {
                            Intent edit = new Intent(context, CubeCapture_Comments.class);
                            edit.putExtra("post_id", notificationList.getCapture_Id());
                            edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(edit);
                        }else if (notificationList.getTrends_Id() != null && !notificationList.getTrends_Id().equalsIgnoreCase("")) {
                            Intent edit = new Intent(context, Trends_Comments.class);
                            edit.putExtra("post_id", notificationList.getTrends_Id());
                            edit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(edit);
                        }
                    }
                });



            }
            return rowView;
        }
    }


    private class GetNotificationlist extends AsyncTask
    {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(Notification.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(Notification.this);

                final String url = Config.BASE_URL + "Posts/Get_Notifications/" + login_user_id;
                Log.i("Test", "onResponse: " + url);

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try
                                {
                                    if (response.toString().contains("True")) {

                                        Log.i("Test", "onResponse: " + response);
                                        try {

                                            if (response.getString("Output").contains("True")) {

                                                notificationlist_element = new ArrayList<>();
                                                JSONArray ress = response.getJSONArray("Response");

                                                for (int i = 0; i < ress.length(); i++) {

                                                    JSONObject data = ress.getJSONObject(i);

                                                    String s = new Gson().toJson(data);
                                                    NotificationList notificationList = new Gson().fromJson(ress.getJSONObject(i).toString(), NotificationList.class);
                                                    notificationlist_element.add(notificationList);

//                                                    notificationlist_element.add(new NotificationList("Arun", "10", R.drawable.college, "trend"));
                                                }
//                                                    notificationlist_element.add(new NotificationList("Arun", "10", R.drawable.college, "trend"));

                                                Log.i("TAG", "onResponse: " + new Gson().toJson(notificationlist_element));
                                                notification_list_view = (ListView) findViewById(R.id.notification_list_view);
                                                adapter = new MyListAdapter(notificationlist_element, Notification.this);
                                                notification_list_view.setAdapter(adapter);


                                            }
                                        } catch (Exception e) {

                                        }
                                    }


                                    if (pd.isShowing()) {

                                        pd.dismiss();
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

            } else {

                Toast.makeText(Notification.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Notification.this, StoryPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        this.finish();

        //        super.onBackPressed();
    }
}
