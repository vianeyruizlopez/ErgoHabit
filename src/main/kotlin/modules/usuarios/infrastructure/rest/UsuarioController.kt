package com.alilopez.modules.usuarios.infrastructure.rest

import com.alilopez.modules.usuarios.application.usecase.ActualizarUseCase
import com.alilopez.modules.usuarios.application.usecase.EliminarUseCase
import com.alilopez.modules.usuarios.application.usecase.VerPerfilUseCase
import com.alilopez.modules.usuarios.application.usecase.VerTodoUseCase
import com.alilopez.modules.usuarios.application.usecase.ActualizarFotoPerfilUseCase
import com.alilopez.modules.usuarios.infrastructure.rest.dto.UsuarioRequests
import com.alilopez.modules.usuarios.infrastructure.rest.dto.toResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond

class UsuarioController(
    private val actualizarUseCase: ActualizarUseCase,
    private val eliminarUseCase: EliminarUseCase,
    private val verPerfilUseCase: VerPerfilUseCase,
    private val verTodoUseCase: VerTodoUseCase,
    private val actualizarFotoPerfilUseCase: ActualizarFotoPerfilUseCase
) {
    suspend fun actualizar(call: ApplicationCall) {
        val idAActualizar = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "ID inválido")

        try {
            val request = call.receive<UsuarioRequests>()
            val usuarioData = request.toDomain()

            val idAutenticado = obtenerIdSolicitante(call)

            val resultado = actualizarUseCase.execute(idAActualizar, usuarioData, idAutenticado)
            call.respond(HttpStatusCode.OK, resultado.toResponse())

        } catch (e: SecurityException) {
            call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
        } catch (e: NoSuchElementException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "No encontrado")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error al actualizar")
        }
    }

    suspend fun actualizarFotoPerfil(call: ApplicationCall) {
        val idAutenticado = obtenerIdSolicitante(call)
        if (idAutenticado == 0) {
            return call.respond(HttpStatusCode.Unauthorized, "Token inválido o expirado")
        }

        try {
            val multipart = call.receiveMultipart()
            var urlFotoResultante: String? = null

            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    part.streamProvider().use { inputStream ->
                        urlFotoResultante = actualizarFotoPerfilUseCase.ejecutar(idAutenticado, inputStream)
                    }
                }
                part.dispose()
            }

            if (urlFotoResultante != null) {
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("mensaje" to "Foto de perfil actualizada con éxito", "url" to urlFotoResultante)
                )
            } else {
                call.respond(HttpStatusCode.BadRequest, "No se proporcionó ningún archivo de imagen válido")
            }

        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "Datos inválidos")
        } catch (e: SecurityException) {
            call.respond(HttpStatusCode.Forbidden, e.message ?: "No autorizado")
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error del servidor al procesar la foto de perfil")
        }
    }

    suspend fun eliminar(call: ApplicationCall) {
        val idAEliminar = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "ID inválido")

        val idAutenticado = obtenerIdSolicitante(call)
        val rolAutenticado = obtenerRolSolicitante(call)

        try {
            eliminarUseCase.execute(idAEliminar, idAutenticado, rolAutenticado)
            call.respond(HttpStatusCode.OK, "Usuario eliminado correctamente")
        } catch (e: SecurityException) {
            call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
        } catch (e: NoSuchElementException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "No encontrado")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error al eliminar")
        }
    }

    suspend fun verPerfil(call: ApplicationCall) {
        val idConsultado = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(HttpStatusCode.BadRequest, "ID inválido")

        val idAutenticado = obtenerIdSolicitante(call)

        try {
            val usuario = verPerfilUseCase.execute(idConsultado, idAutenticado)
            call.respond(HttpStatusCode.OK, usuario.toResponse())
        } catch (e: SecurityException) {
            call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
        } catch (e: NoSuchElementException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "No encontrado")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error interno del servidor")
        }
    }

    suspend fun verTodos(call: ApplicationCall) {
        val rolAutenticado = obtenerRolSolicitante(call)

        val rutaActual = call.request.local.uri
        val filtroTipo = when {
            rutaActual.contains("admi") -> 1
            rutaActual.contains("usuario") -> 2
            else -> -1
        }

        try {
            val lista = verTodoUseCase.execute(rolAutenticado, filtroTipo)
            call.respond(HttpStatusCode.OK, lista.map { it.toResponse() })
        } catch (e: SecurityException) {
            call.respond(HttpStatusCode.Forbidden, e.message ?: "Acceso denegado")
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Error al consultar la lista")
        }
    }

    private fun obtenerIdSolicitante(call: ApplicationCall): Int {
        val principal = call.principal<JWTPrincipal>()
        return principal?.payload?.getClaim("idUsuario")?.asInt() ?: 0
    }

    private fun obtenerRolSolicitante(call: ApplicationCall): Int {
        val principal = call.principal<JWTPrincipal>()
        return principal?.payload?.getClaim("idRol")?.asInt() ?: 0
    }
}