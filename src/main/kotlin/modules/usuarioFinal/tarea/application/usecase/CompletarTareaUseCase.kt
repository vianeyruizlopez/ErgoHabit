package com.alilopez.modules.usuarioFinal.tarea.application.usecase

import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository

class CompletarTareaUseCase(private val repository: TareaRepository) {

    fun ejecutar(idTarea: Int, idUsuario: Int, idRol: Int): Boolean {
        if (idRol != 2) throw IllegalAccessException("No autorizado.")

        val tarea = repository.buscarPorId(idTarea, idUsuario)
            ?: throw IllegalArgumentException("Tarea no encontrada o no tienes permiso para acceder a ella.")

        if (tarea.idEstado != 3) {
            throw IllegalArgumentException("Solo puedes terminar una tarea que está activa o en proceso.")
        }

        return repository.cambiarEstadoTarea(idTarea, idUsuario, nuevoEstado = 2)
    }
}