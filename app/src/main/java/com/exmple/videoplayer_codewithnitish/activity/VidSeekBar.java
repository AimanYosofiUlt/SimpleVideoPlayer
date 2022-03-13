package com.exmple.videoplayer_codewithnitish.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class VidSeekBar extends View {
    public VidSeekBar(Context context) {
        super(context);
    }

    public VidSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VidSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VidSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawLine(canvas);
    }

    private float position = 0f;
    private int maxValue = 0;
    private float barWidth = 0f;
    private float lastPosition = 0f;

    float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            2f,
            getResources().getDisplayMetrics());

    float inside = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            5f,
            getResources().getDisplayMetrics());

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        barWidth = getWidth() - (strokeWidth * 2 + inside) * 2;
        this.position = strokeWidth * 2 + inside;
        postInvalidate();
    }

    public void seekTo(int value) {
        this.position = (barWidth / (float) maxValue) * value + strokeWidth * 2 + inside;
        invalidate();
    }

    private void drawLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);

        RectF roundBorderRect = new RectF(
                0f + inside,
                0f + inside,
                getWidth() - inside,
                getHeight() - inside
        );
        canvas.drawRoundRect(
                roundBorderRect,
                getHeight(),
                getHeight(),
                paint
        );

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.FILL);

        RectF backgroundRect = new RectF(
                0f + strokeWidth * 2 + inside,
                0f + strokeWidth * 2 + inside,
                getWidth() - strokeWidth * 2 - inside,
                getHeight() - strokeWidth * 2 - inside
        );
        canvas.drawRoundRect(
                backgroundRect,
                getHeight(),
                getHeight(),
                paint
        );

//        if (position < 0f + strokeWidth * 2 + inside)
//            position = 0f + strokeWidth * 2 + inside;
//        else if (position > getWidth() - strokeWidth * 2 - inside)
//            position = getWidth() - strokeWidth * 2 - inside;


        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        RectF valueRect = new RectF(
                0f + strokeWidth * 2 + inside,
                0f + strokeWidth * 2 + inside,
                position,
                getHeight() - strokeWidth * 2 - inside
        );
        canvas.drawRoundRect(
                valueRect,
                getHeight(),
                getHeight(),
                paint
        );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE
        ) {
            position = event.getX();
            postInvalidate();
        } else if (event.getAction() == MotionEvent.ACTION_UP && lastPosition < position) {
            lastPosition = position;
            postInvalidate();

        }
        return super.onTouchEvent(event);
    }
}
