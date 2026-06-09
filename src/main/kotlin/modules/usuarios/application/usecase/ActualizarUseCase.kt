package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class ActualizarUseCase(private val repository: UsuarioRepository) {
    suspend fun execute(idAActualizar: Int, usuarioData: Usuario, idAutenticado: Int): Usuario {

        if (idAActualizar != idAutenticado) {
            throw SecurityException("No tienes permiso para actualizar este usuario.")
        }

        val usuarioExistente = repository.verPorId(idAActualizar)
            ?: throw NoSuchElementException("El usuario con ID $idAActualizar no fue encontrado.")

        val usuarioFusionado = usuarioData.copy(
            id = idAActualizar,
            nombre = if (!usuarioData.nombre.isNullOrBlank()) usuarioData.nombre else usuarioExistente.nombre,
            primerApellido = if (!usuarioData.primerApellido.isNullOrBlank()) usuarioData.primerApellido else usuarioExistente.primerApellido,
            segundoApellido = if (!usuarioData.segundoApellido.isNullOrBlank()) usuarioData.segundoApellido else usuarioExistente.segundoApellido,

            email = if (!usuarioData.email.isNullOrBlank()) usuarioData.email else usuarioExistente.email,
            contrasena = if (!usuarioData.contrasena.isNullOrBlank()) usuarioData.contrasena else usuarioExistente.contrasena,
            idRol = usuarioExistente.idRol
        )

        return repository.actualizar(idAActualizar, usuarioFusionado)
            ?: throw IllegalStateException("Error interno al intentar actualizar el usuario.")
    }
}