package com.irfaan.remindermeetings.di.module

import android.content.Context
import android.content.SharedPreferences
import com.irfaan.remindermeetings.utils.AppConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ReminderMeetingsAppModule {

    @Singleton
    @Provides
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun providesRetrofitInstance(gson: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstant.BASE_URL)
            .addConverterFactory(gson).build()
    }

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context : Context) : SharedPreferences {
        return context.getSharedPreferences(AppConstant.APP_SHARED_PREF, Context.MODE_PRIVATE)
    }
}