package com.alilopez.modules.usuarioFinal.sueno.domain.model

import java.time.LocalTime

data class HorarioSueno(
    val horaDormir: LocalTime,
    val horaDespertar: LocalTime
)

data class ProgresoSueno(
    val horasDormidas: Double,
    val despertoATiempo: Boolean
)