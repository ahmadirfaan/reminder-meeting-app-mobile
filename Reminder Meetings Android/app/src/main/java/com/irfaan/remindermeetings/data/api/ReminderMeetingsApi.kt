package com.irfaan.remindermeetings.data.api

import com.irfaan.remindermeetings.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ReminderMeetingsApi {

    @POST("/employees")
    suspend fun createAccountEmployee(@Body request : RegisterAccountRequest) : Response<AccountResponse>

    @POST("/employees/login")
    suspend fun loginAccountEmployee(@Body request: LoginAccountRequest) : Response<AccountResponse>

    @GET("/employees/{idEmployee}")
    suspend fun findEmployeeById(@Path("idEmployee") idEmployee : Int) : Response<AccountResponse>

    @GET("/schedulers/done/{idEmployee}")
    suspend fun getAllMetingsDone(@Path("idEmployee") idEmployee: Int) : Response<ResponseAllMeetings>

    @GET("/schedulers/notdone/{idEmployee}")
    suspend fun getAllMetingsNotDone(@Path("idEmployee") idEmployee: Int) : Response<ResponseAllMeetings>

    @GET("/employees")
    suspend fun getAllEmployee() : Response<AllEmployeeResponse>

    @POST("/schedulers")
    suspend fun addSchedulers(@Body request: MeetingRequest) : Response<ResponseAddMeeting>

}