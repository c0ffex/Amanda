package com.example.neruapp.usageStats

data class AppUsageInfo(
    val packageName: String,
    var totalTimeInForeground: Long
)