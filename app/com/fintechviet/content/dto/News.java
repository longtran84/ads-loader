package com.fintechviet.content.dto;

/**
 * Created by tungn on 9/14/2017.
 */
public class News {
    private String title;
    private String shortDescription;
    private String imageLink;
    private String link;
    private String newsCategoryCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNewsCategoryCode() {
        return newsCategoryCode;
    }

    public void setNewsCategoryCode(String newsCategoryCode) {
        this.newsCategoryCode = newsCategoryCode;
    }
}
