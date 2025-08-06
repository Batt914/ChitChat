package com.example.chitchat;

public class SpotifyMetadata_Model {
    public SpotifyMetadata_Model() {
    }

    String Song,SongName,artistName,albumName,imageUri;
    long dueration;

    public String getSong() {
        return Song;
    }

    public void setSong(String song) {
        Song = song;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getDueration() {
        return dueration;
    }

    public void setDueration(long dueration) {
        this.dueration = dueration;
    }

    public SpotifyMetadata_Model(String song, String songName, String artistName, String albumName, String imageUri, long dueration) {
        Song = song;
        SongName = songName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.imageUri = imageUri;
        this.dueration = dueration;
    }
}
