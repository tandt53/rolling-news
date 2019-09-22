package com.app.simteam.rollingnews.data;

/**
 * Created by sim on 3/30/2016.
 */
public class WebItemData {
    public String title;
    public String sub;
    public String description;
    public String link;
    public String image;
    public boolean isAdded;
    public String economics;
    public String culture;
    public String polytics;
    public String social;
    public String laws;
    public String education;
    public String science;
    public String technology;
    public String world;
    public String sport;


    public WebItemData(String title, String sub, String description, String image, boolean isAdded) {
        this.title = title;
        this.sub = sub;
        this.description = description;
        this.image = image;
        this.isAdded = isAdded;
    }
    public WebItemData() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSub(String sub){
        this.sub = sub;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setIsAdd(boolean isAdded) {
        this.isAdded = isAdded;
    }

    public void setEconomics(String economics) {
        this.economics = economics;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public void setPolytics(String polytics) {
        this.polytics = polytics;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public void setLaws(String laws) {
        this.laws = laws;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setScience(String science) {
        this.science = science;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }


    public String getTitle() {
        return this.title;
    }

    public String getSub(){return this.sub;}

    public String getDescription() {
        return this.description;
    }

    public String getImage() {
        return this.image;
    }

    public String getLink() {
        return this.link;
    }

    public boolean getIsAdd() {
        return this.isAdded;
    }

    public String getEconomics() {
        return this.economics;
    }

    public String getCulture() {
        return this.culture;
    }

    public String getPolytics() {
        return this.polytics;
    }

    public String getSocial() {
        return this.social;
    }

    public String getLaws() {
        return this.laws;
    }

    public String getEducation() {
        return this.education;
    }

    public String getScience() {
        return this.science;
    }

    public String getTechnology() {
        return this.technology;
    }

    public String getWorld() {
        return this.world;
    }

    public String getSport() {
        return this.sport;
    }
}

