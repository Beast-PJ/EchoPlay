package com.beast.echoplay;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.beast.echoplay.AudioPlayer.AudioFiles;
import com.beast.echoplay.AudioPlayer.AudioFragment;
import com.beast.echoplay.VideoPlayer.FolderFragment;
import com.beast.echoplay.VideoPlayer.VideoFiles;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    public static final String MY_PREF = "my pref";
    public static ArrayList<VideoFiles> videoFiles = new ArrayList<>();
    public static ArrayList<AudioFiles> audioFiles = new ArrayList<>();
    public static ArrayList<String> videoFolderList = new ArrayList<>();
    public static ArrayList<String> audioFolderList = new ArrayList<>();
    private SharedPreferences.Editor editor;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean isNightMode;


    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isNightMode = sharedPreferences.getBoolean("NightMode", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.navigation_bar);
        swipeRefreshLayout = findViewById(R.id.swipe_refesh_layout);
        permission();


        swipeRefreshLayout.setOnRefreshListener(() -> {
            videoFiles = getVideoFiles(getApplicationContext());
            audioFiles = getAudioFiles(getApplicationContext());
            swipeRefreshLayout.setRefreshing(false);
        });

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences.edit();
        if (item.getItemId() == R.id.night_mode_switch) {
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("NightMode", false);
                item.setIcon(R.drawable.ic_day_mode);
                isNightMode = false;
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("NightMode", true);
                item.setIcon(R.drawable.ic_night_mode);
                isNightMode = true;
            }
            editor.apply();
        } else if (item.getItemId() == R.id.layout_btn) {
            Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.sort_by) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Sort By");
            alertDialog.setPositiveButton("Ok", (dialog, which) -> {
                editor1.apply();
                videoFiles = getVideoFiles(getApplicationContext());
                refreshFolderFragment();
                dialog.dismiss();
            });
            String[] items = {"Name (A to Z)", "Size (Big to Small)", "Date (New to Old)", "Length (Long to Short)"};
            alertDialog.setSingleChoiceItems(items, -1, (dialog, which) -> {
                switch (which) {
                    case 0:
                        editor1.putString("sort", "sortName");
                        break;
                    case 1:
                        editor1.putString("sort", "sortSize");
                        break;
                    case 2:
                        editor1.putString("sort", "sortDate");
                        break;
                    case 3:
                        editor1.putString("sort", "sortLength");
                        break;
                }
            });
            alertDialog.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshFolderFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, new FolderFragment());
        fragmentTransaction.commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK}, REQUEST_PERMISSION);
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
        SharedPreferences preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String sortValue = preferences.getString("sort", "sortName");
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();
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

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
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
                tempVideoFiles.add(videoFiles);
            }
            cursor.close();
        }

        sortVideoFolders(sortValue);
        return tempVideoFiles;
    }

    private void sortVideoFolders(String sortValue) {
        Map<String, Long> folderSizeMap = new HashMap<>();
        Map<String, Long> folderDateMap = new HashMap<>();

        for (String folder : videoFolderList) {
            folderSizeMap.put(folder, getFolderSize(folder));
            folderDateMap.put(folder, getFolderDate(folder));
        }

        switch (sortValue) {
            case "sortName":
                videoFolderList.sort(String::compareToIgnoreCase);
                break;
            case "sortSize":
                videoFolderList.sort((folder1, folder2) ->
                        Long.compare(folderSizeMap.get(folder2), folderSizeMap.get(folder1)));
                break;
            case "sortDate":
                videoFolderList.sort((folder1, folder2) ->
                        Long.compare(folderDateMap.get(folder2), folderDateMap.get(folder1)));
                break;

        }
    }

    private long getFolderSize(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null) return 0;
        long size = 0;
        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            } else if (file.isDirectory()) {
                size += getFolderSize(file.getAbsolutePath());
            }
        }
        return size;
    }

    private long getFolderDate(String folderPath) {
        File folder = new File(folderPath);
        long lastModified = folder.lastModified();
        File[] files = folder.listFiles();
        if (files == null) return lastModified;
        for (File file : files) {
            if (file.isFile()) {
                lastModified = Math.max(lastModified, file.lastModified());
            } else if (file.isDirectory()) {
                lastModified = Math.max(lastModified, getFolderDate(file.getAbsolutePath()));
            }
        }
        return lastModified;
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
