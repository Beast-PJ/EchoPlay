package com.beast.echoplay;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {
    private static QueueManager instance;
    private List<Song> queue;
    private int currentPosition;

    private QueueManager() {
        queue = new ArrayList<>();
        currentPosition = -1;
    }

    public static QueueManager getInstance() {
        if (instance == null) {
            instance = new QueueManager();
        }
        return instance;
    }

    public void setQueue(List<Song> songs) {
        queue.clear();
        queue.addAll(songs);
        currentPosition = 0;
    }

    public Song getCurrentSong() {
        return currentPosition >= 0 && currentPosition < queue.size() ? queue.get(currentPosition) : null;
    }

    public Song getNextSong() {
        if (currentPosition < queue.size() - 1) {
            currentPosition++;
            return queue.get(currentPosition);
        }
        return null;
    }

    public Song getPreviousSong() {
        if (currentPosition > 0) {
            currentPosition--;
            return queue.get(currentPosition);
        }
        return null;
    }
}
