package com.alilopez.modules.usuarioFinal.tarea.application.usecase

import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository

class EliminarTareaUseCase(private val repository: TareaRepository) {

    fun ejecutar(idTarea: Int, idUsuario: Int, idRol: Int): Boolean {
        if (idRol != 2) throw IllegalAccessException("No autorizado.")
        return repository.eliminarTarea(idTarea, idUsuario)
    }
}