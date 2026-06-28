package com.alilopez.modules.usuarioFinal.tarea.application.usecase

import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository
import java.time.Instant
import java.time.Duration

class PausarTareaUseCase(private val repository: TareaRepository) {

    fun ejecutar(idTarea: Int, idUsuario: Int): Boolean {
        val tarea = repository.buscarPorId(idTarea, idUsuario)
            ?: throw IllegalArgumentException("Tarea no encontrada.")

        val inicio = tarea.fechaInicioCronometro ?: Instant.now()
        val minutosConsumidos = Duration.between(inicio, Instant.now()).toMinutes().toInt()
        val nuevaDuracionRestante = maxOf(0, tarea.duracionTarea - minutosConsumidos)

        return repository.guardarPausaEnBaseDatos(idTarea, idUsuario, nuevaDuracionRestante)
    }
}