package com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest

import com.alilopez.modules.usuarioFinal.progresoDiario.application.usecase.VerProgresoDiarioUseCase
import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.dto.ProgresoDiarioResponse
import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.dto.ProgresoDiarioErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class ProgresoDiarioController(
    private val verProgresoDiarioUseCase: VerProgresoDiarioUseCase
) {
    suspend fun verProgreso(call: ApplicationCall, idUsuarioSolicitado: Int, idUsuarioAutenticado: Int, idRolAutenticado: Int) {
        try {
            val progreso = verProgresoDiarioUseCase.execute(idUsuarioSolicitado, idUsuarioAutenticado, idRolAutenticado)
            val response = ProgresoDiarioResponse.fromDomain(progreso)

            call.respond(HttpStatusCode.OK, response)
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.Unauthorized,
                ProgresoDiarioErrorResponse(code = "TOKEN_NO_VALIDO", message = e.message ?: "No autorizado.")
            )
        } catch (e: IllegalAccessException) {
            call.respond(
                HttpStatusCode.Forbidden,
                ProgresoDiarioErrorResponse(code = "ACCESO_PROHIBIDO", message = e.message ?: "No tienes permisos para ver este progreso.")
            )
        } catch (e: NoSuchElementException) {
            call.respond(
                HttpStatusCode.NotFound,
                ProgresoDiarioErrorResponse(code = "PROGRESO_NOT_FOUND", message = e.message ?: "El progreso solicitado no existe.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ProgresoDiarioErrorResponse(code = "ERROR_SERVIDOR", message = "Error interno al procesar el progreso diario.")
            )
        }
    }
}