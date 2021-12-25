package com.tiktokclone.data.datasource.model.response

import com.google.gson.annotations.SerializedName
import com.tiktokclone.data.datasource.model.inner.Extra
import com.tiktokclone.data.datasource.model.inner.LogPb

data class SetLikeResponse(
    @SerializedName("is_digg") val isDigg: String?, // 0 if liked, 1 if not liked
    @SerializedName("extra") val extra: Extra?,
    @SerializedName("log_pb") val logPb: LogPb?,
    @SerializedName("status_code") val statusCode: String? // 0 if all is good
)
