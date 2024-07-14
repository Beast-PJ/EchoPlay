package com.beast.echoplay;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.beast.echoplay.AudioPlayer.AudioFiles;
import com.beast.echoplay.AudioPlayer.AudioFragment;
import com.beast.echoplay.VideoPlayer.FolderAdapter;
import com.beast.echoplay.VideoPlayer.FolderFragment;
import com.beast.echoplay.VideoPlayer.VideoFiles;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    private FolderAdapter folderAdapter;
    public static ArrayList<VideoFiles> videoFiles = new ArrayList<>();
    public static ArrayList<AudioFiles> audioFiles = new ArrayList<>();
    public static ArrayList<String> videoFolderList = new ArrayList<>();
    public static ArrayList<String> audioFolderList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    boolean isNightMode;


    BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        isNightMode = sharedPreferences.getBoolean("NightMode", false);
//        if (isNightMode) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }

        bottomNavigationView = findViewById(R.id.navigation_bar);
        permission();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.folder_list) {
                item.setChecked(true);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment, new FolderFragment());
                fragmentTransaction.commit();
                return true;
            }
            if (item.getItemId() == R.id.files_list) {
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.main_fragment, new AudioFragment());
                fragmentTransaction2.commit();
                item.setChecked(true);
                return true;
            }
            return false;
        });
        ImageButton nightMode = findViewById(R.id.night_mode_switch);
        nightMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check the current state of night mode
                    if (isNightMode) {
                        // If night mode is currently on, turn it off
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        editor.putBoolean("NightMode", false);
                        // Change the ImageButton to day mode icon
                        nightMode.setImageResource(R.drawable.ic_day_mode);  // Set to your day mode image resource
                        isNightMode = false;
                    } else {
                        // If night mode is currently off, turn it on
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        editor.putBoolean("NightMode", true);
                        // Change the ImageButton to night mode icon
                        nightMode.setImageResource(R.drawable.ic_night_mode);  // Set to your night mode image resource
                        isNightMode = true;
                    }
                    editor.apply();  // Apply changes to shared preferences
                }
            });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            videoFiles = getVideoFiles(this);
            audioFiles = getAudioFiles(this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, new FolderFragment());
            fragmentTransaction.commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                videoFiles = getVideoFiles(this);
                audioFiles = getAudioFiles(this);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment, new FolderFragment());
                fragmentTransaction.commit();
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<VideoFiles> getVideoFiles(Context context) {
        ArrayList<VideoFiles> tempvideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null);
        if (cursor != null){
            while (cursor.moveToNext()){

                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);

                VideoFiles videoFiles = new VideoFiles(id, title, fileName, dateAdded, size, path, duration);
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0, slashFirstIndex);

                if (!videoFolderList.contains(subString)) {
                    videoFolderList.add(subString);
                }
                tempvideoFiles.add(videoFiles);
            }
            cursor.close();
        }
        return tempvideoFiles;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<AudioFiles> getAudioFiles(Context context) {
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
        Cursor cursor2 = context.getContentResolver().query(uri, projection, null, null);
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {

                String id = cursor2.getString(0);
                String path = cursor2.getString(1);
                String title = cursor2.getString(2);
                String size = cursor2.getString(3);
                String dateAdded = cursor2.getString(4);
                String duration = cursor2.getString(5);
                String fileName = cursor2.getString(6);
                String bucket = cursor2.getString(7);
                String artist = cursor2.getString(8);

                AudioFiles audioFiles = new AudioFiles(id, title, path, duration, artist);
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0, slashFirstIndex);

                if (!audioFolderList.contains(subString)) {
                    audioFolderList.add(subString);
                }
                tempAudioFiles.add(audioFiles);
            }
            cursor2.close();
        }
        return tempAudioFiles;
    }


}
