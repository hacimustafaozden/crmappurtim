package com.example.routes

import com.example.authentication.JwtService
import com.example.data.model.Crm
import com.example.data.model.SimpleResponse
import com.example.data.model.User
import com.example.repository.Repo
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val CRMS = "$API_VERSION/crms"
const val CREATE_CRMS = "$CRMS/create"
const val UPDATE_CRMS = "$CRMS/update"
const val DELETE_CRMS = "$CRMS/delete"



fun Route.CrmRoutes(
    db: Repo,
    hashFunction: (String) -> String
) {

    authenticate( "jwt") {

        post (CREATE_CRMS){

            val crm = try {
                call.receive<Crm>()
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"missing Fields"))
                return@post
            }

            try {
                val email = call.principal<User>()!!.email
                db.addCrm(crm,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Crm Added Successfully"))

            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message?:"some problem occount"))
            }

        }

        get (CRMS){

            try{
                val email = call.principal<User>()!!.email
                val crms = db.getAllCrms(email)
                call.respond(HttpStatusCode.OK,crms)
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict, emptyList<Crm>())
            }


        }

        post(UPDATE_CRMS) {

                val crm = try {
                    call.receive<Crm>()
                }catch (e:Exception){
                    call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"missing Fields"))
                    return@post
                }

                try {
                    val email = call.principal<User>()!!.email
                    db.updateCrm(crm,email)
                    call.respond(HttpStatusCode.OK,SimpleResponse(true,"Crm Updated Successfully"))

                }catch (e:Exception){
                    call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message?:"some problem occount"))
                }

        }

        delete(DELETE_CRMS) {
            val crmId = try{
                call.request.queryParameters["id"]!!
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"QueryParamater: id is not fount"))
                return@delete
            }

            try {
                val email = call.principal<User>()!!.email
                db.deleteCrm(crmId,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Crm Deleted Successfully!"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some problem occount"))
            }
        }



    }

}