package com.irfaan.remindermeetings.utils

import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private val getDate = Calendar.getInstance()
    private val startHour = getDate.get(Calendar.HOUR_OF_DAY)
    private val startMinute = getDate.get(Calendar.MINUTE)
    private val formatDate = SimpleDateFormat("HH:mm", Locale.US)


    fun show(context : Context, callback : (dataParsed : String) -> Unit) {
        val timePicker = TimePickerDialog(context, {
                _,hour, minute ->
            val selectDate = Calendar.getInstance()

            selectDate.set(Calendar.HOUR_OF_DAY, hour)
            selectDate.set(Calendar.MINUTE, minute)
            val date = formatDate.format(selectDate.time)
            callback(date)
        }, startHour, startMinute, true)
        timePicker.show()
    }
}