package com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String
)

@Serializable
data class MensajeResponse(
    val mensaje: String
)