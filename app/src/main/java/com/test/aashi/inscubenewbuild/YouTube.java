package com.test.aashi.inscubenewbuild;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class YouTube extends AppCompatActivity {
  WebView webView;
BroadcastReceiver mMessageReceiver;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        String name = getIntent().getStringExtra("webview");
        webView=findViewById(R.id.webview);
        webView.loadUrl(name);
        TextView textView=findViewById(R.id.gettext);
        textView.setText(name);

    }
}
