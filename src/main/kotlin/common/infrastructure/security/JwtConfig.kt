package com.alilopez.common.infrastructure.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private val secret = System.getenv("JWT_SECRET") ?: "llave_temporal_muy_larga_123"
    private const val issuer = "com.alilopez.sarc"
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier = JWT.require(algorithm).withIssuer(issuer).build()

    fun generateToken(id: Int, idRol: Int): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("idUsuario", id)
        .withClaim("idRol", idRol)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000 * 24))
        .sign(algorithm)
}