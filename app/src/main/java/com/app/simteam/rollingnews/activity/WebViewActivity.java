package com.app.simteam.rollingnews.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.utils.HTMLPage;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{
    WebView browser;
    ShareButton btnFB;
    Button btnOther;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

//        String url = getIntent().getExtras().getString("url");
//
//        browser = (WebView) findViewById(R.id.webview);
//        browser.setWebViewClient(new MyBrowser());
//        browser.getSettings().setLoadsImagesAutomatically(true);
//        browser.getSettings().setJavaScriptEnabled(true);
//        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        browser.canGoBack();
//        browser.canGoForward();

        //browser.loadUrl(url);

        //ShareContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse(browser.getUrl())).build();

//        btnFB = (ShareButton) findViewById(R.id.btn_fbshare);
//        btnFB.setShareContent(content);
//        btnOther = (Button) findViewById(R.id.btn_othershare);
//        btnOther.setOnClickListener(this);

        try {
            String page = new HTMLPage().execute("http://www.24h.com.vn/tin-tuc-trong-ngay/hang-tram-phu-tram-ngam-ngai-vao-rung-tim-van-do-c46a791997.html").get();
            Document doc = Jsoup.parse(page);
            Elements elements = doc.select("p");
            for(Element e : elements) {
                Log.d("MRNEO","Element: " + e);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_fbshare:
//                ShareLinkContent content = new ShareLinkContent.Builder()
//                        .setContentUrl(Uri.parse(browser.getUrl().toString())).build();
//
//                break;
//            case R.id.btn_othershare:
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "RollingNEWS");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, browser.getUrl());
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));
//                break;

            default:
                break;

        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(browser.canGoBack()){
            browser.goBack();
        } else {
            browser.clearHistory();
            this.finish();
        }
    }
}
