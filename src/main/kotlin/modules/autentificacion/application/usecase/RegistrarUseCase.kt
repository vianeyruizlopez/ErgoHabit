package com.alilopez.modules.autentificacion.application.usecase

import com.alilopez.modules.autentificacion.domain.model.Registro
import com.alilopez.modules.autentificacion.domain.repository.AutentificacionRepository
import org.mindrot.jbcrypt.BCrypt

class RegistrarUseCase(private val repository: AutentificacionRepository) {

    suspend fun execute(registro: Registro): Registro? {
        val contraseñaOriginal = registro.contrasena
        if (contraseñaOriginal.isNullOrBlank()) {
            throw IllegalArgumentException("La contraseña es requerida.")
        }
        val regexSegura = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$""".toRegex()
        if (!contraseñaOriginal.matches(regexSegura)) {
            throw IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, una minúscula y un número.")
        }

        val usuarioFinal = registro.copy(idRol = 2)
        val existe = repository.verPorEmail(usuarioFinal.email)

        if (existe != null) {
            return null
        }

        val contraseñaEncriptada = BCrypt.hashpw(contraseñaOriginal, BCrypt.gensalt())
        val usuarioConPasswordEncriptado = usuarioFinal.copy(contrasena = contraseñaEncriptada)

        return repository.registrar(usuarioConPasswordEncriptado)
    }
}