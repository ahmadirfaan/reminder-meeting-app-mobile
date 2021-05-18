package com.irfaan.remindermeetings.data.models

import com.google.gson.annotations.SerializedName

data class ResponseAddMeeting(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataMeeting? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)


data class DataMeeting(

	@field:SerializedName("dateMeetings")
	val dateMeetings: String? = null,

	@field:SerializedName("urlDocument")
	val urlDocument: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("detailScheduleList")
	val detailScheduleList: List<DetailScheduleListItem?>? = null
)
