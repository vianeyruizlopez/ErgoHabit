package com.alilopez.modules.usuarioFinal.ejercicio.application.usecase

import com.alilopez.modules.usuarioFinal.ejercicio.domain.repository.EjercicioRepository
import java.math.BigDecimal

class ConfigurarMetaEjercicioUseCase(private val repository: EjercicioRepository) {
    fun execute(idUsuario: Int, nuevaMeta: BigDecimal) {
        if (nuevaMeta <= BigDecimal.ZERO) {
            throw IllegalArgumentException("La meta de kilómetros debe ser mayor a cero.")
        }
        repository.actualizarMetaKilometros(idUsuario, nuevaMeta)
    }
}