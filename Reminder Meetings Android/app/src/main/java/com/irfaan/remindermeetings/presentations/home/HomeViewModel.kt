package com.irfaan.remindermeetings.presentations.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.utils.ResourceState
import com.irfaan.remindermeetings.data.models.ScheduleMeetings
import com.irfaan.remindermeetings.data.repositories.ReminderMeetingsRepo
import com.irfaan.remindermeetings.di.qualifier.ReminderMeetingRepoQualifer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ReminderMeetingRepoQualifer
    private val reminderMeetingsRepo: ReminderMeetingsRepo
) : ViewModel(), HomeClickListener {

    private var _meetingListLiveData = MutableLiveData<ResourceState>()
    val meetingListLiveData: LiveData<ResourceState>
        get() {
            return _meetingListLiveData
        }

    private var _onDetailMeetingLiveData = MutableLiveData<ResourceState>()
    val onDetailMeetingLiveData: LiveData<ResourceState>
        get() {
            return _onDetailMeetingLiveData
        }


    fun setRvMeetingDone(idEmployee : Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _meetingListLiveData.postValue(ResourceState.loading())
                val response = reminderMeetingsRepo.getAllMetingsDone(idEmployee)
                val responseBody = response.body()!!
                if (response.isSuccessful) {
                    if (responseBody?.code != 200) {
                        _meetingListLiveData.postValue(ResourceState.failured(responseBody.message))
                    } else {
                        val listMeeting = responseBody?.data
                        if (listMeeting.isNullOrEmpty()) {
                            _meetingListLiveData.postValue(ResourceState.failured("The Data is Not Avalaible"))
                        } else {
                            _meetingListLiveData.postValue(ResourceState.success(listMeeting))
                        }
                    }
                } else {
                    _meetingListLiveData.postValue(ResourceState.failured(response.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _meetingListLiveData.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }

        }
    }

    fun setRvMeetingComingSoon(idEmployee : Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _meetingListLiveData.postValue(ResourceState.loading())
                val response = reminderMeetingsRepo.getAllMetingsNotDone(idEmployee)
                val responseBody = response.body()!!
                if (response.isSuccessful) {
                    if (responseBody?.code != 200) {
                        _meetingListLiveData.postValue(ResourceState.failured(responseBody.message))
                    } else {
                        val listMeeting = responseBody?.data
                        if (listMeeting.isNullOrEmpty()) {
                            _meetingListLiveData.postValue(ResourceState.failured("The Data is Not Avalaible"))
                        } else {
                            _meetingListLiveData.postValue(ResourceState.success(listMeeting))
                        }
                    }
                } else {
                    _meetingListLiveData.postValue(ResourceState.failured(response.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _meetingListLiveData.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }

        }
    }



    override fun onDetail(meeting: ScheduleMeetings) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _onDetailMeetingLiveData.postValue(ResourceState.loading())
                _onDetailMeetingLiveData.postValue(ResourceState.success(meeting))
            } catch (e: Exception) {
                e.printStackTrace()
                _onDetailMeetingLiveData.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }
        }
    }
}