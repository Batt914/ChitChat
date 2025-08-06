package com.example.chitchat;

public class msgModel {
    String msg,senderUID,shareImages,shareVideos,shareAudios,title,alubumpic,artist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlubumpic() {
        return alubumpic;
    }

    public void setAlubumpic(String alubumpic) {
        this.alubumpic = alubumpic;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getShareVideos() {
        return shareVideos;
    }

    public void setShareVideos(String shareVideos) {
        this.shareVideos = shareVideos;
    }

    public String getShareAudios() {
        return shareAudios;
    }

    public void setShareAudios(String shareAudios) {
        this.shareAudios = shareAudios;
    }

    public String getShareImages() {
        return shareImages;
    }

    public void setShareImages(String shareImages) {
        this.shareImages = shareImages;
    }

    public msgModel() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public msgModel(String msg, String shareImages,String senderUID,String shareVideos, String shareAudios,String title,String alubumpic, String artist ) {
        this.msg = msg;
        this.senderUID = senderUID;
        this.shareImages=shareImages;
        this.shareVideos=shareVideos;
        this.shareAudios=shareAudios;
        this.title=title;
        this.alubumpic=alubumpic;
        this.artist=artist;
    }
}
