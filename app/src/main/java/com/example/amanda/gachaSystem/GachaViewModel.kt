package com.example.neruapp.gachaSystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GachaViewModel(private val repository: GachaRepository) : ViewModel() {

    fun activateGacha(userId: String, coins: Int) {
        viewModelScope.launch {
            try {
                val gachaResponse = repository.activateGacha(userId, coins)

            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}