package com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest

import com.alilopez.modules.usuarioFinal.tarea.application.usecase.*
import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository
import com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.dto.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class TareaController(
    private val crearTareaUseCase: CrearTareaUseCase,
    private val listarTareasUseCase: ListarTareasUseCase,
    private val obtenerDetalleCronometroUseCase: ObtenerDetalleCronometroUseCase,
    private val iniciarCronometroUseCase: IniciarCronometroUseCase,
    private val completarTareaUseCase: CompletarTareaUseCase,
    private val extenderTareaUseCase: ExtenderTareaUseCase,
    private val pausarTareaUseCase: PausarTareaUseCase,
    private val eliminarTareaUseCase: EliminarTareaUseCase,
    private val repository: TareaRepository
) {

    suspend fun obtenerMisTareas(call: ApplicationCall, idUsuario: Int, idRol: Int) {
        try {
            val response = listarTareasUseCase.execute(idUsuario, idRol)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: IllegalAccessException) {
            call.respond(
                HttpStatusCode.Forbidden,
                TareaErrorResponse(code = "ACCESO_DENEGADO", message = e.message ?: "Acceso denegado.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                TareaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al cargar tareas.")
            )
        }
    }

    suspend fun agregarTarea(call: ApplicationCall, idUsuario: Int, idRol: Int) {
        try {
            val request = call.receive<TareaRequest>()

            if (request.titulo.isBlank()) {
                return call.respond(
                    HttpStatusCode.BadRequest,
                    TareaErrorResponse(code = "TITULO_REQUERIDO", message = "El título de la tarea no puede estar vacío.")
                )
            }

            crearTareaUseCase.execute(idUsuario, idRol, request.titulo, request.categoria, request.duracionTarea)
            call.respond(HttpStatusCode.Created, TareaMensajeResponse("Tarea creada de forma exitosa."))
        } catch (e: IllegalAccessException) {
            call.respond(
                HttpStatusCode.Forbidden,
                TareaErrorResponse(code = "NO_AUTORIZADO", message = e.message ?: "No autorizado.")
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                TareaErrorResponse(code = "DATOS_INVALIDOS", message = e.message ?: "Datos inválidos.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                TareaErrorResponse(
                    code = "ERROR_CREAR_TAREA",
                    message = e.message ?: "No se pudo crear la tarea."
                )
            )
        }
    }

    suspend fun iniciarCronometro(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val exito = iniciarCronometroUseCase.ejecutar(idTarea, idUsuario, idRol)
            if (exito) {
                call.respond(HttpStatusCode.OK, TareaMensajeResponse("Cronómetro iniciado de forma exitosa. ¡A enfocar de manera saludable!"))
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    TareaErrorResponse(code = "TAREA_NOT_FOUND", message = "No se pudo encontrar o iniciar el cronómetro de la tarea.")
                )
            }
        } catch (e: IllegalAccessException) {
            call.respond(
                HttpStatusCode.Forbidden,
                TareaErrorResponse(code = "NO_AUTORIZADO", message = e.message ?: "No autorizado.")
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                TareaErrorResponse(code = "ACCION_INVALIDA", message = e.message ?: "Acción inválida.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                TareaErrorResponse(code = "ERROR_CRONOMETRO", message = "Error al iniciar el cronómetro.")
            )
        }
    }

    suspend fun marcarComoCompletada(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val exito = completarTareaUseCase.ejecutar(idTarea, idUsuario, idRol)
            if (exito) {
                val fraseFelicitacion = repository.obtenerFraseAleatoria("TAREA_EXITO")
                call.respond(HttpStatusCode.OK, TareaMensajeResponse("¡Felicidades! Tarea completada con éxito. $fraseFelicitacion"))
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    TareaErrorResponse(code = "TAREA_NOT_FOUND", message = "No se encontró la tarea especificada.")
                )
            }
        } catch (e: IllegalAccessException) {
            call.respond(
                HttpStatusCode.Forbidden,
                TareaErrorResponse(code = "NO_AUTORIZADO", message = e.message ?: "No autorizado.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                TareaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al actualizar la tarea.")
            )
        }
    }

    suspend fun agregarTiempoExtra(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val request = call.receive<TiempoExtraRequest>()
            val exito = extenderTareaUseCase.ejecutar(idTarea, idUsuario, idRol, request.minutosExtra)
            if (exito) {
                val fraseAnimo = repository.obtenerFraseAleatoria("TAREA_PENDIENTE")
                call.respond(HttpStatusCode.OK, TareaMensajeResponse("Tiempo de la sesión extendido correctamente. $fraseAnimo"))
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    TareaErrorResponse(code = "TAREA_NOT_FOUND", message = "No se pudo extender el tiempo, tarea no encontrada.")
                )
            }
        } catch (e: IllegalAccessException) {
            call.respond(
                HttpStatusCode.Forbidden,
                TareaErrorResponse(code = "NO_AUTORIZADO", message = e.message ?: "No autorizado.")
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                TareaErrorResponse(code = "TIEMPO_INVALIDO", message = e.message ?: "Monto de tiempo inválido.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                TareaErrorResponse(code = "JSON_INVALIDO", message = "Formato de tiempo extra inválido.")
            )
        }
    }

    suspend fun pausarTarea(call: ApplicationCall, idUsuario: Int, idTarea: Int) {
        try {
            val exito = pausarTareaUseCase.ejecutar(idTarea, idUsuario)
            if (exito) {
                call.respond(HttpStatusCode.OK, TareaMensajeResponse("Cronómetro pausado. Se guardó tu progreso de tiempo."))
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    TareaErrorResponse(code = "TAREA_NOT_FOUND", message = "No se pudo pausar la tarea, id inválido.")
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                TareaErrorResponse(code = "ACCION_INVALIDA", message = e.message ?: "Acción inválida.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                TareaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al pausar la sesión.")
            )
        }
    }

    suspend fun eliminarTarea(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val exito = eliminarTareaUseCase.ejecutar(idTarea, idUsuario, idRol)
            if (exito) {
                call.respond(HttpStatusCode.OK, TareaMensajeResponse("Tarea eliminada correctamente."))
            } else {
                call.respond(
                    HttpStatusCode.NotFound,
                    TareaErrorResponse(code = "TAREA_NOT_FOUND", message = "No se encontró la tarea a eliminar.")
                )
            }
        } catch (e: IllegalAccessException) {
            call.respond(
                HttpStatusCode.Forbidden,
                TareaErrorResponse(code = "NO_AUTORIZADO", message = e.message ?: "No autorizado.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                TareaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al borrar la tarea.")
            )
        }
    }

    suspend fun obtenerDetalleCronometro(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val response = obtenerDetalleCronometroUseCase.execute(idTarea, idUsuario, idRol)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                TareaErrorResponse(code = "TAREA_INVALIDA", message = e.message ?: "Tarea inválida.")
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                TareaErrorResponse(code = "ERROR_SERVIDOR", message = "Error al obtener alertas de la tarea.")
            )
        }
    }
}