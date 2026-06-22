package com.alilopez.modules.usuarioFinal.ergonomia.infrastructure.rest

import com.alilopez.modules.usuarioFinal.ergonomia.application.usecase.RegistrarPosturaUseCase
import com.alilopez.modules.usuarioFinal.ergonomia.application.usecase.VerHistorialPosturaUseCase
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
    private val verHistorialPosturaUseCase: VerHistorialPosturaUseCase
) {

    suspend fun sincronizarAlertas(call: ApplicationCall) {
        val idAutenticado = obtenerIdSolicitante(call)
        if (idAutenticado == 0) {
            return call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token inválido o expirado."))
        }

        try {
            val request = call.receive<RegistroPosturaRequest>()
            val completado = registrarPosturaUseCase.ejecutar(idAutenticado, request.totalAlertas)

            if (completado) {
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("mensaje" to "Historial de postura cervical actualizado y acumulado con éxito.")
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "No se pudieron procesar las alertas de ergonomía.")
                )
            }

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Datos de petición inválidos.")))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error interno del servidor al registrar la postura."))
        }
    }

    suspend fun verHistorial(call: ApplicationCall) {
        val idAutenticado = obtenerIdSolicitante(call)
        if (idAutenticado == 0) {
            return call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token inválido o expirado."))
        }

        try {
            val historial = verHistorialPosturaUseCase.ejecutar(idAutenticado)
            call.respond(HttpStatusCode.OK, historial.map { it.toResponse() })
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener el historial de ergonomía."))
        }
    }

    private fun obtenerIdSolicitante(call: ApplicationCall): Int {
        val principal = call.principal<JWTPrincipal>()
        return principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
    }
}