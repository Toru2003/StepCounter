package com.example.stepcounter.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.stepcounter.MainActivity
import com.example.stepcounter.R
import com.example.stepcounter.utils.StepCounterService.Companion
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {

    fun Date.formatDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(this)
    }
    fun Date.formatDateDayMonth(): String {
        val dateFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
        return dateFormat.format(this)
    }

    fun createDefaultDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    fun getYesterdayDate () :Date{
        val current = android.icu.util.Calendar.getInstance()
        current.time = createDefaultDate()
        current.add(java.util.Calendar.DAY_OF_YEAR, -1)
        return current.time
    }



}