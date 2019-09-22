package com.app.simteam.rollingnews.data;

/**
 * Created by sim on 3/30/2016.
 */
public class CategoryItemData {
    public String categoryTitle;
    public String categoryImage;
    public boolean categoryIsAdded;

    public CategoryItemData(String title, String image, boolean categoryIsAdded) {
        this.categoryTitle = title;
        this.categoryImage = image;
        this.categoryIsAdded = categoryIsAdded;
    }

    public void setTitle(String title) {
        this.categoryTitle = title;
    }

    public void setImage(String image) {
        this.categoryImage = image;
    }

    public String getTitle() {
        return this.categoryTitle;
    }

    public String getImage() {
        return this.categoryImage;
    }

    public void setCategoryIsAdded(boolean isAdded) {
        this.categoryIsAdded = isAdded;
    }

    public boolean getCategoryIsAdded() {
        return this.categoryIsAdded;
    }
}
