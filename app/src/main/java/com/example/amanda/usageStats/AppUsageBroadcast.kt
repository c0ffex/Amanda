package com.example.neruapp.usageStats

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.amanda.ui.home.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AppUsageBroadcast() : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onReceive(context: Context?, intent: Intent?) {

        val repository = UsageStatsRepository(context!!)
        val view: HomeViewModel = HomeViewModel(repository)
        val fetchUsage = view.fetchUsageTime()
        // Obtendo a data do dia anterior
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val dateOfYesterday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val userId = "1"
        val appName = fetchUsage.packageName
        val timeSpent: Long =
            fetchUsage.totalTimeInForeground // obtenha o tempo gasto no aplicativo aqui

        repository.saveAppUsage(userId, dateOfYesterday, appName, timeSpent,
            onSuccess = {
                Log.d(TAG, "Dados salvos com sucesso!")
            },
            onFailure = { exception ->
                Log.w(TAG, "Erro ao salvar os dados.", exception)
            }
        )
    }

    fun scheduleMidnightAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AppUsageBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Setting the time for next midnight
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DATE, 1)  // This ensures you get the next day's midnight
            set(Calendar.HOUR_OF_DAY, 0)  // Hour is set to 0, not 24
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    // Scheduling the exact alarm for next midnight
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                } else {
                    // Fallback to inexact alarms or notify the user
                    // Using setInexactRepeating as an example fallback
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
                    // Optionally notify the user that an inexact alarm has been set
                }
            } else {
                // For older OS versions, just schedule the alarm
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        } catch (e: SecurityException) {
            // Handle the SecurityException, for instance, by logging it or notifying the user
            Log.e("AlarmScheduler", "SecurityException while setting the alarm: ${e.message}")
        }
    }




}

