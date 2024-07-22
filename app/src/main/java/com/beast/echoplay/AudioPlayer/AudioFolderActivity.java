package com.beast.echoplay.AudioPlayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;

import java.util.ArrayList;

public class AudioFolderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AudioFolderAdapter audioFolderAdapter;
    String myFolderName;
    ArrayList<AudioFiles> audioFilesArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_folder);
        recyclerView = findViewById(R.id.FolderAudioRV);
        myFolderName = getIntent().getStringExtra("folderName");
        if (myFolderName != null) {
            audioFilesArrayList = getAudioFiles(this, myFolderName);
        }
        if (audioFilesArrayList.size() > 0) {
            audioFolderAdapter = new AudioFolderAdapter(this, audioFilesArrayList);
            recyclerView.setAdapter(audioFolderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<AudioFiles> getAudioFiles(Context context, String folderName) {
        ArrayList<AudioFiles> tempAudioFiles = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST
        };
        String selection = MediaStore.Audio.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {

                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);
                String bucket = cursor.getString(7);
                String artist = cursor.getString(8);

                AudioFiles AudioFiles = new AudioFiles(id, title, path, duration, artist, size, dateAdded, fileName, bucket);
                if (folderName.endsWith(bucket)) {
                    tempAudioFiles.add(AudioFiles);
                }
            }
            cursor.close();
        }
        return tempAudioFiles;
    }
}