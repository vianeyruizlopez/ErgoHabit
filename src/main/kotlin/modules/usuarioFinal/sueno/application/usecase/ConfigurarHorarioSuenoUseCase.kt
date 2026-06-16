package com.alilopez.modules.usuarioFinal.sueno.application.usecase

import com.alilopez.modules.usuarioFinal.sueno.domain.repository.SuenoRepository
import java.time.LocalTime
import java.time.Duration

class ConfigurarHorarioSuenoUseCase(private val repository: SuenoRepository) {

    fun execute(idUsuario: Int, horaDespertar: LocalTime, horaDormir: LocalTime): Int {
        val duracion = Duration.between(horaDormir, horaDespertar)
        val horasCalculadas = if (duracion.isNegative) {
            duracion.plusDays(1).toHours().toInt()
        } else {
            duracion.toHours().toInt()
        }

        if (horasCalculadas < 5) {
            throw IllegalArgumentException("No puedes configurar un horario de sueño menor a 5 horas.")
        }

        repository.guardarConfiguracionHorario(idUsuario, horaDormir, horaDespertar)

        return horasCalculadas
    }
}