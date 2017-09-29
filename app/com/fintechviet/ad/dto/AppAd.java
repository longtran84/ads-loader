package com.fintechviet.ad.dto;

/**
 * Created by tungn on 9/29/2017.
 */
public class AppAd {
    private long id;
    private String name;
    private String icon;
    private String installLink;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInstallLink() {
        return installLink;
    }

    public void setInstallLink(String installLink) {
        this.installLink = installLink;
    }
}
