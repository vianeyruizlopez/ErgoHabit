package com.alilopez.modules.usuarioFinal.agua.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.aguaRouter(controller: AguaController) {
    authenticate("auth-jwt") {
        route("api/v1/habitos/agua") {

            get("/dashboard") {
                val principal = call.principal<JWTPrincipal>()
                val idUsuario = principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
                val idRol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                if (idRol != 2) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso denegado. Recurso exclusivo para usuarios finales."))
                    return@get
                }

                controller.verDashboard(call, idUsuario)
            }

            post("/toma") {
                val principal = call.principal<JWTPrincipal>()
                val idUsuario = principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
                val idRol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                if (idRol != 2) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso denegado. No tienes permisos para registrar tomas de agua."))
                    return@post
                }

                controller.registrarToma(call, idUsuario)
            }

            put("/meta") {
                val principal = call.principal<JWTPrincipal>()
                val idUsuario = principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
                val idRol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0

                if (idRol != 2) {
                    call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso denegado. No tienes autorización para modificar metas de hábitos."))
                    return@put
                }

                controller.guardarMeta(call, idUsuario)
            }
        }
    }
}