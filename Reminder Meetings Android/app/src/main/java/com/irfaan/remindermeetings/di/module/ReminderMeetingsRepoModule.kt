package com.irfaan.remindermeetings.di.module

import com.irfaan.remindermeetings.data.repositories.ReminderMeetingsRepo
import com.irfaan.remindermeetings.data.repositories.ReminderMeetingsRepoImpl
import com.irfaan.remindermeetings.di.qualifier.ReminderMeetingRepoQualifer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ReminderMeetingsRepoModule {

    @Binds
    @ReminderMeetingRepoQualifer
    abstract fun bindsRepoReminderMeetings(reminderMeetingsRepoImpl: ReminderMeetingsRepoImpl): ReminderMeetingsRepo

}