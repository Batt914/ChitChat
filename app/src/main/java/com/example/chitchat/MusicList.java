package com.example.chitchat;

import android.net.Uri;

public class MusicList {
    String title,artist,duration;
    Uri MusicUri;
    Boolean isPlaying;

    public MusicList(String title, String artist, String duration, Boolean isPlaying, Uri MusicUri) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.isPlaying = isPlaying;
        this.MusicUri = MusicUri;

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;

        }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public void setPlaying(Boolean playing) {
        isPlaying = playing;

    }
    public Uri getMusicUri() {
        return MusicUri;
    }
    public void setMusicUri(Uri musicUri) {
        this.MusicUri = musicUri;
    }

}
