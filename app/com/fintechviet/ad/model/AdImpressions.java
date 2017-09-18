package com.fintechviet.ad.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by tungn on 9/6/2017.
 */
@Entity
@Table(name = "ad_impressions")
public class AdImpressions {
    private long id;
    private Ad ad;
    private Integer impression;
    private Timestamp date;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "adId")
    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    @Basic
    @Column(name = "impression")
    public Integer getImpression() {
        return impression;
    }

    public void setImpression(Integer impression) {
        this.impression = impression;
    }

    @Basic
    @Column(name = "date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdImpressions that = (AdImpressions) o;

        if (id != that.id) return false;
        if (impression != null ? !impression.equals(that.impression) : that.impression != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (impression != null ? impression.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
