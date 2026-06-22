package com.alilopez.modules.usuarioFinal.agua.application.usecase

import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.dto.DashboardAguaResponse
import com.alilopez.modules.usuarioFinal.frases.domain.repository.FrasesRepository

class ObtenerDashboardAguaUseCase(
    private val repository: AguaRepository,
    private val frasesRepository: FrasesRepository
) {
    fun execute(idUsuario: Int): DashboardAguaResponse {
        val aguaDomain = repository.obtenerDashboard(idUsuario)

        val fraseAleatoria = frasesRepository.obtenerFraseAleatoriaPorCategoria("AGUA")?.texto
            ?: "¡Mantente hidratado hoy!"

        val tips = frasesRepository.obtenerTodasPorCategoria("AGUA").map { it.texto }

        return DashboardAguaResponse(
            metaDiariaMl = aguaDomain.metaDiariaMl,
            consumidoHoyMl = aguaDomain.consumidoHoyMl,
            porcentajeProgreso = aguaDomain.porcentajeProgreso,
            vasosConsumidos = aguaDomain.vasosConsumidos,
            mililitrosRestantes = aguaDomain.mililitrosRestantes,
            estaturaActual = aguaDomain.estaturaActual,
            pesoActual = aguaDomain.pesoActual,
            historialHoy = aguaDomain.historialHoy,
            fraseMotivacional = fraseAleatoria,
            tipsHidratacion = tips
        )
    }
}