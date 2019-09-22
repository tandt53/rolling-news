package com.app.simteam.rollingnews.adapter2;

import android.graphics.Bitmap;

/**
 * Created by sev_user on 9/1/2016.
 */
public class NewsItem {

    public String title;
    public String subTitle;
    public Bitmap btm;

    public NewsItem(String title, String subTitle, Bitmap btm){
        this.title = title;
        this.subTitle = subTitle;
        this.btm = btm;
    }

}
