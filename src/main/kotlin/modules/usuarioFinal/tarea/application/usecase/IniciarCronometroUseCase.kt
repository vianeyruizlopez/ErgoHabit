package com.alilopez.modules.usuarioFinal.tarea.application.usecase

import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository

class IniciarCronometroUseCase(private val repository: TareaRepository) {

    fun ejecutar(idTarea: Int, idUsuario: Int, idRol: Int): Boolean {
        if (idRol != 2) throw IllegalAccessException("No autorizado.")

        val tarea = repository.buscarPorId(idTarea, idUsuario)
            ?: throw IllegalArgumentException("Tarea no encontrada.")

        if (tarea.idEstado == 2) {
            throw IllegalArgumentException("No puedes iniciar una tarea que ya está completada.")
        }

        val tareas = repository.obtenerTareasPendientes(idUsuario)
        val tieneCronometroActivo = tareas.any { it.idEstado == 3 && it.idTarea != idTarea }
        if (tieneCronometroActivo) {
            throw IllegalArgumentException("Ya tienes un cronómetro en progreso. Pausa o termina tu sesión actual antes de iniciar otra.")
        }

        return repository.iniciarCronometroEnBaseDatos(idTarea, idUsuario)
    }
}