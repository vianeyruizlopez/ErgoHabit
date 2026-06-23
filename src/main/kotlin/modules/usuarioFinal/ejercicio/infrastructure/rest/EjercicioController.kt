package com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest

import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.ConfigurarMetaEjercicioUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.RegistrarKilometrosUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.VerDashboardEjercicioUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.ObtenerProgresoEjercicioUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.dto.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.math.BigDecimal

class EjercicioController(
    private val configurarMetaUseCase: ConfigurarMetaEjercicioUseCase,
    private val registrarKilometrosUseCase: RegistrarKilometrosUseCase,
    private val verDashboardUseCase: VerDashboardEjercicioUseCase,
    private val obtenerProgresoEjercicioUseCase: ObtenerProgresoEjercicioUseCase
) {

    suspend fun verDashboard(call: ApplicationCall, idUsuario: Int) {
        try {
            val response = verDashboardUseCase.execute(idUsuario)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                EjercicioErrorResponse(code = "ERROR_SERVIDOR", message = "Error al obtener el dashboard.")
            )
        }
    }

    suspend fun verProgresoSemanal(call: ApplicationCall, idUsuario: Int) {
        try {
            val response = obtenerProgresoEjercicioUseCase.ejecutar(idUsuario)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                EjercicioErrorResponse(code = "ERROR_SERVIDOR", message = "Error al obtener el progreso semanal: ${e.localizedMessage}")
            )
        }
    }

    suspend fun actualizarMeta(call: ApplicationCall, idUsuario: Int) {
        try {
            val request = call.receive<MetaEjercicioRequest>()

            if (request.nuevaMeta <= 0.0) {
                return call.respond(
                    HttpStatusCode.BadRequest,
                    EjercicioErrorResponse(
                        code = "META_INVALIDA",
                        message = "Validación fallida",
                        details = mapOf("field" to "nuevaMeta", "rule" to "debe_ser_mayor_a_cero")
                    )
                )
            }

            configurarMetaUseCase.execute(idUsuario, BigDecimal.valueOf(request.nuevaMeta))
            call.respond(HttpStatusCode.OK, MensajeResponse("Objetivo diario de kilómetros actualizado."))
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                EjercicioErrorResponse(code = "DATOS_INVALIDOS", message = e.message ?: "Monto inválido.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                EjercicioErrorResponse(code = "JSON_INVALIDO", message = "Formato JSON inválido.")
            )
        }
    }

    suspend fun agregarProgreso(call: ApplicationCall, idUsuario: Int) {
        try {
            val request = call.receive<RegistrarKmRequest>()

            if (request.km < 0.0) {
                return call.respond(
                    HttpStatusCode.BadRequest,
                    EjercicioErrorResponse(
                        code = "KILOMETROS_INVALIDOS",
                        message = "Validación fallida",
                        details = mapOf("field" to "km", "rule" to "no_puede_ser_negativo")
                    )
                )
            }

            val exito = registrarKilometrosUseCase.execute(idUsuario, BigDecimal.valueOf(request.km))
            if (exito) {
                call.respond(HttpStatusCode.OK, MensajeResponse("Kilómetros acumulados correctamente hoy."))
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    EjercicioErrorResponse(code = "ERROR_BASE_DATOS", message = "No se pudo guardar el progreso.")
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                EjercicioErrorResponse(code = "DATOS_INVALIDOS", message = e.message ?: "Monto inválido.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                EjercicioErrorResponse(code = "JSON_INVALIDO", message = "Error al procesar la solicitud del sensor.")
            )
        }
    }
}