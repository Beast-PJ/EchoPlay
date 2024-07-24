package com.beast.echoplay.AudioPlayer;

import static com.beast.echoplay.MainActivity.audioFiles;
import static com.beast.echoplay.MainActivity.audioFolderList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;

public class AudioFragment extends Fragment {

    RecyclerView recyclerView;
    View view;
    FolderAdapter audioAdapter;

    public AudioFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_files, container, false);
        recyclerView = view.findViewById(R.id.audioFolderRV);
        if (audioFiles != null && audioFiles.size() > 0) {
            audioAdapter = new FolderAdapter(getContext(), audioFiles, audioFolderList);
            recyclerView.setAdapter(audioAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }
}