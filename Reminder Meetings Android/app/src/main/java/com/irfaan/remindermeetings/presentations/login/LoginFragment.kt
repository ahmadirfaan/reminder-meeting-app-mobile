package com.irfaan.remindermeetings.presentations.login

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.finalproject.utils.ResourceStatus
import com.irfaan.remindermeetings.R
import com.irfaan.remindermeetings.data.models.AccountResponse
import com.irfaan.remindermeetings.data.models.LoginAccountRequest
import com.irfaan.remindermeetings.databinding.FragmentLoginBinding
import com.irfaan.remindermeetings.utils.AppConstant
import com.irfaan.remindermeetings.utils.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private lateinit var loadingDialog: AlertDialog
    private var isBackPressed = false
    private lateinit var emailTextWatcher: TextWatcher

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        loadingDialog = LoadingDialog.build(requireContext())
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (!isBackPressed) {
                Toast.makeText(requireContext(), "Click Once Again to Out Application", Toast.LENGTH_SHORT).show()
                isBackPressed = true
            } else {
                requireActivity().finish()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateEmailOnRuntime()
        binding.apply {
            btnLogin.setOnClickListener {
                val usernameString = inputEmailLogin.editText?.text.toString()
                val passswordString = inputPasswordLogin.editText?.text.toString()
                if(usernameString.matches(emailPattern.toRegex())) {
                    viewModel.checkEmailPasswordLogin(email = usernameString, password = passswordString)
                } else {
                    Toast.makeText(requireContext(), "Harap Masukkan Email yang Benar", Toast.LENGTH_SHORT).show()
                }
            }
            btnSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    //Untuk mengecek apakah input email yang dijalankan sudah memenuhi kriteria inputan sebuah email
    private fun validateEmailOnRuntime() {
        emailTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().matches(emailPattern.toRegex())) {
                    binding.inputEmailLogin.editText?.error = "Mohon isi format email dengan benar"
                }
            }
        }
        binding.inputEmailLogin.editText?.addTextChangedListener(emailTextWatcher)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun subscribe() {
        viewModel.inputValidation.observe(this, {
            when (it.status) {
                ResourceStatus.LOADING -> {
                    loadingDialog.show()
                }
                ResourceStatus.SUCCESS -> {
                    loadingDialog.hide()
                    binding.apply {
                        val emailString = inputEmailLogin.editText?.text.toString()
                        val passswordString = inputPasswordLogin.editText?.text.toString()
                        val loginAccount = LoginAccountRequest(email = emailString, password = passswordString)
                        viewModel.loginAccountToHome(loginAccount)
                    }
                }
                ResourceStatus.FAILURE -> {
                    loadingDialog.hide()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.loginAccount.observe(this, {
            when (it.status) {
                ResourceStatus.LOADING -> loadingDialog.show()
                ResourceStatus.SUCCESS -> {
                    loadingDialog.hide()
                    val accountResponse = it?.data as AccountResponse
                    accountResponse.data?.id?.let { it1 ->
                        sharedPreferences.edit().putInt(AppConstant.APP_ID_EMPLOYEE,
                            it1
                        ).apply()
                    }
                    sharedPreferences.edit().putBoolean(AppConstant.ON_LOGIN_FINISHED, true).apply()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                ResourceStatus.FAILURE -> {
                    loadingDialog.hide()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    companion object {

        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}