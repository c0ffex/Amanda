package com.example.amanda.ui.home

import android.app.usage.UsageStatsManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neruapp.usageStats.AppUsageInfo
import com.example.neruapp.usageStats.UsageStatsRepository

class HomeViewModel(private val repository: UsageStatsRepository) : ViewModel() {
    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text
    private val _totalTimeInForeground = MutableLiveData<Long>()
    val totalTimeInForeground: LiveData<Long> = _totalTimeInForeground
    fun getAppUsageInfo(packageName: String, interval: Int): LiveData<AppUsageInfo?> {
        val data = MutableLiveData<AppUsageInfo?>()
        data.value = repository.getUsageStatsForApp(packageName, interval)
        return data
    }

    fun fetchUsageTime(): AppUsageInfo {
        val appUsageInfo = getAppUsageInfo("com.instagram.android", UsageStatsManager.INTERVAL_DAILY).value
        appUsageInfo?.totalTimeInForeground = appUsageInfo?.totalTimeInForeground!!

        val appName = appUsageInfo.packageName
        _text.value = "Time spent on Instagram: ${appUsageInfo.totalTimeInForeground ?: 0} minutes"
        return appUsageInfo
    }

    fun showUsageTime() {
        val appUsageInfo = repository.getUsageStatsForApp("com.instagram.android", UsageStatsManager.INTERVAL_DAILY)
        _totalTimeInForeground.value = appUsageInfo?.totalTimeInForeground ?: 0
    }
}