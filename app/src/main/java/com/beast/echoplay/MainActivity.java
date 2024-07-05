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

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    private RecyclerView recyclerView;
    private MediaAdapter mediaAdapter;
    private ArrayList<MediaItem> mediaList;
    private ArrayList<MediaItem> playingQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            initializePlayer();
        }
    }

    private void initializePlayer() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mediaList = MusicUtils.getAllMedia(this);
        playingQueue = new ArrayList<>();
        mediaAdapter = new MediaAdapter(this, mediaList, mediaItem -> {
            if (mediaItem.getMimeType().startsWith("audio")) {
                addToQueue(mediaItem);
                Intent intent = new Intent(MainActivity.this, MusicPlayer.class);
                intent.putExtra("queue", playingQueue);
                startActivity(intent);
            } else if (mediaItem.getMimeType().startsWith("video")) {
                Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                intent.putExtra("videoPath", mediaItem.getData());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mediaAdapter);

        ImageButton menuButton = findViewById(R.id.menu_btn);
        menuButton.setOnClickListener(v -> {
            Switch nightModeSwitch = findViewById(R.id.night_mode_switch);
            boolean isNightMode = nightModeSwitch.isChecked();
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void addToQueue(MediaItem mediaItem) {
        playingQueue.add(mediaItem);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializePlayer();
            } else {
                // Permission denied, show a message to the user
            }
        }
    }
}
