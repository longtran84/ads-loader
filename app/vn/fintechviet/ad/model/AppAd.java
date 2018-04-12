package vn.fintechviet.ad.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by tungn on 9/28/2017.
 */
@Entity
@Table(name = "app_ad")
public class AppAd {
    private long id;
    private String name;
    private String icon;
    private String installLink;
    private Campaign campaign;
    private String status = "NEW";
    private Timestamp createdDate;

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
    @Column(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "installLink")
    public String getInstallLink() {
        return installLink;
    }

    public void setInstallLink(String installLink) {
        this.installLink = installLink;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "createdDate", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @ManyToOne
    @JoinColumn(name = "campaignId")
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppAd appAd = (AppAd) o;

        if (id != appAd.id) return false;
        if (name != null ? !name.equals(appAd.name) : appAd.name != null) return false;
        if (icon != null ? !icon.equals(appAd.icon) : appAd.icon != null) return false;
        if (installLink != null ? !installLink.equals(appAd.installLink) : appAd.installLink != null) return false;
        if (status != null ? !status.equals(appAd.status) : appAd.status != null) return false;
        if (createdDate != null ? !createdDate.equals(appAd.createdDate) : appAd.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (installLink != null ? installLink.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }
}
