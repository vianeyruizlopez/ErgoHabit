package com.alilopez.modules.usuarios.application.usecase

import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class EliminarUseCase(private val repository: UsuarioRepository) {
    suspend fun execute(idAEliminar: Int, idAutenticado: Int, rolAutenticado: Int): Boolean {

        val admin = (rolAutenticado == 2)
        val esElMismoDueno = (idAEliminar == idAutenticado)

        if (!admin && !esElMismoDueno) {
            throw SecurityException("No tienes permisos para eliminar este usuario.")
        }

        val eliminado = repository.eliminar(idAEliminar)
        if (!eliminado) {
            throw NoSuchElementException("No se encontró el usuario que se desea eliminar.")
        }

        return true
    }
}