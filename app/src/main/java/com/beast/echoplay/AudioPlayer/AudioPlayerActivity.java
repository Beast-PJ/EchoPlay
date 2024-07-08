package com.beast.echoplay.AudioPlayer;

import static com.beast.echoplay.AudioPlayer.AudioFolderAdapter.folderAudioFiles;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beast.echoplay.R;

import java.io.IOException;
import java.util.ArrayList;

public class AudioPlayerActivity extends AppCompatActivity {
    private TextView songTitle, songArtist, songDuration;
    private ImageView albumArt, playPauseButton;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();
    private Runnable updateSeekBar;
    ArrayList<AudioFiles> myFiles = new ArrayList<>();
    int postion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songTitle = findViewById(R.id.song_title);
        songArtist = findViewById(R.id.artist_name);
        songDuration = findViewById(R.id.totalTime);
        albumArt = findViewById(R.id.coverArt);
        playPauseButton = findViewById(R.id.play_pause_button);
        seekBar = findViewById(R.id.seek_bar);

        postion = getIntent().getIntExtra("postion", -1);
        myFiles = folderAudioFiles;
        String path = myFiles.get(postion).getPath();
        AudioFiles mediaItem = (AudioFiles) getIntent().getSerializableExtra("mediaItem");
        if (mediaItem != null) {
            songTitle.setText(mediaItem.getTitle());
            songArtist.setText(mediaItem.getArtist());
            songDuration.setText(mediaItem.getDuration());
            initializeMediaPlayer(mediaItem.getPath());
        }

        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause);
            }
        });

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    handler.postDelayed(this, 1000);
                }
            }
        };
    }

    private void initializeMediaPlayer(String path) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());
            handler.post(updateSeekBar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        mediaPlayer.setOnCompletionListener(mp -> playPauseButton.setImageResource(R.drawable.play));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBar);
    }
}