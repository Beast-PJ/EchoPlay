package com.beast.echoplay.AudioPlayer;

import java.io.Serializable;

public class AudioFiles implements Serializable {
    private String id;

    private String title;
    private String path;
    private final String duration;
    private String artist;

    String size;
    String dateAdded;
    String fileName;
    String bucket;

    public AudioFiles(String id, String title, String path, String duration, String artist, String size, String dateAdded, String fileName, String bucket) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.duration = duration;
        this.artist = artist;
        this.size = size;
        this.dateAdded = dateAdded;
        this.fileName = fileName;
        this.bucket = bucket;
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


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
