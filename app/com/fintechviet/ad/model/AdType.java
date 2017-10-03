package com.fintechviet.ad.model;

import javax.persistence.*;

/**
 * Created by tungn on 10/3/2017.
 */
@Entity
@Table(name = "ad_type")
public class AdType {
    private int id;
    private String name;
    private int width;
    private int height;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "width")
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Basic
    @Column(name = "height")
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdType adType = (AdType) o;

        if (id != adType.id) return false;
        if (width != adType.width) return false;
        if (height != adType.height) return false;
        if (name != null ? !name.equals(adType.name) : adType.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}
