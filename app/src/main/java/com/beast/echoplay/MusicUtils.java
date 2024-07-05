package com.beast.echoplay;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.util.ArrayList;

public class MusicUtils {

    public static ArrayList<MediaItem> getAllMedia(Context context) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Files.getContentUri("external");

        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DURATION,
                MediaStore.Files.FileColumns.ARTIST
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Cursor cursor = contentResolver.query(uri, projection, selection, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.ARTIST));

                MediaItem mediaItem = new MediaItem(id, title, mimeType, data, duration, artist);
                mediaItems.add(mediaItem);
            }
            cursor.close();
        }
        return mediaItems;
    }
}
