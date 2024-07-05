package com.beast.echoplay;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {
    private static QueueManager instance;
    private List<MediaItem> queue;
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

    public void setQueue(List<MediaItem> MediaItems) {
        queue.clear();
        queue.addAll(MediaItems);
        currentPosition = 0;
    }

    public MediaItem getCurrentMediaItem() {
        return currentPosition >= 0 && currentPosition < queue.size() ? queue.get(currentPosition) : null;
    }

    public MediaItem getNextMediaItem() {
        if (currentPosition < queue.size() - 1) {
            currentPosition++;
            return queue.get(currentPosition);
        }
        return null;
    }

    public MediaItem getPreviousMediaItem() {
        if (currentPosition > 0) {
            currentPosition--;
            return queue.get(currentPosition);
        }
        return null;
    }
}
