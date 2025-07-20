package com.example.chitchat;

public class ImageModel {

    private String imageurl;
    public ImageModel(){

    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public ImageModel(String imageurl) {

        this.imageurl = imageurl;
    }
}
