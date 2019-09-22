
package com.app.simteam.rollingnews.data;

import java.io.Serializable;

public class RSSFeed implements Serializable {

    private String title;
    private String sub;
    private String link;
    private String description;
    private String category;
    private String pubDate;
    private String image;
//    private String guid;
//    private String feedburnerOrigLink;

    public RSSFeed() {
    }

    public RSSFeed(String title, String link, String description, String category, String pubDate, String imgage) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.category = category;
        this.pubDate = pubDate;
        this.image = imgage;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return this.link;
    }

    public String getImage(){
        return this.image;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

}
