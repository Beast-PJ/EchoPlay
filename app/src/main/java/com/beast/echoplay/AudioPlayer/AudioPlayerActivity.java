package com.beast.echoplay.AudioPlayer;

import static com.beast.echoplay.AudioPlayer.AudioFolderAdapter.folderAudioFiles;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beast.echoplay.R;
import com.beast.echoplay.Utility;

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
    private ImageView albumArt, playPauseButton;

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
        ImageView prevButton = findViewById(R.id.previous_button);
        ImageView nextButton = findViewById(R.id.next_button);
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
                handler.post(updateSeekBar);
            }

        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    songCurrentTime.setText(Utility.timeConversion((long) progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                handler.post(updateSeekBar);
            }
        });

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    songCurrentTime.setText(Utility.timeConversion((long) mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 100);
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
        if (!mediaItem.getArtist().equals("<unknown>"))
            songArtist.setText(mediaItem.getArtist());
        else songArtist.setVisibility(View.INVISIBLE);
        songDuration.setText(Utility.timeConversion(Long.parseLong(mediaItem.getDuration())));
        songTitle.setSelected(true);
        loadCoverArt(mediaItem.getPath(), albumArt);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mediaItem.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());
            handler.post(updateSeekBar);
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


    private void loadCoverArt(String path, ImageView albumArt) {
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
