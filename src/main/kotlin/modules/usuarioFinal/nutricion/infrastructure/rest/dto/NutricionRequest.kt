package com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfigurarNutricionRequest(
    val horaDesayuno: String,
    val horaComida: String,
    val horaCena: String
)
@Serializable
data class MarcarComidaRequest(
    val tipoComida: String,
    val estado: Boolean
)