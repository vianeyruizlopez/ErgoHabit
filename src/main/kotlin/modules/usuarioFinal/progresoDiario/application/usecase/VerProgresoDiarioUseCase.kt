package com.alilopez.modules.usuarioFinal.progresoDiario.application.usecase

import com.alilopez.modules.usuarioFinal.progresoDiario.domain.model.ProgresoDiario
import com.alilopez.modules.usuarioFinal.progresoDiario.domain.repository.ProgresoDiarioRepository
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository

class VerProgresoDiarioUseCase(
    private val progresoDiarioRepository: ProgresoDiarioRepository,
    private val usuarioRepository: UsuarioRepository
) {
    suspend fun execute(idUsuarioSolicitado: Int, idUsuarioAutenticado: Int, idRolAutenticado: Int): ProgresoDiario {
        if (idRolAutenticado != 2) {
            throw IllegalArgumentException("Acceso denegado: Este recurso es exclusivo para usuarios finales.")
        }
        if (idUsuarioSolicitado != idUsuarioAutenticado) {
            throw IllegalAccessException("Acceso denegado: No tienes permiso para ver la información de otros usuarios.")
        }
        val usuario = usuarioRepository.verPorId(idUsuarioSolicitado)
            ?: throw NoSuchElementException("El usuario con ID $idUsuarioSolicitado no existe.")

        val nombre = usuario.nombre ?: "Usuario"
        val peso = usuario.peso ?: 0.0

        val datosBD = progresoDiarioRepository.obtenerDatosCrudosHoy(idUsuarioSolicitado)


        val estadoPosturaCalculado = when {
            datosBD.alertasPostura <= 5 -> "Postura OK"
            datosBD.alertasPostura in 10..15 -> "Postura Regular"
            else -> "Postura Crítica"
        }

        val sugerenciaCalculadaMl = if (peso > 0.0) (peso * 35).toInt() else 2450

        val metaComidasFija = 3

        val pctAgua = (datosBD.aguaConsumida.toDouble() / datosBD.metaAgua) * 100
        val pctSueno = (datosBD.horasSueno / 8.0) * 100
        val pctEjercicio = if (datosBD.metaEjercicio > 0) (datosBD.kmRecorridos / datosBD.metaEjercicio) * 100 else 0.0
        val pctNutricion = (datosBD.comidasCompletadas.toDouble() / metaComidasFija) * 100

        return ProgresoDiario(
            nombreUsuario = nombre,
            totalAlertasPostura = datosBD.alertasPostura,
            estadoPostura = estadoPosturaCalculado,
            rachaDias = datosBD.diasRacha,
            metaAguaMl = datosBD.metaAgua,
            aguaConsumidaMl = datosBD.aguaConsumida,
            sugerenciaAguaPesoMl = sugerenciaCalculadaMl,
            porcentajeAgua = Math.round(pctAgua * 10.0) / 10.0,
            horasSuenoRegistradas = datosBD.horasSueno,
            porcentajeSueno = Math.round(pctSueno * 10.0) / 10.0,
            metaEjercicioKm = datosBD.metaEjercicio,
            kmEjercicioRecorridos = datosBD.kmRecorridos,
            caloriasEjercicioQuemadas = datosBD.caloriasQuemadas,
            porcentajeEjercicio = Math.round(pctEjercicio * 10.0) / 10.0,
            comidasCompletadasHoy = datosBD.comidasCompletadas,
            metaComidasTotales = metaComidasFija,
            porcentajeNutricion = Math.round(pctNutricion * 10.0) / 10.0
        )
    }
}