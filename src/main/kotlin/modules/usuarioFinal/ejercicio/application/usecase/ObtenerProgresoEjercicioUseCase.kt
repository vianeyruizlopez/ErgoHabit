package com.alilopez.modules.usuarioFinal.ejercicio.application.usecase

import com.alilopez.modules.usuarioFinal.ejercicio.domain.repository.EjercicioRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.ElementoBarraGrafica
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.HistorialHabitoResponse
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class ObtenerProgresoEjercicioUseCase(private val repository: EjercicioRepository) {

    suspend fun ejecutar(idUsuario: Int): HistorialHabitoResponse {
        val hoy = LocalDate.now(ZoneId.systemDefault())
        val lunesDeEstaSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val diasDeLaSemanaActual = (0..6).map { lunesDeEstaSemana.plusDays(it.toLong()) }
        val metaUsuarioKm = repository.obtenerMetaKilometros(idUsuario).toDouble()

        val registrosEjercicio = repository.obtenerHistorialSemanal(idUsuario, lunesDeEstaSemana)
        val datosGrafica = diasDeLaSemanaActual.map { fecha ->
            val kilometros = registrosEjercicio[fecha] ?: 0.0

            val nombreDia = if (fecha == hoy) "Hoy" else fecha.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("es", "MX"))
                .replaceFirstChar { it.uppercase() }

            ElementoBarraGrafica(
                diaSemana = nombreDia.take(3),
                valor = kilometros,
                metaCumplida = kilometros >= metaUsuarioKm,
                esHoy = fecha == hoy
            )
        }

        return HistorialHabitoResponse(
            tituloSeccion = "DISTANCIA RECORRIDA (KM) · SEMANA ACTUAL",
            mensajeMeta = "Verde = meta cumplida - Rojo = por debajo de $metaUsuarioKm km",
            datosGrafica = datosGrafica
        )
    }
}