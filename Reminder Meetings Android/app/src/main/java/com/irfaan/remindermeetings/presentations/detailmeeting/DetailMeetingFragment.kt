package com.irfaan.remindermeetings.presentations.detailmeeting

import android.app.ActionBar
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.irfaan.remindermeetings.data.models.ScheduleMeetings
import com.irfaan.remindermeetings.databinding.FragmentDetailMeetingBinding
import java.util.*

class DetailMeetingFragment(
    private val scheduleMeetings: ScheduleMeetings
) : DialogFragment() {

    private lateinit var binding: FragmentDetailMeetingBinding
    private var myDownloadId: Long = 0 //Untuk verifikasi jika download complete
    private var br = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == myDownloadId) {
                Toast.makeText(requireContext(), "File Download Completed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        binding.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailMeetingBinding.inflate(layoutInflater)
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        );
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setText()
        binding.apply {
            btnDownloadFile.setOnClickListener {
                downloadFile()
            }
        }

    }

    private fun setText() {
        val dateMeetingString = scheduleMeetings.dateMeetings?.substring(0, 10)
        val timeMeetingString = scheduleMeetings.dateMeetings?.substring(11, 16)
        val listSchedule = scheduleMeetings?.detailScheduleList
        val sb = StringBuilder()
        if (listSchedule != null) {
            for ((index, schedule) in listSchedule.withIndex()) {
                sb.append("${index + 1}").append(". ")
                    .append("Name : ${schedule?.employee?.name}, Email : ")
                    .append("${schedule?.employee?.email}").append(" \n")
            }
            binding.apply {
                titleMeeting.text = "Title : ${scheduleMeetings.title}"
                timeMeeting.text = "   Time : $timeMeetingString"
                dateMeeting.text = "   Date : $dateMeetingString"
                listPeople.text = sb.toString()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setFullScreen()
    }

    private fun downloadFile() {
        var fileName = "${scheduleMeetings?.title} ${scheduleMeetings.dateMeetings}"
        try {
            var request = DownloadManager.Request(Uri.parse(scheduleMeetings.urlDocument))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setTitle(fileName)
                .setDescription("File Still Downloaded")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
            request.allowScanningByMediaScanner()
            val tandaTanya = scheduleMeetings.urlDocument!!.indexOf("?")
            val urlRemove = scheduleMeetings.urlDocument!!.substring(0, tandaTanya)
            val lengthUrl = urlRemove?.length
            val type = lengthUrl?.minus(4)
            val jenisFile = type?.let { urlRemove?.substring(it, lengthUrl) }
            Log.d("JENIS FILE", jenisFile!!)
            if (jenisFile?.equals(".pdf", true) == true) {
                Log.d("MASUK LOG SET MIME TYPE", "MASUK DISINI PDF")
                request.setMimeType("application/pdf")
                fileName += ".pdf"
            } else if (jenisFile?.equals(".jpg", true) == true) {
                request.setMimeType("image/jpg")
                fileName += ".jpg"
            } else if (jenisFile?.equals("jpeg", true) == true) {
                request.setMimeType("image/jpeg")
                fileName += ".jpeg"
            } else if (jenisFile?.equals(".png", true)) {
                request.setMimeType("image/png")
                fileName += ".png"
            } else if (jenisFile?.equals("docx", true)) {
                request.setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                fileName += ".docx"
            } else if (jenisFile?.equals(".doc", true)) {
                request.setMimeType("application/msword")
                fileName += ".doc"
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            var dm = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            myDownloadId = dm.enqueue(request)
            requireActivity().registerReceiver(
                br,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "File Error", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Call this method (in onActivityCreated or later)
     * to make the dialog near-full screen.
     */
    fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}