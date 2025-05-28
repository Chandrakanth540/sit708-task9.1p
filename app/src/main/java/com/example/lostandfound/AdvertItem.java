package com.example.lostandfound;

public class AdvertItem {
    private String postType;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
private String id;
    // Constructor, Getters, and Setters

    public AdvertItem(String id,String postType, String name, String phone, String description, String date, String location) {
        this.id=id;
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public String getPostType() {
        return postType;
    }
    public String getId(){
        return id;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

