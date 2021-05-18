package com.irfaan.remindermeetings.presentations.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.irfaan.remindermeetings.R
import com.irfaan.remindermeetings.databinding.ActivityMainBinding
import com.irfaan.remindermeetings.utils.AppConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
        setupNav()
        checkPermissionFunction()
        supportActionBar?.hide()
    }

    private fun setupNav() {
        navController.addOnDestinationChangedListener{_,destination, _ ->
            when(destination.id) {
                R.id.loginFragment -> hideBottomNav()
                R.id.splashScreenFragment -> hideBottomNav()
                R.id.addMeetingFragment -> hideBottomNav()
                R.id.homeFragment -> {
                    showBottomnav()
                    binding.bottomNavigationView.menu.getItem(0).isEnabled = false
                    binding.bottomNavigationView.menu.getItem(1).isEnabled = true
                }
                R.id.profileFragment -> {
                    showBottomnav()
                    binding.bottomNavigationView.menu.getItem(0).isEnabled = true
                    binding.bottomNavigationView.menu.getItem(1).isEnabled = false
                }
            }
        }
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomnav() {
        binding.bottomNavigationView.apply {
            if (menu.isEmpty()) {
                inflateMenu(R.menu.bottom_nav_menu)
                setupWithNavController(navController)
            }
            visibility = View.VISIBLE
        }
    }

    private fun checkForPermission(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(applicationContext, "$name Tidak Diberikan, maka aplikasi berhenti", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, "$name Granted", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            AppConstant.STORAGE_READ_PERMISSION_CODE -> innerCheck("Read permission access to storage ")
            AppConstant.STORAGE_WRITE_PERMISSION_CODE -> innerCheck("Write permission access to storage")
            AppConstant.MANAGE_STORAGE_PERMISSION_CODE -> innerCheck("Access permission to process to storage")
//            AppConstant.STORAGE_WRITE_PERMISSION_CODE -> innerCheck("Write Storage")
//            REQ_CAMERA -> innerCheck("CAMERA")
        }
    }

    private fun checkPermissionFunction() {
        checkForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, "Read Storage", AppConstant.STORAGE_READ_PERMISSION_CODE)
        checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write Storage", AppConstant.STORAGE_WRITE_PERMISSION_CODE)
//        checkForPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE, "Mengolah Penyimpanan", AppConstant.MANAGE_STORAGE_PERMISSION_CODE)
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("To use the application requires permission to $name")
            setTitle("Needs Permission")
            setPositiveButton("Ok") { _, _ ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}