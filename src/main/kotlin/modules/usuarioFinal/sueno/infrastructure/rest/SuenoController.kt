package com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest

import com.alilopez.modules.usuarioFinal.sueno.application.usecase.ConfigurarHorarioSuenoUseCase
import com.alilopez.modules.usuarioFinal.sueno.application.usecase.RegistrarDespertarUseCase
import com.alilopez.modules.usuarioFinal.sueno.application.usecase.ObtenerProgresoSuenoUseCase
import com.alilopez.modules.usuarioFinal.sueno.application.usecase.VerDashboarSuenoUseCase
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.time.LocalTime
import java.time.format.DateTimeParseException

class SuenoController(
    private val configurarHorarioUseCase: ConfigurarHorarioSuenoUseCase,
    private val verDashboarSuenoUseCase: VerDashboarSuenoUseCase,
    private val registrarDespertarUseCase: RegistrarDespertarUseCase,
    private val obtenerProgresoSuenoUseCase: ObtenerProgresoSuenoUseCase
) {

    suspend fun verDashboard(call: ApplicationCall, idUsuario: Int) {
        try {
            val response = verDashboarSuenoUseCase.execute(idUsuario)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                SuenoErrorResponse(code = "ERROR_DASHBOARD", message = "Error al obtener el dashboard de sueño.")
            )
        }
    }

    suspend fun verProgresoSemanal(call: ApplicationCall, idUsuario: Int) {
        try {
            val response = obtenerProgresoSuenoUseCase.ejecutar(idUsuario)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                SuenoErrorResponse(code = "ERROR_PROGRESO", message = "Error al obtener el progreso semanal de sueño.")
            )
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
                SuenoMensajeResponse("Horario configurado con éxito. Dormirás un aproximado de $horasCalculadas horas.")
            )
        } catch (e: DateTimeParseException) {
            call.respond(
                HttpStatusCode.BadRequest,
                SuenoErrorResponse(
                    code = "FORMATO_HORA_INVALIDO",
                    message = "Validación fallida",
                    details = mapOf("error" to "Usa el formato de 24 horas 'HH:mm' (ej. 23:15)")
                )
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                SuenoErrorResponse(code = "DATOS_HORARIO_INVALIDOS", message = e.message ?: "Datos de horarios inválidos.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                SuenoErrorResponse(code = "JSON_INVALIDO", message = "Estructura JSON corrupta o inválida.")
            )
        }
    }

    suspend fun registrarDespertar(call: ApplicationCall, idUsuario: Int) {
        try {
            val horaActual = LocalTime.now(java.time.ZoneId.of("America/Mexico_City"))
            val exito = registrarDespertarUseCase.execute(idUsuario, horaActual)

            if (exito) {
                call.respond(
                    HttpStatusCode.OK,
                    SuenoMensajeResponse("¡Buenos días! Tu registro de sueño ha sido guardado.")
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    SuenoErrorResponse(code = "ERROR_BASE_DATOS", message = "No se pudo guardar tu progreso de sueño de hoy.")
                )
            }
        } catch (e: IllegalStateException) {
            call.respond(
                HttpStatusCode.BadRequest,
                SuenoErrorResponse(code = "ESTADO_INVALIDO", message = e.message ?: "Error en el flujo del estado de sueño.")
            )
        }
    }
}