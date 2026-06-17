package com.alilopez.modules.usuarioFinal.ejercicio.application.usecase

import com.alilopez.modules.usuarioFinal.ejercicio.domain.repository.EjercicioRepository
import java.math.BigDecimal

class RegistrarKilometrosUseCase(private val repository: EjercicioRepository) {
    fun execute(idUsuario: Int, kilometros: BigDecimal): Boolean {
        if (kilometros < BigDecimal.ZERO) {
            throw IllegalArgumentException("No se pueden registrar kilómetros negativos.")
        }

        val factorCalorias = BigDecimal("65")
        val caloriasCalculadas = kilometros.multiply(factorCalorias).toInt()

        return repository.registrarProgresoKm(idUsuario, kilometros, caloriasCalculadas)
    }
}