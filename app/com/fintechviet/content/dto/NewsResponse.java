package com.fintechviet.content.dto;


import java.util.List;

/**
 * Created by tungn on 9/14/2017.
 */
public class NewsResponse {
    private List<News> newsList;

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
