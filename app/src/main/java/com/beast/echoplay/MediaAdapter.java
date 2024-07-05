package com.beast.echoplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private final Context context;
    private final List<MediaItem> mediaItems;
    private final OnMediaClickListener mediaClickListener;

    public MediaAdapter(Context context, List<MediaItem> mediaItems, OnMediaClickListener mediaClickListener) {
        this.context = context;
        this.mediaItems = mediaItems;
        this.mediaClickListener = mediaClickListener;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        try {
            MediaItem mediaItem = mediaItems.get(position);
            holder.songNameTextView.setText(mediaItem.getTitle());
            holder.artistTextView.setText(mediaItem.getArtist());
            holder.durationTextView.setText(formatDuration(Long.parseLong(mediaItem.getDuration())));

            // Retrieve and set album art
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mediaItem.getPath());
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
                holder.albumArtImageView.setImageBitmap(bitmap);
            } else {
                holder.albumArtImageView.setImageResource(R.drawable.ic_launcher_foreground); // default image
            }
            mmr.release();

            holder.itemView.setOnClickListener(v -> mediaClickListener.onMediaClick(mediaItem, mediaItems));
        } catch (Exception e) {
            Log.e("MediaAdapter", "Error binding media item", e);
        }
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        TextView songNameTextView;
        TextView artistTextView;
        TextView durationTextView;
        ImageView albumArtImageView;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            songNameTextView = itemView.findViewById(R.id.song_name);
            artistTextView = itemView.findViewById(R.id.artist);
            durationTextView = itemView.findViewById(R.id.duration);
            albumArtImageView = itemView.findViewById(R.id.album_art);
        }
    }

    public interface OnMediaClickListener {
        void onMediaClick(MediaItem mediaItem, List<MediaItem> mediaItems);
    }

    private String formatDuration(long duration) {
        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
