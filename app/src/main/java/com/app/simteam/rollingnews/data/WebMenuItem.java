package com.app.simteam.rollingnews.data;

/**
 * Created by MrNeo on 5/21/2016.
 */
public class WebMenuItem {
    String item;
    int icon;

    public WebMenuItem() {
    }

    public WebMenuItem(String item, int icon) {
        this.item = item;
        this.icon = icon;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
