package com.alilopez.modules.usuarioFinal.sueno.application.usecase

import com.alilopez.modules.usuarioFinal.sueno.domain.repository.SuenoRepository
import java.time.LocalTime
import java.time.Duration

class RegistrarDespertarUseCase(private val repository: SuenoRepository) {

    fun execute(idUsuario: Int, horaActual: LocalTime): Boolean {
        val config = repository.obtenerConfiguracion(idUsuario)
            ?: throw IllegalStateException("No tienes un horario de sueño configurado.")

        val diferenciaMinutos = Duration.between(config.horaDespertar, horaActual).toMinutes()
        val despertoATiempo = diferenciaMinutos in 0..10

        val duracionSueno = Duration.between(config.horaDormir, horaActual)
        val horasReales = if (duracionSueno.isNegative) {
            duracionSueno.plusDays(1).toMinutes() / 60.0
        } else {
            duracionSueno.toMinutes() / 60.0
        }

        return repository.registrarProgresoSuenoDiario(idUsuario, horasReales, despertoATiempo)
    }
}