package com.alilopez.modules.usuarioFinal.ejercicio.application.usecase

import com.alilopez.modules.usuarioFinal.ejercicio.domain.repository.EjercicioRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.ElementoBarraGrafica
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.HistorialHabitoResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class ObtenerProgresoEjercicioUseCase(private val repository: EjercicioRepository) {

    suspend fun ejecutar(idUsuario: Int): HistorialHabitoResponse {
        val hoy = LocalDate.now(ZoneId.systemDefault())
        val fechaInicio = hoy.minusDays(6)
        val ultimos7Dias = (0..6).map { hoy.minusDays(it.toLong()) }.reversed()

        val registrosEjercicio = repository.obtenerHistorialSemanal(idUsuario, fechaInicio)

        val datosGrafica = ultimos7Dias.map { fecha ->
            val kilometros = registrosEjercicio[fecha] ?: 0.0

            val nombreDia = if (fecha == hoy) "Hoy" else fecha.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("es", "MX"))
                .replaceFirstChar { it.uppercase() }

            ElementoBarraGrafica(
                diaSemana = nombreDia.take(3),
                valor = kilometros,
                metaCumplida = kilometros >= 8.0,
                esHoy = fecha == hoy
            )
        }

        return HistorialHabitoResponse(
            tituloSeccion = "DISTANCIA RECORRIDA (KM) · ÚLTIMOS 7 DÍAS",
            mensajeMeta = "Verde = meta cumplida 🟢 Rojo = por debajo de 8 km",
            datosGrafica = datosGrafica
        )
    }
}