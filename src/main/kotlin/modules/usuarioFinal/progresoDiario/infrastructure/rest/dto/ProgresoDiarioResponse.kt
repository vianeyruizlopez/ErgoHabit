package com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.dto

import com.alilopez.modules.usuarioFinal.progresoDiario.domain.model.ProgresoDiario
import kotlinx.serialization.Serializable

@Serializable
data class ProgresoDiarioResponse(
    val nombreUsuario: String,
    val totalAlertasPostura: Int,
    val estadoPostura: String,
    val rachaDias: Int,
    val metaAguaMl: Int,
    val aguaConsumidaMl: Int,
    val sugerenciaAguaPesoMl: Int,
    val porcentajeAgua: Double,
    val horasSuenoRegistradas: Double,
    val porcentajeSueno: Double,
    val metaEjercicioKm: Double,
    val kmEjercicioRecorridos: Double,
    val caloriasEjercicioQuemadas: Int,
    val porcentajeEjercicio: Double,
    val comidasCompletadasHoy: Int,
    val metaComidasTotales: Int,
    val porcentajeNutricion: Double
) {
    companion object {
        fun fromDomain(d: ProgresoDiario) = ProgresoDiarioResponse(
            nombreUsuario = d.nombreUsuario,
            totalAlertasPostura = d.totalAlertasPostura,
            estadoPostura = d.estadoPostura,
            rachaDias = d.rachaDias,
            metaAguaMl = d.metaAguaMl,
            aguaConsumidaMl = d.aguaConsumidaMl,
            sugerenciaAguaPesoMl = d.sugerenciaAguaPesoMl,
            porcentajeAgua = d.porcentajeAgua,
            horasSuenoRegistradas = d.horasSuenoRegistradas,
            porcentajeSueno = d.porcentajeSueno,
            metaEjercicioKm = d.metaEjercicioKm,
            kmEjercicioRecorridos = d.kmEjercicioRecorridos,
            caloriasEjercicioQuemadas = d.caloriasEjercicioQuemadas,
            porcentajeEjercicio = d.porcentajeEjercicio,
            comidasCompletadasHoy = d.comidasCompletadasHoy,
            metaComidasTotales = d.metaComidasTotales,
            porcentajeNutricion = d.porcentajeNutricion
        )
    }
}
@Serializable
data class ProgresoDiarioErrorResponse(
    val code: String,
    val message: String
)