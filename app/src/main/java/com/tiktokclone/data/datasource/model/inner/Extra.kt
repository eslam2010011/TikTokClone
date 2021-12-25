package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class Extra(
    @SerializedName("now") val now: String?,
    @SerializedName("fatal_item_ids") val fatalItemsIds: List<String?>?,
    @SerializedName("logid") val logId: String?
)
