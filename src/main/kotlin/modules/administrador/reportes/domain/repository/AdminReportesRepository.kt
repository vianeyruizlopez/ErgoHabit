package com.alilopez.modules.administrador.reportes.domain.repository

import com.alilopez.modules.administrador.reportes.domain.model.ReportePosturaComunidad

interface AdminReportesRepository {
    suspend fun obtenerReportePostura(): ReportePosturaComunidad
    suspend fun obtenerReportePosturaDiasExtremos(): ReportePosturaComunidad
}
