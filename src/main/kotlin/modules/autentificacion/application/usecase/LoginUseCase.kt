package com.alilopez.modules.autentificacion.application.usecase

import com.alilopez.common.infrastructure.security.JwtConfig
import com.alilopez.modules.autentificacion.domain.repository.AutentificacionRepository
import org.mindrot.jbcrypt.BCrypt

class LoginUseCase(private val repository: AutentificacionRepository) {

    suspend fun loginTradicional(email: String, contrasena: String): String? {
        val registro = repository.verPorEmail(email)

        if (registro != null && !registro.contrasena.isNullOrBlank()) {

            val contraseñaEsValida = BCrypt.checkpw(contrasena, registro.contrasena)
            if (contraseñaEsValida) {
                return JwtConfig.generateToken(registro.id ?: 0, registro.idRol)
            }
        }
        return null
    }
}