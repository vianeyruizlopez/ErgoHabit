package com.alilopez.modules.usuarioFinal.progresoDiario.domain.model

data class ProgresoDiario(
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
)