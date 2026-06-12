package com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest

import com.alilopez.modules.usuarioFinal.progresoDiario.application.usecase.VerProgresoDiarioUseCase
import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.dto.ProgresoDiarioResponse
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
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to e.message))
        } catch (e: IllegalAccessException) {
            call.respond(HttpStatusCode.Forbidden, mapOf("error" to e.message))
        } catch (e: NoSuchElementException) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to e.message))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error interno al procesar el progreso diario."))
        }
    }
}