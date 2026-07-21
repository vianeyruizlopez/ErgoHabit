package com.alilopez.modules.administrador.reportes.domain.model

data class KpiPosturaGlobal(
    val promedioAlertasDia1: Double,
    val promedioAlertasUltimo: Double,
    val tasaEfectividad: Double,
    val usuariosActivos: Int,
    val totalRegistrados: Int,
    val tasaAdopcion: Double,
    val estadoMeta: String
)


data class DetalleSemanaPostura(
    val etiquetaSemana: String,
    val numeroSemana: Int,
    val inicioSemana: String,
    val totalUsuarios: Int,
    val promedioAlertas: Double
)


data class ReportePosturaComunidad(
    val kpi: KpiPosturaGlobal,
    val detalleSemanal: List<DetalleSemanaPostura>
)
