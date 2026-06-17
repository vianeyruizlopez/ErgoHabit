package com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class MetaEjercicioRequest(
    val nuevaMeta: Double
)

@Serializable
data class RegistrarKmRequest(
    val km: Double
)