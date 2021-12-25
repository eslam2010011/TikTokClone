package com.tiktokclone.record;

import static com.tiktokclone.record.utils.StaticFinalValues.CHANGE_IMAGE;
import static com.tiktokclone.record.utils.StaticFinalValues.DELAY_DETAL;
import static com.tiktokclone.record.utils.StaticFinalValues.OVER_CLICK;
import static com.tiktokclone.record.utils.StaticFinalValues.RECORD_MIN_TIME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.tiktokclone.App;
import com.tiktokclone.R;
import com.tiktokclone.record.beans.MediaObject;
import com.tiktokclone.record.other.MagicFilterType;
import com.tiktokclone.record.ui.CameraView;
import com.tiktokclone.record.ui.CustomRecordImageView;
import com.tiktokclone.record.ui.FocusImageView;
import com.tiktokclone.record.ui.ProgressView;
import com.tiktokclone.record.ui.SlideGpuFilterGroup;
import com.tiktokclone.record.utils.FileUtils;
import com.tiktokclone.record.utils.StaticFinalValues;
import com.tiktokclone.ui.Sound.SectionSoundListA;
 import com.tiktokclone.utils.Variables;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RecorderActivity extends BaseActivity implements View.OnTouchListener, SlideGpuFilterGroup.OnFilterChangeListener, View.OnClickListener {
    private static final int VIDEO_MAX_TIME = 30 * 1000;
    CameraView mRecordCameraView;
    ProgressView mVideoRecordProgressView;
    LinearLayout mMatchingBack;
    ImageButton mVideoRecordFinishIv;
    ImageView mMeetCamera;
    LinearLayout mIndexDelete;
    CustomRecordImageView mCustomRecordImageView;
    FrameLayout mRecordBtnLl;
    FocusImageView mRecorderFocusIv;
    ImageView mCountTimeDownIv;
    public int mNum = 0;
    private long mLastTime = 0;
    public float mRecordTimeInterval;
    ExecutorService executorService;
    private MediaObject mMediaObject;
    TextView add_sound_txt;
    private MyHandler mMyHandler = new MyHandler(this);
    private boolean isRecording = false;
    MediaPlayer audio;
    String isSelected;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_recorder;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoRecordProgressView.setData(mMediaObject);
    }

    @Override
    public void initView() {


        mRecorderFocusIv = findViewById(R.id.recorder_focus_iv);
        mCountTimeDownIv = findViewById(R.id.count_time_down_iv);

        add_sound_txt = findViewById(R.id.add_sound_txt);

        add_sound_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultCallback.launch(new Intent(RecorderActivity.this, SectionSoundListA.class));
            }
        });


        Intent intent = getIntent();
        if (intent.hasExtra("sound_name")) {
            add_sound_txt.setText(intent.getStringExtra("sound_name"));
            Variables.selectedSoundId = intent.getStringExtra("sound_id");
            isSelected = intent.getStringExtra("isSelected");
            preparedAudio();
        }
        mRecordCameraView = findViewById(R.id.record_camera_view);
        mVideoRecordProgressView = findViewById(R.id.video_record_progress_view);
        mMatchingBack = findViewById(R.id.matching_back);
        mMatchingBack.setOnClickListener(this);
        mVideoRecordFinishIv = findViewById(R.id.video_record_finish_iv);
        mVideoRecordFinishIv.setOnClickListener(this);

        mMeetCamera = findViewById(R.id.switch_camera);
        mMeetCamera.setOnClickListener(this);

        mIndexDelete = findViewById(R.id.index_delete);
        mIndexDelete.setOnClickListener(this);


        mCustomRecordImageView = findViewById(R.id.custom_record_image_view);
        mCustomRecordImageView.setOnClickListener(this);


        mRecordBtnLl = findViewById(R.id.record_btn_ll);

        if (mMediaObject == null) {
            mMediaObject = new MediaObject();
        }
        executorService = Executors.newSingleThreadExecutor();
        mRecordCameraView.setOnTouchListener(this);
        mRecordCameraView.setOnFilterChangeListener(this);
        mVideoRecordProgressView.setMaxDuration(VIDEO_MAX_TIME, false);
        mVideoRecordProgressView.setOverTimeClickListener(new ProgressView.OverTimeClickListener() {
            @Override
            public void overTime() {
                mCustomRecordImageView.performClick();
            }

            @Override
            public void noEnoughTime() {
                setBackAlpha(mVideoRecordFinishIv, 255);
            }

            @Override
            public void isArriveCountDown() {
                mCustomRecordImageView.performClick();
            }
        });
        setBackAlpha(mVideoRecordFinishIv, 127);
    }

    public static String getAppFolder(Context activity) {
        return activity.getExternalFilesDir(null).getPath() + "/";
    }

    @Override
    public void onClick(View view) {
        if (System.currentTimeMillis() - mLastTime < 500) {
            return;
        }
        mLastTime = System.currentTimeMillis();
        if (view.getId() != R.id.index_delete) {
            if (mMediaObject != null) {
                MediaObject.MediaPart part = mMediaObject.getCurrentPart();
                if (part != null) {
                    if (part.remove) {
                        part.remove = false;
                        if (mVideoRecordProgressView != null)
                            mVideoRecordProgressView.invalidate();
                    }
                }
            }
        }
        switch (view.getId()) {
            case R.id.matching_back:
                onBackPressed();
                break;
            case R.id.video_record_finish_iv:
                onStopRecording();
                if (mMediaObject != null) {
                    videoFileName = mMediaObject.mergeVideo();
                }
                break;
            case R.id.switch_camera:
                mRecordCameraView.switchCamera();
                if (mRecordCameraView.getCameraId() == 1) {
                    mRecordCameraView.changeBeautyLevel(3);
                } else {
                    mRecordCameraView.changeBeautyLevel(0);
                }
                break;
            case R.id.index_delete:
                MediaObject.MediaPart part = mMediaObject.getCurrentPart();
                if (part != null) {
                    if (part.remove) {
                        part.remove = false;
                        mMediaObject.removePart(part, true);
                        if (mMediaObject.getMedaParts().size() == 0) {
                            mIndexDelete.setVisibility(View.GONE);
                        }
                        if (mMediaObject.getDuration() < RECORD_MIN_TIME) {
                            setBackAlpha(mVideoRecordFinishIv, 127);
                            mVideoRecordProgressView.setShowEnouchTime(false);
                        }
                    } else {
                        part.remove = true;
                    }
                }
                break;

            case R.id.custom_record_image_view:
                if (!isRecording) {
                    onStartRecording();
                } else {
                    onStopRecording();
                }
                break;
        }
    }


    private void onStartRecording() {
        isRecording = true;
        String storageMp4 = FileUtils.getStorageMp4(String.valueOf(System.currentTimeMillis()));
        MediaObject.MediaPart mediaPart = mMediaObject.buildMediaPart(storageMp4);
        mRecordCameraView.setSavePath(storageMp4);
        mRecordCameraView.startRecord();
        mCustomRecordImageView.startRecord();
        mVideoRecordProgressView.start();
        alterStatus();
        if (audio != null) {
            audio.start();
        }
    }

    private void onStopRecording() {
        isRecording = false;
        mRecordCameraView.stopRecord();
        mVideoRecordProgressView.stop();
        //todo:录制释放有延时，稍后处理
        mMyHandler.sendEmptyMessageDelayed(DELAY_DETAL, 250);
        mCustomRecordImageView.stopRecord();
        alterStatus();

        if (audio != null) {
            if (audio.isPlaying()) {
                audio.pause();
            }
        }
    }

    private void setBackAlpha(ImageButton view, int alpha) {
        if (alpha > 127) {
            view.setClickable(true);
        } else {
            view.setClickable(false);
        }
        view.getBackground().setAlpha(alpha);
    }

    private void showOtherView() {
        if (mMediaObject != null && mMediaObject.getMedaParts().size() == 0) {
            mIndexDelete.setVisibility(View.GONE);
        } else {
            mIndexDelete.setVisibility(View.VISIBLE);
        }
        mMatchingBack.setVisibility(View.VISIBLE);
        mCustomRecordImageView.setVisibility(View.VISIBLE);
    }

    private void hideOtherView() {
        mIndexDelete.setVisibility(View.INVISIBLE);
        mMatchingBack.setVisibility(View.INVISIBLE);
        mCustomRecordImageView.setVisibility(View.INVISIBLE);
    }

    //正在录制中
    public void alterStatus() {
        if (isRecording) {
            mIndexDelete.setVisibility(View.INVISIBLE);

            mMatchingBack.setVisibility(View.INVISIBLE);
        } else {
            if (mMediaObject != null && mMediaObject.getMedaParts().size() == 0) {
                mIndexDelete.setVisibility(View.GONE);
            } else {
                mIndexDelete.setVisibility(View.VISIBLE);
            }

            mMatchingBack.setVisibility(View.VISIBLE);
            mMeetCamera.setVisibility(View.VISIBLE);
            mVideoRecordFinishIv.setVisibility(View.VISIBLE);
            mVideoRecordProgressView.setVisibility(View.VISIBLE);
        }
    }

    private void hideAllView() {
        hideOtherView();
        mVideoRecordFinishIv.setVisibility(View.GONE);
        mVideoRecordProgressView.setVisibility(View.GONE);
        mMeetCamera.setVisibility(View.GONE);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mRecordCameraView.onTouch(event);
        if (mRecordCameraView.getCameraId() == 1) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float sRawX = event.getRawX();
                float sRawY = event.getRawY();
                float rawY = sRawY * App.Companion.getScreenWidth() / App.Companion.getScreenHeight();
                float temp = sRawX;
                float rawX = rawY;
                rawY = (App.Companion.getScreenWidth() - temp) * App.Companion.getScreenHeight() / App.Companion.getScreenWidth();

                Point point = new Point((int) rawX, (int) rawY);
                mRecordCameraView.onFocus(point, new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            mRecorderFocusIv.onFocusSuccess();
                        } else {
                            mRecorderFocusIv.onFocusFailed();
                        }
                    }
                });
                mRecorderFocusIv.startFocus(new Point((int) sRawX, (int) sRawY));
        }
        return true;
    }

    @Override
    public void onFilterChange(final MagicFilterType type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (type == MagicFilterType.NONE) {
                    Toast.makeText(RecorderActivity.this, "当前没有设置滤镜--" + type, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecorderActivity.this, "当前滤镜切换为--" + type, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private static class MyHandler extends Handler {

        private WeakReference<RecorderActivity> mVideoRecordActivity;

        public MyHandler(RecorderActivity videoRecordActivity) {
            mVideoRecordActivity = new WeakReference<>(videoRecordActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            RecorderActivity activity = mVideoRecordActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case DELAY_DETAL:
                        activity.mMediaObject.stopRecord(activity, activity.mMediaObject);
                        break;
                    case CHANGE_IMAGE:
                        switch (activity.mNum) {
                            case 0:
                                activity.mCountTimeDownIv.setVisibility(View.VISIBLE);
                                activity.mCountTimeDownIv.setImageResource(R.drawable.bigicon_3);
                                activity.mMyHandler.sendEmptyMessageDelayed(CHANGE_IMAGE, 1000);
                                break;
                            case 1:
                                activity.mCountTimeDownIv.setImageResource(R.drawable.bigicon_2);
                                activity.mMyHandler.sendEmptyMessageDelayed(CHANGE_IMAGE, 1000);
                                break;
                            case 2:
                                activity.mCountTimeDownIv.setImageResource(R.drawable.bigicon_1);
                                activity.mMyHandler.sendEmptyMessageDelayed(CHANGE_IMAGE, 1000);
                                break;
                            default:
                                activity.mMyHandler.removeCallbacks(null);
                                activity.mCountTimeDownIv.setVisibility(View.GONE);
                                activity.mVideoRecordProgressView.setVisibility(View.VISIBLE);
                                activity.mCustomRecordImageView.setVisibility(View.VISIBLE);
                                activity.mCustomRecordImageView.performClick();
                                activity.mVideoRecordProgressView.setCountDownTime(activity.mRecordTimeInterval);
                                break;
                        }
                        if (activity.mNum >= 3) {
                            activity.mNum = 0;
                        } else {
                            activity.mNum++;
                        }
                        break;
                    case OVER_CLICK:
                        activity.mCustomRecordImageView.performClick();
                        break;
                }
            }
        }
    }

    private static final String TAG = "RecorderActivity";

    String videoFileName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StaticFinalValues.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    ArrayList<VideoFile> list = data.getParcelableArrayListExtra(StaticFinalValues.RESULT_PICK_VIDEO);
                    for (VideoFile file : list) {
                        videoFileName = file.getPath();
                    }


                    //这一段用来判断视频时间的
                    try {
                        MediaPlayer player = new MediaPlayer();
                        player.setDataSource(videoFileName);
                        player.prepare();
                        int duration = player.getDuration();
                        player.release();
                        int s = duration / 1000;
                        int hour = s / 3600;
                        int minute = s % 3600 / 60;
                        int second = s % 60;
                        Log.e(TAG, "视频文件长度,分钟: " + minute + "视频有" + s + "秒");
                        if (s >= 120) {
                            Toast.makeText(this, "视频剪辑不能超过2分钟", Toast.LENGTH_LONG).show();
                            return;
                        } else if (s < 5) {
                            Toast.makeText(this, "视频剪辑不能少于5秒", Toast.LENGTH_LONG).show();
                            return;
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                break;
        }
    }

    ActivityResultLauncher<Intent> resultCallback = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            isSelected = data.getStringExtra("isSelected");
                            if (isSelected.equals("yes")) {
                                add_sound_txt.setText(data.getStringExtra("sound_name"));
                                Variables.selectedSoundId = data.getStringExtra("sound_id");
                                preparedAudio();
                            }

                        }
                    }
                }
            });


    public void preparedAudio() {

        File file = new File(getAppFolder(this) + Variables.APP_HIDED_FOLDER + Variables.SelectedAudio_AAC);
        if (file.exists()) {
            try {
                audio = new MediaPlayer();
                try {
                    audio.setDataSource(getAppFolder(this) + Variables.APP_HIDED_FOLDER + Variables.SelectedAudio_AAC);
                    audio.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(this, Uri.fromFile(file));
                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                final int file_duration = parseInterger(durationStr);
//
//                if (file_duration < Constants.MAX_RECORDING_DURATION) {
//                    Constants.RECORDING_DURATION = file_duration;
//                  //  initlizeVideoProgress();
//                }
            } catch (Exception e) {
                 finish();
            }

        }

    }
}
