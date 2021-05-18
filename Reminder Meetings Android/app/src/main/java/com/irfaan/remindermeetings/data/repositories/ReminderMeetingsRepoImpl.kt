package com.irfaan.remindermeetings.data.repositories

import com.irfaan.remindermeetings.data.api.ReminderMeetingsApi
import com.irfaan.remindermeetings.data.models.*
import retrofit2.Response
import javax.inject.Inject

class ReminderMeetingsRepoImpl @Inject constructor(private val reminderMeetingsApi: ReminderMeetingsApi) : ReminderMeetingsRepo {
    override suspend fun createAccountEmployee(request: RegisterAccountRequest): Response<AccountResponse> {
        return reminderMeetingsApi.createAccountEmployee(request)
    }

    override suspend fun loginAccountEmployee(request: LoginAccountRequest): Response<AccountResponse> {
        return reminderMeetingsApi.loginAccountEmployee(request)
    }

    override suspend fun findEmployeeById(idEmployee: Int): Response<AccountResponse> {
        return reminderMeetingsApi.findEmployeeById(idEmployee)
    }

    override suspend fun getAllMetingsDone(idEmployee: Int): Response<ResponseAllMeetings> {
        return reminderMeetingsApi.getAllMetingsDone(idEmployee)
    }

    override suspend fun getAllMetingsNotDone(idEmployee: Int): Response<ResponseAllMeetings> {
        return reminderMeetingsApi.getAllMetingsNotDone(idEmployee)
    }

    override suspend fun getAllEmployee(): Response<AllEmployeeResponse> {
        return reminderMeetingsApi.getAllEmployee()
    }

    override suspend fun addSchedulers(request: MeetingRequest): Response<ResponseAddMeeting> {
        return reminderMeetingsApi.addSchedulers(request)
    }
}