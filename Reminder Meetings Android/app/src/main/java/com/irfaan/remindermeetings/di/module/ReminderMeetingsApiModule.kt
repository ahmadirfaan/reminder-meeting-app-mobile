package com.irfaan.remindermeetings.di.module

import com.irfaan.remindermeetings.data.api.ReminderMeetingsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReminderMeetingsApiModule {

    @Singleton
    @Provides
    fun provideRemindersMeetingsApi(retrofit : Retrofit) = retrofit.create(ReminderMeetingsApi::class.java)


}