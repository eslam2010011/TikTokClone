package com.tiktokclone.data.repository

import android.content.SharedPreferences
import com.tiktokclone.domain.CookieRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.internal.operators.completable.CompletableFromAction
import javax.inject.Inject

class CookieSharedPrefsRepository @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : CookieRepository {

    override fun getCookie(): Single<String> = Single.fromCallable {
        sharedPrefs.getString(COOKIE_KEY, COOKIE_DEFAULT)
    }

    override fun setCookie(cookie: String): Completable = CompletableFromAction.fromCallable {
        sharedPrefs.edit().putString(COOKIE_KEY, cookie).apply()
    }

    companion object {
        private const val COOKIE_KEY = "COOKIE_KEY"
        private const val COOKIE_DEFAULT = ""
    }
}
