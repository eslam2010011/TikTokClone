package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class StickersOnItem (
	@SerializedName("stickerType") val stickerType : String?,
	@SerializedName("stickerText") val stickerText : List<String?>?
)
