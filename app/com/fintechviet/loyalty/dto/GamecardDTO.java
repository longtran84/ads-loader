package com.fintechviet.loyalty.dto;

/**
 * Created by tungn on 4/11/2018.
 */
public class GamecardDTO {
    private int id;
    private String name;
    private String image;
    private Integer price;
    private String status;
    private String pointExchange;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPointExchange() {
        return pointExchange;
    }

    public void setPointExchange(String pointExchange) {
        this.pointExchange = pointExchange;
    }
}
