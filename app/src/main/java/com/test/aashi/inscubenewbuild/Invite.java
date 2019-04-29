package com.test.aashi.inscubenewbuild;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invite extends AppCompatActivity implements View.OnClickListener {
 String cube_id,user_id,link,name;
 String value="0";
 TextView invite_text;
LinearLayout copylink,invitemail;
Button skip;
JSONArray mail_ids;
ArrayList<String> mail=new ArrayList<>();
PopupWindow popupWindow;
    CallbackManager callbackManager;
    ShareDialog shareDialog ;
    EditText mymail;
    JSONObject jsonObj;
    SharedPreferences login;
    CardView facebook,whatsapp,twitter,others;
    String login_user_id;
    TextView cubejson,cube;
    View popUpView;
    PopupWindow popup;


 @SuppressLint("SetTextI18n")
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
       FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager =CallbackManager.Factory.create();
       shareDialog=new ShareDialog(Invite.this);
        facebook=(CardView)findViewById(R.id.facebook);
     whatsapp=(CardView)findViewById(R.id.whatsapp);
     twitter=(CardView)findViewById(R.id.twitter);
     others=(CardView)findViewById(R.id.others);

     if (BuildConfig.DEBUG) {
         FacebookSdk.setIsDebugEnabled(true);
         FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
     }

     login = getSharedPreferences("Logged_in_details", MODE_PRIVATE);
     login_user_id = login.getString("_id", "");
        value=getIntent().getStringExtra("value");
        user_id=getIntent().getStringExtra("Cube_User_Id");
        cube_id=getIntent().getStringExtra("Cube_Id");
        name=getIntent().getStringExtra("Name");
      popupWindow=new PopupWindow(this);
       link= "http://www.inscube.com/Invite_Cube/"+ cube_id;
        invite_text=(TextView)findViewById(R.id.invite_text);
       invite_text.setText("Invite people to "+ name );
       copylink=(LinearLayout)findViewById(R.id.copy_link);
       invitemail=(LinearLayout)findViewById(R.id.to_mail);
       mail_ids=new JSONArray();
       invitemail.setOnClickListener(this);
       skip=(Button)findViewById(R.id.skip);
       mail=new ArrayList<>();
     final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

     whatsapp.setOnClickListener(this);
     twitter.setOnClickListener(this);
     others.setOnClickListener(this);
       copylink.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v) {
               ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
               ClipData clip = ClipData.newPlainText("label",link);
               clipboard.setPrimaryClip(clip);
               Toast.makeText(getApplicationContext(),"link copied",Toast.LENGTH_LONG).show();
               skip.setText("invite");
           }
       });
       facebook.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (ShareDialog.canShow(ShareLinkContent.class)) {
                   ShareLinkContent linkContent = new ShareLinkContent.Builder()
                           .setContentTitle("INSCUBE group invite")
                           .setContentDescription("click to join "+name+" community in inscube app")
                           .setContentUrl(Uri.parse(link))
                           .build();
                   shareDialog.show(linkContent);


               }
           }
       });
       skip.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (value.equals("0"))
               {
                   Intent in = new Intent(Invite.this, CubeView.class);
                   in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   in.putExtra("Cube_Id", cube_id);
                   in.putExtra("Feeds","0");
                   in.putExtra("Cube_User_Id", user_id);
                   in.putExtra("value","0");
                   startActivity(in);
                   finish();
               }
               else
               {
                   finish();
               }
           }
       });
       invitemail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

            /*   Display display = getWindowManager().getDefaultDisplay();
               Point size = new Point();
               display.getSize(size);
               int width = size.x;
               int height = size.y;

               */
               final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.myvew);
               LayoutInflater inflater = (LayoutInflater) getApplicationContext().
                       getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               popUpView = inflater.inflate(R.layout.mail,  (ViewGroup) findViewById(R.id.my_popup));
               mymail  =(EditText)popUpView.findViewById(R.id.my_mail);
               final Button addmail=(Button) popUpView.findViewById(R.id.addmail);
               final Button sendmail=(Button)popUpView.findViewById(R.id.sendmail);
               ImageView back=(ImageView) popUpView.findViewById(R.id.back4);
               back.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v)
                   {
                       popup.dismiss();
                   }
               });
               jsonObj=new JSONObject();
               cube=(TextView)popUpView.findViewById(R.id.cube_array);
               cubejson=(TextView)popUpView.findViewById(R.id.cubejson);
                   addmail.setOnClickListener(new View.OnClickListener()
                   {
                       @Override
                       public void onClick(View v)
                       {
                           addMail();
                           LinearLayout linearLayout1 = (LinearLayout) popUpView.findViewById(R.id.mail_window);
                           mymail = new EditText(getApplicationContext());
                           mymail.setHint("Enter Mail id");
                           mymail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                           LinearLayout.LayoutParams.WRAP_CONTENT));                           ;
                           linearLayout1.addView(mymail);
                       }
               });
               sendmail.setOnClickListener(new View.OnClickListener()
               {
               @Override
               public void onClick(View v)
               {
                   String mail = mymail.getText().toString();
                   if (!mail.matches(emailPattern))
                   {
                       Toast.makeText(getApplicationContext(),"Invalid Email....!",Toast.LENGTH_LONG).show();
                   }
                   else
                   {
                    send_mail();
                   }
               }
               });
            popup  = new PopupWindow(popUpView,   LinearLayout.LayoutParams.MATCH_PARENT,
                       LinearLayout.LayoutParams.MATCH_PARENT, true);
               popup.setContentView(popUpView);
               popup.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);
           }
       });
    }
    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.whatsapp:
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage ("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT,"INSCUBE group invite \n"+"click to join "+name+
                                " community in inscube app\n"+ link);
                whatsappIntent.setType("text/plain");
                try
                {
                    getApplicationContext(). startActivity (whatsappIntent);
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.twitter:
                Uri screenshotUri = Uri.parse("android.resource://com.video.aashi.invite/drawable/logo");
                String  messages="INSCUBE group invite\nclick to join " +name+ " community in inscube app\n"+ link;
                shareTwitter(messages,screenshotUri);
                break;
            case R.id.others:
                String  messages1="INSCUBE group invite\nnclick to join " +name+ " community in inscube app\n"+ link;
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,messages1);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,"Share Via"));
                break;
                default:
                break;
        }
    }
    private void shareTwitter(String message,Uri image) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.putExtra(Intent.EXTRA_STREAM,image);
        tweetIntent.setType("image/*");
        tweetIntent.setType("text/plain");
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.putExtra(Intent.EXTRA_STREAM,image);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            return "";
        }
    }
    public  void   addMail(){
        jsonObj  = new JSONObject();
        try {
            jsonObj.put("email", mymail.getText().toString()); // Set the first name/
            for (int i=0;i<jsonObj.length();i++)
            {
                mail_ids.put(jsonObj);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void hashKey()
    {
        try{
            PackageInfo info= getPackageManager().getPackageInfo("com.test.aashi.inscubenewbuild",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature: info.signatures)
            {
                MessageDigest digest=MessageDigest.getInstance("SHA");
                digest.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(digest.digest(),Base64.DEFAULT));

            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
    public void send_mail()
    {
         RequestQueue MyRequestQueue = Volley.newRequestQueue(Invite.this);
         String u = Config.BASE_URL + "Cubes/Email_Invite_Cube";
         StringRequest MyStringRequest = new StringRequest(Request.Method.POST, u, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONObject res = new JSONObject(response);
                         if (res.getString("Output").contains("True"))
                         {
                             popup.dismiss();
                             Toast.makeText(getApplicationContext(), "Invitation sent",
                                     Toast.LENGTH_SHORT).show();
                         }
                 }
                 catch (JSONException e)
                 {
                     e.printStackTrace();
                 }
             }
         }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
             @Override
             public void onErrorResponse(VolleyError error)
             {
                 Toast.makeText(Invite.this, " " + error, Toast.LENGTH_SHORT).show();
             }
         }) {
             protected Map<String, String> getParams() {
                 Map<String, String> para = new HashMap<String, String>();
                 para.put("Cube_Id", cube_id);
                 para.put("Email_Ids", mail_ids.toString());
                 para.put("User_Id", login_user_id);
                 return para;
             }
         };
         MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, 3, 2));
         MyRequestQueue.add(MyStringRequest);
    }
}