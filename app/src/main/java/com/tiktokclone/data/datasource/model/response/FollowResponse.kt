package com.tiktokclone.data.datasource.model.response

import com.google.gson.annotations.SerializedName
import com.tiktokclone.data.datasource.model.inner.Extra
import com.tiktokclone.data.datasource.model.inner.LogPb

data class FollowResponse(
	@SerializedName("status_code") val status_code: String?,
	@SerializedName("status_msg") val status_msg: String?,
	@SerializedName("follow_status") val follow_status: String?,
	@SerializedName("watch_status") val watch_status: String?,
	@SerializedName("extra") val extra: Extra?,
	@SerializedName("log_pb") val log_pb: LogPb?
)
