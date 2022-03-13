package com.exmple.videoplayer_codewithnitish.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exmple.videoplayer_codewithnitish.Parsers.Str;
import com.exmple.videoplayer_codewithnitish.R;

import java.util.ArrayList;

public class SubtitleHandler extends VideoPlayerAvtivity {

    VideoPlayerAvtivity activity;
    public TextView subtitle_tv;
    LinearLayout videoView_track;
    ArrayList<Str> strList;
    int index = 0;
    int ablePostion = 0;

    public SubtitleHandler(VideoPlayerAvtivity activity) {
        this.activity = activity;
        subtitle_tv = activity.findViewById(R.id.subtitle_tv);
        videoView_track = activity.findViewById(R.id.videoView_track);

        subtitle_tv.setText("Beginig");
        videoView_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                activity.startActivityForResult(intent, 1);
                ablePostion = 0;
                index = 0;
            }
        });
    }


    public void setSubtitleInTV(int currentPosition) {
        if (index < strList.size() - 2 && currentPosition <= ablePostion) {
            Str currentStr = strList.get(index);
            if (currentPosition >= currentStr.getStartTime()) {
                if (currentPosition <= currentStr.getEndTime()) {
                    subtitle_tv.setVisibility(View.VISIBLE);
                    subtitle_tv.setText(currentStr.getText());
                } else {
                    findNext(currentPosition);
                }
            } else {
                findPrev(currentPosition);
            }
        } else if (currentPosition >= ablePostion) {
            subtitle_tv.setVisibility(View.GONE);
        }
    }

    private void findPrev(int currentPosition) {
        if (index - 1 > 0) {
            index--;
            Str currentStr = strList.get(index);
            if (currentPosition <= currentStr.getEndTime()) {
                if (currentPosition >= currentStr.getStartTime()) {
                    subtitle_tv.setVisibility(View.VISIBLE);
                    subtitle_tv.setText(currentStr.getText());
                } else {
                    findPrev(currentPosition);
                }
            }
        }
    }

    private void findNext(int currentPosition) {
        if (index + 1 < strList.size() - 2) {
            index++;
            Str currentStr = strList.get(index);
            if (currentPosition >= currentStr.getStartTime()) {
                if (currentPosition <= currentStr.getEndTime()) {
                    subtitle_tv.setText(currentStr.getText());
                } else {
                    findNext(currentPosition);
                }
            }
        }
    }

}
