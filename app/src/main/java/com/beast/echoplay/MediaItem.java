package com.beast.echoplay;

import java.io.Serializable;

public class MediaItem implements Serializable {
    private String id;
    private String title;
    private String mimeType;
    private String data;
    private String duration;
    private String artist;
    private String path;

    public MediaItem(String id, String title, String mimeType, String data, String duration, String artist) {
        this.id = id;
        this.title = title;
        this.mimeType = mimeType;
        this.data = data;
        this.duration = duration;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getData() {
        return data;
    }

    public String getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }
    public String getPath() {
        return path;
    }
}
