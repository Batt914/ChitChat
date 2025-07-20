package com.example.chitchat;

public class sendVideoModel {
    String sendVideo,senderUID;

    public String getSendVideo() {
        return sendVideo;
    }

    public void setSendVideo(String sendVideo) {
        this.sendVideo = sendVideo;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public sendVideoModel() {
    }

    public sendVideoModel(String sendVideo, String senderUID) {
        this.sendVideo = sendVideo;
        this.senderUID = senderUID;
    }
}
