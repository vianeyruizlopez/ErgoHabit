package com.alilopez.modules.usuarioFinal.sueno.application.usecase

import com.alilopez.modules.usuarioFinal.sueno.domain.repository.SuenoRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.ElementoBarraGrafica
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.HistorialHabitoResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class ObtenerProgresoSuenoUseCase(private val repository: SuenoRepository) {

    suspend fun ejecutar(idUsuario: Int): HistorialHabitoResponse {
        val hoy = LocalDate.now(ZoneId.systemDefault())
        val fechaInicio = hoy.minusDays(6)
        val ultimos7Dias = (0..6).map { hoy.minusDays(it.toLong()) }.reversed()

        val registrosSueno = repository.obtenerHistorialSemanl(idUsuario, fechaInicio)

        val datosGrafica = ultimos7Dias.map { fecha ->
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
            tituloSeccion = "HORAS DE SUEÑO · ÚLTIMOS 7 DÍAS",
            mensajeMeta = "Verde = meta cumplida 🟢 Rojo = menos de 8h",
            datosGrafica = datosGrafica
        )
    }
}