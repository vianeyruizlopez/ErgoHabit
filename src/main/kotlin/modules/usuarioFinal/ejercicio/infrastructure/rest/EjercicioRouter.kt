package com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ejercicioRouter(controller: EjercicioController) {
    authenticate("auth-jwt") {
        route("/api/v1/habitos/ejercicio") {

            get("/dashboard") {
                val (idUsuario, idRol) = extraerTokenEjercicio(call)
                if (idRol != 2) return@get call.respond(HttpStatusCode.Forbidden, ErrorResponse("Rol insuficiente."))
                controller.verDashboard(call, idUsuario)
            }

            get("/progreso-semanal") {
                val (idUsuario, idRol) = extraerTokenEjercicio(call)
                if (idRol != 2) return@get call.respond(HttpStatusCode.Forbidden, ErrorResponse("Rol insuficiente."))
                controller.verProgresoSemanal(call, idUsuario)
            }

            put("/meta") {
                val (idUsuario, idRol) = extraerTokenEjercicio(call)
                if (idRol != 2) return@put call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso exclusivo para estudiantes."))
                controller.actualizarMeta(call, idUsuario)
            }

            post("/recorrido") {
                val (idUsuario, idRol) = extraerTokenEjercicio(call)
                if (idRol != 2) return@post call.respond(HttpStatusCode.Forbidden, ErrorResponse("No autorizado."))
                controller.agregarProgreso(call, idUsuario)
            }
        }
    }
}

private fun extraerTokenEjercicio(call: io.ktor.server.application.ApplicationCall): Pair<Int, Int> {
    val principal = call.principal<JWTPrincipal>()
    val idUsuario = principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
    val idRol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0
    return Pair(idUsuario, idRol)
}