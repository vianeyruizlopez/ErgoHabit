package com.alilopez.modules.administrador.reportes.infrastructure.persistence

import com.alilopez.modules.administrador.reportes.domain.model.DetalleSemanaPostura
import com.alilopez.modules.administrador.reportes.domain.model.KpiPosturaGlobal
import com.alilopez.modules.administrador.reportes.domain.model.ReportePosturaComunidad
import com.alilopez.modules.administrador.reportes.domain.repository.AdminReportesRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MysqlAdminReportesRepository : AdminReportesRepository {

    override suspend fun obtenerReportePostura(): ReportePosturaComunidad =
        newSuspendedTransaction {

            var kpi = KpiPosturaGlobal(
                promedioAlertasDia1    = 0.0,
                promedioAlertasUltimo  = 0.0,
                tasaEfectividad        = 0.0,
                usuariosActivos        = 0,
                totalRegistrados       = 0,
                tasaAdopcion           = 0.0,
                estadoMeta             = "Sin datos"
            )

            exec("CALL sp_kpi_postura_global()") { rs ->
                if (rs.next()) {
                    kpi = KpiPosturaGlobal(
                        promedioAlertasDia1   = rs.getDouble("promedio_alertas_dia1"),
                        promedioAlertasUltimo = rs.getDouble("promedio_alertas_ultimo"),
                        tasaEfectividad       = rs.getDouble("tasa_efectividad"),
                        usuariosActivos       = rs.getInt("usuarios_activos"),
                        totalRegistrados      = rs.getInt("total_registrados"),
                        tasaAdopcion          = rs.getDouble("tasa_adopcion"),
                        estadoMeta            = rs.getString("estado_meta")
                    )
                }
            }

            val detalleSemanal = mutableListOf<DetalleSemanaPostura>()
            exec("CALL sp_detalle_semanal_postura()") { rs ->
                while (rs.next()) {
                    detalleSemanal.add(
                        DetalleSemanaPostura(
                            etiquetaSemana  = rs.getString("etiquetaSemana"),
                            numeroSemana    = rs.getInt("numeroSemana"),
                            inicioSemana    = rs.getString("inicioSemana"),
                            totalUsuarios   = rs.getInt("totalUsuarios"),
                            promedioAlertas = rs.getDouble("promedioAlertas")
                        )
                    )
                }
            }

            ReportePosturaComunidad(
                kpi            = kpi,
                detalleSemanal = detalleSemanal
            )
        }
    override suspend fun obtenerReportePosturaDiasExtremos(): ReportePosturaComunidad =
        newSuspendedTransaction {

            var kpi = KpiPosturaGlobal(
                promedioAlertasDia1    = 0.0,
                promedioAlertasUltimo  = 0.0,
                tasaEfectividad        = 0.0,
                usuariosActivos        = 0,
                totalRegistrados       = 0,
                tasaAdopcion           = 0.0,
                estadoMeta             = "Sin datos"
            )

            exec("CALL sp_kpi_postura_dias_extremos()") { rs ->
                if (rs.next()) {
                    kpi = KpiPosturaGlobal(
                        promedioAlertasDia1   = rs.getDouble("promedio_alertas_dia1"),
                        promedioAlertasUltimo = rs.getDouble("promedio_alertas_ultimo"),
                        tasaEfectividad       = rs.getDouble("tasa_efectividad"),
                        usuariosActivos       = rs.getInt("usuarios_activos"),
                        totalRegistrados      = rs.getInt("total_registrados"),
                        tasaAdopcion          = rs.getDouble("tasa_adopcion"),
                        estadoMeta            = rs.getString("estado_meta")
                    )
                }
            }

            val detalleSemanal = mutableListOf<DetalleSemanaPostura>()
            exec("CALL sp_detalle_semanal_postura()") { rs ->
                while (rs.next()) {
                    detalleSemanal.add(
                        DetalleSemanaPostura(
                            etiquetaSemana  = rs.getString("etiquetaSemana"),
                            numeroSemana    = rs.getInt("numeroSemana"),
                            inicioSemana    = rs.getString("inicioSemana"),
                            totalUsuarios   = rs.getInt("totalUsuarios"),
                            promedioAlertas = rs.getDouble("promedioAlertas")
                        )
                    )
                }
            }

            ReportePosturaComunidad(
                kpi            = kpi,
                detalleSemanal = detalleSemanal
            )
        }
}