package com.alilopez.modules.usuarioFinal.agua.application.usecase

import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.ElementoBarraGrafica
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.HistorialHabitoResponse
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class ObtenerProgresoAguaUseCase(private val repository: AguaRepository) {

    suspend fun ejecutar(idUsuario: Int): HistorialHabitoResponse {
        val hoy = LocalDate.now(ZoneId.of("America/Mexico_City"))
        val lunesDeEstaSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val diasDeLaSemanaActual = (0..6).map { lunesDeEstaSemana.plusDays(it.toLong()) }
        val dashboard = repository.obtenerDashboard(idUsuario)
        val metaUsuarioMl = dashboard.metaDiariaMl
        val metaEnLitros = metaUsuarioMl / 1000.0
        val registrosAgua = repository.obtenerHistorialSemanal(idUsuario, lunesDeEstaSemana)
        val datosGrafica = diasDeLaSemanaActual.map { fecha ->
            val totalMl = registrosAgua[fecha] ?: 0
            val valorEnLitros = totalMl / 1000.0

            val nombreDia = if (fecha == hoy) "Hoy" else fecha.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("es", "MX"))
                .replaceFirstChar { it.uppercase() }

            ElementoBarraGrafica(
                diaSemana = nombreDia.take(3),
                valor = valorEnLitros,
                metaCumplida = totalMl >= metaUsuarioMl,
                esHoy = fecha == hoy
            )
        }

        return HistorialHabitoResponse(
            tituloSeccion = "HIDRATACIÓN (L) · SEMANA ACTUAL",
            mensajeMeta = "Verde = meta cumplida  Rojo = menos de $metaEnLitros L",
            datosGrafica = datosGrafica
        )
    }
}