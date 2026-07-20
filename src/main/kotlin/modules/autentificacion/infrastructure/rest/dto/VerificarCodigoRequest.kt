package com.alilopez.modules.autentificacion.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerificarCodigoRequest(
    val email: String,
    val codigo: String
)
