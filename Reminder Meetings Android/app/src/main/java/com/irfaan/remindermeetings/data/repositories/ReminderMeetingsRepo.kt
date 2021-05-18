package com.irfaan.remindermeetings.data.repositories

import com.irfaan.remindermeetings.data.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReminderMeetingsRepo {

    suspend fun createAccountEmployee(request : RegisterAccountRequest) : Response<AccountResponse>
    suspend fun loginAccountEmployee(request: LoginAccountRequest) : Response<AccountResponse>
    suspend fun findEmployeeById(idEmployee : Int) : Response<AccountResponse>
    suspend fun getAllMetingsDone(idEmployee: Int) : Response<ResponseAllMeetings>
    suspend fun getAllMetingsNotDone(idEmployee: Int) : Response<ResponseAllMeetings>
    suspend fun getAllEmployee() : Response<AllEmployeeResponse>
    suspend fun addSchedulers(request: MeetingRequest) : Response<ResponseAddMeeting>

}