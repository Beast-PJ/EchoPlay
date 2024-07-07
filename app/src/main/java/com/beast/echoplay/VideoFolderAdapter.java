package com.beast.echoplay;

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

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyViewHolder> {
    static ArrayList<VideoFiles> foldervideoFiles;
    private final Context mContext;
    View view;

    public VideoFolderAdapter(Context mContext, ArrayList<VideoFiles> foldervideoFiles) {
        this.mContext = mContext;
        VideoFolderAdapter.foldervideoFiles = foldervideoFiles;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.fileName.setText(foldervideoFiles.get(position).getTitle());
        holder.videoDuration.setText(foldervideoFiles.get(position).getSize());
        Glide.with(mContext).load(new File(foldervideoFiles.get(position).getPath())).into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                intent.putExtra("postion", position);
                intent.putExtra("sender", "FolderIsSending");
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foldervideoFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail, menuMore;
        TextView fileName, videoDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            menuMore = itemView.findViewById(R.id.menu_more);
            fileName = itemView.findViewById(R.id.video_file_name);
            videoDuration = itemView.findViewById(R.id.vid_duration);
        }
    }
}
