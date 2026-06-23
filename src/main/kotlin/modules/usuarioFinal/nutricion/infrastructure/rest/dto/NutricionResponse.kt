package com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class NutricionDashboardResponse(
    val comidasCompletadasText: String,
    val porcentajeCumplimiento: Int,
    val mensajeFaltanteText: String,
    val horaDesayunoConfigurada: String,
    val horaComidaConfigurada: String,
    val horaCenaConfigurada: String,
    val chequeoDesayuno: Boolean,
    val chequeoComida: Boolean,
    val chequeoCena: Boolean,
    val fraseMotivacional: String,
    val tipsNutricion: List<String>
)
@Serializable
data class NutricionErrorResponse(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
)

@Serializable
data class NutricionMensajeResponse(
    val mensaje: String
)