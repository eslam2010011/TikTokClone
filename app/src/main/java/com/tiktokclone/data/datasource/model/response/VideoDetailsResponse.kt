package com.tiktokclone.data.datasource.model.response

import com.google.gson.annotations.SerializedName
import com.tiktokclone.data.datasource.model.inner.ItemInfo
import com.tiktokclone.data.datasource.model.inner.ShareMeta

data class VideoDetailsResponse (
	@SerializedName("statusCode") val statusCode : String?,
	@SerializedName("itemInfo") val itemInfo : ItemInfo?,
	@SerializedName("shareMeta") val shareMeta : ShareMeta?
)
