package com.test.aashi.inscubenewbuild;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import Fragment.MultipleFragmentPost;
import GetterSetter.CityMaster;
import GetterSetter.CountryMaster;
import GetterSetter.CubeCategoeyList;
import GetterSetter.StateMaster;

import static android.content.ContentValues.TAG;


public class ProfileCompletion extends AppCompatActivity implements View.OnClickListener {

    ImageView  color_info;
    TextView dob, gender, city1, state1, country1;
    PopupWindow city_popup, country_popup, gender_popup;
    LinearLayout main, color1, color2, color3, color4, color5, color6, color7, color8, color9, color10;
    int year = 0, month = 0, date = 0;
    String date_format = null, date_dob = "", color_code, login_user_id, Ins_name, User_Email, selected_color = "color1",
            city_name = "", state_name = "",
            country_name = "", gender_name = "", selectedImagePath = "", User_pimg = "", file_available = "0", update_field = "",
            Intent_Color_code = "", Intent_User_Image = "", Intent_DOB = "", Intent__City = "", Intent_State = "", Intent_Country = "",
            Intent_Gender = "", Intent_Hash_Tag_1 = "", Intent_Hash_Tag_2 = "", Intent_Hash_Tag_3 = "";
    Button discover_cubeb;
    TextView textView;
    EditText hash1, hash2, hash3;
    SharedPreferences login;
    CircularImageView profile_img;
    float totalSize;
    File profile_imagefile;
    AlertDialog alertDialog,alertDialog1,alertDialog2;
    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor edit;
      String cityObj, stateObj, countryObj;
    SharedPreferences location;

    List<CountryMaster> countryMasterList = null;
    List<StateMaster> stateMasterList = null;
    List<CityMaster> cityMasterList = null;

    CountryMaster currentCountry = null;
    StateMaster currentState = null;
    CityMaster currentCity = null;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_completion);

        update_field = getIntent().getStringExtra("update");

       location =getSharedPreferences("location",MODE_PRIVATE);
       countryObj = location.getString("country","");
       stateObj=location.getString("state","");
       cityObj=location.getString("city","");


        login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);

        login_user_id = login.getString("_id", "");
        Ins_name = login.getString("UserName", "");
        User_Email = login.getString("UserEmail", "");
//        login_user_id = "5acc5d6e1295332c28f7e205";
//        Ins_name = "Venky";
//        User_Email = "test001@gmail.com";
//        update_field = "0";
        Log.i(TAG, "onCreate: " + login_user_id);
        Log.i(TAG, "onCreate: " + Ins_name);
        Log.i(TAG, "onCreate: " + User_Email);
        discover_cubeb = (Button) findViewById(R.id.discover_cubeb);

        if (update_field.contains("1"))
        {
            discover_cubeb.setText("Update");
        }

        profile_img = (CircularImageView) findViewById(R.id.profile_img);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(Ins_name);

        dob = (TextView) findViewById(R.id.dob1);
        gender = (TextView) findViewById(R.id.gender1);
        city1 = (TextView) findViewById(R.id.city1);
        state1 = (TextView) findViewById(R.id.state1);
        country1 = (TextView) findViewById(R.id.country1);
        color_info = (ImageView) findViewById(R.id.color_info);
        main = (LinearLayout) findViewById(R.id.main);
        color1 = (LinearLayout) findViewById(R.id.color1);
        color2 = (LinearLayout) findViewById(R.id.color2);
        color3 = (LinearLayout) findViewById(R.id.color3);
        color4 = (LinearLayout) findViewById(R.id.color4);
        color5 = (LinearLayout) findViewById(R.id.color5);
        color6 = (LinearLayout) findViewById(R.id.color6);
        color7 = (LinearLayout) findViewById(R.id.color7);
        color8 = (LinearLayout) findViewById(R.id.color8);
        color9 = (LinearLayout) findViewById(R.id.color9);
        color10 = (LinearLayout) findViewById(R.id.color10);

        hash1 = (EditText) findViewById(R.id.hash1);
        hash2 = (EditText) findViewById(R.id.hash2);
        hash3 = (EditText) findViewById(R.id.hash3);

        dob.setOnClickListener(this);
        gender.setOnClickListener(this);
        city1.setOnClickListener(this);
        state1.setOnClickListener(this);
        country1.setOnClickListener(this);
        color_info.setOnClickListener(this);
        discover_cubeb.setOnClickListener(this);

        profile_img.setOnClickListener(this);

        color1.setOnClickListener(this);
        color2.setOnClickListener(this);
        color3.setOnClickListener(this);
        color4.setOnClickListener(this);
        color5.setOnClickListener(this);
        color6.setOnClickListener(this);
        color7.setOnClickListener(this);
        color8.setOnClickListener(this);
        color9.setOnClickListener(this);
        color10.setOnClickListener(this);

        if (update_field.contains("0")) {

            color_select(color1);
            selected_color = "color1";

        }
        else if (update_field.contains("1"))
        {

            Intent_User_Image = getIntent().getStringExtra("UserImage");
            Intent_Color_code = getIntent().getStringExtra("ColorCode");
            Intent_DOB = getIntent().getStringExtra("DOB");
            Intent__City = getIntent().getStringExtra("City");
            Intent_State = getIntent().getStringExtra("State");
            Intent_Country = getIntent().getStringExtra("Country");
            Intent_Gender = getIntent().getStringExtra("Gender");
            Intent_Hash_Tag_1 = getIntent().getStringExtra("Hash_Tag_1");
            Intent_Hash_Tag_2 = getIntent().getStringExtra("Hash_Tag_2");
            Intent_Hash_Tag_3 = getIntent().getStringExtra("Hash_Tag_3");

            date_dob = Intent_DOB;
            gender_name = Intent_Gender;
            city_name = Intent__City;
            country_name = Intent_Country;

            color_set();

            hash1.setText(Intent_Hash_Tag_1);
            hash2.setText(Intent_Hash_Tag_2);
            hash3.setText(Intent_Hash_Tag_3);

            Glide.with(ProfileCompletion.this)
                    .load(Intent_User_Image).override(200, 200)
                    .into(profile_img);
            dob.setText(Intent_DOB);
            gender.setText(Intent_Gender);
            state1.setText(stateObj);
            country1.setText(countryObj);
            city1.setText(cityObj);


        }

    }


    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void color_set() {
        if (Intent_Color_code.contains("color1")) {

            color_select(color1);
            selected_color = Intent_Color_code;

            setTheme(R.style.AppTheme);


        } else if (Intent_Color_code.contains("color2")) {

            color_select(color2);
            selected_color = Intent_Color_code;

            setTheme(R.style.green);

        } else if (Intent_Color_code.contains("color3")) {

            color_select(color3);
            selected_color = Intent_Color_code;

        } else if (Intent_Color_code.contains("color4")) {

            color_select(color4);
            selected_color = Intent_Color_code;

        } else if (Intent_Color_code.contains("color5")) {

            color_select(color5);
            selected_color = Intent_Color_code;

        } else if (Intent_Color_code.contains("color6")) {

            color_select(color6);
            selected_color = Intent_Color_code;

        } else if (Intent_Color_code.contains("color7")) {

            color_select(color7);
            selected_color = Intent_Color_code;

        } else if (Intent_Color_code.contains("color8")) {

            color_select(color8);
            selected_color = Intent_Color_code;

        } else if (Intent_Color_code.contains("color9")) {

            color_select(color9);
            selected_color = Intent_Color_code;

        } else if (Intent_Color_code.contains("color10")) {

            color_select(color10);
            selected_color = Intent_Color_code;

        }
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
                profile_img.setImageBitmap(BitmapFactory
                        .decodeFile(selectedImagePath));

                profile_imagefile = new File(selectedImagePath);

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


        if (v == profile_img)
        {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 100);
        }

        if (v == discover_cubeb) {

            if (isNetworkConnected()) {

                if(currentCountry==null)
                {
                    Toast.makeText(getApplicationContext(), "Select your Country!!", Toast.LENGTH_SHORT).show();
                }
                else if(currentState==null){
                    Toast.makeText(getApplicationContext(), "Select your State!!", Toast.LENGTH_SHORT).show();

                }else if(currentCity==null){
                    Toast.makeText(getApplicationContext(), "Select your City!!", Toast.LENGTH_SHORT).show();

                }else
                {
                    new ProfileDataSend().execute();
                }


            } else {

                Toast.makeText(getApplicationContext(), "Check Your Internet Connection!!", Toast.LENGTH_SHORT).show();

            }

        }

        if (v == state1) {
            if (currentCountry != null) {
                new GetState().execute(currentCountry.get_id());
            }
        }

        if (v == city1) {

/*            Button city_submit;
            final EditText city_txt;

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            final View customVie = inflater.inflate(R.layout.city_popup, null);

            city_popup = new PopupWindow(
                    customVie,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );

            city_submit = (Button) customVie.findViewById(R.id.city_submit);
            city_txt = (EditText) customVie.findViewById(R.id.city_txt);

            if (update_field.contains("1")) {

                city_txt.setText(Intent__City);

            }

            city_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    city_name = city_txt.getText().toString();

                    if (update_field.contains("1")) {

                        Intent__City = city_txt.getText().toString();
                        ;

                    }

                    city_popup.dismiss();

                }
            });

            city_popup.setFocusable(true);
            city_popup.setAnimationStyle(R.style.popup_window_animation_phone);


            city_popup.showAtLocation(main, Gravity.CENTER, 0, 0);

            dimBehind(city_popup);*/
            if (currentState != null) {
                new GetCity().execute(currentState.get_id());
            }
        }

        if (v == country1) {
            new GetCountry().execute();

  /*          Button country_submit;
            final EditText country_txt;

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            final View customVie = inflater.inflate(R.layout.country_popup, null);

            country_popup = new PopupWindow(
                    customVie,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );

            country_submit = (Button) customVie.findViewById(R.id.country_submit);
            country_txt = (EditText) customVie.findViewById(R.id.country_txt);

            if (update_field.contains("1")) {

                country_txt.setText(Intent_Country);

            }

            country_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    country_name = country_txt.getText().toString();
                    if (update_field.contains("1")) {

                        Intent_Country = country_txt.getText().toString();
                        ;

                    }
                    Intent_Country = country_name;
                    country_popup.dismiss();

                }
            });
            country_popup.setFocusable(true);
            country_popup.setAnimationStyle(R.style.popup_window_animation_phone);
            country_popup.showAtLocation(main, Gravity.CENTER, 0, 0);

            dimBehind(country_popup);*/

        }

        if (v == gender) {

            Button gender_submit;

            final RadioGroup gender_radio_gropu;

            RadioButton r1male, r2female, r3other;

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            final View customVi = inflater.inflate(R.layout.gender_popup, null);
            gender_popup = new PopupWindow
            (
                    customVi,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            gender_submit = (Button) customVi.findViewById(R.id.gender_submit);

            gender_radio_gropu = (RadioGroup) customVi.findViewById(R.id.gender_radio_gropu);
            r1male = (RadioButton) customVi.findViewById(R.id.r1male);
            r2female = (RadioButton) customVi.findViewById(R.id.r2female);
            r3other = (RadioButton) customVi.findViewById(R.id.r3other);

            if (update_field.contains("1")) {


                if (Intent_Gender.isEmpty()) {

                    gender_radio_gropu.clearCheck();

                } else {

                    if (Intent_Gender.contains("Male")) {

                        r1male.setChecked(true);

                    } else if (Intent_Gender.contains("Female")) {

                        r2female.setChecked(true);

                    } else if (Intent_Gender.contains("Other")) {

                        r3other.setChecked(true);

                    }

                }

            }

            gender_popup.setFocusable(true);
            gender_popup.setAnimationStyle(R.style.popup_window_animation_phone);

            gender_popup.showAtLocation(main, Gravity.CENTER, 0, 0);


            gender_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int selectedID = 123;
                    final RadioButton rb;

                    selectedID = gender_radio_gropu.getCheckedRadioButtonId();

                    if (selectedID != 123) {

                        rb = (RadioButton) customVi.findViewById(selectedID);

                        gender_name = rb.getText().toString();

                        if (update_field.contains("1")) {

                            Intent_Gender = rb.getText().toString();
                        }

                    } else {
                        Intent_Gender = "";
                        gender_name = "";


                    }
                        gender.setText(gender_name);
                    gender_popup.dismiss();
                }

            });

            dimBehind(gender_popup);
        }

        if (v == dob) {

            date_dob = datePicker();

        }

        if (v == color_info) {

            Toast.makeText(getApplicationContext(), "Your app will be redesigned based on your chosen profile color. You can change it anytime"
                    , Toast.LENGTH_LONG).show();

        }

        if (v == color1) {

            color_select(color1);
            selected_color = "color1";
        }

        if (v == color2) {

            color_select(color2);
            selected_color = "color2";
        }

        if (v == color3) {

            color_select(color3);
            selected_color = "color3";
        }

        if (v == color4) {
            color_select(color4);
            selected_color = "color4";

        }

        if (v == color5) {
            color_select(color5);
            selected_color = "color5";
        }

        if (v == color6) {

            color_select(color6);
            selected_color = "color6";
        }

        if (v == color7) {

            color_select(color7);
            selected_color = "color7";
        }

        if (v == color8) {
            color_select(color8);
            selected_color = "color8";

        }

        if (v == color9) {

            color_select(color9);
            selected_color = "color9";
        }

        if (v == color10) {
            color_select(color10);
            selected_color = "color10";
        }

    }


    void color_select(LinearLayout linearLayout) {

        color1.setBackgroundColor(0);
        color2.setBackgroundColor(0);
        color3.setBackgroundColor(0);
        color4.setBackgroundColor(0);
        color5.setBackgroundColor(0);
        color6.setBackgroundColor(0);
        color7.setBackgroundColor(0);
        color8.setBackgroundColor(0);
        color9.setBackgroundColor(0);
        color10.setBackgroundColor(0);

        linearLayout.setBackgroundColor(getResources().getColor(R.color.Inscube_match));
    }

    public void date_parse(String da) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date d = sdf.parse(da);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            month = Integer.parseInt(checkDigit(cal.get(Calendar.MONTH)));
            date = Integer.parseInt(checkDigit(cal.get(Calendar.DATE)));
            year = Integer.parseInt(checkDigit(cal.get(Calendar.YEAR)));

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private String datePicker() {

        if (update_field.contains("1")) {

            date_parse(Intent_DOB);

        }

        final Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        date_format = df.format(c.getTime());



        if (!update_field.contains("1")) {

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            date = c.get(Calendar.DAY_OF_MONTH);

        }


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_dob = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        Intent_DOB = date_dob;
                        dob.setText(date_dob);


                    }
                }, year, month, date);


        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() );

        datePickerDialog.show();

        return date_dob;

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

    private class ProfileDataSend extends AsyncTask<Integer, Integer, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ProfileCompletion.this);
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected String doInBackground(Integer... params) {


            return uploadFile();

        }


        @Override
        protected void onPostExecute(String result) {

            try {
                if (pd.isShowing()) {
                    pd.dismiss();

                }

                JSONObject res = new JSONObject(result);


                if (result.contains("True")) {
                    try {
                        JSONObject json = new JSONObject(result);

                        if (json.getString("Output").contains("True")) {


                            JSONObject data = json.getJSONObject("Response");

                            color_code = data.getString("Color_Code");

                            if (data.getString("Image").contains("http")) {

                                User_pimg = data.getString("Image");

                            } else {

                                User_pimg = Config.User_profile_Image + data.getString("Image");

                            }


                            //Storing user_Information
                            SharedPreferences.Editor edit = getSharedPreferences("Logged_in_details", MODE_PRIVATE).edit();
                            edit.putString("Color_code", "color1");
                            edit.putString("UserImage", User_pimg);
                            edit.apply();

                            SharedPreferences.Editor loggedin = getSharedPreferences("login_session", MODE_PRIVATE).edit();
                            loggedin.putString("val", "1");
                            loggedin.apply();

                            if (!update_field.contains("1"))
                            {
                                edit  = getSharedPreferences("location", MODE_PRIVATE).edit();
                                edit.putString("country",currentCountry.getCountry_Name());
                                edit.putString("state",currentState.getState_Name());
                                edit.putString("city",currentCity.getCity_Name());
                                edit.apply();
                                Intent i = new Intent(ProfileCompletion.this, DiscoverCube.class);
                                i.putExtra("feeds","0");
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

                            }
                            else
                            {
                                edit  = getSharedPreferences("location", MODE_PRIVATE).edit();
                                edit.putString("country",currentCountry.getCountry_Name());
                                edit.putString("state",currentState.getState_Name());
                                edit.putString("city",currentCity.getCity_Name());
                                edit.apply();
                                Intent i = new Intent(ProfileCompletion.this, ProfileView.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.BASE_URL + "Signin_Signup/Register_Completion");

            try {



                AndroidMultiPartEntity localMultipartEntity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                if (file_available.contains("1")) {

                    localMultipartEntity.addPart("image", new FileBody(profile_imagefile));
                }


                localMultipartEntity.addPart("User_Id", new StringBody(login_user_id));
                localMultipartEntity.addPart("Color_Code", new StringBody(selected_color));
                localMultipartEntity.addPart("DOB", new StringBody(date_dob));
                localMultipartEntity.addPart("City", new StringBody(new Gson().toJson(currentCity)));
                localMultipartEntity.addPart("Country", new StringBody(new Gson().toJson(currentCountry)));
                localMultipartEntity.addPart("State", new StringBody(new Gson().toJson(currentState)));
                localMultipartEntity.addPart("Gender", new StringBody(gender_name));
                localMultipartEntity.addPart("Hash_Tag_1", new StringBody(hash1.getText().toString()));
                localMultipartEntity.addPart("Hash_Tag_2", new StringBody(hash2.getText().toString()));
                localMultipartEntity.addPart("Hash_Tag_3", new StringBody(hash3.getText().toString()));


                totalSize = localMultipartEntity.getContentLength();

                httppost.setEntity(localMultipartEntity);

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
            file_available = "0";

            Log.i(TAG, "uploadFile: " + responseString);
            return responseString;

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

            pd = new ProgressDialog(ProfileCompletion.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(ProfileCompletion.this);

                final String url = Config.BASE_URL + "Signin_Signup/Country_List";

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @SuppressLint("NewApi")
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
                                                    CountryMaster countryMaster = new CountryMaster(data.getString("_id"),
                                                            data.getString("Country_Name"));
                                                    countryMasterList.add(countryMaster);
                                                    Collections.sort(countryMasterList, new Comparator<CountryMaster>() {
                                                        @Override
                                                        public int compare(CountryMaster o1, CountryMaster o2) {
                                                            return o1.getCountry_Name().compareTo(o2.getCountry_Name());
                                                        }
                                                    });
                                                }

                                                if (countryMasterList != null && countryMasterList.size() > 0) {
                                                    String[] countryType = new String[countryMasterList.size()];
                                                    for (int i = 0; i < countryMasterList.size(); i++) {
                                                        countryType[i] = countryMasterList.get(i).getCountry_Name();
                                                    }

                                                    AlertDialog.Builder b = new AlertDialog.Builder(ProfileCompletion.this);

                                                    LayoutInflater inflater=getLayoutInflater();
                                                    final View dialogView = inflater.inflate(R.layout.countrylist, null);
                                                    final ListView county;
                                                    county=dialogView.findViewById(R.id.country_list);
                                                    county.setVisibility(View.VISIBLE);
                                                    b.setView(dialogView);
                                                    final TextView country;
                                                    country=dialogView.findViewById(R.id.select_country);
                                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                                            (ProfileCompletion.this,
                                                                    R.layout.listview, countryType);
                                                    adapter.notifyDataSetChanged();
                                                    county.setAdapter(adapter);

                                                    county.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                            currentCountry = countryMasterList.get(position);
                                                            Log.i(TAG, "onClick: " + new Gson().toJson(currentCountry));
                                                            alertDialog.dismiss();
                                                            country1.setText(parent.getItemAtPosition(position).toString());
                                                        }
                                                    });
                                                    alertDialog=b.create();
                                                    alertDialog.show();
                                                    Button ok=(Button)dialogView. findViewById(R.id.country_ok);


                                                }
                                            }
                                        }
                                        catch (Exception e)
                                        {


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

                Toast.makeText(ProfileCompletion.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

    private class GetState extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(ProfileCompletion.this);
            pd.setMessage("Loading...");
            pd.show();
        }
        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(ProfileCompletion.this);

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
                                        try
                                        {

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

                                                    AlertDialog.Builder b = new AlertDialog.Builder(ProfileCompletion.this);

                                                    LayoutInflater inflater=getLayoutInflater();
                                                    final View dialogView = inflater.inflate(R.layout.select_state, null);
                                                    final ListView county;
                                                    county=dialogView.findViewById(R.id.country_list1);
                                                    county.setVisibility(View.VISIBLE);
                                                    b.setView(dialogView);
                                                    final AutoCompleteTextView country;
                                                 //   country=dialogView.findViewById(R.id.select_country1);
                                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                                            (ProfileCompletion.this,
                                                                    R.layout.listview, stateType);
                                                    // currentCountry = countryMasterList.get(which);
                                                   // country.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                    county.setAdapter(adapter);
                                                    adapter.sort(new Comparator<String>() {
                                                        @Override
                                                        public int compare(String o1, String o2) {
                                                            return o1.compareTo(o2);
                                                        }
                                                    });
                                                  //  country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                   //     @Override
                                                     //   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                       //     country.setText(parent.getItemAtPosition(position).toString());
                                                       // }
                                                    //});
                                                    county.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                       currentState = stateMasterList.get(position);
                                                            alertDialog1.dismiss();
                                                           state1.setText(parent.getItemAtPosition(position).toString());
                                                        }
                                                    });
                                                    alertDialog1=b.create();
                                                    alertDialog1.show();

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
                            public void onErrorResponse(VolleyError error) {


                            }
                        }
                );

                getRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                queue.add(getRequest);

            } else {

                Toast.makeText(ProfileCompletion.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    }

    private class GetCity extends AsyncTask {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(ProfileCompletion.this);
            pd.setMessage("Loading...");
            pd.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(ProfileCompletion.this);

                final String url = Config.BASE_URL + "Signin_Signup/City_List/" + objects[0];

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.toString().contains("True")) {

                                        try {

                                            if (response.getString("Output").contains("True"))
                                            {
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

                                                    AlertDialog.Builder b = new AlertDialog.Builder(ProfileCompletion.this);

                                                    LayoutInflater inflater=getLayoutInflater();
                                                    final View dialogView = inflater.inflate(R.layout.select_city, null);
                                                    final ListView county;
                                                    county=dialogView.findViewById(R.id.country_list2);
                                                    county.setVisibility(View.VISIBLE);
                                                    b.setView(dialogView);
                                                    final AutoCompleteTextView country;
                                                    country=dialogView.findViewById(R.id.select_country2);
                                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                                            (ProfileCompletion.this,
                                                                    R.layout.listview, countryType);
                                                    // currentCountry = countryMasterList.get(which);
                                                    country.setAdapter(adapter);
                                                    county.setAdapter(adapter);
                                                    adapter.sort(new Comparator<String>() {
                                                        @Override
                                                        public int compare(String o1, String o2) {
                                                            return o1.compareTo(o2);
                                                        }
                                                    });
                                                     county.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            currentCity = cityMasterList.get(position);


                                                            alertDialog2.dismiss();
                                                            city1.setText(parent.getItemAtPosition(position).toString());
                                                        }
                                                    });
                                                    country.clearListSelection();
                                                    Log.i(TAG, "onClick: " + new Gson().toJson(currentCountry));
                                                    alertDialog2=b.create();
                                                    alertDialog2.show();
                                                }
                                            }
                                        }

                                        catch (Exception e) {

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
                }
            else
                {
                Toast.makeText(ProfileCompletion.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            return null;
        }

    }
}


