package com.beast.echoplay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    private RecyclerView recyclerViewFolders;
    private FolderAdapter folderAdapter;
    private HashMap<String, List<MediaItem>> mediaMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewFolders = findViewById(R.id.recyclerViewFolders);
        recyclerViewFolders.setLayoutManager(new LinearLayoutManager(this));

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

        recyclerViewFolders = findViewById(R.id.recyclerViewFolders);
        recyclerViewFolders.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMedia() {
        mediaMap = MusicUtils.getAllMediaByFolder(this);
        Log.d("MainActivity", "Loaded media: " + mediaMap.size() + " folders");

        List<String> folderList = new ArrayList<>(mediaMap.keySet());
        Collections.sort(folderList); // Sort folders alphabetically

        folderAdapter = new FolderAdapter(this, folderList, folderName -> {
            List<MediaItem> mediaItems = mediaMap.get(folderName);
            Intent intent = new Intent(MainActivity.this, FolderContentActivity.class);
            intent.putExtra("folderName", folderName);
            intent.putExtra("mediaItems", (ArrayList<MediaItem>) mediaItems);
            startActivity(intent);
        });
        recyclerViewFolders.setAdapter(folderAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMedia();
            }
        }
    }
}
