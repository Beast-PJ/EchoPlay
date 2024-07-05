package com.beast.echoplay;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MusicUtils {

    public static HashMap<String, List<MediaItem>> getAllMediaByFolder(Context context) {
        HashMap<String, List<MediaItem>> mediaMap = new HashMap<>();

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");

        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DURATION,
                MediaStore.Files.FileColumns.ARTIST,
                MediaStore.Files.FileColumns.PARENT
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Cursor cursor = contentResolver.query(uri, projection, selection, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                @SuppressLint("Range") String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION));
                @SuppressLint("Range") String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.ARTIST));
                @SuppressLint("Range") String parent = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.PARENT));
                int dataIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                String path = cursor.getString(dataIndex);

                MediaItem mediaItem = new MediaItem(id, title, mimeType, data, path, duration, artist);

                if (!mediaMap.containsKey(parent)) {
                    mediaMap.put(parent, new ArrayList<>());
                }
                mediaMap.get(parent).add(mediaItem);
            }
            cursor.close();
        }

        return mediaMap;
    }
}
