package com.beast.echoplay;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    private FolderAdapter folderAdapter;
    private HashMap<String, List<MediaItem>> mediaMap;
    static ArrayList<VideoFiles> videoFiles = new ArrayList<>();
    static ArrayList<String> folderList = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.navigation_bar);
        permission();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.folder_list) {
                Toast.makeText(MainActivity.this, "Folder List", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_fragment, new FolderFragment());
                fragmentTransaction.commit();
                return true;
            }   if (item.getItemId() == R.id.files_list) {
                Toast.makeText(MainActivity.this, "Files List", Toast.LENGTH_SHORT).show();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.main_fragment, new FilesFragment());
                fragmentTransaction2.commit();
                item.setChecked(true);
                return true;
            }
            return false;
        });
        Switch nightModeSwitch = findViewById(R.id.night_mode_switch);
        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            videoFiles =  getVideoFiles(this);
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
                videoFiles =  getVideoFiles(this);
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

                VideoFiles videoFiles = new VideoFiles(id, title, fileName, dateAdded, size, path);
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0, slashFirstIndex);
                int index = subString.lastIndexOf("/");
                String folderName = subString.substring(index + 1, slashFirstIndex);
                if (!folderList.contains(folderName)) {
                    folderList.add(folderName);
                }
                tempvideoFiles.add(videoFiles);
            }
            cursor.close();
        }
        return tempvideoFiles;
    }


}
