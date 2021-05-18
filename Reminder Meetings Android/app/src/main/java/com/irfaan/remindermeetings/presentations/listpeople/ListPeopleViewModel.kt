package com.irfaan.remindermeetings.presentations.listpeople

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.utils.ResourceState
import com.irfaan.remindermeetings.data.models.Employee
import com.irfaan.remindermeetings.data.repositories.ReminderMeetingsRepo
import com.irfaan.remindermeetings.di.qualifier.ReminderMeetingRepoQualifer
import com.irfaan.remindermeetings.utils.AppConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPeopleViewModel @Inject constructor(
    @ReminderMeetingRepoQualifer
    private val reminderMeetingsRepo: ReminderMeetingsRepo
) : ViewModel() {




    private var _peopleListLiveData = MutableLiveData<ResourceState>()
    val peopleListLiveData: LiveData<ResourceState>
        get() {
            return _peopleListLiveData
        }

    fun setRVEmployee() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _peopleListLiveData.postValue(ResourceState.loading())
                val response = reminderMeetingsRepo.getAllEmployee()
                val responseBody = response.body()!!
                if (response.isSuccessful) {
                    if (responseBody?.code != 200) {
                        _peopleListLiveData.postValue(ResourceState.failured(responseBody.message))
                    } else {
                        val listData = responseBody?.data
                        if (listData.isNullOrEmpty()) {
                            _peopleListLiveData.postValue(ResourceState.failured("The Data is Not Avalaible"))
                        } else {
                            _peopleListLiveData.postValue(ResourceState.success(listData))
                        }
                    }
                } else {
                    _peopleListLiveData.postValue(ResourceState.failured(response.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _peopleListLiveData.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }

        }
    }


}