package com.irfaan.remindermeetings.data.models

import com.google.gson.annotations.SerializedName

data class ResponseAllMeetings(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<ScheduleMeetings?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

data class ScheduleMeetings(

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

data class DetailScheduleListItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null
)

data class Employee(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	var isChecked: Boolean = false

)
