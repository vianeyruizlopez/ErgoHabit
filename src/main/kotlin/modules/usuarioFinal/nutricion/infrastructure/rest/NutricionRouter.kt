package com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.nutricionRouter(controller: NutricionController) {
    authenticate("auth-jwt") {
        route("/api/v1/habitos/nutricion") {

            get("/dashboard") {
                val (idUsuario, idRol) = extraerTokenNutricion(call)
                if (idRol != 2) return@get call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso Denegado. Rol insuficiente."))

                controller.verDashboard(call, idUsuario)
            }

            put("/horarios") {
                val (idUsuario, idRol) = extraerTokenNutricion(call)
                if (idRol != 2) return@put call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso Denegado. Solo estudiantes pueden modificar."))

                controller.guardarHorarios(call, idUsuario)
            }

            post("/marcar") {
                val (idUsuario, idRol) = extraerTokenNutricion(call)
                if (idRol != 2) return@post call.respond(HttpStatusCode.Forbidden, ErrorResponse("Acceso Denegado. No autorizado."))

                controller.marcarProgresoComida(call, idUsuario)
            }
        }
    }
}

private fun extraerTokenNutricion(call: io.ktor.server.application.ApplicationCall): Pair<Int, Int> {
    val principal = call.principal<JWTPrincipal>()
    val idUsuario = principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
    val idRol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0
    return Pair(idUsuario, idRol)
}