package com.example.neruapp.gachaSystem

import android.util.Log
import com.example.amanda.gachaSystem.GachaApi
import com.example.amanda.gachaSystem.GachaRequest
import com.example.amanda.gachaSystem.GachaResponse
import com.example.neruapp.usageStats.UsageStatsRepository
import com.google.firebase.firestore.FirebaseFirestore

class GachaRepository(private val gachaApi: GachaApi, private val db: FirebaseFirestore) {


    suspend fun activateGacha(userId: String, coins: Int): GachaResponse {
        val request = GachaRequest(userId, coins)
        val response = gachaApi.activateGacha(request)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Failed to activate Gacha: ${response.errorBody()?.string()}")
        }
    }

    fun saveCharacter(result: String) {
        val character = hashMapOf(
            "name" to result
        )

        db.collection("Users").document("1").collection("Characters")
            .add(character)
            .addOnSuccessListener {
//                Log.d(GachaRepository.TAG, "App usage saved successfully!")
//                onSuccess.invoke()
            }
            .addOnFailureListener { e ->
//                Log.w(GachaRepository.TAG, "Error saving app usage", e)
//                onFailure.invoke(e)
            }
    }
    }
