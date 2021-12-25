package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName
import com.tiktokclone.data.datasource.model.inner.ItemStruct

data class ItemInfo (
	@SerializedName("itemStruct") val itemStruct : ItemStruct?
)
