package com.tiktokclone.data.datasource

import com.tiktokclone.data.datasource.*
import com.tiktokclone.data.datasource.model.response.SetLikeResponse
import com.tiktokclone.data.datasource.model.response.UserInfoResponse
import com.tiktokclone.data.datasource.model.response.VideoDetailsResponse
import com.tiktokclone.data.datasource.model.response.VideosResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface TikTokApi {

    @GET
    fun getQueryCompletable(
        @HeaderMap headers: Map<String, String>,
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Completable

    @POST
    fun postQueryCompletable(
        @HeaderMap headers: Map<String, String>,
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Completable

    @GET
    fun getUser(
        @HeaderMap standardHeaders: Map<String, String>,
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<UserInfoResponse>

    @POST
    fun setLike(
        @HeaderMap standardHeaders: Map<String, String>,
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<SetLikeResponse>

    @GET
    fun getVideos(
        @HeaderMap standardHeaders: Map<String, String>,
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<VideosResponse>

    @GET
    fun getVideoDetails(
        @HeaderMap standardHeaders: Map<String, String>,
        @Header(HEADER_COOKIE) cookie: String,
        @Header(HEADER_CSRF_TOKEN) token: String,
        @Url url: String
    ): Single<VideoDetailsResponse>

    @POST
    fun followUserPost(
        @HeaderMap followRequestHeaders: Map<String, String>,
        @Header("htc6j8njvn-a") a: String,
        @Header("htc6j8njvn-b") b: String,
        @Header("htc6j8njvn-c") c: String,
        @Header("htc6j8njvn-d") d: String,
        @Header("htc6j8njvn-f") f: String,
        @Header("tt-csrf-token") token: String,
        @Header("cookie") cookie: String,
        @Url url: String
    ): Completable

    companion object {
        val standardHeaders = mapOf(
            HEADER_USER_AGENT to HEADER_DEFAULT_USER_AGENT,
            HEADER_ACCEPT to HEADER_DEFAULT_ACCEPT,
            HEADER_CONTENT_TYPE to HEADER_DEFAULT_CONTENT_TYPE,
            HEADER_DNT to HEADER_DEFAULT_DNT,
            HEADER_ORIGIN to HEADER_DEFAULT_ORIGIN,
            HEADER_REFERER to HEADER_DEFAULT_REFERER
        )
        val followRequestHeaders = mapOf(
            "authority" to "m.tiktok.com",
            "content-length" to "0",
            "user-agent" to "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36",
            "htc6j8njvn-z" to "q",
            "content-type" to "application/x-www-form-urlencoded",
            "accept" to "application/json, text/plain, */*",
            "origin" to "https://www.tiktok.com",
            "sec-fetch-site" to "same-site",
            "sec-fetch-mode" to "cors",
            "sec-fetch-dest" to "empty",
            "referer" to "https://www.tiktok.com/",
            "accept-language" to "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7"
        )
    }
}

//      This headers are dynamic
// htc6j8njvn-a
// htc6j8njvn-b
// htc6j8njvn-c
// htc6j8njvn-d
// htc6j8njvn-f
