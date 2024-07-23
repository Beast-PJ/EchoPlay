package com.beast.echoplay.VideoPlayer;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class VideoFolderActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String MY_PREF = "my pref";
    RecyclerView recyclerView;
    VideoFolderAdapter videoFolderAdapter;
    String myFolderName, sort_order;
    ArrayList<VideoFiles> videoFilesArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);
        recyclerView = findViewById(R.id.FolderVideoRV);
        myFolderName = getIntent().getStringExtra("folderName");
        File file = new File(Objects.requireNonNull(myFolderName));
        String currentFolder = file.getName();
        getSupportActionBar().setTitle(currentFolder);

        if (myFolderName != null) {
            videoFilesArrayList = getVideoFiles(this, myFolderName);
        }
        if (videoFilesArrayList.size() > 0) {
            videoFolderAdapter = new VideoFolderAdapter(this, videoFilesArrayList);
            recyclerView.setAdapter(videoFolderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_video);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences.edit();
        if (item.getItemId() == R.id.layout_btn) {
            Toast.makeText(this, "More", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.sort_by) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Sort By");
            alertDialog.setPositiveButton("Ok", (dialog, which) -> {
                editor1.apply();
                finish();
                startActivity(getIntent());
                dialog.dismiss();
            });
            String[] items = {"Name (A to Z)", "Size (Big to Small)", "Date (New to Old)",
                    "Length (Long to Short)"};
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<VideoFiles> getVideoFiles(Context context, String folderName) {
        SharedPreferences preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String sortValue = preferences.getString("sort", "abcd");
        ArrayList<VideoFiles> tempvideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        switch (sortValue) {
            case "sortName":
                sort_order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
                break;
            case "sortSize":
                sort_order = MediaStore.MediaColumns.SIZE + " DESC";
                break;
            case "sortDate":
                sort_order = MediaStore.MediaColumns.DATE_ADDED + " DESC";
                break;
            case "sortLength":
                sort_order = MediaStore.MediaColumns.DURATION + " DESC";
                break;
        }

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        };
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sort_order);
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

                VideoFiles videoFiles = new VideoFiles(id, title, fileName, dateAdded, size, path, duration);
                if (folderName.endsWith(bucket)) {
                    tempvideoFiles.add(videoFiles);
                }
            }
            cursor.close();
        }
        return tempvideoFiles;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String inputs = newText.toLowerCase();
        ArrayList<VideoFiles> mediaFiles = new ArrayList<>();
        for (VideoFiles media : videoFilesArrayList) {
            if (media.getTitle().toLowerCase().contains(inputs)) {
                mediaFiles.add(media);
            }
        }
        videoFolderAdapter.updateVideoFiles(mediaFiles);
        return true;
    }
}