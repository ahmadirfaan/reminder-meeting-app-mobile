package com.irfaan.remindermeetings.presentations.home

import com.irfaan.remindermeetings.data.models.ScheduleMeetings

interface HomeClickListener {
    fun onDetail(meeting : ScheduleMeetings)
}