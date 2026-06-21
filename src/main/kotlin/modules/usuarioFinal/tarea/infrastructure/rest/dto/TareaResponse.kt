package com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class TareaResponse(
    val idTarea: Int,
    val titulo: String,
    val categoria: String,
    val duracionText: String,
    val idEstado: Int
)

@Serializable
data class TareasDashboardResponse(
    val totalPendientesText: String,
    val totalCompletadasText: String,
    val pendientes: List<TareaResponse>,
    val completadas: List<TareaResponse>
)
@Serializable
data class DetalleTareaResponse(
    val idTarea: Int,
    val titulo: String,
    val requiereAlertasPostura: Boolean,
    val accionFisica: String?,
    val fraseMotivacional: String?
)