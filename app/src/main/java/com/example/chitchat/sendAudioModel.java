package com.example.chitchat;

public class sendAudioModel {
    String sendAudio,senderUID;

    public String getSendAudio() {
        return sendAudio;
    }

    public void setSendAudio(String sendAudio) {
        this.sendAudio = sendAudio;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public sendAudioModel(String sendAudio, String senderUID) {
        this.sendAudio = sendAudio;
        this.senderUID = senderUID;
    }

    public sendAudioModel() {

    }
}
