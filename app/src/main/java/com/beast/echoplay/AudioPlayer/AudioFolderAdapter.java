package com.beast.echoplay.AudioPlayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class AudioFolderAdapter extends RecyclerView.Adapter<AudioFolderAdapter.MyViewHolder> {
    static ArrayList<AudioFiles> folderAudioFiles;
    private final Context mContext;
    View view;

    public AudioFolderAdapter(Context mContext, ArrayList<AudioFiles> folderAudioFiles) {
        this.mContext = mContext;
        AudioFolderAdapter.folderAudioFiles = folderAudioFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.fileName.setText(folderAudioFiles.get(position).getTitle());
        holder.audioDuration.setText(formatDuration(Long.parseLong(folderAudioFiles.get(position).getDuration())));
        Glide.with(mContext).load(new File(folderAudioFiles.get(position).getPath())).into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AudioPlayerActivity.class);
                intent.putExtra("position", position);  // corrected key here
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderAudioFiles.size();
    }

    @SuppressLint("DefaultLocale")
    private String formatDuration(long duration) {
        duration = duration + 1;
        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView fileName, audioDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.album_art);
            fileName = itemView.findViewById(R.id.fileName);
            audioDuration = itemView.findViewById(R.id.audioDuration);
        }
    }
}
