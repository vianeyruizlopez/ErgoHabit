package com.alilopez.modules.usuarioFinal.ergonomia.domain.model

import java.time.Instant

data class Postura(
    val idHistorial: Int? = null,
    val idUsuario: Int,
    val fecha: Instant? = null,
    val totalAlertas: Int
)