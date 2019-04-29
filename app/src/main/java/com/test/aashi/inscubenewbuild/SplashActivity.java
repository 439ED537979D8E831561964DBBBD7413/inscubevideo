package com.test.aashi.inscubenewbuild;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {

    float APPVERSION, PLAYSTORE_VERSION;
    PopupWindow update_popup;
    LinearLayout linear;
    SharedPreferences log, login_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        get_playversion();

        PackageManager manager = getPackageManager();
        PackageInfo info = null;

        try
        {
            info = manager.getPackageInfo(getPackageName(), 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        APPVERSION = Float.parseFloat(info.versionName);

        // Toast.makeText(getApplicationContext(), "App-" + APPVERSION, Toast.LENGTH_SHORT).show();
        log = getSharedPreferences("login_session", MODE_PRIVATE);
//        login_details = getSharedPreferences("Logged_in_details", MODE_PRIVATE);
        Handler h = new Handler();
        h.postDelayed(r, 2000);

    }

    Runnable r = new Runnable() {
        @Override
        public void run() {


            if (APPVERSION >= PLAYSTORE_VERSION) {

                start();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                //Uncomment the below code to Set the message and title from the strings.xml file
                //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("New Version Found.Do You Want To Update The App?")
                        .setCancelable(false)
                        .setPositiveButton(Html.fromHtml("<font color='#a80a0a'>UPDATE NOW</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.test.aashi.inscubenewbuild"));
                                startActivity(browserIntent);


                                finish();
                            }
                        }).setNegativeButton(Html.fromHtml("<font color='#000000'>IGNORE!</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        start();

                        dialog.cancel();
                    }
                });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.show();


            }
        }
    };

    void get_playversion() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final String url = Config.BASE_URL + "Signin_Signup/AndroidVersionGet";

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {

                            JSONObject data = response.getJSONObject("data");

                            PLAYSTORE_VERSION = Float.parseFloat(data.getString("Version"));

                            //Toast.makeText(getApplicationContext(), "Play-" + PLAYSTORE_VERSION, Toast.LENGTH_SHORT).show();


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
    }

    void start() {

        if (log.getString("val", "").contains("1")) {

            //  Toast.makeText(getApplicationContext()," "+login_details.getString("_id",""),Toast.LENGTH_SHORT).show();

            Intent login = new Intent(SplashActivity.this, StoryPage.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish();


        } else {

        Intent login = new Intent(SplashActivity.this, Login.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(login);
        finish();

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
        p.dimAmount = 0.9f;
        wm.updateViewLayout(container, p);
    }


}
