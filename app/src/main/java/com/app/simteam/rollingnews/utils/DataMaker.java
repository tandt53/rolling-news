package com.app.simteam.rollingnews.utils;

import android.util.Log;

import com.app.simteam.rollingnews.constant.Constant;
import com.app.simteam.rollingnews.data.CategoryItemData;
import com.app.simteam.rollingnews.data.WebItemData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by sim on 4/11/2016.
 */
public class DataMaker {

    ArrayList<WebItemData> webItems = null;
    ArrayList<CategoryItemData> categoryItems = null;

    public ArrayList<WebItemData> setWebItems(InputStream in) throws XmlPullParserException,
            IOException {
        // TODO Auto-generated method stub
        try {

            XmlPullParserFactory pullParserFactory;
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setInput(in, null);
            int eventType = parser.getEventType();
            WebItemData item = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        webItems = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equals("webitem")) {
                            item = new WebItemData();
                        } else if (item != null) {
                            if (name.equals("title")) {
                                item.setTitle(parser.nextText());
                            } else if (name.equals("sub")) {
                                item.setSub(parser.nextText());
                            } else if (name.equals("description")) {
                                item.setDescription(parser.nextText());
                            } else if (name.equals("link")) {
                                item.setLink(parser.nextText());
                            } else if (name.equals("isAdded")) {
                                if (parser.nextText().equals("false")) {
                                    item.setIsAdd(false);
                                } else
                                    item.setIsAdd(true);
                            } else if (name.equals("image")) {
                                item.setImage(parser.nextText());
                            } else if (name.equals("economic")) {
                                item.setEconomics(parser.nextText());
                            } else if (name.equals("politics")) {
                                item.setPolytics(parser.nextText());
                            } else if (name.equals("culture")) {
                                item.setCulture(parser.nextText());
                            } else if (name.equals("law")) {
                                item.setLaws(parser.nextText());
                            } else if (name.equals("social")) {
                                item.setSocial(parser.nextText());
                            } else if (name.equals("education")) {
                                item.setEducation(parser.nextText());
                            } else if (name.equals("science")) {
                                item.setScience(parser.nextText());
                            } else if (name.equals("technology")) {
                                item.setTechnology(parser.nextText());
                            } else if (name.equals("world")) {
                                item.setWorld(parser.nextText());
                            } else if (name.equals("sport")) {
                                item.setSport(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equals("webitem") && item != null) {
                            webItems.add(item);
                        }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return webItems;
    }

    public ArrayList<CategoryItemData> setCategoryItems() {
        categoryItems = new ArrayList<>();
        categoryItems.add(new CategoryItemData("Economics", "economics", true));
        categoryItems.add(new CategoryItemData("Culture", "culture", true));
        categoryItems.add(new CategoryItemData("Politics", "politics", true));
        categoryItems.add(new CategoryItemData("Social", "social", true));
        categoryItems.add(new CategoryItemData("Law", "law", true));
        categoryItems.add(new CategoryItemData("Education", "education", true));
        categoryItems.add(new CategoryItemData("Science", "science", true));
        categoryItems.add(new CategoryItemData("Technology", "technology", true));
        categoryItems.add(new CategoryItemData("World", "world", true));
        categoryItems.add(new CategoryItemData("Sport", "sport", true));
        return categoryItems;
    }
    public ArrayList<CategoryItemData> setCategoryItems_vi() {
        categoryItems = new ArrayList<>();
        categoryItems.add(new CategoryItemData("Kinh Tế", "economics", true));
        categoryItems.add(new CategoryItemData("Chính Trị", "politics", true));
        categoryItems.add(new CategoryItemData("Văn Hóa", "culture", true));
        categoryItems.add(new CategoryItemData("Xã Hội", "social", true));
        categoryItems.add(new CategoryItemData("Pháp Luật", "law", true));
        categoryItems.add(new CategoryItemData("Giáo Dục", "education", true));
        categoryItems.add(new CategoryItemData("Khoa Học", "science", true));
        categoryItems.add(new CategoryItemData("Công Nghệ", "technology", true));
        categoryItems.add(new CategoryItemData("Thế Giới", "world", true));
        categoryItems.add(new CategoryItemData("Thể Thao", "sport", true));
        return categoryItems;
    }
    public static String getSub(String str) {
        if (str.contains("vnexpress.net"))
            return Constant.SUB_VNEXPRESS;
        else if (str.contains("vietnamnet.vn"))
            return Constant.SUB_VIETNAMNET;
        else if (str.contains("laodong.com.vn"))
            return Constant.SUB_LAODONG;
        else if (str.contains("tuoitre.vn"))
            return Constant.SUB_TUOITRE;
        else if (str.contains("nhandan.com.vn"))
            return Constant.SUB_NHANDAN;
        else if(str.contains("dantri.vn"))
            return Constant.SUB_DANTRI;
        else if(str.contains("thanhnien.vn"))
            return Constant.SUB_THANHNIEN;
        else if(str.contains("tienphong.vn"))
            return Constant.SUB_TIENPHONG;
        else if(str.contains("24h.com.vn"))
            return Constant.SUB_24H;
        else if(str.contains("nld.com.vn"))
            return Constant.SUB_NGUOILAODONG;
        else
            return null;
    }
}
