package com.irfaan.remindermeetings.presentations.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.finalproject.utils.ResourceStatus
import com.irfaan.remindermeetings.R
import com.irfaan.remindermeetings.data.models.AccountResponse
import com.irfaan.remindermeetings.data.models.Employee
import com.irfaan.remindermeetings.databinding.FragmentProfileBinding
import com.irfaan.remindermeetings.utils.AppConstant
import com.irfaan.remindermeetings.utils.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var loadingDialog: AlertDialog
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var idEmployee : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog.build(requireContext())
        initViewModel()
        subscribe()
        idEmployee = sharedPreferences.getInt(AppConstant.APP_ID_EMPLOYEE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        idEmployee?.let {
            viewModel.fillProfileAccount(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnLogOutAccount.setOnClickListener {
                clearSharedPreferencesWhenLogOut()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private fun subscribe() {
        viewModel.setProfile.observe(this, {
            when (it.status) {
                ResourceStatus.LOADING -> loadingDialog.show()
                ResourceStatus.FAILURE -> {
                    loadingDialog.hide()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                ResourceStatus.SUCCESS -> {
                    loadingDialog.hide()
                    val profileAccount = it?.data as Employee?
                    Log.d("Employe Response", profileAccount.toString())
                    settingProfile(profileAccount)
                }
            }
        })
    }

    private fun settingProfile(profileAccount: Employee?) {
        binding.apply {
            idEmployee.text = profileAccount?.id.toString()
            nameEmployee.text = profileAccount?.name
            emailEmployee.text = profileAccount?.email
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    private fun clearSharedPreferencesWhenLogOut() {
        sharedPreferences.edit().remove(AppConstant.ON_LOGIN_FINISHED).apply()
        sharedPreferences.edit().remove(AppConstant.APP_ID_EMPLOYEE).apply()
    }

}