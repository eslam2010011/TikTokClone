package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class Stats(
	@SerializedName("followingCount") val followingCount: String?,
	@SerializedName("followerCount") val followerCount: String?,
	@SerializedName("heartCount") val heartCount: String?,
	@SerializedName("videoCount") val videoCount: String?,
	@SerializedName("diggCount") val likesCount: String?,
	@SerializedName("heart") val heart: String?
)
