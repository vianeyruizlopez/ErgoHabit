package com.alilopez.modules.usuarioFinal.ejercicio.application.usecase

import com.alilopez.modules.usuarioFinal.ejercicio.domain.repository.EjercicioRepository
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.dto.EjercicioResponse
import com.alilopez.modules.usuarioFinal.frases.domain.repository.FrasesRepository
import java.math.BigDecimal
import java.math.RoundingMode

class VerDashboardEjercicioUseCase(
    private val repository: EjercicioRepository,
    private val frasesRepository: FrasesRepository
) {
    fun execute(idUsuario: Int): EjercicioResponse {
        val metaKm = repository.obtenerMetaKilometros(idUsuario)

        val progresoHoy = repository.obtenerProgresoHoy(idUsuario)
        val kmLlevados = progresoHoy?.kmRecorridos ?: BigDecimal.ZERO
        val calorias = progresoHoy?.caloriasQuemadas ?: 0

        val racha = repository.calcularRachaDias(idUsuario)

        val porcentaje = if (metaKm > BigDecimal.ZERO) {
            kmLlevados.multiply(BigDecimal("100")).divide(metaKm, 0, RoundingMode.HALF_UP).toInt()
        } else {
            0
        }
        val faltantes = metaKm.subtract(kmLlevados).max(BigDecimal.ZERO)
        val mensajeFaltanteText: String
        val sugerenciaCaminataText: String

        if (kmLlevados >= metaKm && metaKm > BigDecimal.ZERO) {
            mensajeFaltanteText = "¡Meta diaria alcanzada!"
            sugerenciaCaminataText = "¡Excelente trabajo! Has cumplido tu objetivo de hoy."
        } else {
            val faltantesFormateado = faltantes.setScale(2, RoundingMode.HALF_UP)
            mensajeFaltanteText = "¡Te faltan $faltantesFormateado km!"

            val minutosEstimados = faltantes.multiply(BigDecimal("10")).toInt().coerceAtLeast(5)
            sugerenciaCaminataText = "Una caminata de $minutosEstimados minutos te acercará a tu meta"
        }

        val categoriaFrase = if (porcentaje >= 100) "TAREA_EXITO" else "TAREA_PENDIENTE"
        val fraseAleatoria = frasesRepository.obtenerFraseAleatoriaPorCategoria(categoriaFrase)?.texto
            ?: "¡Cada paso cuenta, mantén tu disciplina hoy!"

        return EjercicioResponse(
            kmRecorridosText = "${kmLlevados.setScale(2, RoundingMode.HALF_UP)} km",
            metaKmText = "de ${metaKm.setScale(1, RoundingMode.HALF_UP)} km",
            porcentajeCumplimiento = porcentaje,
            caloriasQuemadas = calorias,
            rachaDias = racha,
            mensajeFaltanteText = mensajeFaltanteText,
            sugerenciaCaminataText = sugerenciaCaminataText,
            fraseMotivacional = fraseAleatoria
        )
    }
}