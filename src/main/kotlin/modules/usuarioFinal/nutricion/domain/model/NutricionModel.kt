package com.alilopez.modules.usuarioFinal.nutricion.domain.model

import java.time.LocalTime

data class HorarioNutricion(
    val horaDesayuno: LocalTime?,
    val horaComida: LocalTime?,
    val horaCena: LocalTime?
)
data class ProgresoNutricion(
    val realizoDesayuno: Boolean,
    val realizoComida: Boolean,
    val realizoCena: Boolean
)