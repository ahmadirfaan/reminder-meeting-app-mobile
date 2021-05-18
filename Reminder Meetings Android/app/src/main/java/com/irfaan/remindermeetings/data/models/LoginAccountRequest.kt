package com.irfaan.remindermeetings.data.models

import com.google.gson.annotations.SerializedName

data class LoginAccountRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
