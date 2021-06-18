package com.irfaan.remindermeetings.alarmutils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.irfaan.remindermeetings.alarmutils.ScheduledWorker.Companion.NOTIFICATION_MESSAGE
import com.irfaan.remindermeetings.alarmutils.ScheduledWorker.Companion.NOTIFICATION_TITLE
import com.irfaan.remindermeetings.data.models.ScheduleMeetings
import com.irfaan.remindermeetings.utils.NotificationUtil
import com.irfaan.remindermeetings.utils.isTimeAutomatic
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class ScheduleAlarm(private val scheduleMeetings: ScheduleMeetings, private val context: Context) {


    private val title = "${scheduleMeetings?.title}"
    private val dateMeeting = scheduleMeetings.dateMeetings?.substring(0, 10)
    private val timeMeeting = scheduleMeetings.dateMeetings?.substring(11, 16)
    private val message = "The Meeting Will Be Start at $timeMeeting"
    private val scheduledTimeString = "$dateMeeting $timeMeeting"


    fun scheduleAlarm() {
        //Check that 'Automatic Date and Time' settings are turned ON.If it 's not turned on, Return
        if (!isTimeAutomatic(context)) {
            Log.d("TAG", "`Automatic Date and Time` is not enabled")
            return
        }
        println("JAM TIME MEETING $timeMeeting")

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =
            Intent(context, NotificationBroadcastReceiver::class.java).let { intent ->
                intent.putExtra(NOTIFICATION_TITLE, title)
                intent.putExtra(NOTIFICATION_MESSAGE, message)
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }

        // Parse Schedule time
        var scheduledTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            .parse(scheduledTimeString!!)
        val instant = scheduledTime.toInstant().minusSeconds(1800)
        scheduledTime = Date.from(instant)
        if(scheduledTime.time > System.currentTimeMillis())
        scheduledTime?.let {
            // With set(), it'll set non repeating one time alarm.
            alarmMgr.setExact(
                AlarmManager.RTC_WAKEUP,
                it.time,
                alarmIntent
            )
        }
    }

    private fun showNotification(title: String, message: String) {
        NotificationUtil(context).showNotification(title, message)
    }

}