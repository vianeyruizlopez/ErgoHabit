package com.alilopez.modules.usuarioFinal.ergonomia.application.usecase

import com.alilopez.modules.usuarioFinal.ergonomia.domain.model.Postura
import com.alilopez.modules.usuarioFinal.ergonomia.domain.repository.PosturaRepository

class VerHistorialPosturaUseCase(private val repository: PosturaRepository) {

    suspend fun ejecutar(idUsuario: Int): List<Postura> {
        if (idUsuario <= 0) {
            throw IllegalArgumentException("El ID de usuario provisto no es válido.")
        }

        return repository.obtenerHistorialUsuario(idUsuario)
    }
}