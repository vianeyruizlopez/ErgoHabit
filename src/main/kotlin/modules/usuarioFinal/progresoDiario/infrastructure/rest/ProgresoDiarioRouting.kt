package com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.progresoDiarioRouting(controller: ProgresoDiarioController) {
    authenticate("auth-jwt") {
        get("/api/v1/progresodiario/{idUsuario}") {
            val idUsuarioSolicitado = call.parameters["idUsuario"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "El ID de usuario es inválido o no fue enviado."))

            val principal = call.principal<JWTPrincipal>()
            val idUsuarioAutenticado = principal?.payload?.getClaim("idUsuario")?.asInt()
                ?: return@get call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token de usuario inválido."))

            val idRolAutenticado = principal?.payload?.getClaim("idRol")?.asInt()
                ?: return@get call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "No se pudo comprobar el rol del usuario."))

            controller.verProgreso(call, idUsuarioSolicitado, idUsuarioAutenticado, idRolAutenticado)
        }
    }
}