package com.test.aashi.inscubenewbuild;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button loginbackb, signup;
    SharedPreferences.Editor edit;
    EditText name_txt, email_txt, pass_txt;
    TextView error_txt, inscubename_error;
    boolean ok = false;
    String reg_uname, reg_email, reg_pass;
    CheckBox checkBox;
    TextView privacy;

    String valid_main = "0", valid_mail = "0", valid_name = "0";


    String login_userid, Ins_name, User_Email, Color_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        checkBox=(CheckBox)findViewById(R.id.checkbok);
        privacy=(TextView)findViewById(R.id.privacy);

        edit = getSharedPreferences("Logged_in_details", MODE_PRIVATE).edit();

        loginbackb = (Button) findViewById(R.id.loginbackb);
        signup = (Button) findViewById(R.id.Signup);

        name_txt = (EditText) findViewById(R.id.reginscube_nametxt);
        email_txt = (EditText) findViewById(R.id.regemailtxt);
        pass_txt = (EditText) findViewById(R.id.regpasstxt);

        error_txt = (TextView) findViewById(R.id.email_error);
        inscubename_error = (TextView) findViewById(R.id.inscubename_error);

        loginbackb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Register.this);
                LayoutInflater inflater=getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.webview, null);
                builder.setView(dialogView);
                WebView browser = (WebView)dialogView. findViewById(R.id.webview);
               browser.getSettings().setJavaScriptEnabled(true);
              browser.setWebChromeClient(new WebChromeClient());
               browser.loadUrl("file:///android_asset/privacy.html");
                AlertDialog alertDialog;
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkBox.setChecked(true);
                        dialog.dismiss();
                    }
                }).create();
                alertDialog=builder.create();
                alertDialog.show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected()) {

                    Is_Valid_Email(email_txt);

                    boolean okk = false;

                    if (valid_mail.contains("1")) {

                        okk = mail_check();
                    }

                    reg_uname = name_txt.getText().toString();
                    reg_email = email_txt.getText().toString();
                    reg_pass = pass_txt.getText().toString();

                    if (reg_uname.isEmpty() || reg_pass.isEmpty() || reg_email.isEmpty() || !checkBox.isChecked())
                    {
                        valid_main = "0";
                    }

                    else
                    {

                        valid_main = "1";
                    }


                    if (valid_main.contains("1") && valid_mail.contains("1") && okk == true && valid_name.contains("1") ) {

                        new SignUp().execute();

                    }
                    else if(!checkBox.isChecked())
                    {
                        Toast.makeText(getApplicationContext(), "Please agree to the terms and privacy policy to proceed", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Enter Valid Details", Toast.LENGTH_SHORT).show();
                    }



                }



                else {

                    Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();

                }
            }
        });

        email_txt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

                Is_Valid_Email(email_txt);

            }

        });


        email_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                } else {

                    if (valid_mail.contains("1")) {

                        if (isNetworkConnected()) {

                            mail_check();

                        } else {
                            Toast.makeText(Register.this, "Please check your Internet Connection",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        name_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {


                } else {

                    if (isNetworkConnected()) {

                        name_check();

                    } else {
                        Toast.makeText(Register.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    private boolean name_check() {

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.BASE_URL + "Signin_Signup/Inscube_Name_Validate/" + name_txt.getText().toString();
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {                        // display response


                        try {
                            if (response.getString("Status").toString().contains("True") &&
                                    response.getString("Available").toString().contains("True")) {

                                inscubename_error.setVisibility(View.GONE);
                                // inscubename_error.setVisibility(View.VISIBLE);
                                // inscubename_error.setText("Name Available");
                                // inscubename_error.setTextColor(getResources().getColor(R.color.green));
                                valid_name = "1";
                                ok = true;
                            }
                            else if ((response.getString("Status").toString().contains("True") &&
                                    response.getString("Available").toString().contains("False"))) {
                                inscubename_error.setVisibility(View.VISIBLE);
                                inscubename_error.setText("Name already exists");
                                inscubename_error.setTextColor(getResources().getColor(R.color.Red));
                                valid_name = "0";
                                ok = false;
                            } else {
                                inscubename_error.setVisibility(View.VISIBLE);
                                inscubename_error.setText("Connection Issue..Please try again later");
                                inscubename_error.setTextColor(getResources().getColor(R.color.Red));
                                valid_name = "0";
                                ok = false;
                            }
                        } catch (JSONException e) {
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


        return ok;
    }


    private void do_signup() {


        RequestQueue MyRequestQueue = Volley.newRequestQueue(Register.this);

        String u = Config.BASE_URL + "Signin_Signup/Register";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //  Toast.makeText(Register.this, " " + response, Toast.LENGTH_SHORT).show();

                if (response.contains("False")) {

                    Toast.makeText(Register.this, "Registration Failed!!", Toast.LENGTH_SHORT).show();

                } else {

                    try {

                        JSONObject res = new JSONObject(response);
                        JSONObject data = res.getJSONObject("Response");
                        login_userid = data.getString("_id");
                        Ins_name = data.getString("Inscube_Name");
                        User_Email = data.getString("Email");
                        Color_code = data.getString("Color_Code");

                        edit.putString("_id", login_userid);
                        edit.putString("UserName", Ins_name);
                        edit.putString("UserEmail", User_Email);
                        edit.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(Register.this, "Welcome to Inscube", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor e = getSharedPreferences("is_new", MODE_PRIVATE).edit();
                    e.putString("is_new", "new");
                    e.apply();


                    SharedPreferences.Editor loggedin = getSharedPreferences("login_session", MODE_PRIVATE).edit();
                    loggedin.putString("val", "1");
                    loggedin.apply();

                    Intent in = new Intent(Register.this, ProfileCompletion.class);
                    in.putExtra("update", "0");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();
                    name_txt.setText("");
                    pass_txt.setText("");
                    email_txt.setText("");
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Register.this, " " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> para = new HashMap<String, String>();

                para.put("Inscube_Name", reg_uname);
                para.put("Email", reg_email);
                para.put("Password", reg_pass);
                para.put("Color_Code", "");

                return para;
            }
        };

        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

        MyRequestQueue.add(MyStringRequest);

    }


    void Is_Valid_Email(EditText edt) {

        if (edt.getText().toString() == null) {

            error_txt.setText("Enter the Mail ID");
            error_txt.setVisibility(View.VISIBLE);
            error_txt.setTextColor(getResources().getColor(R.color.Red));
            valid_mail = "0";

        } else if (isEmailValid(edt.getText().toString()) == false) {
            error_txt.setVisibility(View.VISIBLE);
            error_txt.setText("Invalid Email Address");
            error_txt.setTextColor(getResources().getColor(R.color.Red));
            valid_mail = "0";

        } else {

            error_txt.setVisibility(View.GONE);
            valid_mail = "1";
        }
    }

    boolean mail_check() {

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = Config.BASE_URL + "Signin_Signup/Email_Validate/" + email_txt.getText().toString();

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {                        // display response


                        try {
                            if (response.getString("Status").toString().contains("True") && response.getString("Output").toString().contains("False")) {

                                error_txt.setVisibility(View.VISIBLE);
                                error_txt.setText("Email Format Error");
                                error_txt.setTextColor(getResources().getColor(R.color.Red));
                                valid_mail = "0";
                                ok = false;

                            } else if (response.getString("Status").toString().contains("True") && response.getString("Output").toString().contains("True") && response.getString("Available").toString().contains("True")) {

                                error_txt.setVisibility(View.GONE);
                                // error_txt.setVisibility(View.VISIBLE);
                                // error_txt.setText("Email Available");
                                // error_txt.setTextColor(getResources().getColor(R.color.green));
                                valid_mail = "1";
                                ok = true;

                            } else if ((response.getString("Status").toString().contains("True") && response.getString("Output").toString().contains("True") && response.getString("Available").toString().contains("False"))) {
                                error_txt.setVisibility(View.VISIBLE);
                                error_txt.setText("Email already exists");
                                error_txt.setTextColor(getResources().getColor(R.color.Red));
                                valid_mail = "0";
                                ok = false;
                            } else {
                                error_txt.setVisibility(View.VISIBLE);
                                error_txt.setText("Connection Issue..Please try again later");
                                error_txt.setTextColor(getResources().getColor(R.color.Red));
                                valid_mail = "0";
                                ok = false;
                            }
                        } catch (JSONException e) {
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


        return ok;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();

    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private class SignUp extends AsyncTask {

        private ProgressDialog dialog;

        public SignUp() {

            dialog = new ProgressDialog(Register.this);
        }

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Loading...");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Object o) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            do_signup();


            return null;
        }
    }
}
