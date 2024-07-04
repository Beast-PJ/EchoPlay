package com.beast.echoplay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.MusicPlayer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongAdapter musicAdapter;
    private ArrayList<Song> songList;
    private ArrayList<Song> playingQueue;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        songList = MusicUtils.getAllSongs(this);
        playingQueue = new ArrayList<>();
        musicAdapter = new SongAdapter(this, songList, song -> {
            addToQueue(song);
            Intent intent = new Intent(MainActivity.this, MusicPlayer.class);
            intent.putExtra("queue", playingQueue);
            startActivity(intent);
        });
        recyclerView.setAdapter(musicAdapter);

        menuButton = findViewById(R.id.menu_btn);
        menuButton.setOnClickListener(v -> {
            Switch nightModeSwitch = findViewById(R.id.action_night_mode);
            boolean isNightMode = nightModeSwitch.isChecked();
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void addToQueue(Song song) {
        playingQueue.add(song);
    }
}
