package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository
import java.io.InputStream

class ActualizarFotoPerfilUseCase(
    private val usuarioRepository: UsuarioRepository
) {
    suspend fun ejecutar(idUsuario: Int, inputStream: InputStream): String {

        val urlImagen = usuarioRepository.actualizarFotoPerfil(idUsuario, inputStream)
            ?: throw RuntimeException("No se pudo subir la imagen a la nube o asociarla al usuario.")

        return urlImagen
    }
}