package com.alilopez.modules.usuarioFinal.agua.application.usecase

import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository

class RegistrarTomaAguaUseCase(private val repository: AguaRepository) {
    fun execute(idUsuario: Int, cantidadMl: Int): Boolean {
        if (cantidadMl <= 0) {
            throw IllegalArgumentException("La cantidad a registrar debe ser mayor a 0 ml.")
        }
        return repository.registrarToma(idUsuario, cantidadMl)
    }
}