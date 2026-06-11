package com.alilopez.modules.usuarioFinal.agua.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Agua(
    val idAgua: Int? = null,
    val idUsuario: Int,
    val fecha: String? = null,
    val cantidadConsumida: Int = 0,
    val estatura: Double = 0.0,
    val peso: Double = 0.0
)