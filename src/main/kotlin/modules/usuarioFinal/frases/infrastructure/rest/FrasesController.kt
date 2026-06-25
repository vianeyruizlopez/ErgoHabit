package com.alilopez.modules.usuarioFinal.frases.infrastructure.rest

import com.alilopez.modules.usuarioFinal.frases.application.usecase.ObtenerFraseAleatoriaUseCase
import com.alilopez.modules.usuarioFinal.frases.infrastructure.rest.dto.FraseErrorResponse
import com.alilopez.modules.usuarioFinal.frases.infrastructure.rest.dto.FraseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class FrasesController(
    private val obtenerFraseAleatoriaUseCase: ObtenerFraseAleatoriaUseCase
) {
    suspend fun obtenerFraseAleatoria(call: ApplicationCall) {
        val categoria = call.parameters["categoria"]

        try {
            val frase = obtenerFraseAleatoriaUseCase.execute(categoria)
            call.respond(HttpStatusCode.OK, FraseResponse.fromDomain(frase))
        } catch (e: NoSuchElementException) {
            call.respond(
                HttpStatusCode.NotFound,
                FraseErrorResponse(code = "FRASE_NOT_FOUND", message = e.message ?: "No hay frases disponibles.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                FraseErrorResponse(code = "ERROR_SERVIDOR", message = "Error interno al obtener la frase motivacional.")
            )
        }
    }
}