package com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest

import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.ConfigurarHorariosNutricionUseCase
import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.MarcarComidaUseCase
import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.VerDashboardNutricionUseCase
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.dto.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.time.LocalTime
import java.time.format.DateTimeParseException

class NutricionController(
    private val configurarHorariosUseCase: ConfigurarHorariosNutricionUseCase,
    private val marcarComidaUseCase: MarcarComidaUseCase,
    private val verDashboardUseCase: VerDashboardNutricionUseCase
) {

    suspend fun verDashboard(call: ApplicationCall, idUsuario: Int) {
        try {
            val response = verDashboardUseCase.execute(idUsuario)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                NutricionErrorResponse(code = "ERROR_SERVIDOR", message = "Error al obtener el dashboard de nutrición.")
            )
        }
    }

    suspend fun guardarHorarios(call: ApplicationCall, idUsuario: Int) {
        try {
            val request = call.receive<ConfigurarNutricionRequest>()

            val desayunoParsed = LocalTime.parse(request.horaDesayuno)
            val comidaParsed = LocalTime.parse(request.horaComida)
            val cenaParsed = LocalTime.parse(request.horaCena)

            configurarHorariosUseCase.execute(idUsuario, desayunoParsed, comidaParsed, cenaParsed)

            call.respond(
                HttpStatusCode.OK,
                NutricionMensajeResponse("Tus horarios de alimentación han sido actualizados correctamente.")
            )
        } catch (e: DateTimeParseException) {
            call.respond(
                HttpStatusCode.BadRequest,
                NutricionErrorResponse(
                    code = "FORMATO_HORA_INVALIDO",
                    message = "Validación fallida",
                    details = mapOf("error" to "Usa formato de 24 horas 'HH:mm' (ej. 14:30)")
                )
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                NutricionErrorResponse(code = "HORARIOS_CRONOLOGICOS_INVALIDOS", message = e.message ?: "Datos de horarios inválidos.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                NutricionErrorResponse(code = "JSON_INVALIDO", message = "Formato JSON inválido u omitido.")
            )
        }
    }

    suspend fun marcarProgresoComida(call: ApplicationCall, idUsuario: Int) {
        try {
            val request = call.receive<MarcarComidaRequest>()

            if (request.tipoComida.isBlank()) {
                return call.respond(
                    HttpStatusCode.BadRequest,
                    NutricionErrorResponse(code = "TIPO_COMIDA_REQUERIDO", message = "El tipo de comida no puede estar vacío.")
                )
            }

            val exito = marcarComidaUseCase.execute(idUsuario, request.tipoComida, request.estado)

            if (exito) {
                val accion = if (request.estado) "marcado" else "desmarcado"
                call.respond(
                    HttpStatusCode.OK,
                    NutricionMensajeResponse("Alimento de tipo '${request.tipoComida}' $accion con éxito.")
                )
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    NutricionErrorResponse(code = "ERROR_BASE_DATOS", message = "No se pudo actualizar tu progreso de comida.")
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                NutricionErrorResponse(code = "PARAMETROS_INVALIDOS", message = e.message ?: "Parámetros inválidos.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                NutricionErrorResponse(code = "JSON_INVALIDO", message = "Error al procesar la solicitud de alimentos.")
            )
        }
    }
}