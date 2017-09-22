package com.fintechviet.user.model;

import javax.persistence.*;

/**
 * Created by tungn on 9/21/2017.
 */
@Entity
@Table(name = "user_mobile_device_token", schema = "mobileads", catalog = "")
public class UserDeviceToken {
    private long id;
    private User userMobile;
    private String deviceToken;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "uid")
    public User getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(User userMobile) {
        this.userMobile = userMobile;
    }

    @Basic
    @Column(name = "deviceToken")
    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDeviceToken that = (UserDeviceToken) o;

        if (id != that.id) return false;
        if (deviceToken != null ? !deviceToken.equals(that.deviceToken) : that.deviceToken != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (deviceToken != null ? deviceToken.hashCode() : 0);
        return result;
    }
}
