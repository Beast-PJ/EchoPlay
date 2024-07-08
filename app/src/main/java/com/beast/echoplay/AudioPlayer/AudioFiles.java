package com.beast.echoplay.AudioPlayer;

import java.io.Serializable;

public class AudioFiles implements Serializable {
    private String id;
    private String title;
    private String path;
    private String duration;
    private String artist;

    public AudioFiles(String id, String title, String path, String duration, String artist) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.duration = duration;
        this.artist = artist;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
