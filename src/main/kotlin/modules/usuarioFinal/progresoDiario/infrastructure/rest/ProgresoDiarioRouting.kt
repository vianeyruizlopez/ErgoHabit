package com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest

import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.dto.ProgresoDiarioErrorResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.progresoDiarioRouting(controller: ProgresoDiarioController) {
    authenticate("auth-jwt") {
        get("/api/v1/progresodiario/{idUsuario}") {
            val idUsuarioSolicitado = call.parameters["idUsuario"]?.toIntOrNull()
            if (idUsuarioSolicitado == null) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ProgresoDiarioErrorResponse(code = "ID_INVALIDO", message = "El ID de usuario es inválido o no fue enviado.")
                )
            }

            val principal = call.principal<JWTPrincipal>()
            val idUsuarioAutenticado = principal?.payload?.getClaim("idUsuario")?.asInt()
            if (idUsuarioAutenticado == null) {
                return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ProgresoDiarioErrorResponse(code = "TOKEN_AUTENTICACION_FALLIDO", message = "Token de usuario inválido.")
                )
            }

            val idRolAutenticado = principal?.payload?.getClaim("idRol")?.asInt()
            if (idRolAutenticado == null) {
                return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ProgresoDiarioErrorResponse(code = "ROL_NO_IDENTIFICADO", message = "No se pudo comprobar el rol del usuario.")
                )
            }

            controller.verProgreso(call, idUsuarioSolicitado, idUsuarioAutenticado, idRolAutenticado)
        }
    }
}