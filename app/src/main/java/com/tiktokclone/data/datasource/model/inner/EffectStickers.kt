package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class EffectStickers (
	@SerializedName("name") val name : String,
	@SerializedName("ID") val iD : String?
)
