package com.example.neruapp.usageStats

import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.type.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UsageStatsRepository(private val context: Context?) {

    private val db = FirebaseFirestore.getInstance()

    fun getUsageStatsForApp(packageName: String, interval: Int): AppUsageInfo? {
        val usageStatsManager = context?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val endTime = System.currentTimeMillis()
        val startTime = endTime - 24 * 60 * 60 * 1000

        val queryUsageStats = usageStatsManager.queryUsageStats(interval, startTime, endTime)
        val appUsageStats = queryUsageStats.find { it.packageName == packageName }

        return appUsageStats?.let {
            val timeInMinutes = it.totalTimeInForeground / (1000 * 60)
            AppUsageInfo(it.packageName, timeInMinutes)
        }
    }
    fun saveAppUsage(userId: String, date: String, appName: String, timeSpent: Long, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val appUsage = hashMapOf(
            "Date" to date,
            "AppName" to appName,
            "TimeSpent" to timeSpent
        )

        db.collection("Users").document(userId).collection("AppUsage")
            .add(appUsage)
            .addOnSuccessListener {
                Log.d(TAG, "App usage saved successfully!")
                onSuccess.invoke()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error saving app usage", e)
                onFailure.invoke(e)
            }
    }

    fun getAppUsage(userId: String, date: String, onSuccess: (Map<String, Any>) -> Unit, onFailure: (Exception) -> Unit) {

        db.collection("Users").document(userId).collection("AppUsage").document(date)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    onSuccess.invoke(document.data ?: emptyMap())
                } else {
                    Log.d(TAG, "No such document")
                    onFailure.invoke(Exception("No such document"))
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                onFailure.invoke(exception)
            }
    }

    fun getAppUsageForSpecificAppOverLastNDays(
        userId: String,
        appName: String, // Added parameter for the specific app name
        days: Int,
        onComplete: (List<Long>) -> Unit
    ) {
        val endDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(java.util.Date())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days + 1)
        val startDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(calendar.time)

        db.collection("Users")
            .document(userId)
            .collection("AppUsage")
            .whereEqualTo("AppName", appName) // Filtering by AppName
            .whereGreaterThanOrEqualTo("Date", startDate) // Note the change to "Date" since in your save function you've used "Date" and not "date".
            .whereLessThanOrEqualTo("Date", endDate)
            .orderBy("Date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val usageTimes = mutableListOf<Long>()
                for (document in documents) {
                    val usageTime = document.getLong("TimeSpent") ?: 0L
                    usageTimes.add(usageTime)
                }
                onComplete(usageTimes) // Returning the list of usage times (in minutes) for the specific app
            }
    }

    companion object {
        const val TAG = "FirestoreRepository"
    }
}
