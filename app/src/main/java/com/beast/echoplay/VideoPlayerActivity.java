package com.beast.echoplay;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {
    private TextView videoTitle;
    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoTitle = findViewById(R.id.videoTitle);
        videoView = findViewById(R.id.videoView);

        MediaItem mediaItem = (MediaItem) getIntent().getSerializableExtra("mediaItem");
        if (mediaItem != null) {
            videoTitle.setText(mediaItem.getTitle());
            initializeVideoPlayer(mediaItem.getPath());
        }
    }

    private void initializeVideoPlayer(String path) {
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);

        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.start();
    }
}
