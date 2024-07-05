package com.beast.echoplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FolderContentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFolderContent;
    private MediaAdapter mediaAdapter;
    private List<MediaItem> mediaItems;
    private ArrayList<MediaItem> playingQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_content);

        recyclerViewFolderContent = findViewById(R.id.recyclerViewMedia);
        recyclerViewFolderContent.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        mediaItems = (List<MediaItem>) intent.getSerializableExtra("mediaItems");

        if (mediaItems != null) {
            mediaAdapter = new MediaAdapter(this, mediaItems);
            recyclerViewFolderContent.setAdapter(mediaAdapter);
        } else {
            Log.e("FolderContentActivity", "No media items found for folder: " + folderName);
        }

        playingQueue = new ArrayList<>();
    }

    private void playMedia(MediaItem mediaItem, List<MediaItem> mediaItems) {
        playingQueue.clear();
        playingQueue.addAll(mediaItems);
        int position = playingQueue.indexOf(mediaItem);

        Intent intent = new Intent(FolderContentActivity.this, PlayerActivity.class);
        intent.putExtra("mediaItem", mediaItem);
        intent.putExtra("queue", playingQueue);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
