package com.beast.echoplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyHolder> {
    private final Context mcontext;
    private final List<VideoFiles> videoFiles;
    private final ArrayList<String> folderName;

    public FolderAdapter(Context mcontext, List<VideoFiles> videoFiles, ArrayList<String> folderName) {
        this.mcontext = mcontext;
        this.videoFiles = videoFiles;
        this.folderName = folderName;
    }

    @NonNull
    @Override
    public FolderAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.folder_item, parent, false);
        return new FolderAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderAdapter.MyHolder holder, int position) {
        holder.folder.setText(folderName.get(position));

    }

    @Override
    public int getItemCount() {
        return folderName.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView folder;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            folder = itemView.findViewById(R.id.folder_name);
        }
    }


}
