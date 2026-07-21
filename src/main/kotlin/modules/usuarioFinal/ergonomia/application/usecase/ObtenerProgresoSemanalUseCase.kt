package com.alilopez.modules.usuarioFinal.ergonomia.application.usecase

import com.alilopez.modules.usuarioFinal.ergonomia.domain.repository.PosturaRepository
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto.ElementoGraficaPostura
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto.ProgresoPosturaResponse
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId

class ObtenerProgresoSemanalUseCase(private val repository: PosturaRepository) {

    suspend fun ejecutar(idUsuario: Int): ProgresoPosturaResponse {
        val zonaHoraria = ZoneId.of("America/Mexico_City")
        val hoy = LocalDate.now(zonaHoraria)

        val inicioSemana = hoy.with(DayOfWeek.MONDAY)

        val historial = repository.obtenerHistorialUsuario(idUsuario)

        val diasNombres = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
        val datosGrafica = mutableListOf<ElementoGraficaPostura>()

        for (i in 0..6) {
            val fechaDia = inicioSemana.plusDays(i.toLong())
            val esHoy = fechaDia.isEqual(hoy)

            val registroDia = historial.find { postura ->
                postura.fecha?.atZone(zonaHoraria)?.toLocalDate()?.isEqual(fechaDia) == true
            }

            val totalAlertas = registroDia?.totalAlertas ?: 0
            val nombreDia = if (esHoy) "Hoy" else diasNombres[i]

            datosGrafica.add(
                ElementoGraficaPostura(
                    diaSemana = nombreDia,
                    totalAlertas = totalAlertas,
                    esHoy = esHoy
                )
            )
        }

        return ProgresoPosturaResponse(
            mensajeMeta = "Monitoreo SELECCIÓN SEMANAL: El objetivo es reducir las barras. Menos alertas indican menor fatiga cervical.",
            datosGrafica = datosGrafica
        )
    }
}