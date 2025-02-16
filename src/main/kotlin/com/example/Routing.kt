package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.User
import com.example.repository.DatabaseFactory
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.repository.Repo
import com.example.routes.CrmRoutes
import com.example.routes.UserRoutes
import kotlinx.serialization.Serializable

fun Application.configureRouting() {

    DatabaseFactory.init()
    val db = Repo()
    val jwtService = JwtService()
    val hashFunction = { s:String -> hash(s) }

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "Crm Server"
            validate{
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUserByEmail(email)
                user
            }
        }
    }

    routing {
        get("/") {
            call.respondText("Hello World!")

        }

        UserRoutes(db, jwtService, hashFunction)
        CrmRoutes(db, hashFunction)

        route("/notes"){


            route("/create") {

                post{
                    val body = call.receive<String>()

                    call.respond(body)
                }
            }

            delete{
                val body = call.receive<String>()
                call.respond(body)
            }
        }
    }
}
