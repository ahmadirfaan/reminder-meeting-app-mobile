package com.irfaan.remindermeetings.data.models

import com.google.gson.annotations.SerializedName

data class AllEmployeeResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<Employee?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

