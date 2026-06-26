package com.alilopez.modules.usuarioFinal.tarea.application.usecase

import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository

class ExtenderTareaUseCase(private val repository: TareaRepository) {

    fun ejecutar(idTarea: Int, idUsuario: Int, idRol: Int, minutosExtra: Int): Boolean {
        if (idRol != 2) throw IllegalAccessException("No autorizado.")
        if (minutosExtra <= 0) throw IllegalArgumentException("Los minutos a agregar deben ser positivos.")

        val tarea = repository.buscarPorId(idTarea, idUsuario)
            ?: throw IllegalArgumentException("No se encontró la tarea especificada.")

        if (tarea.idEstado != 1 || tarea.fechaInicioCronometro == null) {
            throw IllegalArgumentException("No puedes extender el tiempo de una tarea que no ha iniciado su cronómetro.")
        }

        val nuevaDuracionProyectada = tarea.duracionTarea + minutosExtra
        val topeMaximoPermitido = tarea.duracionInicial + 120

        if (nuevaDuracionProyectada > topeMaximoPermitido) {
            val minutosDisponibles = topeMaximoPermitido - tarea.duracionTarea
            throw IllegalArgumentException(
                "Límite excedido. El límite de tiempo extra son 2 horas sobre el tiempo inicial. Solo te quedan $minutosDisponibles minutos disponibles."
            )
        }

        return repository.agregarMinutosTarea(idTarea, idUsuario, minutosExtra)
    }
}