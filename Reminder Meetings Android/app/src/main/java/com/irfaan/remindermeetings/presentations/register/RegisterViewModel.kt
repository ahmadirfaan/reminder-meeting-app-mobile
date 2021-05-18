package com.irfaan.remindermeetings.presentations.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.utils.ResourceState
import com.irfaan.remindermeetings.data.models.RegisterAccountRequest
import com.irfaan.remindermeetings.data.repositories.ReminderMeetingsRepo
import com.irfaan.remindermeetings.di.qualifier.ReminderMeetingRepoQualifer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ReminderMeetingRepoQualifer private val reminderMeetingsRepo: ReminderMeetingsRepo
) : ViewModel() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var _inputValidation = MutableLiveData<ResourceState>()
    val inputValidation: LiveData<ResourceState>
        get() {
            return _inputValidation
        }

    private var _createAccountLiveData = MutableLiveData<ResourceState>()
    val createAccountLiveData: LiveData<ResourceState>
        get() {
            return _createAccountLiveData
        }

    fun checkInputEmailPassword(name : String, email: String, password: String, confirmPassword: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _inputValidation.postValue(ResourceState.loading())
                delay(2000)
                val check = ArrayList<Int>()
                if (email.isNotBlank() && email.matches(emailPattern.toRegex())) {
                    check.add(5)
                }
                if (password.isNotBlank() && password.contentEquals(confirmPassword)) {
                    check.add(0)
                }
                if(name.isNotBlank()) {
                    check.add(10)
                }
                Log.d("ARRAY LIST SIGN UP EMAIL", check.toString())
                if (check.size == 3) {
                    _inputValidation.postValue(ResourceState.success(true))
                } else if(check.size == 0) {
                    _inputValidation.postValue(ResourceState.failured("Email Format is wrong and Password must same with confirm password"))
                }else {
                    if(check[0] == 5) {
                        _inputValidation.postValue(ResourceState.failured("Password and Confirm Password Must Equal"))
                    } else if(check[0] == 0) {
                        _inputValidation.postValue(ResourceState.failured("Please input the correct email"))
                    } else if(check[0] == 10) {
                        _inputValidation.postValue(ResourceState.failured("Please input the your name"))
                    }
                }
            } catch (e : Exception) {
                e.printStackTrace()
                _inputValidation.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }

        }
    }

    fun registerAccount(request: RegisterAccountRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _createAccountLiveData.postValue(ResourceState.loading())
                val response = reminderMeetingsRepo.createAccountEmployee(request)
                val responseBody = response.body()
                if (response.isSuccessful) {
                    if(responseBody?.code?.equals(200) == true) {
                        _createAccountLiveData.postValue(ResourceState.success(responseBody?.data))
                    } else {
                        _createAccountLiveData.postValue(ResourceState.failured("Email has been used"))
                    }
                } else {
                    _createAccountLiveData.postValue(ResourceState.failured(response.message()))
                }
            } catch (e : Exception) {
                e.printStackTrace()
                _createAccountLiveData.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }
        }
    }
}