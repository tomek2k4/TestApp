package com.example.tmaslon.testapp;

/**
 * Created by tmaslon on 2015-12-09.
 */


public class Job {
    private String name;
    private String url;
    private String color;

    public Job(String name, String color) {
        this.name = name;
        this.color = color;
        this.url = "";
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
