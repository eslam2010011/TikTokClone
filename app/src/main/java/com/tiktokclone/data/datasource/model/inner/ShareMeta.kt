package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class ShareMeta(
	@SerializedName("title") val title: String?,
	@SerializedName("desc") val desc: String?
)
