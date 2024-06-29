package com.beast.echoplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {

    private TextView songTitle, artistName, currentTime, totalTime;
    private ImageView coverArt, playPauseButton, nextButton, previousButton;
    private SeekBar seekBar;
    private ArrayList<Song> songList;
    private int position;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songTitle = findViewById(R.id.songTitle);
        artistName = findViewById(R.id.songArtist);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        coverArt = findViewById(R.id.coverArt);
        playPauseButton = findViewById(R.id.playPauseButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.prevButton);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        songList = (ArrayList<Song>) intent.getSerializableExtra("SONG_LIST");
        position = intent.getIntExtra("POSITION", 0);

        playSong();

        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause);
            }
        });

        nextButton.setOnClickListener(v -> playNextSong());

        previousButton.setOnClickListener(v -> playPreviousSong());

        mediaPlayer.setOnCompletionListener(mp -> playNextSong());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateSeekBar);
                mediaPlayer.seekTo(seekBar.getProgress());
                updateSeekBar.run();
            }
        });

        updateSeekBar.run();
    }

    private void playSong() {
        Song song = songList.get(position);
        songTitle.setText(song.getName());
        artistName.setText(song.getArtist());

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(song.getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.pause);
        } catch (IOException e) {
            e.printStackTrace();
        }

        totalTime.setText(formatDuration(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());

        loadCoverArt(song.getData());

        mediaPlayer.setOnCompletionListener(mp -> playNextSong());
    }

    private void playNextSong() {
        if (position < songList.size() - 1) {
            position++;
        } else {
            position = 0;
        }
        playSong();
    }

    private void playPreviousSong() {
        if (position > 0) {
            position--;
        } else {
            position = songList.size() - 1;
        }
        playSong();
    }

    private String formatDuration(int duration) {
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void loadCoverArt(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            coverArt.setImageBitmap(bitmap);
        } else {
            coverArt.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            currentTime.setText(formatDuration(mediaPlayer.getCurrentPosition()));
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            handler.removeCallbacks(updateSeekBar);
            mediaPlayer.release();
        }
    }
}
