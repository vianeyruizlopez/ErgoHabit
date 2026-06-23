package com.alilopez.modules.usuarioFinal.agua.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.application.usecase.ConfigurarMetaUseCase
import com.alilopez.modules.usuarioFinal.agua.application.usecase.ObtenerDashboardAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.application.usecase.ObtenerProgresoAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.application.usecase.RegistrarTomaAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class AguaController(
    private val obtenerDashboardUseCase: ObtenerDashboardAguaUseCase,
    private val registrarTomaUseCase: RegistrarTomaAguaUseCase,
    private val configurarMetaUseCase: ConfigurarMetaUseCase,
    private val obtenerProgresoAguaUseCase: ObtenerProgresoAguaUseCase
) {
    suspend fun verDashboard(call: ApplicationCall, idUsuarioAutenticado: Int) {
        try {
            val dashboard = obtenerDashboardUseCase.execute(idUsuarioAutenticado)
            call.respond(HttpStatusCode.OK, dashboard)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                AguaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al obtener el dashboard.")
            )
        }
    }

    suspend fun verProgresoSemanal(call: ApplicationCall, idUsuarioAutenticado: Int) {
        try {
            val response = obtenerProgresoAguaUseCase.ejecutar(idUsuarioAutenticado)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                AguaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al obtener el progreso semanal: ${e.localizedMessage}")
            )
        }
    }

    suspend fun registrarToma(call: ApplicationCall, idUsuarioAutenticado: Int) {
        try {
            val request = call.receive<RegistrarTomaRequest>()

            if (request.cantidadMl <= 0) {
                return call.respond(
                    HttpStatusCode.BadRequest,
                    AguaErrorResponse(
                        code = "CANTIDAD_INVALIDA",
                        message = "Validación fallida",
                        details = mapOf("field" to "cantidadMl", "rule" to "debe_ser_mayor_a_cero")
                    )
                )
            }

            if (registrarTomaUseCase.execute(idUsuarioAutenticado, request.cantidadMl)) {
                call.respond(HttpStatusCode.Created, MensajeResponse("Toma registrada con éxito."))
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    AguaErrorResponse(code = "ERROR_BASE_DATOS", message = "No se pudo registrar la toma en la base de datos.")
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                AguaErrorResponse(code = "DATOS_INVALIDOS", message = e.message ?: "Datos inválidos")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                AguaErrorResponse(code = "JSON_INVALIDO", message = "Formato JSON inválido: ${e.localizedMessage}")
            )
        }
    }

    suspend fun guardarMeta(call: ApplicationCall, idUsuarioAutenticado: Int) {
        try {
            val request = call.receive<ConfigurarMetaRequest>()

            val exito = if (request.tipo.uppercase() == "PESO") {
                configurarMetaUseCase.porPesoYEstatura(idUsuarioAutenticado, request.peso, request.estatura)
            } else {
                configurarMetaUseCase.manual(idUsuarioAutenticado, request.metaMl)
            }

            if (exito) {
                call.respond(HttpStatusCode.OK, MensajeResponse("Meta actualizada con éxito."))
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    AguaErrorResponse(code = "ERROR_BASE_DATOS", message = "Error al actualizar la meta en la base de datos.")
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                AguaErrorResponse(code = "DATOS_INVALIDOS", message = e.message ?: "Datos de configuración inválidos")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                AguaErrorResponse(code = "JSON_INVALIDO", message = "Formato JSON inválido: ${e.localizedMessage}")
            )
        }
    }
}