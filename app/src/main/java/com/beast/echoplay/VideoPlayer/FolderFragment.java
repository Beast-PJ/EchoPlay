package com.beast.echoplay.VideoPlayer;

import static com.beast.echoplay.MainActivity.videoFiles;
import static com.beast.echoplay.MainActivity.videoFolderList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;

public class FolderFragment extends Fragment {
    FolderAdapter folderAdapter;
    RecyclerView recyclerView;

    public FolderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        recyclerView = view.findViewById(R.id.folderRV);
        if (videoFolderList != null && videoFolderList.size() > 0 && videoFiles != null) {
            folderAdapter = new FolderAdapter(getContext(), videoFiles, videoFolderList);
            recyclerView.setAdapter(folderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }
}