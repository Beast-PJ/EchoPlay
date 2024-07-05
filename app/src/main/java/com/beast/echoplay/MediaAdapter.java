package com.beast.echoplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MediaItem> mediaList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(MediaItem mediaItem);
    }

    public MediaAdapter(Context context, ArrayList<MediaItem> mediaList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mediaList = mediaList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaItem mediaItem = mediaList.get(position);
        holder.songName.setText(mediaItem.getTitle());
        holder.artist.setText(mediaItem.getArtist());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(mediaItem));
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView songName, artist, duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.song_name);
            artist = itemView.findViewById(R.id.artist);
            duration = itemView.findViewById(R.id.duration);
        }
    }
}
