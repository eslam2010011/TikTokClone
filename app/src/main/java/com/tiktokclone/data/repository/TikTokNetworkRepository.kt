package com.tiktokclone.data.repository

import android.content.Context
import android.util.Log
import android.webkit.WebView
import com.tiktokclone.data.datasource.*
import com.tiktokclone.data.datasource.model.response.UserInfoResponse
import com.tiktokclone.data.datasource.model.response.VideoDetailsResponse
import com.tiktokclone.domain.CookieRepository
import com.tiktokclone.data.datasource.TikTokApi
import com.tiktokclone.data.datasource.model.response.VideosResponse
import com.tiktokclone.domain.TikTokRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TikTokNetworkRepository @Inject constructor(
    private val tikTokApi: TikTokApi,
    private val cookieRepository: CookieSharedPrefsRepository,
    @ApplicationContext context: Context
) : TikTokRepository {

    private val webView: WebView = WebView(context)

    init {
        webView.settings.apply {
            javaScriptEnabled = true
            userAgentString = HEADER_DEFAULT_USER_AGENT
        }
        webView.loadUrl("")
    }

    override fun likeVideo(
        videoId: String,
        userId: String,
        like: Boolean
    ): Completable = cookieRepository.getCookie()
        .flatMapCompletable { cookie ->
            val query = ENDPOINT_SET_LIKE
            val queryOptions = mutableMapOf(
                PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                PARAM_AID to PARAM_DEFAULT_AID,
                PARAM_VIDEO_ID to videoId,
                PARAM_USER_ID to userId,
                PARAM_DID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
                PARAM_DEVICE_ID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
                PARAM_DEVICE_FINGERPRINT to PARAM_DEFAULT_DEVICE_FINGERPRINT,
                PARAM_COOKIE_ENABLED_UNDERSCORED to PARAM_DEFAULT_COOKIE_STATUS,
                PARAM_TYPE to like.toInt().toString()
            )
            signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                .observeOn(Schedulers.computation())
                .flatMap { urlSigningCode ->
                    tikTokApi.setLike(
                        standardHeaders = TikTokApi.standardHeaders,
                        cookie = cookie,
                        token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                        url = getFormattedUrl(
                            endpoint = query,
                            options = queryOptions.toMutableMap().apply {
                                put(PARAM_SIGNATURE, urlSigningCode)
                            },
                            fullUrl = false
                        )
                    )
                }
                .flatMapCompletable { response ->
                    if (response.isDigg.toBoolean() != like) {
                        Log.d("asdsadsadsa", response.toString())
                        Completable.complete()
                    } else {
                        Completable.error(Exception("Operation not successful"))
                    }
                }.observeOn(AndroidSchedulers.mainThread())
        }

    override fun getUserDetails(userId: String): Single<UserInfoResponse> =
        cookieRepository.getCookie()
            .flatMap { cookie ->
                val query = ENDPOINT_USER_DETAILS
                val queryOptions = mutableMapOf(
                    PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                    PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                    PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                    PARAM_UNIQUE_ID to userId
                )
                signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                    .observeOn(Schedulers.computation())
                    .flatMap { urlSigningCode ->
                        tikTokApi.getUser(
                            standardHeaders = TikTokApi.standardHeaders,
                            cookie = cookie,
                            token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                            url = getFormattedUrl(
                                endpoint = query,
                                options = queryOptions.apply {
                                    put(
                                        PARAM_SIGNATURE,
                                        urlSigningCode
                                    )
                                },
                                fullUrl = false
                            )
                        )
                    }
                    .observeOn(AndroidSchedulers.mainThread())
            }

    override fun getTrendingStream(userId: String): Single<VideosResponse> =
        cookieRepository.getCookie()
            .flatMap { cookie ->
                val query = ENDPOINT_TRENDING_VIDEOS
                val queryOptions = mutableMapOf(
                    PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                    PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                    PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                    PARAM_ID to PARAM_DEFAULT_ID,
                    PARAM_USER_ID to userId,
                    PARAM_DID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
                    PARAM_COOKIE_ENABLED to PARAM_DEFAULT_COOKIE_STATUS,
                    PARAM_DEVICE_FINGERPRINT to PARAM_DEFAULT_DEVICE_FINGERPRINT,
                    PARAM_SEC_UID to PARAM_DEFAULT_SEC_UID,
                    PARAM_SOURCE_TYPE to 12.toString(),
                    PARAM_TYPE to 5.toString(),
                    PARAM_COUNT to 30.toString(),
                    PARAM_MAX_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                    PARAM_MIN_CURSOR to PARAM_DEFAULT_MIN_CURSOR
                )
                signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                    .observeOn(Schedulers.computation())
                    .flatMap { urlSigningCode ->
                        tikTokApi.getVideos(
                            standardHeaders = TikTokApi.standardHeaders,
                            cookie = cookie,
                            token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                            url = getFormattedUrl(
                                endpoint = query,
                                options = queryOptions.apply {
                                    put(
                                        PARAM_SIGNATURE,
                                        urlSigningCode
                                    )
                                },
                                fullUrl = false
                            )
                        )
                    }
                    .observeOn(AndroidSchedulers.mainThread())
            }

    override fun getUserVideos(
        userId: String,
        userSecUid: String
    ): Single<VideosResponse> = cookieRepository.getCookie()
        .flatMap { cookie ->
            val query = ENDPOINT_USER_VIDEOS
            val queryOptions = mutableMapOf(
                PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                PARAM_AID to PARAM_DEFAULT_AID,
                PARAM_ID to userId,
                PARAM_SEC_UID to userSecUid,
                PARAM_COUNT to 30.toString(),
                PARAM_MAX_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                PARAM_MIN_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                PARAM_SOURCE_TYPE to 8.toString(),
                PARAM_TYPE to 1.toString(),
            )
            signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                .observeOn(Schedulers.computation())
                .flatMap { urlSigningCode ->
                    tikTokApi.getVideos(
                        standardHeaders = TikTokApi.standardHeaders,
                        cookie = cookie,
                        token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                        url = getFormattedUrl(
                            endpoint = query,
                            options = queryOptions.apply {
                                put(
                                    PARAM_SIGNATURE,
                                    urlSigningCode
                                )
                            },
                            fullUrl = false
                        )
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
        }

    override fun getVideoById(
        videoId: String
    ): Single<VideoDetailsResponse> = cookieRepository.getCookie()
        .flatMap { cookie ->
            val query = ENDPOINT_VIDEO
            val queryOptions = mutableMapOf(
                PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                PARAM_LANGUAGE to PARAM_DEFAULT_LANGUAGE,
                PARAM_LANG to PARAM_DEFAULT_LANGUAGE,
                PARAM_AID to PARAM_DEFAULT_AID,
                PARAM_ITEM_ID to videoId,
                PARAM_COUNT to 30.toString(),
                PARAM_MAX_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                PARAM_MIN_CURSOR to PARAM_DEFAULT_MIN_CURSOR,
                PARAM_SOURCE_TYPE to 8.toString(),
                PARAM_TYPE to 1.toString(),
            )
            signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                .observeOn(Schedulers.computation())
                .flatMap { urlSigningCode ->
                    tikTokApi.getVideoDetails(
                        standardHeaders = TikTokApi.standardHeaders,
                        cookie = cookie,
                        token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                        url = getFormattedUrl(
                            endpoint = query,
                            options = queryOptions.apply {
                                put(
                                    PARAM_SIGNATURE,
                                    urlSigningCode
                                )
                            },
                            fullUrl = false
                        )
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
        }

    override fun followUser(
        a: String,
        b: String,
        c: String,
        d: String,
        f: String,
        url: String,
        userToFollowId: String,
        follow: Boolean
    ): Completable = cookieRepository.getCookie()
        .flatMapCompletable { cookie ->
            val query = ENDPOINT_FOLLOW_USER
            val queryOptions = mutableMapOf(
                PARAM_AID to PARAM_DEFAULT_AID,
                //    "app_name" to "tiktok_web",
                //     "device_platform" to "web_pc",
                "referer" to "",
                "root_referer" to "https:%2F%2Fwww.google.com%2F",
                "user_agent" to "Mozilla%2F5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit%2F537.36+(KHTML,+like+Gecko)+Chrome%2F86.0.4240.198+Safari%2F537.36",
                PARAM_COOKIE_ENABLED to PARAM_DEFAULT_COOKIE_STATUS,
                //   "screen_width" to 1920.toString(),
                //    "screen_height" to 1080.toString(),
                //    "browser_language" to "ru-RU",
                //    "browser_platform" to "MacIntel",
                //    "browser_name" to "Mozilla",
                //    "browser_version" to "5.0+(Macintosh%3B+Intel+Mac+OS+X+10_15_7)+AppleWebKit%2F537.36+(KHTML,+like+Gecko)+Chrome%2F86.0.4240.198+Safari%2F537.36",
                //     "browser_online" to true.toString(),
                "ac" to "4g",
                //    "timezone_name" to "Europe%2FKiev",
                //   "page_referer" to "https:%2F%2Fwww.tiktok.com%2F",
                //  "priority_region" to "",
                "verifyFp" to "verify_khlv3g19_MFc8LiTO_QXfi_48KB_9g1G_5f8bq9kBPx5m",
                PARAM_APP_ID to PARAM_DEFAULT_APP_ID,
                //  "region" to "UA",
                //   "appType" to "m",
                //  "isAndroid" to false.toString(),
                //  "isMobile" to false.toString(),
                //   "isIOS" to false.toString(),
                //   "OS" to "mac",
                PARAM_DID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
                PARAM_DEVICE_ID to getDataFromCookie(cookie = cookie, key = COOKIE_KEY_DEVICE_ID),
                PARAM_TYPE to 1.toString(),
                PARAM_USER_ID_UNDERSCORED to userToFollowId,
                "from" to 19.toString(),
                "channel_id" to 3.toString(),
                "from_pre" to 0.toString(),
                //   "app_language" to "ru",
                //   "current_region" to "UA",
                //     "fromWeb" to 1.toString(),
            )
            signUrl(url = getFormattedUrl(endpoint = query, options = queryOptions))
                .observeOn(Schedulers.computation())
                .map { urlSigningCode ->
                    getFormattedUrl(
                        endpoint = query,
                        options = queryOptions.apply {
                            put(PARAM_SIGNATURE, urlSigningCode)
                        },
                        fullUrl = false
                    )
                }
                .flatMapCompletable { url ->
                    tikTokApi.followUserPost(
                        followRequestHeaders = TikTokApi.followRequestHeaders,
                        a = a,
                        b = b,
                        c = c,
                        d = d,
                        f = f,
                        cookie = cookie,
                        token = getDataFromCookie(cookie = cookie, key = COOKIE_KEY_CSRF_TOKEN),
                        url = url
                    )
                }.observeOn(AndroidSchedulers.mainThread())
        }

    // gets _signature parameter, that needs almost all requests
    // requires full url of API call, performs calculations via obfuscated JS code
    private fun signUrl(url: String): Single<String> =
        Single.create { emitter ->
            val jsFunction = webView.context.assets.open("acrawler.js")
                .bufferedReader().use { reader -> reader.readText() }
            val getUrlSigningCodeScript = "$jsFunction; byted_acrawler.sign({ url: \"$url\" })"
            webView.evaluateJavascript(getUrlSigningCodeScript) { result ->
                emitter.onSuccess(result.replace("\"", ""))
            }
        }

    private fun getFormattedUrl(
        endpoint: String,
        options: Map<String, String>,
        fullUrl: Boolean = true
    ): String {
        val url = StringBuilder()
        if (fullUrl) {
            url.append(BASE_URL)
        }
        url.append(endpoint)
        url.append(getOptionsUrl(options))
        return url.toString()
    }

    private fun getOptionsUrl(options: Map<String, String>): String {
        val optionsUrl = StringBuilder()
        if (options.isNotEmpty()) {
            optionsUrl.append("?")
        }
        options.keys.forEach { key ->
            if (optionsUrl.isNotEmpty() && optionsUrl.last() != '?') {
                optionsUrl.append("&")
            }
            optionsUrl.append("$key=${options[key]}")
        }
        return optionsUrl.toString()
    }

    private fun getDataFromCookie(cookie: String, key: String): String = cookie
        .substringAfterLast(key)
        .split("; ")[0]
        .replace("=", "")

    private fun Boolean.toInt(): Int = if (this) 1 else 0

    private fun Int.toBoolean(): Boolean = this != 0
}
