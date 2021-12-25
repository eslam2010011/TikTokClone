package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class Items(
	@SerializedName("id") val id: String?,
	@SerializedName("desc") val desc: String?,
	@SerializedName("createTime") val createTime: String?,
	@SerializedName("video") val video: Video?,
	@SerializedName("author") val author: User?,
	@SerializedName("music") val music: Music?,
	@SerializedName("stats") val stats: Stats?,
	@SerializedName("originalItem") val originalItem: Boolean?,
	@SerializedName("officalItem") val officialItem: Boolean?,
	@SerializedName("secret") val secret: Boolean?,
	@SerializedName("forFriend") val forFriend: Boolean?,
	@SerializedName("digged") val liked: Boolean?,
	@SerializedName("itemCommentStatus") val itemCommentStatus: String?,
	@SerializedName("showNotPass") val showNotPass: Boolean?,
	@SerializedName("vl1") val vl1: Boolean?,
	@SerializedName("itemMute") val itemMute: Boolean?,
	@SerializedName("authorStats") val authorStats: Stats?,
	@SerializedName("privateItem") val privateItem: Boolean?,
	@SerializedName("duetEnabled") val duetEnabled: Boolean?,
	@SerializedName("stitchEnabled") val stitchEnabled: Boolean?,
	@SerializedName("shareEnabled") val shareEnabled: Boolean?,
	@SerializedName("isAd") val isAd: Boolean?
)
