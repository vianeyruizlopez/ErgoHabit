package com.alilopez.modules.usuarioFinal.progresoDiario.domain.model

data class ProgresosDiarioDB(
    val metaAgua: Int,
    val metaEjercicio: Double,
    val aguaConsumida: Int,
    val horasSueno: Double,
    val kmRecorridos: Double,
    val caloriasQuemadas: Int,
    val alertasPostura: Int,
    val diasRacha: Int,
    val comidasCompletadas: Int
)
