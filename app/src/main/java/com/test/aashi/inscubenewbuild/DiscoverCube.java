package com.test.aashi.inscubenewbuild;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import GetterSetter.Category_GridList;
import GetterSetter.CreateCubeCatList;
import GetterSetter.CubeCategoeyList;

public class DiscoverCube extends AppCompatActivity
{
    GridView cube_gridview;
    ArrayList<CreateCubeCatList> grid_element;
    ArrayList<CreateCubeCatList> filter_element;
    DiscoverCubeAdapter adapter;
    ImageView discover_cube_info,grid_view_back,home;
    LinearLayout create_cube;
    String inputLine, result,feeds;
    CardView gotofeed;
    private SearchView searchView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_cube);
        feeds=getIntent().getStringExtra("feeds");
        gotofeed=(CardView)findViewById(R.id.gotofeed);
        cube_gridview = (GridView) findViewById(R.id.discovercube_view);
        discover_cube_info = (ImageView) findViewById(R.id.discover_cube_info);
        grid_view_back = (ImageView) findViewById(R.id.grid_view_back);
        home= (ImageView) findViewById(R.id.home);

        create_cube = (LinearLayout) findViewById(R.id.create_cube);

        grid_element = new ArrayList<>();

        cube_gridview = (GridView) findViewById(R.id.discovercube_view);

        new GetCategory().execute();
        android.support.v7.widget.Toolbar actionBarToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar3);
        actionBarToolBar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(actionBarToolBar);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DiscoverCube.this, StoryPage.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
        if (feeds.contains("0"))
        {
            gotofeed.setVisibility(View.VISIBLE);
            gotofeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DiscoverCube.this, StoryPage.class));
                }
            });
        }
        if (feeds.contains("1"))
        {
            gotofeed.setVisibility(View.GONE);
        }
        discover_cube_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DiscoverCube.this, ExtraActivity.class));
/*
                Toast.makeText(getApplicationContext(), "Create a social media (cube) for your community, organisation or any entity. A cube can be open (public) or exclusive (protected by a cube code). Invite people to join your exclusive social media." +
                        " You are free to create any number of social media, one for each of your community", Toast.LENGTH_LONG).show();
*/
            }
        });


        create_cube.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent(DiscoverCube.this, CreateCube.class);
                i.putExtra("cube_update","0");
                startActivity(i);


            }
        });

        grid_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
            Intent i = new Intent(DiscoverCube.this, StoryPage.class);
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
    public class DiscoverCubeAdapter extends BaseAdapter implements Filterable {


        public class ViewHolder {

            ImageView img;
            TextView name;
            LinearLayout cube_category;
        }

        public ArrayList<CreateCubeCatList> grid_element;

        public Context context;
        public DiscoverCubeAdapter(ArrayList<CreateCubeCatList> apps, Context context) {
            filter_element = apps;
            this.grid_element = apps;
            this.context = context;

        }

        @Override
        public int getCount() {
            return filter_element.size();
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
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        filter_element = grid_element;
                    } else {
                        ArrayList<CreateCubeCatList> filteredList = new ArrayList<>();
                        for (CreateCubeCatList row : grid_element) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getName() .toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }
                        filter_element = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filter_element;
                    return filterResults;
                }
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filter_element = (ArrayList<CreateCubeCatList>) results.values;
                    notifyDataSetChanged();
                }
            };
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           final CreateCubeCatList list = filter_element.get(position);
            View rowView = convertView;
            ViewHolder viewHolder;

            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.discovercube_griddesign, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.img = (ImageView) rowView.findViewById(R.id.grid_img);
                viewHolder.name = (TextView) rowView.findViewById(R.id.grid_nametxt);
                viewHolder.cube_category = (LinearLayout) rowView.findViewById(R.id.cube_category_layout);
                rowView.setTag(viewHolder);

            }
            else
                {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.name.setText(list.getName());

            Log.i("Tes", "getView: " + list.getImg());

            Glide.with(context).load(grid_element.get(position).getImg())
                    .override(200, 200)
//                    .placeholder(R.drawable.loading_spinner)
                    .into(viewHolder.img);



            viewHolder.cube_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    Intent in = new Intent(DiscoverCube.this, Cubes.class);
                    in.putExtra("Cat_id", list.getCat_id());
                    in.putExtra("cat_name", list.getName());
                    startActivity(in);

                }
            });


            return rowView;
        }

    }

    private class GetCategory extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(DiscoverCube.this);
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected void onPostExecute(Object o) {

            adapter = new DiscoverCubeAdapter(grid_element, DiscoverCube.this);
            cube_gridview.setAdapter(adapter);


            if (pd.isShowing()) {

                pd.dismiss();

            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (isNetworkConnected())
            {
                try
                {

                    URL myUrl = new URL(Config.BASE_URL + "Cubes/Category_List");

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

                        if (result.contains("True")) {

                            JSONArray res = json.getJSONArray("Response");

                            for (int j = 0; j < res.length(); j++) {

                                JSONObject data = res.getJSONObject(j);

                                grid_element.add(new CreateCubeCatList(data.getString("Name"),
                                        data.getString("_id")
                                        , Config.Category_Image + data.getString("Image")));

                            }

                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), " " + e, Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                }
            } else {

                Toast.makeText(getApplicationContext(), "Please Check the Internet Connection ", Toast.LENGTH_LONG).show();

            }


            return null;
        }
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }
}
