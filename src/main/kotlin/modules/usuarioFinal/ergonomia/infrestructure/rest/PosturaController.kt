package com.alilopez.modules.usuarioFinal.ergonomia.infrastructure.rest

import com.alilopez.modules.usuarioFinal.ergonomia.application.usecase.ObtenerProgresoSemanalUseCase
import com.alilopez.modules.usuarioFinal.ergonomia.application.usecase.RegistrarPosturaUseCase
import com.alilopez.modules.usuarioFinal.ergonomia.application.usecase.VerHistorialPosturaUseCase
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto.ErgonomiaErrorResponse
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto.ErgonomiaMensajeResponse
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto.RegistroPosturaRequest
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto.toResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class PosturaController(
    private val registrarPosturaUseCase: RegistrarPosturaUseCase,
    private val verHistorialPosturaUseCase: VerHistorialPosturaUseCase,
    private val obtenerProgresoSemanalUseCase: ObtenerProgresoSemanalUseCase
) {

    suspend fun sincronizarAlertas(call: ApplicationCall) {
        val idAutenticado = obtenerIdSolicitante(call)
        if (idAutenticado == 0) {
            return call.respond(
                HttpStatusCode.Unauthorized,
                ErgonomiaErrorResponse(code = "TOKEN_INVALIDO", message = "Token inválido o expirado.")
            )
        }

        try {
            val request = call.receive<RegistroPosturaRequest>()

            if (request.totalAlertas < 0) {
                return call.respond(
                    HttpStatusCode.BadRequest,
                    ErgonomiaErrorResponse(
                        code = "ALERTAS_INVALIDAS",
                        message = "Validación fallida",
                        details = mapOf("field" to "totalAlertas", "rule" to "no_puede_ser_negativo")
                    )
                )
            }

            val completado = registrarPosturaUseCase.ejecutar(idAutenticado, request.totalAlertas)

            if (completado) {
                call.respond(
                    HttpStatusCode.OK,
                    ErgonomiaMensajeResponse("Historial de postura cervical actualizado y acumulado con éxito.")
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErgonomiaErrorResponse(code = "ERROR_PROCESAMIENTO", message = "No se pudieron procesar las alertas de ergonomía.")
                )
            }

        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErgonomiaErrorResponse(code = "DATOS_INVALIDOS", message = e.message ?: "Datos de petición inválidos.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErgonomiaErrorResponse(code = "JSON_INVALIDO", message = "Formato JSON inválido.")
            )
        }
    }

    suspend fun verHistorial(call: ApplicationCall) {
        val idAutenticado = obtenerIdSolicitante(call)
        if (idAutenticado == 0) {
            return call.respond(
                HttpStatusCode.Unauthorized,
                ErgonomiaErrorResponse(code = "TOKEN_INVALIDO", message = "Token inválido o expirado.")
            )
        }

        try {
            val historial = verHistorialPosturaUseCase.ejecutar(idAutenticado)
            call.respond(HttpStatusCode.OK, historial.map { it.toResponse() })
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErgonomiaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al obtener el historial de ergonomía.")
            )
        }
    }

    suspend fun obtenerProgresoSemanal(call: ApplicationCall) {
        val idAutenticado = obtenerIdSolicitante(call)
        if (idAutenticado == 0) {
            return call.respond(
                HttpStatusCode.Unauthorized,
                ErgonomiaErrorResponse(code = "TOKEN_INVALIDO", message = "Token inválido o expirado.")
            )
        }

        try {
            val progreso = obtenerProgresoSemanalUseCase.ejecutar(idAutenticado)
            call.respond(HttpStatusCode.OK, progreso)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ErgonomiaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al procesar la gráfica de progreso.")
            )
        }
    }

    private fun obtenerIdSolicitante(call: ApplicationCall): Int {
        val principal = call.principal<JWTPrincipal>()
        return principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
    }
}