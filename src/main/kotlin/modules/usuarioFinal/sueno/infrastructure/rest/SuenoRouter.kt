package com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.suenoRouter(controller: SuenoController) {
    authenticate("auth-jwt") {
        route("api/v1/habitos/sueno") {

            get("/dashboard") {
                val (idUsuario, idRol) = extraerToken(call)
                if (idRol != 2) return@get call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso Denegado"))

                controller.verDashboard(call, idUsuario)
            }

            put("/horario") {
                val (idUsuario, idRol) = extraerToken(call)
                if (idRol != 2) return@put call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso Denegado"))

                controller.guardarHorario(call, idUsuario)
            }

            post("/despertar") {
                val (idUsuario, idRol) = extraerToken(call)
                if (idRol != 2) return@post call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso Denegado"))

                controller.registrarDespertar(call, idUsuario)
            }
        }
    }
}

private fun extraerToken(call: io.ktor.server.application.ApplicationCall): Pair<Int, Int> {
    val principal = call.principal<JWTPrincipal>()
    val idUsuario = principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
    val idRol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0
    return Pair(idUsuario, idRol)
}