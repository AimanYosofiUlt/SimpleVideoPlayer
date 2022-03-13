package com.exmple.videoplayer_codewithnitish.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.exmple.videoplayer_codewithnitish.Parsers.StrParser;
import com.exmple.videoplayer_codewithnitish.R;
import com.exmple.videoplayer_codewithnitish.VideoModel;
import com.exmple.videoplayer_codewithnitish.adapter.VideoAdapter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


public class VideoPlayerAvtivity extends AppCompatActivity
        implements View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener, View.OnClickListener {
    int position = -1;
    VideoView videoView;

    LinearLayout one, tow, three, four;
    RelativeLayout zoomLayout;
    boolean isOpen = true;
    ScaleGestureDetector scaleDetector;
    GestureDetectorCompat gestureDetector;

    static final float MIN_ZOOM = 1.0f;
    static final float MAX_ZOOM = 5.0f;
    boolean intleft, intRight;
    Display display;
    Point size;
    private Mode mode = Mode.NONE;


    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    int device_width;
    int sWidth;
    boolean isEnable = true;
    float scale = 1.0f;
    float lastScaleFactor = 0f;
    float startx = 0f;
    float starty = 0f;
    float dx = 0f;
    float dy = 0f;
    float prevDx = 0f;
    float prevDy = 0f;

    ImageButton goBack, rewind, forward, playPause;
    TextView title, endTime;
    SeekBar videoSeekbar;
    SubtitleHandler subHandler;
    LinearLayout videoView_rotation, videoView_lock_screen;
    VidSeekBar vidSeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Objects.requireNonNull(getSupportActionBar()).hide();
        subHandler = new SubtitleHandler(this);
        videoView = findViewById(R.id.video_view);
        one = findViewById(R.id.videoView_one_layout);
        tow = findViewById(R.id.videoView_two_layout);
        three = findViewById(R.id.videoView_three_layout);
        four = findViewById(R.id.videoView_four_layout);
        goBack = findViewById(R.id.videoView_go_back);
        rewind = findViewById(R.id.videoView_rewind);
        forward = findViewById(R.id.videoView_forward);
        playPause = findViewById(R.id.videoView_play_pause_btn);
        endTime = findViewById(R.id.videoView_endtime);
        videoSeekbar = findViewById(R.id.videoView_seekbar);
        title = findViewById(R.id.videoView_title);
        videoView_rotation = findViewById(R.id.videoView_rotation);
        videoView_lock_screen = findViewById(R.id.videoView_lock_screen);
        vidSeek = findViewById(R.id.vidSeek);

        goBack.setOnClickListener(this);
        rewind.setOnClickListener(this);
        forward.setOnClickListener(this);
        playPause.setOnClickListener(this);
        videoView_rotation.setOnClickListener(this);
        videoView_lock_screen.setOnClickListener(this);
        position = getIntent().getIntExtra("p", -1);
        VideoModel videoModel = VideoAdapter.videoFolders.get(position);
        title.setText(videoModel.getTitle());
        String path = videoModel.getPath();
        if (path != null) {
            videoView.setVideoPath(path);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoSeekbar.setMax(videoView.getDuration());
                    vidSeek.setMaxValue(videoView.getDuration());
                    videoView.start();
                }
            });
        } else {
            Toast.makeText(this, "path didn't exist", Toast.LENGTH_SHORT).show();
        }

        zoomLayout = findViewById(R.id.zoom_layout);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        sWidth = size.x;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        device_width = displayMetrics.widthPixels;
        zoomLayout.setOnTouchListener(this);
        scaleDetector = new ScaleGestureDetector(getApplicationContext(), this);
        gestureDetector = new GestureDetectorCompat(getApplicationContext(), new GestureDetector2());

        initalizeSeekBars();
        setHandler();
    }


    private void initalizeSeekBars() {
        videoSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (videoSeekbar.getId() == R.id.videoView_seekbar)
                    if (fromUser) {
                        videoView.seekTo(progress);
                        vidSeek.seekTo(progress);
                        videoView.start();
                        int currentPos = videoView.getCurrentPosition();
                        endTime.setText("" + convertIntoTime(videoView.getDuration() - currentPos));
                    }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String convertIntoTime(int ms) {
        String time;
        int x, seconds, minutes, hours;
        x = ms / 1000;
        seconds = x % 60;
        x /= 60;
        minutes = x % 60;
        x /= 60;
        hours = x % 24;
        if (hours != 0)
            time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        else time = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        return time;
    }

    private void setHandler() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.getDuration() > 0) {
                    int currentPos = videoView.getCurrentPosition();
                    videoSeekbar.setProgress(currentPos);
                    vidSeek.seekTo(currentPos);

                    if (subHandler.strList != null)
                        subHandler.setSubtitleInTV(currentPos);
                    endTime.setText("" + convertIntoTime(videoView.getDuration() - currentPos));
                }
                handler.postDelayed(this, 0);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    private class GestureDetector2 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isEnable) {
                hideDefualtControls();
                isEnable = false;
            } else {
                showDefualtControls();
                isEnable = true;
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (e.getX() < (sWidth / 2)) {
                intleft = true;
                intRight = false;
                subHandler.ablePostion = videoView.getCurrentPosition();
                videoView.seekTo(videoView.getCurrentPosition() - 10000);
                Toast.makeText(VideoPlayerAvtivity.this, "-20sec", Toast.LENGTH_SHORT).show();
            } else if (e.getX() > (sWidth / 2)) {
                intleft = false;
                intRight = true;
                videoView.seekTo(videoView.getCurrentPosition() + 10000);
                Toast.makeText(VideoPlayerAvtivity.this, "+20sec", Toast.LENGTH_SHORT).show();
            }
            return super.onDoubleTap(e);
        }
    }

    void hideDefualtControls() {
        one.setVisibility(View.GONE);
        tow.setVisibility(View.GONE);
        three.setVisibility(View.GONE);
        four.setVisibility(View.GONE);

        final Window window = getWindow();

        if (window == null) {
            return;
        }

        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final View decorView = window.getDecorView();

        if (decorView != null) {
            int uioption = decorView.getSystemUiVisibility();

            if (Build.VERSION.SDK_INT >= 14) {
                uioption |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            } else if (Build.VERSION.SDK_INT >= 16) {
                uioption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            } else if (Build.VERSION.SDK_INT >= 19) {
                uioption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            decorView.setSystemUiVisibility(uioption);
        }
    }

    void showDefualtControls() {
        one.setVisibility(View.VISIBLE);
        tow.setVisibility(View.VISIBLE);
        three.setVisibility(View.VISIBLE);
        four.setVisibility(View.VISIBLE);

        final Window window = getWindow();

        if (window == null) {
            return;
        }

        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        final View decorView = window.getDecorView();

        if (decorView != null) {
            int uioption = decorView.getSystemUiVisibility();

            if (Build.VERSION.SDK_INT >= 14) {
                uioption &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            } else if (Build.VERSION.SDK_INT >= 16) {
                uioption &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            } else if (Build.VERSION.SDK_INT >= 19) {
                uioption &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }

            decorView.setSystemUiVisibility(uioption);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.videoView_go_back:
                onBackPressed();
                break;

            case R.id.videoView_rewind:
                //1000 = 1sec
                subHandler.ablePostion = videoView.getCurrentPosition();
                videoView.seekTo(videoView.getCurrentPosition() - 10000);
                break;

            case R.id.videoView_forward:
                videoView.seekTo(videoView.getCurrentPosition() + 10000);
                break;

            case R.id.videoView_play_pause_btn:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_arrow_24));
                } else {
                    videoView.start();
                    playPause.setImageDrawable(getResources().getDrawable(R.drawable.netflix_pause_button));
                }
                break;

            case R.id.videoView_rotation:
                int orintation = getResources().getConfiguration().orientation;
                if (orintation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (orintation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;

            case R.id.videoView_lock_screen:
                subHandler.ablePostion = 0;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                hideDefualtControls();
                if (scale > MIN_ZOOM) {
                    mode = Mode.DRAG;
                    startx = event.getX() - prevDx;
                    starty = event.getY() - prevDy;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                hideDefualtControls();
                isEnable = false;
                if (mode == Mode.DRAG) {
                    dx = event.getX() - startx;
                    dy = event.getY() - starty;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = Mode.ZOOM;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = Mode.DRAG;
                break;
            case MotionEvent.ACTION_UP:
                mode = Mode.NONE;
                prevDx = dx;
                prevDy = dy;
                break;
        }
        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
            zoomLayout.requestDisallowInterceptTouchEvent(true);
            float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
            float maxDy = (child().getHeight() - (child().getHeight() / scale)) / 2 * scale;
            dx = Math.min(Math.max(dx, -maxDx), maxDx);
            dy = Math.min(Math.max(dy, -maxDy), maxDy);
            applyScaleAndTranslation();
        }
        return true;
    }

    private void applyScaleAndTranslation() {
        child().setScaleX(scale);
        child().setScaleY(scale);
        child().setTranslationX(dx);
        child().setTranslationY(dy);
    }

    private View child() {
        return zoomLayout(0);
    }

    private View zoomLayout(int i) {
        return videoView;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = scaleDetector.getScaleFactor();
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
        } else {
            lastScaleFactor = 0;
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Context context = this;

            String path = RealPathUtils.getRealPath(context, uri);
            File file = new File(path);
            StringBuilder myDataString = new StringBuilder();
            try {
                FileInputStream fis = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String strLine;
                while ((strLine = br.readLine()) != null) {
                    myDataString.append(strLine).append("\n");
                }
                br.close();
                in.close();
                fis.close();
            } catch (IOException e) {
                Toast.makeText(this, "Fiel error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            subHandler.strList = StrParser.parse(myDataString.toString());
        }
    }
}