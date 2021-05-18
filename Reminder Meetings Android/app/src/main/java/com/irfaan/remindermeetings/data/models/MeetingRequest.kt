package com.irfaan.remindermeetings.data.models

data class MeetingRequest(
	val employeesList: List<EmployeesListItem?>? = null,
	val dateMeetings: String? = null,
	val urlDocument: String? = null,
	val title: String? = null
)

data class EmployeesListItem(
	val id: Int? = null
)

