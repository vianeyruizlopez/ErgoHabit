package com.alilopez.common.infrastructure.security

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.verifier)
            validate { credential ->
                val idClaim = credential.payload.getClaim("id")
                val rolClaim = credential.payload.getClaim("idRol")

                if (!idClaim.isNull && !rolClaim.isNull) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}