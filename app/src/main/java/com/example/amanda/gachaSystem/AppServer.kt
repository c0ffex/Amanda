package com.example.amanda.gachaSystem

import android.util.Log
import com.example.amanda.gachaSystem.ApiProvider.gachaApi
import com.example.neruapp.gachaSystem.GachaRepository
import com.example.neruapp.usageStats.UsageStatsRepository
import com.google.firebase.firestore.FirebaseFirestore
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


class AppServer {

    private val db = FirebaseFirestore.getInstance()

    private val gachaRepo = GachaRepository(gachaApi, db)

    private val server = embeddedServer(Netty, port = 8080) {
        routing {
            post("/post") {
                val result = call.receiveText()
                saveResultToDatabase(result)
                call.respondText("Result received", status = HttpStatusCode.OK)
            }
        }
    }

    fun start() {
        server.start(wait = false)
    }

    fun stop() {
        server.stop(1000, 5000)
    }

    private fun saveResultToDatabase(result: String) {
        gachaRepo.saveCharacter(result)
    }

}