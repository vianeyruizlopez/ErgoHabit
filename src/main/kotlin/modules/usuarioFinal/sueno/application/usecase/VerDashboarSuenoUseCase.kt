package com.alilopez.modules.usuarioFinal.sueno.application.usecase

import com.alilopez.modules.usuarioFinal.frases.domain.repository.FrasesRepository
import com.alilopez.modules.usuarioFinal.sueno.domain.repository.SuenoRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.SuenoResponse
import java.time.format.DateTimeFormatter
import java.time.Duration

class VerDashboarSuenoUseCase(
    private val repository: SuenoRepository,
    private val frasesRepository: FrasesRepository
) {

    fun execute(idUsuario: Int): SuenoResponse {
        val config = repository.obtenerConfiguracion(idUsuario)
            ?: return SuenoResponse(
                horasPlanificadas = 0,
                horasDormidasReales = 0.0,
                despertoATiempo = false,
                horaDormirConfigurada = "00:00",
                horaDespertarConfigurada = "00:00",
                porcentajeCumplimiento = 0,
                fraseMotivacional = "¡Establece tu horario de descanso!",
                tipsSueno = emptyList()
            )

        val duracion = Duration.between(config.horaDormir, config.horaDespertar)
        val horasPlanificadas = if (duracion.isNegative) {
            duracion.plusDays(1).toHours().toInt()
        } else {
            duracion.toHours().toInt()
        }

        val progresoHoy = repository.obtenerProgresoHoy(idUsuario)
        val horasReales = progresoHoy?.horasDormidas ?: 0.0
        val despertoATiempo = progresoHoy?.despertoATiempo ?: false

        val porcentaje = ((horasReales / 8.0) * 100).toInt().coerceAtMost(100)

        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val categoriaFrase = if (despertoATiempo && porcentaje >= 85) "TAREA_EXITO" else "TAREA_PENDIENTE"
        val fraseAleatoria = frasesRepository.obtenerFraseAleatoriaPorCategoria(categoriaFrase)?.texto
            ?: "Un buen descanso es la clave para un día productivo."

        val tips = frasesRepository.obtenerTodasPorCategoria("DORMIR").map { it.texto }

        return SuenoResponse(
            horasPlanificadas = horasPlanificadas,
            horasDormidasReales = horasReales,
            despertoATiempo = despertoATiempo,
            horaDormirConfigurada = config.horaDormir.format(formatter),
            horaDespertarConfigurada = config.horaDespertar.format(formatter),
            porcentajeCumplimiento = porcentaje,
            fraseMotivacional = fraseAleatoria,
            tipsSueno = tips
        )
    }
}