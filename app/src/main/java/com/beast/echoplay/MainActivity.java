package com.beast.echoplay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    private RecyclerView recyclerViewFolders;
    private RecyclerView recyclerViewMedia;
    private FolderAdapter folderAdapter;
    private MediaAdapter mediaAdapter;
    private HashMap<String, List<MediaItem>> mediaMap;
    private ArrayList<MediaItem> playingQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewFolders = findViewById(R.id.recyclerViewFolders);
        recyclerViewMedia = findViewById(R.id.recyclerViewMedia);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            loadMedia();
        }

        Switch nightModeSwitch = findViewById(R.id.night_mode_switch);
        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });



        recyclerViewFolders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMedia.setLayoutManager(new LinearLayoutManager(this));

        playingQueue = new ArrayList<>();
    }

    private void loadMedia() {
        mediaMap = MusicUtils.getAllMediaByFolder(this);

        List<String> folderList = new ArrayList<>(mediaMap.keySet());
        folderAdapter = new FolderAdapter(this, folderList, folderName -> {
            List<MediaItem> mediaItems = mediaMap.get(folderName);
            mediaAdapter = new MediaAdapter(MainActivity.this, mediaItems, (mediaItem, mediaItems1) -> playMedia(mediaItem, mediaItems1));
            recyclerViewMedia.setAdapter(mediaAdapter);
        });
        recyclerViewFolders.setAdapter(folderAdapter);
    }

    private void playMedia(MediaItem mediaItem, List<MediaItem> mediaItems) {
        playingQueue.clear();
        playingQueue.addAll(mediaItems);
        int position = playingQueue.indexOf(mediaItem);

        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.putExtra("mediaItem", mediaItem);
        intent.putExtra("queue", playingQueue);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMedia();
            } else {
                // Permission denied
            }
        }
    }
}
