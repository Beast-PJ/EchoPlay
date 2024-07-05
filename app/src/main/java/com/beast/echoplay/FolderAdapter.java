package com.beast.echoplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private Context context;
    private List<String> folders;
    private OnFolderClickListener onFolderClickListener;

    public interface OnFolderClickListener {
        void onFolderClick(String folderName);
    }

    public FolderAdapter(Context context, List<String> folders, OnFolderClickListener onFolderClickListener) {
        this.context = context;
        this.folders = folders;
        this.onFolderClickListener = onFolderClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String folderName = folders.get(position);
        holder.folderName.setText(folderName);
        holder.itemView.setOnClickListener(v -> onFolderClickListener.onFolderClick(folderName));
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name);
        }
    }
}
