package com.alilopez.modules.administrador.reportes.infrastructure.rest

import com.alilopez.modules.administrador.reportes.application.usecase.ObtenerReportePosturaUseCase
import com.alilopez.modules.administrador.reportes.infrastructure.rest.dto.AdminErrorResponse
import com.alilopez.modules.administrador.reportes.infrastructure.rest.dto.DetalleSemanaPosturaResponse
import com.alilopez.modules.administrador.reportes.infrastructure.rest.dto.KpiPosturaGlobalResponse
import com.alilopez.modules.administrador.reportes.infrastructure.rest.dto.ReportePosturaComunidadResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class AdminReportesController(
    private val obtenerReportePosturaUseCase: ObtenerReportePosturaUseCase
) {
    suspend fun obtenerReportePostura(call: ApplicationCall, idRol: Int) {
        try {
            val datos = obtenerReportePosturaUseCase.execute(idRol)
            val response = ReportePosturaComunidadResponse(
                kpi = KpiPosturaGlobalResponse(
                    promedioAlertasDia1   = datos.kpi.promedioAlertasDia1,
                    promedioAlertasUltimo = datos.kpi.promedioAlertasUltimo,
                    tasaEfectividad       = datos.kpi.tasaEfectividad,
                    usuariosActivos       = datos.kpi.usuariosActivos,
                    totalRegistrados      = datos.kpi.totalRegistrados,
                    tasaAdopcion          = datos.kpi.tasaAdopcion,
                    estadoMeta            = datos.kpi.estadoMeta
                ),
                detalleSemanal = datos.detalleSemanal.map {
                    DetalleSemanaPosturaResponse(
                        numeroSemana    = it.numeroSemana,
                        inicioSemana    = it.inicioSemana,
                        totalUsuarios   = it.totalUsuarios,
                        promedioAlertas = it.promedioAlertas
                    )
                }
            )
            call.respond(HttpStatusCode.OK, response)
        } catch (e: IllegalAccessException) {
            call.respond(HttpStatusCode.Forbidden,
                AdminErrorResponse("ACCESO_DENEGADO", e.message ?: "Sin permisos."))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError,
                AdminErrorResponse("ERROR_SERVIDOR", "Error al obtener el reporte de postura."))
        }
    }
}
