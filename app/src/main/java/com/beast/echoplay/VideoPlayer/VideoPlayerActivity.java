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
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;

import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView video_title;
    ImageView next, previous, videoBack, lock, unlock, scaling;
    RelativeLayout root;
    boolean expand = false;
    int postion = -1;
    ControlsMode controlsMode;

    ArrayList<IconModel> iconModelArrayList = new ArrayList<>();
    PlaybackIconsAdapter playbackIconsAdapter;
    RecyclerView recyclerViewIcons;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenMethod();
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.exoplayer);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.prev);
        videoBack = findViewById(R.id.video_back);
        unlock = findViewById(R.id.unlock);
        lock = findViewById(R.id.lock);
        scaling = findViewById(R.id.scaling);
        root = findViewById(R.id.root_layout);
        video_title = findViewById(R.id.video_text);
        recyclerViewIcons = findViewById(R.id.recyclerview_icon);
        postion = getIntent().getIntExtra("postion", -1);
        myFiles = foldervideoFiles;
//        screenOrientation();
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        videoBack.setOnClickListener(this);
        lock.setOnClickListener(this);
        unlock.setOnClickListener(this);
        scaling.setOnClickListener(firstListerner);

        iconModelArrayList.add(new IconModel(R.drawable.right, ""));
        iconModelArrayList.add(new IconModel(R.drawable.ic_night_mode, "Night"));
        iconModelArrayList.add(new IconModel(R.drawable.volume_off, "Mute"));
        iconModelArrayList.add(new IconModel(R.drawable.screen_rotation, "Rotate"));
        playbackIconsAdapter = new PlaybackIconsAdapter(iconModelArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL, true);
        recyclerViewIcons.setLayoutManager(layoutManager);
        recyclerViewIcons.setAdapter(playbackIconsAdapter);
        playbackIconsAdapter.notifyDataSetChanged();
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

            case "unlock":
                controlsMode = ControlsMode.FULLSCREEN;
                root.setVisibility(View.VISIBLE);
                unlock.setVisibility(View.INVISIBLE);
                break;

            case "lock":
                controlsMode = ControlsMode.LOCK;
                root.setVisibility(View.INVISIBLE);
                unlock.setVisibility(View.VISIBLE);
                break;

        }
    }

    ArrayList<VideoFiles> myFiles = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
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



    View.OnClickListener firstListerner = new View.OnClickListener() {
        @UnstableApi
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            simpleExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.fullscreen);
            scaling.setOnClickListener(secondListerner);
        }
    };
    View.OnClickListener secondListerner = new View.OnClickListener() {
        @UnstableApi
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            simpleExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.zoom);
            scaling.setOnClickListener(thirdListerner);
        }
    };
    View.OnClickListener thirdListerner = new View.OnClickListener() {
        @UnstableApi
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            simpleExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.fit_screen);
            scaling.setOnClickListener(firstListerner);
        }
    };

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
