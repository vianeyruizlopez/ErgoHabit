package com.alilopez.modules.usuarioFinal.sueno.application.usecase

import com.alilopez.modules.usuarioFinal.sueno.domain.repository.SuenoRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.ElementoBarraGrafica
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.HistorialHabitoResponse
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class ObtenerProgresoSuenoUseCase(private val repository: SuenoRepository) {

    suspend fun ejecutar(idUsuario: Int): HistorialHabitoResponse {
        val hoy = LocalDate.now(ZoneId.systemDefault())
        val lunesDeEstaSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val diasDeLaSemanaActual = (0..6).map { lunesDeEstaSemana.plusDays(it.toLong()) }
        val registrosSueno = repository.obtenerHistorialSemanl(idUsuario, lunesDeEstaSemana)

        val datosGrafica = diasDeLaSemanaActual.map { fecha ->
            val horas = registrosSueno[fecha] ?: 0.0

            val nombreDia = if (fecha == hoy) "Hoy" else fecha.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("es", "MX"))
                .replaceFirstChar { it.uppercase() }

            ElementoBarraGrafica(
                diaSemana = nombreDia.take(3),
                valor = horas,
                metaCumplida = horas >= 8.0,
                esHoy = fecha == hoy
            )
        }

        return HistorialHabitoResponse(
            tituloSeccion = "HORAS DE SUEÑO · SEMANA ACTUAL",
            mensajeMeta = "Verde = meta cumplida - Rojo = menos de 8h",
            datosGrafica = datosGrafica
        )
    }
}