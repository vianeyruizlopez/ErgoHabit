package com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto

import com.alilopez.modules.usuarioFinal.agua.domain.model.TomaCronologica
import kotlinx.serialization.Serializable

@Serializable
data class AguaErrorResponse(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
)

@Serializable
data class MensajeResponse(
    val mensaje: String
)
@Serializable
data class DashboardAguaResponse(
    val metaDiariaMl: Int,
    val consumidoHoyMl: Int,
    val porcentajeProgreso: Int,
    val vasosConsumidos: Int,
    val mililitrosRestantes: Int,
    val estaturaActual: Double,
    val pesoActual: Double,
    val historialHoy: List<TomaCronologica>,
    val fraseMotivacional: String,
    val tipsHidratacion: List<String>
)