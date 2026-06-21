package com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest

import com.alilopez.modules.usuarioFinal.tarea.application.usecase.*
import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository
import com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.dto.*
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.MensajeResponse
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.ErrorResponse
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
            call.respond(HttpStatusCode.Forbidden, ErrorResponse(e.message ?: "Acceso denegado."))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al cargar tareas."))
        }
    }

    suspend fun agregarTarea(call: ApplicationCall, idUsuario: Int, idRol: Int) {
        try {
            val request = call.receive<TareaRequest>()
            crearTareaUseCase.execute(idUsuario, idRol, request.titulo, request.categoria, request.duracionTarea)
            call.respond(HttpStatusCode.Created, MensajeResponse("Tarea creada de forma exitosa."))
        } catch (e: IllegalAccessException) {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse(e.message ?: "No autorizado."))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Datos inválidos."))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error del servidor."))
        }
    }

    suspend fun iniciarCronometro(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val exito = iniciarCronometroUseCase.ejecutar(idTarea, idUsuario, idRol)
            if (exito) {
                call.respond(HttpStatusCode.OK, MensajeResponse("Cronómetro iniciado de forma exitosa. ¡A enfocar de manera saludable!"))
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("No se pudo iniciar el cronómetro."))
            }
        } catch (e: IllegalAccessException) {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse(e.message ?: "No autorizado."))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Acción inválida."))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al iniciar el cronómetro."))
        }
    }

    suspend fun marcarComoCompletada(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val exito = completarTareaUseCase.ejecutar(idTarea, idUsuario, idRol)
            if (exito) {
                val fraseFelicitacion = repository.obtenerFraseAleatoria("TAREA_EXITO")
                call.respond(HttpStatusCode.OK, MensajeResponse("¡Felicidades! Tarea completada con éxito. $fraseFelicitacion"))
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("No se encontró la tarea especificada."))
            }
        } catch (e: IllegalAccessException) {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse(e.message ?: "No autorizado."))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al actualizar la tarea."))
        }
    }

    suspend fun agregarTiempoExtra(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val request = call.receive<TiempoExtraRequest>()
            val exito = extenderTareaUseCase.ejecutar(idTarea, idUsuario, idRol, request.minutosExtra)
            if (exito) {
                val fraseAnimo = repository.obtenerFraseAleatoria("TAREA_PENDIENTE")
                call.respond(HttpStatusCode.OK, MensajeResponse("Tiempo de la sesión extendido correctamente. $fraseAnimo"))
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("No se pudo extender el tiempo."))
            }
        } catch (e: IllegalAccessException) {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse(e.message ?: "No autorizado."))
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Monto inválido."))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error de comunicación."))
        }
    }

    suspend fun pausarTarea(call: ApplicationCall, idUsuario: Int, idTarea: Int) {
        try {
            val exito = pausarTareaUseCase.ejecutar(idTarea, idUsuario)
            if (exito) {
                call.respond(HttpStatusCode.OK, MensajeResponse("Cronómetro pausado. Se guardó tu progreso de tiempo."))
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("No se pudo pausar la tarea."))
            }
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Acción inválida."))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al pausar la sesión."))
        }
    }

    suspend fun eliminarTarea(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val exito = eliminarTareaUseCase.ejecutar(idTarea, idUsuario, idRol)
            if (exito) {
                call.respond(HttpStatusCode.OK, MensajeResponse("Tarea eliminada correctamente."))
            } else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("No se encontró la tarea a eliminar."))
            }
        } catch (e: IllegalAccessException) {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse(e.message ?: "No autorizado."))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al borrar la tarea."))
        }
    }

    suspend fun obtenerDetalleCronometro(call: ApplicationCall, idUsuario: Int, idRol: Int, idTarea: Int) {
        try {
            val response = obtenerDetalleCronometroUseCase.execute(idTarea, idUsuario, idRol)
            call.respond(HttpStatusCode.OK, response)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message ?: "Tarea inválida."))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Error al obtener alertas."))
        }
    }
}