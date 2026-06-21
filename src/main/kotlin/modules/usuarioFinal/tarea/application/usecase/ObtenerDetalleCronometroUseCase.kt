package com.alilopez.modules.usuarioFinal.tarea.application.usecase

import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository
import com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.dto.DetalleTareaResponse

class ObtenerDetalleCronometroUseCase(private val repository: TareaRepository) {
    fun execute(idTarea: Int, idUsuario: Int, idRol: Int): DetalleTareaResponse {
        if (idRol != 2) throw IllegalAccessException("No autorizado.")

        val tarea = repository.buscarPorId(idTarea, idUsuario)
            ?: throw IllegalArgumentException("Tarea no encontrada.")

        val esCategoriaRiesgo = tarea.categoria.equals("Académica", ignoreCase = true) ||
                tarea.categoria.equals("Laboral", ignoreCase = true)

        val requiereAlertas = tarea.duracionTarea >= 60 && esCategoriaRiesgo

        return if (requiereAlertas) {
            DetalleTareaResponse(
                idTarea = tarea.idTarea,
                titulo = tarea.titulo,
                requiereAlertasPostura = true,
                accionFisica = repository.obtenerPausaFisicaAleatoria(),
                fraseMotivacional = repository.obtenerFraseAleatoria("POSTURE")
            )
        } else {
            DetalleTareaResponse(
                idTarea = tarea.idTarea,
                titulo = tarea.titulo,
                requiereAlertasPostura = false,
                accionFisica = null,
                fraseMotivacional = null
            )
        }
    }
}