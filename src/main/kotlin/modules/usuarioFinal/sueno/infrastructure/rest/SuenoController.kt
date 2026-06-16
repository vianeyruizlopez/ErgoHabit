package com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest

import com.alilopez.modules.usuarioFinal.sueno.application.usecase.ConfigurarHorarioSuenoUseCase
import com.alilopez.modules.usuarioFinal.sueno.application.usecase.RegistrarDespertarUseCase
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.SuenoRequest
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.MensajeResponse
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
import com.alilopez.modules.usuarioFinal.sueno.application.usecase.VerDashboarSuenoUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.time.LocalTime
import java.time.format.DateTimeParseException

class SuenoController(
    private val configurarHorarioUseCase: ConfigurarHorarioSuenoUseCase,
    private val verDashboarSuenoUseCase: VerDashboarSuenoUseCase,
    private val registrarDespertarUseCase: RegistrarDespertarUseCase
) {

    suspend fun verDashboard(call: ApplicationCall, idUsuario: Int) {
        try {
            val response = verDashboarSuenoUseCase.execute(idUsuario)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al obtener dashboard: ${e.localizedMessage}"))
        }
    }

    suspend fun guardarHorario(call: ApplicationCall, idUsuario: Int) {
        try {
            val request = call.receive<SuenoRequest>()
            val horaDespertarParsed = LocalTime.parse(request.horaDespertar)
            val horaDormirParsed = LocalTime.parse(request.horaDormir)

            val horasCalculadas = configurarHorarioUseCase.execute(idUsuario, horaDespertarParsed, horaDormirParsed)

            call.respond(
                HttpStatusCode.OK,
                MensajeResponse("Horario configurado. Dormirás $horasCalculadas horas.")
            )
        } catch (e: DateTimeParseException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Formato inválido. Usa 'HH:mm'"))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Datos inválidos"))
        }
    }

    suspend fun registrarDespertar(call: ApplicationCall, idUsuario: Int) {
        try {
            val horaActual = LocalTime.now()
            val exito = registrarDespertarUseCase.execute(idUsuario, horaActual)

            if (exito) {
                call.respond(HttpStatusCode.OK, MensajeResponse("¡Buenos días! Tu registro de sueño ha sido guardado."))
            } else {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("No se pudo guardar tu progreso de sueño."))
            }
        } catch (e: IllegalStateException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Error de estado"))
        }
    }
}