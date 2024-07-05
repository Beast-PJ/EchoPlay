package com.beast.echoplay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView songTitle, artistName;
    private ImageButton playPauseButton, nextButton, previousButton;
    private ImageView coverArt;
    private ArrayList<MediaItem> playingQueue;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songTitle = findViewById(R.id.song_title);
        artistName = findViewById(R.id.artist_name);
        seekBar = findViewById(R.id.seek_bar);
        playPauseButton = findViewById(R.id.play_pause_button);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);
        coverArt = findViewById(R.id.coverArt);

        Intent intent = getIntent();
        MediaItem mediaItem = (MediaItem) intent.getSerializableExtra("mediaItem");
        playingQueue = (ArrayList<MediaItem>) intent.getSerializableExtra("queue");
        position = intent.getIntExtra("position", 0);

        setMediaItem(mediaItem);

        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause);
            }
        });

        nextButton.setOnClickListener(v -> playNext());
        previousButton.setOnClickListener(v -> playPrevious());
    }

    private void setMediaItem(MediaItem mediaItem) {
        songTitle.setText(mediaItem.getTitle());
        artistName.setText(mediaItem.getArtist());

        if (mediaItem.getMimeType().startsWith("audio")) {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(mediaItem.getData()));
            mediaPlayer.start();
            playPauseButton.setImageResource(R.drawable.pause);

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mediaItem.getData());
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
                coverArt.setImageBitmap(bitmap);
            } else {
                coverArt.setImageResource(R.drawable.ic_launcher_foreground);
            }

            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(0);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            mediaPlayer.setOnCompletionListener(mp -> playNext());

        }
    }

    private void playNext() {
        if (position < playingQueue.size() - 1) {
            position++;
            setMediaItem(playingQueue.get(position));
        }
    }

    private void playPrevious() {
        if (position > 0) {
            position--;
            setMediaItem(playingQueue.get(position));
        }
    }
}
