package com.alilopez.modules.usuarioFinal.tarea.application.usecase

import com.alilopez.modules.usuarioFinal.tarea.domain.model.TareaEnfoque
import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository

class CrearTareaUseCase(private val repository: TareaRepository) {
   fun execute(idUsuario: Int, idRol: Int, titulo: String, categoria: String, duracion: Int): TareaEnfoque {
        if (idRol != 2) throw IllegalAccessException("No tienes permisos para realizar esta acción.")
        if (titulo.isBlank()) throw IllegalArgumentException("El título de la tarea no puede estar vacío.")
        if (duracion < 20) throw IllegalArgumentException("La sesión de enfoque debe ser de al menos 20 minutos.")

        return repository.crearTarea(idUsuario, titulo, categoria, duracion)
   }
}