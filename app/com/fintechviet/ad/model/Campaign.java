package com.fintechviet.ad.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tungn on 8/26/2017.
 */
@Entity
@Table(name = "campaign")
public class Campaign {
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private Byte isFreCap;
    private Integer freCap;
    private Integer freCapDuration;
    private String freCapType;
    private String description;
    private String status;
    private Advertiser advertiser;
    private String startDateTmp;
    private String endDateTmp;
    private boolean isFreCapTmp;
    private String freCapTmp;
    private String freCapDurationTmp;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    @Column(name = "startDate")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "endDate")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "isFreCap")
    public Byte getIsFreCap() {
        return isFreCap;
    }

    public void setIsFreCap(Byte isFreCap) {
        this.isFreCap = isFreCap;
    }

    @Basic
    @Column(name = "freCap")
    public Integer getFreCap() {
        return freCap;
    }

    public void setFreCap(Integer freCap) {
        this.freCap = freCap;
    }

    @Basic
    @Column(name = "freCapDuration")
    public Integer getFreCapDuration() {
        return freCapDuration;
    }

    public void setFreCapDuration(Integer freCapDuration) {
        this.freCapDuration = freCapDuration;
    }

    @Basic
    @Column(name = "freCapType")
    public String getFreCapType() {
        return freCapType;
    }

    public void setFreCapType(String freCapType) {
        this.freCapType = freCapType;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "advertiserId")
    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(Advertiser advertiser) {
        this.advertiser = advertiser;
    }

    @Transient
    public boolean isIsFreCapTmp() {
        return isFreCapTmp;
    }

    public void setIsFreCapTmp(boolean isFreCapTmp) {
        this.isFreCapTmp = isFreCapTmp;
    }

    @Transient
    public String getEndDateTmp() {
        return endDateTmp;
    }

    public void setEndDateTmp(String endDateTmp) {
        this.endDateTmp = endDateTmp;
    }

    @Transient
    public String getStartDateTmp() {
        return startDateTmp;
    }

    public void setStartDateTmp(String startDateTmp) {
        this.startDateTmp = startDateTmp;
    }

    @Transient
    public String getFreCapTmp() {
        return freCapTmp;
    };

    public void setFreCapTmp(String freCapTmp) {
        this.freCapTmp = freCapTmp;
    }

    @Transient
    public String getFreCapDurationTmp() {
        return freCapDurationTmp;
    }

    public void setFreCapDurationTmp(String freCapDurationTmp) {
        this.freCapDurationTmp = freCapDurationTmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campaign campaign = (Campaign) o;

        if (id != campaign.id) return false;
        if (name != null ? !name.equals(campaign.name) : campaign.name != null) return false;
        if (startDate != null ? !startDate.equals(campaign.startDate) : campaign.startDate != null) return false;
        if (endDate != null ? !endDate.equals(campaign.endDate) : campaign.endDate != null) return false;
        if (isFreCap != null ? !isFreCap.equals(campaign.isFreCap) : campaign.isFreCap != null) return false;
        if (freCap != null ? !freCap.equals(campaign.freCap) : campaign.freCap != null) return false;
        if (freCapDuration != null ? !freCapDuration.equals(campaign.freCapDuration) : campaign.freCapDuration != null)
            return false;
        if (freCapType != null ? !freCapType.equals(campaign.freCapType) : campaign.freCapType != null) return false;
        if (description != null ? !description.equals(campaign.description) : campaign.description != null)
            return false;
        if (status != null ? !status.equals(campaign.status) : campaign.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (isFreCap != null ? isFreCap.hashCode() : 0);
        result = 31 * result + (freCap != null ? freCap.hashCode() : 0);
        result = 31 * result + (freCapDuration != null ? freCapDuration.hashCode() : 0);
        result = 31 * result + (freCapType != null ? freCapType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
