package com.tiktokclone.utils;

/**
 * Created by qboxus on 3/25/2019.
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;



public abstract class CountDownTimerWithPause {

    /**
     * Millis since boot when alarm should stop.
     */
    private long mStopTimeInFuture;

    /**
     * Real time remaining until timer completes
     */
    private long mMillisInFuture;

    /**
     * Total time on timer at start
     */
    private final long mTotalCountdown;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;

    /**
     * The time remaining on the timer when it was paused, if it is currently paused; 0 otherwise.
     */
    private long mPauseTimeRemaining;

    /**
     * True if timer was started running, false if not.
     */
    private boolean mRunAtStart;


    public CountDownTimerWithPause(long millisOnTimer, long countDownInterval, boolean runAtStart) {
        mMillisInFuture = millisOnTimer;
        mTotalCountdown = mMillisInFuture;
        mCountdownInterval = countDownInterval;
        mRunAtStart = runAtStart;
    }

    /**
     * Cancel the countdown and clears all remaining messages
     */
    public final void cancel() {
        mHandler.removeMessages(MSG);
    }

    /**
     * Create the timer object.
     */
    public synchronized final CountDownTimerWithPause create() {
        if (mMillisInFuture <= 0) {
            onFinish();
        } else {
            mPauseTimeRemaining = mMillisInFuture;
        }

        if (mRunAtStart) {
            resume();
        }

        return this;
    }

    /**
     * Pauses the counter.
     */
    public void pause() {
        if (isRunning()) {
            mPauseTimeRemaining = timeLeft();
             cancel();
        }
    }

    /**
     * Resumes the counter.
     */
    public void resume() {
        if (isPaused()) {
            mMillisInFuture = mPauseTimeRemaining;
            mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
            mPauseTimeRemaining = 0;

         }
    }


    public void back_countdown(long backtime) {
        mPauseTimeRemaining = mPauseTimeRemaining + backtime;
    }


    /**
     * Tests whether the timer is paused.
     *
     * @return true if the timer is currently paused, false otherwise.
     */
    public boolean isPaused() {
        return (mPauseTimeRemaining > 0);
    }

    /**
     * Tests whether the timer is running. (Performs logical negation on {@link #isPaused()})
     *
     * @return true if the timer is currently running, false otherwise.
     */
    public boolean isRunning() {
        return (!isPaused());
    }

    /**
     * Returns the number of milliseconds remaining until the timer is finished
     *
     * @return number of milliseconds remaining until the timer is finished
     */
    public long timeLeft() {
        long millisUntilFinished;
        if (isPaused()) {
            millisUntilFinished = mPauseTimeRemaining;
        } else {
            millisUntilFinished = mStopTimeInFuture - SystemClock.elapsedRealtime();
            if (millisUntilFinished < 0) millisUntilFinished = 0;
        }
        return millisUntilFinished;
    }


    /**
     * Callback fired on regular interval
     *
     * @param millisUntilFinished The amount of time until finished
     */
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDownTimerWithPause.this) {
                long millisLeft = timeLeft();

                if (millisLeft <= 0) {
                    cancel();
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long delay = mCountdownInterval - (SystemClock.elapsedRealtime() - lastTickStart);

                    // special case: user's onTick took more than mCountdownInterval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };


}