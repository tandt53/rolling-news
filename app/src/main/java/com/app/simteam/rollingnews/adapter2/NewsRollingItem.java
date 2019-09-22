package com.app.simteam.rollingnews.adapter2;

import android.graphics.Bitmap;

/**
 * Created by ADMIN on 9/2/2016.
 */
public class NewsRollingItem {
    public String title;
    public String sub;
    public String url;
    public String imgUrl;
    public Bitmap btm;

    public NewsRollingItem(String title, String sub, String url, String imgUrl) {
        this.title = title;
        this.sub = sub;
        this.url = url;
        this.imgUrl = imgUrl;
    }

    public void setBitmap(Bitmap btm){
        this.btm = btm;
    }
}
