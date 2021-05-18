package com.irfaan.remindermeetings.presentations.addmeeting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.utils.ResourceState
import com.irfaan.remindermeetings.data.models.MeetingRequest
import com.irfaan.remindermeetings.data.repositories.ReminderMeetingsRepo
import com.irfaan.remindermeetings.di.qualifier.ReminderMeetingRepoQualifer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMeetingViewModel
@Inject
constructor(
    @ReminderMeetingRepoQualifer
    private val reminderMeetingsRepo: ReminderMeetingsRepo
) : ViewModel() {

    private var _addMeetingLiveData = MutableLiveData<ResourceState>()
    val addMeetingLiveData: LiveData<ResourceState>
        get() {
            return _addMeetingLiveData
        }

    private var _inputValidation = MutableLiveData<ResourceState>()
    val inputValidation: LiveData<ResourceState>
        get() {
            return _inputValidation
        }

    fun checkInputValidation(dateMeeting: String, timeMeeting: String, titleMeeting : String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _inputValidation.postValue(ResourceState.loading())
                if (dateMeeting.isBlank()) {
                    _inputValidation.postValue(ResourceState.failured("Your Date Meeting is Blank"))
                } else if (timeMeeting.isBlank()) {
                    _inputValidation.postValue(ResourceState.failured("Your Time Meeting is Blank"))
                } else if (titleMeeting.isBlank()) {
                    _inputValidation.postValue(ResourceState.failured("Your Title Meeting is Blank"))
                } else {
                    _inputValidation.postValue(ResourceState.success(true))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _inputValidation.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }
        }
    }

    fun addMeetingRequest(request: MeetingRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _addMeetingLiveData.postValue(ResourceState.loading())
                Log.d("INI LOGIN 1", "LOGIN 1")
                val response = reminderMeetingsRepo.addSchedulers(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val dataMeeting = responseBody?.data
                    if (responseBody?.data == null || responseBody.code != 200) {
                        _addMeetingLiveData.postValue(ResourceState.failured(responseBody?.message))
                    } else {
                        _addMeetingLiveData.postValue(ResourceState.success(dataMeeting))
                    }
                } else {
                    _addMeetingLiveData.postValue(ResourceState.failured(response.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _addMeetingLiveData.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }
        }
    }
}
