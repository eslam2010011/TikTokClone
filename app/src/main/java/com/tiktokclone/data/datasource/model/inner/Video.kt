package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class Video(
	@SerializedName("id") val id: String?,
	@SerializedName("height") val height: String?,
	@SerializedName("width") val width: String?,
	@SerializedName("duration") val duration: String?,
	@SerializedName("ratio") val ratio: String?,
	@SerializedName("cover") val cover: String?,
	@SerializedName("originCover") val originCover: String?,
	@SerializedName("dynamicCover") val dynamicCover: String?,
	@SerializedName("playAddr") val playAddr: String?,
	@SerializedName("downloadAddr") val downloadAddr: String?,
	@SerializedName("shareCover") val shareCover: List<String?>?,
	@SerializedName("reflowCover") val reflowCover: String?
)
