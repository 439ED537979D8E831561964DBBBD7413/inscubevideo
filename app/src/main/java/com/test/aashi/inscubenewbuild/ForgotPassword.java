package com.test.aashi.inscubenewbuild;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    ImageView forgot_pass_back;
    Button send_email, otp_verify, new_pass_submit;
    PopupWindow email;
    LinearLayout verify_layout, newpass_layout, send_email_layout;
    EditText codetxt, passwordtxt;
    SharedPreferences login;
    String login_user_id = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);

        // login_user_id = login.getString("_id", "");
        forgot_pass_back = (ImageView) findViewById(R.id.forgot_pass_back);
        send_email = (Button) findViewById(R.id.send_email);
        otp_verify = (Button) findViewById(R.id.otp_verify);
        new_pass_submit = (Button) findViewById(R.id.new_pass_submit);
        codetxt = (EditText) findViewById(R.id.codetxt);
        passwordtxt = (EditText) findViewById(R.id.passwordtxt);
        verify_layout = (LinearLayout) findViewById(R.id.verify_layout);
        newpass_layout = (LinearLayout) findViewById(R.id.newpass_layout);
        send_email_layout = (LinearLayout) findViewById(R.id.send_email_layout);
        otp_verify.setEnabled(false);
        codetxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (codetxt.getText().toString().length() == 6) {
                    otp_verify.setEnabled(true);
                }
              }
            }
        );
        new_pass_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordtxt.getText().toString().length() != 0) {
                    RequestQueue MyRequestQueue = Volley.newRequestQueue(ForgotPassword.this);
                    String u = Config.BASE_URL + "Signin_Signup/password_reset_submit/" + passwordtxt.getText().toString() + "/" + login_user_id;

                    StringRequest MyStringRequest = new StringRequest(Request.Method.GET, u, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject json = new JSONObject(response);

                                if (json.getString("Output").contains("True")) {

                                    Toast.makeText(ForgotPassword.this, "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {

                                    Toast.makeText(ForgotPassword.this, "Something went wrong! Please try again later", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e)

                            {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ForgotPassword.this, "Connection Error!!", Toast.LENGTH_SHORT).show();

                        }
                    });

                    MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                    MyRequestQueue.add(MyStringRequest);


                } else {


                }


            }
        });


        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final Button send;
                final EditText emailtxt;

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                final View mView = inflater.inflate(R.layout.get_email, null);

                email = new PopupWindow(
                        mView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                email.setFocusable(true);

                email.setAnimationStyle(R.style.popup_window_animation_phone);

                email.showAtLocation(mView, Gravity.CENTER, 0, 0);
                dimBehind(email);

                emailtxt = mView.findViewById(R.id.get_email);
                send = mView.findViewById(R.id.sendb);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!emailtxt.getText().toString().isEmpty()) {

                            send.setEnabled(false);

                            RequestQueue MyRequestQueue = Volley.newRequestQueue(ForgotPassword.this);

                            String u = Config.BASE_URL + "Signin_Signup/Password_reset_Email_Validate/" + emailtxt.getText().toString();

                            StringRequest MyStringRequest = new StringRequest(Request.Method.GET, u, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        JSONObject json = new JSONObject(response);

                                        if (json.getString("Output").contains("True")) {

                                            login_user_id = json.getString("User_Id");

                                            RequestQueue MyRequestQueue = Volley.newRequestQueue(ForgotPassword.this);

                                            String u = Config.BASE_URL + "Signin_Signup/Send_Email_Password_Reset_OTP/" + emailtxt.getText().toString();

                                            StringRequest MyStringRequest = new StringRequest(Request.Method.GET, u, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {

                                                        JSONObject json = new JSONObject(response);

                                                        if (json.getString("Output").contains("True")) {

                                                            emailtxt.setText("");
                                                            email.dismiss();
                                                            Toast.makeText(ForgotPassword.this, "Please Check Your Mail", Toast.LENGTH_SHORT).show();

                                                            verify_layout.setVisibility(View.VISIBLE);
                                                            send_email_layout.setVisibility(View.GONE);


                                                        } else {

                                                            email.dismiss();
                                                            Toast.makeText(ForgotPassword.this, "Something went wrong! Please try again later", Toast.LENGTH_SHORT).show();

                                                        }
                                                    } catch (JSONException e)

                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(ForgotPassword.this, "Connection Error!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                                            MyRequestQueue.add(MyStringRequest);


                                        } else {

                                            emailtxt.setText("");
                                            Toast.makeText(ForgotPassword.this, "Enter Valid Mail ID", Toast.LENGTH_SHORT).show();
                                            send.setEnabled(true);
                                        }
                                    } catch (JSONException e)

                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(ForgotPassword.this, "Connection Error!!", Toast.LENGTH_SHORT).show();

                                }
                            });

                            MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                            MyRequestQueue.add(MyStringRequest);

                        } else {

                            send.setEnabled(true);
                            Toast.makeText(ForgotPassword.this, "Enter the Mail ID!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });


        otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (codetxt.getText().toString().length() == 6) {


                    RequestQueue MyRequestQueue = Volley.newRequestQueue(ForgotPassword.this);

                    String u = Config.BASE_URL + "Signin_Signup/password_reset_OTP_check/" + login_user_id + "/" + codetxt.getText().toString();

                    StringRequest MyStringRequest = new StringRequest(Request.Method.GET, u, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject json = new JSONObject(response);

                                if (json.getString("Output").contains("True")) {

                                    verify_layout.setVisibility(View.GONE);
                                    newpass_layout.setVisibility(View.VISIBLE);


                                } else {

                                    Toast.makeText(ForgotPassword.this, "Wrong Code!!", Toast.LENGTH_SHORT).show();
                                    codetxt.setText("");

                                }
                            } catch (JSONException e)

                            {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(ForgotPassword.this, "Connection Error!!", Toast.LENGTH_SHORT).show();

                        }
                    });

                    MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));

                    MyRequestQueue.add(MyStringRequest);


                } else {

                    Toast.makeText(ForgotPassword.this, "Enter valid code", Toast.LENGTH_SHORT).show();


                }


            }
        });


        forgot_pass_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
