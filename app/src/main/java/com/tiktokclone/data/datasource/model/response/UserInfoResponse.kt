package com.tiktokclone.data.datasource.model.response

import com.google.gson.annotations.SerializedName
import com.tiktokclone.data.datasource.model.inner.UserInfo

data class UserInfoResponse(
	@SerializedName("statusCode") val statusCode: String?,
	@SerializedName("userInfo") val userInfo: UserInfo?
)
