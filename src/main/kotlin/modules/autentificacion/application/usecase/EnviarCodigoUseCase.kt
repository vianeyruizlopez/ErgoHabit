package com.alilopez.modules.autentificacion.application.usecase

import com.alilopez.modules.autentificacion.domain.repository.AutentificacionRepository
import com.alilopez.modules.autentificacion.domain.repository.CodigoVerificacionRepository
import com.alilopez.modules.autentificacion.infrastructure.email.EmailService
import org.mindrot.jbcrypt.BCrypt

class EnviarCodigoUseCase(
    private val autentificacionRepository: AutentificacionRepository,
    private val codigoRepository: CodigoVerificacionRepository
) {
    suspend fun execute(email: String, contrasena: String): Boolean {
        val registro = autentificacionRepository.verPorEmail(email)
            ?: return false

        if (registro.contrasena.isNullOrBlank()) return false
        if (!BCrypt.checkpw(contrasena, registro.contrasena)) return false

        val codigo = (100000..999999).random().toString()

        codigoRepository.eliminarCodigosAnteriores(registro.id!!)
        codigoRepository.guardarCodigo(registro.id, codigo)

        return EmailService.enviarCodigo(email, codigo)
    }
}
