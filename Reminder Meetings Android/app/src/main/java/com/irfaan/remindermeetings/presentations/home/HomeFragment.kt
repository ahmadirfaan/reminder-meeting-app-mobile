package com.irfaan.remindermeetings.presentations.home

import android.content.SharedPreferences
import android.content.res.ColorStateList
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.finalproject.utils.ResourceStatus
import com.irfaan.remindermeetings.R
import com.irfaan.remindermeetings.alarmutils.ScheduleAlarm
import com.irfaan.remindermeetings.data.models.ScheduleMeetings
import com.irfaan.remindermeetings.databinding.FragmentHomeBinding
import com.irfaan.remindermeetings.presentations.detailmeeting.DetailMeetingFragment
import com.irfaan.remindermeetings.utils.AppConstant
import com.irfaan.remindermeetings.utils.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentHomeBinding
    private lateinit var loadingDialog: AlertDialog
    private lateinit var viewModel: HomeViewModel
    private lateinit var homeViewAdapter: HomeViewAdapter
    private var idEmployee: Int? = null
    private var setScheduleAlarm: List<ScheduleMeetings>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog.build(requireContext())
        initViewModel()
        subscribe()
        setAlarm()
        homeViewAdapter = HomeViewAdapter(viewModel)
        idEmployee = sharedPreferences.getInt(AppConstant.APP_ID_EMPLOYEE, 0)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComingSoonMeeting()
        binding.apply {
            btnListFinish.setOnClickListener {
                setFinishMeeting()
            }
            btnNotFinish.setOnClickListener {
                setComingSoonMeeting()
            }
            addMeetingFab.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addMeetingFragment)
            }
            rvListMeeting.apply {
                adapter = homeViewAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun setFinishMeeting() {
        binding.btnListFinish.apply {
            isEnabled = false
            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.color_utama))
            setTextColor(ColorStateList.valueOf(resources.getColor(R.color.white)))
        }
        binding.btnNotFinish.apply {
            isEnabled = true
            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
            setTextColor(ColorStateList.valueOf(resources.getColor(R.color.color_utama)))
        }
        idEmployee?.let { viewModel.setRvMeetingDone(it) }
    }

    private fun setComingSoonMeeting() {
        Log.d("SCHEDULE ALARM", "CHECK SCHEDULE ALARM COMING SOON")
        binding.btnNotFinish.apply {
            isEnabled = false
            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.color_utama))
            setTextColor(ColorStateList.valueOf(resources.getColor(R.color.white)))
        }
        binding.btnListFinish.apply {
            isEnabled = true
            backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
            setTextColor(ColorStateList.valueOf(resources.getColor(R.color.color_utama)))
        }
        idEmployee?.let { viewModel.setRvMeetingComingSoon(it) }

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private fun subscribe() {
        viewModel.meetingListLiveData.observe(this, {
            when (it.status) {
                ResourceStatus.LOADING -> loadingDialog.show()
                ResourceStatus.FAILURE -> {
                    loadingDialog.hide()
                    binding.rvListMeeting.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                ResourceStatus.SUCCESS -> {
                    loadingDialog.hide()
                    val listMeetings = it.data as List<ScheduleMeetings>
                    if(this.setScheduleAlarm.isNullOrEmpty()) {
                        for(meeting in listMeetings) {
                            this.setScheduleAlarm = listMeetings
                        }
                    }
                    binding.rvListMeeting.visibility = View.VISIBLE
                    homeViewAdapter.setScheduleMeetingList(listMeetings)
                }
            }
        })
        viewModel.onDetailMeetingLiveData.observe(this, {
            when (it.status) {
                ResourceStatus.LOADING -> loadingDialog.show()
                ResourceStatus.FAILURE -> {
                    loadingDialog.hide()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                ResourceStatus.SUCCESS -> {
                    loadingDialog.hide()
                    val data = it.data as ScheduleMeetings
                    val fragment = DetailMeetingFragment(data)
                    childFragmentManager?.let { it1 -> fragment.show(it1, "Detail Meeting") }
                }
            }
        })
    }
    
    private fun setAlarm() {
        println("SCHEDULE ALARM ${this.setScheduleAlarm}")
        if(!this.setScheduleAlarm.isNullOrEmpty()) {
            println("SCHEDULE ALARM LOOPING DATA FROM API")
                val meeting = this.setScheduleAlarm!!.get(0)
                val meetingalarm = ScheduleAlarm(meeting, requireContext())
                meetingalarm.scheduleAlarm()
        } else {
            println("SCHEDULE ALARM LOOPING DATA FROM API FALSE")
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}