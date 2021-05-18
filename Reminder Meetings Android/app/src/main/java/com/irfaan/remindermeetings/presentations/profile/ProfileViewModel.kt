package com.irfaan.remindermeetings.presentations.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.utils.ResourceState
import com.irfaan.remindermeetings.data.repositories.ReminderMeetingsRepo
import com.irfaan.remindermeetings.di.qualifier.ReminderMeetingRepoQualifer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ReminderMeetingRepoQualifer
    private val reminderMeetingsRepo: ReminderMeetingsRepo
) : ViewModel() {

    private var _setProfile = MutableLiveData<ResourceState>()
    val setProfile : LiveData<ResourceState>
        get() {
            return _setProfile
        }

    fun fillProfileAccount(idEmployee : Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _setProfile.postValue(ResourceState.loading())
                val response = reminderMeetingsRepo.findEmployeeById(idEmployee)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    _setProfile.postValue(ResourceState.success(responseBody?.data))
                } else {
                    _setProfile.postValue(ResourceState.failured(response.message()))
                }
            } catch (e : Exception) {
                e.printStackTrace()
                _setProfile.postValue(ResourceState.failured("Sorry the application is having a trouble"))

            }
        }
    }
}