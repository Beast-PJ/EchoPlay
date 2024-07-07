package com.beast.echoplay;

import static com.beast.echoplay.MainActivity.folderList;
import static com.beast.echoplay.MainActivity.videoFiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FolderFragment extends Fragment {
    FolderAdapter folderAdapter;
    RecyclerView recyclerView;

    public FolderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        recyclerView = view.findViewById(R.id.folderRV);
        if (folderList != null && folderList.size() > 0 && videoFiles != null) {
            folderAdapter = new FolderAdapter(getContext(), videoFiles, folderList);
            recyclerView.setAdapter(folderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }
}