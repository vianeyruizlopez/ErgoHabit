package com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class SuenoResponse(
    val horasPlanificadas: Int,
    val horasDormidasReales: Double,
    val despertoATiempo: Boolean,
    val horaDormirConfigurada: String,
    val horaDespertarConfigurada: String,
    val porcentajeCumplimiento: Int
)
@Serializable
data class ElementoBarraGrafica(
    val diaSemana: String,
    val valor: Double,
    val metaCumplida: Boolean,
    val esHoy: Boolean
)

@Serializable
data class HistorialHabitoResponse(
    val tituloSeccion: String,
    val mensajeMeta: String,
    val datosGrafica: List<ElementoBarraGrafica>
)
