package com.beast.echoplay;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {
    private Context context;
    private List<MediaItem> mediaItemList;

    public MediaAdapter(Context context, List<MediaItem> mediaItemList) {
        this.context = context;
        this.mediaItemList = mediaItemList;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        MediaItem mediaItem = mediaItemList.get(position);
        holder.songName.setText(mediaItem.getTitle());
//        holder.artistName.setText(mediaItem.getArtist());
        holder.duration.setText(mediaItem.getDuration());

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            if (mediaItem.getMimeType().startsWith("audio")) {
                intent = new Intent(context, PlayerActivity.class);
            } else {
                intent = new Intent(context, VideoPlayerActivity.class);
            }
            intent.putExtra("mediaItem", mediaItem);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mediaItemList.size();
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        TextView songName, artistName, duration;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.song_name);
            artistName = itemView.findViewById(R.id.artist_name);
            duration = itemView.findViewById(R.id.duration);
        }
    }
}
