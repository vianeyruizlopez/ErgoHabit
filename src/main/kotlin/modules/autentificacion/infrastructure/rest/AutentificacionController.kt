package com.alilopez.modules.autentificacion.infrastructure.rest

import com.alilopez.modules.autentificacion.application.usecase.LoginUseCase
import com.alilopez.modules.autentificacion.application.usecase.RegistrarUseCase
import com.alilopez.modules.autentificacion.infrastructure.rest.dto.LoginRequest
import com.alilopez.modules.autentificacion.domain.model.Registro
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class AutentificacionController(
    private val loginUseCase: LoginUseCase,
    private val registrarUseCase: RegistrarUseCase
) {
    suspend fun login(call: ApplicationCall) {
        try {
            val request = call.receive<LoginRequest>()
            val token = loginUseCase.loginTradicional(request.email, request.contrasena)

            if (token != null) {
                call.respond(HttpStatusCode.OK, mapOf("token" to token))
            } else {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Correo o contraseña incorrectos.")
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Datos de inicio de sesión inválidos o faltantes.")
            )
        }
    }

    suspend fun registrar(call: ApplicationCall) {
        try {
            val datosCliente = call.receive<Registro>()
            val resultado = registrarUseCase.execute(datosCliente)

            if (resultado != null) {
                call.respond(HttpStatusCode.Created, resultado)
            } else {
                call.respond(
                    HttpStatusCode.Conflict,
                    mapOf("error" to "El correo electrónico ya se encuentra registrado.")
                )
            }
        } catch (e: IllegalArgumentException) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to (e.message ?: "Contraseña inválida."))
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Datos de registro inválidos o faltantes.")
            )
        }
    }
}