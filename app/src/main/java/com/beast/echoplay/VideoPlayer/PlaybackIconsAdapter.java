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
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PlaybackIconsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icons_layout, parent, false);
        return new ViewHolder(view, mListener);
    }

    public PlaybackIconsAdapter(ArrayList<IconModel> iconModelsList, Context context) {
        this.iconModelArrayList = iconModelsList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
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

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            icon = itemView.findViewById(R.id.playback_icon);
            iconName = itemView.findViewById(R.id.icon_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
