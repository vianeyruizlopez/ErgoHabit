package com.alilopez.modules.usuarioFinal.ergonomia.application.usecase

import com.alilopez.modules.usuarioFinal.ergonomia.domain.repository.PosturaRepository
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto.ElementoGraficaPostura
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto.ProgresoPosturaResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class ObtenerProgresoSemanalUseCase(private val repository: PosturaRepository) {

    suspend fun ejecutar(idUsuario: Int): ProgresoPosturaResponse {
        val hoy = LocalDate.now(ZoneId.systemDefault())

        val ultimos7Dias = (0..6).map { hoy.minusDays(it.toLong()) }.reversed()

        val historial = repository.obtenerHistorialUsuario(idUsuario)

        val datosGrafica = ultimos7Dias.map { fecha ->
            val registroDia = historial.find { postura ->
                LocalDate.ofInstant(postura.fecha, ZoneId.systemDefault()) == fecha
            }

            val nombreDia = if (fecha == hoy) "Hoy" else fecha.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("es", "MX"))
                .replaceFirstChar { it.uppercase() }

            ElementoGraficaPostura(
                diaSemana = nombreDia.take(3),
                totalAlertas = registroDia?.totalAlertas ?: 0,
                esHoy = fecha == hoy
            )
        }

        return ProgresoPosturaResponse(
            mensajeMeta = "Monitoreo: El objetivo es reducir las barras. Menos alertas indican menor fatiga cervical.",
            datosGrafica = datosGrafica
        )
    }
}