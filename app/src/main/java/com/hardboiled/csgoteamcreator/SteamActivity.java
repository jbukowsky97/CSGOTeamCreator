package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class SteamActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam);

        Intent i = this.getIntent();
        String urlString = i.getStringExtra("url");
        webView = (WebView) findViewById(R.id.steam_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.steamcommunity.com/id/" + urlString);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
    }
}
