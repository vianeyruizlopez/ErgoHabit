package com.alilopez.modules.usuarioFinal.agua.application.usecase

import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.ElementoBarraGrafica
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto.HistorialHabitoResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

class ObtenerProgresoAguaUseCase(private val repository: AguaRepository) {

    suspend fun ejecutar(idUsuario: Int): HistorialHabitoResponse {
        val hoy = LocalDate.now(ZoneId.systemDefault())
        val fechaInicio = hoy.minusDays(6)
        val ultimos7Dias = (0..6).map { hoy.minusDays(it.toLong()) }.reversed()

        val registrosAgua = repository.obtenerHistorialSemanal(idUsuario, fechaInicio)

        val datosGrafica = ultimos7Dias.map { fecha ->
            val totalMl = registrosAgua[fecha] ?: 0
            val valorEnLitros = totalMl / 1000.0

            val nombreDia = if (fecha == hoy) "Hoy" else fecha.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale("es", "MX"))
                .replaceFirstChar { it.uppercase() }

            ElementoBarraGrafica(
                diaSemana = nombreDia.take(3),
                valor = valorEnLitros,
                metaCumplida = totalMl >= 2000,
                esHoy = fecha == hoy
            )
        }

        return HistorialHabitoResponse(
            tituloSeccion = "HIDRATACIÓN (L) · ÚLTIMOS 7 DÍAS",
            mensajeMeta = "Verde = meta cumplida  Rojo = menos de 2,000 ml",
            datosGrafica = datosGrafica
        )
    }
}