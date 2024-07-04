package com.beast.echoplay;

import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    private String artist;
    private String duration;
    private String data;

    public Song(String name, String artist, String duration, String data, String s) {
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.data = data;
    }

    public String getName() { return name; }
    public String getArtist() { return artist; }
    public String getDuration() { return duration; }
    public String getData() { return data; }
}
