package com.tiktokclone.data.datasource

import com.tiktokclone.data.datasource.*
import com.tiktokclone.data.datasource.model.response.SetLikeResponse
import com.tiktokclone.data.datasource.model.response.UserInfoResponse
import com.tiktokclone.data.datasource.model.response.VideoDetailsResponse
import com.tiktokclone.data.datasource.model.response.VideosResponse
import com.tiktokclone.data.datasource.model.sound.SoundData
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface SoundApi {
    @GET("tracks")
   suspend fun getSound(): SoundData


}
