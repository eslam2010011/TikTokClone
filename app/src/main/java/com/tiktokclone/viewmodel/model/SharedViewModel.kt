package com.tiktokclone.viewmodel.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tiktokclone.data.datasource.model.inner.Items
import com.tiktokclone.data.datasource.model.inner.User
import com.tiktokclone.data.datasource.model.response.UserInfoResponse
import com.tiktokclone.data.repository.CookieSharedPrefsRepository
import com.tiktokclone.data.repository.TikTokNetworkRepository
import com.tiktokclone.viewmodel.base.BaseRxViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val cookieRepository: CookieSharedPrefsRepository,
    private val tikTokRepository: TikTokNetworkRepository
) : BaseRxViewModel() {

    val list: MutableLiveData<List<Items>> = MutableLiveData()
    val userInfo: MutableLiveData<UserInfoResponse> = MutableLiveData()




    fun getVideos(): MutableLiveData<List<Items>> {
        return list;
    }

    fun getUser(): MutableLiveData<UserInfoResponse> {
        return userInfo;
    }
    fun getData() {
        val currentUserLogin = "eslammostafa078"
        val targetUserLogin = "markiv_anastasia"
        var currentUserId = ""
        var targetUserId = ""
        var targetUserSecUid = ""
        var targetVideoId = ""
        tikTokRepository.getUserDetails(userId = currentUserLogin)
            .flatMap { user ->
                currentUserId = user.userInfo?.user?.id ?: ""
                Log.d(tag, "currentUserId = $currentUserId")
                tikTokRepository.getTrendingStream(userId = currentUserId)
            }
            .subscribeBy(this::onError) {
                it?.let {
                    list.value = it.items

                }
            }
            .apply(this::addDisposable)

    }

    fun onGetUserDetailsClick() {
        tikTokRepository.getUserDetails(userId = TEST_CURRENT_USER_LOGIN)
            .subscribeBy(this::onError) {
                userInfo.value= it
                onComplete("GetUserDetails completed") }
            .apply(this::addDisposable)
    }

    fun onGetVideoPreviewImageClick() {
        tikTokRepository.getVideoById(videoId = TEST_LINK.split("/").last())
            .map { response ->
                val preview = response.itemInfo?.itemStruct?.video?.cover
                val originPreview = response.itemInfo?.itemStruct?.video?.originCover
                Log.d(tag, "preview = $preview")
                Log.d(tag, "originPreview = $originPreview")
                response
            }
            .subscribeBy(this::onError) { onComplete("GetVideoPreviewImage completed") }
            .apply(this::addDisposable)
    }

    fun onGetVideosClick() {
        tikTokRepository.getUserDetails(userId = TEST_CURRENT_USER_LOGIN)
            .flatMap { user ->
                tikTokRepository.getTrendingStream(userId = user.userInfo?.user?.id ?: "")
            }
            .subscribeBy(this::onError) {
                list.value = it.items
                onComplete("GetVideos completed") }
            .apply(this::addDisposable)
    }

    fun onGetUserVideosClick() {
        tikTokRepository.getUserDetails(userId = TEST_TARGET_USER_LOGIN)
            .flatMap { user ->
                tikTokRepository.getUserVideos(
                    userId = user.userInfo?.user?.id ?: "",
                    userSecUid = user.userInfo?.user?.secUid ?: ""
                )
            }
            .subscribeBy(this::onError) { onComplete("GetUserVideos completed") }
            .apply(this::addDisposable)
    }

    fun onSetLikeClick() {
        tikTokRepository.getUserDetails(userId = TEST_CURRENT_USER_LOGIN)
            .flatMapCompletable { user ->
                tikTokRepository.likeVideo(
                    userId = user.userInfo?.user?.id ?: "",
                    videoId = "6893087314021485825",
                    like = true
                )
            }
            .subscribeBy(this::onError) { onComplete("SetLike completed") }
            .apply(this::addDisposable)
    }

    fun onFollowUserClick(headers: Map<String, String>) {

    }

    companion object {
        private const val TEST_CURRENT_USER_LOGIN = "eslammostafa078"
        private const val TEST_TARGET_USER_LOGIN = "eslammostafa078"

        private const val TEST_LINK =
            "https://www.tiktok.com/foryou?lang=ru#/@markiv_anastasia/video/6902046223763623169"

        private const val CURRENT_USER_KEY = "current"
        private const val TARGET_USER_KEY = "target"
    }
}
