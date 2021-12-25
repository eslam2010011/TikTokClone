package com.tiktokclone.record;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;






public abstract class BaseActivity extends AppCompatActivity {
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
         startRequestPermission();
        initView();
    }

    protected void startRequestPermission() {
        ActivityCompat.requestPermissions(this,BASIC_PERMISSIONS,123);
    }
    public void initView(){

    }
    protected abstract int setLayoutId();
}