package com.exmple.videoplayer_codewithnitish.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.exmple.videoplayer_codewithnitish.R;
import com.exmple.videoplayer_codewithnitish.VideoModel;
import com.exmple.videoplayer_codewithnitish.adapter.FolderAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> folderList = new ArrayList<>();
    private ArrayList<VideoModel> videoList = new ArrayList<>();
    FolderAdapter folderAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        videoList = fetchAllVideos(this);
        if (folderList != null && folderList.size() > 0 && videoList != null) {
            folderAdapter = new FolderAdapter(folderList, videoList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(folderAdapter);
        } else {
            Toast.makeText(this, "no any video detect", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.getFiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, GetFileActivity.class));
            }
        });
    }

    private ArrayList<VideoModel> fetchAllVideos(Context context) {
        ArrayList<VideoModel> videoModels = new ArrayList<>();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC ";

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.RESOLUTION
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, orderBy);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String resolution = cursor.getString(4);
                String duration = cursor.getString(5);
                String disName = cursor.getString(6);
                String width_hight = cursor.getString(7);

                VideoModel videoModel = new VideoModel(id, path, title, size, resolution, duration, disName, width_hight);

                int slashFirstIndex = path.lastIndexOf("/");

                String subString = path.substring(0, slashFirstIndex);

                if (!folderList.contains(subString)) {
                    folderList.add(subString);
                }

                videoModels.add(videoModel);
            }
        }
        assert cursor != null;
        cursor.close();

        return videoModels;
    }
}