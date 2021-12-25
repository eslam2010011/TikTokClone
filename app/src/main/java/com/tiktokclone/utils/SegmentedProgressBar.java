package com.tiktokclone.utils;

/**
 * Created by qboxus on 3/25/2019.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;


import java.util.ArrayList;
import java.util.List;

public class SegmentedProgressBar extends View {

    private static final String TAG = "SegmentedProgressBar";
    private static final int FPS_IN_MILLI = 16;

    private Paint progressPaint = new Paint();
    private Paint dividerPaint = new Paint();
    private int[] gradientColors = new int[3];

    private float lastDividerPosition;
    private float percentCompleted;


    private int progressBarWidth;
    private long maxTimeInMillis;

    private int dividerCount = 0;
    private float dividerWidth = 1;

    private boolean isDividerEnabled;

    private List<Float> dividerPositions;
    private CountDownTimerWithPause countDownTimerWithPause;

    private float cornerRadius;

    ProgressBarListener listener;

    public SegmentedProgressBar(Context context) {
        super(context);
        init();
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(new RectF(0, 0, percentCompleted, getHeight()), cornerRadius, cornerRadius, progressPaint);

        if (dividerCount > 0 && isDividerEnabled) {
            for (int i = 0; i < dividerCount; i++) {
                float leftPosition = dividerPositions.get(i);
                canvas.drawRect(leftPosition, 0, leftPosition + dividerWidth, getHeight(), dividerPaint);
            }
        }
    }

    private void init() {
        dividerPositions = new ArrayList<>();
        cornerRadius = 0;

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    progressBarWidth = getWidth();

                    if (gradientColors.length > 0) {
                        Shader shader = new LinearGradient(0, 0, progressBarWidth, getHeight(), gradientColors, null, Shader.TileMode.MIRROR);
                        progressPaint.setShader(shader);
                    }
                }
            });
        }
    }



    public void SetListener(ProgressBarListener listeter){
        this.listener=listeter;
    }


    public void updateProgress(long millisPassed) {
        listener.timeinMill(millisPassed);
        percentCompleted = progressBarWidth * (float) millisPassed / maxTimeInMillis;
        invalidate();
    }



    public void back_countdown(long timeinmillis){
        countDownTimerWithPause.back_countdown(timeinmillis);
    }

    public void pause() {
        if (countDownTimerWithPause == null) {
            Log.e(TAG, "pause: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
            return;
        }

        countDownTimerWithPause.pause();
    }


    public void resume() {
        if (countDownTimerWithPause == null) {
            Log.e(TAG, "resume: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
            return;
        }

        countDownTimerWithPause.resume();
    }



    public void cancel() {
        if (countDownTimerWithPause == null) {
            Log.e(TAG, "cancel: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
            return;
        }

        countDownTimerWithPause.cancel();
    }


    public void setShader(final int[] colors) {
        this.gradientColors = colors;

        if (progressBarWidth > 0) {
            Shader shader = new LinearGradient(0, 0, progressBarWidth, getHeight(), colors, null, Shader.TileMode.MIRROR);
            progressPaint.setShader(shader);
        }
    }


    public void setDividerColor(int color) {
        dividerPaint.setColor(color);
    }


    public void setDividerWidth(float width) {
        if (width < 0) {
            Log.w(TAG, "setDividerWidth: Divider width can not be negative");
            return;
        }

        dividerWidth = width;
    }


    public void enableAutoProgressView(long timeInMillis) {
        if (timeInMillis < 0) {
            Log.w(TAG, "enableAutoProgressView: Time can not be in negative");
            return;
        }

        this.maxTimeInMillis = timeInMillis;

        countDownTimerWithPause = new CountDownTimerWithPause(maxTimeInMillis, FPS_IN_MILLI, false) {
            @Override
            public void onTick(long millisUntilFinished) {
                long timePassed = SegmentedProgressBar.this.maxTimeInMillis - millisUntilFinished;
                updateProgress(timePassed);
            }

            @Override
            public void onFinish() {
                updateProgress(SegmentedProgressBar.this.maxTimeInMillis);
            }
        }.create();

    }

    public void setDividerEnabled(boolean value) {
        isDividerEnabled = value;
    }



    public void addDivider() {
        if (lastDividerPosition != percentCompleted) {
            lastDividerPosition = percentCompleted;
            dividerCount += 1;
            dividerPositions.add(percentCompleted);
            invalidate();
        } else {
            Log.w(TAG, "addDivider: Divider already added to current position");
        }
    }

    public void removeDivider(){
        dividerCount -= 1;
        dividerPositions.remove(dividerPositions.size()-1);
        lastDividerPosition=percentCompleted;
        invalidate();
    }



}
