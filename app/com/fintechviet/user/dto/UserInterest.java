package com.fintechviet.user.dto;

import java.util.List;

/**
 * Created by tungn on 9/15/2017.
 */
public class UserInterest {
    private String deviceToken;
    private List<String> interests;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
