package com.alilopez.modules.usuarios.infrastructure.rest

import com.alilopez.modules.usuarios.application.usecase.ActualizarUseCase
import com.alilopez.modules.usuarios.application.usecase.EliminarUseCase
import com.alilopez.modules.usuarios.application.usecase.VerPerfilUseCase
import com.alilopez.modules.usuarios.application.usecase.VerTodoUseCase
import com.alilopez.modules.usuarios.application.usecase.ActualizarFotoPerfilUseCase
import com.alilopez.modules.usuarios.infrastructure.rest.dto.*
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
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                UsuarioErrorResponse(code = "ID_INVALIDO", message = "El ID de usuario proporcionado no es válido.")
            )

        try {
            val request = call.receive<UsuarioRequests>()
            val usuarioData = request.toDomain()

            val idAutenticado = obtenerIdSolicitante(call)

            val resultado = actualizarUseCase.execute(idAActualizar, usuarioData, idAutenticado)
            call.respond(HttpStatusCode.OK, resultado.toResponse())

        } catch (e: SecurityException) {
            call.respond(
                HttpStatusCode.Forbidden,
                UsuarioErrorResponse(code = "ACCESO_DENEGADO", message = e.message ?: "No tienes permisos para modificar este perfil.")
            )
        } catch (e: NoSuchElementException) {
            call.respond(
                HttpStatusCode.NotFound,
                UsuarioErrorResponse(code = "USUARIO_NOT_FOUND", message = e.message ?: "El usuario a actualizar no existe.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                UsuarioErrorResponse(code = "ERROR_SERVIDOR", message = "Error interno al intentar actualizar los datos.")
            )
        }
    }

    suspend fun actualizarFotoPerfil(call: ApplicationCall) {
        val idAutenticado = obtenerIdSolicitante(call)
        if (idAutenticado == 0) {
            return call.respond(
                HttpStatusCode.Unauthorized,
                UsuarioErrorResponse(code = "TOKEN_INVALIDO", message = "Token inválido o sesión expirada.")
            )
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
                    FotoPerfilResponse(mensaje = "Foto de perfil actualizada con éxito", url = urlFotoResultante!!)
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    UsuarioErrorResponse(code = "ARCHIVO_REQUERIDO", message = "No se proporcionó ningún archivo de imagen válido.")
                )
            }

        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                UsuarioErrorResponse(code = "FORMATO_INVALIDO", message = e.message ?: "Estructura o formato de imagen no admitido.")
            )
        } catch (e: SecurityException) {
            call.respond(
                HttpStatusCode.Forbidden,
                UsuarioErrorResponse(code = "NO_AUTORIZADO", message = e.message ?: "No tienes autorización para realizar esta acción.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                UsuarioErrorResponse(code = "ERROR_MULTIMEDIA", message = "Error del servidor al procesar la foto de perfil.")
            )
        }
    }

    suspend fun eliminar(call: ApplicationCall) {
        val idAEliminar = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                UsuarioErrorResponse(code = "ID_INVALIDO", message = "El ID de usuario proporcionado no es válido.")
            )

        val idAutenticado = obtenerIdSolicitante(call)
        val rolAutenticado = obtenerRolSolicitante(call)

        try {
            eliminarUseCase.execute(idAEliminar, idAutenticado, rolAutenticado)
            call.respond(HttpStatusCode.OK, UsuarioMensajeResponse("Usuario eliminado correctamente."))
        } catch (e: SecurityException) {
            call.respond(
                HttpStatusCode.Forbidden,
                UsuarioErrorResponse(code = "ACCESO_DENEGADO", message = e.message ?: "No posees los permisos requeridos para eliminar esta cuenta.")
            )
        } catch (e: NoSuchElementException) {
            call.respond(
                HttpStatusCode.NotFound,
                UsuarioErrorResponse(code = "USUARIO_NOT_FOUND", message = e.message ?: "El usuario solicitado no existe.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                UsuarioErrorResponse(code = "ERROR_SERVIDOR", message = "Error interno al procesar la eliminación.")
            )
        }
    }

    suspend fun verPerfil(call: ApplicationCall) {
        val idConsultado = call.parameters["id"]?.toIntOrNull()
            ?: return call.respond(
                HttpStatusCode.BadRequest,
                UsuarioErrorResponse(code = "ID_INVALIDO", message = "El ID de usuario proporcionado no es válido.")
            )

        val idAutenticado = obtenerIdSolicitante(call)

        try {
            val usuario = verPerfilUseCase.execute(idConsultado, idAutenticado)
            call.respond(HttpStatusCode.OK, usuario.toResponse())
        } catch (e: SecurityException) {
            call.respond(
                HttpStatusCode.Forbidden,
                UsuarioErrorResponse(code = "ACCESO_DENEGADO", message = e.message ?: "No estás autorizado para consultar este perfil.")
            )
        } catch (e: NoSuchElementException) {
            call.respond(
                HttpStatusCode.NotFound,
                UsuarioErrorResponse(code = "USUARIO_NOT_FOUND", message = e.message ?: "Perfil de usuario no localizado.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                UsuarioErrorResponse(code = "ERROR_SERVIDOR", message = "Error interno al obtener los detalles del perfil.")
            )
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
            call.respond(
                HttpStatusCode.Forbidden,
                UsuarioErrorResponse(code = "ACCESO_DENEGADO", message = e.message ?: "Acceso denegado. Rol insuficiente.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                UsuarioErrorResponse(code = "ERROR_SERVIDOR", message = "Error al consultar la lista de usuarios.")
            )
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