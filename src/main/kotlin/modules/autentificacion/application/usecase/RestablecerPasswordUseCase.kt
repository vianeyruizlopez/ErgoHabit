package com.alilopez.modules.autentificacion.application.usecase

import com.alilopez.modules.autentificacion.domain.repository.AutentificacionRepository
import org.mindrot.jbcrypt.BCrypt

class RestablecerPasswordUseCase(private val repository: AutentificacionRepository) {

    suspend fun execute(email: String, nuevaContrasena: String, confirmarContrasena: String): Boolean {
        if (email.isNullOrBlank() || nuevaContrasena.isNullOrBlank() || confirmarContrasena.isNullOrBlank()) {
            throw IllegalArgumentException("Todos los campos son requeridos.")
        }
        if (nuevaContrasena != confirmarContrasena) {
            throw IllegalArgumentException("La nueva contraseña y la confirmación no coinciden.")
        }
        val regexSegura = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$""".toRegex()
        if (!nuevaContrasena.matches(regexSegura)) {
            throw IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, una minúscula y un número.")
        }
        val usuario = repository.verPorEmail(email.trim().lowercase())
            ?: throw NoSuchElementException("El correo electrónico no se encuentra registrado.")
        val passwordEncriptado = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt())

        return repository.actualizarContrasena(usuario.email, passwordEncriptado)
    }
}