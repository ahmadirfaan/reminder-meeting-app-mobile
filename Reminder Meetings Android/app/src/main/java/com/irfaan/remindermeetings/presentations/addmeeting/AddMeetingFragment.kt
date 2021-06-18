package com.irfaan.remindermeetings.presentations.addmeeting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.irfaan.remindermeetings.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.finalproject.utils.ResourceStatus
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.irfaan.remindermeetings.data.models.DataMeeting
import com.irfaan.remindermeetings.data.models.EmployeesListItem
import com.irfaan.remindermeetings.data.models.MeetingRequest
import com.irfaan.remindermeetings.databinding.FragmentAddMeetingBinding
import com.irfaan.remindermeetings.presentations.listpeople.ListPeopleFragment
import com.irfaan.remindermeetings.utils.DateUtils
import com.irfaan.remindermeetings.utils.LoadingDialog
import com.irfaan.remindermeetings.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddMeetingFragment : Fragment() {

    private lateinit var binding: FragmentAddMeetingBinding
    private var addPeople: List<Int> = ArrayList()
    private lateinit var viewModel: AddMeetingViewModel
    private lateinit var loadingDialog: AlertDialog
    private var dataUri: Uri? = null
    private var file: File? = null
    private var uriString: String? = null
    val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it?.resultCode == Activity.RESULT_OK) {
            dataUri = it.data?.data
//            file = File(dataUri?.path)
            file = File(dataUri?.path?.let { it1 -> uriPath(it1) })
            Log.d("DATA URI", dataUri.toString())
            Log.d("DATA PATH URI", dataUri?.path.toString())
            Log.d("FILE OBJECT", file.toString())
            Log.d("FILE PATH", file!!.path)
            Log.d("FILE EXTENSION", file!!.extension)
            binding.apply {
                tvNameFile.text = "File : ${file?.name}"
                uriString = it.data?.data.toString()
            }

        } else {
            binding.tvNameFile.text = "Not Knowing The File"
        }
    }

    private lateinit var storageReference: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        loadingDialog = LoadingDialog.build(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMeetingBinding.inflate(layoutInflater)
        binding.apply {
            btnUploadFiles.setOnClickListener {
                callChooseFileFromDevice()
            }
            etDateMeeting.setOnClickListener {
                DateUtils.show(requireContext()) {
                    etDateMeeting.setText(it)
                }
            }
            etTimeMeeting.setOnClickListener {
                TimeUtils.show(requireContext()) {
                    etTimeMeeting.setText(it)
                }
            }
            btnAddPeople.setOnClickListener {
                val fragment = ListPeopleFragment()
                childFragmentManager?.let { it1 -> fragment.show(it1, "List People Fragment") }
                addPeople = fragment.listCountEmployee
            }
            btnAddMeeting.setOnClickListener {
                val formatFile = arrayListOf<String>("jpg", "pdf", "png", "jpeg", "docx", "doc")
                val tvNameFile = tvNameFile.text.toString()
                val dateMeeting = etDateMeeting.text.toString()
                val timeMeeting = etTimeMeeting.text.toString()
                val titleMeeting = etTitleMeeting.text.toString()
                if (tvNameFile.isBlank() || tvNameFile.equals("Tidak Diketahui Filenya")) {
                    Toast.makeText(requireContext(), "File Tidak Benar", Toast.LENGTH_SHORT).show()
                } else if (!formatFile.contains(file?.extension)) {
                    Toast.makeText(requireContext(), "File Yang Anda Masukkan tidak didukung", Toast.LENGTH_SHORT).show()
                } else {
                    storageReference = FirebaseStorage.getInstance().getReference("${System.currentTimeMillis()}.${file?.extension}")
                    viewModel.checkInputValidation(
                        dateMeeting = dateMeeting,
                        timeMeeting = timeMeeting,
                        titleMeeting = titleMeeting
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AddMeetingViewModel::class.java)
    }

    private fun subscribe() {
        viewModel.inputValidation.observe(this, {
            when (it.status) {
                ResourceStatus.LOADING -> {
                    loadingDialog.show()

                }
                ResourceStatus.SUCCESS -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        loadingDialog.show();
                        delay(3000)
                        loadingDialog.hide()

                    }
                    CoroutineScope(Dispatchers.Default).launch {
                        val uploadTask = storageReference.putFile(dataUri!!)
                        val task = uploadTask.continueWithTask{ task ->
                            if(!task.isSuccessful) {
                                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                            }
                            storageReference!!.downloadUrl
                        }.addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                val downloadUri = task.result
                                val downloadUrl = downloadUri!!.toString()
                                binding.apply {
                                    val dateMeeting = etDateMeeting.text.toString()
                                    val timeMeeting = etTimeMeeting.text.toString()
                                    val titleMeeting = etTitleMeeting.text.toString()
                                    val listAddPeople = ArrayList<EmployeesListItem>()
                                    for (id in addPeople) {
                                        val idEmployee = EmployeesListItem(id)
                                        listAddPeople.add(idEmployee)
                                    }
                                    val requestMeeting = MeetingRequest(
                                        title = titleMeeting,
                                        dateMeetings = "${dateMeeting} ${timeMeeting}",
                                        urlDocument = downloadUrl,
                                        employeesList = listAddPeople
                                    )
                                    viewModel.addMeetingRequest(requestMeeting)
                                }
                            } else {
                                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                                clearData()
                            }
                        }
                    }
                }
                ResourceStatus.FAILURE -> {
                    loadingDialog.hide()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.addMeetingLiveData.observe(this, {
            when (it.status) {
                ResourceStatus.LOADING -> loadingDialog.show()
                ResourceStatus.SUCCESS -> {
                    loadingDialog.hide()
                    clearData()
                    val data = it?.data as DataMeeting
                    Toast.makeText(
                        requireContext(),
                        "You Have Add ${data.detailScheduleList?.size} People  To Meeting ${data.title}",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_addMeetingFragment_to_homeFragment)

                }
                ResourceStatus.FAILURE -> {
                    loadingDialog.hide()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun callChooseFileFromDevice() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("*/*")
        intent = Intent.createChooser(intent, "Choose from A File")
        resultContract.launch(intent)
    }

    private fun uriPath(mypath: String): String {
        var path = ""
        if (mypath.contains("document/raw:")) {
            path = mypath.replace("/document/raw:", "");
        } else {
            path = mypath
        }
        return path
    }

    private fun clearData() {
        addPeople.toMutableList().clear()
        binding.apply {
            etDateMeeting.setText("")
            etTimeMeeting.setText("")
            etTitleMeeting.setText("")
            tvNameFile.setText("")
        }
        file = null
    }


    companion object {

        @JvmStatic
        fun newInstance() = AddMeetingFragment()
    }
}