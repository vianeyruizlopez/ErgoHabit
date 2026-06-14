package com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegistrarTomaRequest(
    val cantidadMl: Int
)

@Serializable
data class ConfigurarMetaRequest(
    val tipo: String,
    val metaMl: Int? = null,
    val peso: Double? = null,
    val estatura: Double? = null
)