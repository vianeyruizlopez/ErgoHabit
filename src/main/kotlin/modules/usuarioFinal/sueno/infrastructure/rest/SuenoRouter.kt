package com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest

import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.SuenoErrorResponse
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
                if (idRol != 2) {
                    return@get call.respond(
                        HttpStatusCode.Forbidden,
                        SuenoErrorResponse(code = "ACCESO_DENEGADO", message = "Acceso Denegado. Rol insuficiente.")
                    )
                }

                controller.verDashboard(call, idUsuario)
            }

            get("/progreso-semanal") {
                val (idUsuario, idRol) = extraerToken(call)
                if (idRol != 2) {
                    return@get call.respond(
                        HttpStatusCode.Forbidden,
                        SuenoErrorResponse(code = "ACCESO_DENEGADO", message = "Acceso Denegado. Rol insuficiente.")
                    )
                }

                controller.verProgresoSemanal(call, idUsuario)
            }

            put("/horario") {
                val (idUsuario, idRol) = extraerToken(call)
                if (idRol != 2) {
                    return@put call.respond(
                        HttpStatusCode.Forbidden,
                        SuenoErrorResponse(code = "ACCESO_DENEGADO", message = "Acceso Denegado. Solo estudiantes pueden modificar.")
                    )
                }

                controller.guardarHorario(call, idUsuario)
            }

            post("/despertar") {
                val (idUsuario, idRol) = extraerToken(call)
                if (idRol != 2) {
                    return@post call.respond(
                        HttpStatusCode.Forbidden,
                        SuenoErrorResponse(code = "NO_AUTORIZADO", message = "Acceso Denegado. No autorizado.")
                    )
                }

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