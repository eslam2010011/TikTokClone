package com.tiktokclone.data.datasource

// Base
const val BASE_URL = "https://m.tiktok.com/"
const val MOBILE_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 2.2) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1"
    //"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"


// Endpoints
const val ENDPOINT_USER_DETAILS = "api/user/detail/"
const val ENDPOINT_USER_VIDEOS = "api/item_list/"
const val ENDPOINT_VIDEO = "api/item/detail/"
const val ENDPOINT_TRENDING_VIDEOS = "api/item_list/"
const val ENDPOINT_SET_LIKE = "api/commit/item/digg/"
const val ENDPOINT_FOLLOW_USER = "api/commit/follow/user/"

// Headers
const val HEADER_USER_AGENT = "User-Agent"
const val HEADER_ACCEPT = "Accept"
const val HEADER_CONTENT_TYPE = "Content-Type"
const val HEADER_DNT = "DNT"
const val HEADER_ORIGIN = "Origin"
const val HEADER_REFERER = "Referer"
const val HEADER_COOKIE = "Cookie"
const val HEADER_CSRF_TOKEN = "tt-csrf-token"

// Headers default values
const val HEADER_DEFAULT_USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"
const val HEADER_DEFAULT_ACCEPT = "application/json, text/plain, */*"
const val HEADER_DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded"
const val HEADER_DEFAULT_DNT = "1"
const val HEADER_DEFAULT_ORIGIN = "https://www.tiktok.com"
const val HEADER_DEFAULT_REFERER = "https://www.tiktok.com/"

// Parameters
const val PARAM_UNIQUE_ID = "uniqueId"
const val PARAM_ID = "id"
const val PARAM_ITEM_ID = "itemId"
const val PARAM_APP_ID = "appId"
const val PARAM_LANGUAGE = "language"
const val PARAM_LANG = "lang"
const val PARAM_AID = "aid"
const val PARAM_VIDEO_ID = "aweme_id"
const val PARAM_USER_ID = "uid"
const val PARAM_USER_ID_UNDERSCORED = "user_id"
const val PARAM_DID = "did"
const val PARAM_DEVICE_ID = "device_id"
const val PARAM_SEC_UID = "secUid"
const val PARAM_SOURCE_TYPE = "sourceType"
const val PARAM_DEVICE_FINGERPRINT = "verifyFp"
const val PARAM_COOKIE_ENABLED = "cookieEnabled"
const val PARAM_COOKIE_ENABLED_UNDERSCORED = "cookie_enabled"
const val PARAM_TYPE = "type"
const val PARAM_SIGNATURE = "_signature"
const val PARAM_COUNT = "count"
const val PARAM_MAX_CURSOR = "maxCursor"
const val PARAM_MIN_CURSOR = "minCursor"

// Parameters default values
const val PARAM_DEFAULT_ID = 1.toString()
const val PARAM_DEFAULT_APP_ID = 1233.toString()
const val PARAM_DEFAULT_LANGUAGE = "en"
const val PARAM_DEFAULT_AID = 1988.toString()
const val PARAM_DEFAULT_DEVICE_FINGERPRINT = ""
const val PARAM_DEFAULT_SEC_UID = ""
const val PARAM_DEFAULT_COOKIE_STATUS = true.toString()
const val PARAM_DEFAULT_MIN_CURSOR = 0.toString()

// Cookie entries
const val COOKIE_KEY_CSRF_TOKEN = "tt_csrf_token"
const val COOKIE_KEY_DEVICE_ID = "tt_webid"
