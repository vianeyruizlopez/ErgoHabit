package com.alilopez.modules.usuarios.domain.repository

import com.alilopez.modules.usuarios.domain.model.Usuario
import java.io.InputStream

interface UsuarioRepository {
    suspend fun verTodos(): List<Usuario>
    suspend fun verPorId(id: Int): Usuario?
    suspend fun actualizar(id: Int, usuario: Usuario): Usuario?
    suspend fun eliminar(id: Int): Boolean
    suspend fun actualizarFotoPerfil(idUsuario: Int, imagenStream: InputStream): String?
}