package com.beast.echoplay.AudioPlayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;
import com.beast.echoplay.Utility;

import java.util.ArrayList;
import java.util.Objects;

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
        Objects.requireNonNull(holder).audioDuration.setText(Utility.timeConversion(Long.parseLong(folderAudioFiles.get(position).getDuration())));
        loadCoverArt(folderAudioFiles.get(position).getPath(), holder.item_thumbnail);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AudioPlayerActivity.class);
            intent.putExtra("position", position);  // corrected key here
            mContext.startActivity(intent);
        });


    }

    private void loadCoverArt(String path, ImageView albumArt) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            albumArt.setImageBitmap(bitmap);
        } else {
            albumArt.setImageResource(R.drawable.music_note);
        }
    }

    @Override
    public int getItemCount() {
        return folderAudioFiles.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView item_thumbnail;
        TextView fileName, audioDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_thumbnail = itemView.findViewById(R.id.album_art);
            fileName = itemView.findViewById(R.id.fileName);
            audioDuration = itemView.findViewById(R.id.audioDuration);
        }
    }
}
