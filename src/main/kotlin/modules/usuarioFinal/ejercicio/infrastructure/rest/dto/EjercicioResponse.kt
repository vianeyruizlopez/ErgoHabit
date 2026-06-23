package com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class EjercicioResponse(
    val kmRecorridosText: String,
    val metaKmText: String,
    val porcentajeCumplimiento: Int,
    val caloriasQuemadas: Int,
    val rachaDias: Int,
    val mensajeFaltanteText: String,
    val sugerenciaCaminataText: String,
    val fraseMotivacional: String
)
@Serializable
data class EjercicioErrorResponse(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
)

@Serializable
data class MensajeResponse(
    val mensaje: String
)