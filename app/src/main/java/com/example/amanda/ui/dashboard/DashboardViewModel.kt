package com.example.amanda.ui.dashboard

import android.app.usage.UsageStatsManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neruapp.usageStats.UsageStatsRepository
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardViewModel(private val repository: UsageStatsRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _chartEntryModel = MutableLiveData<ChartEntryModel>()
    val chartEntryModel: LiveData<ChartEntryModel> = _chartEntryModel

    private val _floatData = MutableLiveData<List<Float>>()
    val floatData: LiveData<List<Float>> = _floatData

    fun fetchInstagramUsageForLast7Days() {
        repository.getAppUsageForSpecificAppOverLastNDays("1", "com.instagram.android", 7) { usageList ->
            if (usageList.isEmpty()) {
                fetchAndSaveDeviceUsage()
            } else {
                val floatList = usageList.map { it.toFloat() }
                _floatData.postValue(floatList)
            }
        }
    }

    private fun fetchAndSaveDeviceUsage() {
        val appUsageInfo = repository.getUsageStatsForApp("com.instagram.android", UsageStatsManager.INTERVAL_DAILY)
        appUsageInfo?.let {
            val usageInMinutes = it.totalTimeInForeground.toFloat()

            // Save the data to the database
            val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
            repository.saveAppUsage("1", formattedDate, "com.instagram.android", usageInMinutes.toLong(), {
                println("Successfully saved app usage to database.")
            }, { e ->
                println("Error saving app usage to database. Error: ${e.message}")
            })

            // Update the LiveData
            _floatData.postValue(listOf(usageInMinutes))
        } ?: run {
            // Handle the case when the app hasn't been used at all.
            _floatData.postValue(listOf(0f))
        }
    }
}