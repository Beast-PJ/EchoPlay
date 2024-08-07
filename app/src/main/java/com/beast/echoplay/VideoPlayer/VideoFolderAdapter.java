package com.beast.echoplay.VideoPlayer;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.beast.echoplay.R;
import com.beast.echoplay.Utility;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyViewHolder> {
    public static ArrayList<VideoFiles> foldervideoFiles;
    private final Context mContext;
    BottomSheetDialog bottomSheetDialog;
    View view;

    public VideoFolderAdapter(Context mContext, ArrayList<VideoFiles> foldervideoFiles) {
        this.mContext = mContext;
        VideoFolderAdapter.foldervideoFiles = foldervideoFiles;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"CheckResult", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.fileName.setText(foldervideoFiles.get(position).getTitle());
        holder.videoDuration.setText(Utility.timeConversion(Long.parseLong(foldervideoFiles.get(position).getDuration())));
        Glide.with(mContext).load(new File(foldervideoFiles.get(position).getPath())).into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, VideoPlayerActivity.class);
            intent.putExtra("postion", position);
            mContext.startActivity(intent);
        });
        holder.menuMore.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetTheme);
            View bsView = LayoutInflater.from(mContext).inflate(R.layout.video_bs_layout,
                    v.findViewById(R.id.bottom_sheet));
            bsView.findViewById(R.id.bs_close).setOnClickListener(v12 -> bottomSheetDialog.dismiss());
            bsView.findViewById(R.id.bs_play).setOnClickListener(v1 -> {
                holder.itemView.performClick();
                bottomSheetDialog.dismiss();
            });
            bsView.findViewById(R.id.bs_rename).setOnClickListener(v13 -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Rename to");
                EditText editText = new EditText(mContext);
                String path = foldervideoFiles.get(position).getPath();
                final File file = new File(path);
                String videoName = file.getName();
                videoName = videoName.substring(0, videoName.lastIndexOf("."));
                editText.setText(videoName);
                alertDialog.setView(editText);
                editText.requestFocus();

                alertDialog.setPositiveButton("OK", (dialog, which) -> {
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        Toast.makeText(mContext, "Can't rename empty file", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String onlyPath = Objects.requireNonNull(file.getParentFile()).getAbsolutePath();
                    String ext = file.getAbsolutePath();
                    ext = ext.substring(ext.lastIndexOf("."));
                    String newPath = onlyPath + "/" + editText.getText().toString() + ext;
                    File newFile = new File(newPath);
                    boolean rename = file.renameTo(newFile);
                    if (rename) {
                        ContentResolver resolver = mContext.getApplicationContext().getContentResolver();
                        resolver.delete(MediaStore.Files.getContentUri("external"),
                                MediaStore.MediaColumns.DATA + "=?", new String[]
                                        {file.getAbsolutePath()});
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(newFile));
                        mContext.getApplicationContext().sendBroadcast(intent);

                        notifyDataSetChanged();
                        Toast.makeText(mContext, "Video Renamed", Toast.LENGTH_SHORT).show();

                        SystemClock.sleep(200);
                        ((Activity) mContext).recreate();
                    } else {
                        Toast.makeText(mContext, "Process Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                alertDialog.create().show();
                bottomSheetDialog.dismiss();
            });

            bsView.findViewById(R.id.bs_share).setOnClickListener(v14 -> {
                Uri uri = Uri.parse(foldervideoFiles.get(position).getPath());
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("video/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                mContext.startActivity(Intent.createChooser(shareIntent, "Share Video via"));
                bottomSheetDialog.dismiss();
            });

            bsView.findViewById(R.id.bs_delete).setOnClickListener(v15 -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Do you want to delete this video");
                alertDialog.setPositiveButton("Delete", (dialog, which) -> {
                    Uri contentUri = ContentUris
                            .withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    Long.parseLong(foldervideoFiles.get(position).getId()));
                    File file = new File(foldervideoFiles.get(position).getPath());
                    boolean delete = file.delete();
                    if (delete) {
                        mContext.getContentResolver().delete(contentUri, null, null);
                        foldervideoFiles.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, foldervideoFiles.size());
                        Toast.makeText(mContext, "Video Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "can't deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                alertDialog.show();
                bottomSheetDialog.dismiss();
            });

            bsView.findViewById(R.id.bs_properties).setOnClickListener(v16 -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Properties");

                String one = "File: " + foldervideoFiles.get(position).getFileName();

                String path = foldervideoFiles.get(position).getPath();
                int indexOfPath = path.lastIndexOf("/");
                String two = "Path: " + path.substring(0, indexOfPath);

                String three = "Size: " + android.text.format.Formatter
                        .formatFileSize(mContext, Long.parseLong(foldervideoFiles.get(position).getSize()));

                String four = "Length: " + Utility.timeConversion(Long.valueOf(foldervideoFiles.get(position).getDuration()));

                String namewithFormat = foldervideoFiles.get(position).getFileName();
                int index = namewithFormat.lastIndexOf(".");
                String format = namewithFormat.substring(index + 1);
                String five = "Format: " + format;


                alertDialog.setMessage(one + "\n\n" + two + "\n\n" + three + "\n\n" + four +
                        "\n\n" + five);
                alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                alertDialog.show();
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.setContentView(bsView);
            bottomSheetDialog.show();

        });


    }

    @Override
    public int getItemCount() {
        return foldervideoFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, menuMore;
        TextView fileName, videoDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            fileName = itemView.findViewById(R.id.video_file_name);
            videoDuration = itemView.findViewById(R.id.vid_duration);
            menuMore = itemView.findViewById(R.id.more);
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateVideoFiles(ArrayList<VideoFiles> files) {
        foldervideoFiles = new ArrayList<>();
        foldervideoFiles.addAll(files);
        notifyDataSetChanged();
    }
}
