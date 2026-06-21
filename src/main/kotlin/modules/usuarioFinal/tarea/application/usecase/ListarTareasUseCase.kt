package com.alilopez.modules.usuarioFinal.tarea.application.usecase

import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository
import com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.dto.TareaResponse
import com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.dto.TareasDashboardResponse

class ListarTareasUseCase(private val repository: TareaRepository) {
    fun execute(idUsuario: Int, idRol: Int): TareasDashboardResponse {
        if (idRol != 2) throw IllegalAccessException("Acceso denegado.")

        val pendientes = repository.obtenerTareasPendientes(idUsuario).map {
            TareaResponse(it.idTarea, it.titulo, it.categoria, "${it.duracionTarea} min", it.idEstado)
        }

        val completadas = repository.obtenerTareasCompletadasHoy(idUsuario).map {
            TareaResponse(it.idTarea, it.titulo, it.categoria, "${it.duracionTarea} min", it.idEstado)
        }

        return TareasDashboardResponse(
            totalPendientesText = "PENDIENTES · ${pendientes.size}",
            totalCompletadasText = "COMPLETADAS · ${completadas.size}",
            pendientes = pendientes,
            completadas = completadas
        )
    }
}