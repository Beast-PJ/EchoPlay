package com.beast.echoplay.AudioPlayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;

import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyHolder> {
    private final Context mcontext;
    private final List<AudioFiles> audioFiles;
    private final ArrayList<String> folderName;

    public FolderAdapter(Context mcontext, List<AudioFiles> audioFiles, ArrayList<String> folderName) {
        this.mcontext = mcontext;
        this.audioFiles = audioFiles;
        this.folderName = folderName;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.folder_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        int index = folderName.get(position).lastIndexOf("/");
        String folder = folderName.get(position).substring(index + 1);
        holder.folder.setText(folder);
        holder.counterFiles.setText(String.valueOf(NumberOfFiles(folderName.get(position))));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mcontext, AudioFolderActivity.class);
            intent.putExtra("folderName", folderName.get(position));
            mcontext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return folderName.size();
    }

    public int NumberOfFiles(String folderName) {
        int count_files = 0;
        for (AudioFiles audioFiles : audioFiles) {
            if (audioFiles.getPath().substring(0, audioFiles.getPath().lastIndexOf("/")).endsWith(folderName)) {
                count_files++;
            }
        }
        return count_files;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView folder, counterFiles;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            folder = itemView.findViewById(R.id.folder_name);
            counterFiles = itemView.findViewById(R.id.count_files_folder);
        }
    }


}
