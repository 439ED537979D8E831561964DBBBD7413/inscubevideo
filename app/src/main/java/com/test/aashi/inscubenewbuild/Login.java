package com.test.aashi.inscubenewbuild;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button loginb, signupb, forgot_passb;
    ProgressDialog pb;
    EditText uname, upass;
    String user_name, user_pass, User_ID, Ins_Name, User_Email, User_Pimage, DeviceToken = "", color_code = "";
    SharedPreferences sharedPreferences;
    String launch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginb = (Button) findViewById(R.id.loginb);
        signupb = (Button) findViewById(R.id.signupb);
        forgot_passb = (Button) findViewById(R.id.forgot_passb);
        sharedPreferences =getApplicationContext().getSharedPreferences("explain",MODE_PRIVATE);

        launch = sharedPreferences.getString("explains","");

        uname = (EditText) findViewById(R.id.emailtxt);
        upass = (EditText) findViewById(R.id.passtxt);

        SharedPreferences sharedPreferences = getSharedPreferences("firebase", MODE_PRIVATE);
        DeviceToken = sharedPreferences.getString("refreshToken", "");

        if (DeviceToken.equalsIgnoreCase("")) {
            SharedPreferences.Editor edit= sharedPreferences.edit();
            edit.putString("refreshToken", FirebaseInstanceId.getInstance().getToken());
            edit.commit();
            DeviceToken= FirebaseInstanceId.getInstance().getToken();
        }

        Log.i("Test", "onCreate: " + DeviceToken);

        forgot_passb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Login.this, ForgotPassword.class);
                startActivity(in);
            }
        });

        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_name = uname.getText().toString();
                user_pass = upass.getText().toString();

                if (!user_name.isEmpty() && !user_pass.isEmpty()) {

                    new login_task().execute();

                } else if (user_name.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Please enter the email", Toast.LENGTH_SHORT).show();

                } else if (user_pass.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();

                }

            }
        });

        signupb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);

            }
        });


    }

    class login_task extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pb = new ProgressDialog(Login.this);
            pb.setMessage("Loggin In");
            pb.setCancelable(false);
            pb.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pb != null) {
                pb.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (isNetworkConnected()) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = Config.BASE_URL + "Signin_Signup/User_Validate";
                Log.i("Test", "doInBackground: " + url);
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


                                            JSONObject data = json.getJSONObject("Response");

                                            User_ID = data.getString("_id");
                                            Ins_Name = data.getString("Inscube_Name");
                                            User_Email = data.getString("Email");
                                            color_code = data.getString("Color_Code");

                                            if (data.getString("Image").contains("http")) {

                                                User_Pimage = data.getString("Image");

                                            } else {

                                                User_Pimage = Config.User_profile_Image + data.getString("Image");

                                            }
                                            //Storing user_Information
                                            SharedPreferences.Editor edit = getSharedPreferences("Logged_in_details", MODE_PRIVATE).edit();
                                            edit.putString("_id", User_ID);
                                            edit.putString("UserName", Ins_Name);
                                            edit.putString("UserEmail", User_Email);
                                            edit.putString("UserImage", User_Pimage);
                                            edit.putString("ColorCode", color_code);
                                            edit.apply();

                                            SharedPreferences.Editor loggedin = getSharedPreferences("login_session", MODE_PRIVATE).edit();
                                            loggedin.putString("val", "1");
                                            loggedin.apply();


                                            if (launch.contains("1"))
                                            {

                                                Intent i = new Intent(Login.this, StoryPage.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                finish();
                                            }
                                            else
                                            {
                                                Intent i = new Intent(Login.this, ReadActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                finish();
                                            }

                                            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

                                        } else {

                                            Toast.makeText(Login.this, " " + json.getString("Message"), Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else {

                                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> para = new HashMap<String, String>();

                        para.put("Email", user_name);
                        para.put("Password", user_pass);
                        para.put("Firebase_Token", DeviceToken);
                        return para;
                    }
                };
                // Add the request to the RequestQueue.
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
                queue.add(stringRequest);
            } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }


    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
