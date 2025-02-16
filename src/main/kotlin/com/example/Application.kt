package com.example

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.repository.DatabaseFactory
import com.example.repository.Repo
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {


    configureSecurity()
    configureSerialization()
    configureRouting()
}
