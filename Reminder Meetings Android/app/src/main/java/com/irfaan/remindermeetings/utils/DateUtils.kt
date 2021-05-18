package com.irfaan.remindermeetings.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

object DateUtils {
    private val getDate = Calendar.getInstance()
    private val year = getDate.get(Calendar.YEAR)
    private val month = getDate.get(Calendar.MONTH)
    private val day = getDate.get(Calendar.DAY_OF_MONTH)
    private val formatDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)


    fun show(context : Context, callback : (dataParsed : String) -> Unit) {
        val datepicker = DatePickerDialog(context, {
                view, year, month, dayOfMonth ->
            val selectDate = Calendar.getInstance()
            selectDate.set(Calendar.YEAR, year)
            selectDate.set(Calendar.MONTH, month)
            selectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = formatDate.format(selectDate.time)
            callback(date)
        },year, month, day)
        datepicker.show()
    }

}