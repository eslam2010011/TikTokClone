package com.tiktokclone.data.datasource.model.response

import com.google.gson.annotations.SerializedName
import com.tiktokclone.data.datasource.model.inner.Items

data class VideosResponse(
    @SerializedName("statusCode") val statusCode: String?,
    @SerializedName("items") val items: List<Items>,
    @SerializedName("hasMore") val hasMore: Boolean?,
    @SerializedName("maxCursor") val maxCursor: String?,
    @SerializedName("minCursor") val minCursor: String?
)
