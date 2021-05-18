package com.irfaan.remindermeetings.presentations.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.utils.ResourceState
import com.irfaan.remindermeetings.data.models.LoginAccountRequest
import com.irfaan.remindermeetings.data.repositories.ReminderMeetingsRepo
import com.irfaan.remindermeetings.di.qualifier.ReminderMeetingRepoQualifer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        @ReminderMeetingRepoQualifer
        private val reminderMeetingRepo : ReminderMeetingsRepo
    )
    : ViewModel() {



    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var _inputValidation = MutableLiveData<ResourceState>()
    val inputValidation: LiveData<ResourceState>
        get() {
            return _inputValidation
        }

    private var _loginAccount = MutableLiveData<ResourceState>()
    val loginAccount: LiveData<ResourceState>
        get() {
            return _loginAccount
        }

    fun checkEmailPasswordLogin(email: String, password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _inputValidation.postValue(ResourceState.loading())
                if (email.isBlank() && !email.matches(emailPattern.toRegex())) {
                    _inputValidation.postValue(ResourceState.failured("Your Email is Blank or Not Email"))
                } else if (password.isBlank()) {
                    _inputValidation.postValue(ResourceState.failured("Your Password is Blank"))
                } else {
                    _inputValidation.postValue(ResourceState.success(true))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _inputValidation.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }
        }
    }

    fun loginAccountToHome(request: LoginAccountRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _loginAccount.postValue(ResourceState.loading())
                Log.d("INI LOGIN 1", "LOGIN 1")
                val response = reminderMeetingRepo.loginAccountEmployee(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.data == null || responseBody.code != 200) {
                        _loginAccount.postValue(ResourceState.failured(responseBody?.message))
                    } else {
                        _loginAccount.postValue(ResourceState.success(responseBody))
                    }
                } else {
                    _loginAccount.postValue(ResourceState.failured(response.message()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _loginAccount.postValue(ResourceState.failured("Sorry the application is having a trouble"))
            }
        }
    }
}