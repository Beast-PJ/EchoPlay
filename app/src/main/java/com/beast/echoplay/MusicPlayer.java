package com.beast.echoplay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class MusicPlayer extends AppCompatActivity {

    private TextView songTitle, songArtist;
    private ImageView coverArt;
    private SeekBar seekBar;
    private ImageButton playPauseButton, nextButton, prevButton;
    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private int currentPosition;
    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songTitle = findViewById(R.id.songTitle);
        songArtist = findViewById(R.id.songArtist);
        coverArt = findViewById(R.id.coverArt);
        seekBar = findViewById(R.id.seekBar);
        playPauseButton = findViewById(R.id.playPauseButton);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);

        songList = (List<Song>) getIntent().getSerializableExtra("SONG_LIST");
        currentPosition = getIntent().getIntExtra("POSITION", 0);

        setupMediaPlayer();
        updateUI();

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseButton.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    playPauseButton.setImageResource(R.drawable.pause);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition < songList.size() - 1) {
                    currentPosition++;
                    setupMediaPlayer();
                    updateUI();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition > 0) {
                    currentPosition--;
                    setupMediaPlayer();
                    updateUI();
                }
            }
        });

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

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000);
            }
        };
    }

    private void setupMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(songList.get(currentPosition).getData());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    mediaPlayer.start();
                    playPauseButton.setImageResource(R.drawable.pause);
                    handler.post(updateSeekBar);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        Song currentSong = songList.get(currentPosition);
        songTitle.setText(currentSong.getName());
        songArtist.setText(currentSong.getArtist());

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(currentSong.getData());
        byte[] artBytes = mmr.getEmbeddedPicture();
        if (artBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            coverArt.setImageBitmap(bitmap);
        } else {
            coverArt.setImageResource(R.drawable.ic_launcher_foreground); // Set a default image if no cover art is found
        }
        playPauseButton.setImageResource(mediaPlayer.isPlaying()?R.drawable.play:R.drawable.pause);

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
