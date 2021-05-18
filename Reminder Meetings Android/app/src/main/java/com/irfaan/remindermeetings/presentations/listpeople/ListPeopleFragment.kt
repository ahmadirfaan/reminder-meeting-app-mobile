package com.irfaan.remindermeetings.presentations.listpeople

import android.app.ActionBar
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.finalproject.utils.ResourceStatus
import com.irfaan.remindermeetings.data.models.Employee
import com.irfaan.remindermeetings.data.models.ScheduleMeetings
import com.irfaan.remindermeetings.databinding.FragmentListPeopleAddBinding
import com.irfaan.remindermeetings.utils.AppConstant
import com.irfaan.remindermeetings.utils.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListPeopleFragment() : DialogFragment() {

    private lateinit var binding: FragmentListPeopleAddBinding
    private lateinit var viewModel: ListPeopleViewModel
    private lateinit var listPeopleAdapter: ListPeopleViewAdapter
    private lateinit var loadingDialog: AlertDialog
    val listCountEmployee = ArrayList<Int>()
    private var listEmployee: List<Employee> = ArrayList()

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private var idEmployee:Int? = null



    override fun onStart() {
        super.onStart()
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog.build(requireContext())
        initViewModel()
        subscribe()
        idEmployee = sharedPreferences.getInt(AppConstant.APP_ID_EMPLOYEE, 0)
        listPeopleAdapter = ListPeopleViewAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListPeopleAddBinding.inflate(layoutInflater)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        );
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setRVEmployee()
        binding.apply {
            rvListPeople.apply {
                adapter = listPeopleAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            btnAddPeople.setOnClickListener {
                listEmployee = listPeopleAdapter.getSelectedList()
                dismiss()
                addPeople()
                Toast.makeText(requireContext(), "You Have Add People ${listCountEmployee.size}", Toast.LENGTH_LONG).show()
                Log.d("LIST INT EMPLOYEE", listCountEmployee.toString())
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ListPeopleViewModel::class.java)
    }

    private fun subscribe() {
        viewModel.peopleListLiveData.observe(this, {
            when (it.status) {
                ResourceStatus.LOADING -> loadingDialog.show()
                ResourceStatus.FAILURE -> {
                    loadingDialog.hide()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                ResourceStatus.SUCCESS -> {
                    loadingDialog.hide()
                    val listEmployee = it.data as List<Employee>
                    val filterEmployee = listEmployee?.filter {
                        it?.id?.equals(idEmployee) != true
                    }
                    listPeopleAdapter.setEmployeeList(filterEmployee)
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setWidthPercent(85)
    }

    private fun addPeople() {
        listCountEmployee.clear()
        for(employee in listEmployee) {
            listCountEmployee.add(employee.id!!)
        }
        listCountEmployee.add(idEmployee!!)
    }
    /**
     * Call this method (in onActivityCreated or later)
     * to make the dialog near-85%.
     */
    private fun DialogFragment.setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }



}