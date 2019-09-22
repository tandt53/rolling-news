
package com.app.simteam.rollingnews.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.app.simteam.rollingnews.constant.Constant;
import com.app.simteam.rollingnews.utils.DataMaker;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public class RSSFeedParser {
    private InputStream urlStream;
    private XmlPullParserFactory factory;
    private XmlPullParser parser;

    private ArrayList<RSSFeed> rssFeedList;
    private RSSFeed rssFeed;

    private String urlString;
    private String tagName;

    private String title;
    private String sub;
    private String link;
    private String description;
    private String category;
    private String pubDate;
    private String enclosure;
    private String image = "image";

    public static final String ITEM = "item";
    public static final String CHANNEL = "channel";

    public static final String TITLE = "title";
    public static final String SUB = "sub";
    public static final String LINK = "link";
    public static final String DESCRIPTION = "description";
    public static final String CATEGORY = "category";
    public static final String PUBLISHEDDATE = "pubDate";
    public static final String IMAGE = "image";
    public static final String GUID = "guid";
    public static final String ENCLOSURE = "enclosure";
    public static final String FEEDBURNERORIGLINK = "feedburner:origLink";
    private static String web;


    public RSSFeedParser(String urlString) {
        this.urlString = urlString;
    }

    public static InputStream downloadUrl(String urlString) throws IOException {
        web = DataMaker.getSub(urlString);
        Log.d("TAN", "web: " + web);
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    public ArrayList<RSSFeed> parse() {
        try {
            int count = 0;
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            urlStream = downloadUrl(urlString);
            parser.setInput(urlStream, null);
            int eventType = parser.getEventType();
            boolean done = false;
//            rssFeed = new RSSFeed();
            rssFeedList = new ArrayList<RSSFeed>();
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                tagName = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:

                        if (tagName.equals(ITEM)) {
                            rssFeed = new RSSFeed();
                        }

                        if (tagName.equals(TITLE)) {
                            title = parser.nextText().toString();
                        }
                        if (tagName.equals(SUB)) {
                            sub = parser.nextText().toString();
                        }
                        if (tagName.equals(LINK)) {
                            link = parser.nextText().toString();
                        }
                        if (tagName.equals(DESCRIPTION)) {
                            description = parser.nextText().toString();
//                            image = getLink(description);
                        }
                        if (tagName.equals(CATEGORY)) {
                            category = parser.nextText().toString();
                        }
                        if (tagName.equals(PUBLISHEDDATE)) {
                            pubDate = parser.nextText().toString();
                        }
                        if (tagName.equals(IMAGE)) {
                            try {
                                String tmp = parser.nextText();
                                image = tmp;
                                Log.d("TAN", "Image: " + tmp);
                            } catch (Exception e) {
                                Log.d("TAN", "Exception Image: " + tagName);
                                image = "NONE";
                            }
                        }
                        if (tagName.equals(ENCLOSURE)) {
                            enclosure = parser.getAttributeValue(2);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equals(CHANNEL)) {
                            done = true;
                        } else if (tagName.equals(ITEM)) {
                            if (image.equals("NONE") || !image.contains("http")) {
                                if (web.equals(Constant.SUB_NHANDAN)) {
                                    image = enclosure;
                                } else
                                    image = getLink(description);
                            }

//                            Log.d("TAN", "Title: " + title);
                            rssFeed = new RSSFeed(title, link, description, category, pubDate, image);
                            image = "NONE";
//                            Log.d("TAN", "Image Link: " + image);
                            rssFeedList.add(rssFeed);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rssFeedList;

    }

    static String getLink(String url) {
        String tmpUrl = url.toLowerCase();

        ArrayList<String> imgExtension = new ArrayList<String>();
        imgExtension.add(".jpg");
        imgExtension.add(".png");
        imgExtension.add(".gif");
        imgExtension.add(".jpeg");

        int end = -1;
        int ind = 0;
        while (ind < imgExtension.size()) {
            end = tmpUrl.indexOf(imgExtension.get(ind));
            if (end != -1) {
                break;
            } else {
                ind++;
            }
        }

        if (end != -1) {
            int start = url.indexOf("http://");
            int start2;
            while (true) {
                start2 = url.indexOf("http://", start + 1);
                if (start2 != -1 && start2 < end) {
                    start = start2;
                } else {
                    break;
                }
            }
            String result = url.substring(start, end) + imgExtension.get(ind);
//            Log.d("TAN", "get Img URL: " + result);
            return result;
        } else {
            Log.d("TAN", "fail to get img link: \n" + url);
            return "NONE";
        }

    }

}
