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
