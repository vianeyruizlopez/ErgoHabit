package com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest

import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.ConfigurarMetaEjercicioUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.RegistrarKilometrosUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.VerDashboardEjercicioUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.dto.MetaEjercicioRequest
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.dto.RegistrarKmRequest
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.MensajeResponse
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.math.BigDecimal

class EjercicioController(
    private val configurarMetaUseCase: ConfigurarMetaEjercicioUseCase,
    private val registrarKilometrosUseCase: RegistrarKilometrosUseCase,
    private val verDashboardUseCase: VerDashboardEjercicioUseCase
) {

    suspend fun verDashboard(call: ApplicationCall, idUsuario: Int) {
        try {
            val response = verDashboardUseCase.execute(idUsuario)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error: ${e.localizedMessage}"))
        }
    }

    suspend fun actualizarMeta(call: ApplicationCall, idUsuario: Int) {
        try {
            val request = call.receive<MetaEjercicioRequest>()
            configurarMetaUseCase.execute(idUsuario, BigDecimal.valueOf(request.nuevaMeta))
            call.respond(HttpStatusCode.OK, MensajeResponse("Objetivo diario de kilómetros actualizado."))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Monto inválido."))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error de servidor."))
        }
    }

    suspend fun agregarProgreso(call: ApplicationCall, idUsuario: Int) {
        try {
            val request = call.receive<RegistrarKmRequest>()
            val exito = registrarKilometrosUseCase.execute(idUsuario, BigDecimal.valueOf(request.km))
            if (exito) {
                call.respond(HttpStatusCode.OK, MensajeResponse("Kilómetros acumulados correctamente hoy."))
            } else {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("No se pudo guardar el progreso."))
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Monto inválido."))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al procesar el sensor."))
        }
    }
}