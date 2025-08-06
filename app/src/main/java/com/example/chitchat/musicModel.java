package com.example.chitchat;

public class musicModel {
    String title,artist,album,song;

    public musicModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public musicModel(String title, String artist, String album, String song) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.song = song;
    }
}
