package com.tiktokclone.domain

import androidx.annotation.CheckResult
import com.tiktokclone.data.datasource.model.response.UserInfoResponse
import com.tiktokclone.data.datasource.model.response.VideoDetailsResponse
import com.tiktokclone.data.datasource.model.response.VideosResponse
import io.reactivex.Completable
import io.reactivex.Single

interface TikTokRepository {

    @CheckResult
    fun getUserDetails(userId: String): Single<UserInfoResponse>

    @CheckResult
    fun getTrendingStream(userId: String): Single<VideosResponse>

    @CheckResult
    fun getUserVideos(userId: String, userSecUid: String): Single<VideosResponse>

    @CheckResult
    fun getVideoById(videoId: String): Single<VideoDetailsResponse>

    @CheckResult
    fun likeVideo(videoId: String, userId: String, like: Boolean): Completable

    @CheckResult
    fun followUser(
        a: String,
        b: String,
        c: String,
        d: String,
        f: String,
        url: String,
        userToFollowId: String,
        follow: Boolean
    ): Completable

    //@CheckResult
    //fun checkUserFollowStatus(userId: String, userToFollowId: String, follow: Boolean): Completable
}
