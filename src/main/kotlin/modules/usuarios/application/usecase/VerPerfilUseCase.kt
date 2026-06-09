package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class VerPerfilUseCase(private val repository: UsuarioRepository) {
    suspend fun execute(idConsultado: Int, idAutenticado: Int): Usuario {
        if (idConsultado != idAutenticado) {
            throw SecurityException("No tienes permiso para ver este perfil. Solo el propietario puede acceder.")
        }

        return repository.verPorId(idConsultado)
            ?: throw NoSuchElementException("Usuario no encontrado.")
    }
}