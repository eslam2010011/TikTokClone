package com.tiktokclone.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class Author (
	@SerializedName("id") val id : String?,
	@SerializedName("uniqueId") val uniqueId : String?,
	@SerializedName("nickname") val nickname : String?,
	@SerializedName("avatarThumb") val avatarThumb : String?,
	@SerializedName("avatarMedium") val avatarMedium : String?,
	@SerializedName("avatarLarger") val avatarLarger : String?,
	@SerializedName("signature") val signature : String?,
	@SerializedName("verified") val verified : Boolean?,
	@SerializedName("secUid") val secUid : String?,
	@SerializedName("secret") val secret : Boolean?,
	@SerializedName("ftc") val ftc : Boolean?,
	@SerializedName("relation") val relation : String?,
	@SerializedName("openFavorite") val openFavorite : Boolean?,
	@SerializedName("commentSetting") val commentSetting : String?,
	@SerializedName("duetSetting") val duetSetting : String?,
	@SerializedName("stitchSetting") val stitchSetting : String?,
	@SerializedName("privateAccount") val privateAccount : Boolean?
)
