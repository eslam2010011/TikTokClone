package com.tiktokclone.domain

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Single

interface CookieRepository {

    @CheckResult
    fun getCookie(): Single<String>

    @CheckResult
    fun setCookie(cookie: String): Completable
}
