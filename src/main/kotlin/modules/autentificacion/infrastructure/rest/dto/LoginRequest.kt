package com.alilopez.modules.autentificacion.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val contrasena: String
)