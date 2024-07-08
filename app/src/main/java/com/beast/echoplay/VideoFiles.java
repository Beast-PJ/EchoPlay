package com.beast.echoplay;

public class VideoFiles {
    private String id;
    private String title;
    private String fileName;
    private String dateAdded;
    private String size;
    private String path;


    private String duration;

    public VideoFiles(String id, String title, String fileName, String dateAdded, String size, String path, String duration) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.dateAdded = dateAdded;
        this.size = size;
        this.path = path;
        this.duration = duration;
    }


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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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
}
