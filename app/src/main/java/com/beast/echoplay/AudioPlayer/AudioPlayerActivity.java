package com.beast.echoplay.AudioPlayer;

import static com.beast.echoplay.AudioPlayer.AudioFolderAdapter.folderAudioFiles;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
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
    private TextView songTitle, songArtist, songDuration, songCurrentTime;
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
        songCurrentTime = findViewById(R.id.currentTime);
        albumArt = findViewById(R.id.coverArt);
        playPauseButton = findViewById(R.id.play_pause_button);
        prevButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        seekBar = findViewById(R.id.seek_bar);

        position = getIntent().getIntExtra("position", -1);
        myFiles = folderAudioFiles;

        if (position != -1) {
            playAudio(position);
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
                handler.post(updateSeekBar); // Ensure the seek bar updates are resumed
            }

        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    songCurrentTime.setText(formatDuration(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                handler.post(updateSeekBar); // Resume the seek bar updates after seeking
            }
        });

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    songCurrentTime.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 1000); // Update every second
                }
            }
        };
        updateSeekBar.run();
    }

    private void playAudio(int position) {

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        AudioFiles mediaItem = myFiles.get(position);
        songTitle.setText(mediaItem.getTitle());
        songArtist.setText(mediaItem.getArtist());
        songDuration.setText(formatDuration(Long.parseLong(mediaItem.getDuration())));
        songTitle.setSelected(true);
        loadCoverArt(mediaItem.getPath());

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mediaItem.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());
            handler.post(updateSeekBar); // Start the seek bar update runnable
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(mp -> {
            playPauseButton.setImageResource(R.drawable.play);
            playNextSong();
        });
    }

    public void playNextSong() {
        if (position < myFiles.size() - 1) {
            position++;
            playAudio(position);
        }
    }

    public void playPreviousSong() {
        if (position > 0) {
            position--;
            playAudio(position);
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatDuration(long duration) {
        long minutes = (duration / 1000) / 60;
        long seconds = (duration / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void loadCoverArt(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        if (art != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            albumArt.setImageBitmap(bitmap);
        } else {
            albumArt.setImageResource(R.drawable.music_note);
        }
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
