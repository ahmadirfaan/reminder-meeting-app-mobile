package com.irfaan.remindermeetings.presentations.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.irfaan.remindermeetings.data.models.ScheduleMeetings
import com.irfaan.remindermeetings.databinding.LayoutRvMeetingsBinding

class HomeViewHolder(private val view : View, private val homeClickListener: HomeClickListener) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutRvMeetingsBinding.bind(view)

    fun bind(scheduleMeetings: ScheduleMeetings) {
        val dateMeeting = scheduleMeetings.dateMeetings?.substring(0,10)
        val timeMeeting = scheduleMeetings.dateMeetings?.substring(11,16)
        binding.apply {
            tvTitleMeeting.text = "${scheduleMeetings.title}"
            tvDateMeeting.text = "Date Meeting : $dateMeeting"
            tvTimeMeeting.text = "Time Meeting : $timeMeeting"
            btnDetailMeetings.setOnClickListener {
                homeClickListener.onDetail(scheduleMeetings)
            }
        }
    }
}