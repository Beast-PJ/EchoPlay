package com.beast.echoplay.VideoPlayer;

import static com.beast.echoplay.VideoPlayer.VideoFolderAdapter.foldervideoFiles;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beast.echoplay.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;

import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView video_title;
    ImageView next, previous, videoBack, lock, unlock, scaling;
    RelativeLayout root;
    int postion = -1;
    ArrayList<VideoFiles> myFiles = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenMethod();
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.exoplayer);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.prev);
        videoBack = findViewById(R.id.video_back);
        lock = findViewById(R.id.lock);
        unlock = findViewById(R.id.unlock);
        scaling = findViewById(R.id.scaling);
        root = findViewById(R.id.root_layout);
        video_title = findViewById(R.id.video_text);
        postion = getIntent().getIntExtra("postion", -1);
        myFiles = foldervideoFiles;
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        videoBack.setOnClickListener(this);
        lock.setOnClickListener(this);
        unlock.setOnClickListener(this);
        scaling.setOnClickListener(this);
        playVideo(postion);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String contentDescription = String.valueOf(v.getContentDescription());
        switch (contentDescription) {
            case "next":
                try {
                    simpleExoPlayer.stop();
                    postion++;
                    playVideo(postion);
                } catch (Exception e) {
                    Toast.makeText(this, "No Next Video", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case "prev":
                try {
                    simpleExoPlayer.stop();
                    postion--;
                    playVideo(postion);
                } catch (Exception e) {
                    Toast.makeText(this, "No Next Video", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case "videoBack":
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.release();
                }
                finish();
                break;

            case "lock":
                ControlsMode controlsMode = ControlsMode.FULLSCREEN;
                root.setVisibility(View.VISIBLE);
                lock.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "unLocked", Toast.LENGTH_SHORT).show();
                break;

            case "unlock":
                controlsMode = ControlsMode.LOCK;
                root.setVisibility(View.INVISIBLE);
                lock.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Locked", Toast.LENGTH_SHORT).show();
                break;

            case "scaling":

                break;
        }
    }

    public void playVideo(int position) {
        String path = myFiles.get(postion).getPath();
        String video_name = myFiles.get(position).getFileName();
        video_title.setText(video_name);
        if (path != null) {
            Uri uri = Uri.parse(path);
            simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
            DefaultDataSource.Factory factory = new DefaultDataSource.Factory(this);
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(MediaItem.fromUri(uri));
            playerView.setPlayer(simpleExoPlayer);
            playerView.setKeepScreenOn(true);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    public enum ControlsMode {
        LOCK, FULLSCREEN
    }

    private void setFullScreenMethod() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.stop();
    }

}
