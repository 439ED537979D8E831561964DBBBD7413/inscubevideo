package com.test.aashi.inscubenewbuild;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GetterSetter.CubeCategoeyList;
import GetterSetter.TopicList;

public class Cubes extends AppCompatActivity {

    TextView category_name, no_cubes;
    Button create_media;
    RecyclerView cube_list_rv;
    String Cat_Id, Cube_Id, Cube_User_Id, Cube_Name, Cube_members, Cube_Image, Cube_type, Security_Code = "", login_user_id, User_Image, Color_code, Ins_name;
    CubeListAdapter adapter;
    SharedPreferences login;
 //   List<CubeCategoeyList> cube_list_data;
    List<CubeCategoeyList> cubetListFiltered;
    LinearLayout main;
    LinearLayoutManager linearLayoutManager;
    PopupWindow security_code_popup;
    int success_value = 0;
    ImageView grid_view_back, home;
    List<CubeCategoeyList> listt;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_category);
        TextView id=(TextView)findViewById(R.id.myid);
        Cat_Id = getIntent().getStringExtra("Cat_id");
        login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        User_Image = login.getString("UserImage", "");
        Color_code = login.getString("ColorCode", "");
        Ins_name = login.getString("UserName", "");
        id.setText(login_user_id);
        android.support.v7.widget.Toolbar actionBarToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        actionBarToolBar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(actionBarToolBar);
        new GetCubelist().execute();
        category_name = (TextView) findViewById(R.id.category_name);
        no_cubes = (TextView) findViewById(R.id.no_cubes);
        category_name.setText(getIntent().getStringExtra("cat_name"));
        create_media = (Button) findViewById(R.id.create_media);

        main = (LinearLayout) findViewById(R.id.main);


     //   home = (ImageView) findViewById(R.id.home);

        cube_list_rv = (RecyclerView) findViewById(R.id.cube_list);



        listt = new ArrayList<>();
        cube_list_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        cube_list_rv.setLayoutManager(linearLayoutManager);

        create_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Cubes.this, CreateCube.class);
                in.putExtra("cube_update","0");
                startActivity(in);
            }
        });



     /*   home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cubes.this, StoryPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        */
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView = (SearchView) menu.findItem(R.id.action_search)
               .getActionView();
       searchView.setSearchableInfo(searchManager
               .getSearchableInfo(getComponentName()));
      searchView.setMaxWidth(Integer.MAX_VALUE);
        setSearchIcons(searchView);

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchAutoComplete.setTextColor(Color.WHITE);

        /*Code for changing the search icon */
        ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.search);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
    private void setSearchIcons(SearchView search) {
        try
        {
            Field searchField = SearchView.class.getDeclaredField("mCloseButton");
            searchField.setAccessible(true);
            ImageView closeBtn = (ImageView) searchField.get(search);
            closeBtn.setImageResource(R.drawable.ic_multiply);
            ImageView searchButton = (ImageView) search.findViewById(android.support.v7.appcompat.R.id.search_button );
            searchButton.setImageResource(R.drawable.ic_magnifier);

        } catch (NoSuchFieldException e)
        {
            Log.e("SearchView", e.getMessage(), e);
        } catch (IllegalAccessException e)
        {
            Log.e("SearchView", e.getMessage(), e);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.home) {
            Intent i = new Intent(Cubes.this, StoryPage.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
        if (id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
    public class CubeListAdapter extends RecyclerView.Adapter<CubeListAdapter.ViewHolder> implements Filterable {
        Context con;

        public CubeListAdapter(Context con, List<CubeCategoeyList> list) {
            this.con = con;
            listt = list;
            cubetListFiltered = list;
            cubetListFiltered  = listt;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cube_list, parent, false);
            return new ViewHolder(v);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);

          final CubeCategoeyList cubeCategoeyList = cubetListFiltered.get(position);


            Glide.with(con).load(cubeCategoeyList.getCube_image()).override(200, 200)
                    .into(holder.cube_img);
            if (cubeCategoeyList.getCube_name().length() > 23) {
                holder.cube_name.setText(cubeCategoeyList.getCube_name().subSequence(0, 23) + "...");
            }
            else
            {
                holder.cube_name.setText(cubeCategoeyList.getCube_name());
            }

            holder.layout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    {

                        String a = String.valueOf(position);
                        Intent i = new Intent(Cubes.this, CubeView.class);
                        i.putExtra("cube_update","0");
                        i.putExtra("Cube_Id", cubeCategoeyList.getCube_id());
                        i.putExtra("Cube_User_Id", cubeCategoeyList.getCubeuser_id());
                        i.putExtra("Cube_Type", cubeCategoeyList.getCube_type());
                        i.putExtra("Feeds","1");
                        i.putExtra("Cube_Code", cubeCategoeyList.getSecurity_code());
                        i.putExtra("Position", a);
                        startActivityForResult(i, 90);
                    }

                }
            });
            String cube_type = cubeCategoeyList.getCube_type();

            if (cube_type.contains("Open"))
            {

                holder.cube_type_img.setImageResource(R.drawable.globe);
            }
            else if (cube_type.contains("Close")) {

                holder.cube_type_img.setImageResource(R.drawable.lock);
            }

            holder.join_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cubeCategoeyList.getCube_type().contains("Open")) {

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
                                                    Toast.makeText(getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();

                                                    cubetListFiltered.remove(position);
                                                    //listt.remove(position);
                                                    notifyDataSetChanged();
                                                } else
                                                {
                                                    success_value = 0;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                        else
                                        {

                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), "Connection Error!..", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            protected Map<String, String> getParams()
                            {
                                Map<String, String> para = new HashMap<String, String>();
                                para.put("Cube_Id", cubeCategoeyList.getCube_id());
                                para.put("User_Id", login_user_id);
                                return para;
                            }
                        };
                        // Add the request to the RequestQueue.
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
                        queue.add(stringRequest);

                    } else {

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

                                    if (cube_code.equals(cubeCategoeyList.getSecurity_code())) {

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

                                                                    //Log.d("join--", response.toString());

                                                                    Toast.makeText(getApplicationContext(), "Joined",
                                                                            Toast.LENGTH_SHORT).show();

                                                                    cubetListFiltered.remove(position);
                                                                    listt.remove(position);

                                                                    adapter.notifyDataSetChanged();


                                                                }
                                                                else
                                                                {

                                                                    success_value = 0;

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

                                                para.put("Cube_Id", cubeCategoeyList.getCube_id());
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
            holder.cube_img.setVisibility(View.VISIBLE);
            holder.cube_name.setVisibility(View.VISIBLE);

        }


        @Override
        public int getItemCount() {

            return cubetListFiltered.size();

        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        cubetListFiltered = listt;
                    } else {
                        List<CubeCategoeyList> filteredList = new ArrayList<>();
                        for (CubeCategoeyList row : listt) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getCube_name().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        cubetListFiltered = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = cubetListFiltered;
                    return filterResults;
                }
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    cubetListFiltered = (ArrayList<CubeCategoeyList>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView cube_name, members_count;
            ImageView cube_img, cube_type_img;
            Button join_button;
            LinearLayout layout;
            public ViewHolder(View itemView)
            {
                super(itemView);
                cube_name = (TextView) itemView.findViewById(R.id.cube_name);
                //  members_count = (TextView) itemView.findViewById(R.id.cube_members);
                layout = (LinearLayout) itemView.findViewById(R.id.layout);
                cube_img = (ImageView) itemView.findViewById(R.id.cube_img);
                cube_type_img = (ImageView) itemView.findViewById(R.id.cube_join_info_img);
                join_button = (Button) itemView.findViewById(R.id.join_button);
            }
        }
    }
    private class GetCubelist extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(Cubes.this);
            pd.setMessage("Loading...");
            pd.show();
            listt = new ArrayList<>();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(Cubes.this);
                final String url = Config.BASE_URL + "Cubes/Cube_List/" + Cat_Id + "/" + login_user_id;
                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                try {
                                        if (response.getString("Output").contains("True")) {
                                                JSONArray ress = response.getJSONArray("Response");
                                                for (int i = 0; i < ress.length(); i++) {
                                                    JSONObject data = ress.getJSONObject(i);
                                                    Cube_Id = data.getString("_id");
                                                    Cube_User_Id = data.getString("User_Id");
                                                    Cube_Name = data.getString("Name");
                                                    Cube_members = data.getString("Members");
                                                    Cube_Image = Config.Cube_Image + data.getString("Image");
                                                    Cube_type = data.getString("Security");
                                                    Security_Code = data.getString("Security_Code");

                                           listt.add(new CubeCategoeyList(Cube_Name, Cube_User_Id, Cube_Id, Cube_Image,
                                                    Cube_members, Cube_type, Security_Code, "", "0"));

                                                }
                                            }
                                            if (listt.size() != 0) {

                                                no_cubes.setVisibility(View.GONE);
                                                cube_list_rv.setVisibility(View.VISIBLE);
                                                adapter = new CubeListAdapter(Cubes.this, listt);
                                                cube_list_rv.setAdapter(adapter);

                                            }
                                            else
                                            {

                                                cube_list_rv.setVisibility(View.GONE);
                                                no_cubes.setVisibility(View.VISIBLE);

                                            }

                                        }
                                        catch (Exception e)
                                        {

                                        }
                                    }
                                    //Log.d("Cubesize---", String.valueOf(cube_list_data.size()));
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                //  Log.d("req--", error.toString());
                            }
                        }
                );
                getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
                queue.add(getRequest);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            if (pd.isShowing())
            {
                pd.dismiss();
            }
        }
    }
    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    public void dimBehind(PopupWindow popupWindow)
    {
        View container;
        if (popupWindow.getBackground() == null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        }
        else
            {
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 90:
                if (resultCode == RESULT_OK)
                {
                    Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();
                    listt.remove(Integer.parseInt(data.getData().toString()));
                    cubetListFiltered.remove(Integer.parseInt(data.getData().toString()));
                    adapter.notifyDataSetChanged();
                }

                break;
        }
    }

}
