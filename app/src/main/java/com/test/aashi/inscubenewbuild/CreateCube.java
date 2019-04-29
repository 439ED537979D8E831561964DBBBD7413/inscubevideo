package com.test.aashi.inscubenewbuild;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Adapter.CreateCubeCategoryAdapter;
import GetterSetter.Category_GridList;
import GetterSetter.CityMaster;
import GetterSetter.CountryMaster;
import GetterSetter.CreateCubeCatList;
import GetterSetter.StateMaster;

import static android.content.ContentValues.TAG;

public class CreateCube extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    ArrayList<CreateCubeCatList> grid_element = new ArrayList<>();
    CreateCubeAdapter adapter;
    ToggleButton toggleButton;
    LinearLayout main, note_layout, location_layout, website_layout, mail_layout, contact_layout, add_cube_img;
    EditText disnotes, dislocation, dismail, discontact, diswebsite, cube_nametxt, cube_code, cube_descriptiontxt;
    Button invite_people;
    LinearLayout city_state_layout;
    SharedPreferences cube;
    SharedPreferences login;

    SharedPreferences.Editor sp;
    String login_user_id, User_Image, Color_code, Ins_name, selectedImagePath = "", inputLine, result, Cat_id = "",
            Cube_name = "",Cube_Image="",Cube_Catname="", Cube_Description="",Cube_Contact="",Cube_location="",
            Cube_Web="",Cube_mail="",
            SecurityCode = "", isCheckedd = "0", Security = "Open", file_available = "0", Cube_Update = "0", Cube_Id = "",cubecountry;
    File cube_imagefile;
    ImageView cube_img;
    PopupWindow addtopic_popup, cube_code_popup;
    float totalSize;
    TextView cube_category;
    ImageView cube_create_back, cube_code_info;

    int selectedPosition=-1;
    List<CountryMaster> countryMasterList = null;
    List<StateMaster> stateMasterList = null;
    List<CityMaster> cityMasterList = null;

    CountryMaster currentCountry = null;
    StateMaster currentState = null;
    CityMaster currentCity = null;
    ImageView city, state, country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cube);
        login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);
        login_user_id = login.getString("_id", "");
        User_Image = login.getString("UserImage", "");
        Color_code = login.getString("ColorCode", "");
        Ins_name = login.getString("UserName", "");
        Cube_Update = getIntent().getStringExtra("cube_update");
        new GetCategory().execute();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        cube_code = (EditText) findViewById(R.id.cube_code);
        toggleButton = (ToggleButton) findViewById(R.id.security_switch);
        note_layout = (LinearLayout) findViewById(R.id.note);
        location_layout = (LinearLayout) findViewById(R.id.location);
        city_state_layout = (LinearLayout) findViewById(R.id.city_state_layout);
        website_layout = (LinearLayout) findViewById(R.id.website);
        mail_layout = (LinearLayout) findViewById(R.id.mail);
        contact_layout = (LinearLayout) findViewById(R.id.contact);
        add_cube_img = (LinearLayout) findViewById(R.id.add_cube_img);
        main = (LinearLayout) findViewById(R.id.main);

        cube_create_back = (ImageView) findViewById(R.id.cube_create_back);
        cube_code_info = (ImageView) findViewById(R.id.cube_code_info);
        cube_img = (ImageView) findViewById(R.id.cube_img);

        disnotes = (EditText) findViewById(R.id.topictxt);
        dislocation = (EditText) findViewById(R.id.locationtxt);
        dismail = (EditText) findViewById(R.id.mailtxt);
        discontact = (EditText) findViewById(R.id.contacttxt);
        diswebsite = (EditText) findViewById(R.id.websitetxt);
        cube_nametxt = (EditText) findViewById(R.id.cube_nametxt);
        cube_descriptiontxt = (EditText) findViewById(R.id.cube_descriptiontxt);
        cube_category = (TextView) findViewById(R.id.cube_category);
        city = (ImageView) findViewById(R.id.city);
        state = (ImageView) findViewById(R.id.state);
        country = (ImageView) findViewById(R.id.country);

        invite_people = (Button) findViewById(R.id.invite_people);

        city.setOnClickListener(this);
        state.setOnClickListener(this);
        country.setOnClickListener(this);

        note_layout.setOnClickListener(this);
        location_layout.setOnClickListener(this);
        website_layout.setOnClickListener(this);
        mail_layout.setOnClickListener(this);
        contact_layout.setOnClickListener(this);
        invite_people.setOnClickListener(this);
        add_cube_img.setOnClickListener(this);
        cube_img.setOnClickListener(this);
        cube_create_back.setOnClickListener(this);
        cube_code_info.setOnClickListener(this);
        cube =getSharedPreferences("cube",MODE_PRIVATE);
        cubecountry = cube.getString("country","");




        if (Cube_Update.contains("1")) {

            Cube_Id = getIntent().getStringExtra("cube_id");
            Cube_name = getIntent().getStringExtra("cube_name");
            Security = getIntent().getStringExtra("cube_type");
            SecurityCode = getIntent().getStringExtra("cube_code");
            Cube_Image =getIntent().getStringExtra("cube_image");
            Cube_Catname=getIntent().getStringExtra("cube_cat");
            Cube_location=getIntent().getStringExtra("cube_location");
            Cube_mail=getIntent().getStringExtra("cube_mail");
            Cube_Web=getIntent().getStringExtra("cube_web");
            Cube_Contact=getIntent().getStringExtra("cube_contact");
            Cube_Description=getIntent().getStringExtra("cube_des");
            setData();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    isCheckedd = "1";

                    Security = "Close";

                    cube_code.setVisibility(View.VISIBLE);
                    cube_code.setText("");

                    ImageView close;

                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                    final View customVie = inflater.inflate(R.layout.cube_code_info, null);

                    cube_code_popup = new PopupWindow(
                            customVie,
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                    );

                    close = (ImageView) customVie.findViewById(R.id.close);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cube_code_popup.dismiss();
                        }
                    });


                    cube_code_popup.setFocusable(true);
                    cube_code_popup.setAnimationStyle(R.style.popup_window_animation_phone);
                    cube_code_popup.setOutsideTouchable(true);
                    cube_code_popup.showAtLocation(main, Gravity.CENTER, 0, 0);

                    dimBehind(cube_code_popup);


                } else {

                    isCheckedd = "0";

                    Security = "Open";

                    cube_code.setVisibility(View.GONE);

                }

            }
        });


    }

    private void setData() {

        add_cube_img.setVisibility(View.GONE);
        cube_img.setVisibility(View.VISIBLE);

        Glide.with(CreateCube.this).load(getIntent().getStringExtra("cube_image"))
                .override(200, 200).into(cube_img);


        invite_people.setText("Submit");

        cube_nametxt.setText(Cube_name);

        if (Security.contains("Open")) {

            toggleButton.setChecked(false);
            cube_code.setVisibility(View.GONE);

        } else {

            toggleButton.setChecked(true);
            cube_code.setVisibility(View.VISIBLE);

        }

        cube_code.setText(SecurityCode);


    if (!Cube_location.isEmpty())
        {
            dislocation.setVisibility(View.VISIBLE);
            dislocation.setText(cubecountry);

        }

        if (!Cube_mail.isEmpty()) {

            dismail.setVisibility(View.VISIBLE);
            dismail.setText(getIntent().getStringExtra("cube_mail"));

        }


        if (!Cube_Contact.isEmpty()) {

            discontact.setVisibility(View.VISIBLE);
            discontact.setText(getIntent().getStringExtra("cube_contact"));

        }


        if (!Cube_Web.isEmpty()) {

            diswebsite.setVisibility(View.VISIBLE);
            diswebsite.setText(getIntent().getStringExtra("cube_web"));

        }

        cube_descriptiontxt.setText(Cube_Description);

    }


    public class CreateCubeAdapter extends RecyclerView.Adapter<CreateCubeAdapter.ViewHolder> {


        Context con;
        List<CreateCubeCatList> list;

        CreateCubeAdapter a;

        public void setA(CreateCubeAdapter a) {
            this.a = a;
        }

        public CreateCubeAdapter getA() {
            return a;
        }


        public CreateCubeAdapter(Context con, List<CreateCubeCatList> list, CreateCubeAdapter a) {
            this.con = con;
            this.list = list;


        }

        @NonNull
        @Override
        public CreateCubeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_cube_design, parent, false);

            return new CreateCubeAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final CreateCubeAdapter.ViewHolder holder, final int position) {

            Glide.with(con).load(list.get(position).getImg()).override(200, 200)
                    .into(holder.cube_type_img);

            holder.cube_name.setText(list.get(position).getName());

            holder.remove_icon.setVisibility(View.GONE);

            holder.remove_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    list.remove(position);
                    a.notifyDataSetChanged();


                }
            });

          holder.select.setBackgroundResource(R.drawable.unselection_square);
            if (selectedPosition== position)
            {
                holder.select.setBackgroundResource(  R.drawable.selection_square);

            }
            else
            {
                holder.select.setBackgroundResource(  R.drawable.unselection_square);
            }

            if (Cube_Update.contains("1")) {

                for (int i = 0; i < list.size(); i++) {

                    if (getIntent().getStringExtra("cube_cat").contains(list.get(i).getName())) {

                        Cat_id = list.get(i).getCat_id();
                        cube_category.setText(list.get(i).getName());
                        break;

                    }
                }
            }



            holder.main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                list.get(position).setSelect(!list.get(position).isSelect());


                    selectedPosition=position;
                    notifyDataSetChanged();

                    Cat_id = list.get(position).getCat_id();
                    cube_category.setText(list.get(position).getName());

                    Toast.makeText(getApplicationContext(), " " +
                            list.get(position).getName(), Toast.LENGTH_SHORT).show();


                }
            });


        }

        @Override
        public int getItemCount() {

            return list.size();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            ImageView cube_type_img, remove_icon;
            TextView cube_name;
            LinearLayout main;
           final RelativeLayout select;

            public ViewHolder(View itemView) {
                super(itemView);
                select=(RelativeLayout)itemView.findViewById(R.id.slects);
                remove_icon = (ImageView) itemView.findViewById(R.id.remove_icon);
                cube_type_img = (ImageView) itemView.findViewById(R.id.view_cube_img);
                cube_name = (TextView) itemView.findViewById(R.id.cube_name);
                main = (LinearLayout) itemView.findViewById(R.id.main);

            }

        }
    }


    void showKeys(EditText t) {

        t.setVisibility(View.VISIBLE);
        t.setFocusable(true);
        t.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(t,
                InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                selectedImagePath = cursor.getString(columnIndex);

                cursor.close();

                // Set the Image in ImageView after decoding the String
                cube_img.setImageBitmap(BitmapFactory
                        .decodeFile(selectedImagePath));

                cube_img.setVisibility(View.VISIBLE);

                add_cube_img.setVisibility(View.GONE);

                cube_imagefile = new File(selectedImagePath);

                file_available = "1";


            }

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v)
    {
        if (v == cube_code_info) {

            Toast.makeText(getApplicationContext(), "Cube code is user-defined secret code (limited to 10 characters) " +
                    "which is necessary to join a closed cube", Toast.LENGTH_LONG).show();

        }

        if (v == cube_create_back) {

            finish();

        }

        if (v == add_cube_img) {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 100);

        }

        if (v == cube_img) {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 100);

        }


        if (v == note_layout) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            final View mView = inflater.inflate(R.layout.add_topic, null);

            addtopic_popup = new PopupWindow(
                    mView,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );

            addtopic_popup.setFocusable(true);

            addtopic_popup.setAnimationStyle(R.style.popup_window_animation_phone);

            addtopic_popup.showAtLocation(main, Gravity.CENTER, 0, 0);

            dimBehind(addtopic_popup);

        }
        if (v == city)
        {
            if (currentState != null) {
                new GetCity().execute(currentState.get_id());
            }
        }

        if (v == country) {
            new GetCountry().execute();
        }

        if (v == state) {
            if (currentCountry != null) {
                new GetState().execute(currentCountry.get_id());

            }
        }


        if (v == location_layout) {
//            showKeys(dislocation);
            if (city_state_layout.getVisibility() == View.VISIBLE) {
                city_state_layout.setVisibility(View.GONE);

            } else {
                city_state_layout.setVisibility(View.VISIBLE);

            }

        }

        if (v == website_layout) {
            showKeys(diswebsite);
        }

        if (v == mail_layout) {
            showKeys(dismail);
        }

        if (v == contact_layout) {
            showKeys(discontact);
        }

        if (v == invite_people) {

            if (Cube_Update.contains("1")) {

                Cube_name = cube_nametxt.getText().toString();
                SecurityCode = cube_code.getText().toString();


                if (isCheckedd.contains("0"))
                {

                    if (Cube_name != "" && Cat_id != "") {

                            new CreateCubeData().execute();


                    } else if (Cube_name.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Enter the Cube Name", Toast.LENGTH_SHORT).show();

                    } else if (Cat_id.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Select the Category", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    if (!Cube_name.isEmpty() && !SecurityCode.isEmpty() && !Cat_id.isEmpty()) {

                        if (currentCountry == null) {
                            Toast.makeText(getApplicationContext(), "Select your Country!!", Toast.LENGTH_SHORT).show();

                        } else if (currentState == null) {
                            Toast.makeText(getApplicationContext(), "Select your State!!", Toast.LENGTH_SHORT).show();

                        } else if (currentCity == null) {
                            Toast.makeText(getApplicationContext(), "Select your City!!", Toast.LENGTH_SHORT).show();

                        } else {

                            new CreateCubeData().execute();
                        }
                    } else if (Cube_name.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Enter the Cube Name", Toast.LENGTH_SHORT).show();

                    } else if (SecurityCode.isEmpty())
                    {

                        Toast.makeText(getApplicationContext(), "Enter the Security Code", Toast.LENGTH_SHORT).show();

                    } else if (Cat_id.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Select the Category", Toast.LENGTH_SHORT).show();

                    }
                }

            }
            else
                {

                Cube_name = cube_nametxt.getText().toString();
                SecurityCode = cube_code.getText().toString();


                if (isCheckedd.contains("0")) {

                    if (Cube_name != "" && Cat_id != "") {

                        new CreateCubeData().execute();

                    } else if (Cube_name.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Enter the Cube Name", Toast.LENGTH_SHORT).show();

                    } else if (Cat_id.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Select the Category", Toast.LENGTH_SHORT).show();

                    }

                }
                else
                    {

                    if (!Cube_name.isEmpty() && !SecurityCode.isEmpty() && !Cat_id.isEmpty()) {

                        new CreateCubeData().execute();

                    } else if (Cube_name.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Enter the Cube Name", Toast.LENGTH_SHORT).show();

                    } else if (SecurityCode.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Enter the Security Code", Toast.LENGTH_SHORT).show();

                    } else if (Cat_id.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Select the Category", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }
    }


    private class CreateCubeData extends AsyncTask<Integer, Integer, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(CreateCube.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected void onPostExecute(String s)
        {
            try {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                JSONObject res = new JSONObject(s);


                if (s.contains("True")) {

                    try {

                        JSONObject json = new JSONObject(s);

                        if (json.getString("Output").contains("True"))
                        {
                            JSONObject ress = json.getJSONObject("Response");


                                    Intent in = new Intent(CreateCube.this,Invite.class);
                                    in.putExtra("Cube_Id", ress.getString("_id"));
                                    in.putExtra("Cube_User_Id", ress.getString("User_Id"));
                                    in.putExtra("Name",cube_nametxt.getText().toString());
                                    in.putExtra("value","0");
                                    startActivity(in);
                                    finish();

                                }

                    } catch (Exception e) {

                    }
                }
            }
            catch (Exception e)
            {
            }
        }

        @Override
        protected String doInBackground(Integer... params) {

            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost;

            if (Cube_Update.contains("1"))
            {

                httppost = new HttpPost(Config.BASE_URL + "Cubes/Update_cube");

            }
            else
            {
                httppost = new HttpPost(Config.BASE_URL + "Cubes/Cube_Creation");
            }
            try
            {
                AndroidMultiPartEntity localMultipartEntity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (file_available.equals("1")) {

                    localMultipartEntity.addPart("image", new FileBody(cube_imagefile));
                }
                else
                {
                    file_available = "0";

                }

                String state;
                String country;
                String city;

                if (Cube_Update.contains("1"))
                {
                    localMultipartEntity.addPart("Cube_Id", new StringBody(Cube_Id));
                }
                localMultipartEntity.addPart("User_Id", new StringBody(login_user_id));
                localMultipartEntity.addPart("Category_Id", new StringBody(Cat_id));
                localMultipartEntity.addPart("Name", new StringBody(cube_nametxt.getText().toString()));
                localMultipartEntity.addPart("Security", new StringBody(Security));
                localMultipartEntity.addPart("Security_Code", new StringBody(cube_code.getText().toString()));
                localMultipartEntity.addPart("Description", new StringBody(cube_descriptiontxt.getText().toString()));
                localMultipartEntity.addPart("City", new StringBody(new Gson().toJson(currentCity)));
                localMultipartEntity.addPart("Country", new StringBody( new Gson().toJson(currentCountry)));
                localMultipartEntity.addPart("State", new StringBody(new Gson().toJson(currentState)));
                localMultipartEntity.addPart("Web", new StringBody(diswebsite.getText().toString()));
                localMultipartEntity.addPart("Mail", new StringBody(dismail.getText().toString()));
                localMultipartEntity.addPart("Contact", new StringBody(discontact.getText().toString()));
                totalSize = localMultipartEntity.getContentLength();
                httppost.setEntity(localMultipartEntity);
                // Making server call

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200)
                {   // Server response
                    responseString = EntityUtils.toString(r_entity);
                    // Log.d("error--", responseString);
                } else {

                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            }
            catch(ClientProtocolException e)

            {

                responseString = e.toString();

            } catch (IOException e)
            {

                responseString = e.toString();

            }
            file_available = "0";

            return responseString;

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
    private class GetCategory extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(CreateCube.this);
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected void onPostExecute(Object o) {

            adapter = new CreateCubeAdapter(CreateCube.this, grid_element, adapter);
            recyclerView.setAdapter(adapter);

            if (pd.isShowing()) {

                pd.dismiss();

            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                try {

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

                    try
                    {
                        JSONObject json = new JSONObject(result);


                        if (result.contains("True")) {

                            JSONArray res = json.getJSONArray("Response");

                            for (int j = 0; j < res.length(); j++)
                            {
                                JSONObject data = res.getJSONObject(j);
                                grid_element.add(new CreateCubeCatList(data.getString("Name"), data.getString("_id")
                                        ,Config.BASE_URL + "Uploads/Category/" + data.getString("Image")));

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

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }


    private class GetCountry extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(CreateCube.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(CreateCube.this);

                final String url = Config.BASE_URL + "Signin_Signup/Country_List";

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.toString().contains("True")) {

                                        try {

                                            if (response.getString("Output").contains("True")) {
                                                countryMasterList = new ArrayList<>();
                                                currentState = null;
                                                currentCity = null;
                                                JSONArray ress = response.getJSONArray("Response");

                                                for (int i = 0; i < ress.length(); i++) {
                                                    JSONObject data = ress.getJSONObject(i);
                                                    CountryMaster countryMaster = new CountryMaster(data.getString("_id"), data.getString("Country_Name"));
                                                    countryMasterList.add(countryMaster);
                                                }

                                                if (countryMasterList != null && countryMasterList.size() > 0) {
                                                    String[] countryType = new String[countryMasterList.size()];
                                                    for (int i = 0; i < countryMasterList.size(); i++) {
                                                        countryType[i] = countryMasterList.get(i).getCountry_Name();
                                                    }

                                                    AlertDialog.Builder b = new AlertDialog.Builder(CreateCube.this);
                                                    b.setTitle("Country");
                                                    b.setItems(countryType, new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            currentCountry = countryMasterList.get(which);
                                                            Log.i(TAG, "onClick: " + new Gson().toJson(currentCountry));
                                                            dialog.dismiss();

                                                        }

                                                    });

                                                    b.show();
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
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

                Toast.makeText(CreateCube.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

    private class GetState extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(CreateCube.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(CreateCube.this);

                final String url = Config.BASE_URL + "Signin_Signup/State_List/" + objects[0];
                Log.i(TAG, "doInBackground: " + url);
                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.i(TAG, "onResponse: " + response);
                                    if (response.toString().contains("True")) {

                                        try {

                                            if (response.getString("Output").contains("True")) {
                                                stateMasterList = new ArrayList<>();
                                                currentCity = null;
                                                JSONArray ress = response.getJSONArray("Response");

                                                for (int i = 0; i < ress.length(); i++) {
                                                    JSONObject data = ress.getJSONObject(i);
                                                    StateMaster stateMaster = new StateMaster(data.getString("_id"), data.getString("State_Name"));
                                                    stateMasterList.add(stateMaster);
                                                }

                                                if (stateMasterList != null && stateMasterList.size() > 0) {
                                                    String[] stateType = new String[stateMasterList.size()];
                                                    for (int i = 0; i < stateMasterList.size(); i++) {
                                                        stateType[i] = stateMasterList.get(i).getState_Name();
                                                    }

                                                    Log.i(TAG, "onResponse: " + new Gson().toJson(stateType));

                                                    AlertDialog.Builder b1 = new AlertDialog.Builder(CreateCube.this);
                                                    b1.setTitle("State");
                                                    b1.setItems(stateType, new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            currentState = stateMasterList.get(which);
                                                            dialog.dismiss();
                                                        }

                                                    });
                                                    /**/
                                                    b1.show();

                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (pd.isShowing()) {

                                        pd.dismiss();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {

                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                queue.add(getRequest);

            } else {

                Toast.makeText(CreateCube.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

    private class GetCity extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(CreateCube.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(CreateCube.this);

                final String url = Config.BASE_URL + "Signin_Signup/City_List/" + objects[0];

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.toString().contains("True")) {

                                        try {

                                            if (response.getString("Output").contains("True")) {
                                                cityMasterList = new ArrayList<>();
                                                JSONArray ress = response.getJSONArray("Response");

                                                for (int i = 0; i < ress.length(); i++) {
                                                    JSONObject data = ress.getJSONObject(i);
                                                    CityMaster cityMaster = new CityMaster(data.getString("_id"), data.getString("City_Name"));
                                                    cityMasterList.add(cityMaster);
                                                }

                                                if (cityMasterList != null && cityMasterList.size() > 0) {
                                                    String[] countryType = new String[cityMasterList.size()];
                                                    for (int i = 0; i < cityMasterList.size(); i++) {
                                                        countryType[i] = cityMasterList.get(i).getCity_Name();
                                                    }

                                                    AlertDialog.Builder b = new AlertDialog.Builder(CreateCube.this);
                                                    b.setTitle("City");
                                                    b.setItems(countryType, new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            currentCity = cityMasterList.get(which);
                                                            dialog.dismiss();
                                                        }

                                                    });

                                                    b.show();
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
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

                Toast.makeText(CreateCube.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

}
