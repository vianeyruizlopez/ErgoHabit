package com.alilopez.modules.usuarioFinal.ejercicio.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class ProgresoEjercicio(
    val kmRecorridos: BigDecimal,
    val caloriasQuemadas: Int,
    val fecha: LocalDate
)