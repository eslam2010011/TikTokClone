package com.tiktokclone

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.doikki.videoplayer.exo.ExoMediaPlayerFactory
import xyz.doikki.videoplayer.player.VideoViewConfig
import xyz.doikki.videoplayer.player.VideoViewManager
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.DisplayMetrics


@HiltAndroidApp
class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null
        var screenWidth = 0
        var screenHeight = 0
        fun getContext() : App {
            return instance as App
        }

        fun screenWidth() : Int {
            val mDisplayMetrics = getContext().applicationContext.resources
                .displayMetrics
            return mDisplayMetrics.widthPixels
        }

        fun screenHeight() : Int {
            val mDisplayMetrics =getContext().applicationContext.resources
                .displayMetrics
            return  mDisplayMetrics.heightPixels
        }
    }

    override fun onCreate() {
        super.onCreate()
        VideoViewManager.setConfig(
            VideoViewConfig.newBuilder()
                .setPlayerFactory(ExoMediaPlayerFactory.create())
                .build()
        );

        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)



    }







}