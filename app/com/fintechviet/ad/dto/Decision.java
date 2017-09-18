package com.fintechviet.ad.dto;

/**
 * Created by tungn on 9/7/2017.
 */
public class Decision {
    // id for the ad that was selected
    private long adId;
    // url redirects to the target
    private String clickUrl;
    // url endpoint that, using a GET, triggers the recording of the click
    private String trackingUrl;
    // url endpoint that, using a GET, triggers the recording of the view
    private String viewUrl;
    // url endpoint that, using a GET, triggers the recording of the impression
    private String impressionUrl;
    private Content content;

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getImpressionUrl() {
        return impressionUrl;
    }

    public void setImpressionUrl(String impressionUrl) {
        this.impressionUrl = impressionUrl;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }
}
