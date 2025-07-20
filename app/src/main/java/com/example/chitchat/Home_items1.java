package com.example.chitchat;

public class Home_items1 {
    private  String h_Name;
    private String h_Status;
    String h_Image;
    private String id;


    public Home_items1(String user_Name, String user_Status, String user_Image, String id) {
        this.h_Name = user_Name;
        this.h_Status = user_Status;
        this.h_Image = user_Image;
        this.id=id;
    }

    public String getH_Name() {
        return h_Name;
    }

    public void setH_Name(String h_Name) {
        this.h_Name = h_Name;
    }

    public String getH_Status() {
        return h_Status;
    }

    public void setH_Status(String h_Status) {
        this.h_Status = h_Status;
    }

    public String getH_Image() {
        return h_Image;
    }

    public void setH_Image(String h_Image) {
        this.h_Image = h_Image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_Name() {
        return h_Name;
    }

    public String getUser_Status() {
        return h_Status;
    }

    public String getUser_Image() {
        return h_Image;
    }

    public String getId() {
        return id;
    }
}