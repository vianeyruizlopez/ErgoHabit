package com.alilopez.modules.usuarioFinal.sueno.application.usecase

import com.alilopez.modules.usuarioFinal.sueno.domain.repository.SuenoRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.SuenoResponse
import java.time.format.DateTimeFormatter
import java.time.Duration

class VerDashboarSuenoUseCase (private val repository: SuenoRepository) {

    fun execute(idUsuario: Int): SuenoResponse {
        val config = repository.obtenerConfiguracion(idUsuario)
            ?: return SuenoResponse(0, 0.0, false, "00:00", "00:00", 0)

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

        return SuenoResponse(
            horasPlanificadas = horasPlanificadas,
            horasDormidasReales = horasReales,
            despertoATiempo = despertoATiempo,
            horaDormirConfigurada = config.horaDormir.format(formatter),
            horaDespertarConfigurada = config.horaDespertar.format(formatter),
            porcentajeCumplimiento = porcentaje
        )
    }
}