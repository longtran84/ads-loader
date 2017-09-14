package com.fintech.adl.model;

import javax.persistence.*;

/**
 * Created by tungn on 8/31/2017.
 */
@Entity
@Table(name = "ad")
@NamedQueries({
        @NamedQuery(name = "Advertisment.findAdByTemplate", query = "SELECT ad FROM Ad ad WHERE ad.flight.startDate <= CURRENT_DATE AND (ad.flight.endDate >= CURRENT_DATE OR ad.flight.endDate IS NULL) " +
                "AND ad.impressions > (SELECT COUNT(adi.id) FROM AdImpressions adi WHERE adi.ad.id = ad.id) AND ad.creative.template = :template AND ad.status = 'ACTIVE'")
})
public class Ad {
    private long id;
    private String name;
    private Integer impressions;
    private Byte isFreCap;
    private Integer freCap;
    private Integer freCapDuration;
    private String freCapType;
    private String description;
    private String status;
    private Flight flight;
    private Creative creative;
    private boolean isFreCapTmp;
    private String impressionsTmp;
    private String freCapTmp;
    private String freCapDurationTmp;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
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
    @Column(name = "impressions")
    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
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

    public void setFreCap(Integer preCap) {
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
    @JoinColumn(name = "flightId")
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    @ManyToOne
    @JoinColumn(name = "creativeId")
    public Creative getCreative() {
        return creative;
    }

    public void setCreative(Creative creative) {
        this.creative = creative;
    }

    @Transient
    public boolean isIsFreCapTmp() {
        return isFreCapTmp;
    }

    public void setIsFreCapTmp(boolean isFreCapTmp) {
        this.isFreCapTmp = isFreCapTmp;
    }

    @Transient
    public String getImpressionsTmp() {
        return impressionsTmp;
    }

    public void setImpressionsTmp(String impressionsTmp) {
        this.impressionsTmp = impressionsTmp;
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

        Ad ad = (Ad) o;

        if (id != ad.id) return false;
        if (name != null ? !name.equals(ad.name) : ad.name != null) return false;
        if (impressions != null ? !impressions.equals(ad.impressions) : ad.impressions != null) return false;
        if (isFreCap != null ? !isFreCap.equals(ad.isFreCap) : ad.isFreCap != null) return false;
        if (freCap != null ? !freCap.equals(ad.freCap) : ad.freCap != null) return false;
        if (freCapDuration != null ? !freCapDuration.equals(ad.freCapDuration) : ad.freCapDuration != null)
            return false;
        if (freCapType != null ? !freCapType.equals(ad.freCapType) : ad.freCapType != null) return false;
        if (description != null ? !description.equals(ad.description) : ad.description != null) return false;
        if (status != null ? !status.equals(ad.status) : ad.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (impressions != null ? impressions.hashCode() : 0);
        result = 31 * result + (isFreCap != null ? isFreCap.hashCode() : 0);
        result = 31 * result + (freCap != null ? freCap.hashCode() : 0);
        result = 31 * result + (freCapDuration != null ? freCapDuration.hashCode() : 0);
        result = 31 * result + (freCapType != null ? freCapType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
