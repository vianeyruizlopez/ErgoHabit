package com.alilopez.modules.administrador.reportes.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class KpiPosturaGlobalResponse(
    val promedioAlertasDia1: Double,
    val promedioAlertasUltimo: Double,
    val tasaEfectividad: Double,
    val usuariosActivos: Int,
    val totalRegistrados: Int,
    val tasaAdopcion: Double,
    val estadoMeta: String
)

@Serializable
data class DetalleSemanaPosturaResponse(
    val numeroSemana: Int,
    val inicioSemana: String,
    val totalUsuarios: Int,
    val promedioAlertas: Double
)

@Serializable
data class ReportePosturaComunidadResponse(
    val kpi: KpiPosturaGlobalResponse,
    val detalleSemanal: List<DetalleSemanaPosturaResponse>
)

@Serializable
data class AdminErrorResponse(
    val code: String,
    val message: String
)
