package com.tiktokclone.data.repository

import android.content.SharedPreferences
import com.tiktokclone.data.datasource.SoundApi
import com.tiktokclone.data.datasource.model.sound.SoundData
import com.tiktokclone.domain.CookieRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.internal.operators.completable.CompletableFromAction
import javax.inject.Inject

class SoundRepository @Inject constructor(
    private val soundApi: SoundApi
)   {


   suspend fun getTracks(): SoundData {
        return soundApi.getSound()

    }



}
