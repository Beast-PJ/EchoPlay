package com.beast.echoplay.VideoPlayer;

public class VideoFiles {
    private String id;
    private final String title;
    private final String fileName;
    private final String dateAdded;
    private final String size;
    private String path;


    private final String duration;

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

    public String getFileName() {
        return fileName;
    }


    public String getDateAdded() {
        return dateAdded;
    }


    public String getSize() {
        return size;
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

}
