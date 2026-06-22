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
        val dashboard = obtenerDashboardUseCase.execute(idUsuarioAutenticado)
        call.respond(HttpStatusCode.OK, dashboard)
    }

    suspend fun verProgresoSemanal(call: ApplicationCall, idUsuarioAutenticado: Int) {
        try {
            val response = obtenerProgresoAguaUseCase.ejecutar(idUsuarioAutenticado)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al obtener el progreso semanal: ${e.localizedMessage}"))
        }
    }

    suspend fun registrarToma(call: ApplicationCall, idUsuarioAutenticado: Int) {
        try {
            val request = call.receive<RegistrarTomaRequest>()

            if (registrarTomaUseCase.execute(idUsuarioAutenticado, request.cantidadMl)) {
                call.respond(HttpStatusCode.Created, MensajeResponse("Toma registrada con éxito."))
            } else {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("No se pudo registrar la toma en la base de datos."))
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Datos inválidos"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Formato JSON inválido: ${e.localizedMessage}"))
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
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al actualizar la meta en la base de datos."))
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Datos de configuración inválidos"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Formato JSON inválido: ${e.localizedMessage}"))
        }
    }
}