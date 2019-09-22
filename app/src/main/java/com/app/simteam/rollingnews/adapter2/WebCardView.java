package com.app.simteam.rollingnews.adapter2;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;

/**
 * Created by sev_user on 8/26/2016.
 */
public class WebCardView {
    String name;
    String url;
    boolean isSelected;
    Drawable icon;
    String rssUrl;
    String homeUrl;

    public WebCardView(String name, String url, boolean isSelected, Drawable icon, String rssUrl, String homeUrl) {
        this.name = name;
        this.url = url;
        this.isSelected = isSelected;
        this.icon = icon;
        this.rssUrl = rssUrl;
        this.homeUrl = homeUrl;
    }
}
