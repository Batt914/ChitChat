package com.example.chitchat;

public class Group_items {
    private final String User_Name,User_Status,User_Image;
    private int User_Image1;

    public int getUser_Image1() {
        return User_Image1;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public String getUser_Status() {
        return User_Status;
    }

    public String getUser_Image() {
        return User_Image;
    }

    public Group_items(String user_Name, String user_Status, String user_Image) {
        this.User_Name = user_Name;
        this.User_Status = user_Status;
        this.User_Image = user_Image;
    }
}
