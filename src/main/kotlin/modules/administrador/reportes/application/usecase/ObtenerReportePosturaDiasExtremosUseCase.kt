package com.alilopez.modules.administrador.reportes.application.usecase

import com.alilopez.modules.administrador.reportes.domain.model.ReportePosturaComunidad
import com.alilopez.modules.administrador.reportes.domain.repository.AdminReportesRepository

class ObtenerReportePosturaDiasExtremosUseCase(
    private val repository: AdminReportesRepository
) {
    suspend fun execute(idRolAutenticado: Int): ReportePosturaComunidad {
        if (idRolAutenticado != 1) {
            throw IllegalAccessException("Acceso denegado. Solo administradores pueden ver este reporte.")
        }
        return repository.obtenerReportePosturaDiasExtremos()
    }
}