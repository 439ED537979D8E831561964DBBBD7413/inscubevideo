package com.test.aashi.inscubenewbuild;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.SearchUser_adapter;
import Adapter.Search_capture;
import Adapter.Search_cubes;
import Adapter.Search_treans;
import Adapter.SearchingAdapter;
import GetterSetter.Capture_search;
import GetterSetter.Cube_search;
import GetterSetter.SearchAdapter;
import GetterSetter.Search_user;
import GetterSetter.Trend_sarch;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener
{
    ImageView back;
    CardView highlights,trens,capture,posts,cube;
    String get_id1,cube_image2,members,category,getCube_name1;
    String username3,capturetext3,count3,userimage3,id3,cubename3,security,userid;
    String id4,tag;
    CardView getHighlights,users_post;
    TextView text_high,text_cube,text_cap,text_trends,text_users;
    Context context;
    RecyclerView highlist;
    String[] searchFilter = {"User", "Cube", "Highlights", "Capture", "Trends"};
    EditText editText;
    ArrayAdapter<String> adapter;
    List<Capture_search> capture_searches=new ArrayList<>();
    List<Cube_search> cube_searches=new ArrayList<>();
    List<SearchAdapter>   lists=new ArrayList<>();
    List<Search_user> search_user=new ArrayList<>();
    List<Trend_sarch> trend_sarches=new ArrayList<>();
    Search_capture search_capture;
    Search_treans search_treans;
    SearchUser_adapter searchUser_adapter;
    Search_cubes search_cubes;
    SharedPreferences user;
    String users;
    TextView name;
    ImageView clear;
    SearchingAdapter adapter1;
    String _id1,hashtag1,gashtag2,hashtag3,cube_name1,user_image1;
    String _id,User_id,Post_Category,Post_Text,createdAt,Cube_Name,User_Image,Cubes_Count;
    ArrayList<String> cubes_Id;
    ArrayList<String> cube_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        name=(TextView)findViewById(R.id.names);
        clear=(ImageView)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");

            }
        });
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        text_cap=findViewById(R.id.text_cap);
        text_cube=findViewById(R.id.text_cube);
        text_high=findViewById(R.id.text_high);
        text_trends=findViewById(R.id.text_trends);
        text_users=findViewById(R.id.text_users);
        highlights=(CardView)findViewById(R.id.highights);
        trens=(CardView)findViewById(R.id.trends);
        capture=(CardView)findViewById(R.id.capture);
        posts=(CardView)findViewById(R.id.user);
        cube=(CardView)findViewById(R.id.cube);
        highlist=(RecyclerView)findViewById(R.id.myhighlight);
        highlights.setOnClickListener(this);
        trens.setOnClickListener(this);
        capture.setOnClickListener(this);
        posts.setOnClickListener(this);
        cube.setOnClickListener(this);
        highlist.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       highlist.setLayoutManager(linearLayoutManager);
        final Spinner spin = (Spinner) findViewById(R.id.spin_search);
        editText = (EditText) findViewById(R.id.edit_search);
        user =getApplicationContext().getSharedPreferences("Logged_in_details", Context.MODE_PRIVATE);
        users =user.getString("_id","");
        name.setText(users);
       editText.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {


           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
               highlights.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
               text_high.setTextColor(Color.parseColor( "#E04006"));
               posts.setCardBackgroundColor(Color.parseColor("#E04006"));
               text_users.setTextColor(Color.parseColor("#F5F5F5"));
               trens.setCardBackgroundColor(Color.parseColor("#E04006"));
               text_trends.setTextColor(Color.parseColor("#F5F5F5"));
               cube.setCardBackgroundColor(Color.parseColor("#E04006"));
               text_cube.setTextColor(Color.parseColor("#F5F5F5"));
               capture.setCardBackgroundColor(Color.parseColor("#E04006"));
               text_cap.setTextColor(Color.parseColor("#F5F5F5"));
               highlightServiceCall(s.toString());
               textchange(s.toString());


           }
       });
      editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
              if (actionId == EditorInfo.IME_ACTION_DONE) {

                  textchange(v.getText().toString());
                  return true;
              }
              return false;
          }
      });



    /*    editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                           }

            @Override
            public void afterTextChanged(Editable s) {

                searchFilterMethod(editText.selected, searchFilter[spin.getSelectedItemPosition()]);
            }
        });
        */

        adapter = new ArrayAdapter<String>
                (SearchActivity.this, android.R.layout.simple_spinner_dropdown_item, searchFilter);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getSelectedItemId();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


public void textchange(final String text)
{
    highlights.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            highlightServiceCall(text);
            highlights.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            text_high.setTextColor(Color.parseColor("#E04006"));

            posts.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_users.setTextColor(Color.parseColor("#F5F5F5"));
            trens.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_trends.setTextColor(Color.parseColor("#F5F5F5"));
            cube.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_cube.setTextColor(Color.parseColor("#F5F5F5"));
            capture.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_cap.setTextColor(Color.parseColor("#F5F5F5"));

        }
    });
    posts.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            userServiceCall(text);
            posts.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            text_users.setTextColor(Color.parseColor("#E04006"));


            trens.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_trends.setTextColor(Color.parseColor("#F5F5F5"));

            cube.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_cube.setTextColor(Color.parseColor("#F5F5F5"));
            capture.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_cap.setTextColor(Color.parseColor("#F5F5F5"));
            highlights.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_high.setTextColor(Color.parseColor("#F5F5F5"));

        }
    });
    cube.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cubeServiceCall(text);
            cube.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            text_cube.setTextColor(Color.parseColor("#E04006"));
            posts.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_users.setTextColor(Color.parseColor("#F5F5F5"));
            trens.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_trends.setTextColor(Color.parseColor("#F5F5F5"));
            capture.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_cap.setTextColor(Color.parseColor("#F5F5F5"));
            highlights.setCardBackgroundColor(Color.parseColor("#E04006"));
            text_high.setTextColor(Color.parseColor("#F5F5F5"));
        }
    });
 capture.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         captureServiceCall(text);
         capture.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
         text_cap.setTextColor(Color.parseColor("#E04006"));
         posts.setCardBackgroundColor(Color.parseColor("#E04006"));
         text_users.setTextColor(Color.parseColor("#F5F5F5"));
         trens.setCardBackgroundColor(Color.parseColor("#E04006"));
         text_trends.setTextColor(Color.parseColor("#F5F5F5"));
         cube.setCardBackgroundColor(Color.parseColor("#E04006"));
         text_cube.setTextColor(Color.parseColor("#F5F5F5"));
         highlights.setCardBackgroundColor(Color.parseColor("#E04006"));
         text_high.setTextColor(Color.parseColor("#F5F5F5"));
     }
 });
 trens.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         trendsServiceCall(text);
         trens.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
         text_trends.setTextColor(Color.parseColor("#E04006"));
         capture.setCardBackgroundColor(Color.parseColor("#E04006"));
         text_cap.setTextColor(Color.parseColor("#F5F5F5"));
         posts.setCardBackgroundColor(Color.parseColor("#E04006"));
         text_users.setTextColor(Color.parseColor("#F5F5F5"));
         cube.setCardBackgroundColor(Color.parseColor("#E04006"));
         text_cube.setTextColor(Color.parseColor("#F5F5F5"));
         highlights.setCardBackgroundColor(Color.parseColor("#E04006"));
         text_high.setTextColor(Color.parseColor("#F5F5F5"));
     }
 });
}
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.highights:

                break;
            case R.id.capture:
                break;
            case R.id.trends:
                break;
            case R.id.user:
                break;
            case R.id.cube:
                break;
                default:
                    break;

        }

    }


    private void searchFilterMethod(String text, String searchType) {
//        String[] searchFilter = {"User", "Cube", "Highlights", "Capture", "Trends"};

        switch (searchType) {
            case "User":
                userServiceCall(text);
                break;
            case "Cube":
                cubeServiceCall(text);
                break;
            case "Highlights":
                highlightServiceCall(text);
                break;
            case "Capture":
                captureServiceCall(text);
                break;
            case "Trends":
                trendsServiceCall(text);
                break;
            default:
                break;
        }
    }
    private void userServiceCall(String text)
    {
        new Search_User().execute(text);
    }

    private void cubeServiceCall(String text) {
        new Search_Cube().execute(text);
    }

    private void highlightServiceCall(String text) {
        new Search_Highlight().execute(text);
    }

    private void captureServiceCall(String text) {
        new Search_Capture().execute(text);
    }

    private void trendsServiceCall(String text) {
        new Search_Trends().execute(text);
    }



    @SuppressLint("StaticFieldLeak")
    private class Search_User extends AsyncTask {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SearchActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected Object doInBackground(final Object[] objects) {
            RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
            search_user=new ArrayList<>();

            String url = Config.BASE_URL + "Posts/Search_Users/" + objects[0];

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.getString("Status").contains("True")) {
                                    if (res.getString("Output").contains("True")) {
                                        JSONArray respon = res.getJSONArray("Response");
                                        if (respon.length() != 0) {

                                            for (int i = 0; i < respon.length(); i++) {
                                                JSONObject data = respon.getJSONObject(i);
                                                _id1=data.getString("_id");
                                                hashtag1=data.getString("Hash_Tag_1");
                                                gashtag2=data.getString("Hash_Tag_2");
                                                hashtag3=data.getString("Hash_Tag_3");
                                                cube_name1=data.getString("Inscube_Name");
                                                user_image1=data.getString("Image");
                                                search_user.add(new Search_user(_id1,Config.UserImages+ user_image1,cube_name1,hashtag1,gashtag2,hashtag3));
                                            }

                                        }

                                        if (pd.isShowing()) {

                                            pd.dismiss();

                                        }
                                    }
                                }
                               searchUser_adapter=new SearchUser_adapter(SearchActivity.this,search_user);
                                highlist.setAdapter(searchUser_adapter);
                                searchUser_adapter.setA(searchUser_adapter);
                                searchUser_adapter.notifyDataSetChanged();
                            }
                            catch (JSONException e)
                            {
                                if (pd.isShowing())
                                {
                                    pd.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(SearchActivity.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }
    }

    private class Search_Cube extends AsyncTask {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SearchActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            cube_searches=new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);

            String url = Config.BASE_URL + "Posts/Search_Cubes/"+ users + "/" +objects[0];
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.getString("Status").contains("True")) {

                                    if (res.getString("Output").contains("True")) {

                                        JSONArray respon = res.getJSONArray("Response");

                                        if (respon.length() != 0) {
                                            for (int i = 0; i < respon.length(); i++) {

                                                JSONObject data = respon.getJSONObject(i);
                                                get_id1 =data.getString("_id");
                                                 cube_image2 =data.getString("Image");
                                                 members=data.getString("Members_Count");
                                                 category=data.getString("Category_Name");
                                                 getCube_name1=data.getString("Name");
                                                security=data.getString("Security");
                                                userid =data.getString("User_Id");
                                                 cube_searches.add(new Cube_search(get_id1,getCube_name1,
                                                        Config.Cube_Image+ cube_image2,members,category,security,userid));

                                            }
                                        }
                                        if (pd.isShowing()) {
                                            pd.dismiss();
                                        }
                                    }
                                } search_cubes=new Search_cubes(SearchActivity.this,cube_searches);
                                highlist.setAdapter(search_cubes);
                                search_cubes.setA(search_cubes);
                               search_cubes.notifyDataSetChanged();


                            } catch (JSONException e) {
                                if (pd.isShowing()) {

                                    pd.dismiss();

                                }
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(SearchActivity.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }
    }

    private class Search_Highlight extends AsyncTask {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SearchActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            lists=new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);
            String url = Config.BASE_URL + "Posts/Search_Posts/" + users + "/"+ objects[0];
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.getString("Status").contains("True")) {

                                    if (res.getString("Output").contains("True")) {

                                        JSONArray respon = res.getJSONArray("Response");
                                        if (respon.length() != 0) {
                                            for (int i = 0; i < respon.length(); i++)
                                            {
                                                JSONObject data = respon.getJSONObject(i);
                                                User_id = data.getString("User_Name");
                                                Post_Category = data.getString("Post_Category");
                                                Post_Text = data.getString("Post_Text");
                                                createdAt = data.getString("createdAt");
                                                Cube_Name=data.getString("Cube_Name");
                                                User_Image =data.getString("User_Image");
                                                Cubes_Count =data.getString("Cubes_Count");
                                                _id =data.getString("_id");
                                                lists.add(new SearchAdapter(User_id,Post_Category,Post_Text,createdAt,Cube_Name,
                                                        Config.UserImages + User_Image,Cubes_Count,_id));
                                            }
                                        }
                                    }
                                }
                                adapter1=new SearchingAdapter(SearchActivity.this,lists);
                                highlist.setAdapter(adapter1);
                                adapter1.setA(adapter1);

                                adapter1.notifyDataSetChanged();
                            }
                            catch (JSONException e) {

                              //  Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(SearchActivity.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }
    }

    private class Search_Capture extends AsyncTask {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SearchActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            capture_searches=new ArrayList<>();


            RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);

            String url = Config.BASE_URL + "Capture/Search_Captures/"+ users + "/" + objects[0];

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject res = new JSONObject(response);
                                if (res.getString("Status").contains("True")) {

                                if (res.getString("Output").contains("True"))
                                {


                                        JSONArray respon = res.getJSONArray("Response");

                                        if (respon.length() != 0) {

                                            for (int i = 0; i < respon.length(); i++) {

                                                JSONObject data = respon.getJSONObject(i);
                                          //      username3,capturetext3,count3,userimage3,id3,cubename3;
                                                username3=data.getString("User_Name");
                                                capturetext3=data.getString("Capture_text");
                                                count3=data.getString("Cubes_Count");
                                                userimage3=data.getString("User_Image");
                                                cubename3=data.getString("Cube_Name");
                                                id3=data.getString("_id");
                                                capture_searches.add(new Capture_search(username3,capturetext3,cubename3,Config.UserImages+ userimage3,count3,id3));



                                            }

                                        }
                                    }
                                    if (pd.isShowing())
                                    {

                                        pd.dismiss();

                                    }
                                }
                               search_capture=new Search_capture(SearchActivity.this,capture_searches);
                                highlist.setAdapter(search_capture);
                                search_capture.setA(search_capture);

                                search_capture.notifyDataSetChanged();


                            } catch (JSONException e) {
                                if (pd.isShowing()) {

                                    pd.dismiss();

                                }
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(SearchActivity.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }
    }

    private class Search_Trends extends AsyncTask {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(SearchActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        @Override
        protected Object doInBackground(Object[] objects) {
           trend_sarches=new ArrayList<>();

            RequestQueue queue = Volley.newRequestQueue(SearchActivity.this);

            String url = Config.BASE_URL + "Trends/Search_Trends_Tag/" + objects[0];

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject res = new JSONObject(response);
                                if (res.getString("Status").contains("True")) {

                                    if (res.getString("Output").contains("True")) {

                                        JSONArray respon = res.getJSONArray("Response");

                                        if (respon.length() != 0) {


                                            for (int i = 0; i < respon.length(); i++) {

                                                JSONObject data = respon.getJSONObject(i);
                                                id4=data.getString("_id");
                                                tag=data.getString("Tag");
                                                trend_sarches.add(new Trend_sarch(id4,tag));

                                            }

                                        }

                                        if (pd.isShowing()) {

                                            pd.dismiss();

                                        }
                                    }
                                }


                                search_treans=new Search_treans(SearchActivity.this,trend_sarches);
                                highlist.setAdapter(search_treans);
                                search_treans.setA(search_treans);

                                search_treans.notifyDataSetChanged();

                            } catch (JSONException e) {
                                if (pd.isShowing()) {

                                    pd.dismiss();

                                }
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(SearchActivity.this, "Connection Error!..", Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
            queue.add(stringRequest);

            return null;
        }
    }

}
