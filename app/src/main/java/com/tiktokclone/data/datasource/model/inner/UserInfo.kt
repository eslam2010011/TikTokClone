package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class UserInfo(
	@SerializedName("user") val user: User?,
	@SerializedName("stats") val stats: Stats?,
	@SerializedName("shareMeta") val shareMeta: ShareMeta?
)
