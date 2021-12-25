package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class Music(
	@SerializedName("id") val id: String?,
	@SerializedName("title") val title: String?,
	@SerializedName("playUrl") val playUrl: String?,
	@SerializedName("coverThumb") val coverThumb: String?,
	@SerializedName("coverMedium") val coverMedium: String?,
	@SerializedName("coverLarge") val coverLarge: String?,
	@SerializedName("authorName") val authorName: String?,
	@SerializedName("original") val original: Boolean?
)
