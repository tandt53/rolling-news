package com.app.simteam.rollingnews.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.app.simteam.rollingnews.R;
import com.app.simteam.rollingnews.adapter2.NewsRollingItem;
import com.app.simteam.rollingnews.adapter2.PreviewNewsAdapter;
import com.app.simteam.rollingnews.data.RSSFeed;
import com.app.simteam.rollingnews.data.RSSFeedParser;
import com.app.simteam.rollingnews.utils.DataMaker;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    Context context;
    RecyclerView rc;

    private static ArrayList<String> feed;
    private static ArrayList<String> feedLink;
    private static ArrayList<String> feedSub;

    private static ArrayList<NewsRollingItem> listNews;

    private DoRssTask3 RssTask;
    PreviewNewsAdapter adapter;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listNews = new ArrayList<>();

        rc = (RecyclerView) findViewById(R.id.rc_preview);

        Intent intent = getIntent();
        url = intent.getExtras().getString("RSS_URL");
        String webName = intent.getExtras().getString("WEB_NAME");
        getSupportActionBar().setTitle(webName);

        adapter = new PreviewNewsAdapter(context, listNews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rc.setLayoutManager(layoutManager);
        rc.setHasFixedSize(true);
        rc.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        RssTask = new DoRssTask3(this);
        RssTask.execute(url);
    }


    public class DoRssTask3 extends AsyncTask<String, Void, List<RSSFeed>> {
        private RSSFeedParser mNewsFeeder;
        private ArrayList<RSSFeed> mRssFeedList;
        private String sub;
        private ProgressDialog progress;

        public DoRssTask3(PreviewActivity previewActivity) {
            progress = new ProgressDialog(previewActivity);
            progress.setIndeterminate(true);
            progress.setCancelable(true);
        }

        @Override
        protected void onPreExecute() {
            this.progress.setMessage("Loading....");
            this.progress.show();
        }

        @Override
        protected ArrayList<RSSFeed> doInBackground(String... params) {
            for (String urlVal : params) {
                sub = new DataMaker().getSub(urlVal);
                mNewsFeeder = new RSSFeedParser(urlVal);
            }
            mRssFeedList = mNewsFeeder.parse();
            publishProgress();
            return mRssFeedList;
        }

        @Override
        protected void onPostExecute(List<RSSFeed> result) {
            adapter.notifyDataSetChanged();
            progress.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (mRssFeedList != null) {
                int size = mRssFeedList.size();
                for (int i = 0; i < size; i++) {
                    String title = mRssFeedList.get(i).getTitle();
                    String url = mRssFeedList.get(i).getLink();
                    String subTitle = mRssFeedList.get(i).getDescription();
                    String imgUrl = mRssFeedList.get(i).getImage();
                    NewsRollingItem newsItem = new NewsRollingItem(title, subTitle, url, imgUrl);
                    listNews.add(newsItem);
                }

            }
        }

    }

}
