package com.example.amanda.gachaSystem

data class GachaRequest(val userId: String, val coins: Int)

data class GachaResponse(val success: Boolean, val character: Character?)
