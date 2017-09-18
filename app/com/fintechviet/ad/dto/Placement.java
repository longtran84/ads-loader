package com.fintechviet.ad.dto;

/**
 * Created by tungn on 9/7/2017.
 */
public class Placement {
    private String template;

    public Placement() {
    }

    public Placement(String template) {
        setTemplate(template);
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
