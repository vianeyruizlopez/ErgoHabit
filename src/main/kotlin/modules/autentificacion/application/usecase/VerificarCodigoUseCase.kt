package com.alilopez.modules.autentificacion.application.usecase

import com.alilopez.common.infrastructure.security.JwtConfig
import com.alilopez.modules.autentificacion.domain.repository.AutentificacionRepository
import com.alilopez.modules.autentificacion.domain.repository.CodigoVerificacionRepository
import java.time.LocalDateTime
import java.time.ZoneId


class VerificarCodigoUseCase(
    private val autentificacionRepository: AutentificacionRepository,
    private val codigoRepository: CodigoVerificacionRepository
) {
    suspend fun execute(email: String, codigoIngresado: String): String? {
        val registro = autentificacionRepository.verPorEmail(email)
            ?: throw NoSuchElementException("El correo no está registrado.")

        val codigoBD = codigoRepository.obtenerCodigoActivo(registro.id!!)
            ?: throw IllegalStateException("No hay un código activo. Inicia sesión de nuevo.")

        if (codigoBD.codigo != codigoIngresado.trim()) {
            throw IllegalArgumentException("El código ingresado es incorrecto.")
        }

        val ahora = LocalDateTime.now(ZoneId.of("America/Mexico_City"))
        if (ahora.isAfter(codigoBD.expiraEn)) {
            throw IllegalStateException("El código ha expirado. Inicia sesión de nuevo.")
        }

        if (codigoBD.usado) {
            throw IllegalStateException("Este código ya fue utilizado. Inicia sesión de nuevo.")
        }

        codigoRepository.marcarComoUsado(codigoBD.id!!)
        return JwtConfig.generateToken(registro.id, registro.idRol)
    }
}
