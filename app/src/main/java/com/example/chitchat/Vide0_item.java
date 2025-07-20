package com.example.chitchat;

public class Vide0_item {
    private int vedio_image;
    private int chanel_image;
    private String tittle;
    private String chanel_name;
    public Vide0_item(int vedio_image, int chanel_image, String tittle, String chanel_name) {
        this.vedio_image = vedio_image;
        this.chanel_image = chanel_image;
        this.tittle = tittle;
        this.chanel_name = chanel_name;
    }
    public int getVedio_image() {
        return vedio_image;
    }

    public int getChanel_image() {
        return chanel_image;
    }
    public String getTittle() {
        return tittle;
    }
    public String getChanel_name() {
        return chanel_name;
    }
}
