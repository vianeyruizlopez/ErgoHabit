package com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest

import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.ConfigurarHorariosNutricionUseCase
import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.MarcarComidaUseCase
import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.VerDashboardNutricionUseCase
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.dto.ConfigurarNutricionRequest
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.dto.MarcarComidaRequest
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.MensajeResponse
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
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
                ErrorResponse("Error al obtener el dashboard de nutrición: ${e.localizedMessage}")
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
                MensajeResponse("Tus horarios de alimentación han sido actualizados correctamente.")
            )
        } catch (e: DateTimeParseException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Formato de hora inválido. Usa 'HH:mm' (24 hrs)"))
        } catch (e: IllegalArgumentException) {
            // Atrapa el error si la comida o cena se intentan programar al revés cronológicamente
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Datos de horarios inválidos."))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error del servidor: ${e.localizedMessage}"))
        }
    }

    suspend fun marcarProgresoComida(call: ApplicationCall, idUsuario: Int) {
        try {
            val request = call.receive<MarcarComidaRequest>()

            val exito = marcarComidaUseCase.execute(idUsuario, request.tipoComida, request.estado)

            if (exito) {
                val accion = if (request.estado) "marcado" else "desmarcado"
                call.respond(
                    HttpStatusCode.OK,
                    MensajeResponse("Alimento de tipo '${request.tipoComida}' $accion con éxito.")
                )
            } else {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("No se pudo actualizar tu progreso de comida."))
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Parámetros inválidos."))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al registrar comida: ${e.localizedMessage}"))
        }
    }
}