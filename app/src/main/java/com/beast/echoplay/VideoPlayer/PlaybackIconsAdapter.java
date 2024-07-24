package com.beast.echoplay.VideoPlayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;

import java.util.ArrayList;

public class PlaybackIconsAdapter extends RecyclerView.Adapter<PlaybackIconsAdapter.ViewHolder> {
    private final ArrayList<IconModel> iconModelArrayList;
    private final Context context;

    public PlaybackIconsAdapter(ArrayList<IconModel> iconModelsList, Context context) {
        this.iconModelArrayList = iconModelsList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaybackIconsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icons_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaybackIconsAdapter.ViewHolder holder, int position) {
        holder.icon.setImageResource(iconModelArrayList.get(position).getImageView());
        holder.iconName.setText(iconModelArrayList.get(position).getIconTitle());
    }

    @Override
    public int getItemCount() {
        return iconModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView iconName;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.playback_icon);
            iconName = itemView.findViewById(R.id.icon_title);
        }
    }
}
