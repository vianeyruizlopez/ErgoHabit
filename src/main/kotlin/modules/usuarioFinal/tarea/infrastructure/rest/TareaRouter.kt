package com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tareaRouter(controller: TareaController) {
    authenticate("auth-jwt") {
        route("/api/v1/tareas") {

            get {
                val (idUsuario, idRol) = extraerTokenTarea(call)
                controller.obtenerMisTareas(call, idUsuario, idRol)
            }

            post {
                val (idUsuario, idRol) = extraerTokenTarea(call)
                controller.agregarTarea(call, idUsuario, idRol)
            }

            patch("/{idTarea}/iniciar") {
                val (idUsuario, idRol) = extraerTokenTarea(call)
                val idTarea = call.parameters["idTarea"]?.toIntOrNull()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID de tarea inválido."))

                controller.iniciarCronometro(call, idUsuario, idRol, idTarea)
            }

            patch("/{idTarea}/pausar") {
                val (idUsuario, _) = extraerTokenTarea(call)
                val idTarea = call.parameters["idTarea"]?.toIntOrNull()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID de tarea inválido."))

                controller.pausarTarea(call, idUsuario, idTarea)
            }

            patch("/{idTarea}/completar") {
                val (idUsuario, idRol) = extraerTokenTarea(call)
                val idTarea = call.parameters["idTarea"]?.toIntOrNull()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID de tarea inválido."))

                controller.marcarComoCompletada(call, idUsuario, idRol, idTarea)
            }

            patch("/{idTarea}/extender") {
                val (idUsuario, idRol) = extraerTokenTarea(call)
                val idTarea = call.parameters["idTarea"]?.toIntOrNull()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID de tarea inválido."))

                controller.agregarTiempoExtra(call, idUsuario, idRol, idTarea)
            }

            get("/{idTarea}/cronometro") {
                val (idUsuario, idRol) = extraerTokenTarea(call)
                val idTarea = call.parameters["idTarea"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID de tarea inválido."))

                controller.obtenerDetalleCronometro(call, idUsuario, idRol, idTarea)
            }

            delete("/{idTarea}") {
                val (idUsuario, idRol) = extraerTokenTarea(call)
                val idTarea = call.parameters["idTarea"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID de tarea inválido."))

                controller.eliminarTarea(call, idUsuario, idRol, idTarea)
            }
        }
    }
}

private fun extraerTokenTarea(call: ApplicationCall): Pair<Int, Int> {
    val principal = call.principal<JWTPrincipal>()
    val idUsuario = principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
    val idRol = principal?.payload?.getClaim("idRol")?.asInt() ?: 0
    return Pair(idUsuario, idRol)
}