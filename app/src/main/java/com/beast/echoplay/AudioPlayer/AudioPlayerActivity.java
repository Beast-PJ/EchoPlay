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
    int position;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler();
    private Runnable updateSeekBar;
    ArrayList<AudioFiles> myFiles = new ArrayList<>();
    private ImageView albumArt, playPauseButton, prevButton, nextButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songTitle = findViewById(R.id.song_title);
        songArtist = findViewById(R.id.artist_name);
        songDuration = findViewById(R.id.totalTime);
        albumArt = findViewById(R.id.coverArt);
        playPauseButton = findViewById(R.id.play_pause_button);
        prevButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        seekBar = findViewById(R.id.seek_bar);

        position = getIntent().getIntExtra("postion", -1);
        myFiles = folderAudioFiles;
        String path = myFiles.get(position).getPath();
        AudioFiles mediaItem = (AudioFiles) getIntent().getSerializableExtra("mediaItem");
        if (path != null) {
            songTitle.setText(folderAudioFiles.get(position).getTitle());
            songArtist.setText(folderAudioFiles.get(position).getArtist());
            songDuration.setText(folderAudioFiles.get(position).getDuration());
            initializeMediaPlayer(path);
        }

        nextButton.setOnClickListener(v -> playNextSong());

        prevButton.setOnClickListener(v -> playPreviousSong());

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
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mediaPlayer.setOnCompletionListener(mp -> playPauseButton.setImageResource(R.drawable.play));
    }

    public void playNextSong() {
        if (position < folderAudioFiles.size() - 1) {
            position++;
            setupMediaPlayer();
            updateUI();
        }
    }

    public void playPreviousSong() {
        if (position > 0) {
            position--;
            setupMediaPlayer();
            updateUI();
        }
    }

    private void setupMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(folderAudioFiles.get(position).getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        AudioFiles currentSong = folderAudioFiles.get(position);
        songTitle.setText(currentSong.getTitle());
        songArtist.setText(currentSong.getArtist());
        playPauseButton.setImageResource(mediaPlayer.isPlaying() ? R.drawable.pause : R.drawable.play);
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
